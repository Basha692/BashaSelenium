package wat;

import java.util.List;

import org.openqa.selenium.WebElement;
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
 * Class for Verify that User should be able to ignore Org dropdown and proceed with author search
 * 
 * @author UC225218
 *
 */

public class WAT118 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";
	static String sar_labs_text = "SaR Labs";
	static String Suggestion_text = "We've found a large number of records matching your search.\n"
			+ "For the most relevant results, please select at least one location where this author has published, or select Find to view all results.";
		
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
	 * Method to Verify that User should be able to ignore Org dropdown and proceed with author search
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 * 
	 */
	
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "CountryName1", "CountryName2","OrgName1","OrgName2" })
	public void testFindButtonFunctionality3(String LastName, String CountryName1,String CountryName2, String OrgName1,String OrgName2)
			throws Exception {
			try {
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
				pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
				pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
				pf.getBrowserWaitsInstance(ob).waitTime(4);
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).isDisplayed(),
						"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).getText(), Suggestion_text,
						"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS NOT MATCHING");
				test.log(LogStatus.INFO, "AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED AND MATCHING");
				List<WebElement> ele = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
				if (ele.size() != 0) 
				{
					test.log(LogStatus.INFO, "Country Dropdown is disaplayed when search results are more than 50 clusters");
					Assert.assertTrue(pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
							"Find button is not enabled when country dropdown is diaplayed");
					test.log(LogStatus.INFO, "Find button is enabled when country dropdown is diaplayed");
					pf.getSearchAuthClusterPage(ob).selectCountryofAuthor(test,CountryName1, CountryName2);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
					Assert.assertTrue(pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
							"Find button is not enabled when country option is clicked");
					pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
					Assert.assertEquals(
							(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
							"Search Results", "Unable to search for an author and land in Author search result page.");
					test.log(LogStatus.PASS, "Able to Ignore Org filter and successfully land in Author search result page");
					pf.getBrowserActionInstance(ob).closeBrowser();
				}
				else throw new Exception("Unable to Ignore Org filter and successfully land in Author search result page");
			} catch (Throwable t) {
				logFailureDetails(test, t, "Unable to Ignore Org filter and successfully land in Author search result page", "Find_btn_error");
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
