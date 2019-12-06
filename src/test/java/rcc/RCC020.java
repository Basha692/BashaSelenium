package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class RCC020 extends TestBase {
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
			// login with Owneruser and search for the article
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER007", "RCCTESTUSERPWD007");
			//BrowserWaits.waitTime(10);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsListPage(ob).deleteAllGroups();
			int beforecreation = pf.getGroupsPage(ob).getGroupsCount();
			waitForAjax(ob);
			System.out.println(beforecreation);

			//searching the specified search
			pf.getHFPageInstance(ob).searchForText("micro biology");
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).clickAddToGroup();
			try{
				Assert.assertTrue(pf.getSearchResultsPageInstance(ob).validateAddToGroupDropDown());
				test.log(LogStatus.PASS, "Add to group drop down validation passed when there no groups");
			}catch(Throwable t){
				logFailureDetails(test, t, "Add to group drop down validation failed when there no groups", "Addtogroup_validation_failed");
			}
			
			pf.getSearchResultsPageInstance(ob).createGroupAddToGroupDropDown(groupTitle);
			test.log(LogStatus.INFO, "Group creation successfull");
			
			try{
				Assert.assertTrue(pf.getSearchResultsPageInstance(ob).checkgroupNameIsSelectedResultsPage(groupTitle));
				test.log(LogStatus.PASS, "Newly creadted group is available in drop down");
			}catch(Throwable t){
				logFailureDetails(test, t, "Add to group drop down validation failed when there no groups", "Addtogroup_validation_failed");
			}
			
			// pf.getSearchResultsPageInstance(ob).verifyGroupCreationIcon();
			
			//verifying the group is present in the group list
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try{
			Assert.assertTrue(pf.getGroupsListPage(ob).checkForGroup(groupTitle));
			test.log(LogStatus.PASS, "Group created from add to group drop down is present in the grouplist");
			}catch(Throwable t){
				logFailureDetails(test, t, "Group created from add to group drop down is present in the grouplist", "Add_to_group_validation_failed");
			}
			int aftercreation = pf.getGroupsPage(ob).getGroupsCount();
			System.out.println(aftercreation);
			try{
			Assert.assertEquals(beforecreation + 1, aftercreation);
			test.log(LogStatus.PASS, "Group count is updated when users creates group from add to group drop down");
			}catch(Throwable t){
				logFailureDetails(test, t, "Group count is not updated when users creates group from add to group drop down", "Add_to_groupCount_validation_failed");
			}
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			String groupTitle1 = this.getClass().getSimpleName() + "_Group_" + "_" + getCurrentTimeStamp();
			pf.getGroupsListPage(ob).createGroup(groupTitle1);		
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("micro biology");
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).clickAddToGroup();
			try{
			pf.getSearchResultsPageInstance(ob).verifyFirstGroupInList(groupTitle1);
			test.log(LogStatus.PASS, "Most recent group is listed in the top in add to group list");
			}catch(Throwable t){
				logFailureDetails(test, t, "Most recent group is not listed in the top in add to group list", "Add_to_group_order_validation_failed");
			}
			
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

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
