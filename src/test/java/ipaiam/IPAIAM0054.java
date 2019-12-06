package ipaiam;



import java.io.PrintWriter;
import java.io.StringWriter;

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

public class IPAIAM0054 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");

	}

	@Test
	public void testcaseIPA0054() throws Exception {

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
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));

			
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("IPAUSER0054"),
					LOGIN.getProperty("IPAPWD0054"));
			pf.getIpaPage(ob).clickLoginIPA();
			test.log(LogStatus.PASS,
					"user successfully authenticated to the platform by by supplying correct STeAM credentials (email address + password), on the DRA sign in screen.");
			String DRAProfileName = pf.getDraPageInstance(ob).getProfileNameDRA();
			
			test.log(LogStatus.INFO, "DRA account profile name: " + DRAProfileName);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
			//BrowserWaits.waitTime(5);
			ob.navigate().refresh();
			ob.navigate().to(host);
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUSERsteam0016"),
					LOGIN.getProperty("DRAUSERsteamPWD16"));
			pf.getLoginTRInstance(ob).clickLogin();

			pf.getDraPageInstance(ob).SearchDRAprofileName("kavita manaz");
			
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DR_SEARCH_RESULT_CSS);
			pf.getIpaPage(ob).validateSearchResultMsg(test, "kavita manaz");
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
