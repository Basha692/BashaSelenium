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

public class ENWIAM006 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	@Test
	public void testcaseA15() throws Exception {

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
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			// ob.navigate().to(CONFIG.getProperty("enwUrl"));
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);

			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitUntilText("Sign up");
			waitForElementTobeVisible(ob, By.name(OR.getProperty("signup_email_texbox")), 30);
//			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
//			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).sendKeys("trloginid@gmail.com");
			ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).click();
			waitUntilText("Already have an account?");
			/*String error_message1 = ob.findElement(By.cssSelector(OR.getProperty("reg_errorMessage"))).getText();
			if (error_message1.contains("Sign up")) {
				ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).sendKeys("Neon@123");
				ob.findElement(By.name(OR.getProperty("signup_firstName_textbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_firstName_textbox"))).sendKeys("Neon12");
				ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).sendKeys("Neon12");
				ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
				BrowserWaits.waitTime(3);
			}*/

			/*
			 * //ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).clear();
			 * ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).click(); //jsClick(ob,
			 * ob.findElement(By.name(OR.getProperty("signup_password_textbox"))));
			 * //ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).sendKeys("A");
			 * BrowserWaits.waitTime(6);
			 */
			/*if (!checkElementPresence_id("reg_errorMessage")) {

				test.log(LogStatus.FAIL,
						"User able to create a new TR account with an email id that has already been used");// extent
																											// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_user_able_to_create_TR_account_with_emailid_that_has_already_been_used")));// screenshot

			}

			String error_message = ob.findElement(By.cssSelector(OR.getProperty("reg_errorMessage"))).getText();
			logger.info("Error Message : " + error_message);
			BrowserWaits.waitTime(4);
			// String emailAddress=ob.findElement(By.xpath(OR.getProperty("existing_email_address"))).getText();
			// logger.info("Email Address : "+emailAddress);

			if (!compareStrings("Already have an account?", error_message)) {

				test.log(LogStatus.FAIL, "Error text is incorrect");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_incorrect_error_text")));// screenshot

			}*/
			jsClick(ob, ob.findElement(By.xpath(OR.getProperty("tryAgain"))));
//			BrowserWaits.waitTime(2);
			waitUntilText("Sign up");
			closeBrowser();

		}

		catch (Throwable t) {

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
