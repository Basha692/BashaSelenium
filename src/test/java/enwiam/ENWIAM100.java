package enwiam;

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

public class ENWIAM100 extends TestBase {

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	@Test
	public void testcaseG100() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("enwsoclogin"));
			Assert.assertTrue(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"));
			String statuCode2 = deleteUserAccounts(LOGIN.getProperty("myacc"));
			Assert.assertTrue(statuCode2.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"));

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			openBrowser();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("enwsoclogin"),
					LOGIN.getProperty("enwsocpwd"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
			pf.getHFPageInstance(ob).clickOnEndNoteLink();
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAl_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAl_CLOSE_BUTTON_CSS.toString()))
					.click();
			test.log(LogStatus.PASS, "Linking modal has been closed");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
			pf.getHFPageInstance(ob).clickOnEndNoteLink();
			test.log(LogStatus.PASS, "User navigate to End note");
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAl_CSS.toString()), 40);
			pf.getENWReferencePageInstance(ob).yesAccount(LOGIN.getProperty("myacc"), LOGIN.getProperty("mypwd"));
			test.log(LogStatus.PASS, "User linked with steam account");
			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			} catch (Exception e) {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			}

			logoutEnw();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("enwsoclogin"),
					LOGIN.getProperty("enwsocpwd"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getLoginTRInstance(ob).checkLinking();
			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount("Facebook", LOGIN.getProperty("enwsoclogin")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).verifyLinkedAccount("neon", LOGIN.getProperty("myacc")));

			test.log(LogStatus.PASS, "Linked accounts are available in accounts page: Neon");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
			pf.getHFPageInstance(ob).clickOnEndNoteLink();
			BrowserWaits.waitTime(2);
			test.log(LogStatus.PASS, "User navigate to End note");

			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			} catch (Exception e) {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			}
			logoutEnw();
			closeBrowser();
			pf.clearAllPageObjects();

		}

		catch (Throwable t) {

			test.log(LogStatus.FAIL, "Facebook is not linked with ENW ");// extent
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
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(authoringxls, "Test Cases"
		 * , TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()),
		 * "SKIP");
		 */

	}

}
