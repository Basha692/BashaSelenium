package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;


public class RCC101  extends TestBase{
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
	public void verifyingSorting() throws Exception {

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

			String title = this.getClass().getSimpleName() + "_Update_Group_" + "_" + getCurrentTimeStamp();
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER004", "RCCTESTUSERPWD004");
			pf.getGroupsPage(ob).clickOnGroupsTab();
		
			Random rand = new Random(); 
			int value = rand.nextInt(5); 
			 pf.getGroupsListPage(ob).randomUpdate(value,title);
			pf.getGroupsPage(ob).clickOnGroupsLink();
			try
			{
				pf.getGroupsListPage(ob).sortByMostRecentActivity();
			test.log(
					LogStatus.PASS,
					"Grops are sorted properly by Most Recent Activity");
					pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			}
			catch(Throwable t)
			{
				logFailureDetails(test, t, "Gropus are not sort down properly for Most recent activity",
						"Drop_down_menu");
				closeBrowser();
			}
			
           
			
			
		}
		
		catch (Throwable t) {
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

	}

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
