package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring76 extends TestBase {

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
			String postString = "PostCreationTest" + RandomStringUtils.randomNumeric(10);
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
			int postCountAfter = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountAfter);
			pf.getProfilePageInstance(ob).clickOnDraftPostsTab();
			String postTitle = ob
					.findElement(By
							.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.getText().trim();
			try {
				Assert.assertTrue(postCountAfter == (postCountBefore + 1) && postString.equals(postTitle));
				test.log(LogStatus.PASS,
						"Draft Post count is  incremented and post is present in saved drafts section");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Draft Post count is not incremented and post is not present after clicking keep draft button");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}

			/*pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			test.log(LogStatus.INFO, "Open Post Modal by clicking on Publish a post ");
			waitForElementTobeVisible(ob,
					By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_MODAL_DRAFTS_LINK_XPATH.toString()),
					60);
			ob.findElement(
					By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_MODAL_DRAFTS_LINK_XPATH.toString()))
					.click();
			test.log(LogStatus.INFO, "Click on link for drafts");

			String postTitleBeforeEditing = ob
					.findElement(By
							.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.getText();
			System.out.println(postTitleBeforeEditing);
			waitForAjax(ob);
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()), 40);
			ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.click();

			pf.getProfilePageInstance(ob).enterPostTitle(postString);
			test.log(LogStatus.INFO, "Edited Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postString);
			test.log(LogStatus.INFO, "Edited Post Content");

			pf.getProfilePageInstance(ob).clickOnPostCancelButton();
			pf.getProfilePageInstance(ob).clickOnPostCancelKeepDraftButton();
			// pf.getProfilePageInstance(ob).clickOnDraftPostsTab();
			waitForPageLoad(ob);
			waitForAjax(ob);
			String postTitleAfterEditing = ob
					.findElement(By
							.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.getText();
			System.out.println(postTitleAfterEditing);
			System.out.println(postString);
			try {
				Assert.assertTrue(postString.equals(postTitleAfterEditing));
				test.log(LogStatus.PASS, "Draft Post is accessed and edited successfully through post modal");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Draft Post accessing and editing failed");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}*/
			postCountBefore = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Draft Post count:" + postCountBefore);

			pf.getProfilePageInstance(ob).deleteDraftPostFromPostModal(postString);
			postCountAfter = 0;
			if (postCountBefore == 1) {

				if (!pf.getProfilePageInstance(ob).isDraftPostTabDispalyed()) {
					postCountAfter = 0;
				}

				else {

					postCountAfter = pf.getProfilePageInstance(ob).getDraftPostsCount();

				}
			} else {
				postCountAfter = pf.getProfilePageInstance(ob).getDraftPostsCount();
			}
			test.log(LogStatus.INFO, "Draft Post count:" + postCountAfter);

			try {
				Assert.assertEquals(postCountBefore - 1, postCountAfter);
				test.log(LogStatus.PASS, "Post count is decremented after the post deletion");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post count is not decremented after the post deletion");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}
			if (postCountBefore != 1) {
				try {
					Assert.assertTrue(!pf.getProfilePageInstance(ob).getAllDraftPostTitle().contains(postString));
					test.log(LogStatus.PASS, "Deleted post is not displayed under posts tab in profile");
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Deleted post is displayed under posts tab in profile");
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "Post_creation_validation_failed")));// screenshot

				}
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
