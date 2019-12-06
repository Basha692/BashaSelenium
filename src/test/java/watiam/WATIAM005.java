package watiam;

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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class WATIAM005 extends TestBase {

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
	public void testcaseA4() throws Exception {
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

			waitForElementTobeVisible(ob,
					By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK.toString()), 30);

			ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK.toString())).click();
			BrowserWaits.waitTime(3);

			String termsOfUserText = ob
					.findElement(By.cssSelector(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS.toString())).getText();
			logger.info("Modal Text : " + termsOfUserText);

			Assert.assertEquals(termsOfUserText, "Terms of Use");
			BrowserWaits.waitTime(3);
			ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS.toString()))
					.click();
			BrowserWaits.waitTime(2);

			WebElement myE = ob.findElement(
					By.partialLinkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIVACY_STATEMENT_LINK.toString()));
			myE.click();
			BrowserWaits.waitTime(3);
			String privacyStatementsText = ob
					.findElement(By.cssSelector(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS.toString())).getText();
			logger.info("Modal Text : " + privacyStatementsText);
			Assert.assertEquals(privacyStatementsText, "Privacy Statement");
			BrowserWaits.waitTime(3);
			ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS.toString()))
					.click();

			test.log(LogStatus.PASS,
					"Terms of User and Privacy Statements are displayed in WAT application login page");
			pf.getBrowserActionInstance(ob).closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Terms of User and Privacy Statements are not displayed in WAT application login page");
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

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
