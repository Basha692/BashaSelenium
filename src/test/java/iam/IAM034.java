package iam;

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
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;

public class IAM034 extends TestBase {

	static int status = 1;
	String document_title = null;
	String document_url = null;

	PageFactory pf = new PageFactory();
	String postString = null;
	public int screen = 0;
	String[] tests;
	String[] tests_dec;

	@BeforeTest
	public void beforeTest() throws Exception {

		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		String var = rowData.getTestcaseId();
		String dec = rowData.getTestcaseDescription();
		tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
		tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
		test = extent.startTest(tests[0], tests_dec[0]).assignCategory("IAM");
		test.log(LogStatus.INFO, tests[0]);
	}

	@Test
	public void testcaseF20() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		logger.info("Test --" + suiteRunmode + "--" + testRunmode);
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
				test = extent.startTest(tests[i], tests_dec[i]).assignCategory("IAM");
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				extent.endTest(test);
			}
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		try {
			openBrowser();
			maximizeWindow();
			clearCookies();

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-1837",
								"Verify that 'Sign up' link should be displayed on Neon registration page .")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.get("https://www.guerrillamail.com");
				waitUntilText("Guerrilla Mail");
				email = ob.findElement(By.id(OR.getProperty("email_textBox"))).getText();
				ob.navigate().to(host);
				waitUntilText("Sign in");
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);
				ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
				String signupStatus = ob.findElement(By.xpath(OR.getProperty("signup_button")))
						.getAttribute("disabled");
				logger.info("SingUp Status : " + signupStatus);
				Assert.assertNotEquals(signupStatus, "disabled");
				test.log(LogStatus.PASS, "Sign up link displayed on Neon registration page .");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Sign up link not displayed on Neon registration page ." + t);// extent
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
						.startTest("OPQA-1838",
								"Verify that Neon registration screen should be displayed and User should be able to enter email address (required), name (required), and password (required).")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				waitForElementTobeVisible(ob, By.name(OR.getProperty("signup_email_texbox")), 30);
//				ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
//				ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).sendKeys(email);
				ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
						.sendKeys(CONFIG.getProperty("defaultPassword"));
				ob.findElement(By.name(OR.getProperty("signup_firstName_textbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_firstName_textbox"))).sendKeys("Duster");
				ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).clear();
				ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).sendKeys("man");
				test.log(LogStatus.PASS, "Required fields are enter properly");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Required fields are enter properly");
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
						.startTest("OPQA-1844",
								"Verify that the user should be able click on 'sign up' button after filling the above fields correctly.")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
				waitUntilText("Thank you");
				waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("signup_confom_sent_mail")), 30);

				String text = ob.findElement(By.cssSelector(OR.getProperty("signup_confom_sent_mail"))).getText();

				if (!StringContains(text, email)) {
					if (test != null) {
						test.log(LogStatus.FAIL, "Account activation email not sent");// extent
																						// reports
						test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
								this.getClass().getSimpleName() + "_account_activation_email_not_sent")));// screenshot
					}
				}

				test.log(LogStatus.PASS, "Able to click on 'Sign up' button after filling the required fields");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Not Able to click on 'Sign up' button after filling the required fields" + t);// extent
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
						.startTest("OPQA-1854",
								"Verify that user should get an Email verification Link on the registered Email Id .")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
				waitUntilText("Sign in");
				ob.get("https://www.guerrillamail.com");
				waitUntilText("Please activate your Project Neon account");
				List<WebElement> email_list = ob.findElements(By.xpath(OR.getProperty("email_list")));
				WebElement myE = email_list.get(0);
				JavascriptExecutor executor = (JavascriptExecutor) ob;
				executor.executeScript("arguments[0].click();", myE);
				String text = ob.findElement(By.cssSelector("h3[class='email_subject']")).getText();
				Assert.assertEquals(text, "Please activate your Project Neon account");
				test.log(LogStatus.PASS, "User get an Email verification Link on the registered Email Id");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User not get an Email verification Link on the registered Email Id" + t);// extent
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
						.startTest("OPQA-1856",
								"Verify that after clicking verification link user should get the message as' Success!You have successfully activated your account. Please sign in.'")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("email_body")), 30);
				WebElement email_body = ob.findElement(By.xpath(OR.getProperty("email_body")));
				List<WebElement> links = email_body.findElements(By.tagName("a"));

				ob.get(links.get(0).getAttribute("href"));
				waitUntilText("Success!");
				String confomMessage = ob.findElement(By.cssSelector("h2[class='login-title']")).getText();

				Assert.assertEquals(confomMessage, "Success!");
				test.log(LogStatus.PASS, "After clicking verification link user get the message as Success! Message");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"After clicking verification link user not get the message as Success! Message" + t);// extent
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
						.startTest("OPQA-1857",
								"Verify that After completion of verification process,Neon Identity should be created with steam account.")
						.assignCategory("IAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
				waitUntilText("Sign in");
				waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(email);
				ob.findElement(By.name(OR.getProperty("TR_password_textBox")))
						.sendKeys(CONFIG.getProperty("defaultPassword"));
				ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
				pf.getLoginTRInstance(ob).closeOnBoardingModal();
				test.log(LogStatus.PASS,
						"After completion of verification process,user successfylly login to Neon application");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"After completion of verification process,user successfylly login to Neon application" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			} finally {
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution end");
				extent.endTest(test);
			}

			logout();

		} catch (Throwable t) {
			if (test == null) {
				extent = ExtentManager.getReporter(filePath);
				String var = rowData.getTestcaseId();
				String dec = rowData.getTestcaseDescription();
				String[] tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
				String[] tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
				for (int i = 0; i < tests.length; i++) {
					test = extent.startTest(tests[i], tests_dec[i]).assignCategory("IAM");
					test.log(LogStatus.FAIL, "FAIL - " + t.getMessage());
					extent.endTest(test);
				}
			} else {
				test.log(LogStatus.FAIL, "Something unexpected happened" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			}
		} finally {
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		// extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(notificationxls, "Test Cases", TestUtil.getRowNum(notificationxls,
		 * this.getClass().getSimpleName()), "FAIL"); else TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
