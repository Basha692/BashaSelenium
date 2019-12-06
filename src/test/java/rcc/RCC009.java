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

public class RCC009 extends TestBase {

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
	public void verifyingInvitations() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
		try {
			String groupTitle = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			// login with Owneruser
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER040", "RCCTESTUSERPWD040");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle, desc);
			test.log(LogStatus.PASS, "Group is created by the owner ");
			BrowserWaits.waitTime(30);

			// Inviting user2
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE39"));
			pf.getGroupDetailsPage(ob).clickOnSendInvitation();
			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the Neon user1");
			else {
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to neon user1 does not exist");
				throw new Exception("Sending Invitation is failed due to user1 does not exist");
			}
			// Inviting user3
			boolean result1 = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE38"));
			pf.getGroupDetailsPage(ob).clickOnSendInvitation();

			if (result1)
				test.log(LogStatus.PASS, "Invitation has been send to the Neon user2");
			else {
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to neon user2 does not exist");
				throw new Exception("Sending Invitation is failed due to user2 does not exist");
			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			// logging in with user2 to accept invitation
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER039", "RCCTESTUSERPWD039");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			try {
				pf.getGroupInvitationPage(ob).acceptInvitation(groupTitle);
				test.log(LogStatus.INFO, "Invitation has been send to the Neon user2 and accepted");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error in sending invitation");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			// logging in with user3 to accept invitation
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER038", "RCCTESTUSERPWD038");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(groupTitle);
			test.log(LogStatus.INFO, "Invitation has been send to the Neon user3 and accepted");
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getMembersCounts(), 2);
				test.log(LogStatus.PASS, "Members count is updated correctly in group details page in members view");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"Members count is not updated correctly in group details page in members view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			// logging in with owneruser1 and check the members count in the
			// list page and details pag

			// As user 2 leave the group and check the pop up buttons
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER039", "RCCTESTUSERPWD039");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			BrowserWaits.waitTime(10);
			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);
			pf.getGroupDetailsPage(ob).clickonLeaveGroupButton();
			test.log(LogStatus.INFO, "Clicked on leave group button Pop Up opened");

			try {
				pf.getGroupDetailsPage(ob).verifyLeaveGroupPopupMessage(test);
				test.log(LogStatus.PASS, "verified on leave group popup message");

				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyleavegroupPopupButtons());
				test.log(LogStatus.PASS, "verified on leave group popup buttons");

				Assert.assertTrue(pf.getGroupDetailsPage(ob).clickonCancelButtononPopup());
				test.log(LogStatus.PASS, "Cancel button is working fine");
				BrowserWaits.waitTime(5);
				pf.getGroupDetailsPage(ob).clickonLeaveGroupButton();
				Assert.assertTrue(pf.getGroupDetailsPage(ob).clickonCloseButtononPopup());
				test.log(LogStatus.PASS, "Close button is working fine");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error in verifying the pop up");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			// leaving the group and verifying the group list
			try {
				BrowserWaits.waitTime(5);
				pf.getGroupDetailsPage(ob).clickonLeaveGroupButton();
				pf.getGroupDetailsPage(ob).clickOnLeaveGroupButtoninPopUp();
				Assert.assertFalse(pf.getGroupsListPage(ob).checkForGroup(groupTitle));
				test.log(LogStatus.PASS, "After clicking the on the leave group button, Group is not present");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error in verifying the pop up");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER038", "RCCTESTUSERPWD038");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();

			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyMembersCount(1, groupTitle));
				test.log(LogStatus.PASS, "Members count is updated correctly in group list page in member view");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Members count is not updated correctly in group list page in memner view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getMembersCounts(), 1);
				test.log(LogStatus.PASS, "Members count is updated correctly in group details page in member view");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"Members count is not updated correctly in group details page in member view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER040", "RCCTESTUSERPWD040");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();

			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyMembersCount(1, groupTitle));
				test.log(LogStatus.PASS, "Members count is updated correctly in group list page in owner view");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Members count is not updated correctly in group list page in owner view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getMembersCounts(), 1);
				test.log(LogStatus.PASS, "Members count is updated correctly in group details page in owner view");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Members count is not updated correctly in group details page in owner view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}

			try {
				Assert.assertFalse(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE39")));
				test.log(LogStatus.PASS, "Member is not available in the members tab after leaving the group");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Member is available in the members tab after leaving the group",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}

			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something went wrong");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
			closeBrowser();
		}
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
