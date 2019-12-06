package watiam;

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

public class WATIAM009 extends TestBase {

	static int status = 1;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WATIAM");
	}

	@Test
	public void testcaseA3() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

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
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.IPA_BRANDING_NAME_CSS.toString()),
					30);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			BrowserWaits.waitTime(2);
			String emailErrorMessage = ob
					.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(0)
					.getText();
			Assert.assertTrue(emailErrorMessage.contains("Please enter a valid email address."));
			String passwordErrorMessage = ob
					.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(1)
					.getText();
			Assert.assertTrue(passwordErrorMessage.contains("Please enter a password."));
			test.log(LogStatus.PASS, "Correct Error message displayed when user click submit button without enter any email and password");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Correct Error message not displayed when user click submit button without enter any email and password");
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
