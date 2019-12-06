package iam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.TestUtil;

public class IAM010 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
		runmodes = TestUtil.getDataSetRunmodes(iamxls, this.getClass().getSimpleName());
	}

	@Test(dataProvider = "getTestData")
	public void testcaseA10(String charLength,
			String suffix,
			String error,
			String validity) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		// test the runmode of current dataset
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {

			test.log(LogStatus.INFO, "Runmode for test set data set to no " + (count + 1));
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + (count + 1));
		}

		try {

			String characterLength = charLength.substring(0, 3);
			Double d = new Double(Double.parseDouble(characterLength));
			int i = d.intValue();
			logger.info("Char Length : " + characterLength);
			test.log(LogStatus.INFO,
					this.getClass().getSimpleName() + " execution starts for data set #" + (count + 1) + "--->");
			test.log(LogStatus.INFO, characterLength + " -- " + validity);

			logger.info("Length : " + characterLength);
			String email = generateRandomName(i) + suffix;
			logger.info(email);

			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitForElementTobeVisible(ob, By.name(OR.getProperty("signup_email_texbox")), 30);
//			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
//			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("signup_password_textbox"))).click();

			if (email.contains(".com")) {
				waitForElementTobeVisible(ob, By.name(OR.getProperty("signup_email_texbox")), 30);

				JavascriptExecutor js = (JavascriptExecutor) ob;
				String text = (String) (js.executeScript("return arguments[0].value",
						ob.findElement(By.name(OR.getProperty("signup_email_texbox")))));
				
				
				//String text = ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).getText();
				logger.info("Text : " + text);

				if (validity.equalsIgnoreCase("YES")) {
					String textSignup = ob.findElement(By.xpath(OR.getProperty("signup_button"))).getText();
					try {
						Assert.assertTrue(textSignup.contains("Sign up"));
						test.log(LogStatus.PASS, "Sign up button is enabled in Singn up page");
					} catch (Throwable t) {
						test.log(LogStatus.FAIL, "Sign up button is not enabled in Singn up page" + t);// extent
						StringWriter errors = new StringWriter();
						t.printStackTrace(new PrintWriter(errors));
						// test.log(LogStatus.INFO, errors.toString()); // reports
						test.log(LogStatus.INFO, "Error--->" + t);
						ErrorUtil.addVerificationFailure(t);
						status = 2;// excel
						test.log(LogStatus.INFO, "Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName())));// screenshot
					}

				}

				else {
					String title = ob.findElement(By.xpath("//h3[@class='wui-subtitle wui-subtitle--app-signin']")).getText();
					try {
						Assert.assertTrue(title.contains("Sign up"));
						test.log(LogStatus.PASS, "Sign up Page is opened successfully");
					} catch (Throwable t) {
						test.log(LogStatus.FAIL, "Sign up Page is not opened successfully" + t);// extent
						StringWriter errors = new StringWriter();
						t.printStackTrace(new PrintWriter(errors));
						test.log(LogStatus.INFO, "Error--->" + t);
						ErrorUtil.addVerificationFailure(t);
						status = 2;// excel
						test.log(LogStatus.INFO, "Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName())));// screenshot
					}

				}
			}
			closeBrowser();

		}

		catch (Throwable t) {

			status = 2;// excel-main testcase
			fail = true;// excel-dataset
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO,
				this.getClass().getSimpleName() + " execution ends for data set #" + (count + 1) + "--->");
	}

	@AfterMethod
	public void reportDataSetResult() {
		if (skip)
			TestUtil.reportDataSetResult(iamxls, this.getClass().getSimpleName(), count + 2, "SKIP");

		else if (fail) {

			status = 2;
			TestUtil.reportDataSetResult(iamxls, this.getClass().getSimpleName(), count + 2, "FAIL");
		} else
			TestUtil.reportDataSetResult(iamxls, this.getClass().getSimpleName(), count + 2, "PASS");

		skip = false;
		fail = false;

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

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(iamxls, this.getClass().getSimpleName());
	}

}
