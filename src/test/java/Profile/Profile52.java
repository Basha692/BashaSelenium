package Profile;

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

public class Profile52 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Profile");
	}

	/**
	 * Method for wait TR Login Screen
	 * 
	 * @throws Exception, When TR Login screen not displayed
	 */
	@Test
	@Parameters({"socialLogin", "username", "password", "deepLinkUrl"})
	public void deepLinkValidation(String socialLogin,
			String username,
			String pwd,
			String deepLinkUrl) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

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
			String fbLILogins[] = socialLogin.split("\\|");
			for (String fbLILogin : fbLILogins) {
				String deepLink = "https://apps.dev-stable.clarivate.com/cmty"+ deepLinkUrl;
				logger.info("Profile deep link URL-->" + deepLink);
				ob.navigate().to(deepLink);

				if (fbLILogin.equalsIgnoreCase("FB")) {
					test.log(LogStatus.INFO, "Validate own Profile Deep link using FB");
					pf.getLoginTRInstance(ob).loginWithFBCredentials(username, pwd);
					pf.getLoginTRInstance(ob).clickNotnowLink();
					validateProfileDeepLink();
				} else if (fbLILogin.equalsIgnoreCase("LI")) {
					test.log(LogStatus.INFO, "Validate own Profile Deep link using LI");
					pf.getLoginTRInstance(ob).loginWithLinkedInCredentials("testing.prasadreddy@gmail.com", "Apple@123");
					pf.getLoginTRInstance(ob).clickNotnowLink();
					validateProfileDeepLink();
				}
			}
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something Unexpected");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
			closeBrowser();
		}
	}

	/**
	 * Method for validate profile Deep link functionality
	 * 
	 * @throws Exception, When Profile deep linking not working
	 */
	public void validateProfileDeepLink() throws Exception {
		try {
			test.log(LogStatus.INFO, "Profile page is opened and Validate profile fields");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Posts", "Comments", "Followers", "Following", "Watchlists");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Activity", "Interests and Skills");
			pf.getLoginTRInstance(ob).logOutApp();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Own Profile Deeplink not working");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "Own Profile Deeplink not working")));// screenshot
			closeBrowser();
		}
	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 * 
	 * @throws Exception
	 */
	@AfterTest
	public void reportTestResult() throws Exception {
		extent.endTest(test);
		/*
		 * if(status==1) TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
