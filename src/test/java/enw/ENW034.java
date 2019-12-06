package enw;

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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENW034 extends TestBase {

	static int status = 1;

	// Following is the list of status:

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW034() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			String header_Expected = "Thomson Reuters";
			ob.navigate().to(host);
			loginAs("NEONFIRSTUSER", "PASSWORD10");
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.NEON_SWITCH_APPS_CSS.toString())));
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_SWITCH_APPS_ENDNOTE_LINK_CSS.toString()),
					180);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.NEON_SWITCH_APPS_ENDNOTE_LINK_CSS.toString())));
			handleContinueAndAgree();
			waitForElementTobeVisible(ob, By.cssSelector("span[class='ne-nav-logo__text ne-nav-logo__text--tr']"), 180);
			String actual_result = ob
					.findElement(By.cssSelector("span[class='ne-nav-logo__text ne-nav-logo__text--tr']")).getText();
			logger.info("Header Text displayed as:" + actual_result);
			try {
				Assert.assertEquals(header_Expected, actual_result);
				test.log(LogStatus.PASS, " Header Logo text is displayed properly for Non-Market users");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, " Header Logo text is not displayed properly for Non-Market users");// extent
				ErrorUtil.addVerificationFailure(t);// testng reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
			}
			closeBrowser();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// //
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

	}

	private void handleContinueAndAgree() {
		try {
			String text = ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()))
					.getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}