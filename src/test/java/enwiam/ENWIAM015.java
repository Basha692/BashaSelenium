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

public class ENWIAM015 extends TestBase {

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
			String first_name = "disco";
			String last_name = "dancer";

			// 1)Create a new user
			// 2)Login with new user and logout
			openBrowser();
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			String email = createENWNewUser(first_name, last_name);
			logger.info("Email Address : " + email);
			waitUntilText("I Agree");
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString())).click();
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString()), 30);
			String text = ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}
			waitUntilText("Getting Started","Find","Collect");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString()), 30);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString())));
			waitUntilText("Account");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString())).click();
			waitUntilText("Change Password");
			String newPass = "Neon@1234";
			ob.findElement(By.cssSelector("input[id='oldpw']")).sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.cssSelector("input[id='newpw']")).sendKeys(newPass);
			ob.findElement(By.cssSelector("input[id='confirmpw']")).sendKeys(newPass);
			ob.findElement(By.cssSelector("input[class='blue_btn']")).click();
			waitUntilText("Your password has been updated.");
			String str = ob.findElement(By.cssSelector("td[class='errtext2']")).getText();
			try {
				Assert.assertTrue(str.contains("Your password has been updated."));
				test.log(LogStatus.PASS, "User Password changed successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User Password not changed successfylly");// extent
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

			logoutEnw();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys(newPass);
			ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
			try {
				String text1 = ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).getText();
				logger.info("Text : " + text1);
				if (text1.equalsIgnoreCase("Continue")) {
					ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
				}
			} catch (Exception e) {

			}

			waitUntilText("Getting Started","Find","Collect");
			if (!checkElementPresence("ul_name")) {

				test.log(LogStatus.FAIL, "Newly registered user credentials are not working fine");// extent
																									// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_newly_registered_user_credentials_are_not_working_fine")));// screenshot
				closeBrowser();

			}

			logoutEnw();
			ob.quit();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
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
