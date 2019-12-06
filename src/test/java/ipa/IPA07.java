package ipa;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
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
public class IPA07 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception,
	 *             When Something unexpected
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
	 * @throws Exception,
	 *             When TR Login is not done
	 */
	@Test
	@Parameters({ "username", "password" })
	public void testLoginIPA(String username, String password) throws Exception {

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
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			if (!pf.getLoginTRInstance(ob).loginToIPA(username, password))
				throw new Exception("Login not sucess");
			test.log(LogStatus.PASS, "Login successfully");
			pf.getDashboardPage(ob).SearchTermEnter("technology", "Java");
			pf.getDashboardPage(ob).addTechnologyTerms("", 6, false, false);
			pf.getDashboardPage(ob).exploreSearch();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			pf.getDashboardPage(ob).selectTechPatentTAB();
			int pub = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_PUBLICATION_NUMS_CSS,
					test);
			int pubdate = pf.getDashboardPage(ob)
					.getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_PUBLICATION_DATES_CSS, test);
			int Assignee = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_ASSIGNEES_CSS,
					test);
			int parent = pf.getDashboardPage(ob)
					.getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_ULIMATE_PATENTS_CSS, test);
			int title = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_DWPI_TITLES_CSS,
					test);
			//int relavences = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_RELAVENCES_CSS,
				//	test);
			int status = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_LIVE_STATUS_CSS,
					test);
			int expiredates = pf.getDashboardPage(ob)
					.getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_EXPIRATION_DATES_CSS, test);
			int dwpiimg = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_DWPI_IMAGES_CSS,
					test);
			int pdfs = pf.getDashboardPage(ob).getDisplayedCount(OnePObjectMap.NEON_IPA_RESULTLIST_PDF_LINKS_CSS, test);
			//&& pub == relavences 
			if (pub == pubdate && pub == Assignee && pub == parent && pub == title && pub == status
					&& pub == expiredates && pub == dwpiimg && pub == pdfs)
				test.log(LogStatus.PASS, "All Mandatory Fields are displayed successfully");
			else
				test.log(LogStatus.FAIL, "All Mandatory Fields are not displayed successfully");
			String defaultvalue=new Select(ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PAT_SEL_DD_CSS.toString()))).getFirstSelectedOption().getText();
			
			
			if(defaultvalue.equalsIgnoreCase("Relevance"))
				test.log(LogStatus.PASS, "Relevance is the default option selected in sort Dropdown");
			else
				test.log(LogStatus.FAIL, "Relevance is not the default option selected in sort Dropdown");
			
			List<Double> ele=pf.getDashboardPage(ob).getRelavencesList(OnePObjectMap.NEON_IPA_RESULTLIST_RELAVENCES_CSS);
			List<Double> sorted =new ArrayList<>(ele);
			Collections.sort(sorted,Collections.reverseOrder());
			if(ele.equals(sorted))
				test.log(LogStatus.PASS, "Sort By Relevance  is working as Expected");
			else
				test.log(LogStatus.FAIL, "Sort By Relevance  is not working as Expected.Expected:"+sorted+" Actual:"+ele+".");
			
			pf.getDashboardPage(ob).sortByText("Assignee");
			List<String> ele1=pf.getDashboardPage(ob).getStringList(OnePObjectMap.NEON_IPA_RESULTLIST_ASSIGNEES_CSS);
			List<String> sorted1 =new ArrayList<>(ele1);
			Collections.sort(sorted1);
			if(ele1.equals(sorted1))
				test.log(LogStatus.PASS, "Sort By Assignee is working as Expected");
			else{
				logFailureDetails(test, "Sort By Assignee is not working as Expected.Expected:"+sorted1+" Actual:"+ele1+".", "fail");
			}
			
				
			/*
			pf.getDashboardPage(ob).sortByText("Publication Number");
			
			ele1=pf.getDashboardPage(ob).getStringList(OnePObjectMap.NEON_IPA_RESULTLIST_PUBLICATION_NUMS_CSS);
			sorted1 =new ArrayList<>(ele1);
			Collections.sort(sorted1);
			System.out.println(ele1.equals(sorted1));
			*/
			pf.getDashboardPage(ob).sortByText("Ultimate Parent");
			
			ele1=pf.getDashboardPage(ob).getStringList(OnePObjectMap.NEON_IPA_RESULTLIST_ULIMATE_PATENTS_CSS);
			sorted1 =new ArrayList<>(ele1);
			Collections.sort(sorted1);
			if(ele1.equals(sorted1))
				test.log(LogStatus.PASS, "Sort By Ultimate Parent is working as Expected");
			else{
				
				logFailureDetails(test, "Sort By Ultimate Parent is not working as Expected.Expected:"+sorted1+" Actual:"+ele1+".", "fail");
			}
				
		
			pf.getDashboardPage(ob).sortByText("Date added");
			
			List<Date> dates=pf.getDashboardPage(ob).getDateList(OnePObjectMap.NEON_IPA_RESULTLIST_PUBLICATION_DATES_CSS);
			ArrayList<Date> datessorted =new ArrayList<>(dates);
			Collections.sort(datessorted,Collections.reverseOrder());
			if(dates.equals(datessorted))
				test.log(LogStatus.PASS, "Sort By Date added is working as Expected");
			else
				test.log(LogStatus.FAIL, "Sort By Date added is not working as Expected.Expected:"+datessorted+" Actual:"+dates+".");
		
			
			pf.getDashboardPage(ob).sortByText("Relevance");
			ele=pf.getDashboardPage(ob).getRelavencesList(OnePObjectMap.NEON_IPA_RESULTLIST_RELAVENCES_CSS);
			sorted =new ArrayList<>(ele);
			Collections.sort(sorted,Collections.reverseOrder());
			if(ele.equals(sorted))
				test.log(LogStatus.PASS, "Sort By Relevance  is working as Expected when selecting after some other sorts");
			else
				test.log(LogStatus.FAIL, "Sort By Relevance  is not working as Expected when selecting after some other sorts.Expected:"+sorted+" Actual:"+ele+".");
			
			pf.getDashboardPage(ob).sortByText("Expiration Date");
			dates= pf.getDashboardPage(ob).getExpiryDates(10);
			datessorted =new ArrayList<>(dates);
			Collections.sort(datessorted,Collections.reverseOrder());
			if(dates.equals(datessorted))
				test.log(LogStatus.PASS, "Sort By Expiration Date is working as Expected");
			else
				test.log(LogStatus.FAIL, "Sort By Expiration Date is not working as Expected.Expected:"+datessorted+" Actual:"+dates+".");
			
			
			
			pf.getDashboardPage(ob).sortByText("Strength");
			
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_RESULTLIST_DWPI_TITLES_CSS).click();
			ele= pf.getDashboardPage(ob).getPatentStrength(10);
			sorted =new ArrayList<>(ele);
			Collections.sort(sorted,Collections.reverseOrder());
			if(ele.equals(sorted))
				test.log(LogStatus.PASS, "Sort By Strength is working as Expected");
			else
				test.log(LogStatus.FAIL, "Sort By Strength is not working as Expected.Expected:"+sorted+" Actual:"+ele+".");
			
			
			
			
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
	 * updating Extent Report with test case status whether it is PASS or FAIL
	 * or SKIP
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
