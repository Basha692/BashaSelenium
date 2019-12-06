package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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

public class Customercare015 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("customercare");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseCustomercare015() throws Exception {
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
			ob.navigate().to(host + CONFIG.getProperty("appendDRACCUrl"));

			try {

				
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_EXTN_NAME);
				WebElement extn_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_EXTN_NAME);
				if (extn_element.isDisplayed()) {

					test.log(LogStatus.PASS,
							"extension field should be placed next to phone number field in customer care page.");
				}
				String firstName = "Naznina";
				String lastName ="clarivate";
				String Orgname = "Clarivate Analytics";
				String Email = "naznina.b@clarivate.com";
				String Phone = "74873023486";
				String Extension = "+91";
				Select dropdown = new Select(ob.findElement(By.name("country")));
				dropdown.selectByIndex(10);
				String Country = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME).getAttribute("value");
						
				String Request = "Please test the above scenario --ts0001";
				pf.getCustomercarePage(ob).EnterCustomercareDetails(test, firstName, lastName, Orgname, Email, Phone, Extension, Country, Request);

				test.log(LogStatus.PASS, "user is able to submit the form in DRA Customer care page . ");
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_MSG_CSS);
				//BrowserWaits.waitTime(3);
				pf.getCustomercarePage(ob).ValidateSuccessMessage(test);
				test.log(LogStatus.PASS,
						"Success message is displayed that confirms submission and allows user to raise a new ticket.");
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.CUSTOMER_CARE_USER_OK_BUTTON_CSS);

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "user is not able to submit the form in DRA Customer care page .");
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
