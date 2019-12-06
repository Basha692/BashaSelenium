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
import util.OnePObjectMap;

public class IAM023 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
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
			openBrowser();
			maximizeWindow();
			clearCookies();
			String email = createNewUser("disco", "dancer");
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
			waitUntilText("Account");
			fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString()), 30);
			//waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString()), 30);
			//ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString())).click();
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString())));
			waitUntilText("Account");
			String beforeChangePass = ob
					.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_LAST_LOGIN_TIME_CSS.toString())).getText();
			BrowserWaits.waitTime(20);

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ACCOUNT_CHANGE_PASSWORD_LINK_CSS.toString()),
					30);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_CHANGE_PASSWORD_LINK_CSS.toString())));

			ob.findElement(
					By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_OLD_PASSWORD_FIELD_CSS.toString()))
					.sendKeys("Neon@123");
			ob.findElement(
					By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_NEW_PASSWORD_FIELD_CSS.toString()))
					.sendKeys("Neon@1234");
			ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_PAGE_CHANGE_PASSWORD_LINK_SUBMIT_BUTTON_XPATH.toString()))
					.click();
			waitUntilText("Change password");
			logout();
			ob.get("https://www.guerrillamail.com");
			waitUntilText("Project Neon password changed");
			List<WebElement> email_list = ob.findElements(By.xpath(OR.getProperty("email_list")));
			WebElement myE = email_list.get(0);
			JavascriptExecutor executor = (JavascriptExecutor) ob;
			executor.executeScript("arguments[0].click();", myE);
			waitUntilText("Project Neon password changed");
			String email_subject = ob.findElement(By.xpath(OR.getProperty("email_subject_label"))).getText();
			logger.info("Email Subject Text : " + email_subject);
			if (!StringContains(email_subject, "Project Neon password changed")) {

				test.log(LogStatus.FAIL, "Email for changing password not received");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_password_change_email_not_received")));// screenshot

			}

			ob.navigate().to(host);
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(email);
			ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).sendKeys("Neon@1234");
			ob.findElement(By.cssSelector(OR.getProperty("login_button"))).click();
			waitUntilText("Newsfeed","Watchlists","Groups");

			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString())).click();

			waitUntilText("Account");
			String beforeChangePass1 = ob
					.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_PAGE_LAST_LOGIN_TIME_CSS.toString())).getText();

			if (beforeChangePass1.equals(beforeChangePass)) {
				test.log(LogStatus.FAIL, "Last login time same");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_unable_to_login_with_changed_password")));// screenshot
			}

			logout();
			ob.quit();
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
