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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

public class ENW011 extends TestBase {

	static int status = 1;

	// 
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW011() throws Exception {
		boolean testRunmode = TestUtil.isTestCaseRunnable(enwxls, this.getClass().getSimpleName());
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

			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			String actual_result = "";
			String user_First_Name = "";
			String user_Last_Name = "";
			String user_Full_name = "";
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("ENW011USERNAME"),(LOGIN.getProperty("ENW011PASSWORD")));
			//BrowserWaits.waitTime(6);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_OPTIONS_TAB_XPATH,10);
			jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_OPTIONS_TAB_XPATH.toString())));
			//BrowserWaits.waitTime(4);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_PROFILE_TAB_XPATH,4);
			jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_TAB_XPATH.toString())));
			//BrowserWaits.waitTime(1);
			user_First_Name = ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_FIRST_NAME_XPATH.toString()))
					.getAttribute("value");
			user_Last_Name = ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_LAST_NAME_XPATH.toString()))
					.getAttribute("value");

			logger.info("User name Saved as:" + user_Full_name);
			//BrowserWaits.waitTime(1);
			ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())).click();
			actual_result = ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_NAME_XPATH.toString())).getText();
			if (ob.findElement(By.className("inactiveLink")) == null) {
				logger.info("User name NOT hyperlinked to Profile page");
				logger.info("NOT hyperlinked:" + actual_result);
				actual_result = "test case is failed";
				Assert.assertEquals(true, false);
			}
			logger.info("Actual result displayed as :" + actual_result);
			try {
				Assert.assertTrue(actual_result.contains(user_First_Name));
				Assert.assertTrue(actual_result.contains(user_Last_Name));
				test.log(LogStatus.PASS, " User first and last name are displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User first and last name are not displayed correctly");// extent
				ErrorUtil.addVerificationFailure(t);// testng // reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "User first and last name are not displayed correctly")));// screenshot
			}
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
		closeBrowser();
	}

}
