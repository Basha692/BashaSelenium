package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class ENWIAM010 extends TestBase {

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

		rowData = testcase.get(this.getClass().getSimpleName());
		/*extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		String var = rowData.getTestcaseId();
		String dec = rowData.getTestcaseDescription();
		tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
		tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
		test = extent.startTest(tests[0], tests_dec[0]).assignCategory("ENWIAM");
		test.log(LogStatus.INFO, tests[0]);*/
		
		
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
			logger.info("length : " + tests.length);
			logger.info("doc length : " + tests_dec.length);
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

			// String email=createNewUser(first_name, last_name);
			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-2007",
								"Verify that STeAM user is able to submit an email address and password on the ENW Landing screen.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				String email = createENWNewUser(first_name, last_name);
				logger.info("Email Address : " + email);
				//Assert.assertTrue(!email.contains(null));
				test.log(LogStatus.PASS,
						"STeAM user successfully submit an email address and password on the ENW Landing screen.");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"STeAM user not submit an email address and password on the ENW Landing screen." + t);// extent
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
						.startTest("OPQA-3652",
								"Verify that,user should receive the ENW EULA acceptance after signed into ENW for the first time.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				waitUntilText("I Agree");
				waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString()), 30);
				String agreeButton = ob.findElement(By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString()))
						.getAttribute("title");
				logger.info("Text : " + agreeButton);
				Assert.assertEquals(agreeButton, "I Agree");
				test.log(LogStatus.PASS,
						"User receive the ENW EULA acceptance after signed into ENW for the first time.");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User not receive the ENW EULA acceptance after signed into ENW for the first time." + t);// extent
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
						.startTest("OPQA-2008",
								"Verify that a user shall successfully authenticate by supplying correct STeAM credentials (email address + password), on the ENW landing screen.")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.findElement(By.cssSelector(OnePObjectMap.ENW_HOME_AGREE_CSS.toString())).click();
				waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString()), 30);
				String text = ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).getText();
				if (text.equalsIgnoreCase("Continue")) {
					ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
				}

				if (!checkElementPresence("ul_name")) {

					test.log(LogStatus.FAIL, "Newly registered user credentials are not working fine");// extent
																										// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_newly_registered_user_credentials_are_not_working_fine")));// screenshot
					closeBrowser();

				}

				// Verify that profile image using below xpath is present or not
				String profile_name_xpath = "//a[@title='" + first_name + " " + last_name + "']";
				element = ob.findElement(By.xpath(profile_name_xpath));
				if (element == null) {
					test.log(LogStatus.FAIL, "Incorrect profile name getting displayed");// extent
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_incorrect_profile_name_getting_displayed")));// screenshot

				}
				test.log(LogStatus.PASS,
						"User successfully authenticated by supplying correct STeAM credentials (email address + password), on the ENW landing screen.");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User not authenticated by supplying correct STeAM credentials (email address + password), on the ENW landing screen."
								+ t);// extent
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

			// waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_AGREE_BUTTON_CSS.toString()),
			// 30);
			// ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_AGREE_BUTTON_CSS.toString())).click();

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("OPQA-2009",
								"Verify that A user should not be allowed to sign-in to ENW if an incorrect email address and password combination is provided on the ENW landing screen")
						.assignCategory("ENWIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				waitUntilText("Getting Started","Find","Collect");
				logoutEnw();
				try{
					waitUntilText("Sign in");
				}catch (Throwable t) {
					ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
				}
				
				waitForElementTobeVisible(ob, By.name(OR.getProperty("TR_email_textBox")), 30);
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
				ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys("userendnote@gmail.com");
				ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys("Pass123!");
				ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
				waitUntilText("Invalid email/password. Please try again.");
				String status = ob
						.findElement(By.cssSelector(OnePObjectMap.DRA_INVALIDCREDENTIALS_ERRORMSG_CSS.toString()))
						.getText();
				Assert.assertEquals(status, "Invalid email/password. Please try again.");
				test.log(LogStatus.PASS,
						"User not sign-in to ENW if an incorrect email address and password combination is provided on the ENW landing screen");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User sign-in to ENW if an incorrect email address and password combination is provided on the ENW landing screen"
								+ t);// extent
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
