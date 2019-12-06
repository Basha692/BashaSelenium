package iam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import Authoring.LoginTR;
import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IAM031 extends TestBase {

	static int status = 1;
	static boolean fail = false;
	static boolean skip = false;
	static int time = 30;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseA26() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			facebookLogin();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Deep linking not working for Facebook login");
		}

		try {
			linkedinLogin();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Deep linking not working for linkedin login");
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void linkedinLogin() throws Exception {
		String email = "linkedinloginid@gmail.com";
		String password = "Neon@1234";
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			// Navigate to deep link account page using linkedin
			ob.navigate().to(CONFIG.getProperty("helpLink"));

			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("LI_login_button")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("LI_login_button"))).click();
			waitForElementTobeVisible(ob, By.name(OR.getProperty("LI_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("LI_email_textBox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("LI_password_textBox"))).sendKeys(password);
			ob.findElement(By.name(OR.getProperty("LI_allowAccess_button"))).click();
			waitUntilText("Send feedback to the Project Neon team", "Report a problem or submit a support request");
			String str = ob.findElement(By.cssSelector("a[class='feedback-link__anchor ng-binding']")).getText();
			logger.info("Title : " + str);
			String feedBack = ob.findElement(By.cssSelector("a[class='feedback-link__anchor']")).getText();
			logger.info("Emai Text : " + feedBack);
			try {
				Assert.assertTrue(str.contains("Send feedback to the Project Neon team")
						&& feedBack.contains("Report a problem or submit a support request"));
				test.log(LogStatus.PASS, "Deep link is working correctly for help page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Deep link is not working correctly for help page" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Deep_link_is_not_working_correctly_ for_help_page")));// screenshot
			}

			// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_ONEP_APPS_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);

			logout();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("login_banner")), 8);
			if (!checkElementPresence("login_banner")) {

				test.log(LogStatus.FAIL, "User not able to logout successfully");// extent
																					// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_user_unable_to_logout_successfully")));// screenshot
				closeBrowser();

			}

			closeBrowser();

		}

		catch (Throwable t) {

			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();

		}

	}

	private void facebookLogin() throws Exception {

		WebDriver driver = null;
		String email = "test_apvhlfj_user@tfbnw.net";
		String password = "auth123*";
		try {

			test.log(LogStatus.INFO, this.getClass().getSimpleName()
					+ "  UnSupported HTML Tags execution starts for data set #" + (count + 1) + "--->");

			driver = LoginTR.launchBrowser();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.navigate().to(CONFIG.getProperty("helpLink"));

			pf.getLoginTRInstance(driver).loginWithFBCredentials(driver, email, password);
			waitForPageLoad(driver);
			String str = driver.findElement(By.cssSelector("a[class='feedback-link__anchor ng-binding']")).getText();
			logger.info("Title : " + str);
			String feedBack = driver.findElement(By.cssSelector("a[class='feedback-link__anchor']")).getText();
			logger.info("Emai Text : " + feedBack);
			try {
				Assert.assertTrue(str.contains("Send feedback to the Project Neon team")
						&& feedBack.contains("Report a problem or submit a support request"));
				test.log(LogStatus.PASS, "Deep link is working correctly for help page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Deep link is not working correctly for help page" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Deep_link_is_not_working_correctly_ for_help_page")));// screenshot
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "UnExpected Error");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Article_Search_not_happening")));
			// closeBrowser();
		} finally {
			if (driver != null)
				driver.quit();
			reportDataSetResult();
			++count;
		}

	}

	public void reportDataSetResult() {
		/*
		 * if(skip) { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2, "SKIP"); }
		 * else if(fail) { status=2; TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "FAIL"); } else { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "PASS"); }
		 */

		skip = false;
		fail = false;

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
