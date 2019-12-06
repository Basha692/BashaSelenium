package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class DRAIAM0016 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");

	}

	@Test
	public void testcaseDRA0016() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host + CONFIG.getProperty("appendDRAAppUrl"));

			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAfbuser14"),
					LOGIN.getProperty("DRAfbpw14"));
			pf.getDraPageInstance(ob).clickLoginDRA();
			test.log(LogStatus.PASS,
					"user successfully authenticated to the platform by by supplying correct STeAM credentials (email address + password), on the DRA sign in screen.");
			String DRAProfileName = pf.getDraPageInstance(ob).getProfileNameDRA();

			test.log(LogStatus.INFO, "DRA account profile name: " + DRAProfileName);
			try {
			pf.getBrowserWaitsInstance(ob)
			.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK);
	        pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK);
	        pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILEFLYOUT_ACCOUNTLINK_CSS);
	       pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_ACCOUNT_CROSS_CSS);
	       test.log(LogStatus.PASS, " Accounts setting modal has not initiated on-boarding. ");
			}
			catch (Throwable t) {
				test.log(LogStatus.FAIL, "Accounts setting modal has  initiated on-boarding.");
				ErrorUtil.addVerificationFailure(t);// testng
				
			}
	       // pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
			//pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
	        pf.getDraPageInstance(ob).logoutDRA();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUSERsteam0016"),
					LOGIN.getProperty("DRAUSERsteamPWD16"));
			pf.getLoginTRInstance(ob).clickLogin();

			pf.getDraPageInstance(ob).SearchDRAprofileName(DRAProfileName);
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DR_SEARCH_RESULT_CSS);
			pf.getDraPageInstance(ob).validateSearchResultMsg(test, DRAProfileName);
			pf.getLoginTRInstance(ob).logOutApp();
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
