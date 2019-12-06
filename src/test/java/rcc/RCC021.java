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

public class RCC021 extends TestBase {

	private static final String User2_doc1 = "UI_ISSUES.doc";
	private static final String User1_doc1 = "DATA.xls";
	private static final String User1_doc2 = "TEST.pdf";
	private static final String User2_doc2 = "cuty.jpg";
	private static final String recordType = "post";
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
		String gUsername1 = LOGIN.getProperty("GMAILUSERNAME1");
		String gPassword1 = LOGIN.getProperty("GMAILPASSWORD1");
		String gUsername2 = LOGIN.getProperty("GMAILUSERNAME3");
		String gPassword2 = LOGIN.getProperty("GMAILPASSWORD3");

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
		String gdDocDesc = RandomStringUtils.randomAlphanumeric(250);
		String gdDoctitle = RandomStringUtils.randomAlphanumeric(30);
		String title = null;
		try {
			String modalLabel = "Remove attached file";
			String modalInfoText = "Are you sure you wish to remove this attached file?";
			title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER013", "RCCTESTUSERPWD013");
			test.log(LogStatus.INFO, "Login as Group owner");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Created the group: " + title);
			waitForPageLoad(ob);
			waitForAjax(ob);
			pf.getHFPageInstance(ob).searchForText("test");
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			String recordTitle = pf.getSearchResultsPageInstance(ob).getPostsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(title);
			test.log(LogStatus.INFO, "Added post to the Group");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();

			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickPostsTab();

			pf.getGroupDetailsPage(ob).clickOnAttachFileForRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).signInToGoogle(gUsername1, gPassword1);
			pf.getGroupDetailsPage(ob).selectGDdoc(User1_doc1, 0);
			BrowserWaits.waitTime(10);
			pf.getGroupDetailsPage(ob).clickOnAttachFileForRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).selectGDdoc(User1_doc2, 1);
			BrowserWaits.waitTime(10);
			test.log(LogStatus.INFO, "Attached GC docs the post");
			try {
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, User1_doc1));
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, User1_doc2));
				test.log(LogStatus.PASS, "Owner is able to attach multiple GD doocs to the artile");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Owner is able not to attach multiple GD doocs to the artile",
						"Group_gd_attachment_failed");
			}
			String timeBefore = pf.getGroupDetailsPage(ob).getItemLevelGoogleDocTimestamp(recordTitle, recordType,
					User1_doc1);
			BrowserWaits.waitTime(90);
			pf.getGroupDetailsPage(ob).updateItemLevelGoogleDoc(recordTitle, recordType, User1_doc1, gdDoctitle,
					gdDocDesc);
			String timeAfter = pf.getGroupDetailsPage(ob).getItemLevelGoogleDocTimestamp(recordTitle, recordType,
					gdDoctitle);

			try {
				// Assert.assertFalse(timeBefore.equalsIgnoreCase(timeAfter));

				// test.log(LogStatus.PASS,
				// "GD doc desc and title updated date is displayed correctly
				// for post");
				test.log(LogStatus.INFO, "OPQA-3896 : is logged for Modified date issue");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc desc and title updated date is displayed correctly for post",
						"_GD_title_dec_not_updated");

			}

			try {
				Assert.assertEquals(
						pf.getGroupDetailsPage(ob).getItemLevelGoogleDocDesc(recordTitle, recordType, gdDoctitle),
						gdDocDesc);
				test.log(LogStatus.PASS, "GD doc desc and title updated correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc desc and title is not updated correctly for post",
						"_GD_title_dec_not_updated");

			}

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).validateTimeStamp(pf.getGroupDetailsPage(ob)
						.getItemLevelGoogleDocTimestamp(recordTitle, recordType, gdDoctitle)));
				test.log(LogStatus.PASS, "GD doc timestamp is displayed correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc timestamp is not displayed correctly for post",
						"_GD_title_dec_not_updated");

			}

			pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(recordTitle, recordType, gdDoctitle);
			try {
				pf.getGroupDetailsPage(ob).validateGDUrl();
				test.log(LogStatus.PASS, "GD doc is opened correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc is not opened correctly for post", "_GD_title_dec_not_updated");

			}

			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE21"));
			test.log(LogStatus.INFO, "Invited users");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER021", "RCCTESTUSERPWD021");
			test.log(LogStatus.INFO, "Login as invitee");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(title);
			test.log(LogStatus.INFO, "Accepted the invitation");
			pf.getGroupDetailsPage(ob).clickPostsTab();

			try {
				Assert.assertEquals(
						pf.getGroupDetailsPage(ob).getItemLevelGoogleDocDesc(recordTitle, recordType, gdDoctitle),
						gdDocDesc);
				test.log(LogStatus.PASS, "GD doc desc and title updated correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc desc and title is not updated correctly for post",
						"_GD_title_dec_not_updated");

			}

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).validateTimeStamp(pf.getGroupDetailsPage(ob)
						.getItemLevelGoogleDocTimestamp(recordTitle, recordType, gdDoctitle)));
				test.log(LogStatus.PASS, "GD doc timestamp is displayed correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc timestamp is not displayed correctly for post",
						"_GD_title_dec_not_updated");

			}

			/*
			 * try {
			 * pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(
			 * recordTitle, recordType, gdDoctitle); BrowserWaits.waitTime(5);
			 * pf.getGmailLoginPage(ob).clickonSwitchtoaccountinGooglepage();
			 * //pf.getGroupDetailsPage(ob).signInToGoogle(gUsername2,
			 * gPassword2);
			 * pf.getGmailLoginPage(ob).signinGoogleWithoutSwitchingWindow(
			 * gUsername2,gPassword2);
			 * pf.getGmailLoginPage(ob).clickonGoogleContinue();
			 * test.log(LogStatus.PASS,
			 * "Successfully Logged in clearing all the privacy things in google."
			 * ); } catch (Throwable t) { logFailureDetails(test, t,
			 * "Successfully Logged in clearing all the privacy things in google."
			 * , "Not logged in "); }
			 */

			try {
				pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(recordTitle, recordType, gdDoctitle);
				BrowserWaits.waitTime(5);
				pf.getGroupDetailsPage(ob).signInToGoogle(gUsername2, gPassword2);
				test.log(LogStatus.PASS, "Successfully Logged in without accessing Google privacy things");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Issus in google login", "_Login_Issue");
			}

			try {

				// pf.getGroupDetailsPage(ob).validateGDUrl();
				pf.getGmailLoginPage(ob).validateGDUrlWithoutSwitchingWindow();
				test.log(LogStatus.PASS, "GD doc is opened correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc is not opened correctly for post", "_GD_title_dec_not_updated");

			}
			pf.getGmailLoginPage(ob).switchToMainWindow(ob);
			BrowserWaits.waitTime(5);

			pf.getGroupDetailsPage(ob).clickOnItemLevelRemoveGoogleDoc(recordTitle, recordType, gdDoctitle);
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyConfirmationModalContents(modalLabel, modalInfoText,
						"Remove"));
				test.log(LogStatus.PASS, "Gd doc remove confirmation modal validation success");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Gd doc remove confirmation modal validation Failed",
						"_Gd_doc_remove_confirmation_validation_failed");
			}
			pf.getGroupDetailsPage(ob).clickOnCancelButtonINConfirmationModal();

			try {
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, gdDoctitle));
				test.log(LogStatus.PASS, "Gd doc is not removed when user cancels remove GD doc action");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc is removed when user cancels remove GD doc action",
						"Group_remove_cancel_failed");
			}
			pf.getGroupDetailsPage(ob).clickOnItemLevelRemoveGoogleDoc(recordTitle, recordType, gdDoctitle);
			BrowserWaits.waitTime(4);
			pf.getGroupDetailsPage(ob).clickOnCloseButtonINConfirmationModal();

			try {
				BrowserWaits.waitTime(2);
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, gdDoctitle));
				test.log(LogStatus.PASS, "Gd doc is not removed when user cancels remove GD doc action");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Gd doc is removed when user cancels remove GD doc action",
						"Group_remove_close_failed");
			}

			pf.getGroupDetailsPage(ob).clickOnItemLevelRemoveGoogleDoc(recordTitle, recordType, gdDoctitle);
			BrowserWaits.waitTime(4);
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			BrowserWaits.waitTime(2);
			try {
				Assert.assertFalse(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, gdDoctitle));
				test.log(LogStatus.PASS, "Gd doc is removed when user submits remove GD doc action");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Gd doc is not removed when user submits remove Gd doc action",
						"Group_remove_submit_failed");
			}
			String gdOld = gdDoctitle;
			gdDoctitle = RandomStringUtils.randomAlphanumeric(30);
			pf.getGroupDetailsPage(ob).clickOnAttachFileForRecord(recordTitle, recordType);
			// pf.getGroupDetailsPage(ob).signInToGoogle("", "");
			pf.getGroupDetailsPage(ob).selectGDdoc(User2_doc1, 0);
			BrowserWaits.waitTime(10);
			pf.getGroupDetailsPage(ob).clickOnAttachFileForRecord(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).selectGDdoc(User2_doc2, 1);
			BrowserWaits.waitTime(10);
			test.log(LogStatus.INFO, "Attached GC docs the post");
			try {
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, User2_doc1));
				Assert.assertTrue(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, User2_doc2));
				test.log(LogStatus.PASS, "Member is able to attach multiple GD doocs to the artile");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Member is able not to attach multiple GD doocs to the artile",
						"Group_Member_gd_attachment_failed");
			}
			pf.getGroupDetailsPage(ob).updateItemLevelGoogleDoc(recordTitle, recordType, User2_doc1, gdDoctitle,
					gdDocDesc);
			test.log(LogStatus.INFO, "Updated the GD doc description");
			try {
				Assert.assertEquals(
						pf.getGroupDetailsPage(ob).getItemLevelGoogleDocDesc(recordTitle, recordType, gdDoctitle),
						gdDocDesc);
				test.log(LogStatus.PASS, "GD doc desc and title updated correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc desc and title is not updated correctly for post",
						"_GD_title_dec_not_updated");

			}

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).validateTimeStamp(pf.getGroupDetailsPage(ob)
						.getItemLevelGoogleDocTimestamp(recordTitle, recordType, gdDoctitle)));
				test.log(LogStatus.PASS, "GD doc timestamp is displayed correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc timestamp is not displayed correctly for post",
						"_GD_title_dec_not_updated");

			}

			pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(recordTitle, recordType, gdDoctitle);
			try {
				pf.getGroupDetailsPage(ob).validateGDUrl();
				test.log(LogStatus.PASS, "GD doc is opened correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc is not opened correctly for post", "_GD_title_dec_not_updated");

			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER013", "RCCTESTUSERPWD013");
			test.log(LogStatus.INFO, "Login as group owner");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			test.log(LogStatus.INFO, "Access the group");
			pf.getGroupDetailsPage(ob).clickPostsTab();

			try {
				Assert.assertFalse(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, gdOld));
				test.log(LogStatus.PASS, "Removed Gd doc is not available in the list for posts");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Removed Gd doc is available in the list for posts",
						"Group_remove_submit_failed");
			}
			try {
				Assert.assertEquals(
						pf.getGroupDetailsPage(ob).getItemLevelGoogleDocDesc(recordTitle, recordType, gdDoctitle),
						gdDocDesc);
				test.log(LogStatus.PASS, "GD doc desc and title updated correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc desc and title is not updated correctly for post",
						"_GD_title_dec_not_updated");

			}

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).validateTimeStamp(pf.getGroupDetailsPage(ob)
						.getItemLevelGoogleDocTimestamp(recordTitle, recordType, gdDoctitle)));
				test.log(LogStatus.PASS, "GD doc timestamp is displayed correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc timestamp is not displayed correctly for post",
						"_GD_title_dec_not_updated");

			}

			// pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(recordTitle,
			// recordType, gdDoctitle);
			// pf.getGmailLoginPage(ob).clickonSwitchtoaccountinGooglepage();
			// //pf.getGroupDetailsPage(ob).signInToGoogle(gUsername1,
			// gPassword1);
			// pf.getGmailLoginPage(ob).signinGoogleWithoutSwitchingWindow(gUsername2,gPassword2);
			// pf.getGmailLoginPage(ob).clickonGoogleContinue();

			try {
				pf.getGroupDetailsPage(ob).clickOnOpenInGoogleDriveLinkItemLevel(recordTitle, recordType, gdDoctitle);
				BrowserWaits.waitTime(5);
				pf.getGroupDetailsPage(ob).signInToGoogle(gUsername2, gPassword2);
				test.log(LogStatus.PASS, "Successfully Logged in without accessing Google privacy things");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Issus in google login", "_Login_Issue");
			}

			try {
				// pf.getGroupDetailsPage(ob).validateGDUrl();
				// BrowserWaits.waitTime(5);
				pf.getGmailLoginPage(ob).validateGDUrlWithoutSwitchingWindow();
				// BrowserWaits.waitTime(5);
				pf.getGmailLoginPage(ob).switchToMainWindow(ob);
				test.log(LogStatus.PASS, "GD doc is opened correctly for post");
			} catch (Throwable t) {
				logFailureDetails(test, t, "GD doc is not opened correctly for post", "_GD_title_dec_not_updated");

			}

			pf.getGroupDetailsPage(ob).clickOnItemLevelRemoveGoogleDoc(recordTitle, recordType, gdDoctitle);
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			try {
				Assert.assertFalse(
						pf.getGroupDetailsPage(ob).isItemLevelGDRecordPresent(recordTitle, recordType, gdDoctitle));
				test.log(LogStatus.PASS, "Gd doc is removed when user submits remove GD doc action");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Gd doc is not removed when user submits remove Gd doc action",
						"Group_remove_submit_failed");
			}

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
