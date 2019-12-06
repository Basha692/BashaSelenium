package ipa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for follow/unfollow profile from search page itself
 * 
 * @author UC202376
 *
 */
public class IPA16 extends TestBase {

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
	@Parameters({"username","password"})
	public void testLoginIPA(String username,String password) throws Exception {

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
			if(!pf.getLoginTRInstance(ob).loginToIPA(username, password))
					throw new Exception("Login not sucess");				
			test.log(LogStatus.PASS, "Login successfully");
			pf.getDashboardPage(ob).SearchTermEnter("technology", "Java");
			pf.getDashboardPage(ob).addTechnologyTerms("",6 , false, false);
			pf.getDashboardPage(ob).exploreSearch();
			ViewByValidation("competitors","where;trend;patents");
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

	
	private void ViewByValidation(String BaseTab, String Tabs) throws Exception {
		pf.getDashboardPage(ob).selectTab(BaseTab);
		String[] TabsArray = Tabs.split(";");
		int PatentCount=pf.getDashboardPage(ob).getPatentCount();
		int afterviewBy=0;
		Map<String,String> options=new HashMap<String,String>();
		options.put("Families", "Basic");
		options.put("All", "undefined");
		options.put("Applications", "A");
		options.put("Grants", "G");
		options.put("Utility Models", "U");
		
		if(pf.getDashboardPage(ob).getSelectedViewByOption().equals("All"))
			test.log(LogStatus.PASS, "View By Option by default selected with option:All");
		else
			test.log(LogStatus.FAIL, "View By Option by default not selected with option:All");
		
		if(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_APPLICATION_CSS).isEnabled())
			test.log(LogStatus.PASS, "Application Option is enabled when View By selected with Publication");
		else
			test.log(LogStatus.FAIL, "Application Option is not enabled when View By selected with Publication");
		if(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_GRANT_CSS).isEnabled())
			test.log(LogStatus.PASS, "GRANT Option is enabled when View By selected with Publication");
		else
			test.log(LogStatus.FAIL, "GRANT Option is not enabled when View By selected with Publication");
		if(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_UTILITY_CSS).isEnabled())
			test.log(LogStatus.PASS, "UTILITY Option is enabled when View By selected with Publication");
		else
			test.log(LogStatus.FAIL, "UTILITY Option is not enabled when View By selected with Publication");
		if(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_ALL_CSS).isEnabled())
			test.log(LogStatus.PASS, "All Option is enabled when View By selected with Publication");
		else
			test.log(LogStatus.FAIL, "All Option is not enabled when View By selected with Publication");
		
		pf.getDashboardPage(ob).selectViewBy(options.get("Families"));
		if(pf.getDashboardPage(ob).getSelectedViewByOption().equals("Families"))
			test.log(LogStatus.PASS, "Families Selected in view By option as expected");
		else
			test.log(LogStatus.FAIL, "Families not Selected in view By option as expected");
		if(!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_APPLICATION_CSS).isEnabled())
			test.log(LogStatus.PASS, "Application Option is  disabled when View By selected with Families");
		else
			test.log(LogStatus.FAIL, "Application Option is  not disabled when View By selected with Families");
		if(!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_GRANT_CSS).isEnabled())
			test.log(LogStatus.PASS, "Grant Option is  disabled when View By selected with Families");
		else
			test.log(LogStatus.FAIL, "Grant Option is  not disabled when View By selected with Families");
		if(!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_UTILITY_CSS).isEnabled())
			test.log(LogStatus.PASS, "Utility Option is  disabled when View By selected with Families");
		else
			test.log(LogStatus.FAIL, "Utility Option is  not disabled when View By selected with Families");
		if(!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_VIEWBY_ALL_CSS).isEnabled())
			test.log(LogStatus.PASS, "ALL Option is  disabled when View By selected with Families");
		else
			test.log(LogStatus.FAIL, "ALL Option is  not disabled when View By selected with Families");
		
		
		for(Map.Entry<String, String> entry : options.entrySet()) {
			String ViewBy=entry.getKey();
			pf.getDashboardPage(ob).selectViewBy(entry.getValue());
			boolean flag=pf.getDashboardPage(ob).getSelectedViewByOption().equals(ViewBy);
			test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" option is "+(flag?"":" not")+"Selected");
			afterviewBy=pf.getDashboardPage(ob).getPatentCount();
			flag=afterviewBy!=PatentCount;
			test.log(flag?LogStatus.PASS:LogStatus.FAIL, "Patents found  is "+(flag?"":" not")+"changed on selecting "+ViewBy);
			
			for (String Tab : TabsArray) {
				pf.getDashboardPage(ob).selectTab(Tab);
				flag=pf.getDashboardPage(ob).getSelectedViewByOption().equals(ViewBy);
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" option selection is "+(flag?"":" not")+"retained when user swictches from"+BaseTab+" to "+Tab);
				flag=pf.getDashboardPage(ob).getPatentCount()==afterviewBy;
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" Patents found "+(flag?"":" not")+"retained when user swictches from"+BaseTab+" to "+Tab);
				pf.getDashboardPage(ob).selectTab(BaseTab);	
				flag=pf.getDashboardPage(ob).getSelectedViewByOption().equals(ViewBy);
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" option selection is "+(flag?"":" not")+"retained when user swictches from"+Tab+" to "+BaseTab);
				flag=pf.getDashboardPage(ob).getPatentCount()==afterviewBy;
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" Patents found "+(flag?"":" not")+"retained when user swictches from"+Tab+" to "+BaseTab);
				
				//Delete Search Term
				test.log(LogStatus.INFO, "Deleteing a search term");
				int searchtermbefore=pf.getDashboardPage(ob).getSearchCount();
				String deletedterm=pf.getDashboardPage(ob).deleteAnySearchTerm();
				int searchtermafter=pf.getDashboardPage(ob).getSearchCount();
				pf.getBrowserActionInstance(ob).waitForAjax(ob);
				flag=(searchtermafter+1)==searchtermbefore;
				test.log(flag?LogStatus.PASS:LogStatus.FAIL,"Search Term "+deletedterm+(flag?"":" not")+"Deleted from dashboard on "+BaseTab);
				flag=pf.getDashboardPage(ob).getPatentCount()<afterviewBy;
				test.log(flag?LogStatus.PASS:LogStatus.FAIL,"On deleting Search Term "+deletedterm+"from dashboard on "+BaseTab+" Patents found"+(flag?"":" not ")+"changed");
				flag=pf.getDashboardPage(ob).getSelectedViewByOption().equals(ViewBy);
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" option selection is "+(flag?"":" not")+"retained when user deletes search from"+BaseTab+" with view by"+ViewBy);
				
				test.log(LogStatus.INFO, "Adding a search term");
				searchtermbefore=pf.getDashboardPage(ob).getSearchCount();
				pf.getDashboardPage(ob).addSearchTerm(deletedterm);
				int searchtermafterdelete=pf.getDashboardPage(ob).getSearchCount();
				pf.getBrowserActionInstance(ob).waitForAjax(ob);
				flag=(searchtermafter)==(searchtermbefore+1);
				test.log(flag?LogStatus.PASS:LogStatus.FAIL,"Search Term "+deletedterm+(flag?"":" not")+"added to dashboard on "+BaseTab);
				flag=pf.getDashboardPage(ob).getPatentCount()==afterviewBy;
				test.log(flag?LogStatus.PASS:LogStatus.FAIL,"On adding Search Term "+deletedterm+"from dashboard on "+BaseTab+" Patents found"+(flag?"":" not ")+"changed");
				flag=pf.getDashboardPage(ob).getSelectedViewByOption().equals(ViewBy);
				test.log(flag?LogStatus.PASS:LogStatus.FAIL, ViewBy+" option selection is "+(flag?"":" not")+"retained when user adds search term from"+BaseTab+" with view by"+ViewBy);
				
			
			}
			
			
			
			
		}
		
	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		
		/*LogStatus info =test.getRunStatus();
		
		if(info.toString().equalsIgnoreCase("PASS"))
			status=1;
		else if(info.toString().equalsIgnoreCase("FAIL"))
			status=2;
		
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
