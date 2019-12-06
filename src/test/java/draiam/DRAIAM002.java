package draiam;

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

public class DRAIAM002 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseDRA2() throws Exception {
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
			String statuCode = deleteUserAccounts(LOGIN.getProperty("DRAUserNameValid"));
			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
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
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("DRAUserNameValid"),
					LOGIN.getProperty("DRAPasswordValid"));
			test.log(LogStatus.PASS, "user has logged in with social account in Neon");
			String firstAccountProfileName = pf.getLinkingModalsInstance(ob).getProfileName();
			test.log(LogStatus.INFO, "Social account profile name: " + firstAccountProfileName);
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getHFPageInstance(ob).clickOnAccountLink();
			String accountType = "Facebook";

			validateAccounts(1, accountType);
			pf.getLoginTRInstance(ob).logOutApp();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);

			try {
				ob.navigate().to(host + CONFIG.getProperty("appendDRAAppUrl"));
				ob.navigate().refresh();
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_LOGO_CSS);
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUserNameValid"),
						LOGIN.getProperty("DRAPasswordValid"));
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
				pf.getDraPageInstance(ob).clickOnSignInWithFBOnDRAModal();
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_SEARCH_BOX_CSS);
				test.log(LogStatus.PASS, "User is able to click the link button");
				String secondAccountProfileName = pf.getDraPageInstance(ob).getProfileNameDRA();
				test.log(LogStatus.INFO, "Steam account profile name: " + secondAccountProfileName);
				//BrowserWaits.waitTime(2);
				pf.getDraPageInstance(ob).clickOnProfileImageDRA();
				pf.getDraPageInstance(ob).clickOnAccountLinkDRA();
				//BrowserWaits.waitTime(4);
				validateLinkedAccounts(2, accountType);

				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_ACCOUNT_CROSS_CSS);
				//BrowserWaits.waitTime(2);

				Assert.assertEquals(secondAccountProfileName, firstAccountProfileName);
				test.log(LogStatus.PASS, "Forward Merge is happened");
				if (secondAccountProfileName.contains(firstAccountProfileName)) {
					test.log(LogStatus.PASS, "Winning account is Facebook");
				}
			} catch (Throwable t) {
				closeBrowser();
				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is not able to click the link button");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_link")));

			}

			//BrowserWaits.waitTime(2);
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
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}

	private void validateAccounts(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("DRAUserNameValid")));
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

			Assert.assertTrue(
					pf.getDraPageInstance(ob).verifyLinkedAccountInDRA("Steam", LOGIN.getProperty("DRAUserNameValid")));
			Assert.assertTrue(
					pf.getDraPageInstance(ob).verifyLinkedAccountInDRA(linkName, LOGIN.getProperty("DRAUserNameValid")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS,
					"Linked accounts are available in accounts page : Neon and " + linkName + " accounts");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Linked accounts are not available in accounts page : Neon and " + linkName + " accounts");
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
