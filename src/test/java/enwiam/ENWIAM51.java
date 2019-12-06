package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class ENWIAM51 extends TestBase {

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
	public void testcaseh11() throws Exception {
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
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));

			// Verify EndNote landing page displays EndNote branding and
			// marketing copy

			try {

				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.BRANDING_NAME_CSS);
				WebElement b_element = ob.findElement(By.cssSelector(OnePObjectMap.BRANDING_NAME_CSS.toString()));
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_MARKETING_COPY_CSS);
				WebElement m_element = ob.findElement(By.cssSelector(OnePObjectMap.NEON_MARKETING_COPY_CSS
								.toString()));

				String marketing_Copy = m_element.getText();

				if (b_element.isDisplayed() && m_element.isDisplayed()) {
				
					Assert.assertEquals(marketing_Copy, "EndNote");
					test.log(LogStatus.PASS,
							"EndNote Landing page displays EndNote branding and marketing copy");
				}
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "EndNote Landing page doesn't displays EndNote branding and marketing copy");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_close_modal")));

			}

			// Verifying that on ENW landing page, link to login with Shibboleth
			// is displayed.
			try {
				boolean shibbLink = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.ENW_SHIBB_LINK_CSS.toString()));
				Assert.assertEquals(shibbLink, true);
				test.log(LogStatus.PASS, "EndNote Landing page displays link to login with Shibboleth");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "EndNote Landing page doesn't display link to login with Shibboleth");
				ErrorUtil.addVerificationFailure(t);
			}

			// verifying that on ENW landing page displays, informational text
			// that explains the trial link for Endnote desktop.

			try {
				boolean endnote_desktop_text = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.ENW_DESKTOP_TEXT_CSS.toString()));
				Assert.assertEquals(endnote_desktop_text, true);
				test.log(LogStatus.PASS,
						"EndNote Landing page displays text that explains the trial link for Endnote desktop");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL,
						"EndNote Landing page doesn't display text that explains the trial link for Endnote desktop");
				ErrorUtil.addVerificationFailure(t);
			}

			// verifying that user is taken to the ENW desktop X7 free trial
			// download form

			try {
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_DESKTOP_DOWNLOAD_LINK);
				BrowserWaits.waitTime(4);

				Set<String> myset = ob.getWindowHandles();
				Iterator<String> myIT = myset.iterator();
				ArrayList<String> al = new ArrayList<String>();
				for (int i = 0; i < myset.size(); i++) {

					al.add(myIT.next());
				}

				ob.switchTo().window(al.get(1));

				test.log(LogStatus.INFO, "Preference link is present and clicked");

				test.log(LogStatus.INFO, "Preference page is opened successfully");
				String actual_URL = ob.getCurrentUrl();
				String expected_URL = "https://endnote.com/downloads/30-day-trial/?utm_source=en-online-1p&utm_medium=referral&utm_campaign=en-online-trial";
				Assert.assertTrue(actual_URL.contains(expected_URL));
				test.log(LogStatus.PASS,
						"user is taken to the Endnote desktop X7 free trial download form by clicking Download link on the Endnote sign");
				BrowserWaits.waitTime(2);
				ob.close();
				ob.switchTo().window(al.get(0));

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL,
						"user is not taken to the Endnote desktop X7 free trial download form by clicking Download link on the Endnote sign");
				ErrorUtil.addVerificationFailure(t);
			}

			// Verifying links to EndNote marketing pages are displayed.
			try {
				boolean find_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.ENWLANDINGPAGE_FIND_ICON_CSS.toString()));
				Assert.assertEquals(find_icon, true);
				test.log(LogStatus.PASS, "Find icon is displayed on EndNote landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Find icon is not displayed on EndNote landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean organize_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.ENWLANDINGPAGE_ORGANIZE_ICON_CSS.toString()));
				Assert.assertEquals(organize_icon, true);
				test.log(LogStatus.PASS, "Organize icon is displayed on EndNote landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Organize icon is not displayed on EndNote landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean create_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.ENWLANDINGPAGE_CREATE_ICON_CSS.toString()));
				Assert.assertEquals(create_icon, true);
				test.log(LogStatus.PASS, "Create icon is displayed on EndNote landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Create icon is not displayed on EndNote landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean connect_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.NEON_CONNECT_ICON_CSS.toString()));
				Assert.assertEquals(connect_icon, true);
				test.log(LogStatus.PASS, "Connect icon is displayed on EndNote landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Connect icon is not displayed on EndNote landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {

				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_SHIBB_LINK_CSS);
				String expectedShibbLink = "http://error-qa.newisiknowledge.com";
				//BrowserWaits.waitTime(4);
				String actualShibbLinkurl = ob.getCurrentUrl();
				// Assert.assertEquals(actualShibbLinkurl, expectedShibbLink);
				if (actualShibbLinkurl.contains(expectedShibbLink))
					test.log(LogStatus.PASS, "Shibboleth link is taking to proper url");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Shibboleth link is not taking to proper url");
				ErrorUtil.addVerificationFailure(t);
			}

			//BrowserWaits.waitTime(2);
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
