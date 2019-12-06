package wat;

import java.util.Set;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import watpages.SearchAuthorClusterResultsPage;

import base.TestBase;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for Verify that the publication list shall be sorted by Most recent (by publication year) - default.
 * 
 * @author UC225218
 *
 */

public class WAT125 extends TestBase {

	String Static_Text = "Sorted by Date (newest first)";
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
	 * Method to Verify that the publication list shall be sorted by Most recent (by publication year) - default.
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 * 
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({"lastName"})
	public void testAuthorSearchResultPageSortFunctionality(String lastName) throws Exception {
		try {
			test.log(LogStatus.INFO, "Search an Author using last name");
			pf.getSearchAuthClusterPage(ob).SearchAuthorCluster(lastName,test);
			pf.getSearchAuthClusterResultsPage(ob).waitForauthorClusterSearchResults(test);
			test.log(LogStatus.INFO, "Combine two Authors");
			pf.getSearchAuthClusterResultsPage(ob).combineAuthorCard(test);
			test.log(LogStatus.INFO, "Verify whether publications are available");
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_MAIN_CARD_XPATH).isDisplayed(), "No Publications available for the user");
			test.log(LogStatus.PASS, "publications are displayed in Author Record page");
			Assert.assertEquals(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_SORT_XPATH).getText(), Static_Text, "Sort Static text Fail");
			test.log(LogStatus.PASS, "Sorting text is displayed as expected");

		} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "Sorting text is not displayed as expected");
				logFailureDetails(test, e, "Sorting text is not displayed as expected", "Sort_fail");
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
