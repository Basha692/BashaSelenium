package ipaiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IPAIAM0051 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");

	}

	@Test
	public void testcaseIPA0051() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

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
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			pf.getIpaPage(ob).steamLockedIPA(LOGIN.getProperty("IPAUSER0051LOCKED")); 
			//BrowserWaits.waitTime(2);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString()),
					30);
			locked = ob.findElement(By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString()))
					.getText();
			//BrowserWaits.waitTime(2);
			if (locked.equalsIgnoreCase(str)) {
				test.log(LogStatus.PASS, "The locked string is displayed, the account got locked on ENW");
			}

			else {
				test.log(LogStatus.FAIL, "The locked string is not displayed, the account is not locked on ENW");
			}
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_OK_BUTTON_XPATH);
			BrowserWaits.waitTime(2);
			pf.getDraPageInstance(ob).loginTofbSuspended();
			String evict = ob.findElement(By.xpath(OnePObjectMap.DRA_EVICT_MSG_XPATH.toString())).getText();
			if (evict.equalsIgnoreCase(evictMsg)) {
				test.log(LogStatus.PASS, "The evicted string is displayed, the account got evicted");

			} else {
				test.log(LogStatus.FAIL, "The evicted string is not displayed");

			}
			//BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_OK_BUTTON_XPATH);
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
			ErrorUtil.addVerificationFailure(t);

			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

}
