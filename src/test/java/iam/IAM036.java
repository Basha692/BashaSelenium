package iam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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

public class IAM036 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseENW000010() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			String statuCode = deleteUserAccounts(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			logger.info("User Status : " + statuCode);
			if (statuCode.equalsIgnoreCase("200")) {
				logger.info("User Deleted Successfully");
			} else if (statuCode.equalsIgnoreCase("400")) {
				logger.info("User Already Deleted Successfully");
			} else {
				test.log(LogStatus.FAIL, "Bad request Error..Server down");
			}

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);

			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("LI_login_button")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("LI_login_button"))).click();
			waitForElementTobeVisible(ob, By.name(OR.getProperty("LI_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("LI_email_textBox"))).sendKeys(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			ob.findElement(By.name(OR.getProperty("LI_password_textBox")))
					.sendKeys(LOGIN.getProperty("LINKINGANDMERGINGPASS"));
			ob.findElement(By.name(OR.getProperty("LI_allowAccess_button"))).click();
			pf.getLoginTRInstance(ob).closeOnBoardingModal();

			logout();
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("FB_login_button")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("FB_login_button"))).click();
			waitUntilText("Log in to Facebook");
			waitForElementTobeVisible(ob, By.name(OR.getProperty("FB_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("FB_email_textBox"))).sendKeys(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			ob.findElement(By.name(OR.getProperty("FB_password_textBox")))
					.sendKeys(LOGIN.getProperty("LINKINGANDMERGINGPASS"));
			ob.findElement(By.name(OR.getProperty("FB_page_login_button"))).click();
			waitUntilText("Already have an account?");
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SINGIN_USING_LINKEDIN_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.SINGIN_USING_LINKEDIN_CSS.toString())).click();
			waitForElementTobeVisible(ob, By.name(OR.getProperty("LI_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("LI_email_textBox"))).sendKeys(LOGIN.getProperty("LINKINGANDMERGINGNAME"));
			ob.findElement(By.name(OR.getProperty("LI_password_textBox")))
					.sendKeys(LOGIN.getProperty("LINKINGANDMERGINGPASS"));
			ob.findElement(By.name(OR.getProperty("LI_allowAccess_button"))).click();
			waitUntilText("Newsfeed","Watchlists","Groups");

			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.ACCOUNT_LINK_CSS.toString())).click();
			waitUntilText("Account");
			List<WebElement> list = ob.findElements(By.xpath(
					"//div[@class='account-option-item ng-scope']/div[@class='account-option-item__text-container']/span"));
			Assert.assertEquals(list.get(0).getText(), list.get(1).getText());
			String str = ob.findElement(By.cssSelector("label[for='radio-1']")).getText();
			logger.info("Account Status : " + str);
			Assert.assertTrue(str.contains("Primary account"));
			logout();
			ob.close();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
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
