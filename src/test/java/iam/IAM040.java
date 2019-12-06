package iam;

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

public class IAM040 extends TestBase {

	static int status = 1;
	static boolean fail = false;
	static boolean skip = false;
	static int time = 30;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseA26() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);
			waitUntilText("Sign in");
			checkForLoginPageValidations();
			ob.navigate().to(host+CONFIG.getProperty("appendENWAppUrl"));
			ob.navigate().refresh();
			waitUntilText("Sign in");
			checkForLoginPageValidations();
			closeBrowser();
		} catch (Throwable t) {
			status = 2;// excel-main testcase
			fail = true;// excel-dataset
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void checkForLoginPageValidations() throws InterruptedException {
		waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
		ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).click();
		ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).click();
		waitUntilText("Please enter a valid email address.");
		String emailErrorMessage = ob
				.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(0)
				.getText();
		Assert.assertTrue(emailErrorMessage.contains("Please enter a valid email address."));
		ob.navigate().refresh();
		waitUntilText("Sign in");
		waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
		ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).click();
		ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).click();
		waitUntilText("Please enter a password.");
		String passwordErrorMessage = ob
				.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(1)
				.getText();
		Assert.assertTrue(passwordErrorMessage.contains("Please enter a password."));

		ob.navigate().refresh();
		waitUntilText("Sign in");
		waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
		ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys("abcd.com");
		ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).click();
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString()), 30);
		String emailInvalidErrorMessage = ob
				.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(0)
				.getText();
		logger.info("Error message: "+emailInvalidErrorMessage);
		Assert.assertTrue(emailInvalidErrorMessage.contains("Please enter a valid email address."));
		ob.navigate().refresh();
		waitUntilText("Sign in");
		ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
		waitUntilText("Please enter a valid email address.");
		String emailErrorMessage1 = ob
				.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(0)
				.getText();
		String passwordErrorMessage1 = ob
				.findElements(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString())).get(1)
				.getText();
		Assert.assertTrue(emailErrorMessage1.contains("Please enter a valid email address."));
		Assert.assertTrue(passwordErrorMessage1.contains("Please enter a password."));
		ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
		ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys("neon.user12@tr.com");
		ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).clear();
		ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys("abcd1233");
		ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
		waitUntilText("Invalid email/password. Please try again.");
		String errorMessage = ob.findElement(By.cssSelector(OnePObjectMap.DRA_INVALIDCREDENTIALS_ERRORMSG_CSS.toString()))
				.getText();
		Assert.assertTrue(errorMessage.contains("Invalid email/password. Please try again."));
		
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
