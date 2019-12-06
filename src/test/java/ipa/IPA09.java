package ipa;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Class for follow/unfollow profile from search page itself
 * 
 * @author UC202376
 *
 */
public class IPA09 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPA");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception, When TR Login is not done
	 */
	@Test
	@Parameters({"username","password"})
	public void testLoginIPA(String username,String password) throws Exception {

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
			test.log(LogStatus.INFO, "Login to IPA application");
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			pf.getLoginTRInstance(ob).loginToIPA(username,password);
			test.log(LogStatus.PASS, "Login successfully");
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Error: Login not happended");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
			
			closeBrowser();
		}

	}

	
	@Test(dependsOnMethods = "testLoginIPA")
	public void technologySearch() throws Exception {
		try {
			test.log(LogStatus.INFO, "Verifying the search textbox for the Technology");
			pf.getSearchPageInstance(ob).validateTechnologySearch(test);
			pf.getSearchPageInstance(ob).validateNewSearchLandingPage(test);
			pf.getSearchPageInstance(ob).validateIPAnalyticsLandingPage(test);
			pf.getSearchPageInstance(ob).validateCompanySearch(test);
			
			closeBrowser();
			
		} catch (Throwable t ) {
			test.log(LogStatus.FAIL, "Search page validation is failed");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_add_topic_not_done")));
			closeBrowser();
		}
	}

	
	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		
		/*if (status == 1)
			TestUtil.reportDataSetResult(ipaxls, "Test Cases",
					TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "PASS");
		else if (status == 2)
			TestUtil.reportDataSetResult(ipaxls, "Test Cases",
					TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "FAIL");
		else
			TestUtil.reportDataSetResult(ipaxls, "Test Cases",
					TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "SKIP");*/
		 
	}

}
