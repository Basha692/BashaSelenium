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
	public class IPA11 extends TestBase {

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
				//NEON-487
				test.log(LogStatus.PASS, "Login successfully");
				//OPQA-4336 , OPQA-4338
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				pf.getSearchPageInstance(ob).clickOnSearchTermDropDown(searchTerm);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).getselectedSynonymsCount(searchTerm)>0);
				test.log(LogStatus.PASS,
						"User is able to pre select  the Synonyms from technology Search page");
				
				} catch (Throwable e) {
					logFailureDetails(test,
							"User is not able to pre select  the Synonyms from technology Search page",
							"_Failed_ScreenShot1");
				}
				pf.getSearchPageInstance(ob).exploreSearch();
				
				pf.getSearchPageInstance(ob).clickOnSearchTermDropDownDashboard(searchTerm);
				
				
			try {
				Assert.assertTrue(pf.getSearchPageInstance(ob).getselectedSynonymsCountDashboard(searchTerm) > 0);
				test.log(LogStatus.PASS,
						"clicking the term drop down is displayed with the list of Synonyms when the user submits Technology Search");
			} catch (Throwable e) {
				logFailureDetails(test,
						"clicking the term drop down is displayed with the list of Synonyms when the user submits Technology Search",
						"_Failed_ScreenShot2");
			}
				
				//OPQA-4334 , OPQA-4335
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				pf.getSearchPageInstance(ob).clickOnSearchTermDropDown(searchTerm);
				List<String> synms=pf.getSearchPageInstance(ob).selectSearchTermSynms(searchTerm, 0);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).getselectedSynonymsCount(searchTerm)==synms.size());
				test.log(LogStatus.PASS,
						"User is able to add synonym suggestions to the Search Terms List from the technology search page ");
				} catch (Throwable e) {
					logFailureDetails(test,
							"User is not able to add synonym suggestions to the Search Terms List from the technology search page ",
							"_Failed_ScreenShot3");
				}
				//OPQA-4339
				pf.getSearchPageInstance(ob).deselectSearchTermSynms(searchTerm, synms);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).getselectedSynonymsCount(searchTerm)==0);
				test.log(LogStatus.PASS,
						"User is able to deselect the Synonyms from Technology search");
				}catch (Throwable e) {
					logFailureDetails(test,
							"User is not able to deselect the Synonyms from Technology search",
							"_Failed_ScreenShot4");
				}
				//OPQA-4340
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				
				String freeformText="gggggggg";
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", freeformText);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkIfSearchTermDropDownIsDispalyed(freeformText));
				test.log(LogStatus.PASS,
						"Search term dropdown is not displayed when there are no synonyms");
				}catch (Throwable e) {
					logFailureDetails(test,
							"Search term dropdown is displayed when there are no synonyms",
							"_Failed_ScreenShot5");
				}		
				//OPQA-4341
				pf.getSearchPageInstance(ob).removeSearchTerm(freeformText);
				try{
					Assert.assertFalse(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(freeformText));
					test.log(LogStatus.PASS,
							"user is able to Delete [x] the search term");
				}catch (Throwable e) {
					logFailureDetails(test,
							"user is not able to Delete [x] the search term",
							"_Failed_ScreenShot5");
				}		
				
				
				//OPQA-4342
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", searchTerm);
				String selectedTerm=	pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
				pf.getSearchPageInstance(ob).clickOnSearchTermDropDown(selectedTerm);
				pf.getSearchPageInstance(ob).removeSearchTerm(selectedTerm);
				try{
				Assert.assertFalse(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm));
				test.log(LogStatus.PASS,
						"user is not able to Delete [x] all associated searched term Synonyms");
				}catch (Throwable e) {
					logFailureDetails(test,
							"user is not able to Delete [x] all associated searched term Synonyms",
							"_Failed_ScreenShot5");
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
