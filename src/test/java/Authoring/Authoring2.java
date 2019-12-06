package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;

public class Authoring2 extends TestBase {

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
	}

	/**
	 * Method for validating TR Login Screen
	 * 
	 * @throws Exception, When TR Login Home screen not displaying
	 */
	@Test
	public void testAuthoringTestAccount() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts");
		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(System.getProperty("host"));
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("LOGINUSERNAME1"),
					LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getLoginTRInstance(ob).clickLogin();
			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));
			pf.getSearchResultsPageInstance(ob).selectArtcleWithComments();
			pf.getPostCommentPageInstance(ob).validateAppreciationComment(test);
			pf.getPostCommentPageInstance(ob).validateAppreciationComment(test);
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " Test execution ends ");
			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something UnExpected");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_profile_data_updation_not_done")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "SKIP");
		 */
		// closeBrowser();

	}

}
