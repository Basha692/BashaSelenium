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

public class RCC0003 extends TestBase {

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
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER008", "RCCTESTUSERPWD008");

			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle);
			pf.getGroupDetailsPage(ob).clickOnEditButton();
			String titleUpdate = RandomStringUtils.randomAlphanumeric(50);

			pf.getGroupDetailsPage(ob).updateGroupTitle(titleUpdate);
			pf.getGroupsListPage(ob).clickOnCancelGroupButton(test);

			try{
				Assert.assertTrue(pf.getGroupDetailsPage(ob).checkIfEditGroupTitleFieldIsDisplayed());
				test.log(LogStatus.PASS, "Edit mode is closed");
				Assert.assertEquals(groupTitle, pf.getGroupDetailsPage(ob).getGroupTitle());
				test.log(LogStatus.PASS, "Group title is not on cancelling the edit updated in group details page");
				}catch(Throwable t){
					logFailureDetails(test, t, "Group title is not on cancelling the edit updated in group details page",
							"_Group_creation_with_two_chars_Failed");
					
				}

			// OPQA-1571
			pf.getGroupDetailsPage(ob).clickOnEditButton();
			pf.getGroupDetailsPage(ob).updateGroupTitle(titleUpdate);
			pf.getGroupDetailsPage(ob).clickOnSaveButton();
			
			try{
				Assert.assertEquals(titleUpdate, pf.getGroupDetailsPage(ob).getGroupTitle());
				test.log(LogStatus.PASS,
						"user is able to edit group with group name of 50 characters and without any description.");
				}catch(Throwable t){
					logFailureDetails(test, t, "Groupd title is not updated in group details page",
							"_Group_creation_with_50_chars_Failed");
					
				}
			// OPQA-1573
			String desc50 = RandomStringUtils.randomAlphanumeric(51);

			pf.getGroupDetailsPage(ob).clickOnEditButton();
			pf.getGroupDetailsPage(ob).clickOnSaveButton();
			desc50=pf.getGroupDetailsPage(ob).getGroupTitle().trim();
			try{
				Assert.assertTrue(desc50.length()==50);
				test.log(LogStatus.PASS,
						"user is not able to create a new group with group name > 50 chars");
			}catch(Throwable t){
					logFailureDetails(test, t, "user is able to create a new group with group name > 50 chars",
							"_Group_creation_with_500_chars_Failed");
					
			}
			
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try {
				Assert.assertTrue(pf.getGroupsListPage(ob).checkForGroup(desc50));
				test.log(LogStatus.PASS, "group title is updated in group list page");
				
			} catch (Throwable t) {
				
				logFailureDetails(test, t, "group title is not updated in group list page", "_Group_title_mismatch");
			}
			
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
