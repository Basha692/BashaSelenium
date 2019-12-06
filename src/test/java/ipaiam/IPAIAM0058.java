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

public class IPAIAM0058 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");

	}

	@Test
	public void testcaseIPA0058() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

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
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("DRAFBUSER0018"),
					LOGIN.getProperty("DRAFBUSERPWD18"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			BrowserWaits.waitTime(2);
			pf.getIpaPage(ob).clickIPALink();
			test.log(LogStatus.PASS, "STeAM Step Up Auth Modal is displayed");
			//pf.getDraPageInstance(ob).validateProductOverviewPage(test);

			pf.getDraPageInstance(ob).clickDRAStepUpAuthLoginNotEntitledUser(test, "abcd");
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.DRA_INVALIDCREDENTIALS_ERRORMSG_CSS);
			pf.getDraPageInstance(ob).validateInvalidCredentialsErrorMsg(test);
			BrowserWaits.waitTime(2);
			pf.getDraPageInstance(ob).clickDRAStepUpAuthLoginNotEntitledUser(test, LOGIN.getProperty("DRAFBUSERPWD18"));
			pf.getIpaPage(ob).validateIPAInactiveErrorMsg(test);
			BrowserWaits.waitTime(3);
			//pf.getDraPageInstance(ob).validateProductOverviewPage(test);
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent

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

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}

