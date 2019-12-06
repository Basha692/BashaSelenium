	package ipa;

	import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

	/**
	 * Class for follow/unfollow profile from search page itself
	 * 
	 * @author UC202376
	 *
	 */
	public class IPA14 extends TestBase {

		static int count = -1;	

		static boolean fail = false;
		static boolean skip = false;
		static int status = 1;
		static String followBefore = null;
		static String followAfter = null;

		/**
		 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
		 * 
		 * @throws Exception, When Something unexpected
		 */
		@BeforeTest
		public void beforeTest() throws Exception {
			extent = ExtentManager.getReporter(filePath);
			rowData = testcase.get(this.getClass().getSimpleName());
			test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPA");
			
		}

		/**
		 * Method for login into Neon application using TR ID
		 * 
		 * @throws Exception, When TR Login is not done
		 */
		@Test
		public void testLoginIPA() throws Exception {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			boolean master_condition = suiteRunmode && testRunmode;
			logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

			if (!master_condition) {
				status = 3;
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
			}

			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

			try {
				String searchTerm = "Laser";
				openBrowser();
				clearCookies();
				maximizeWindow();
				test.log(LogStatus.INFO, "Login to IPA application");
				ob.navigate().to(host+CONFIG.getProperty("appendIPAAppUrl"));
				if(!pf.getLoginTRInstance(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1")))
						throw new Exception("Login not sucess");				
				//NEON-317
				test.log(LogStatus.PASS, "Login successfully");
				
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(searchTerm));
				pf.getSearchPageInstance(ob).exploreSearch();
				pf.getDashboardPage(ob).clickOnPatentFoundIcon();
				String title=pf.getDashboardPage(ob).clickOnNthPatentRecords(1);
				try{
				Assert.assertEquals(title, pf.getIpaRecordViewPage(ob).getTitle());
				test.log(LogStatus.PASS,
						"full content of record is displayed in same window  when user selects a record from ResultList from Technology");
				} catch (Exception e) {
					e.printStackTrace();
					logFailureDetails(test,
							"full content of record is not displayed in same window  when user selects a record from ResultList from Technology",
							"Failed_Screenshot1");
				}
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "Network");
				List<String> companyList=pf.getSearchPageInstance(ob).addCompanyTerms("3:1");
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(companyList.get(0)));
				pf.getSearchPageInstance(ob).exploreSearch();
				pf.getDashboardPage(ob).clickOnPatentFoundIcon();
				title=pf.getDashboardPage(ob).clickOnNthPatentRecords(1);
				try{
				Assert.assertEquals(title, pf.getIpaRecordViewPage(ob).getTitle());
				test.log(LogStatus.PASS,
						"full content of record is displayed in same window  when user selects a record from ResultList from company");
				} catch (Exception e) {
					logFailureDetails(test,
							"full content of record is not displayed in same window  when user selects a record from ResultList from company",
							"Failed_Screenshot1");
				}
				closeBrowser();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Error: Login not happended");
				// print full stack trace
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: "
						+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
				
				closeBrowser();
			}

		}

		
		/**
		 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
		 */
		@AfterTest
		public void reportTestResult() {

			extent.endTest(test);

			
			/*if (status == 1)
				TestUtil.reportDataSetResult(ipaxls, "Test Cases",
						TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "PASS");
			else if (status == 2)
				TestUtil.reportDataSetResult(ipaxls, "Test Cases",
						TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "FAIL");
			else
				TestUtil.reportDataSetResult(ipaxls, "Test Cases",
						TestUtil.getRowNum(ipaxls, this.getClass().getSimpleName()), "SKIP");*/
			 
		}
}
