package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring15 extends TestBase {

	static int status = 1;
	PageFactory pf = new PageFactory();

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
	}

	@Test
	public void testFlagInUserComments() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			loginAs("USERNAME16", "PASSWORD16");
			String PROFILE_NAME = LOGIN.getProperty("PROFILE16");
			pf.getHFPageInstance(ob).searchForText("biology");
			pf.getSearchResultsPageInstance(ob).searchForArticleWithComments();
			pf.getpostRVPageInstance(ob).loadComments();
			// pf.getLoginTRInstance(ob).logOutApp();
			int commentsCount = pf.getpostRVPageInstance(ob).clickOnFlagOfOtherUserComments(PROFILE_NAME);
			pf.getpostRVPageInstance(ob).selectReasonInFlagModal();
			pf.getpostRVPageInstance(ob).clickFlagButtonInFlagModal();
			waitForAllElementsToBePresent(ob,
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()), 80);
			List<WebElement> commentsList = ob
					.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
			try {

				Thread.sleep(6000);// Wait for flag modal to disappear
				boolean IsFlagged = commentsList.get(commentsCount)
						.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()))
						.getAttribute("class").contains("fa-flag-o");
				Assert.assertTrue(!IsFlagged);
				test.log(LogStatus.PASS, "User comment is flagged");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User comment is not flagged");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Flag_validation_for_comments_failed")));// screenshot

			}
			commentsList = ob.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));

			jsClick(ob, commentsList.get(commentsCount)
					.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString())));

			pf.getpostRVPageInstance(ob).clickCancelButtonInFlagModal();
			try {
				boolean isFlagged = commentsList.get(commentsCount)
						.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()))
						.getAttribute("class").contains("fa-flag-o");
				Assert.assertTrue(!isFlagged);
				test.log(LogStatus.PASS, "Cancel unflag action without reason selected is working fine for comments");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Cancel unflag action without reason selected verification failed");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Cancel_Flag_validation_for_comments_failed")));// screenshot

			}

			commentsList = ob.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));

			jsClick(ob, commentsList.get(commentsCount)
					.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString())));
			pf.getpostRVPageInstance(ob).selectReasonInFlagModal();

			pf.getpostRVPageInstance(ob).clickCancelButtonInFlagModal();
			try {
				boolean isFlagged = commentsList.get(commentsCount)
						.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()))
						.getAttribute("class").contains("fa-flag-o");
				Assert.assertTrue(!isFlagged);
				test.log(LogStatus.PASS, "Cancel unflag action with reason selected is working fine for comments");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Cancel unflag action verification failed");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Cancel_Flag_validation_for_comments_failed")));// screenshot

			}
			commentsList = ob.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));

			jsClick(ob, commentsList.get(commentsCount)
					.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString())));
			List<WebElement> chkBoxList = ob.findElements(
					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CHECKBOX_CSS.toString()));
			for (WebElement we : chkBoxList) {
				if (!we.isSelected()) {
					jsClick(ob, we);
					break;
				}
			}
			Thread.sleep(5000);// wait for flag modal to appear
			pf.getpostRVPageInstance(ob).clickFlagButtonInFlagModal();
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			status = 2;// excel
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
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls, "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" , TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */

	}

}
