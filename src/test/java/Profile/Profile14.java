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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class Profile14 extends TestBase {

	static int status = 1;
	static int followersCount;

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

	@Test
	@Parameters({"username", "password"})
	public void testLoginTRAccount(String username,
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

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts");

		try {
			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(System.getProperty("host"));
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Login_not_done");// extent reports
			// print stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Login is not done")));
			closeBrowser();
		}
	}

	@Test(dependsOnMethods = "testLoginTRAccount")
	public void getFollowers() throws Exception {
		try {
			test.log(LogStatus.INFO, "get users who are following me-My Followers");
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			followersCount = pf.getProfilePageInstance(ob).getFollowersCount();
			logger.info("Before followers count-->"+followersCount);
			test.log(LogStatus.INFO, "Logout from the application");
			pf.getLoginTRInstance(ob).logOutApp();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something Unexpected");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_followers_count")));
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "getFollowers")
	@Parameters({"otherUsername", "otherUserPassword"})
	public void loginwithOtherUser(String otherUsername,
			String otherUserPassword) throws Exception {
		try {
			test.log(LogStatus.INFO, "login with different user");
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(otherUsername, otherUserPassword);
			pf.getLoginTRInstance(ob).clickLogin();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "login_not_done");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "loginwithOtherUser")
	public void followUserAndLogout() throws Exception {
		try {
			test.log(LogStatus.INFO, "Follow/unfollow other user");
			ob.navigate().to("https://apps.dev-stable.clarivate.com/cmty" + "/#/profile/59ddd666-fc1c-4e22-a26b-952bfe11bb96");
			//BrowserWaits.waitTime(10);
			pf.getProfilePageInstance(ob).followOtherProfileFromProfilePage();
			test.log(LogStatus.INFO, "Logout from the application and login with tested user");
			pf.getLoginTRInstance(ob).logOutApp();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something Unexpected");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_followers_logout")));
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "followUserAndLogout")
	@Parameters({"username", "password"})
	public void validateFollowersCount(String username,
			String password) throws Exception {
		try {
			test.log(LogStatus.INFO, "login with user");
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
			test.log(LogStatus.INFO, "Go to his own profile page");
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			test.log(LogStatus.INFO, "validate profile followers count, should increase or decrease");
			pf.getProfilePageInstance(ob).validateFollowersCount(followersCount);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "others are unable to following me");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_followers_validation_count")));
			closeBrowser();
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
