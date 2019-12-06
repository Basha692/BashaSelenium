package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;

public class RCC011 extends TestBase {

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
	public void testRCC011() throws Exception {

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
			String title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			String expectedInfoText = "Are you sure you want to remove this person from the group?";
			String label = "Remove from the group?";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER044", "RCCTESTUSERPWD044");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.PASS, "Group is created by the owner ");
			//BrowserWaits.waitTime(30);
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE43"));
			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the Neon user");
			else
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to user does not exist");
			//BrowserWaits.waitTime(20);
			result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE45"));
			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the another Neon user");
			else
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to user does not exist for second user");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER043", "RCCTESTUSERPWD043");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			// pf.getGroupsPage(ob).switchToInvitationTab();
			try {
				pf.getGroupInvitationPage(ob).acceptInvitation(title);
				test.log(LogStatus.PASS, "User1 has accepted the invitation");
			}

			catch (Exception e) {
				test.log(LogStatus.FAIL, "Invitation is not present in the invitation tab of user1");
				ErrorUtil.addVerificationFailure(e);
			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER045", "RCCTESTUSERPWD045");
			test.log(LogStatus.PASS, "User2 has login with valid credentials");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			// pf.getGroupsPage(ob).switchToInvitationTab();
			//BrowserWaits.waitTime(30);
			try {
				pf.getGroupInvitationPage(ob).acceptInvitation(title);
				test.log(LogStatus.PASS, "User2 has accepted the invitation");
				int count = pf.getGroupDetailsPage(ob).getMembersCounts();
				if (count == 2)
					test.log(LogStatus.PASS, "Members count in incremented ");
			}

			catch (Exception e) {
				test.log(LogStatus.FAIL, "Invitation is not present in the invitation tab of user2");
				ErrorUtil.addVerificationFailure(e);
			}
			pf.getGroupDetailsPage(ob).clickMembersTab();
			Assert.assertTrue(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE43")));
			Assert.assertTrue(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE45")));
			test.log(LogStatus.PASS, "2 members are present in the group");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER044", "RCCTESTUSERPWD044");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			test.log(LogStatus.PASS, "Owner navigate to groups details page");
			pf.getGroupDetailsPage(ob).clickMembersTab();
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_REMOVE_BUTTON_CSS);
			Assert.assertTrue(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE43")));
			Assert.assertTrue(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE45")));
			test.log(LogStatus.PASS, "Same  members are present in the group owner page");
			pf.getGroupDetailsPage(ob).removeMembers(LOGIN.getProperty("RCCPROFILE45"));
			//BrowserWaits.waitTime(3);
			test.log(LogStatus.PASS, "Remove button is clicked for a memeber");

			// Verify Custom Messages and cancel button for Cancel Invitation
			// Modal
			Assert.assertTrue(
					pf.getGroupDetailsPage(ob).verifyConfirmationModalContents(label, expectedInfoText, "Remove"),
					"Cancel Modal is displaying with Message");
			pf.getGroupDetailsPage(ob).clickOnCancelButtonINConfirmationModal();

			test.log(LogStatus.PASS, "Cancel button is working fine for closing model");
			int count = pf.getGroupDetailsPage(ob).getMembersCounts();
			if (count == 2)
				test.log(LogStatus.PASS, "Members count in dispalaying correctly ");
			else
				test.log(LogStatus.PASS, "Members count in notdisplaying correctly ");

			// Checking Cross button is working for Cancel Invitation Modal
			pf.getGroupDetailsPage(ob).removeMembers(LOGIN.getProperty("RCCPROFILE45"));
			//BrowserWaits.waitTime(3);
			pf.getGroupDetailsPage(ob).clickOnCloseButtonINConfirmationModal();
			test.log(LogStatus.PASS, "X button is working fine for closing model");
			pf.getGroupDetailsPage(ob).clickMembersTab();
			count = pf.getGroupDetailsPage(ob).getMembersCounts();
			if (count == 2)
				test.log(LogStatus.PASS, "Members count in displaying correctly ");
			else
				test.log(LogStatus.PASS, "Members count in notdisplaying correctly ");

			// Verify thet submitt button is working for Cancel Invitation Modal
			pf.getGroupDetailsPage(ob).removeMembers(LOGIN.getProperty("RCCPROFILE45"));
			//BrowserWaits.waitTime(3);
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			pf.getGroupDetailsPage(ob).clickMembersTab();
			int aftercount = pf.getGroupDetailsPage(ob).getMembersCounts();
			if (aftercount == 1)
				test.log(LogStatus.PASS, "Members count in decreased after removing the member from group ");
			else
				test.log(LogStatus.PASS,
						"Members count in not displaying correctly after removing the member from group ");
			Assert.assertFalse(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE23")));
			test.log(LogStatus.PASS, "Member is removed from the group in owner group details page");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER043", "RCCTESTUSERPWD043");
			test.log(LogStatus.PASS, "User has login with valid credentials");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyMembersCount(1,title));
				test.log(LogStatus.PASS,
						"Members count is updated correctly in group list page in member view");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"Members count is not updated correctly in group list page in memner view",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickMembersTab();
			aftercount = pf.getGroupDetailsPage(ob).getMembersCounts();
			if (aftercount == 1)
				test.log(LogStatus.PASS, "Members count in decreased after removing the member from group ");
			else
				test.log(LogStatus.PASS,
						"Members count in not displaying correctly after removing the member from group ");
			Assert.assertFalse(pf.getGroupDetailsPage(ob).checkMemberInList(LOGIN.getProperty("RCCPROFILE45")));
			test.log(LogStatus.PASS, "Member is removed from the group in members group details page");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER045", "RCCTESTUSERPWD045");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				Assert.assertFalse(pf.getGroupsListPage(ob).checkForGroup(title));
				test.log(LogStatus.PASS,
						"Group is not available in member view when he is been removed from the group");
			} catch (Throwable t) {
				logFailureDetails(test, t,
						"Group is available in member view when he is been removed from the group",
						this.getClass().getSimpleName() + "_Group_Member_count_mismatch");

			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER044", "RCCTESTUSERPWD044");
			pf.getUtility(ob).deleteGroup(title);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

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

	}

}
