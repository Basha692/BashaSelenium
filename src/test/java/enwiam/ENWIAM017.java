package enwiam;

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

public class ENWIAM017 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {

		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

	}

	
	@Test
	public void testCaseA24() throws Exception {

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
			String firstName = generateRandomName(8);
			String lastName = generateRandomName(10);
			System.out.println(firstName + " " + lastName);
			String email = createENWNewUser(firstName, lastName);
			logger.info("Email Address : " + email);
			test.log(LogStatus.INFO, " New User created");
			waitUntilText("I Agree");
			ob.findElement(By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString())).click();
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString()), 30);
			String text = ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}
			waitUntilText("Getting Started","Find","Collect");
			logoutEnw();
			test.log(LogStatus.INFO, " Attempting Login by providing wrong password");
			for (int i = 0; i < 9; i++) {
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(email);
				ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys("neon@126");
				ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
				waitUntilText("Invalid email/password. Please try again.");
			}
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys("neon@126");
			ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
			waitUntilText("Your account has been locked.");
			test.log(LogStatus.INFO, " 10 unsuccessfull login attempts");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("lock_title")), 10);
			String message = ob.findElement(By.xpath(OR.getProperty("lock_title"))).getText();
			String expectedMessage = "Your account has been locked.";
			System.out.println(message);
			try {
				Assert.assertEquals(expectedMessage, message);
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " message is displayed as expected");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, " Message is either not displayed or displaying wrong message");// extent
																											// reports
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

			ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
			try{
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("login_banner")), 8);
				}catch (Throwable t) {
					ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
				}
			if (!checkElementPresence("login_banner")) {

				test.log(LogStatus.FAIL, "User not able to logout successfully");// extent
																					// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_user_unable_to_logout_successfully")));// screenshot
				closeBrowser();

			}

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
