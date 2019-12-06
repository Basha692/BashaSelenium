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

public class ENWIAM43 extends TestBase {

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	@Test
	public void testcaseh4() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			openBrowser();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("sru_fbusername03"),
					LOGIN.getProperty("sru_fbpwd03"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getHFPageInstance(ob).clickOnEndNoteLink();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.ENW_YES_I_HAVE_AN_ACCOUNT_BUTTON_CSS);
			pf.getENWReferencePageInstance(ob).yesAccount(LOGIN.getProperty("NonActivated_SteamUsername"),
					LOGIN.getProperty("NonActivated_SteamPw"));

			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ACCOUNT_NOTACTIVATED_MSG_XPATH);

			String expected_msg = "Your account registration has not yet been confirmed";

			try {
				Assert.assertEquals(pf.getLinkingModalsInstance(ob).getMessageOnNotActivatedAccountModal(),
						expected_msg);
				test.log(LogStatus.PASS,
						"User is able to see message - Your account registration has not yet been confirmed.");

				try {
					pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.OK_BUTTON_ACCOUNT_NOTACTIVATED_XPATH);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.OK_BUTTON_ACCOUNT_NOTACTIVATED_XPATH);

				} catch (Throwable t) {
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is able to click on OK button");
					ErrorUtil.addVerificationFailure(t);
				}

			} catch (Throwable t) {

				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is able to see Activation not confirmed message");
				ErrorUtil.addVerificationFailure(t);

			}

			BrowserWaits.waitTime(2);
			logout();
			closeBrowser();
			pf.clearAllPageObjects();

		}

		catch (Throwable t) {

			test.log(LogStatus.FAIL, "Something Unexpected ");// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
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
