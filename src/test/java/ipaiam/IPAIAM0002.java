package ipaiam;

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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IPAIAM0002 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/**
	 * 
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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseIPA2() throws Exception {
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
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
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
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("DRAUserNameValid"),
					LOGIN.getProperty("DRAPasswordValid"));
			test.log(LogStatus.PASS, "user has logged in with social account in Neon");
			pf.getHFPageInstance(ob).clickOnAccountLink();
			pf.getLoginTRInstance(ob).logOutApp();
			//BrowserWaits.waitTime(5);

			try {
				ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
				ob.navigate().refresh();
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_BRANDING_NAME_CSS);
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("DRAUserNameValid"),
						LOGIN.getProperty("DRAPasswordValid"));
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS);

				String expected_title_onmodal = "Already have an account?";
				String expected_text1 = "You have previously signed in with ";
				String expected_text2 = "To protect your security, please sign into Facebook so that we can link your account.";
				String expected_email = LOGIN.getProperty("DRAUserNameValid");
				// verifying that did you know modal is displaying or not.
				String actualtext1 = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LINKINGMODAL_TEXT1_XPATH).getText();
				String actualtext2 = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LINKINGMODAL_TEXT2_XPATH).getText();
				String actualEmail = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LINKINGMODAL_EMAIL_CSS).getText();
				try {
					Assert.assertEquals(pf.getLinkingModalsInstance(ob).getMessageOnDidYouKnowModal(),
							expected_title_onmodal);
					if (actualtext1.contains(expected_text1) && actualtext2.contains(expected_text2)
							&& actualEmail.contains(expected_email)) {
						test.log(LogStatus.PASS,
								"User is able to see 'Already have an account? ...' Modal with correct text ");
					} else {
						test.log(LogStatus.FAIL, "Incorrect text is displayed on 'Already have an account? ...' Modal");
					}

				} catch (Throwable t) {
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to see 'Already have an account? ...' Modal");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_msg_not_displayed")));

				}

				// verifying user is not able to exit the linking modal by clicking
				// outside the linking modal

				try {
					pf.getDraPageInstance(ob).clickOutsideTheDRAModal();

					Assert.assertEquals(pf.getBrowserActionInstance(ob).checkElementIsDisplayed(ob,
							By.cssSelector(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS.toString())), true);

					test.log(LogStatus.PASS, "User is able to click outside the modal");
				} catch (Throwable t) {
					closeBrowser();
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to click outside the modal");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_link")));
				}
				
				// verifying user is able to exit the linking modal by clicking
				// close button on the modal

				try {
					pf.getLinkingModalsInstance(ob).clickOnCloseButton();
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS);
					Assert.assertEquals(pf.getBrowserActionInstance(ob).checkElementIsDisplayed(ob,
							By.cssSelector(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS.toString())), false);
					test.log(LogStatus.PASS,
							"User is able to close the modal ");
					
				} catch (Throwable t) {
					closeBrowser();
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to close the modal");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_link")));
				}
				
					

				//BrowserWaits.waitTime(2);
				closeBrowser();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User is not able to login to IPA");// extent
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
