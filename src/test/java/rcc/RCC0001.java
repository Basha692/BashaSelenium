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

public class RCC0001 extends TestBase {

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
			String title = RandomStringUtils.randomAlphanumeric(2);
			// String desc = "GR1";
			String groupTitle = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			String desc = RandomStringUtils.randomAlphanumeric(500);
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER008", "RCCTESTUSERPWD008");

			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title);
			try{
				Assert.assertEquals(title, pf.getGroupDetailsPage(ob).getGroupTitle());
			test.log(LogStatus.PASS,
					"user is able to create a new group with group name of 2 characters and without any description.");
			
			}catch(Throwable t){
				logFailureDetails(test, t, "user is not able to create a new group with group name of 2 characters and without any description.",
						"_Group_creation_with_two_chars_Failed");
				
			}
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			//BrowserWaits.waitTime(2);
			String title50 = RandomStringUtils.randomAlphanumeric(50);
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title50);

			
			try{
				Assert.assertEquals(title50, pf.getGroupDetailsPage(ob).getGroupTitle());
				test.log(LogStatus.PASS,
						"user is able to create a new group with group name of 50 characters and without any description.");
				
				}catch(Throwable t){
					logFailureDetails(test, t, "user is not able to create a new group with group name of 50 characters and without any description.",
							"_Group_creation_with_50_chars_Failed");
					
				}
			
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
			//BrowserWaits.waitTime(2);
			String title2 = RandomStringUtils.randomAlphanumeric(1);

			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).enterGroupTitle(title2);
			try{
				Assert.assertTrue(pf.getGroupsListPage(ob).validateCreateGroupCardErrorMessage());
				test.log(LogStatus.PASS,
						"Error validation for 1 char is passed for group title");
				Assert.assertFalse(pf.getGroupsListPage(ob).validateSaveButtonDisabled());
				test.log(LogStatus.PASS,
						"Save botton is disabled when min requirement for group title is not met");
				}catch(Throwable t){
					logFailureDetails(test, t, "Error message and save button validation faled for group with 1 char",
							"_Group_validation_with_1_chars_Failed");
					
				}
			
			pf.getGroupsPage(ob).CilckGroupTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle, desc);

			try{
				Assert.assertEquals(groupTitle, pf.getGroupDetailsPage(ob).getGroupTitle());
				Assert.assertEquals(desc, pf.getGroupDetailsPage(ob).getGroupDescription());
				test.log(LogStatus.PASS,
						"user is able to create a new group with group name and with description of 500 chars");
				
			}catch(Throwable t){
					logFailureDetails(test, t, "user is not able to create a new group with group name and with description of 500 chars",
							"_Group_creation_with_500_chars_Failed");
					
				}
			
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
			//BrowserWaits.waitTime(2);
			String Desc500 = RandomStringUtils.randomAlphanumeric(510);
			String title500 = RandomStringUtils.randomAlphanumeric(10);
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title500, Desc500);
			
			try{
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getGroupDescription().trim().length()==500);
				test.log(LogStatus.PASS,
						"user is not able to create a new group with group name and with description > 500 chars");
			}catch(Throwable t){
					logFailureDetails(test, t, "user is able to create a new group with group name and with description > 500 chars",
							"_Group_creation_with_500_chars_Failed");
					
				}
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
			
			String title55 = RandomStringUtils.randomAlphanumeric(55);
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title55);
			
			try{
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getGroupTitle().trim().length()==50);
				test.log(LogStatus.PASS,
						"user is not able to create a new group with group name > 50 chars");
			}catch(Throwable t){
					logFailureDetails(test, t, "user is able to create a new group with group name > 50 chars",
							"_Group_creation_with_MOrethan_50_chars_Failed");
					
				}
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
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
