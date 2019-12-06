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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENW000010 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}

	@Test
	public void testcaseENW000010() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			try {
				String statuCode = deleteUserAccounts(LOGIN.getProperty("UserName18"));
				String statuCode2 = deleteUserAccounts(LOGIN.getProperty("UserENWsteam000010"));
				if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
					// test.log(LogStatus.FAIL, "Delete accounts api call failed");
					throw new Exception("Delete API Call failed");
				}

				if (!(statuCode2.equalsIgnoreCase("200") || statuCode2.equalsIgnoreCase("400"))) {
					// test.log(LogStatus.FAIL, "Delete accounts api call failed");
					throw new Exception("Delete API Call failed");
				}
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
				ErrorUtil.addVerificationFailure(t);
			}
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
            
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("UserENWsteam000010"),LOGIN.getProperty("PWDuserENW000010"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeClickable(ob, By.xpath("//button[@class='wui-modal__close-btn']/i"), 60);
			ob.findElement(By.xpath("//button[@class='wui-modal__close-btn']/i")).click();
			
			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));
		

			logout();

			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserName18"),
					LOGIN.getProperty("Password18"));


			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getHFPageInstance(ob).clickOnAccountLink();
			
			// OPQA-1905
			String actualEmail = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_ACTUAL_EMAIL_XPATH.toString()))
					.getText();
			System.out.println(actualEmail);

			try {
				Assert.assertEquals(LOGIN.getProperty("UserName18"), actualEmail);
				test.log(LogStatus.PASS, " Email id getting displayed in Account Setting page is correct");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Email id getting displayed in Account Setting page is incorrect");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}
			// OPQA-1906

			String passwordText = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_TEXT_COMPARE_PASSWORD_XPATH.toString()))
					.getText();
			System.out.println(passwordText);
			try {
				Assert.assertEquals(passwordText, "Password is associated with your Facebook");
				test.log(LogStatus.PASS,
						"Message 'Password is associated with your Facebook account.' is dispalyed correctly in account setting page");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Message 'Password is associated with your Facebook' is not displayed correctly in account setting page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}
			String LInkText = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_LINK_BUTTON_XPATH.toString())).getText();
			System.out.println(LInkText);
			try {
				Assert.assertEquals(LInkText, "Link accounts");
				test.log(LogStatus.PASS, "Link button is displayed in Account setting page.");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Link button is not displayed in Account setting page.");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}
			ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_LINK_BUTTON_XPATH.toString())).click();
			BrowserWaits.waitTime(3);
			//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ACCOUNT_STEAM_EMAILID_NAME);
			ob.findElement(By.name(OnePObjectMap.ACCOUNT_STEAM_EMAILID_NAME.toString())).sendKeys(LOGIN.getProperty("UserENWsteam000010"));
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ACCOUNT_STEAM_PASSWORD_NAME);
			ob.findElement(By.name(OnePObjectMap.ACCOUNT_STEAM_PASSWORD_NAME.toString())).sendKeys(LOGIN.getProperty("PWDuserENW000010"));
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DONE_BUTTON_CLICK_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.DONE_BUTTON_CLICK_XPATH.toString())).click();
			
			String actualEmail1 = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_ACTUAL_EMAIL1_XPATH.toString()))
					.getText();

			System.out.println(actualEmail1);
			try {
				Assert.assertEquals(LOGIN.getProperty("STEAMUSEREMAIL"), actualEmail1);
				test.log(LogStatus.PASS, " Email id getting displayed in Account Setting page is correct");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Email id getting displayed in Account Setting page is incorrect");
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}

			// OPQA-1904
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ACCOUNT_fACEBOOK_ICON_XPATH.toString()), 8);
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ACCOUNT_STEAM_ICON_XPATH.toString()), 8);

			try {
				if (checkElementPresence("Facebook_icon") && checkElementPresence("Steam_icon")) {
					test.log(LogStatus.PASS, "Facebook authentication account is linked to the Neon account");
					System.out.println("pass");
				}

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Facebook authentication account is not linked to the Neon account");
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);
				System.out.println("fail");

			}
			//BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ACCOUNT_RADIOBUTTON1_XPATH.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_RADIOBUTTON1_XPATH.toString())).click();

			String TextcompareAfterLink = ob
					.findElement(By.xpath(OnePObjectMap.ACCOUNT_TEXT_COMPARE_AFTERLINK_XPATH.toString())).getText();

			System.out.println(TextcompareAfterLink);
			try {
				Assert.assertEquals(TextcompareAfterLink,
						"Project Neon has linked your accounts. You can sign in with any of the accounts you already use.");
				test.log(LogStatus.PASS,
						"Message 'Project Neon has linked your accounts.' is displayed correctly after linking from account setting page ");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Message 'Project Neon has linked your accounts.' is not displayed after linking from account setting page ");
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
