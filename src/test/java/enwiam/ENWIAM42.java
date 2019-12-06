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

public class ENWIAM42 extends TestBase {

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
	public void testcaseh3() throws Exception {
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

			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("sru_fbusername02"),
					LOGIN.getProperty("sru_fbpwd02"));
			test.log(LogStatus.PASS, "user has logged in with social account which has Matching Steam");
			pf.getHFPageInstance(ob).clickOnEndNoteLink();

			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS);

			String expected_text_onmodal = "Did you know?"; 

			// verifying that did you know modal is displaying or not.

			try {
				Assert.assertEquals(pf.getLinkingModalsInstance(ob).getMessageOnDidYouKnowModal(),
						expected_text_onmodal);
				test.log(LogStatus.PASS,
						"User is able to see 'Did you know? ...' Modal when user has email same as existing steam acount ");

				// verifying user is able to exit the linking modal by clicking
				// close button on the modal

				try {
					pf.getLinkingModalsInstance(ob).clickOnCloseButton();

					if (!checkElementPresence("ul_name")) {

						test.log(LogStatus.FAIL, "User is to not taken back to the Neon Home page ");// extent
																										// reports
						status = 2;// excel

						closeBrowser();

					}

					test.log(LogStatus.PASS,
							"User is to taken back to the Neon Home page when clicked on close button on modal ");

				} catch (Throwable t) {
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to close the linking modal");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_close_modal")));

				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is not able to see 'Did you know? ...' Modal");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_msg_not_displayed")));

			}
			BrowserWaits.waitTime(2);
			pf.getHFPageInstance(ob).clickOnEndNoteLink();

			// verifying user is able to exit the linking modal through clicking
			// anywhere on the page
			try {

				pf.getLinkingModalsInstance(ob).clickOutsideTheModal();

				if (!checkElementPresence("ul_name")) {

					test.log(LogStatus.FAIL, "User is not able to exit the linking modal ");// extent
																							// reports
					status = 2;// excel

					closeBrowser();

				}

				test.log(LogStatus.PASS, "User is to taken back to the Neon Home page when clicked outside the modal ");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is not able to click outside the linking modal");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_click_outside_modal")));

			}

			BrowserWaits.waitTime(2);
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
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}