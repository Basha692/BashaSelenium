package iam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

public class IAM013 extends TestBase {

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
	public void testcaseA13() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		// Verify that TERMS OF USE and PRIVACY STATEMENT links are working correctly in Singn In Page

		openBrowser();
		maximizeWindow();
		try {
			ob.navigate().to(host);
			waitUntilText("Sign in");
			termofUserAndPrivacyStatement();
			waitUntilText("Sign in");
			WebElement myE = ob.findElements(By.xpath("//a[contains(text(),'Privacy Statement')]")).get(0);
			myE.click();
			waitUntilText("Privacy Statement");
			/*
			 * if (!checkElementPresence("reg_PageHeading_label")) { test.log(LogStatus.FAIL,
			 * "Either PRICAY STATEMENT link is not working or the page is not getting displayed correctly");// extent
			 * // reports status = 2;// excel test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() + "_issue_with_privacyStatement_link")));// screenshot
			 * }
			 */
			ob.findElement(By.xpath(OR.getProperty("close_PageHeading_label"))).click();
			waitUntilText("Sign in");
		} catch (Throwable t) {

			test.log(LogStatus.FAIL, "Links are  not Clickable");// extent reports
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_signup_button_not_clickable")));// screenshot
			closeBrowser();
		}
		try {
			// Verify that TERMS OF USE and PRIVACY STATEMENT links are working correctly in Singn Up Page
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitUntilText("Sign up");
			termofUserAndPrivacyStatement();
			waitUntilText("Sign up");
			List<WebElement> myE1 = ob.findElements(By.xpath("//a[contains(text(),'Privacy Statement')]"));
			logger.info("" + myE1.size());
			myE1.get(0).click();
			waitUntilText("Privacy Statement");
			/*
			 * if (!checkElementPresence("reg_PageHeading_label")) { test.log(LogStatus.FAIL,
			 * "Either PRICAY STATEMENT link is not working or the page is not getting displayed correctly");// extent
			 * // reports status = 2;// excel test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() + "_issue_with_privacyStatement_link")));// screenshot
			 * }
			 */
			ob.findElement(By.xpath(OR.getProperty("close_PageHeading_label"))).click();
			waitUntilText("Sign up");

		} catch (Throwable t) {

			test.log(LogStatus.FAIL, "Sign Up Button is not Clickable");// extent reports
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_signup_button_not_clickable")));// screenshot
			closeBrowser();
		}

		try{
		ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
		ob.navigate().refresh();
		waitUntilText("Sign in");
		termofUserAndPrivacyStatement();
		waitUntilText("Sign in");
		WebElement myE2 = ob.findElements(By.xpath("//a[contains(text(),'Privacy Statement')]")).get(0);
		myE2.click();
		waitUntilText("Privacy Statement");
		/*
		 * if (!checkElementPresence("reg_PageHeading_label")) { test.log(LogStatus.FAIL,
		 * "Either PRICAY STATEMENT link is not working or the page is not getting displayed correctly");// extent //
		 * reports status = 2;// excel test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
		 * captureScreenshot(this.getClass().getSimpleName() + "_issue_with_privacyStatement_link")));// screenshot }
		 */
		ob.findElement(By.xpath(OR.getProperty("close_PageHeading_label"))).click();
		waitUntilText("Sign in");
		}catch (Throwable t) {

			test.log(LogStatus.FAIL, "Links are not Clickable");// extent reports
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_signup_button_not_clickable")));// screenshot
			closeBrowser();
		}
		try {
			// Verify that TERMS OF USE and PRIVACY STATEMENT links are working correctly in Singn Up Page
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitUntilText("Sign up");
			termofUserAndPrivacyStatement();
			waitUntilText("Sign up");
			List<WebElement> myE3 = ob.findElements(By.xpath("//a[contains(text(),'Privacy Statement')]"));
			myE3.get(0).click();
			waitUntilText("Privacy Statement");
			/*
			 * if (!checkElementPresence("reg_PageHeading_label")) { test.log(LogStatus.FAIL,
			 * "Either PRICAY STATEMENT link is not working or the page is not getting displayed correctly");// extent
			 * // reports status = 2;// excel test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() + "_issue_with_privacyStatement_link")));// screenshot
			 * }
			 */
			ob.findElement(By.xpath(OR.getProperty("close_PageHeading_label"))).click();
			waitUntilText("Sign up");

		} catch (Throwable t) {

			test.log(LogStatus.FAIL, "Sign Up Button is not Clickable");// extent reports
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_signup_button_not_clickable")));// screenshot
			closeBrowser();
		}

		closeBrowser();

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void termofUserAndPrivacyStatement() throws Exception {

		try {
			//clearCookies();

			waitForElementTobeVisible(ob, By.linkText(OR.getProperty("reg_TermsOfUse_link")), 30);

			ob.findElement(By.linkText(OR.getProperty("reg_TermsOfUse_link"))).click();
			waitUntilText("Terms of Use");

			/*
			 * String headerModalText = null; headerModalText =
			 * ob.findElement(By.xpath(OR.getProperty("reg_PageHeading_label"))).getText(); logger.info("Modal Text : "
			 * + headerModalText); if (!checkElementPresence("reg_PageHeading_label")) { test.log(LogStatus.FAIL,
			 * "Either TERMS OF USE link is not working or the page is not getting displayed correctly");// extent //
			 * reports status = 2;// excel test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() + "_issue_with_termsOfUse_link")));// screenshot }
			 */

			ob.findElement(By.xpath(OR.getProperty("close_PageHeading_label"))).click();

		} catch (Throwable t) {

			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
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
