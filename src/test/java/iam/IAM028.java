package iam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class IAM028 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseA1() throws Exception {
		WebElement element = null;

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
			maximizeWindow();
			clearCookies();

			String first_name = "duster";
			String last_name = "man";

			boolean registationStatus = registrationForm(first_name, last_name);
			if (registationStatus) {
				waitUntilText("Sign in");
				ob.get("https://www.guerrillamail.com");
				waitUntilText("Please activate your Project Neon account");
				ob.navigate().to(host);
				waitUntilText("Sign in");
				pf.getLoginTRInstance(ob).enterTRCredentials(email, CONFIG.getProperty("defaultPassword"));
				ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
			}

			waitUntilText("Please activate your account");
			String textMessage = ob.findElement(By.cssSelector(OR.getProperty("reg_errorMessage"))).getText();
			logger.info("Text Message : " + textMessage);
			if (!textMessage.contains("Please activate your account")) {
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_your_account_not_display_activate_page")));// screenshot
				closeBrowser();
			}

			ob.findElement(By.cssSelector(OR.getProperty("resend_activation"))).click();
			waitUntilText("Thank You");
			BrowserWaits.waitTime(2);
			ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
			waitUntilText("Sign in");
			
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
			pf.getLoginTRInstance(ob).enterTRCredentials(email, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			
			String profile_name_xpath = "//img[@title='" + first_name + " " + last_name + "']";
			element = ob.findElement(By.xpath(profile_name_xpath));
			if (element == null) {

				test.log(LogStatus.FAIL, "Incorrect profile name getting displayed");// extent
																						// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_incorrect_profile_name_getting_displayed")));// screenshot

			}
			logout();
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
