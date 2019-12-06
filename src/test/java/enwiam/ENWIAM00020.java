package enwiam;

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

public class ENWIAM00020 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception
	 *             , When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcase20() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		/*try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("ENWIAM00020User"));
			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
				throw new Exception("Delete API Call failed");
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}*/

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			// Activating the Steam account
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("ENWIAM00020User"),
					LOGIN.getProperty("ENWIAM00020UserPWD"));
			pf.getLoginTRInstance(ob).clickLogin();

			String firstAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
			test.log(LogStatus.INFO, "Steam account profile name: " + firstAccountProfileName);
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getHFPageInstance(ob).clickOnAccountLink();
			String accountType = "Neon";

			validateNeonAccount(1, accountType);
			pf.getLoginTRInstance(ob).logOutApp();
			BrowserWaits.waitTime(5);

			try {
				pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("ENWIAM00020User"),
						LOGIN.getProperty("ENWIAM00020UserPWD"));
				test.log(LogStatus.PASS, "user has logged in with social account");
				pf.getENWReferencePageInstance(ob).didYouKnow(LOGIN.getProperty("ENWIAM00020UserPWD"));
				test.log(LogStatus.PASS, "user is able to link");
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_HEADER_NEWSFEED_CSS);
				pf.getHFPageInstance(ob).clickOnAccountLink();
				accountType = "Neon";

				try {
					validateLinkedAccounts(2, accountType);
					String secondAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
					test.log(LogStatus.INFO, "After merging Social account profile name: " + secondAccountProfileName);
					Assert.assertEquals(secondAccountProfileName, firstAccountProfileName);
					test.log(LogStatus.PASS, " Merging is happened");
					if (secondAccountProfileName.contains(firstAccountProfileName)) {
						test.log(LogStatus.PASS, "Winning account is Steam");
					}
					pf.getHFPageInstance(ob).clickProfileImage();
					pf.getHFPageInstance(ob).clickProfileImage();
					waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEON_OK_BUTTON_XPATH.toString()), 30);
					ob.findElement(By.xpath(OnePObjectMap.NEON_OK_BUTTON_XPATH.toString())).click();

					BrowserWaits.waitTime(4);

					pf.getBrowserActionInstance(ob)
							.scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS);

				}

				catch (Throwable t) {

					test.log(LogStatus.FAIL, "Forward Merge is not happened");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
							.getClass().getSimpleName()
							+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
					ErrorUtil.addVerificationFailure(t);

				}

			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "User is not able to link");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);

			}

		} catch (Throwable t) {
			closeBrowser();
			t.printStackTrace();
			test.log(LogStatus.FAIL, "User is not able to skip linking");
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_skip_link")));

		}
		logout();
		closeBrowser();

	}

	private void validateNeonAccount(int accountCount, String linkName) throws Exception {
		try {
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("sru_steam10")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Single Steam account is available and is not linked to Social account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Linked accounts are available in accounts page");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}

	/*private void validateAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("sru_fbusername10")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Single Social account is available and is not linked to Steam account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Linked accounts are available in accounts page : Neon and " + linkName + " accounts");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}*/

	private void validateLinkedAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(pf.getAccountPageInstance(ob).verifyLinkedAccount("Facebook",
					LOGIN.getProperty("sru_fbusername10")));
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("sru_steam10")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS,
					"Linked accounts are available in accounts page : Facebook and " + linkName + " accounts");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Linked accounts are not available in accounts page : Facebook and " + linkName + " accounts");
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
