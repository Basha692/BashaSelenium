package ipaiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IPAIAM0001 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseIPA1() throws Exception {
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
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));

			// Verify that IPA Landing page, displays application branding and
			// logo
			try {

				// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_BRANDING_NAME_CSS);
				WebElement brand_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_BRANDING_NAME_CSS);

				String branding_name = brand_element.getText();

				if (brand_element.isDisplayed()) {
					Assert.assertEquals(branding_name, "IP Analytics");
					test.log(LogStatus.PASS, "IPA Landing page displays branding and marketing copy");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "IPA Landing page doesn't displays branding and marketing copy");
				ErrorUtil.addVerificationFailure(t);
			}
			try {

				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_NEW_LOGO_CSS);
				WebElement brand_logo = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.IPA_NEW_LOGO_CSS);

				if (brand_logo.isDisplayed()) {
					test.log(LogStatus.PASS, "IPA Landing page displays Clarivate Analytics logo");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "IPA Landing page doesn't display Clarivate Analytics logo");
				ErrorUtil.addVerificationFailure(t);
			}
			// Verifying that IPA Landing page, displays privacy statement and
			// terms of use links
			try {

				/*
				 * pf.getBrowserWaitsInstance(ob)
				 * .waitUntilElementIsDisplayed(OnePObjectMap.
				 * IPA_LANDINGPAGE_TERMS_LINK_CSS);
				 * pf.getBrowserWaitsInstance(ob)
				 * .waitUntilElementIsDisplayed(OnePObjectMap.
				 * IPA_LANDINGPAGE_PRIVACY_LINK_CSS);
				 */
				WebElement tl_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LANDINGPAGE_TERMS_LINK_CSS);
				WebElement pl_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LANDINGPAGE_PRIVACY_LINK_CSS);

				if (tl_element.isDisplayed() && pl_element.isDisplayed()) {
					test.log(LogStatus.PASS, "IPA Landing page displays terms of use and privacy links");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "IPA Landing page doesn't displays terms of use and privacy links");
				ErrorUtil.addVerificationFailure(t);
			}

			// verifying that IPA Landing page, displays the message and email
			// id
			try {
				/*
				 * pf.getBrowserWaitsInstance(ob)
				 * .waitUntilElementIsDisplayed(OnePObjectMap.
				 * IPA_LANDINGPAGE_HELP_MESSAGE_CSS);
				 * pf.getBrowserWaitsInstance(ob)
				 * .waitUntilElementIsDisplayed(OnePObjectMap.
				 * IPA_LANDINGPAGE_SUPPORT_MAILID_CSS);
				 */
				/*WebElement helpmsg = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LANDINGPAGE_HELP_MESSAGE_CSS);
				WebElement supportmailid = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.IPA_LANDINGPAGE_SUPPORT_MAILID_CSS);
				String actual_help_text = helpmsg.getText();
				String actual_mail_id = supportmailid.getText();

				if (actual_help_text.contains("Need help signing in? Please contact")
						&& actual_mail_id.contains("IPA_support@thomsonreuters.com")) {
					test.log(LogStatus.PASS, "IPA Landing page  displays the message and email");
				}*/

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "IPA Landing page displays the incorrect message and email");
				ErrorUtil.addVerificationFailure(t);
			}

			// Verifying IPA marketing copy are displayed.
			try {
				boolean find_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.IPA_LANDINGPAGE_EXPLORE_ICON_CSS.toString()));
				Assert.assertEquals(find_icon, true);
				test.log(LogStatus.PASS, "Explore icon is displayed on IPA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Explore icon is not displayed on IPA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean Analyze_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.IPA_LANDINGPAGE_ANALYZE_ICON_CSS.toString()));
				Assert.assertEquals(Analyze_icon, true);
				test.log(LogStatus.PASS, "Analyze icon is displayed on IPA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Analyze icon is not displayed on IPA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean visualize_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.IPA_LANDINGPAGE_VISUALIZE_ICON_CSS.toString()));
				Assert.assertEquals(visualize_icon, true);
				test.log(LogStatus.PASS, "Visualize icon is displayed on IPA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Visualize icon is not displayed on IPA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean Report_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.IPA_LANDINGPAGE_REPORT_ICON_CSS.toString()));
				Assert.assertEquals(Report_icon, true);
				test.log(LogStatus.PASS, "Report icon is displayed on IPA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Report icon is not displayed on IPA landing page");
				ErrorUtil.addVerificationFailure(t);
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
