package draiam;

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

public class DRAIAM0013 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");

	}

	@Test
	public void testcaseDRA0013() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host + CONFIG.getProperty("appendDRAAppUrl"));
			test.log(LogStatus.PASS, "User is succeccfully sent to the DRA landing page. ");

			String cssValue = ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS.toString()))
					.getCssValue("background-color");
			System.out.println(cssValue);
			if (cssValue.contains("rgba(0, 128, 128, 1)")) {
				test.log(LogStatus.PASS, "Sign in button is  displayed in  application specific green colour");
			} else {
				test.log(LogStatus.FAIL, "Sign in button is not  displayed in  application specific green colour");
				status = 2;
			}
			String cssValue1 = ob.findElement(By.cssSelector("html[class='unauth-page--dra']"))
					.getCssValue("background");
			logger.info("Values : " + cssValue1);

			if (cssValue1.contains("rgb(139, 197, 197)")== true && cssValue1.contains("rgba(0, 0, 0, 0)")== true || cssValue1.contains("")==true ) {
				test.log(LogStatus.PASS, " DRA Landing page displayed  in  application specific green colour");
			} else {
				test.log(LogStatus.FAIL, " DRA Landing page is not displayed  in  application specific green colour");
				status = 2;
			}
			try {
				boolean ForgotPasswordLink = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.DRA_FORGOT_PASSWORD_LINK_CSS.toString()));
				Assert.assertEquals(ForgotPasswordLink, true);
				test.log(LogStatus.PASS, "DRA Landing page displays forgot password link");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "DRA Landing page doesn't display forgot password link");
				ErrorUtil.addVerificationFailure(t);
			}

			
			ob.close();

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
			ErrorUtil.addVerificationFailure(t);

			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
