package wat;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;

public class WAT82 extends TestBase {

	static int status = 1;

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
	public void testLoginWATApp() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		String username="user1testautomation@gmail.com";
		String password="Neon@123";
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
	 * Method for validate alternative names tab in Author Record page
	 * 
	 * @param lastName
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"testLoginWATApp"})
	@Parameters("lastName")
	public void authorRecordPageAlternativeNameTab(String lastName) throws Exception {

		try {
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getSearchAuthClusterPage(ob).SearchAuthorCluster(lastName, test);
			pf.getAuthorRecordPage(ob).waitForAuthorRecordPage(test);
			pf.getAuthorRecordPage(ob).checkForAlternativeNames();
			pf.getAuthorRecordPage(ob).clickAlternativeNamesTab();
			test.log(LogStatus.PASS, "Alternative names tab highlighted by highlight bar when user clicked Alternative names tab");
			pf.getWatPageInstance(ob).logoutWAT();
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Alternative names tab not highlighted by highlight bar when user clicked Alternative names tab",
					"alternative_names_tab_not_highlighted");
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
