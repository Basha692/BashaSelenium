package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Customercare014 extends TestBase{
	
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
			ob.navigate().to(host + CONFIG.getProperty("appendDRACCUrl"));

			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_FIRSTNAME_NAME);
				WebElement firstname_element = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_FIRSTNAME_NAME);
				
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_ORGANIZATION_NAME);
				WebElement organz_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_ORGANIZATION_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_EMAILID_NAME);
				WebElement email_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_EMAILID_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME);
				WebElement phone_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME);
				WebElement country_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_CATEGORY_NAME);
				WebElement category_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_CATEGORY_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_NAME);
				WebElement description_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_NAME);
			

				if (firstname_element.isDisplayed() && organz_element.isDisplayed() && email_element.isDisplayed()
						&& phone_element.isDisplayed() && country_element.isDisplayed() &&  category_element.isDisplayed() && description_element.isDisplayed()){
					
					test.log(LogStatus.PASS,
							"DRA Customer care page displays required fields : Name,Organization,Contact details (email, telephone),Issue Category,Country,Description of issue");
				}
					

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"DRA Customer care page is not displaying required fields ");
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
