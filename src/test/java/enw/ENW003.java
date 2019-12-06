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

public class ENW003 extends TestBase {

	static int status = 1;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}

	@Test
	public void testcaseENW003() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("STEAMUSEREMAIL"));
			Assert.assertTrue(statuCode.equalsIgnoreCase("200"));

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			
			ob.get(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("STEAMUSEREMAIL"),
					LOGIN.getProperty("STEAMUSERPASSWORD"));
			//BrowserWaits.waitTime(5);
			pf.getLoginTRInstance(ob).closeOnBoardingModal();
			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));

			pf.getSearchResultsPageInstance(ob).clickSendToEndnoteSearchPage();

			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAl_CLOSE_BUTTON_CSS.toString()))
					.click();
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS,10);
			BrowserWaits.waitTime(8);
			pf.getSearchResultsPageInstance(ob).clickSendToEndnoteSearchPage();

			ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_STEAMLINKING_WHILE_SENDTOENW_BUTTON_CSS.toString()))
					.sendKeys("asdfg");
			BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SOCIAL_LINKING_ONBOARDING_MODAL_BUTTON_CSS,5);
			ob.findElement(By
					.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SOCIAL_LINKING_ONBOARDING_MODAL_BUTTON_CSS.toString()))
					.click();
			try {
				String expectedErrorMsg = "The email and password do not match. Please try again.";

				String actualErrorMsg = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.HOME_PROJECT_NEON_INCORRECTPWD_ERRORMSG_CSS).getText();
				Assert.assertEquals(actualErrorMsg, expectedErrorMsg);
				test.log(LogStatus.PASS,
						"Error message displayed is correct after enting wrong password while linking");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL,
						"Error message displayed is incorrect after enting wrong password while linking");
				ErrorUtil.addVerificationFailure(t);
			}
			pf.getSearchResultsPageInstance(ob).linkSteamAcctWhileSendToEndnoteSearchPage(LOGIN.getProperty("STEAMUSERPASSWORD"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

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
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS");
		 * else if(status==2) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL");
		 * else TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
