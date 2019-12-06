package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class Customercare013 extends TestBase {

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
	public void testcaseDRA70() throws Exception {
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
			ob.navigate().to(host + CONFIG.getProperty("appendPACCAppUrl"));

			try {
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CALLUS_SECTION_CSS);
				WebElement callus_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_CALLUS_SECTION_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_REGION_CSS);
				WebElement region_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_REGION_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_HRS_XPATH);
				WebElement hrs_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_HRS_XPATH);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_LANGUAGE_XPATH);
				WebElement language_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_LANGUAGE_XPATH);

				String hours_of_operation = hrs_element.getText();
				String lang = language_element.getText();

				if (callus_element.isDisplayed() && region_element.isDisplayed() && hrs_element.isDisplayed()
						&& language_element.isDisplayed())
					Assert.assertEquals(lang, "English");
				Assert.assertTrue(hours_of_operation.contains("GMT-5"));

				test.log(LogStatus.PASS,
						"IPA Customer care page displays Call us section and customer care contact details");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"IPA Customer care page doesn't display Call us section and customer care contact details");
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
