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
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENWIAM0001 extends TestBase {

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

	}

	@Test
	public void testLogin() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("UserFBENWIAM80"));
			Assert.assertTrue(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"));

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			loginTofb();
			loginToFacebook();
			closeBrowser();
		} catch (Throwable t) {
			logger.info("field");
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

	// Linking Facebook with the existing STeAM

	private void loginTofb() throws Exception {

		// On ENW, Navigate to TR login page and login with valid TR credentials
		ob.navigate().to(host+CONFIG.getProperty("appendENWAppUrl"));
		//String accountType = "Facebook";

		pf.getENWReferencePageInstance(ob).loginWithENWFBCredentials(LOGIN.getProperty("UserFBENWIAM80"), LOGIN.getProperty("PWDUserFBENWIAM80"));
		pf.getENWReferencePageInstance(ob).didYouKnow(LOGIN.getProperty("PWDUserFBENWIAM80"));

		/*try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		} catch (Exception e) {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		}*/
	pf.getENWReferencePageInstance(ob).clickAccount();
		//pf.getENWReferencePageInstance(ob).closeOnBoardingModal();
		//validateLinkedAccounts(2, accountType);
		pf.getENWReferencePageInstance(ob).logout();


	}

	
	// Signing into Facebook account again to ensure that linking modal is not displaying

	private void loginToFacebook() throws Exception {
		waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("FB_login_button")), 30);
		ob.findElement(By.cssSelector(OR.getProperty("FB_login_button"))).click();
		try {
			String text = ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()))
					.getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pf.getENWReferencePageInstance(ob).clickAccount();
		pf.getENWReferencePageInstance(ob).logout();

	}



	// Validating the linked accounts with STeAM
	private void validateLinkedAccounts(int accountCount,
			String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount("Neon", LOGIN.getProperty("UserFBENWIAM80")));
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("UserFBENWIAM80")));
			test.log(LogStatus.PASS, "The account are matching");
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			System.out.println(accountCount);
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
