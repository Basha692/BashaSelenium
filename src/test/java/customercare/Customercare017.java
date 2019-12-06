package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Customercare017 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/** Verify that Countries list should be updated to match with SFDC list in customer care page as per document OPWLRA-630.xlsx.
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
	public void testcaseCustomercare017() throws Exception {
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
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_CC_HEADER_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_CC_HEADER_CSS);
				String DropText = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME).getText();
			   
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME);
				if (DropText.contains("Åland Islands") && DropText.contains("Côte d'Ivoire")
						&& DropText.contains("Holy See (Vatican City State)")
						&& DropText.contains("Iran, Islamic Republic of")
						&& DropText.contains("Korea, Democratic People's Republic of")
						&& DropText.contains("Korea, Republic of") && DropText.contains("Libyan Arab Jamahiriya")
						&& DropText.contains("Macedonia, The Former Yugoslav Republic of")
						&& DropText.contains("Netherlands Antilles")
						&& DropText.contains("Palestinian Territory, Occupied") && DropText.contains("South Georgia")
						&& DropText.contains("Tanzania, United Republic of")
						&& DropText.contains("United States & Outlying Islands") && DropText.contains("United States")
						&& DropText.contains("Virgin Islands, British") && DropText.contains("Virgin Islands, U.S.")) {
					
					test.log(LogStatus.PASS,
							"Countries list is updated to match with SFDC list in customer care page as per document OPWLRA-630.xlsx");

				} else {
					test.log(LogStatus.FAIL,
							"Countries list is not updated to match with SFDC list in customer care page");

				}

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Countries list is not updated to match with SFDC list in customer care page");
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
