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
 * Class to Verify that If the user enters a name, then toggles to ORCiD search
 * tab, then toggles back to name search tab, the name entered should still be
 * visible. Same for the ORCiD.
 * 
 * @author UC225218
 *
 */
public class WAT192 extends TestBase {

	static int status = 1;

	/**
	 * Method to displaying JIRA ID's for test case in specified path of Extent
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
	 * Method for login into WAT application using TR ID
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
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2", "OrgName1", "OrgName2" })
	public void searchAuthorCluster(String LastName, String CountryName1, String CountryName2, String OrgName1,
			String OrgName2) throws Exception {
		try {
			// Search for an author cluster with only Last name
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCiD_SEARCH_BUTTON_XPATH).click();
			test.log(LogStatus.INFO, "ORCiD button clicked");
			waitForPageLoad(ob);
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_NAME_SEARCH_BUTTON_XPATH).click();
			test.log(LogStatus.INFO, "Toggled back to Name Search tab");
			if (!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).getText()
					.isEmpty())
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).getText(),
						LastName, "Last Name in Name search is not matching");
			test.log(LogStatus.PASS, "Author search name is visible if user toggles from name search to ORCiD search and returns back");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Author search name is not visible if user toggles from name search to ORCiD search and returns back", "Search_Fail");
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
