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
	public class IPA12 extends TestBase {

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
				openBrowser();
				clearCookies();
				maximizeWindow();
				test.log(LogStatus.INFO, "Login to IPA application");
				ob.navigate().to(host+CONFIG.getProperty("appendIPAAppUrl"));
				if(!pf.getLoginTRInstance(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1")))
						throw new Exception("Login not sucess");				
				//NEON-484
				test.log(LogStatus.PASS, "Login successfully");
				
				/*pf.getSearchPageInstance(ob).SearchTermEnter("company", "nokia");
				pf.getSearchPageInstance(ob).clickOnShowAllLinkInTypeAhead();*/
				//validate show all page
				//OPQA-4305
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				try{
				
					pf.getSearchPageInstance(ob).SearchTermEnter("company", "n");
					test.log(LogStatus.PASS,
							"type-ahead suggestions is displayed after typing a minimum of 1 character");
				}
				catch(Throwable e){
					logFailureDetails(test,
							"type-ahead suggestions is not displayed after typing a minimum of 1 character",
							"Failed_Screenshot1");
				}
				//OPQA-4310
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "network");
				List<String> selectedTerm=pf.getSearchPageInstance(ob).addCompanyTerms("2");
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm.get(0)));
				test.log(LogStatus.PASS,
						"user is able to add company type-ahead suggestions to the searched query at parent level ");
				}catch(Throwable e){
					logFailureDetails(test,
							"user is not able to add company type-ahead suggestions to the searched query at parent level ",
							"Failed_Screenshot2");
				}
				//OPQA-4311
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "network");
				selectedTerm=pf.getSearchPageInstance(ob).addCompanyTerms("3:1");
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm.get(0)));
				test.log(LogStatus.PASS,
						"user is able to add company type-ahead suggestions to the searched query at child level ");
				}catch(Throwable e){
					logFailureDetails(test,
							"user is not able to add company type-ahead suggestions to the searched query at child level ",
							"Failed_Screenshot3");
				}
				//OPQA-4313
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "network");
				selectedTerm=pf.getSearchPageInstance(ob).addCompanyTerms("2&&3:1");
				try{
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm.get(0)));
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm.get(1)));
				test.log(LogStatus.PASS,
						"user is able to select multiple company  type-ahead suggestions");
				}catch(Throwable e){
					logFailureDetails(test,
							"user is not able to select multiple company  type-ahead suggestions",
							"Failed_Screenshot4");
				}
				//OPQA-4309
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "ariba");
				selectedTerm=pf.getSearchPageInstance(ob).addCompanyTerms("2");
				try{
				Assert.assertFalse(selectedTerm.contains("ariba"));
				Assert.assertTrue(pf.getSearchPageInstance(ob).checkForTextInSearchTermList(selectedTerm.get(0)));
				test.log(LogStatus.PASS,
						"company type ahead suggestions  hierarchy displayed parents for children that match even if the parents does not match");
				}catch(Throwable e){
					logFailureDetails(test,
							"company type ahead suggestions  hierarchy not displayed parents for children that match even if the parents does not match",
							"Failed_Screenshot5");
				}
				//OPQA-4309
				pf.getSearchPageInstance(ob).clickOnNewSearchLinkInHeader();
				pf.getSearchPageInstance(ob).SearchTermEnter("company", "network");
				try{
				pf.getSearchPageInstance(ob).validatePatentsCountForCompanyInTypeAhead();
				test.log(LogStatus.PASS,
						"company type-ahead suggestions are displayed with the matching term and number of associated patents");
				
				}catch(Throwable e){
					logFailureDetails(test,
							"company type-ahead suggestions are not displayed with the matching term and number of associated patents",
							"Failed_Screenshot5");
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
