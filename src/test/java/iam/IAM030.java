package iam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

public class IAM030 extends TestBase {

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
			ob.navigate().to(CONFIG.getProperty("accountLink"));
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("LI_login_button")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("LI_login_button"))).click();

			waitForElementTobeVisible(ob, By.name(OR.getProperty("LI_email_textBox")), 30);

			// Verify that existing LI user credentials are working fine
			ob.findElement(By.name(OR.getProperty("LI_email_textBox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("LI_password_textBox"))).sendKeys(password);
			ob.findElement(By.name(OR.getProperty("LI_allowAccess_button"))).click();
			waitUntilText("Account");
			String str = ob.findElement(By.cssSelector("h2[class='wui-title']")).getText();
			logger.info("Title : " + str);
			String emailName = ob.findElement(By.cssSelector("div[class='account-option-item__text-container'] span")).getText();
			logger.info("Emai Text : " + emailName);
			String additionalMail = ob.findElement(By.cssSelector("a[class='wui-btn wui-btn--secondary']")).getText();
			logger.info("Additional Email Link Text : " + additionalMail);
			try {
				Assert.assertTrue(str.contains("Account") && emailName.contains(email)
						&& additionalMail.contains("View additional email preferences"));
				test.log(LogStatus.PASS, "Deep link is working correctly for account page using linkedin");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Deep link is not working correctly for account page" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Deep_link_is_not_working_correctly_ for_ account_page")));// screenshot
			}

			//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_ONEP_APPS_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			logout();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.NEON_LANDING_PAGE_LOGGIN_BANNER_CSS);
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
			driver.navigate().to(CONFIG.getProperty("accountLink"));

			pf.getLoginTRInstance(driver).loginWithFBCredentials(driver, email, password);
			waitForPageLoad(driver);

			String str = driver.findElement(By.cssSelector("h2[class='wui-title']")).getText();
			logger.info("Title : " + str);
			String emailName = driver.findElement(By.cssSelector("div[class='account-option-item__text-container'] span")).getText();
			logger.info("Emai Text: " + emailName);
			String additionalMail = driver.findElement(By.cssSelector("a[class='wui-btn wui-btn--secondary']"))
					.getText();
			logger.info("Additional Email Link Text : " + additionalMail);
			try {
				Assert.assertTrue(str.contains("Account") && emailName.contains(email)
						&& additionalMail.contains("View additional email preferences"));
				test.log(LogStatus.PASS, "Deep link is working correctly for account page using FB");
				jsClick(driver, driver
						.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
				new WebDriverWait(driver, time)
						.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign out")));
				jsClick(driver, driver
						.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString())));


			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Deep link is not working correctly for account page using FB" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "Deep_link_is_not_working_correctly_ for_ account_page_using_FB")));// screenshot
				closeBrowser();
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
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