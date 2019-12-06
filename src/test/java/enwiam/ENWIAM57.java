package enwiam;

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

public class ENWIAM57 extends TestBase {

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
	public void testcaseh17() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("UserFBENWIAM80"));

			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
				throw new Exception("Delete API Call failed");
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			// Activating the facebook account

			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserFBENWIAM80"),
					LOGIN.getProperty("PWDUserFBENWIAM80"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getLoginTRInstance(ob).closeOnBoardingModal();
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			String firstAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
			test.log(LogStatus.INFO, "Social account profile name: " + firstAccountProfileName);
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getHFPageInstance(ob).clickOnAccountLink();

			String accountType = "Facebook";

			validateAccounts(1, accountType);
			pf.getLoginTRInstance(ob).logOutApp();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);

			try {
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("UserFBENWIAM80"),
						LOGIN.getProperty("PWDUserFBENWIAM80"));
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
				//BrowserWaits.waitTime(8);
				pf.getLinkingModalsInstance(ob).clickOnNotNowButton();
				BrowserWaits.waitTime(8);
				pf.getLoginTRInstance(ob).closeOnBoardingModal();
				
				
				//pf.getBrowserWaitsInstance(ob)
					//	.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
				test.log(LogStatus.PASS, "Avoiding the Linking is happened");
			//	pf.getLoginTRInstance(ob).closeOnBoardingModal();
				String secondAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
				test.log(LogStatus.INFO, "Steam account profile name: " + secondAccountProfileName);
				pf.getHFPageInstance(ob).clickProfileImage();
				pf.getHFPageInstance(ob).clickOnAccountLink();
				accountType = "Change password";

				validateNeonAccount(1, accountType);

				int watchlistCount = 1;
				try {

					for (int j = 1; j <= watchlistCount; j++) {
						logger.info("Creating " + j + " Watchlist");
						pf.getLinkingModalsInstance(ob).toMakeAccountNeonActive();
					}
					test.log(LogStatus.PASS, "Made Steam account Neon Active");

				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Unable to create 1 watchlists");// extent
					ErrorUtil.addVerificationFailure(t);

				}
				pf.getLoginTRInstance(ob).logOutApp();
				//BrowserWaits.waitTime(5);
				// Trying to Link the accounts
				try {
                    
					pf.getLinkingModalsInstance(ob).clickOnSignInWithFB();
					pf.getENWReferencePageInstance(ob).didYouKnow(LOGIN.getProperty("PWDUserFBENWIAM80"));

					test.log(LogStatus.PASS, "Linking is happened");
					pf.getLoginTRInstance(ob).closeOnBoardingModal();
					pf.getBrowserWaitsInstance(ob)
							.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
					pf.getHFPageInstance(ob).clickOnAccountLink();
					accountType = "Change password";

					try {
						// validating two accounts are linked or not
						validateLinkedAccounts(2, accountType);
						String winingAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
						test.log(LogStatus.INFO, "After merging account profile name: " + winingAccountProfileName);

						// Verifying that Profile name is same as winning
						// account after merging
						Assert.assertEquals(winingAccountProfileName, secondAccountProfileName);
						test.log(LogStatus.PASS, "Automated Merge is happened");

						if (winingAccountProfileName.contains(secondAccountProfileName)) {
							test.log(LogStatus.PASS, "Winning account is Steam account");
						} else
							throw new Exception("Winning account is cannot be determined");

						pf.getHFPageInstance(ob).clickProfileImage();
						pf.getHFPageInstance(ob).clickProfileImage();
						// pf.getProfilePageInstance(ob).clickProfileLink();
						waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEON_OK_BUTTON_XPATH.toString()), 30);
						ob.findElement(By.xpath(OnePObjectMap.NEON_OK_BUTTON_XPATH.toString())).click();

						//BrowserWaits.waitTime(4);

						pf.getBrowserActionInstance(ob)
								.scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS);
						int WinningAccount_WatclistCount = pf.getLinkingModalsInstance(ob).getWatchlistCount();
						System.out.println(WinningAccount_WatclistCount);
						if (WinningAccount_WatclistCount == watchlistCount) {
							test.log(LogStatus.PASS,
									"User is able to see the same watchlist count in profile page in Social account as in Neon");
						}

						else {

							test.log(LogStatus.FAIL,
									"User is not able to see the same watchlist count in Social account");
						}
					}

					catch (Throwable t) {

						test.log(LogStatus.FAIL, "Automated Merge is not happened");// extent
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
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
							.getClass().getSimpleName()
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
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_skip_link")));

			}
			logout();
			closeBrowser();

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

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}

	private void validateNeonAccount(int accountCount, String linkName) throws Exception {
		try {
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserFBENWIAM80")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Single Steam account is available and is not linked to Social account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Linked accounts are available in accounts page");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}

	private void validateAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserFBENWIAM80")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Single Social account is available and is not linked to Steam account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Linked accounts are available in accounts page : Neon and " + linkName + " accounts");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}

	private void validateLinkedAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(pf.getAccountPageInstance(ob).verifyLinkedAccount("Facebook",
					LOGIN.getProperty("UserFBENWIAM80")));
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserFBENWIAM80")));
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
