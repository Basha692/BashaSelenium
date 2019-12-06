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

public class RCC026 extends TestBase {
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
			loginAs("RCCTESTUSER020", "RCCTESTUSERPWD020");
			//BrowserWaits.waitTime(10);
			pf.getGroupsPage(ob).clickOnGroupsTab();
		//	pf.getGroupsListPage(ob).deleteAllGroups();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle);		
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("network traffic");
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			String articleTitle=pf.getSearchResultsPageInstance(ob).getArticleTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(groupTitle);
			test.log(LogStatus.INFO, "Added article to the group from results page");
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			String patentTitle=pf.getSearchResultsPageInstance(ob).getPatentsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(groupTitle);
			test.log(LogStatus.INFO, "Added patent to the group from results page");
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			String postTitle=pf.getSearchResultsPageInstance(ob).getPostsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(groupTitle);
			test.log(LogStatus.INFO, "Added post to the group from results page");
			
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			
			pf.getGroupsListPage(ob).clickOnGroupTitle(groupTitle);
			test.log(LogStatus.INFO, "Access the group");
			pf.getGroupDetailsPage(ob).clickArticlesTab();
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(articleTitle, "article");
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			test.log(LogStatus.INFO, "Removed article to the group");
			waitForAjax(ob);
			pf.getGroupDetailsPage(ob).clickPatentstab();
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(patentTitle, "patent");
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			test.log(LogStatus.INFO, "Removed patent to the group");
			waitForAjax(ob);
			pf.getGroupDetailsPage(ob).clickPostsTab();
			pf.getGroupDetailsPage(ob).clickOnRemoveRecord(postTitle, "post");
			pf.getGroupDetailsPage(ob).clickOnSubmitButtonINConfirmationModal();
			test.log(LogStatus.INFO, "Removed post to the group");
			waitForAjax(ob);
			pf.getHFPageInstance(ob).clickOnHomeLink();
			pf.getHFPageInstance(ob).searchForText(articleTitle);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).clickAddToGroupForRecord(articleTitle,"article");
			
			try{
				Assert.assertFalse(pf.getSearchResultsPageInstance(ob).checkgroupNameIsSelectedResultsPage(groupTitle));
				test.log(LogStatus.PASS, "Group name is not selected in drop down when the article is removed from the group");
			}catch(Throwable t){
				logFailureDetails(test, t, "Group name is selected in drop down when the article is removed from the group", "Addtogroup_validation_failed");
			}
			
			pf.getHFPageInstance(ob).searchForText(patentTitle);
			//BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			pf.getSearchResultsPageInstance(ob).clickAddToGroupForRecord(patentTitle,"patent");
			
			try{
				Assert.assertFalse(pf.getSearchResultsPageInstance(ob).checkgroupNameIsSelectedResultsPage(groupTitle));
				test.log(LogStatus.PASS, "Group name is not selected in drop down when the patent is removed from the group");
			}catch(Throwable t){
				logFailureDetails(test, t, "Group name is selected in drop down when the patent is removed from the group", "Addtogroup_validation_failed");
			}
			pf.getHFPageInstance(ob).searchForText(postTitle);
			//BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			pf.getSearchResultsPageInstance(ob).clickAddToGroupForRecord(postTitle, "post");
			
			try{
				Assert.assertFalse(pf.getSearchResultsPageInstance(ob).checkgroupNameIsSelectedResultsPage(groupTitle));
				test.log(LogStatus.PASS, "Group name is not selected in drop down when the post is removed from the group");
			}catch(Throwable t){
				logFailureDetails(test, t, "Group name is selected in drop down when the post is removed from the group", "Addtogroup_validation_failed");
			}
			
			pf.getUtility(ob).deleteGroup(groupTitle);
			test.log(LogStatus.INFO, "Deleted the group");
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
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends ");
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
