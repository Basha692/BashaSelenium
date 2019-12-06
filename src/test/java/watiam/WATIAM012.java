package watiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
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

public class WATIAM012 extends TestBase {

	static int status = 1;
	static boolean fail = false;
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
		test = extent.startTest(tests[0], tests_dec[0]).assignCategory("WATIAM");
		test.log(LogStatus.INFO, tests[0]);

		// extent = ExtentManager.getReporter(filePath);
		// rowData = testcase.get(this.getClass().getSimpleName());
		// test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRA");

	}

	@Test
	public void testcaseDRA0010() throws Exception {

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
				test = extent.startTest(tests[i], tests_dec[i]).assignCategory("WATIAM");
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				extent.endTest(test);
			}
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			String newPassword = "Neon@1234";
			openBrowser();
			clearCookies();
			maximizeWindow();

			pf.getIamPage(ob).openGurillaMail();
			String email = pf.getIamPage(ob).getEmail();
			pf.getIamPage(ob).openCCURL("http://10.205.147.235:7270/steam-admin-app/");
			pf.getIamPage(ob).loginCustomerCare("mahesh.morsu@thomsonreuters.com", "Neon@123");
			pf.getIamPage(ob).openMenuPanel();
			pf.getIamPage(ob).clickUserManagement();
			pf.getIamPage(ob).clickCreateUser();
			pf.getIamPage(ob).closeMenuPanel();
			pf.getIamPage(ob).openMainPanel();
			pf.getIamPage(ob).registerNewUser(email);
			pf.getIamPage(ob).closeMainPanel();
			pf.getIamPage(ob).openMenuPanel();
			pf.getIamPage(ob).clickAssociateAndDisassociate();
			pf.getIamPage(ob).clickUserToClimeTicket();
			pf.getIamPage(ob).closeMenuPanel();
			pf.getIamPage(ob).openMainPanel();
			pf.getIamPage(ob).enterEmailField(email, "225202WwGo");
			pf.getIamPage(ob).closeMainPanel();
			pf.getIamPage(ob).openHeaderPanel();
			pf.getIamPage(ob).logoutCustomerCare();
			pf.getIamPage(ob).closeHeaderPanel();
			pf.getIamPage(ob).checkCCLoginPage();

			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			waitUntilText("Sign in");
			pf.getWatPageInstance(ob).loginToWAT(email, "Neon@123", test);
			pf.getWatPageInstance(ob).logoutWAT();
			waitUntilText("Sign in");

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("WAT-252", "Verify that Forgot your password? Link is clickable on WAT Landing page")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).clickForgotPasswordLink();
				test.log(LogStatus.PASS, "Forgot password? Link is clickable on EndNote landing page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Forgot password? Link is not clickable on EndNote landing page" + t);// extent
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
						.startTest("WAT-253",
								"Verify that Thomson Reuters is replaced with Clarivate Analytics to all WAT pages related forgot password&&Verify that Thomson Reuters logo is replaced with Clarivate Analytics logo.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getBrowserWaitsInstance(ob).waitUntilText("Forgot password");
				pf.getIamPage(ob).checkForgotPasswordPageCALogo();
				test.log(LogStatus.PASS, "Company name displayed successfully in forgot password page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Company name not displayed in forgot password page" + t);// extent
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
						.startTest("WAT-559",
								"Verify that 'SaR Labs' should be moved within the white area (above and centered over the 'Forgot Password' text).")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).checkWATAppname("SaR Labs");
				test.log(LogStatus.PASS, "Application name displayed successfully in forgot password page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Application name not displayed in forgot password page" + t);// extent
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
						.startTest("WAT-254",
								"Verify that the system is navigating to Forgot Password page or not, after clicking on Forgot your password? Link&Verify that,the system should support a WAT password reset workflow with the following configurations")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).validateTextInForgotPasswordPage();
				test.log(LogStatus.PASS,
						"System is navigating to Forgot Password page, after clicking on Forgot password? Link");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"System is not navigating to Forgot Password page, after clicking on Forgot password? Link"
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

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("WAT-255",
								"Verify that system should not inform user that entered email is not found.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).sendEamilToTextBox("absssaa112222cddffdd@tr.com");
				pf.getIamPage(ob).clickSendEmailButton();
				pf.getIamPage(ob).checkEmailSentText("absssaa112222cddffdd@tr.com");
				pf.getIamPage(ob).clickOkButton();
				pf.getIamPage(ob).checkLoginPage();

				test.log(LogStatus.PASS, "System not inform user that entered email is not found.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "System inform user that entered email is not found." + t);// extent
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
						.startTest("WAT-256",
								"Verify that user should be able to enter email address in Forgot password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).clickForgotPasswordLink();
				pf.getIamPage(ob).validateTextInForgotPasswordPage();
				pf.getIamPage(ob).sendEamilToTextBox(email);
				test.log(LogStatus.PASS, "User entered email address in Forgot password page successfylly.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User not entered email address in Forgot password page successfylly." + t);// extent
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
						.startTest("WAT-257",
								"Verify that forget password service should send a forgot password email when the email entered is registered in the system")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).clickSendEmailButton();
				pf.getIamPage(ob).checkEmailSentText(email);
				pf.getIamPage(ob).clickOkButton();
				pf.getIamPage(ob).checkLoginPage();
				test.log(LogStatus.PASS, "Forget password service sent email to the registered email in the system");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Forget password service not sent email to the registered email in the system" + t);// extent
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
						.startTest("WAT-258",
								"Verify that the platform password reset service should send a platform forget password email with branding that corresponds with the originating application as per wireframe")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).openGurillaMail();
				pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs password reset");
				pf.getIamPage(ob).clickReceivedMail("SaR Labs");
				pf.getIamPage(ob).checkWATApplicationName("SaR Labs");
				test.log(LogStatus.PASS,
						"User receive a email with branding that corresponds with the originating application");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User receive not email with branding that corresponds with the originating application" + t);// extent
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
						.startTest("WAT-281",
								"Verify that The support contact email in the email body is sarlabs@clarivate.com")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkEmilBodySupportContactEmail("sarlabs@clarivate.com");
				test.log(LogStatus.PASS, "Support contact email sarlabs@clarivate.com displayed in email body");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Support contact email sarlabs@clarivate.com not displayed in email body" + t);// extent
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
						.startTest("WAT-282",
								"Verify that The support contact email in the footer is sarlabs@clarivate.com")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkFooterSupportContactEmail("sarlabs@clarivate.com");
				test.log(LogStatus.PASS, "Support contact email sarlabs@clarivate.com displayed in footer");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Support contact email sarlabs@clarivate.com not displayed in footer" + t);// extent
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
						.startTest("WAT-259",
								"Verify that When the password reset token in the email is valid, upon clicking the password reset link in the the platform forget password email, the user shall be taken to the External Password Reset Page")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).clickResetYourPasswordLink();
				pf.getBrowserWaitsInstance(ob).waitUntilText("Reset your password");
				pf.getIamPage(ob).checkExternalPasswordPageText("Reset your password", "Enter a new password below");
				test.log(LogStatus.PASS, "Reset your password page is opened successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Reset your password page is not opened successfylly" + t);// extent
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
				test = extent.startTest("WAT-560", "Verify that 'SaR Labs' should be moved within the white area")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).checkForgotPasswordPageCALogo();
				pf.getIamPage(ob).checkWATAppname("SaR Labs");
				test.log(LogStatus.PASS, "Company name displayed successfully in forgot password page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Company name not displayed in forgot password page" + t);// extent
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
				test = extent
						.startTest("WAT-260",
								"Verify Password must have at least one special character from !@#$%^*()~`{}[]| in reset password page")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkTextBox("!");
				pf.getBrowserWaitsInstance(ob).waitUntilText("Reset your password");
				waitForElementTobeVisible(ob,
						By.xpath(
								"(//div[@class='row password-validator__item ng-scope'])[5]//div[@class='col-xs-1 password-validator__icon fa text-success fa-check']"),
						30);
				pf.getIamPage(ob).checkTextBox("!@#$%^*()~`{}[]|");
				waitForElementTobeVisible(ob,
						By.xpath(
								"(//div[@class='row password-validator__item ng-scope'])[5]//div[@class='col-xs-1 password-validator__icon fa text-success fa-check']"),
						30);
				test.log(LogStatus.PASS,
						"Password field allow one special character from !@#$%^*()~`{}[]| in account page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Password field not allow one special character from !@#$%^*()~`{}[]| in account page");
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
				test = extent
						.startTest("WAT-261",
								"Verify Password must contain at least one number is ALWAYS enforced in password reset page")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).checkTextBox("1");
				pf.getBrowserWaitsInstance(ob).waitUntilText("Reset your password");
				waitForElementTobeVisible(ob,
						By.xpath(
								"(//div[@class='row password-validator__item ng-scope'])[3]//div[@class='col-xs-1 password-validator__icon fa text-success fa-check']"),
						30);
				test.log(LogStatus.PASS, "Password field allowed one number");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Password field not allowed one number");
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
				test = extent
						.startTest("WAT-262",
								"Verify Password must have at least one alphabet character either upper or lower case is ALWAYS enforced in reset password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkTextBox("a");
				pf.getBrowserWaitsInstance(ob).waitUntilText("Reset your password");
				waitForElementTobeVisible(ob,
						By.xpath(
								"(//div[@class='row password-validator__item ng-scope'])[2]//div[@class='col-xs-1 password-validator__icon fa text-success fa-check']"),
						30);
				test.log(LogStatus.PASS, "Password field allowed one alphabet character");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Password field not allowed one alphabet character");
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
				test = extent
						.startTest("WAT-263",
								"Verify Password Maximum Length of 95 characters is ALWAYS enforced in reset password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				String name = "N@1";
				String maxPassword = name + generateRandomName(93);
				logger.info("Last Name : " + maxPassword);
				pf.getIamPage(ob).checkTextBox(maxPassword);
				pf.getBrowserWaitsInstance(ob).waitUntilText("Reset your password");
				waitForElementTobeVisible(ob,
						By.xpath(
								"(//div[@class='row password-validator__item ng-scope'])[6]//div[@class='col-xs-1 password-validator__icon fa color-c5-red fa-times']"),
						30);
				test.log(LogStatus.PASS, "Password field allowed only 95 characters");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Password field allowed more than 95 characters");
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
						.startTest("WAT-264",
								"Verify that External Password Reset Page should have a new password field where the user enters their new password.&& Verify that the Password minimum length of 8 characters is ALWAYS enforced in reset password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkTextBox(newPassword);
				pf.getIamPage(ob).clickResetButton();
				test.log(LogStatus.PASS, "New password text box is available in External Password reset Page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Reset your password page is not opened successfylly" + t);// extent
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
						.startTest("WAT-265",
								"Verify that when reset Password Token already used user should be taken to sign in screen")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkLoginPage();
				ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
				ob.navigate().refresh();
				pf.getWatPageInstance(ob).loginToWAT(email, newPassword, test);
				pf.getWatPageInstance(ob).logoutWAT();
				test.log(LogStatus.PASS, "New password text box is available in External Password reset Page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Reset your password page is not opened successfylly" + t);// extent
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
						.startTest("WAT-266",
								"Verify that upon successful submission of a password change, The user should receive a password change confirmation email to the user's primary email address with branding that corresponds with the application that the user completed the password change")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).openGurillaMail();
				pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs password changed");
				pf.getIamPage(ob).checkChangedPasswordMailSubject("SaR Labs");
				pf.getIamPage(ob).checkWATApplicationName("SaR Labs");
				pf.getIamPage(ob).checkEmilBodySupportContactEmail("sarlabs@clarivate.com");
				pf.getIamPage(ob).checkFooterSupportContactEmail("sarlabs@clarivate.com");
				test.log(LogStatus.PASS, "User received password changed conformation mail");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User not received password changed conformation mail" + t);// extent
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
						.startTest("WAT-267",
								"Verify that the password change confirmation email should reference the fact that credentials are shared across all products.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.navigate().to(System.getProperty("host") + CONFIG.getProperty("appendENWAppUrl"));
				pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(email, newPassword);
				logoutEnw();
				test.log(LogStatus.PASS, "User login and logout successfylly other applications");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User not login and logout successfylly other applications" + t);// extent
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
						.startTest("WAT-268",
								"Verify that when the password reset token in the email is expired or already used, upon clicking the password reset link in the the platform forget password email, the user should be taken to the External Invalid Password Reset Token Page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).openGurillaMail();
				pf.getIamPage(ob).checkAlreadyUsedMailSubject("SaR Labs");
				pf.getIamPage(ob).checkWATApplicationName("SaR Labs");
				pf.getIamPage(ob).clickResetYourPasswordLink();
				test.log(LogStatus.PASS, "User login and logout successfylly other applications");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User not login and logout successfylly other applications" + t);// extent
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
						.startTest("WAT-269",
								"Verify that the email address on the External Invalid Password Reset Token Page should be pre-populated with the email address that matches the email that the forgot password email was sent.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				waitUntilText("Password reset link has expired");
				pf.getIamPage(ob).checkInvalidPasswordResetPage();
				test.log(LogStatus.PASS, "Invalid password reset pagee is opend successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Invalid password reset pagee is opend successfylly" + t);// extent
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
						.startTest("WAT-270",
								"Verify that user who clicks the submit button on the the External Invalid Password Reset Token page, should be taken to the target application sign in page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");

				pf.getIamPage(ob).checkPrepopulatedText(email);
				pf.getIamPage(ob).clickResendEmailButton();
				test.log(LogStatus.PASS, "Invalid password reset pagee is opend successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Invalid password reset pagee is opend successfylly" + t);// extent
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

			/*try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("WAT-271",
								"Verify that when Email address is known from password reset token,error message 'The email address is prepopulated.' should be displayed and email address field should be editable")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
				pf.getIamPage(ob).sendEamilToTextBox("5j6b6y+dzzvwq8idhgf4@sharklasers.com");
				pf.getIamPage(ob).clickForgotPasswordLink();
				pf.getIamPage(ob).checkPrepopulatedText("5j6b6y+dzzvwq8idhgf4@sharklasers.com");
				pf.getIamPage(ob).clickCancelButton();
				pf.getIamPage(ob).checkLoginPage();
				test.log(LogStatus.PASS, "Email field is prepopulated.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Email field is not prepopulated" + t);// extent
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
			}*/

			try {
				extent = ExtentManager.getReporter(filePath);
				test = extent
						.startTest("WAT-272",
								"Verify that when Email address is not known from password reset token,email address field should be blank and user should be able to enter any email address")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).clickForgotPasswordLink();
				pf.getIamPage(ob).checkPrepopulatedText("");
				pf.getIamPage(ob).clickCancelButton();
				pf.getIamPage(ob).checkLoginPage();
				test.log(LogStatus.PASS, "Email field is prepopulated.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Email field is not prepopulated" + t);// extent
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
						.startTest("WAT-273",
								"Verify that error message Please enter a valid email address.should be displayed in red color when user enters email address in wrong format")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).clickForgotPasswordLink();
				pf.getIamPage(ob).sendEamilToTextBox("abcd.com");
				ob.findElement(By.cssSelector(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_PAGE_CSS.toString())).click();
				pf.getIamPage(ob).checkErrorMessage("Please enter a valid email address.");
				pf.getIamPage(ob).clickCancelButton();
				pf.getIamPage(ob).checkLoginPage();
				test.log(LogStatus.PASS, "Email field is prepopulated.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Email field is not prepopulated" + t);// extent
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
						.startTest("WAT-274",
								"Verify that error message New password should not match current password. when enter Old and New password are same in reset password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).openGurillaMail();
				BrowserWaits.waitTime(22);
				pf.getIamPage(ob).clickReceivedMail("SaR Labs");
				pf.getIamPage(ob).checkWATApplicationName("SaR Labs");
				pf.getIamPage(ob).clickResetYourPasswordLink();
				pf.getIamPage(ob).checkExternalPasswordPageText("Reset your password", "Enter a new password below");
				pf.getIamPage(ob).checkTextBox(newPassword);
				pf.getIamPage(ob).clickResetButton();
				waitUntilText("New password should not match current password");
				String str = ob
						.findElement(
								By.cssSelector(OnePObjectMap.PASSWORD_RESET_PAGE_PASSWORD_ERROR_MESSAGE_CSS.toString()))
						.getText();

				Assert.assertEquals(str, "New password should not match current password");
				test.log(LogStatus.PASS, "Valid error massage displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "New password should match current password" + t);// extent
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
						.startTest("WAT-275",
								"Verify that error message New password should not match previous 4 passwords. when enter new password match with previous four passwords in reset password page.")
						.assignCategory("WATIAM");
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution start");
				pf.getIamPage(ob).checkTextBox("Neon@123");
				pf.getIamPage(ob).clickResetButton();
				waitUntilText("New password should not match previous 4 passwords");
				String str = ob
						.findElement(
								By.cssSelector(OnePObjectMap.PASSWORD_RESET_PAGE_PASSWORD_ERROR_MESSAGE_CSS.toString()))
						.getText();
				Assert.assertEquals(str, "New password should not match previous 4 passwords");
				test.log(LogStatus.PASS, "Valid error massage displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "New password should match current password" + t);// extent
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

			ob.quit();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			extent = ExtentManager.getReporter(filePath);
			String var = rowData.getTestcaseId();
			String dec = rowData.getTestcaseDescription();
			String[] tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
			String[] tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
			logger.info(rowData.getTestcaseId());
			for (int i = 0; i < tests.length; i++) {
				logger.info(tests_dec[i]);
				test = extent.startTest(tests[i], tests_dec[i]).assignCategory("WATIAM");
				test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
						+ " User Not created, hence skiping this test case");
				extent.endTest(test);
			}
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			ErrorUtil.addVerificationFailure(t);

			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
