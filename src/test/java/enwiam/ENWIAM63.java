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
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENWIAM63 extends TestBase {

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static int time = 30;
	PageFactory pf = new PageFactory();
	// 1) Sign-in with Steam and link existing social account with matching email. 
	//2) Sign-in with Steam account which already has linked social account. Verify that user should never be prompted 
	//to link accounts, when sign in first time on Neon landing screen using social.
	//3)Verify that user should be prompted to link accounts, when sign in first time on Neon landing screen using STeAM. 
	//(Note:User should already been sign into social)
	
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}
	@Test
	public void testInitiatePostCreation() throws Exception {

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
			String statuCode = deleteUserAccounts(LOGIN.getProperty("UserName18"));
			Assert.assertTrue(statuCode.equalsIgnoreCase("200"));
			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
				throw new Exception("Delete API Call failed");
			}
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		loginTofb();
		linkAccounts("Facebook");
		
		closeBrowser();
//			
	}

	/*private void loginToLinkedIn() throws Exception {
		openBrowser();
		maximizeWindow();
		clearCookies();

		// Navigate to TR login page and login with valid TR credentials
		ob.navigate().to(host);
		pf.getLoginTRInstance(ob).loginWithLinkedInCredentials(LOGIN.getProperty("UserName18"),
				LOGIN.getProperty("Password18"));
		pf.getLoginTRInstance(ob).logOutApp();
		closeBrowser();
		pf.clearAllPageObjects();

	}*/
	private void loginTofb() throws Exception {
		openBrowser();
		maximizeWindow();
		clearCookies();
		// Navigate to TR login page and login with valid TR credentials
		ob.navigate().to(host);
		pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserName18"),
				LOGIN.getProperty("Password18"));
		pf.getLoginTRInstance(ob).logOutApp();
		closeBrowser();
		pf.clearAllPageObjects();

	}

	private void linkAccounts(String accountType) throws Exception {

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
		// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));

			
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("UserName18"),
						LOGIN.getProperty("Password18"));
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
				test.log(LogStatus.PASS, "User is able to log in with steam credentials");
				pf.getLinkingModalsInstance(ob).clickOnSignInUsingFB();
			
			
			if (accountType.equalsIgnoreCase("Facebook")) {
				pf.getLoginTRInstance(ob).signInToFacebook(LOGIN.getProperty("UserName18"),
						LOGIN.getProperty("Password18"));
				test.log(LogStatus.PASS, "User is able to link " + accountType + " account to Neon account");
			} else if (accountType.equalsIgnoreCase("LinkedIn")) {
				pf.getLoginTRInstance(ob).signInToLinkedIn(LOGIN.getProperty("UserName18"),
						LOGIN.getProperty("Password18"));
				test.log(LogStatus.PASS, "User is able to link " + accountType + " account to Neon account");
			}

			pf.getHFPageInstance(ob).clickOnAccountLink();
			validateLinkedAccounts(2, accountType);
			pf.getLoginTRInstance(ob).logOutApp();
			try {

				loginAs("UserName18", "Password18");
				test.log(LogStatus.PASS,
						"User is not asked to link the account again when user has already linked them");
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "User is asked to link the account again when user has already linked them");
				ErrorUtil.addVerificationFailure(e);// testng
				test.log(LogStatus.INFO, "Snapshot below: "
						+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "Linking_failed")));// screenshot
			}
			pf.getHFPageInstance(ob).clickOnAccountLink();
			validateLinkedAccounts(2, accountType);
			pf.getLoginTRInstance(ob).logOutApp();

		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			status = 2;// excel
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng

			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot

		} finally {
			closeBrowser();
			pf.clearAllPageObjects();

		}
	}

	private void validateLinkedAccounts(int accountCount,
			String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount("Change password", LOGIN.getProperty("UserName18")));
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserName18")));
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

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls, "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" , TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */

	}
}
