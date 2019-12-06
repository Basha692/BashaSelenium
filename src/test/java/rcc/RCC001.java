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
import util.ErrorUtil;
import util.ExtentManager;

public class RCC001 extends TestBase {

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

		try {

			String title = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER023", "RCCTESTUSERPWD023");
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

			pf.getGroupsPage(ob).clickOnGroupsLink();
			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(desc, title));
				test.log(LogStatus.PASS, "Group description in Group list page is displayed correctly");
				pf.getGroupsListPage(ob).verifyItemsCount(0, title);
				test.log(LogStatus.PASS, "Items count in Group list page in Group list page is displayed correctly");
				Assert.assertTrue(pf.getGroupsListPage(ob).verifytheDateandTimeofIvitation(title));
				test.log(LogStatus.PASS, "Creation date is displayed for the group in group list page");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group details in Group list page is not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			String expectedStr = pf.getGroupsListPage(ob).getGroupOwnerDetails(title);
			pf.getGroupsListPage(ob).clickOnGroupOwnerName(title);
			String actProfileData = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata().toString();
			actProfileData = actProfileData.substring(1, actProfileData.length() - 1);
			try {
				Assert.assertEquals(actProfileData, expectedStr);
				test.log(LogStatus.PASS, "Group owner profile details are displayed correctly in Groups list page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group owner profile details mismatch");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE5"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER005", "RCCTESTUSERPWD005");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(title);

			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getGroupTitle(), title);
				test.log(LogStatus.PASS, "Group title displayed in Group details page correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group title mismatch in Group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsPage(ob).clickOnGroupsLink();
			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(desc, title));
				test.log(LogStatus.PASS, "Group description in Group list page is displayed correctly");
				pf.getGroupsListPage(ob).verifyItemsCount(0, title);
				test.log(LogStatus.PASS, "Items count in Group list page in Group list page is displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Items count in Group list page in Group list page is not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			expectedStr = pf.getGroupsListPage(ob).getGroupOwnerDetails(title);
			pf.getGroupsListPage(ob).clickOnGroupOwnerName(title);
			actProfileData = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata().toString();
			actProfileData = actProfileData.substring(1, actProfileData.length() - 1);
			try {
				Assert.assertEquals(actProfileData, expectedStr);
				test.log(LogStatus.PASS, "Group owner profile details are displayed correctly in Groups list page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Group owner profile details mismatch");
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
			loginAs("RCCTESTUSER023", "RCCTESTUSERPWD023");
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
