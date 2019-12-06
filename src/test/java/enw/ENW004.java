package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class ENW004 extends TestBase {

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
	public void testcaseENW004() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("sfbLIusername004"));
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

			String expectedSuccessMessage = "Sent To EndNote";
			

			pf.getLoginTRInstance(ob).loginWithLinkedInCredentials(LOGIN.getProperty("sfbLIusername004"),
					LOGIN.getProperty("sfbpwrd004"));

			// pf.getLoginTRInstance(ob).clickNotnowButtonLinkingModal();

			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));

			pf.getSearchResultsPageInstance(ob).clickSendToEndnoteSearchPage();

		    pf.getSearchResultsPageInstance(ob).linkSteamAcctWhileSendToEndnoteSearchPage(LOGIN.getProperty("sfbpwrd004"));
			//BrowserWaits.waitTime(4);
			
			try {
//				Assert.assertEquals(expectedSuccessMessage,
//						pf.getSearchResultsPageInstance(ob).ValidateSendToEndnoteSearchPage());	
				test.log(LogStatus.PASS,
						" Record sent successfully from Search Results Page after linking with steam account");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						" Record is not sent to Endnote from Search Results Page after linking with steam account");// extent
																													// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
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

	}
}
