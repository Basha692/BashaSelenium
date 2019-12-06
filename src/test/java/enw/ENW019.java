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

public class ENW019 extends TestBase {
	static int status = 1;

// For Market User - After submitting request on Neon feedback page by clicking "Report a problem or submit a support request" hyperlink.
	//"Your support request has been submitted." Message should be displayed.
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}
	@Test
	public void testcaseENW019() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		String expected_URL = "Thank You";
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
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH.toString())));
			//BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH);
			jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			try {
			if (ob.findElements(By.xpath(OnePObjectMap.SEND_FEEDBACK_COUNTRY_SELECTION_XPATH.toString())).size() > 0) {
				Select Country = new Select(ob.findElement(By.xpath(OnePObjectMap.COUNTRY_SELECT_IN_NEON.toString())));
				Country.selectByVisibleText("India");
				ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString())).sendKeys("Feedback has been sent");
				BrowserWaits.waitTime(5);
				//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH);
				jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
				//BrowserWaits.waitTime(7);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH);
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
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "Feedback New window is not displayed and content is not matching")));// screenshot
			}
			//BrowserWaits.waitTime(15);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH.toString())).click();
			//BrowserWaits.waitTime(4);
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
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
		closeBrowser();
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		closeBrowser();
	}

}
