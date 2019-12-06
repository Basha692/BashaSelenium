package draiam;

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

public class DRAIAM007 extends TestBase {

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
	public void testcaseh2() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}


		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("DRAfbuser7"),
					LOGIN.getProperty("DRAfbpw7"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getDraPageInstance(ob).clickDRALink();
			test.log(LogStatus.PASS, "user is navigate to Step up Auth modal");
			pf.getDraPageInstance(ob).validateStepUPModalTitle(test);
			pf.getDraPageInstance(ob).validateFirstTextOnStepUp(test);
			test.log(LogStatus.INFO, "Verified whether user is able to see First text on step up modal");
			pf.getDraPageInstance(ob).validateSecondTextOnStepUP(test);
			test.log(LogStatus.INFO, "Verified whether user is able to see second text on step up modal");
			pf.getDraPageInstance(ob).validateThirdTextOnStepUp(test);
			test.log(LogStatus.INFO, "Verified whether user is able to see Third text on step up modal");
			pf.getDraPageInstance(ob).validateProductOverviewPage(test);
			BrowserWaits.waitTime(2);
			try{
				pf.getDraPageInstance(ob).validateForgotPwOnStepup(test);
				test.log(LogStatus.INFO, "forgot password link is present on Stepup");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "forgot password link is not present on Stepup");
			}	
			BrowserWaits.waitTime(2);
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

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
