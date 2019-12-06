package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class ENWIAM55 extends TestBase {

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
	public void testcaseh15() throws Exception {
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
			String statuCode2 = deleteUserAccounts(LOGIN.getProperty("UserFBENWIAM80"));
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

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			// Activating the Steam account
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("UserFBENWIAM80"),
					LOGIN.getProperty("PWDUserFBENWIAM80"));
			pf.getLoginTRInstance(ob).clickLogin();

			String firstAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
			test.log(LogStatus.INFO, "Steam account profile name: " + firstAccountProfileName);
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getHFPageInstance(ob).clickOnAccountLink();
			String accountType = "Change password";

			validateNeonAccount(1, accountType);
			pf.getLoginTRInstance(ob).logOutApp();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);

			try {
				pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserFBENWIAM80"),
						LOGIN.getProperty("PWDUserFBENWIAM80"));
				test.log(LogStatus.PASS, "user has logged in with social account");
				pf.getLinkingModalsInstance(ob).clickOnNotNowButton();
				test.log(LogStatus.PASS, "Avoiding the Linking is happened");
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
				pf.getLoginTRInstance(ob).closeOnBoardingModal();
				String secondAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
				test.log(LogStatus.INFO, "Steam account profile name: " + secondAccountProfileName);
				pf.getHFPageInstance(ob).clickProfileImage();
				pf.getHFPageInstance(ob).clickOnAccountLink();
				accountType = "Facebook";

				validateAccounts(1, accountType);
				pf.getLoginTRInstance(ob).logOutApp();
				//BrowserWaits.waitTime(5);
				// Trying to Link the accounts
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
				try {
					pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("UserFBENWIAM80"),
							LOGIN.getProperty("PWDUserFBENWIAM80"));
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
					pf.getLinkingModalsInstance(ob).clickOnSignInUsingFB();
					test.log(LogStatus.PASS, "Linking is happened");
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
						Assert.assertEquals(winingAccountProfileName,firstAccountProfileName);
						test.log(LogStatus.PASS, "Random Merge is happened");

						if (winingAccountProfileName.contains(firstAccountProfileName)) {
							test.log(LogStatus.PASS, "Winning account is steam account");
						} else throw new Exception("Winning account is cannot be determined");
						
						pf.getHFPageInstance(ob).clickProfileImage();
					}

					catch (Throwable t) {

						test.log(LogStatus.FAIL, "Random Merge is not happened");// extent
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
