package wat;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for Verify the static text in Author record Page
 * 
 * @author UC225218
 *
 */

public class WAT123 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";
	static String sar_labs_text = "SaR Labs";
	static String welcome_text = "The following details are available for this author. This author record is algorithmically generated and may not be complete. All information is derived from the publication metadata.\n"
			+ "\n"
			+ "Help to improve this author record\n"
			+ "\n"
			+"Provide your input on publications that may belong to this author and remove publications that don't belong. Review your updates and submit them for review.";

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
	 * Method to verify all the static page content in author search page.
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 * 
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2","OrgName1","OrgName2" })
	public void testStaticPageContent(String LastName, String CountryName1,String CountryName2, String OrgName1,String OrgName2) throws Exception {

		// NOTE - Assertion ERRORs are caught in EXCEPTION block just for
		// Reporting purpose
			try {
				// Verify WOS title text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).isDisplayed(),
						"WOS title is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(), wos_title,
						"WOS title text not matching");
				pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1,CountryName2, OrgName1,OrgName2, test);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_CARD_1_XPATH);
				Assert.assertEquals(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_WELCOME_TEXT_XPATH).getText(), welcome_text);
				test.log(LogStatus.PASS, "WOS title text is matching in WOS Author Search page");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "WOS title text is not present or not matching in WOS Author Search page");
				logFailureDetails(test, e, "WOS title is not displayed", "wos_title_fail");
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
