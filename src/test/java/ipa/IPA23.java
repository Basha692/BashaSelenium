package ipa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class IPA23 extends TestBase {

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
			pf.getDashboardPage(ob).SearchTermEnter("company", "Apple");
			pf.getDashboardPage(ob).addCompanyTerms("1");
			pf.getDashboardPage(ob).exploreSearch();
			boolean leader = ValidationPinVisualization();
			pf.getDashboardPage(ob).selectTab("trend");
			boolean where = ValidationPinVisualization();
			pf.getDashboardPage(ob).selectTab("comparison");
			boolean trend = ValidationPinVisualization();
			
			if(leader && where && trend)
				test.log(LogStatus.PASS, "Pin Visulizations is displayed for Compititor, Where in World and Trending");
			else
				logFailureDetails(test, "Pin Visualization validation for Compititor, Where in World and Trending is failed", "fail");
			
			
			List<WebElement> pins = ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_VISUALIZATION_PIN_CSS.toString()));
			
			for(int i=0; i<pins.size(); i++){
				pins.get(i).click();
			}
			
			boolean pinVisual = validateAllGraphDisplay();
			
			if(pinVisual)
				test.log(LogStatus.PASS, "All graphs are displayed when pin is selected");
			else
				logFailureDetails(test, "All graphs are not displayed when pin is selected", "fail");
			
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
	
	public boolean ValidationPinVisualization() throws Exception{
		
		List<WebElement> tabs = ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_NONSELECTED_TABS_CSS.toString()));
		String tabName = "div[class='ng-binding']";
		String pin = "div[class='pin ng-scope']";
		int ret = 0;
		
		for(int i=0; i<tabs.size()-1; i++){
			
			String vName = tabs.get(i).findElement(By.cssSelector(tabName)).getText();
			WebElement ele = tabs.get(i).findElement(By.cssSelector(pin));
			boolean pinDisplayed = ele.isDisplayed();
			
			test.log(LogStatus.INFO, "Name of Visualization - " + vName);
			test.log(LogStatus.INFO, "Is Pin available for the visualisation - " + pinDisplayed);
			
			if(!(pinDisplayed && vName.equals("Patents")))
				ret = 1;
			else
				ret = 0;
		}
		
		if(ret==1)
			return true;
		else
			return false;		
		
	}
	
	public boolean validateAllGraphDisplay(){
		
		int activeTabs =  ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_SELECTED_TABS_CSS.toString())).size();
		int displayedGraphs = ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_DISPLAYED_GRAPHS_CSS.toString())).size();
		
		if(activeTabs == displayedGraphs+1)
			return true;
		else 
			return false;
				
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
