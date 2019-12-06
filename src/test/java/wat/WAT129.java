package wat;

import java.util.List;

import org.openqa.selenium.WebElement;
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
 * Class for Verify that selected options should stay visible at the top of each
 * filter
 * 
 * @author UC225218
 *
 */
public class WAT129 extends TestBase {

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
	 * Method to Verify that selected options should stay visible at the top of
	 * each filter
	 * 
	 * @param LastName,
	 *            FirstName, CountryName, OrgName
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2", "OrgName1", "OrgName2" })
	public void testAuthorNameFilterSelectionLocation(String LastName, String CountryName1, String CountryName2,
			String OrgName1, String OrgName2) throws Exception {

		try {
			pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1, CountryName2,
					OrgName1, OrgName2, test);
			test.log(LogStatus.INFO, "Verifying whether Author name filter exists and options also exists");
			if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_NAME_XPATH)
					.isDisplayed()
					&& pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_NAME_XPATH).isDisplayed()) {
				test.log(LogStatus.INFO, "Clicking first option in author name filter");
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_NAME_XPATH);
				test.log(LogStatus.INFO, "Waiting for divider line to appear");
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_NAME_DIVIDER_XPATH);
				String SelectedAuthorName = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_NAME_TEXT_XPATH).getText();
				List<WebElement> UnselectedAuthorNames = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_NAME_XPATH);
				test.log(LogStatus.INFO,
						"Looping across author name filter options to check wheather already selected name appears again in the unselected list of options");
				for (WebElement x : UnselectedAuthorNames) {
					if (x.getText().equals(SelectedAuthorName))
						throw new Exception("Filtered Author name is also present in Unfiltered list");
				}
				test.log(LogStatus.PASS,
						"Already selected Author name do not appear again in the unselected list of options");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_SELECTED_AUTHOR_NAME_XPATH).getText(),
						pf.getBrowserActionInstance(ob)
								.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_NAME_TEXT_XPATH).getText(),
						"User filtered Author name and Name appearing above the autor name filter divider are not same.");
				test.log(LogStatus.PASS,
						"User filtered Author name and Name appearing above the autor name filter divider are same.");
				pf.getBrowserActionInstance(ob).closeBrowser();
			} else {
				throw new Exception(
						"Divider line for Author name seperating filtered and unfiltered names is not displayed");
			}

		} catch (Throwable t) {
			logFailureDetails(test, t,
					"User filtered Author name and Name appearing above the autor name filter divider are not same.",
					"Author_Name_Filter_Selection_Location_issue");
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