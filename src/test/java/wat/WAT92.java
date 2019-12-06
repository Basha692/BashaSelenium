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
 * Class for Verify that User must be navigated back to the search results page and the original search results must be displayed when the "Search results" tab is clicked.
 * 
 * @author UC225218
 *
 */
public class WAT92 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";

	/**
	 * Method to displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WAT");
	}

	/**
	 * Method for login into WAT application using TR ID
	 * 
	 * @throws Exception, When WAT Login is not done
	 */
	@Test
	@Parameters({"username", "password"})
	public void testLoginWATApp(String username,
			String password) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			test.log(LogStatus.INFO, "Logging into WAT Applicaton using valid WAT Entitled user ");
			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			pf.getLoginTRInstance(ob).loginToWAT(username, password, test);
			pf.getSearchAuthClusterPage(ob).validateAuthorSearchPage(test);

		} catch (Throwable t) {
			logFailureDetails(test, t, "Login Fail", "login_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * Method for Navigate to Author Record page from Search page
	 * 
	 * @param orcid
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"testLoginWATApp"})
	@Parameters("LastName")
	public void ORCIDSearchToAuthorRecordPage(String LastName) throws Exception {

		try {
			pf.getSearchAuthClusterPage(ob).SearchAuthorCluster(LastName, test);
			test.log(LogStatus.PASS, "user is successfully navigated to Author record page from Search/Search Results page");
		} catch (Throwable t) {
			logFailureDetails(test, t, "Navigation is fail from Search/Search Results page Author Record page","author_record_navigation_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
	}
	
	
	/**
	 * Method for Navigate from Author Record page to Search page
	 * 
	 * @param orcid
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"ORCIDSearchToAuthorRecordPage"})
	@Parameters("LastName")
	public void navigateBackAuthorRecordToSearchPage(String LastName) throws Exception {

		try {
			test.log(LogStatus.INFO, "Click Search Results tab");
			pf.getAuthorRecordPage(ob).clickSearchTab(test);
			// Verify whether control is back in Author Search page
			Assert.assertEquals(
					pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
					wos_title, "Control is not back in WOS Author Search page");
					test.log(LogStatus.INFO, "Control is back in WOS Author Search page");
			test.log(LogStatus.PASS, "user is successfully navigated to Author Search page after clicking search link");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Navigation is fail from Search Results page","author_Search_navigation_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */

	}

}
