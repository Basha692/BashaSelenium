package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENWIAM019 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

	}

	@Test
	public void testCaseA23() throws Exception {

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
			// 1)Create a new user
			// 2)Login with new user and logout
			openBrowser();
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob, By.name("loginEmail"), 180);
			ob.findElement(By.name("loginEmail")).sendKeys("userendnote@gmail.com");
			ob.findElement(By.name("loginPassword")).sendKeys("Neon@123");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			String text = ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()))
					.getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}
			waitUntilText("Getting Started","Find","Collect");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString()), 30);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString())));
			waitUntilText("Account");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString())).click();
			waitUntilText("View additional email preferences");

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("change_password_link")), 30);
			jsClick(ob, ob.findElement(By.xpath(OR.getProperty("change_password_link"))));
			waitUntilText("Forgot password?");
			ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_OLD_PASSWORD_FIELD_CSS.toString())).sendKeys("Neon@123");
			ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_NEW_PASSWORD_FIELD_CSS.toString())).sendKeys("Neon@1234");
			ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_CANCEL_BUTTON_XPATH.toString())).click();
			waitUntilText("Change password");
			/*String resertPassPage = ob
					.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_RESET_PASSWORD_PAGE_CSS.toString())).getText();
			BrowserWaits.waitTime(3);
			Assert.assertEquals(resertPassPage, "Reset your password");
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));*/
			logout();
			ob.quit();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		closeBrowser();
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
