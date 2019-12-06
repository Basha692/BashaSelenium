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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENWIAM00019 extends TestBase {

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
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("ENWIAM00019User"),
					LOGIN.getProperty("ENWIAM00019UserPWD"));
			pf.getLoginTRInstance(ob).clickLogin();
			test.log(LogStatus.PASS, "user has logged in to Neon with steam account");
			pf.getHFPageInstance(ob).clickOnEndNoteLink();
			test.log(LogStatus.PASS, "user has logged in to ENDNote with steam account");
			BrowserWaits.waitTime(3);
			try {

				try {
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				} catch (Exception e) {
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				}
				BrowserWaits.waitTime(3);
				try {
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENDNOTE_LOGO_CSS);
					Assert.assertEquals(pf.getEnwReferenceInstance(ob).validateNavigationToEnw(), true);
					test.log(LogStatus.PASS, "user is able navigate to EndNote");
					logoutEnw();

					BrowserWaits.waitTime(5);
					String url = "https://dev-stable.1p.thomsonreuters.com";
					String actualurl = ob.getCurrentUrl();
					try {
						if (actualurl.contains(url))
							test.log(LogStatus.PASS, " when  user signs out of ENW, system has referrer URL = endnote");
						else {
							test.log(LogStatus.FAIL,
									"when  user signs out of ENW, system does not has referrer URL = endnote");// extent

						}
					}

					catch (Throwable t) {

						test.log(LogStatus.FAIL,
								"when  user signs out of ENW, system does not has referrer URL = endnote");// extent
						// reports
						status = 2;// excel
						test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
								.getClass().getSimpleName()
								+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
						ErrorUtil.addVerificationFailure(t);

					}
				} catch (Throwable t) {
					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to navigate from Neon to EndNote");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_Not_navigated_to_Enw")));

				}
				test.log(LogStatus.PASS, "ENW session Closed");
				BrowserWaits.waitTime(3);
				ob.navigate().to(host);
				waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
				test.log(LogStatus.PASS, "Neon session Closed");
				ob.navigate().to(host + CONFIG.getProperty("appendENWAppUrl"));
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("ENWIAM00019User"),
						LOGIN.getProperty("ENWIAM00019UserPWD"));
				test.log(LogStatus.PASS, "user has logged in To ENW");
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
				waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENW_PROFILE_HEADER_XPATH.toString()), 30);
				ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_HEADER_XPATH.toString())).click();
				test.log(LogStatus.PASS, "user is able navigate to Neon");
				BrowserWaits.waitTime(3);
				logout();
				test.log(LogStatus.PASS, "Neon session Closed");
				ob.navigate().to(host + CONFIG.getProperty("appendENWAppUrl"));
				test.log(LogStatus.PASS, "ENW session Closed");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "user is not able to link and navigate to EndNote");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_link_and_navigate_to_enw")));

			}

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
