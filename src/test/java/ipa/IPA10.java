	package ipa;

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
import util.ErrorUtil;
import util.ExtentManager;

	/**
	 * Class for follow/unfollow profile from search page itself
	 * 
	 * @author UC202376
	 *
	 */
	public class IPA10 extends TestBase {

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
				String freeformText=RandomStringUtils.randomAlphabetic(5).toLowerCase();
				openBrowser();
				clearCookies();
				maximizeWindow();
				test.log(LogStatus.INFO, "Login to IPA application");
				ob.navigate().to(host+CONFIG.getProperty("appendIPAAppUrl"));
				if(!pf.getLoginTRInstance(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1")))
						throw new Exception("Login not sucess");				
				//NEON-485
				//OPQA-4315
				test.log(LogStatus.PASS, "Login successfully");
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", freeformText);
				pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(0);
			try {
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(freeformText));
				test.log(LogStatus.PASS,
						"User is able to add search term which is not available in the type ahead suggestion");
			} catch (Exception e) {
				logFailureDetails(test,
						"User is not able to add search term which is not available in the type ahead suggestion",
						"_FreeFormText_validation_Failed");
			}
				
				
				//OPQA-4316
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", "Laser");
				String selectedTerm=pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(1);
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm));
				test.log(LogStatus.PASS,
						"User is able to add search term which is available in the type ahead suggestion");
				} catch (Exception e) {
						logFailureDetails(test,
								"User is not able to add search term which is available in the type ahead suggestion",
								"_SerachTerm_validation_Failed");
					}
				
				//NEON-492
				//OPQA-4329
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("technology", "Laser");
				try{
				pf.getSearchPageInstance(ob).exploreSearch();
				test.log(LogStatus.PASS,
						"User is able not to submit search from the search box when user enters terms from Technology search box");
				}catch (Exception e) {
					logFailureDetails(test,
							"User is able to submit search from the search box when user enters terms from Technology search box",
							"_SerachTerm_validation_Failed");
				}
				
				//-------------------------------------------------------------
				//NEON-485
				/*pf.getDashboardPage(ob).SearchTermEnter("company", freeformText);
				pf.getDashboardPage(ob).selectSearchTermFromSuggestion(0);
				
				try{
				pf.getDashboardPage(ob).checkForTextInSearchTermList(freeformText));
				test.log(LogStatus.PASS,
						"User is able to add search term which is not available in company type ahead suggestion");
				}catch (Exception e) {
				logFailureDetails(test,
						"User is not able to add search term which is not available in company type ahead suggestion",
						"_FreeFormText_validation_Failed");
			}*/
				//NEON-575
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "Network");
				List<String> list=pf.getSearchPageInstance(ob).addCompanyTerms("2:1");
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(list.get(0)));
				test.log(LogStatus.PASS,
						"User is able to submit search from the search box when user enters terms from Company search box");
				}catch (Exception e) {
					logFailureDetails(test,
							"User is able to submit search from the search box when user enters terms from Company search box",
							"_SerachTerm_validation_Failed");
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
