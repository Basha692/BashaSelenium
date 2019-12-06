	package ipa;

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

	/**
	 * Class for follow/unfollow profile from search page itself
	 * 
	 * @author UC202376
	 *
	 */
	public class IPA13 extends TestBase {

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
				//NEON-490
				test.log(LogStatus.PASS, "Login successfully");
				
				/*OPQA-4292:Verify that Keyboard inputs are accepted in Technology search text box
				When Focus is on Technology textbox and some text is typed from keyboard*/

				/*OPQA-4295:Verify that Show All link in type-ahead will show full list of dictionary terms
				When type some text in search textbox and press Show All link*/
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).clickOnShowAllLinkInTypeAhead();
				waitForAjax(ob);
				/*OPQA-4302:Verify that User is taken to intermittent page with a message stating no results
				when search returns 0 results from main landing page*/
				
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", "jkhsdfkss");
				pf.getSearchPageInstance(ob).exploreSearch();
				try{
				Assert.assertTrue(pf.getDashboardPage(ob).getPatentCount()==0);
				test.log(LogStatus.PASS,
						"User is taken to intermittent page with a message stating no results when search returns 0 results from main landing page");
				} catch (Throwable e) {
					logFailureDetails(test,
							"User is not taken to intermittent page with a message stating no results when search returns 0 results from main landing page",
							"Failed_Screenshot2");
				}
				BrowserWaits.waitTime(4);
				/*OPQA-4293:Verify that terms can be added to clipboard from type-ahead
				When some text is entered in search textbox and add button is pressed*/
				/* OPQA-4300:Verify that Pressing Enter key on the search text box will navigate to dashboard
				When Some terms are added to clipboard and term is removed from search text box. Enter key is pressed in search text box*/
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();				
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(searchTerm));
				test.log(LogStatus.PASS, "Search terms are added to the clipboard");
				pf.getSearchPageInstance(ob).removeSearchTerm(searchTerm);
				pf.getSearchPageInstance(ob).exploreSearch();
				test.log(LogStatus.PASS, "User is able to explore search after terms are removed from clipboard");
			} catch (Throwable e) {
				logFailureDetails(test,
						"Adding serach terms and removing them and performing search failed",
						"Failed_Screenshot3");
			}		
				
				/* OPQA-4301:Verify that Pressing Enter key on the search text box will navigate to dashboard
				When no terms are added to clipboard and type some text in search text box press Enter key*/
				try{
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).exploreSearch();
				test.log(LogStatus.PASS, "User is able to explore search without adding any terms to clipboard");
				} catch (Throwable e) {
					logFailureDetails(test,
							"User is not able to explore search without adding any terms to clipboard",
							"Failed_Screenshot4");
				}		
				/*OPQA-4299:Verify that Pressing Enter key on the search text box will navigate to dashboard
				When Some terms are added to clipboard and Enter key is pressed in search text box*/
				
				try{
					pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				pf.getSearchPageInstance(ob).exploreSearch();
				test.log(LogStatus.PASS, "User is able to explore search by adding any terms to clipboard");
				} catch (Throwable e) {
					logFailureDetails(test,
							"User is not able to explore search by adding any terms to clipboard",
							"Failed_Screenshot4");
				}	
				/*OPQA-4295 :Verify that User is taken to intermittent page with a message stating no results
				when search returns 0 results from dashboard*/
				
				//TODO Implement OPQA-4295
				
				
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

			/*
			if (status == 1)
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
