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
import util.ErrorUtil;
import util.ExtentManager;

public class RCC004 extends TestBase{

	
static int status = 1;
	
	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * @throws Exception, When Something unexpected
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
	 * @throws Exception, When TR Login screen not displayed 
	 */
	@Test
	public void testGroupCreation() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
					+ " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {
			
			
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER026", "RCCTESTUSERPWD026");
			
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).getGroupsCount();
			pf.getGroupsPage(ob).getInvitationsCount();
			
			try{
			Assert.assertEquals(pf.getGroupsPage(ob).getGroupsCount(),0);
			Assert.assertEquals(pf.getGroupsPage(ob).getInvitationsCount(),0);
			test.log(LogStatus.PASS, "Groups count and Invitation count is zero for new user");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Groups count or Invitation count is not zero");
				test.log(
						LogStatus.FAIL,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_Groupandinvitationcount_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			
			pf.getGroupsPage(ob).switchToGroupTab();
			pf.getGroupsListPage(ob).verifyGroupsTabDefaultMessage(test);
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
	        boolean sortbyoptionpresent=pf.getGroupsListPage(ob).verifyGroupsortIsDisplayed();
				if(!sortbyoptionpresent)
            {test.log(LogStatus.PASS, "Sort by Group option is not present");
			
            }else
            {// bug is already logged for this
				test.log(LogStatus.INFO, "Sort by Group option is present:OPQA-3903  Bug is already available for this");
			/*	test.log(
						LogStatus.FAIL,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_sortbygroup")));// screenshot
				//ErrorUtil.addVerificationFailure(new Exception("Failure"));
*/				
            }
		
		    pf.getGroupsPage(ob).switchToInvitationTab();
			pf.getGroupInvitationPage(ob).verifyInvitationTabDefaultMessage(test);
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
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_login_not_done")));// screenshot
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
	}
	

	
	
	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 * @throws Exception 
	 */
	@AfterTest
	public void reportTestResult() throws Exception {
		extent.endTest(test);
		
	}
	
}
