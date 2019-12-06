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

public class Profile62 extends OnboardingModalsTest {

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
	public void testLoginEndNoteApplication(String username,
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
			// Appednd ENW url to neon stable
			ob.navigate().to(System.getProperty("host") + CONFIG.getProperty("appendENWAppUrl"));
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			test.log(LogStatus.INFO, "Login to ENW Application");
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(username, password);
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
	 * Method
	 * 
	 * @throws Exception,
	 */
	@Test(dependsOnMethods = "testLoginEndNoteApplication")
	@Parameters("enwProfileFlyout")
	public void validateENWProfileFlyout(String enwProfileFlyout) throws Exception {
		try {
			test.log(LogStatus.INFO, "Validate ENW page for Market Test user");
			pf.getOnboardingModalsPageInstance(ob).ENWToNeonNavigationScreenForMarketTestUser();
			test.log(LogStatus.INFO, "Validate ENW Profile flyout");
			pf.getOnboardingModalsPageInstance(ob).validateENWProfileFlyout(enwProfileFlyout);

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "ENW Profile flyout not displayed");
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
	 * Method
	 * 
	 * @throws Exception,
	 */
	@Test(dependsOnMethods = "validateENWProfileFlyout")
	public void validateENWToNeonUsingProfileLink() throws Exception {
		try {
			test.log(LogStatus.INFO,
					"Navigate to Neon application from EndNoteWeb using Profile link for First time users");
			test.log(LogStatus.INFO, "Verify onboarding modals are dispalyed first time users");
			pf.getOnboardingModalsPageInstance(ob).validateENWToNeonOnboardingModalsUsingProfileLink();
			test.log(LogStatus.INFO, "logout from the Application");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "ENW to Neon navigation using Profile link not done");
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
