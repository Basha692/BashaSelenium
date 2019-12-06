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
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class RCC013 extends TestBase {

	static int status = 1;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
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
	 * @throws Exception, When TR Login screen not displayed
	 */
	@Test
	public void verifyingGroupDetails() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
					+ " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
		try {

			String title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			String newtitle = this.getClass().getSimpleName() + "_UPDATED_Group_" + "_" + getCurrentTimeStamp();
			String newdesc = this.getClass().getSimpleName() + "_UPDATED_Group_"
					+ RandomStringUtils.randomAlphanumeric(100);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER047", "RCCTESTUSERPWD047");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.PASS, "Group is created by the USER1 ");
			//BrowserWaits.waitTime(30);
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE46"));
			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the USER2");
			else
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to user does not exist");

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER046", "RCCTESTUSERPWD046");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			// pf.getGroupsPage(ob).switchToInvitationTab();
			try {
				pf.getGroupInvitationPage(ob).acceptInvitation(title);
				test.log(LogStatus.PASS, "USER2 has accepted the invitation");
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
			loginAs("RCCTESTUSER047", "RCCTESTUSERPWD047");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			waitForAjax(ob);
			// pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			test.log(LogStatus.PASS, "USER1 navigate to groups details page");
			pf.getGroupDetailsPage(ob).clickOnEditButton();
			pf.getGroupDetailsPage(ob).updateGroupDetails(newtitle, newdesc);
			test.log(LogStatus.PASS, "Group title and description is updated by USER1");
			try{
				
			Assert.assertTrue(pf.getGroupDetailsPage(ob).isGroupInfoUpdated(newtitle, newdesc));
			test.log(LogStatus.PASS, "Group Title and description is updated in Group Details Page of user1");
			}
			catch (Throwable t) {
				logFailureDetails(test, t, "Group Title and description is not updated in Group Details Page of user1",
						"Group_Info_updated");
			}
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try
			{
			Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(newdesc, newtitle));
			test.log(LogStatus.PASS, "Group Title and Desc is updated in Group List Page for User1");
			}
			catch (Throwable t) {
				logFailureDetails(test, t, "Group Title and description is not updated in Group List Page of user1",
						"Group_Info_updated_In_Group_List_page");
			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER046", "RCCTESTUSERPWD046");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			waitForAjax(ob);
			// pf.getGroupsPage(ob).switchToGroupTab();
			try{
			
			Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(newdesc, newtitle));
			test.log(LogStatus.PASS, "Group Title and Desc is updated in Group List Page for User2");
			}
			catch (Throwable t) {
				logFailureDetails(test, t, "Group Title and description is not updated in Group List Page of user2",
						"Group_Info_updated_In_Group_List_Page");
			}
			pf.getGroupsListPage(ob).clickOnGroupTitle(newtitle);
			try{
			Assert.assertTrue(pf.getGroupDetailsPage(ob).isGroupInfoUpdated(newtitle, newdesc));
			test.log(LogStatus.PASS, "Group name and description is updated in Group Details Page for user2");
			}
			catch (Throwable t) {
				logFailureDetails(test, t, "Group Title and description is not updated in Group Details Page of user2",
						"Group_Info_updated");
			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER047", "RCCTESTUSERPWD047");
			pf.getUtility(ob).deleteGroup(newtitle);
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
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_login_not_done")));// screenshot
			closeBrowser();
		}

	}


	@AfterTest
	public void reportTestResult() throws Exception {
		extent.endTest(test);
		/*
		 * if(status==1) TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls,this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
