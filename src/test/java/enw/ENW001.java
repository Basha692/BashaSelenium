package enw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

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

public class ENW001 extends TestBase {
	
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
	public void testcaseENW001() throws Exception {
		
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
			test.log(LogStatus.INFO, "Sending record for Market user");
			sendRecordTOENW(LOGIN.getProperty("MARKETUSERENW001"),LOGIN.getProperty("MARKETUSERPWDENW001"));
			test.log(LogStatus.INFO, "Sending record for non Market user");
			sendRecordTOENW(LOGIN.getProperty("MARKETUSERENW002"),LOGIN.getProperty("MARKETUSERPWDENW002"));
			
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

	
	private void sendRecordTOENW(String username, String password) throws Exception
	{
		ob.navigate().refresh();
		List<String> list =Arrays.asList(new String[]{"Articles","Patents","Posts"});
		String expectedSuccessMessage="Sent To EndNote";
		pf.getLoginTRInstance(ob).enterTRCredentials(username,password);
		pf.getLoginTRInstance(ob).clickLogin();
		
		pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));
		
		for(int i=0; i<list.size();i++)
		{
			String recordType=list.get(i);
			
			if (recordType.equalsIgnoreCase("Articles"))
			{
				pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
				pf.getSearchResultsPageInstance(ob).chooseArticle();
				//BrowserWaits.waitTime(4);
				
			}
			/*else if(recordType.equalsIgnoreCase("Patents"))
			{
				pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
				pf.getSearchResultsPageInstance(ob).chooseArticle();
				//BrowserWaits.waitTime(4);
			}else
			{
				pf.getSearchResultsPageInstance(ob).clickOnPostTab();
				pf.getSearchResultsPageInstance(ob).clickOnFirstPostTitle();
				//BrowserWaits.waitTime(4);
			}*/
				
		
//			pf.getBrowserWaitsInstance(ob)
//			.waitUntilElementIsDisplayed(OnePObjectMap.RECORD_VIEW_PAGE_SENDTOENDNOTE_BUTTON_CSS);
		waitForAjax(ob);
		
	
		try
		{
		Assert.assertEquals(expectedSuccessMessage,pf.getpostRVPageInstance(ob).clickSendToEndnoteRecordViewPage());
		test.log(LogStatus.PASS,
				recordType+" record sent successfully");
		}
		
		catch (Throwable t) {

			test.log(LogStatus.FAIL,
					recordType+	" record is not sent to Endnote for ");// extent
																														// reports
			status = 2;// excel
			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_Neonrecord_not_sent")));// screenshot
			ErrorUtil.addVerificationFailure(t);
		}
    
		//ob.navigate().back();
		//clearing the record from Endnote
		//pf.getENWReferencePageInstance(ob).clearRecordEndnote();
		
		}
		
		logout();
		
	}
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
