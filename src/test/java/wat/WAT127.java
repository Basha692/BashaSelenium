package wat;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for Verify that user is able to see the selections they made in the
 * Journal filter in Author search results page
 * 
 * @author UC225218
 *
 */
public class WAT127 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WAT");
	}

	/**
	 * Method for login into WAT application using Steam ID
	 * 
	 * @throws Exception,
	 *             When WAT Login is not done
	 */
	@Test
	@Parameters({ "username", "password" })
	public void testLoginWATApp(String username, String password) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts.... ");

		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			test.log(LogStatus.INFO, "Login to WAT Applicaton using valid WAT Entitled user ");
			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			pf.getLoginTRInstance(ob).loginToWAT(username, password, test);

		} catch (Throwable t) {
			logFailureDetails(test, t, "Login Fail", "login_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * Method to Verify that user is able to see the selections they made in the
	 * Journal filter in Author search results page
	 * 
	 * @param LastName,
	 *            FirstName, CountryName, OrgName
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2", "OrgName1", "OrgName2" })
	public void testJournalFilter(String LastName, String CountryName1, String CountryName2,
			String OrgName1, String OrgName2) throws Exception {

		try {
			pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1, CountryName2,
					OrgName1, OrgName2, test);
			test.log(LogStatus.INFO, "Verifying whether Journal filter exists and options also exists");
			if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_ORG_XPATH)
					.isDisplayed()
					&& pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_ORG_XPATH).isDisplayed()) {
				test.log(LogStatus.INFO, "Clicking first option in Journal filter");
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_ORG_XPATH);
				test.log(LogStatus.INFO, "Waiting for divider line to appear");
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORGANIZATION_DIVIDER_XPATH);
				test.log(LogStatus.INFO, "Clicking second option in Journal filter");
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_2nd_ORG_XPATH);
				Assert.assertTrue(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORGANIZATION_DIVIDER_XPATH)
								.isDisplayed(),
						"Divider line for Journal seperating filtered and unfiltered names is not displayed");
				test.log(LogStatus.PASS,
						"Divider line displayed and successfully seperates filtered and unfiltered Journal in the Journal filter in Author search results page");
				pf.getBrowserActionInstance(ob).closeBrowser();
			} else {
				throw new Exception(
						"Divider line for Journal seperating filtered and unfiltered names is not displayed");
			}

		} catch (Throwable t) {
			logFailureDetails(test, t, "Divider line for Journal is not displayed", "Filter_line_issue");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL
	 * or SKIP
	 */
	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()),
		 * "SKIP");
		 */

	}
}