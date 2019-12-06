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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring62 extends TestBase {

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
	public void testEditDraftsFromModalWindow() throws Exception {

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

			loginAs("USERNAME4", "PASSWORD4");
			test.log(LogStatus.INFO, "Logged in to NEON");
			pf.getHFPageInstance(ob).clickOnProfileLink();
			test.log(LogStatus.INFO, "Navigated to Profile Page");
			if (pf.getProfilePageInstance(ob).getDraftPostsCount() == 0) {
				String tilte = "DraftPostTest" + RandomStringUtils.randomNumeric(10);
				pf.getProfilePageInstance(ob).clickOnPublishPostButton();
				pf.getProfilePageInstance(ob).enterPostTitle(tilte);
				pf.getProfilePageInstance(ob).enterPostContent(tilte);
				pf.getProfilePageInstance(ob).clickOnPostCancelButton();
				pf.getProfilePageInstance(ob).clickOnPostCancelKeepDraftButton();
			}
			pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			test.log(LogStatus.INFO, "Open Post Modal by clicking on Publish a post ");
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
