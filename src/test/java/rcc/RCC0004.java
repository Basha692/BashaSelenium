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

import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class RCC0004 extends TestBase {

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
			String desc = RandomStringUtils.randomAlphanumeric(500);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER008", "RCCTESTUSERPWD008");
			// OPQA-1578--start--edit group description with <= 500 characters.

			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle,"test");
			pf.getGroupDetailsPage(ob).clickOnEditButton();
			pf.getGroupDetailsPage(ob).updateGroupDescription(desc);
			pf.getGroupDetailsPage(ob).clickOnSaveButton();

			try{
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getGroupDescription().equals(desc));
				test.log(LogStatus.PASS,
						"user is able to edit a group with description == 500 chars");
			}catch(Throwable t){
					logFailureDetails(test, t, "user is not able to edit a group with description > 500 chars",
							"_Group_edit_with_500_chars_Failed");
					
				}
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try{
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(desc, groupTitle));
				test.log(LogStatus.PASS,
						"Group description of 500 chars is updated in group list page");
			}catch(Throwable t){
					logFailureDetails(test, t, "Group description of 500 chars is updated in group list page",
							"_Group_edit_with_500_chars_Failed");
					
				}
			
			String desc500 = RandomStringUtils.randomAlphanumeric(510);

			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);	
			pf.getGroupDetailsPage(ob).clickOnEditButton();
			pf.getGroupDetailsPage(ob).updateGroupDescription(desc500);
			pf.getGroupDetailsPage(ob).clickOnSaveButton();
			try{
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getGroupDescription().trim().length()==500);
				test.log(LogStatus.PASS,
						"user is not able to edit a group with description > 500 chars");
			}catch(Throwable t){
					logFailureDetails(test, t, "user is able to edit a group with description > 500 chars",
							"_Group_edit_with_510_chars_Failed");
					
				}

			pf.getGroupsPage(ob).clickOnGroupsLink();
			try{
				Assert.assertTrue(pf.getGroupsListPage(ob).verifyGroupDescription(desc500, groupTitle));
				test.log(LogStatus.PASS,
						"Group description of 500 chars is updated in group list page");
			}catch(Throwable t){
					logFailureDetails(test, t, "Group description of 500 chars is updated in group list page",
							"_Group_edit_with_500_chars_Failed");
					
				}
			pf.getUtility(ob).deleteGroup(groupTitle);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends ");
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

	public void Create50CharGroup(String title) {

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
