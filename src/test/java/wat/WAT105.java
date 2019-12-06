package wat;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import watpages.SearchAuthorClusterResultsPage;

import base.TestBase;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for Verify the ORCID format followed by orcid.org/<orcid-identifier> i.e.orcid.org/0000-0002-1825-0097
 * 
 * @author UC225218
 *
 */

public class WAT105 extends TestBase {

	static int status = 1;
	private static final String ORCID_MATCHER_PATTERN = "orcid.org/([0-9]{4}-){3}[0-9]{4}";
	static String wos_title = "Web of Science: Author search";
	static String Search_result_static_text = "The following author records match your search. Author records are algorithmically generated and may not be complete, or an individual author may be represented by multiple records.\n"

			+ "Select results to combine them into a single author record. Tell us what you think.";

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WAT");
	}

	/**
	 * Method for login into WAT application using Steam ID
	 * 
	 * @throws Exception,
	 *             When WAT Login is not done
	 */
	@Test
	@Parameters({ "username", "password" })
	public void testLoginWATApp(String username, String password) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts.... ");

		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			test.log(LogStatus.INFO, "Login to WAT Applicaton using valid WAT Entitled user ");
			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			pf.getLoginTRInstance(ob).loginToWAT(username, password, test);

		} catch (Throwable t) {
			logFailureDetails(test, t, "Login Fail", "login_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * Method to Verify the ORCID format followed by orcid.org/<orcid-identifier> i.e.orcid.org/0000-0002-1825-0097
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 * 
	 */
	@SuppressWarnings("static-access")
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "lastName", "CountryName1", "CountryName2","OrgName1","OrgName2" })
	public void testORCIDFormat(String LastName, String CountryName1,String CountryName2, String OrgName1,String OrgName2) throws Exception {
		try {
			pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1,CountryName2, OrgName1,OrgName2, test);			
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_CARD_1_XPATH).click();
			pf.getBrowserWaitsInstance(ob).waitTime(3);
			String ORCID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LINK_XPATH).getText();
		    Pattern pattern = Pattern.compile(ORCID_MATCHER_PATTERN, Pattern.CASE_INSENSITIVE);
		   
		    Matcher matcher = pattern.matcher(ORCID);
		    if (matcher.matches()) {
		    	test.log(LogStatus.PASS, "ORCID match found " + matcher.group()
		             + " starting at index " + matcher.start()
		             + " and ending at index " + matcher.end());
		    } else {
		    	Assert.assertTrue(false);
		    	test.log(LogStatus.FAIL, "ORCID match not found");
		    }
		    pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "ORCID match not found");
				logFailureDetails(test, e, "ORCID match not found", "ORCID_match_fail");
				pf.getBrowserActionInstance(ob).closeBrowser();
			}
	}
	
	

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL
	 * or SKIP
	 */
	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(profilexls, "Test Cases",
		 * TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()),
		 * "SKIP");
		 */

	}
}
