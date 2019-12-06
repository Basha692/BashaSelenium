package wat;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;

/**
 * Class for Verify Verify that user is able to sort author records/results using Sort by 'Relevance'
 * 
 * @author UC202376
 *
 */
public class WAT166 extends TestBase {

	static int status = 1;

	/**
	 * Method to displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WAT");
	}

	/**
	 * Method for login into WAT application using TR ID
	 * 
	 * @throws Exception, When WAT Login is not done
	 */
	@Test
	@Parameters({"username", "password"})
	public void testLoginWATApp(String username,
			String password) throws Exception {

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
			test.log(LogStatus.INFO, "Logging into WAT Applicaton using valid WAT Entitled user ");
			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			pf.getLoginTRInstance(ob).loginToWAT(username, password, test);
			pf.getSearchAuthClusterPage(ob).validateAuthorSearchPage(test);

		} catch (Throwable t) {
			logFailureDetails(test, t, "Login Fail", "login_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * Method for Search Author cluster Results
	 * 
	 * @param lastName,countryName,orgName
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"testLoginWATApp"})
	@Parameters({"lastName","countryName","orgName"})
	public void searchAuthorCluster(String lastName,String countryName,String orgName) throws Exception {

		try {
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getSearchAuthClusterPage(ob).SearchAuthorClusterLastName(lastName,countryName,orgName,test);
			test.log(LogStatus.INFO, "Author Search Results are displayed");
		} catch (Throwable t) {
			logFailureDetails(test, t, "Publications_count_morethan1_results_fail","publication_count_morethan1");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}
	
	
	/**
	 * Method for verify publication count in default sort order(sort by relevance)
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"searchAuthorCluster"})
	public void sortedByRelavanceDropdown() throws Exception {

		try {
			test.log(LogStatus.INFO, "Validate Author search results in sort order dropdown (sorted by relevance) ");
			test.log(LogStatus.INFO, "Perform Search results dropdown using Publication years (oldest first)");
			pf.getSearchAuthClusterResultsPage(ob).sortedByDropddown("Publication years (oldest first)", test);
			test.log(LogStatus.INFO, "Perform Search results dropdown back using Relevance (Top hits, aggregated by count)");
			pf.getSearchAuthClusterResultsPage(ob).sortedByDropddown("Relevance (Top hits, aggregated by count)", test);
			pf.getSearchAuthClusterResultsPage(ob).sortByRelevance(test);
			test.log(LogStatus.PASS, "All Author search results in sort order (sorted by relevance) ");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Author search results are not in  sort order (sorted by relevance) ","sorted_by_relevace_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */

	}

}
