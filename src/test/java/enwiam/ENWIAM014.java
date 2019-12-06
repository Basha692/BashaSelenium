package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class ENWIAM014 extends TestBase {

	static int status = 1;
	String[] tests;
	String[] tests_dec;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		String var = rowData.getTestcaseId();
		String dec = rowData.getTestcaseDescription();
		tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
		tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
		test = extent.startTest(tests[0], tests_dec[0]).assignCategory("ENWIAM");
		test.log(LogStatus.INFO, tests[0]);

//		extent = ExtentManager.getReporter(filePath);
//		rowData = testcase.get(this.getClass().getSimpleName());
//		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	@Test
	public void testcaseA1() throws Exception {
		WebElement element = null;
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;// excel
			extent = ExtentManager.getReporter(filePath);
			String var = rowData.getTestcaseId();
			String dec = rowData.getTestcaseDescription();
			String[] tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
			String[] tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
			logger.info(rowData.getTestcaseId());
			for (int i = 0; i < tests.length; i++) {
				logger.info(tests_dec[i]);
				test = extent.startTest(tests[i], tests_dec[i]).assignCategory("ENWIAM");
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				extent.endTest(test);
			}
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

			String first_name = "duster";
			String last_name = "man";

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-1848",
								"Verify that,an error message should display as 'email activation',when User did'nt activate the link in that respective mail after completing the registration process in ENW.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				boolean registationStatus = registrationEnwForm(first_name, last_name);
				if (registationStatus) {
					ob.get("https://www.guerrillamail.com");
					waitUntilText("Please activate your EndNote account");
					ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
					pf.getLoginTRInstance(ob).enterTRCredentials(email, CONFIG.getProperty("defaultPassword"));
					ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
				}

				String textMessage = ob.findElement(By.cssSelector(OR.getProperty("reg_errorMessage"))).getText();
				logger.info("Text Message : " + textMessage);
				waitUntilText("Please activate your account");
				Assert.assertTrue(textMessage.contains("Please activate your account"));
				test.log(LogStatus.PASS, "Error message displayed 'Please activate your account'");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error message not displayed 'Please activate your account'");
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-3666",
								"Verify that,the system should send an email verification to the correct email address after clicking the button 'Resend Activation' on resend email verification page.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				BrowserWaits.waitTime(2);
				waitUntilText("Please activate your account"," Resend activation");
				ob.findElement(By.cssSelector(OR.getProperty("resend_activation"))).click();
				waitUntilText("Thank You","OK");
				ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
				test.log(LogStatus.PASS,
						"System send an email verification to the correct email address after clicking the 'Resend Activation' button on resend email verification page.");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"System not send an email verification to the correct email address after clicking the 'Resend Activation' button on resend email verification page.");
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}

			//boolean userAction = false;
			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-3667",
								"Verify that,after clicking the button on resend email verification,the Neon or ENW login page should display a message that informs the user as the email has been sent.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				//waitUntilText("Sign in");
				try{
					waitForElementTobeVisible(ob, By.xpath(OR.getProperty("login_banner")), 8);
					}catch (Throwable t) {
						ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
					}
				if (!checkElementPresence("login_banner")) {

					test.log(LogStatus.FAIL, "User not get login page successfully");// extent reports
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_user_unable_to_logout_successfully")));// screenshot
				}
				test.log(LogStatus.PASS,
						"After clicking the 'Resend Activation' button on email verification,the ENW login page displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"After clicking the 'Resend Activation' button on email verification,the ENW login page not displayed");
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-1849",
								"Verify that,user should sent to ENW home page after clicking the link in the ENW verification email.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				ob.get("https://www.guerrillamail.com");
				BrowserWaits.waitTime(22);
				waitUntilText("Please activate your", "account");
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("email_list")), 30);
				List<WebElement> email_list = ob.findElements(By.xpath(OR.getProperty("email_list")));
				WebElement myE = email_list.get(0);
				JavascriptExecutor executor = (JavascriptExecutor) ob;
				executor.executeScript("arguments[0].click();", myE);
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("email_body")), 30);
				WebElement email_body = ob.findElement(By.xpath(OR.getProperty("email_body")));
				List<WebElement> links = email_body.findElements(By.tagName("a"));
				ob.get(links.get(0).getAttribute("href"));
				waitUntilText("Success!");
				ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
				waitUntilText("Sign in");
				
				test.log(LogStatus.PASS,
						"User sent to ENW home page after clicking the link in the ENW verification email.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User not sent to ENW home page after clicking the link in the ENW verification email.");
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-3665",
								"Verify that system should force the users to verify their email address upon sign in to Neon or ENW with STeAM and provide a way for the user to send another email verification to the user's email address.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				
					pf.getLoginTRInstance(ob).enterTRCredentials(email, CONFIG.getProperty("defaultPassword"));
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
					
				test.log(LogStatus.PASS, "User successfylly login ENW application");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User successfylly not login ENW application");
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_AGREE_BUTTON_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_AGREE_BUTTON_CSS.toString())).click();

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()),
					30);
			String continueText = ob
					.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).getText();
			if (continueText.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}

			if (!checkElementPresence("ul_name")) {

				test.log(LogStatus.FAIL, "Newly registered user credentials are not working fine");// extent
																									// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_newly_registered_user_credentials_are_not_working_fine")));// screenshot
				closeBrowser();

			}
			waitUntilText("Getting Started","Find","Collect");
			String profile_name_xpath = "//img[@title='" + first_name + " " + last_name + "']";
			element = ob.findElement(By.xpath(profile_name_xpath));
			if (element == null) {

				test.log(LogStatus.FAIL, "Incorrect profile name getting displayed");// extent
																						// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_incorrect_profile_name_getting_displayed")));// screenshot

			}
			logoutEnw();
			closeBrowser();

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

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		// extent.endTest(test);

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
