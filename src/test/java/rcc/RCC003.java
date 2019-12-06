package rcc;

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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class RCC003 extends TestBase {

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
			String message = this.getClass().getSimpleName() + "_Welcome_" + RandomStringUtils.randomAlphanumeric(100);
			String msg = "WElcome to my group";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER030", "RCCTESTUSERPWD030");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle, desc);
			pf.getGroupDetailsPage(ob).typeCustomMessage(msg);
			BrowserWaits.waitTime(10);
			boolean result = pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE31"));
			
			pf.getGroupDetailsPage(ob).clickOnSendInvitation();

			if (result)
				test.log(LogStatus.PASS, "Invitation has been send to the Neon user");
			else {
				test.log(LogStatus.FAIL, "Sending Invitation is failed due to user does not exist");
				throw new Exception("Sending Invitation is failed due to user does not exist");
			}
			Thread.sleep(5000);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			/**
			 * 
			 * Verifying the custom message
			 * 
			 * 
			 */
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER031", "RCCTESTUSERPWD031");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			if (pf.getGroupInvitationPage(ob).verifyCustomMessage(groupTitle, msg)) {
				test.log(LogStatus.PASS, "Logstatus has been verified successfully");
			} else {
				test.log(LogStatus.FAIL, "Mismatch in the custom message or custom message not appeared");
				throw new Exception("Mismatch in the custom message or custom message not appeared");
			}

			/**
			 * 
			 * Verifying the group details link
			 *
			 */
			if (pf.getGroupInvitationPage(ob).VerifyGroupDetailsLinkOfInvitation(groupTitle, desc)) {
				test.log(LogStatus.PASS, "Grouplink has been been verified successfully");
			} else {
				test.log(LogStatus.FAIL, "Grouplink verification failed");
				throw new Exception("Grouplink verification failed");
			}
			 

			/**
			 * 
			 * checking the follow functionality
			 *
			 */
			pf.getGroupInvitationPage(ob).validateFollowOrUnfollow(groupTitle, test);
			pf.getGroupInvitationPage(ob).validateFollowOrUnfollow(groupTitle, test);

			/**
			 * 
			 * Verifying the date
			 *
			 */
			if (pf.getGroupInvitationPage(ob).VerifytheDateandTimeofIvitation(groupTitle))
				test.log(LogStatus.PASS, "TimeCard is working fine");
			else {
				test.log(LogStatus.FAIL, "TimeCard is not working fine");
				throw new Exception("TimeCard is not working fine");
			}
			
			String expectedStr=pf.getGroupInvitationPage(ob).getGroupOwnerDetails(groupTitle);
			pf.getGroupInvitationPage(ob).clickOnGroupOwnerName(groupTitle);
			String actProfileData=pf.getProfilePageInstance(ob).getProfileTitleAndMetadata().toString();
			actProfileData=actProfileData.substring(1,actProfileData.length()-1);
			try{
			Assert.assertEquals(actProfileData.trim(), expectedStr.trim());
			test.log(LogStatus.PASS, "Group owner profile details are displayed correctly in Invitation page");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Group owner profile details mismatch");
				test.log(
						LogStatus.FAIL,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			logout();
			closeBrowser();
			pf.clearAllPageObjects();
			/**
			 * 
			 * deleting the group.
			 *
			 */
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER030", "RCCTESTUSERPWD030");
			pf.getUtility(ob).deleteGroup(groupTitle);
			test.log(LogStatus.PASS, "Created groups have been deleted successfully");
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
