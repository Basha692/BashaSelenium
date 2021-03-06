package Profile;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import util.ErrorUtil;
import util.ExtentManager;

public class Profile60 extends OnboardingModalsTest {

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
	@Parameters({"username", "password"})
	public void testLoginTRAccount(String username,
			String password) throws Exception {

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
			ob.navigate().to(System.getProperty("host"));
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			test.log(LogStatus.INFO, "Login to Neon Application");
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getOnboardingModalsPageInstance(ob).clickLogin();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Login_not_done");
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
	 * Method for validate Onboarding Modals displayed for first time users
	 * 
	 * @throws Exception, When Onboarding Modals are not displayed for first time users
	 */
	@Test(dependsOnMethods = "testLoginTRAccount")
	public void closeWelcomeOnboardingModalsForFirstTimeUser() throws Exception {
		try {
			test.log(LogStatus.INFO, "Close Onboarding welcome modals for first time users");
			pf.getOnboardingModalsPageInstance(ob).welcomeOnboardingModalClose();
			pf.getLoginTRInstance(ob).logOutApp();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "welcome onboarding modal not closed");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "onboarding_not_done")));// screenshot
			closeBrowser();
		}
	}

	/**
	 * Method for validate Onboarding Modals not displayed for second time logged users
	 * 
	 * @throws Exception, When Onboarding Modals are displayed for second time users
	 */
	@Test(dependsOnMethods = "closeWelcomeOnboardingModalsForFirstTimeUser")
	@Parameters({"username", "password"})
	public void validateOnboardingModalsForSecondTimeUser(String username,
			String password) throws Exception {
		try {
			test.log(LogStatus.INFO,
					"Validate Onboarding modals for Second time users and verify onboarding modals should display if not onboarded successfully first time");
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			test.log(LogStatus.INFO, "Login to Neon Application Same user with second time");
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getOnboardingModalsPageInstance(ob).clickLogin();
			test.log(LogStatus.INFO, "Onboarding modals should be present for second time logged user since he is not onboarded succesfully");
			pf.getOnboardingModalsPageInstance(ob).validateOnboardingModalsForSecondTimeUser();
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "onboarding modals are present for second time logged user");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "onboarding_for_second_time_user")));// screenshot
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
