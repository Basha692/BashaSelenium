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

public class RCC119 extends TestBase {

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

			// Login with user1 and create Group
			String Uname = "romanreings2016csk@gmail.com";
			String password = "India@2020";
			String title = RandomStringUtils.randomAlphanumeric(4);
			//String groupTitle = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = "This group is to check the Gmail Invitation";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER008", "RCCTESTUSERPWD008");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			try {
				Assert.assertEquals(title, pf.getGroupDetailsPage(ob).getGroupTitle());
				test.log(LogStatus.PASS, "user is able to create a new group");

			} catch (Throwable t) {
				logFailureDetails(test, t,
						"user is not able to create a new group with group name of 2 characters and without any description.",
						"_Group_creation_with_two_chars_Failed");
			}
			// Inviting user2
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			test.log(LogStatus.INFO, "Group Created and Invitation has been to sent to the User");
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCGMAILPROFILE93"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			// Login with User2Gmail and checking the Link
			openBrowser();
			clearCookies();
			maximizeWindow();

			test.log(LogStatus.INFO, "Logging in with Gmail and Verifying View Invitation Link");
			pf.getGmailLoginPage(ob).gmailLogin(Uname, password);
			pf.getGmailLoginPage(ob).mailProtectclick();
			pf.getGmailLoginPage(ob).clickonMail();
			//BrowserWaits.waitTime(5);
			try {
				Assert.assertTrue(pf.getGmailLoginPage(ob).verifyEmailSubject());
				test.log(LogStatus.PASS, "Email Subject Verification Success");
			} catch (Throwable t) {
				logFailureDetails(test, t, "failed to verify the Email Subject Verification",
						"Subject Verification Failed");
			}
			try {
				pf.getGmailLoginPage(ob).clickViewInvitationLink();
				test.log(LogStatus.PASS, "Clicked on View Invitation Link");
			} catch (Throwable t) {
				logFailureDetails(test, t, "failed to Click on the Link", "Failed to Click on the Link");
			}
			pf.getGmailLoginPage(ob).MailWindowHandle();
			// Login with main user and accept the invitation.
			test.log(LogStatus.INFO, "logging in with user and verifying the Invitation");
			loginAs("RCCGMAILUSER", "RCCGMAILPASSWORD");
			//BrowserWaits.waitTime(5);
			pf.getGroupInvitationPage(ob).verifyingInvitations(title);
			//BrowserWaits.waitTime(10);
			test.log(LogStatus.INFO, "Invitation Accepted");
			pf.getGroupInvitationPage(ob).acceptInvitation(title);
			test.log(LogStatus.INFO, "verify the group desc");

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).verifyGroupDescription(desc));
				test.log(LogStatus.PASS, "Group desc verified");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Group description not verified", "Group_Desc_verified");
			}
			logout();
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
