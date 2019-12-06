package enw;

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

public class ENW00029 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}

	@Test
	public void testcaseENW00029() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);

				pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserName18"),
						LOGIN.getProperty("Password18"));

				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
	
				pf.getHFPageInstance(ob).clickProfileImage();
				waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK_CSS.toString()), 180);
				ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK_CSS.toString())).click();

				String actualEmail = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_ACTUAL_EMAIL_XPATH.toString()))
						.getText();
				try {
					Assert.assertEquals(LOGIN.getProperty("UserName18"), actualEmail);
					test.log(LogStatus.PASS, "First  Email id getting displayed in Account Setting page ");
				}
				catch (Throwable t) {

					test.log(LogStatus.FAIL, "Email id getting displayed in Account Setting page is incorrect");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
							.getClass().getSimpleName()
							+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
					ErrorUtil.addVerificationFailure(t);
				}

				String accountType = "Facebook";
				validateSocialAccounts(2, accountType);
				String actualEmail1 = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_ACTUAL_EMAIL1_XPATH.toString())).getText();
				try {
					Assert.assertEquals(actualEmail1,LOGIN.getProperty("UserName18"));
					test.log(LogStatus.PASS, "alternate Email id is displayed in Account Setting page.");
				}

				catch (Throwable t) {

					test.log(LogStatus.FAIL, "alternate Email id is not displayed in Account Setting page.");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
							.getClass().getSimpleName()
							+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
					ErrorUtil.addVerificationFailure(t);
				}

			String accountpageText = ob.findElement(By.xpath(OnePObjectMap.TEXT_ACCOUNTPAGE1_XPATH.toString())).getText();
			
			try {
				Assert.assertEquals(accountpageText,
						"Project Neon has linked your accounts. You can sign in with any of the accounts you already use.");
				test.log(LogStatus.PASS,
						"Message 'Project Neon has linked your accounts. You can sign ' is dispalyed correctly in account setting page");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Message 'Project Neon supports linking your accounts - ' is not displayed correctly in account setting page");// extent
				// reports
				status = 2;// excel
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

	public void validateSocialAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount("Facebook", LOGIN.getProperty("UserName18")));
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserName18")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Multiple accounts are present in account setting page");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Multiple accounts are not present in account setting page");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "Linking_failed")));// screenshot
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
