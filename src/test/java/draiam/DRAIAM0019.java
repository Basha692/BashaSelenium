package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
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

public class DRAIAM0019 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");

	}

	@Test
	public void testcaseDRA0010() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			String str = "Your account has been locked.";
			String evictMsg = "Your account has been evicted.";
			String locked = "";

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("sru_steam11"),
					LOGIN.getProperty("sru_fbpwd11"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getDraPageInstance(ob).clickDRALink();
			test.log(LogStatus.PASS, "STeAM Step Up Auth Modal is displayed");
			BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_USERNAME_CSS);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString())).clear();
			//waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString()), 30);
			pf.getDraPageInstance(ob).steamLockedDRA(LOGIN.getProperty("DRAUSER0019Locked"));  
			BrowserWaits.waitTime(3);  
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString()),
					30);
			locked = ob.findElement(By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString()))
					.getText();
			BrowserWaits.waitTime(2);
			if (locked.equalsIgnoreCase(str)) {
				test.log(LogStatus.PASS, "The locked string is displayed, the account got locked on DRA");
			}

			else {
				test.log(LogStatus.FAIL, "The locked string is not displayed, the account is not locked on DRA");
			}
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_OK_BUTTON_XPATH);

			BrowserWaits.waitTime(5);
			ob.navigate().to(host);
			pf.getLinkingModalsInstance(ob).clickOnSignInWithFB();
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getDraPageInstance(ob).clickDRALink();
			test.log(LogStatus.PASS, "STeAM Step Up Auth Modal is displayed");
			BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_USERNAME_CSS);
			//waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString()), 30);
			pf.getDraPageInstance(ob).loginTofbSuspended();
			String evict = ob.findElement(By.xpath(OnePObjectMap.DRA_EVICT_MSG_XPATH.toString())).getText();
			if (evict.equalsIgnoreCase(evictMsg)) {
				test.log(LogStatus.PASS, "The evicted string is displayed, the account got evicted");

			} else {
				test.log(LogStatus.FAIL, "The evicted string is not displayed");

			}
			BrowserWaits.waitTime(3);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_OK_BUTTON_XPATH);
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent

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

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
