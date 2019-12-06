package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

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

//Verify that the user signed in to community enabled version of Endnote and having valid Neon 
//session will be taken to Profile page seamlessly by clicking on the profilename in profile flyout
// Verify that the user signed in to community enabled version of Endnote and having valid Neon session 
//will be taken to Account page seamlessly by clicking on the Account in profile flyout
public class ENW042 extends TestBase {
	static int status = 1;
	String expectedUrl = "https://dev-stable.1p.thomsonreuters.com/#/profile/";

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}

	@Test
	public void testcaseENW042() throws Exception {
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
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("MarketUser42"),
					(LOGIN.getProperty("MarketUser42PWD")));
			BrowserWaits.waitTime(6);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HEADER_XPATH);
			String actual_result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HEADER_XPATH).getText();
			logger.info("Header Text displayed as:" + actual_result);
			logger.info("Actual result displayed as :" + actual_result
					+ " text without the hot link and not allow user to Navigate to Neon");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
			BrowserWaits.waitTime(3);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.IMAGE_USER_XPATH.toString())));
			BrowserWaits.waitTime(8);
			if (ob.getCurrentUrl().contains(expectedUrl)) {
				if (!ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).isDisplayed()) {
					test.log(LogStatus.FAIL, "Expected page is not displayed");
					Assert.assertEquals(true, false);
				} else {
					test.log(LogStatus.PASS, "Neon Home page is displayed and  Navigating to the proper Page.");
				}
			} else {
				test.log(LogStatus.FAIL, "Neon Home page is not displayed");
			}
			ob.navigate().refresh();
			BrowserWaits.waitTime(2);
			//NavigateToENW();
			logout();
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
		String expected_uRL= "https://dev-stable.1p.thomsonreuters.com/#/account?app=endnote";
		BrowserWaits.waitTime(5);
		jsClick(ob, ob.findElement(By.xpath("//span[@class='ng-binding']")));
//		BrowserWaits.waitTime(2);
//		jsClick(ob, ob.findElement(By.cssSelector("a[href='/#/bridge?app=endnote']")));
//		ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
//		ob.navigate().refresh();
//		pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("MarketUser42"),(LOGIN.getProperty("MarketUser42PWD")));
		BrowserWaits.waitTime(10);
//		try {
//			if (ob.findElements(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).size() != 0) {
//				ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).click();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
		BrowserWaits.waitTime(3);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.ENW_PROFILE_USER_ACCOUNT_LINK_CSS.toString())));
		BrowserWaits.waitTime(8);
		waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString()), 180);
		String actualEmail = ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_ACTUAL_EMAIL_XPATH.toString())).getText();
		System.out.println(actualEmail);
		try {
			Assert.assertEquals(ob.getCurrentUrl(), expected_uRL);
			Assert.assertEquals(LOGIN.getProperty("MarketUser42"), actualEmail);
			test.log(LogStatus.PASS, " Email id getting displayed in Neon Account Setting page is correct");
		}
		catch (Throwable t) {
			test.log(LogStatus.FAIL, "Email id getting displayed in Neon Account Setting page is incorrect");// extent
			// reports
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
					.getSimpleName()
					+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
			ErrorUtil.addVerificationFailure(t);
		}
		
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
