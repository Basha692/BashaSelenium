package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;


/**
 * The RCC113 program covers Add Article/Patent/Post to Group from Watchlist Page
 * @author Chinna
 * 
 */
public class RCC114 extends TestBase {

	static int status = 1;
	static String groupTitle=null;
	static String groupTitle2=null;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
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
	@Parameters({"username", "password"})
	public void testLoginTRAccount(String username,String password) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts --->");

		try {
			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(host);
			test.log(LogStatus.INFO, "Login to Neon using STeAM account");
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Login_not_done");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_TR_Login_Not_happended")));
			closeBrowser();
		}
	}
	
	
	/**
	 * Method for wait TR Login Screen
	 * 
	 * @throws Exception,
	 *             When TR Login screen not displayed
	 */
	@Test(dependsOnMethods="testLoginTRAccount")
	public void createTwoNewGroups() throws Exception {

		test.log(LogStatus.INFO, "Create Group initiaziated. ");
		try {
			groupTitle = this.getClass().getSimpleName() + "_Group_1" + "_" + getCurrentTimeStamp();
			String desc = this.getClass().getSimpleName() + "_Group_" + RandomStringUtils.randomAlphanumeric(100);
			groupTitle2 = this.getClass().getSimpleName() + "_Group_2" + "_" + getCurrentTimeStamp();
			
			//BrowserWaits.waitTime(21);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle, desc);
			//BrowserWaits.waitTime(21);
			test.log(LogStatus.INFO, "Group one Created");
			
			
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup(groupTitle2, desc);
			test.log(LogStatus.INFO, "Group two Created");
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Not able to Creat Group");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_group_creation_not_done")));// screenshot
			closeBrowser();
		}
	}
	
	/**
	 * Method for Add Article/Patent/Post into Group from Record view page
	 * @param documentTitle
	 * @throws Exception, When not able to add record to group
	 */
	/**
	 * @param documentTitle
	 * @throws Exception
	 */
	@Test(dependsOnMethods="createTwoNewGroups")
	@Parameters("documentTitle") 
	public void addDocumentToMultipleGroupsFromSearchResultsPage(String documentTitle) throws Exception {

		test.log(LogStatus.INFO, "Add Article/Patent/Post into the group from Watchlist page");
		try {
			ob.navigate().refresh();
			test.log(LogStatus.INFO, "Search for Article/Patent/Post");
			pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(documentTitle);
			test.log(LogStatus.INFO, "Add Article into Group");
			String articleTitle=pf.getSearchResultsPageInstance(ob).getArticleTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToMultipleGroup(Arrays.asList(groupTitle,groupTitle2));
			
			
			/*test.log(LogStatus.INFO, "Add Patent into Group");
			String patentTitle=pf.getSearchResultsPageInstance(ob).getPatentsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToMultipleGroup(Arrays.asList(groupTitle,groupTitle2));
			
			test.log(LogStatus.INFO, "Add Post into Group");
			String postTitle=pf.getSearchResultsPageInstance(ob).getPostsTitle();
			pf.getSearchResultsPageInstance(ob).addDocumentToMultipleGroup(Arrays.asList(groupTitle,groupTitle2));*/
			
			
			test.log(LogStatus.INFO, "Go to First Group Record Details Page");
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnGroupsLink();
			
			pf.getGroupsListPage(ob).navigateToGroupRecordPage(groupTitle);
			test.log(LogStatus.INFO, "Verify Added Article available in Group Details Artilce tab");
			pf.getGroupDetailsPage(ob).validateArtcileInGroupDetailsPage(test,articleTitle);
			
			/*test.log(LogStatus.INFO, "Verify Added Patent available in Group Details Patents tab");
			pf.getGroupDetailsPage(ob).validatePatentInGroupDetailsPage(test,patentTitle);
			
			test.log(LogStatus.INFO, "Verify Added Post available in Group Details Patents tab");
			pf.getGroupDetailsPage(ob).validatePostInGroupDetailsPage(test,postTitle);*/
			
			
			test.log(LogStatus.INFO, "Delete Created Group");
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
			
			test.log(LogStatus.INFO, "Go to Second Group Record Details Page");
			//BrowserWaits.waitTime(4);
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnGroupsLink();
			
			pf.getGroupsListPage(ob).navigateToGroupRecordPage(groupTitle2);
			test.log(LogStatus.INFO, "Verify Added Article available in Group Details Artilce tab");
			pf.getGroupDetailsPage(ob).validateArtcileInGroupDetailsPage(test,articleTitle);
			
			/*test.log(LogStatus.INFO, "Verify Added Patent available in Group Details Patents tab");
			pf.getGroupDetailsPage(ob).validatePatentInGroupDetailsPage(test,patentTitle);
			
			test.log(LogStatus.INFO, "Verify Added Post available in Group Details Patents tab");
			pf.getGroupDetailsPage(ob).validatePostInGroupDetailsPage(test,postTitle);*/
			
			
			test.log(LogStatus.INFO, "Delete Created Group");
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
			
			
			//pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Document not added into Group Details Page from Search Results page");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_document_not_added_into_group_details_page")));// screenshot
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
