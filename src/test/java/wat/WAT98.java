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
 * Class for Verify that in the author search result page, user should be provided with REFINE capability.
 * 
 * @author UC225218
 *
 */

public class WAT98 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";
	static String Search_result_static_text = "The following author records match your search. Author records are algorithmically generated and may not be complete, or an individual author may be represented by multiple records.\n"

			+ "Select results to combine them into a single author record. Tell us what you think.";

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
	 * Method to Verify that in the author search result page, user should be provided with REFINE capability.
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 * 
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2","OrgName1","OrgName2" })
	public void testFilterInAuthorSearchResultPage(String LastName, String CountryName1,String CountryName2, String OrgName1,String OrgName2) throws Exception {
		try {
			pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1,CountryName2, OrgName1,OrgName2, test);
			pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
			
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_NAME_XPATH).isDisplayed(), "Author filter name not displayed in Author search results page");
			test.log(LogStatus.PASS, "Author filter name displayed in Author search results page");
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_NAME_XPATH).isDisplayed(), "Author Name filter not displayed in Author search results page");
			test.log(LogStatus.PASS, "Author Name filter displayed in Author search results page");
			
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_ORG_XPATH).isDisplayed(), "Org filter name not displayed in Author search results page");
			test.log(LogStatus.PASS, "Org filter name displayed in Author search results page");
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_ORG_XPATH).isDisplayed(), "Org filter not displayed in Author search results page");
			test.log(LogStatus.PASS, "Org filter displayed in Author search results page");
			
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_SUBCAT_XPATH).isDisplayed(), "Subject categories filter name not displayed in Author search results page");
			test.log(LogStatus.PASS, "Subject categories filter name displayed in Author search results page");
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_SUBCAT_XPATH).isDisplayed(), "Subject categories filter not displayed in Author search results page");
			test.log(LogStatus.PASS, "Subject categories filter displayed in Author search results page");
			
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "Author Name filter displayed in Author search results page");
				logFailureDetails(test, e, "Author Name filter displayed in Author search results page", "Name_filter_fail");
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
