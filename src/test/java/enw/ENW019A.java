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

public class ENW019A extends TestBase {

	static int status = 1;


	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}
	@Test
	public void testcaseENW019A() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		String expected_Str = "Thank You";
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
			loginAs("NONMARKETUSEREMAIL", "NONMARKETUSERPASSWORD");
			pf.getHFPageInstance(ob).clickProfileImage();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.NEON_HELP_FEEDBACK_XPATH.toString())));
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_SEND_FEEDBACK_TO_NEONTEAM_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.NEON_SEND_FEEDBACK_TO_NEONTEAM_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString()))
					.sendKeys("Feedback sending");
			BrowserWaits.waitTime(5);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
			//BrowserWaits.waitTime(12);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH);
			String str = ob.findElement(By.xpath(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH.toString())).getText();
			try {
				Assert.assertEquals(expected_Str, str);
				test.log(LogStatus.PASS, " Non Market user Feedback has been sent successfully .");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Non Market user Feedback has been sent");// extent
				ErrorUtil.addVerificationFailure(t); // reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "Feedback New window is not displayed and content is not matching")));// screenshot
			}
			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH.toString())).click();
			//BrowserWaits.waitTime(10);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH.toString())));
			//BrowserWaits.waitTime(5);
			if (ob.findElements(By.xpath(OnePObjectMap.SEND_FEEDBACK_COUNTRY_SELECTION_XPATH.toString())).size() > 0) {
				Select Country = new Select(ob.findElement(By.xpath(OnePObjectMap.COUNTRY_SELECT_IN_NEON.toString())));
				Country.selectByVisibleText("India");
				ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString())).sendKeys("testing");
				BrowserWaits.waitTime(5);
				jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
				//BrowserWaits.waitTime(8);
				str = ob.findElement(By.xpath(OnePObjectMap.FEEDBACK_THANKU_PAGE_XPATH.toString())).getText();
				Assert.assertEquals(expected_Str, str);
				test.log(LogStatus.PASS, " Non market user support request has  been sent successfully.");
				ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_CLOSE_XPATH.toString())).click();
				//Thread.sleep(2000);
			} else {
				test.log(LogStatus.FAIL, "Non market user support request has not been sent");
			}
			logout();
			ob.navigate().refresh();
			//BrowserWaits.waitTime(2);
			NavigateToENW();
			logoutEnw();
			closeBrowser();
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
	}

	private void NavigateToENW() throws Exception {
		String header_Expected = "Thomson Reuters";
//		jsClick(ob, ob.findElement(By.cssSelector("i[class='wui-icon wui-icon--app']")));
//		BrowserWaits.waitTime(2);
//		jsClick(ob, ob.findElement(By.cssSelector("a[href='/#/bridge?app=endnote']")));
		ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
		ob.navigate().refresh();
		pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("NONMARKETUSEREMAIL"),(LOGIN.getProperty("NONMARKETUSERPASSWORD")));
		//BrowserWaits.waitTime(10);
		try {
			if (ob.findElements(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).size() != 0) {
				ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).click();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		BrowserWaits.waitTime(8);
//		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_HEADER_XPATH);
//		String actual_result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HEADER_XPATH).getText();
//		logger.info("Header Text displayed as:" + actual_result);
//		//String profileLink = ob.findElement(By.cssSelector("li[class='dropdown-header'] a")).getAttribute("class");
//		//logger.info("is profile image linked -->" + profileLink);
//		logger.info("Actual result displayed as :" + actual_result
//				+ " text without the hot link and not allow user to Navigate to Neon");
//		try {
//			Assert.assertEquals(header_Expected, actual_result);
//			//Assert.assertEquals(profileLink, "inactiveLink");
//			test.log(LogStatus.PASS, " Header Logo text is displayed properly for Non-Market users");
//		} catch (Throwable t) {
//			test.log(LogStatus.FAIL, " Header Logo text is not displayed properly for Non-Market users");// extent
//			ErrorUtil.addVerificationFailure(t);// testng reports
//			status = 2;// excel
//			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
//					this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
//		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
