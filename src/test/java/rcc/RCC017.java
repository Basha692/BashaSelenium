package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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

public class RCC017 extends TestBase {

	private static final String recordType = "Patent";
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
			String infoText = "Add patents to this group by selecting the \"Add to group\" option wherever it is displayed in Project Neon.";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER052", "RCCTESTUSERPWD052");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(title, desc);
			test.log(LogStatus.INFO, "Group is created successfully: " + title);
			//BrowserWaits.waitTime(8);
			pf.getGroupDetailsPage(ob).clickPatentstab();

			try {
				Assert.assertEquals(pf.getGroupDetailsPage(ob).getNoRecordsInfoText(), infoText);
				test.log(LogStatus.PASS, "No records informational text is displayed correcty in patents tab ");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "No records informational text is not displayed correcty in patents tab");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_No_Records_info_missing")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			pf.getHFPageInstance(ob).clickOnHomeLink();
			waitForPageLoad(ob);
			pf.getHFPageInstance(ob).searchForText("micro biology");
			String recordTitle = pf.getSearchResultsPageInstance(ob).getPatentsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToGroup(title);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			try {
				pf.getGroupsListPage(ob).verifyItemsCount(1, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Items count in Group list page not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Item_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getPatentsCounts() == 1);
				test.log(LogStatus.PASS,
						"Patents count is updated properly after adding the Patent from neon to the group");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Patents count is not updated properly after adding the Patent from neon to the group");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Patents_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupDetailsPage(ob).clickPatentstab();
			//BrowserWaits.waitTime(10);
			String recordDetals = pf.getGroupDetailsPage(ob).getRecordContent(recordTitle, recordType);
			List<String> metrics = pf.getGroupDetailsPage(ob).getRecordMetrics(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnRecordTitle(recordTitle, recordType);

			try {
				Assert.assertEquals(recordTitle, pf.getpostRVPageInstance(ob).getPostTitle().trim());
				test.log(LogStatus.PASS, "Patent title in groups deails page is matching with record view page");
				Assert.assertTrue(
						pf.getpostRVPageInstance(ob).getPatentRecordDetails(recordTitle).contains(recordDetals));
				test.log(LogStatus.PASS, "Patent content in groups deails page is matching with record view page");
				// Assert.assertEquals(metrics,
				// pf.getpostRVPageInstance(ob).getRecordMetrics());
				// test.log(LogStatus.PASS, "Patent Metrics in groups deails
				// page is matching with record view page");
				test.log(LogStatus.INFO, "OPQA-4161 is already available for Metrics issue for  patent");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Patent details are not displayed correctly in group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Patent_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).clickOnGroupTitle(title);
			pf.getGroupDetailsPage(ob).clickOnInviteOthersButton();
			pf.getGroupDetailsPage(ob).inviteMembers(LOGIN.getProperty("RCCPROFILE53"));
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER053", "RCCTESTUSERPWD053");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupInvitationPage(ob).acceptInvitation(title);

			try {
				Assert.assertTrue(pf.getGroupDetailsPage(ob).getPatentsCounts() == 1);
				test.log(LogStatus.PASS,
						"Patents count is updated properly after adding the Patents from neon to the group");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Patents count is not updated properly after adding the Patents from neon to the group");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_patent_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupDetailsPage(ob).clickPatentstab();

			recordDetals = pf.getGroupDetailsPage(ob).getRecordContent(recordTitle, recordType);
			metrics = pf.getGroupDetailsPage(ob).getRecordMetrics(recordTitle, recordType);
			pf.getGroupDetailsPage(ob).clickOnRecordTitle(recordTitle, recordType);

			try {
				Assert.assertEquals(recordTitle, pf.getpostRVPageInstance(ob).getPostTitle().trim());
				test.log(LogStatus.PASS, "Patent title in groups deails page is matching with record view page");
				Assert.assertTrue(
						pf.getpostRVPageInstance(ob).getPatentRecordDetails(recordTitle).contains(recordDetals));
				test.log(LogStatus.PASS, "Patent content in groups deails page is matching with record view page");
				// Assert.assertEquals(metrics,
				// pf.getpostRVPageInstance(ob).getRecordMetrics());
				// test.log(LogStatus.PASS, "Patent Metrics in groups deails
				// page is matching with record view page");
				test.log(LogStatus.INFO, "OPQA-4161 is already available for Metrics issue for  patent");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Patent details are not displayed correctly in group details page");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Patent_details_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).switchToGroupTab();

			try {
				pf.getGroupsListPage(ob).verifyItemsCount(1, title);
				test.log(LogStatus.PASS, "Items count in Group list page displayed correctly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Items count in Group list page not displayed correctly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_Item_count_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			pf.clearAllPageObjects();

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER052", "RCCTESTUSERPWD052");
			pf.getUtility(ob).deleteGroup(title);
			pf.getLoginTRInstance(ob).logOutApp();

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

		} finally {
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
