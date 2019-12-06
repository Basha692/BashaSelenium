package Profile;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
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

public class Profile13 extends TestBase {

	static int status = 1;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Profile");
	}

	@Test
	public void testprofileSummary() throws Exception {
		String str = RandomStringUtils.randomAlphabetic(1600);
		int maxLength = 1500;

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

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

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			login();
			// waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_profile_dropdown_css")), 90);
			pf.getHFPageInstance(ob).clickProfileImage();
			// ob.findElement(By.cssSelector(OR.getProperty("tr_profile_dropdown_css"))).click();
			pf.getBrowserWaitsInstance(ob).waitUntilText("Profile");
			// ob.findElement(By.linkText(OR.getProperty("tr_profile_link"))).click();
			pf.getProfilePageInstance(ob).clickProfileLink();
			// Thread.sleep(4000);
			pf.getBrowserActionInstance(ob).scrollingPageUp();

			try {
				waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_profile_add_summary_css")), 30);
				ob.findElement(By.cssSelector(OR.getProperty("tr_profile_add_summary_css"))).click();

			} catch (Exception e1) {
				boolean profileIncomplete = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
				if (!profileIncomplete) {
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
					pf.getBrowserWaitsInstance(ob)
							.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
					pf.getBrowserWaitsInstance(ob)
							.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
				}

				waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_profile_summary_textarea_css")), 90);
				ob.findElement(By.cssSelector(OR.getProperty("tr_profile_summary_textarea_css"))).clear();
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);

				//BrowserWaits.waitTime(4);
				waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_profile_add_summary_css")), 60);
				ob.findElement(By.cssSelector(OR.getProperty("tr_profile_add_summary_css"))).click();
			}

			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_profile_summary_textarea_css")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("tr_profile_summary_textarea_css"))).sendKeys(str);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
			//BrowserWaits.waitTime(4);
			waitForElementTobeVisible(ob,By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SUMMARY_CSS.toString()), 90);
			String str3 = ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SUMMARY_CSS.toString()))
					.getText();
			try {
				Assert.assertEquals(str3.length(), maxLength);
				test.log(LogStatus.PASS, "Maximum length for add summary field validation success");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Maximum length for add summary field validation Failed");
				test.log(LogStatus.INFO, "Error--->" + t);
				status = 2;
				ErrorUtil.addVerificationFailure(t);
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "maximum length validation failed for summary field")));// screenshot

			}

			pf.getLoginTRInstance(ob).logOutApp();

			closeBrowser();

		}

		catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
			// next 3 lines to print whole testng error in report
			status = 2;// excel
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng

			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */

	}

}
