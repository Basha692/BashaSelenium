package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;


public class Authoring1 extends TestBase {
	
	//Global Variables
	static int status = 1;
	static int time = 15;

	//To be deleted
	//PageFactory pf = new PageFactory();

	// Checking whether this test case should be skipped or not
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
	public void testLoginTRAccount() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts");

		// selenium code
		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(System.getProperty("host"));
			loginAs("USERNAME15", "PASSWORD15");
			pf.getSearchResultsPageInstance(ob).searchArticle(CONFIG.getProperty("article"));
			pf.getSearchResultsPageInstance(ob).chooseArticle();
			int count = pf.getProfilePageInstance(ob).getCommentCount();
			pf.getPostCommentPageInstance(ob).enterArticleComment(CONFIG.getProperty("commentText"));
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			pf.getPostCommentPageInstance(ob).validateCommentAdd(test, count);
			pf.getPostCommentPageInstance(ob).validateViewComment(test, CONFIG.getProperty("commentText"));
			pf.getPostCommentPageInstance(ob).updateComment(test, "comment updated");
			validateUpdatedComment("comment updated");
			closeBrowser();
		} catch (Throwable e) {
			test.log(LogStatus.FAIL, "Error: Something went wrong");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
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

	
	public void validateUpdatedComment(String updatedComments) throws Exception {
		//scrollingToElementofAPage();
		String commentText = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString())).get(0)
				.getText();
		System.out.println("Commentary Text-->" + commentText);
		if (!(commentText.contains(updatedComments) && commentText.contains("EDITED"))) {
			// TestBase.test.log(LogStatus.INFO, "Snapshot below: " +
			// TestBase.test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"Entered
			// comment not added")));
			status = 2;
			throw new Exception("Updated " + updatedComments + " not present");
		} else {

			test.log(LogStatus.PASS, "Updated comment validation passed");
		}
	}

}
