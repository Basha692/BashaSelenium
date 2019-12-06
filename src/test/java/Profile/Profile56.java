package Profile;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import util.ErrorUtil;
import util.ExtentManager;

public class Profile56 extends OnboardingModalsTest {

	static int status = 1;
	String[] tests;
	String[] tests_dec;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		String var = rowData.getTestcaseId();
		String dec = rowData.getTestcaseDescription();
		tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
		tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
		test = extent.startTest(tests[0], tests_dec[0]).assignCategory("Profile");
		test.log(LogStatus.INFO, tests[0]);
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
			logger.info("Test --" + suiteRunmode + "--" + testRunmode);
			if (!master_condition) {
				status = 3;// excel
				extent = ExtentManager.getReporter(filePath);

				logger.info(rowData.getTestcaseId());
				for (int i = 0; i < tests.length; i++) {
					logger.info(tests_dec[i]);
					test = extent.startTest(tests[i], tests_dec[i]).assignCategory("Profile");
					test.log(LogStatus.SKIP,
							"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
					extent.endTest(test);
				}
				throw new SkipException(
						"Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
			}
		}

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
	 * Method for Validate Welcome Modal Features
	 * 
	 * @throws Exception, When Welcome modal doesnot exist
	 */
	@Test(dependsOnMethods = "testLoginTRAccount")
	public void validateWelcomeOnboardingModal() throws Exception {
		try {
			test = extent.startTest(tests[0], tests_dec[0]).assignCategory("Profile");
			test.log(LogStatus.INFO, tests_dec[0]);
			pf.getOnboardingModalsPageInstance(ob).validateWelcomeOnboardingModal();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Welcome onboarding not done");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "onboarding_not_done")));// screenshot
			// closeBrowser();
		} finally {
			extent.endTest(test);
		}

		try {
			test = extent.startTest(tests[1], tests_dec[1]).assignCategory("Profile");
			test.log(LogStatus.INFO, tests_dec[1]);
			pf.getOnboardingModalsPageInstance(ob).validateWelcomeOnboardingModal();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Welcome onboarding not done");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "onboarding_not_done")));// screenshot
			// closeBrowser();
		} finally {
			extent.endTest(test);
		}

		try {
			test = extent.startTest(tests[2], tests_dec[2]).assignCategory("Profile");
			test.log(LogStatus.INFO, tests_dec[2]);
			pf.getOnboardingModalsPageInstance(ob).validateWelcomeOnboardingModalRecommendedPeople();
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Welcome onboarding not done");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "onboarding_not_done")));// screenshot
			closeBrowser();
		} finally {
			extent.endTest(test);
		}

	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 * 
	 * @throws Exception
	 */
	@AfterTest
	public void reportTestResult() throws Exception {
		// extent.endTest(test);
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
