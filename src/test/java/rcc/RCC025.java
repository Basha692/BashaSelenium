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

public class RCC025 extends TestBase {

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
			loginAs("RCCTESTUSER020", "RCCTESTUSERPWD020");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle, desc);
			test.log(LogStatus.PASS, "Group is created by the USER1 ");

			// Inviting user2
			//BrowserWaits.waitTime(25);
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE1"));
			//BrowserWaits.waitTime(25);
			pf.getGroupDetailsPage(ob).clickOnSendInvitation();
			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the Neon user1");
			else {
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to neon user1 does not exist");
				throw new Exception("Sending Invitation is failed due to user1 does not exist");
			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			// logging in with user2 to accept invitation
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER003", "RCCTESTUSERPWD003");
			int beforeusergroupcount = -1;
			try {
				pf.getGroupsPage(ob).clickOnGroupsTab();
				pf.getGroupInvitationPage(ob).acceptInvitation(groupTitle);

				test.log(LogStatus.INFO, "Invitation has been send to the Neon user2 and accepted");

				ob.navigate().refresh();
				pf.getGroupsPage(ob).clickOnGroupsTab();
				pf.getGroupsPage(ob).switchToGroupTab();
				beforeusergroupcount = pf.getGroupsPage(ob).getGroupsCount();

				Assert.assertTrue(pf.getGroupsListPage(ob).checkForGroup(groupTitle));
				test.log(LogStatus.PASS, "Group is present in the grouplist");
				pf.getLoginTRInstance(ob).logOutApp();
				closeBrowser();
				pf.clearAllPageObjects();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error in verifying the group");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			// Logging in with user1 and verifying the delete group pop up
			int beforegroupcount = -1;
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER020", "RCCTESTUSERPWD020");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();

			beforegroupcount = pf.getGroupsPage(ob).getGroupsCount();
			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			test.log(LogStatus.INFO, "Clicked on leave group button Pop Up opened");

			Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyDeleteGroupPopupMessage());
			test.log(LogStatus.PASS, "verified the delete group pop up message");

			Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyDeletegroupPopupButtons());
			test.log(LogStatus.PASS, "verified the delete group pop up buttons");

			Assert.assertTrue(pf.getGroupDetailsPage(ob).clickonCancelButtononDEleteGroupPopup());
			test.log(LogStatus.PASS, "verified the cancel button delete group pop up buttons");
			ob.navigate().refresh();
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();

			Assert.assertTrue(pf.getGroupDetailsPage(ob).clickonCloseButtononPopup());
			test.log(LogStatus.PASS, "verified the close button delete group pop up buttons");

			// deleting the group and verifying the group list
			try {
				ob.navigate().refresh();
				pf.getGroupDetailsPage(ob).clickOnDeleteButton();
				//BrowserWaits.waitTime(2);
				pf.getGroupDetailsPage(ob).clickOnDeleteGroupButtoninPopUp();

				Assert.assertFalse(pf.getGroupsListPage(ob).checkForGroup(groupTitle));
				test.log(LogStatus.PASS, "After clicking the on the delete group button, Group is not present");

				int aftergroupcount = pf.getGroupsPage(ob).getGroupsCount();

				Assert.assertEquals(beforegroupcount, aftergroupcount + 1, "Verifird the group count with user1 ");
				test.log(LogStatus.PASS, "verified the groups count after deleting");

				logout();
				closeBrowser();
				pf.clearAllPageObjects();

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error in verifying the group count after deleting ");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			// login with user2 and verifying the group count
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER003", "RCCTESTUSERPWD003");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			int afterusergroupcount = pf.getGroupsPage(ob).getGroupsCount();
			//BrowserWaits.waitTime(4);
			Assert.assertEquals(beforeusergroupcount, afterusergroupcount + 1, "Verifird the group count wit user2 ");
			test.log(LogStatus.PASS, "verified the groups count with user after deleting");

			logout();
			closeBrowser();
			pf.clearAllPageObjects();

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
