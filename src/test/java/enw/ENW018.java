package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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

public class ENW018 extends TestBase {

	static int status = 1;
	String expected_URL = "Thank You";

	// Verify that the Neon specific Feedback page is displayed, When a user is navigating from Neon 
	//|| Verify that,the user's message should be sent to a configurable email box specific for Neon when user submitting the 
	//feedback from Neon version of the new "Feedback" page.
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}
	@Test
	public void testcaseENW018() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);			
			loginAs("MarketUser42", "MarketUser42PWD");
			pf.getHFPageInstance(ob).clickProfileImage();
			//BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_SEND_FEEDBACK_TO_NEONTEAM_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.NEON_SEND_FEEDBACK_TO_NEONTEAM_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString()))
					.sendKeys("Feedback sending");
			BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH,10);
			//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
			//BrowserWaits.waitTime(20);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH,20);
			String str = ob.findElement(By.xpath(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH.toString())).getText();
			try {
				Assert.assertEquals(expected_URL, str);
				test.log(LogStatus.PASS, " Feedback has  been sent successfully.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Feedback not sent successfully..");// extent
				ErrorUtil.addVerificationFailure(t); // reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "Feedback New window is not displayed and content is not matching")));// screenshot
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH,5);
			//BrowserWaits.waitTime(8);
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH.toString())).click();
			//BrowserWaits.waitTime(8);
			ReportAproblem();
			logout();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		closeBrowser();
		
	}

	private void ReportAproblem() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH);
		jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH.toString())));
		//BrowserWaits.waitTime(3);
		try {
		if (ob.findElements(By.xpath(OnePObjectMap.SEND_FEEDBACK_COUNTRY_SELECTION_XPATH.toString())).size() > 0) {
			Select Country = new Select(ob.findElement(By.xpath(OnePObjectMap.COUNTRY_SELECT_IN_NEON.toString())));
			Country.selectByVisibleText("India");
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString())).sendKeys("Feedback has been sent");
			BrowserWaits.waitTime(5);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH,10);
			String str = ob.findElement(By.xpath(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH.toString())).getText();
			Assert.assertEquals(expected_URL, str);
			test.log(LogStatus.PASS, " Market user Feedback has  been sent successfully.");
		} else {
			test.log(LogStatus.FAIL, " Market user Feedback has not been sent .");
			Assert.assertEquals(true, false);
		}				
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Feedback has not sent.");// extent
			ErrorUtil.addVerificationFailure(t); // reports
			status = 2;// excel
			try {
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "Feedback New window is not displayed and content is not matching")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// screenshot
		}
		//BrowserWaits.waitTime(8);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH,5);
		ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH.toString())).click();
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
		
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
