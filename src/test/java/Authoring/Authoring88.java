package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserAction;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring88 extends TestBase {

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
	public void testSaveDrafts() throws Exception {

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
			String postString = "<strong>test</strong> mackprofanity";
			List<String> profanityWord=Arrays.asList(new String[]{"mackprofanity","<strong>","</strong>"});
			openBrowser();
			maximizeWindow();
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));
			loginAs("USERNAME16", "PASSWORD16");
			test.log(LogStatus.INFO, "Logged in to NEON");
			pf.getHFPageInstance(ob).clickOnProfileLink();
			//BrowserWaits.waitTime(10);
			pf.getHFPageInstance(ob).clickOnProfileLink();
			test.log(LogStatus.INFO, "Navigated to Profile Page");
			int postCountBefore = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountBefore);
			pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			pf.getProfilePageInstance(ob).enterPostTitle(postString);
			test.log(LogStatus.INFO, "Entered Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postString);
			test.log(LogStatus.INFO, "Entered Post Content");
			pf.getProfilePageInstance(ob).clickOnPostCancelButton();
			pf.getProfilePageInstance(ob).clickOnPostCancelKeepDraftButton();
			test.log(LogStatus.INFO, "Saved the Post as a draft");
			//BrowserWaits.waitTime(3);
			//ob.navigate().refresh();
			waitForPageLoad(ob);
			
			int postCountAfter = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountAfter);
			scrollingToElementofAPage();
			pf.getProfilePageInstance(ob).clickOnDraftPostsTab();
			String postTitle = ob
					.findElement(By
							.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.getText().trim();
			try {
				Assert.assertTrue(postCountAfter == (postCountBefore + 1) && postString.equals(postTitle));
				test.log(LogStatus.PASS, "Spam check not applied on the post and saved as draft");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Spam check is applied on the post and post is not saved as draft");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}

			//BrowserWaits.waitTime(5);
			waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()), 200);
			jsClick(ob, ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString())));
			//BrowserWaits.waitTime(5);
			pf.getProfilePageInstance(ob).clickOnPostPublishButton();
			//Added by KR
			ob.navigate().back();
			ob.navigate().back();
			
			pf.getProfilePageInstance(ob).clickPostsTab();

			pf.getProfilePageInstance(ob).clickOnFirstPost();
			try {
				
				Assert.assertFalse(pf.getpostRVPageInstance(ob).validateProfanityWordsMaskedForPostTitle(profanityWord,test));
				test.log(LogStatus.PASS, "Profanity words are masked for post title");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Spam check is not applied on the draft post while publishing it");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}
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
