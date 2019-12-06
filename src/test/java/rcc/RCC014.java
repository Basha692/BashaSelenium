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

public class RCC014 extends TestBase {

	private static final String recordType = "article";
	static int status = 1;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception,
	 *             When Something unexpected
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
			String infoText = "Add articles to this group by selecting the \"Add to group\" option wherever it is displayed in Project Neon.";
			String modalLabel = "Remove item";
			String modalInfoText = "Are you sure you wish to remove this item from the group?";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER054", "RCCTESTUSERPWD054");
			test.log(LogStatus.INFO, "Login as Group owner");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Group is created successfully: " + title);
			//BrowserWaits.waitTime(5);
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("test");
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			String recordTitle = pf.getSearchResultsPageInstance(ob).getArticleTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(title);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE55"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER055", "RCCTESTUSERPWD055");
			test.log(LogStatus.INFO, "Login as Group member");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(title);
			pf.getGroupDetailsPage(ob).clickArticlesTab();
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(recordTitle, recordType);

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyConfirmationModalContents(modalLabel, modalInfoText,
						"Remove"));
				test.log(LogStatus.PASS, "Article remove confirmation modal validation success");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Article remove confirmation modal validation Failed",
						"_article_remove_confirmation_validation_failed");
			}
			pf.getGroupDetailsPage(ob).clickOnCancelButtonINConfirmationModal();

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getArticlesCounts() == 1);
				test.log(LogStatus.PASS, "Article count is not decreased when user cancels remove article action");
				Assert.assertTrue(pf.getGroupDetailsPage(ob).IsRecordPresent(recordTitle, recordType));
				test.log(LogStatus.PASS, "Article is not removed when user cancels remove article action");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Article is removed when user cancels remove article action",
						"Group_remove_cancel_failed");
			}
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnCloseButtonINConfirmationModal();

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getArticlesCounts() == 1);
				test.log(LogStatus.PASS, "Article count is not decreased when user closes remove aricle action");
				Assert.assertTrue(pf.getGroupDetailsPage(ob).IsRecordPresent(recordTitle, recordType));
				test.log(LogStatus.PASS, "Article is not removed when user closes remove article action");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Article is removed when user closes remove article action",
						"Group_remove_close_failed");
			}
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getArticlesCounts() == 0);
				test.log(LogStatus.PASS, "Article count is not decreased when user closes remove article action");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Article is removed when user submits remove article action",
						"Group_remove_close_failed");
			}

			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getNoRecordsInfoText(), infoText);
				test.log(LogStatus.PASS,
						"No records informational text is displayed correcty in article tab when there are no articles");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"No records informational text is not displayed correcty in article tab when there are no articles",
						"_No_Records_info_missing");

			}
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(0, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {

				logFailureDetails(test, t, "Items count in Group list page not displayed correctly",
						"_Group_Item_count_mismatch");
			}
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("biology");
			recordTitle = pf.getSearchResultsPageInstance(ob).getArticleTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(title);
			waitForAjax(ob);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER054", "RCCTESTUSERPWD054");
			test.log(LogStatus.INFO, "Login as group owner");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(1, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Items count in Group list page not displayed correctly",
						"_Group_Item_count_mismatch");

			}
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickArticlesTab();
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getArticlesCounts() == 0);
				test.log(LogStatus.PASS, "Article count is decreased when user submits remove article action");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Article is not removed when user submits remove article action",
						"Group_remove_submit_failed");
			}
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getNoRecordsInfoText(), infoText);
				test.log(LogStatus.PASS,
						"No records informational text is displayed correcty in article tab when there are no articles");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"No records informational text is not displayed correcty in article tab when there are no articles",
						"_No_Records_info_missing");

			}
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(0, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {

				logFailureDetails(test, t, "Items count in Group list page not displayed correctly",
						"_Group_Item_count_mismatch");
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER055", "RCCTESTUSERPWD055");
			test.log(LogStatus.INFO, "Login as group member");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(0, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Items count in Group list page not displayed correctly",
						"_Group_Item_count_mismatch");

			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER054", "RCCTESTUSERPWD054");
			test.log(LogStatus.INFO, "Login as Group owner");
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

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends ");
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
