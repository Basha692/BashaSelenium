package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
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

public class RCC015 extends TestBase {

	private static final String recordType = "post";
	static int status = 1;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception,
	 * When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("RCC");
	}

	/**
	 * Method for wait TR Login Screen
	 * 
	 * @throws Exception,
	 *             When TR Login screen not displayed
	 */
	@Test
	public void testGroupCreation() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
		String title = null;
		try {

			title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			String infoText = "Add posts to this group by selecting the \"Add to group\" option wherever it is displayed in Project Neon.";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER048", "RCCTESTUSERPWD048");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Group is created successfully: " + title);
			//BrowserWaits.waitTime(20);
			waitForAjax(ob);
			pf.getGroupDetailsPage(ob).clickPostsTab();

			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getNoRecordsInfoText(), infoText);
				test.log(LogStatus.PASS, "No records informational text is displayed correcty in Post tab ");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "No records informational text is not displayed correcty in Post tab");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_No_Records_info_missing")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("test");
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			String recordTitle = pf.getSearchResultsPageInstance(ob).getPostsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(title);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(1, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Items count in Group list page not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Item_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getPostsCounts() == 1);
				test.log(LogStatus.PASS, "Post count is updated properly after adding the post from neon to the group");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Post count is not updated properly after adding the post from neon to the group");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Post_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupDetailsPage(ob).clickPostsTab();

			String recordDetals = pf.getGroupDetailsPage(ob).getRecordContent(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnRecordTitle(recordTitle, recordType);

			try {
				//BrowserWaits.waitTime(10);
				Assert.assertEquals(recordTitle, pf.getpostRVPageInstance(ob).getPostTitle().trim());
				test.log(LogStatus.PASS, "Post title in groups deails page is matching with record view page");
				//BrowserWaits.waitTime(10);
				Assert.assertTrue(
						pf.getpostRVPageInstance(ob).getPostContent().contains(recordDetals.substring(0, 30)));
				test.log(LogStatus.PASS, "Post content in groups deails page is matching with record view page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post details are not displayed correctly in group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Post_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			ob.navigate().back();
			pf.getGroupDetailsPage(ob).validateFollowOrUnfollow(recordTitle, recordType, test);
			pf.getGroupDetailsPage(ob).validateFollowOrUnfollow(recordTitle, recordType, test);
			String expectedStr = pf.getGroupDetailsPage(ob).getPostAuthorDetails(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickPostAuthorName(recordTitle, recordType);
			String actProfileData = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata().toString();
			actProfileData = actProfileData.substring(1, actProfileData.length() - 1);
			try {
				Assert.assertEquals(actProfileData, expectedStr);
				test.log(LogStatus.PASS, "Post author profile details are displayed correctly in Groups details page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post author profile details mismatch");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_post_author_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			ob.navigate().back();
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE49"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER049", "RCCTESTUSERPWD049");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(title);

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getPostsCounts() == 1);
				test.log(LogStatus.PASS, "Post count is updated properly after adding the post from neon to the group");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Post count is not updated properly after adding the post from neon to the group");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Post_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupDetailsPage(ob).clickPostsTab();

			recordDetals = pf.getGroupDetailsPage(ob).getRecordContent(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnRecordTitle(recordTitle, recordType);

			try {
				//BrowserWaits.waitTime(5);
				Assert.assertEquals(recordTitle, pf.getpostRVPageInstance(ob).getPostTitle().trim());
				test.log(LogStatus.PASS, "Post title in groups deails page is matching with record view page");
				//BrowserWaits.waitTime(5);
				Assert.assertTrue(pf.getpostRVPageInstance(ob).getPostContent().contains(recordDetals));
				test.log(LogStatus.PASS, "Post content in groups deails page is matching with record view page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post details are not displayed correctly in group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Post_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			ob.navigate().back();

			pf.getGroupDetailsPage(ob).validateFollowOrUnfollow(recordTitle, recordType, test);
			pf.getGroupDetailsPage(ob).validateFollowOrUnfollow(recordTitle, recordType, test);
			expectedStr = pf.getGroupDetailsPage(ob).getPostAuthorDetails(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickPostAuthorName(recordTitle, recordType);
			actProfileData = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata().toString();
			actProfileData = actProfileData.substring(1, actProfileData.length() - 1);
			ob.navigate().back();
			try {
				Assert.assertEquals(actProfileData, expectedStr);
				test.log(LogStatus.PASS, "Post author profile details are displayed correctly in Groups details page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post author profile details mismatch");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_post_author_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			//BrowserWaits.waitTime(5);
			pf.getGroupsPage(ob).clickOnGroupsLink();

			try {
				pf.getGroupsListPage(ob).verifyItemsCount(1, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Items count in Group list page not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Item_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER048", "RCCTESTUSERPWD048");
			test.log(LogStatus.INFO, "Login as Owner");
			pf.getUtility(ob).deleteGroup(title);
			test.log(LogStatus.INFO, "Deleted the group");
			pf.getLoginTRInstance(ob).logOutApp();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something went wrong");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot

		} finally {
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL
	 * or SKIP
	 * 
	 * @throws Exception
	 */
	@AfterTest
	public void reportTestResult() throws Exception {
		extent.endTest(test);
		/*
		 * if(status==1) TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()),
		 * "PASS"); else if(status==2) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()),
		 * "SKIP");
		 */
	}

}
