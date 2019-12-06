package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
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

public class Authoring90 extends TestBase {

	static int status = 1;
	static int time = 90;
	static int totalCommentsBeforeDeletion = 0;
	static int totalCommentsAfterDeletion = 0;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");

	}

	/**
	 * Method for deleting a post with video content and checking the count in
	 * post.
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 */
	// @Test(dependsOnMethods="testAddVideosToPost")
	@Test
	public void testDeleteVideoPost() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts for data set #" + count + "--->");
		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			loginAs("LOGINUSERNAME1", "LOGINPASSWORD1");
			test.log(LogStatus.INFO, "Logged in to NEON");
			//BrowserWaits.waitTime(2);
			pf.getHFPageInstance(ob).clickOnProfileLink();
			//BrowserWaits.waitTime(5);
			test.log(LogStatus.INFO, "Navigated to Profile Page");

			int postCountBeforeDeleting = pf.getProfilePageInstance(ob).getPostsCount();
			test.log(LogStatus.INFO, "Post count before deleting a video post:" + postCountBeforeDeleting);

			pf.getProfilePageInstance(ob).clickOnFirstPost();
			//BrowserWaits.waitTime(5);
			WebElement deleteCommentButton = ob
					.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_BUTTON1_XPATH.toString()));
			JavascriptExecutor executor = (JavascriptExecutor) ob;
			executor.executeScript("arguments[0].click();", deleteCommentButton);
			//BrowserWaits.waitTime(5);
			waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_CONFIMATION_OK_BUTTON1_XPATH.toString()), 200);
			jsClick(ob, ob.findElement(
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_CONFIMATION_OK_BUTTON1_XPATH.toString())));
			waitForAjax(ob);
			String Delete_Text = ob
					.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_DELETE_POST_CONFIRMATION_XPATH.toString()))
					.getText();
			Assert.assertEquals(Delete_Text, "This post has been removed by the member.");
			//BrowserWaits.waitTime(3);
			pf.getHFPageInstance(ob).clickOnProfileLink();
			//BrowserWaits.waitTime(5);
			test.log(LogStatus.INFO, "Navigated to Profile Page");

			int postCountAfterDeleting = pf.getProfilePageInstance(ob).getPostsCount();
			test.log(LogStatus.INFO, "Post count after deleting a video post:" + postCountAfterDeleting);

			if (!(postCountBeforeDeleting > postCountAfterDeleting)) {
				test.log(LogStatus.FAIL, "Error: Video comment not deleted successfully");// extent
				// reports
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_DeletComments_not_done")));// screenshot
			}
			closeBrowser();
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Unable_to_share_the_Article")));
			closeBrowser();
		}

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}

}