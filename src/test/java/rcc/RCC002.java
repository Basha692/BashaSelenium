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

public class RCC002 extends TestBase {

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

		try {

			String title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER028", "RCCTESTUSERPWD028");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Group is created successfully: " + title);
			BrowserWaits.waitTime(30);
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getGroupTitle(), title);
				test.log(LogStatus.PASS, "Group title displayed in Group details page correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group title mismatch in Group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE29"));
			if (result) {
				test.log(LogStatus.PASS, "User Invited sucessfully");
			} else {
				test.log(LogStatus.FAIL, "User not Invited");
			}
			BrowserWaits.waitTime(2);
			pf.getLoginTRInstance(ob).logOutApp();
			loginAs("RCCTESTUSER029", "RCCTESTUSERPWD029");

			int countGroupsTabOverlay = pf.getGroupsPage(ob).countGroupsTabOverlay();
			logger.info("Count Groups Tab Overlay : "+countGroupsTabOverlay);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			int countGroups = pf.getGroupsPage(ob).getGroupsCount();
			logger.info("Count Groups Tab : "+countGroups);
			int countInvitaions = pf.getGroupsPage(ob).getInvitationsCount();
			logger.info("Count invitation tab : "+countInvitaions);

			String str = pf.getGroupsPage(ob).defaultselectedTab();
			try {
				Assert.assertTrue(str.contains("Invitations"));
				test.log(LogStatus.PASS, "By default Invitation tab is active state");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "By default Invitation tab is not active state");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			if (countGroupsTabOverlay == countInvitaions) {
				test.log(LogStatus.PASS, "GroupTab overlay count and invitation tab count same");
				pf.getGroupsPage(ob).acceptInvitation();
				
			} else {
				test.log(LogStatus.FAIL, "GroupTab overlay count and invitation tab count not same");
			}
			BrowserWaits.waitTime(2);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			int countGroupsTabOverlay1 = pf.getGroupsPage(ob).countGroupsTabOverlay();
			logger.info("Count Groups Tab Overlay : "+countGroupsTabOverlay1);
			int countGroups1 = pf.getGroupsPage(ob).getGroupsCount();
			logger.info("Count Groups Tab : "+countGroups1);
			int countInvitaions1 = pf.getGroupsPage(ob).getInvitationsCount();
			logger.info("Count invitation tab : "+countInvitaions1);
			if (countGroupsTabOverlay1 < countGroupsTabOverlay && countGroups1 > countGroups
					&& countInvitaions1 < countInvitaions && countInvitaions1 == countGroupsTabOverlay1) {
				test.log(LogStatus.PASS, "All tabs are working properly");
			} else {
				test.log(LogStatus.FAIL, "All tabs are not working");
			}
			BrowserWaits.waitTime(2);
			pf.getLoginTRInstance(ob).logOutApp();
			
			loginAs("RCCTESTUSER028", "RCCTESTUSERPWD028");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try{
			Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(desc, title));
			test.log(LogStatus.PASS, "Group description in Group list page is displayed correctly");
			pf.getGroupsListPage(ob).verifyItemsCount(0, title);
			test.log(LogStatus.PASS, "Items count in Group list page in Group list page is displayed correctly");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Items count in Group list page in Group list page is not displayed correctly");
				test.log(
						LogStatus.FAIL,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			int memberCount=pf.getGroupDetailsPage(ob).getMembersCounts();
			logger.info("Member Count : "+memberCount);
			if(memberCount!=0){
				test.log(LogStatus.PASS, "Group member added successfylly in members tab in group details page");
			}else{
				test.log(LogStatus.FAIL, "Group member not added successfylly in members tab in group details page");
			}
			
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			String otherMemberJoinGroup=pf.getGroupDetailsPage(ob).getPendingInvitationMessage();
			logger.info("Message : "+otherMemberJoinGroup);
			if(otherMemberJoinGroup.contains("Invite other members to join your group")){
				test.log(LogStatus.PASS, "Invite other members to join your group message displayed");
			}else{
				test.log(LogStatus.FAIL, "Invite other members to join your group message not displayed");
			}
			
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
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 * 
	 * @throws Exception
	 */
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
