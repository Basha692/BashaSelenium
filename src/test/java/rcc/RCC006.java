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

public class RCC006 extends TestBase{

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
			loginAs("RCCTESTUSER025", "RCCTESTUSERPWD025");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Group is created successfully: " + title);
			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getGroupTitle(), title);
				test.log(LogStatus.PASS, "Group title displayed in Group details page correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group title mismatch in Group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyGroupDescription(desc),
						"Group decription is not matching in Group details page");
				test.log(LogStatus.PASS, "Group description in Group list page is displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			boolean deleteButtonStatus = pf.getGroupDetailsPage(ob).checkDeleteButtonIsDisplay();
			boolean editButtonStatus = pf.getGroupDetailsPage(ob).checkEditButtonIsDisplay();
			boolean attachFileButtonStatus = pf.getGroupDetailsPage(ob).checkAttachFileButtonIsDisplay();
			boolean inviteOtherButtonStatus = pf.getGroupDetailsPage(ob).checkInviteOthersButtonIsDisplay();
			
			
			try {
				Assert.assertTrue(deleteButtonStatus && editButtonStatus && attachFileButtonStatus && inviteOtherButtonStatus);
				test.log(LogStatus.PASS, "All Button are Displayed.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "All Button are not Displayed.");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			

			/*if (deleteButtonStatus && editButtonStatus && attachFileButtonStatus && inviteOtherButtonStatus) {
				test.log(LogStatus.PASS, "All Button are Displayed.");
			} else {
				test.log(LogStatus.FAIL, "All Button are not Displayed.");
			}*/

			String actualContent = "Note: You can link Google Drive items to share with others. For details on setting sharing rights visit Google's support center.";
			String checkContent = pf.getGroupDetailsPage(ob).checkSupportCenter();
			try {
				Assert.assertTrue(actualContent.contains(checkContent));
				test.log(LogStatus.PASS, "Support Center link is displayed successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Support Center link is not displayed successfylly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			int articleCount = pf.getGroupDetailsPage(ob).getArticlesCounts();
			int patentCount = pf.getGroupDetailsPage(ob).getPatentsCounts();
			int postCount = pf.getGroupDetailsPage(ob).getPostsCounts();
			int attachFileCount = pf.getGroupDetailsPage(ob).getAttachedFilesCounts();
			int membercount = pf.getGroupDetailsPage(ob).getMembersCounts();
			
			
			try {
				Assert.assertTrue(articleCount == 0 && patentCount == 0 && postCount == 0 && attachFileCount == 0 && membercount == 0);
				test.log(LogStatus.PASS, "All counts are displayed successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "All counts are not displayed successfylly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			
/*
			if (articleCount == 0 && patentCount == 0 && postCount == 0 && attachFileCount == 0 && membercount == 0) {
				test.log(LogStatus.PASS, "All counts are displayed successfylly");
			} else {
				test.log(LogStatus.FAIL, "All counts are not displayed successfylly");
			}*/

			
			
			boolean checkShareStatus = pf.getGroupDetailsPage(ob).checkShareStatus();
			
			try {
				Assert.assertTrue(checkShareStatus);
				test.log(LogStatus.PASS, "Shared Group is not displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Shared Group is displayed");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			/*if (!checkShareStatus) {
				test.log(LogStatus.PASS, "Shared Group is not displayed");
			} else {
				test.log(LogStatus.FAIL, "Shared Group is displayed");
			}*/

			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE20"));
			if (result) {
				test.log(LogStatus.PASS, "User Invited sucessfully");
			} else {
				test.log(LogStatus.FAIL, "User not Invited");
			}
			
			
			String ownername = pf.getGroupDetailsPage(ob).getGroupOwnerDetails();
			pf.getGroupDetailsPage(ob).clickOnGroupOwnerName();
			String recordPageOwnerName = pf.getGroupDetailsPage(ob).getRecordPageOwnerName();
			
			
			try {
				Assert.assertTrue(ownername.contains(recordPageOwnerName));
				test.log(LogStatus.PASS, "Owner name same");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Owner name not same");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			/*
			if (ownername.equals(recordPageOwnerName)) {
				test.log(LogStatus.PASS, "Owner name same");
			} else {
				test.log(LogStatus.FAIL, "Owner name not same");
			}*/

			//BrowserWaits.waitTime(2);
			pf.getLoginTRInstance(ob).logOutApp();
			loginAs("RCCTESTUSER020", "RCCTESTUSERPWD020"); 

			//BrowserWaits.waitTime(2);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).acceptInvitation();

			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getGroupTitle(), title);
				test.log(LogStatus.PASS, "Group title displayed in Group details page correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group title mismatch in Group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			boolean acceptUserDeleteButtonStatus = pf.getGroupDetailsPage(ob).checkDeleteButtonIsDisplay();
			boolean acceptUserEditButtonStatus = pf.getGroupDetailsPage(ob).checkEditButtonIsDisplay();
			boolean acceptUserInviteOtherButtonStatus = pf.getGroupDetailsPage(ob).checkInviteOthersButtonIsDisplay();
			
			
			try {
				Assert.assertFalse(acceptUserDeleteButtonStatus && acceptUserEditButtonStatus && acceptUserInviteOtherButtonStatus);
				test.log(LogStatus.PASS, "Group Details page Invite Others,edit and delete button are not displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group Details page Invite Others,edit and delete button are displayed");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyGroupDescription(desc),
						"Group decription is not matching in Group details page");
				test.log(LogStatus.PASS, "Group description in Group list page is displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			boolean checkLeaveGroupButtonStatus = pf.getGroupDetailsPage(ob).checkLeaveGroupButtonIsDisplay();
			boolean attachFileButtonStatus1 = pf.getGroupDetailsPage(ob).checkAttachFileButtonIsDisplay();

			if (checkLeaveGroupButtonStatus && attachFileButtonStatus1) {
				test.log(LogStatus.PASS, "All Button are Displayed.");
			} else {
				test.log(LogStatus.FAIL, "All Button are not Displayed.");
			}

			String actualContent1 = "Note: You can link Google Drive items to share with others. For details on setting sharing rights visit Google's support center.";
			String checkContent1 = pf.getGroupDetailsPage(ob).checkSupportCenter();
			try {
				Assert.assertTrue(actualContent1.contains(checkContent1));
				test.log(LogStatus.PASS, "Support Center link is displayed successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Support Center link is not displayed successfylly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			int articleCount1 = pf.getGroupDetailsPage(ob).getArticlesCounts();
			int patentCount1 = pf.getGroupDetailsPage(ob).getPatentsCounts();
			int postCount1 = pf.getGroupDetailsPage(ob).getPostsCounts();
			int attachFileCount1 = pf.getGroupDetailsPage(ob).getAttachedFilesCounts();
			int membercount1 = pf.getGroupDetailsPage(ob).getMembersCounts();
			
			try {
				Assert.assertTrue(articleCount1 == 0 && patentCount1 == 0 && postCount1 == 0 && attachFileCount1 == 0
						&& membercount1 == 1);
				test.log(LogStatus.PASS, "All counts are displayed successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "All counts are not displayed successfylly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			

			/*if (articleCount1 == 0 && patentCount1 == 0 && postCount1 == 0 && attachFileCount1 == 0
					&& membercount1 == 1) {
				test.log(LogStatus.PASS, "All counts are displayed successfylly");
			} else {
				test.log(LogStatus.FAIL, "All counts are not displayed successfylly");
			}*/

			boolean checkShareStatus1 = pf.getGroupDetailsPage(ob).checkShareStatus();
			
			
			try {
				Assert.assertFalse(checkShareStatus1);
				test.log(LogStatus.PASS, "Shared Group is displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Shared Group is not displayed");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			
			
			
			/*if (checkShareStatus1) {
				test.log(LogStatus.PASS, "Shared Group is not displayed");
			} else {
				test.log(LogStatus.FAIL, "Shared Group is displayed");
			}*/

			String ownername1 = pf.getGroupDetailsPage(ob).getGroupOwnerDetails();
			logger.info("Owner Name : "+ownername1);
			pf.getGroupDetailsPage(ob).clickOnGroupOwnerName();
			String recordPageOwnerName1 = pf.getGroupDetailsPage(ob).getRecordPageOwnerName();
			logger.info("Recore view page Owner Name : "+recordPageOwnerName1);
			
			try {
				Assert.assertTrue(ownername1.contains(recordPageOwnerName1));
				test.log(LogStatus.PASS, "Owner name same");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Owner name not same");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			/*if (ownername1.equals(recordPageOwnerName1)) {
				test.log(LogStatus.PASS, "Owner name same");
			} else {
				test.log(LogStatus.FAIL, "Owner name not same");
			}*/

			//BrowserWaits.waitTime(2);
			pf.getLoginTRInstance(ob).logOutApp();
			loginAs("RCCTESTUSER025", "RCCTESTUSERPWD025");
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
