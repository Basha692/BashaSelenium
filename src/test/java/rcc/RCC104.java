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

public class RCC104 extends TestBase {

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

			boolean statusAfterClickCreateButton = pf.getGroupsListPage(ob).getCreateGroupCard(title);
			try {
				Assert.assertTrue(statusAfterClickCreateButton);
				test.log(LogStatus.PASS, "Create Group card is displayed and Canceled successfylly");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Create Group card is not displayed and Canceled successfylly");
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			boolean statusAfterClickCancelButton = pf.getGroupsListPage(ob).checkForGroup(title);
			try {
				Assert.assertFalse(statusAfterClickCancelButton);
				test.log(LogStatus.PASS, "Create Group card is not displayed");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_desc_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}

			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			String ownerName = pf.getGroupsListPage(ob).getGroupOwnerDetailsFromCreateGroupCard();
			logger.info("Create Group card Page : " + ownerName);
			String recordPageOwnerName1 = pf.getGroupDetailsPage(ob).getRecordPageOwnerName();
			logger.info("Record View Page : " + recordPageOwnerName1);
			try {
				Assert.assertTrue(ownerName.contains(recordPageOwnerName1));
				test.log(LogStatus.PASS, "Owner name same");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_Group_title_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
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
