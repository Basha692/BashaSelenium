package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
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

public class DRAIAM0010 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");

	}

	@Test
	public void testcaseDRA0010() throws Exception {

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
			test.log(LogStatus.PASS, "User is succeccfully sent to the DRA landing page. ");

			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUSER0010"),
					LOGIN.getProperty("INVALIDDRAPWD10"));
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

			pf.getDraPageInstance(ob).validateInvalidCredentialsErrorMsg(test);
			//BrowserWaits.waitTime(2);
			ob.navigate().refresh();
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUSER0010"),
					LOGIN.getProperty("DRAUSERPWD10"));
			pf.getDraPageInstance(ob).clickLoginDRA();
			test.log(LogStatus.PASS,
					"user successfully authenticated to the platform by by supplying correct STeAM credentials (email address + password), on the DRA sign in screen.");
			
            //
			//fluentwaitforElement(ob, By.xpath(OnePObjectMap.DRA_EXPLORE_TARGET_DRUGGABILITY_XPATH.toString()), 60);
			//BrowserWaits.waitTime(5);
			fluentwaitforElement(ob, By.xpath(OnePObjectMap.DRA_EXPLORE_TARGET_DRUGGABILITY_XPATH.toString()),60);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_EXPLORE_TARGET_DRUGGABILITY_XPATH);
			
			pf.getDraPageInstance(ob).logoutDRA();
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
