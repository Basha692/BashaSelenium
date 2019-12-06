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
import util.ErrorUtil;
import util.ExtentManager;

public class ENW035 extends TestBase {
	
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
	public void testcaseENW002() throws Exception {
		
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
			
			ob.get(host);
			String expectedSuccessMessage="Send To EndNote";
			
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("maxacctUserenw035"),
					LOGIN.getProperty("maxacctPwdenw035"));
			pf.getLoginTRInstance(ob).clickLogin();
			
			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));
			
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickSendToEndnoteSearchPage();
		    
			try
			{
			Assert.assertEquals(expectedSuccessMessage,pf.getSearchResultsPageInstance(ob).ValidateSendToEndnoteSearchPage());
			test.log(LogStatus.PASS,
					" Record is not sent as there is no enough space in ENW");
			}
			
			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						" Record is sent as there is no enough space in ENW");// extent
																															// reports
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_Record_not_sent")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
        
			
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
