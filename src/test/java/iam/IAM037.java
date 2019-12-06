package iam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IAM037 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");

	}

	@Test
	public void testcaseENW000012() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			String password = "Neon@123";
			String first_name = "duster";
			String last_name = "man";

			String statuCode = deleteUserAccounts(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			logger.info("User Status : " + statuCode);
			if (statuCode.equalsIgnoreCase("200")) {
				logger.info("User Deleted Successfully");
			} else if (statuCode.equalsIgnoreCase("400")) {
				logger.info("User Already Deleted Successfully");
			} else {
				test.log(LogStatus.FAIL, "Bad request Error..Server down");
			}

			openBrowser();
			clearCookies();
			maximizeWindow();
			String email = createNewUser(first_name, last_name);
			logger.info("Email Address : " + email);
			logout();

			ob.navigate().to(host);

			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("LI_login_button")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("LI_login_button"))).click();
			waitForElementTobeVisible(ob, By.name(OR.getProperty("LI_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("LI_email_textBox"))).sendKeys(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			ob.findElement(By.name(OR.getProperty("LI_password_textBox")))
					.sendKeys(LOGIN.getProperty("LINKINGANDMERGINGPASS"));
			ob.findElement(By.name(OR.getProperty("LI_allowAccess_button"))).click();
			pf.getLoginTRInstance(ob).closeOnBoardingModal();
			pf.getLoginTRInstance(ob).clickNotnowLink();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString())).click();
			waitUntilText("Account");
			String actualEmail = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_PAGE_LINKEDIN_MAIL_XPATH.toString()))
					.getText();
			System.out.println(actualEmail);

			try {
				Assert.assertEquals(LOGIN.getProperty("LINKINGANDMERGINGNAME"), actualEmail);
				test.log(LogStatus.PASS, " Email id getting displayed in Account Setting page is correct");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Email id getting displayed in Account Setting page is incorrect");// extent
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}
			waitUntilText("Link accounts");
			String linkText = ob.findElement(By.xpath(OnePObjectMap.LINK_ACCOUNT_BUTTON_ACCOUNT_PAGE_XPATH.toString()))
					.getText();
			logger.info("" + linkText);
			try {
				Assert.assertEquals(linkText, "Link accounts");
				test.log(LogStatus.PASS, "Link button is displayed in Account setting page.");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Link button is not displayed in Account setting page.");// extent
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}

			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.LINK_ACCOUNT_BUTTON_ACCOUNT_PAGE_XPATH.toString()),
					30);
			ob.findElement(By.xpath(OnePObjectMap.LINK_ACCOUNT_BUTTON_ACCOUNT_PAGE_XPATH.toString())).click();
			waitUntilText("Link accounts");
			waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.LINK_ACCOUNT_BUTTON_ACCOUNT_PAGE_XPATH.toString()),
					30);
			waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_PASSWORD_XPATH.toString()),
					30);
			ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_EMAIL_XPATH.toString())).sendKeys(email);
			ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_PASSWORD_XPATH.toString())).sendKeys(password);
			ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_DONE_BUTTON_XPATH.toString())).click();

			waitUntilText("Account");
			String actualEmail1 = ob.findElement(By.xpath(OnePObjectMap.LINKED_STEAM_ACCOUNT_XPATH.toString()))
					.getText();

			logger.info("linked steam account : " + actualEmail1);
			try {
				Assert.assertEquals(email, actualEmail1);
				test.log(LogStatus.PASS, " Email id getting displayed in Account Setting page is correct");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Email id getting displayed in Account Setting page is incorrect");
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}
			logout();
			ob.close();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			ErrorUtil.addVerificationFailure(t);
			closeBrowser();

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
