package wat;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class to Verify that after removing a publication, the total publication
 * count should get updated accordingly.
 * 
 * @author UC225218
 *
 */
public class WAT141 extends TestBase {

	static int status = 1;
	int pubCountBeforeDeletion;

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
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getSearchAuthClusterPage(ob).searchAuthorClusterOnlyLastName(LastName, CountryName1, CountryName2,
					OrgName1, OrgName2, test);
			test.log(LogStatus.INFO, "Author Search Results are displayed");
		} catch (Throwable t) {
			logFailureDetails(test, t, "Author Search Results are not displayed", "Search_Fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}

	}

	/**
	 * Method to Verify that after removing a publication, the total publication
	 * count should get updated accordingly.
	 * 
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@Test(dependsOnMethods = { "searchAuthorCluster" })
	public void testPubDeletionCount() throws Exception {
		try {
			test.log(LogStatus.INFO, "Clicking first author card");
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_CARD_XPATH).click();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_RECORD_DEFAULT_AVATAR_CSS);
			test.log(LogStatus.INFO, "Getting into curation mode by clicking Suggest Update button");
			pf.getAuthorRecordPage(ob).checkSuggestUpdateBtn();
			test.log(LogStatus.PASS, "Suggest updates button is displayed in author record page");
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUGGEST_UPDATE_BTN_XPATH)
					.click();
			test.log(LogStatus.PASS, "Entered Curation mode through the Suggest Update button");
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH).isDisplayed(),
					"Remove Publication button is not displayed");
			test.log(LogStatus.INFO, "Remove Publication button is displayed for each publication");
			test.log(LogStatus.INFO, "Reading total publication count");
			int pubCountBeforeRemoval = pf.getAuthorRecordPage(ob).getPublicationCount();
			for (int x = 1; x<6; x++) {
				test.log(LogStatus.INFO, "Removing " + x + "Publication.");
				String j = OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_PARAMETERIZED_XPATH.toString();
				ob.findElement(By.xpath(j.replaceAll("q", String.valueOf(x)))).click();
				BrowserWaits.waitTime(2);
				waitForPageLoad(ob);
			}
			test.log(LogStatus.INFO, "Removed 5 Publications.");
			test.log(LogStatus.INFO, "Reading Current publication count");
			int pubCountAfterRemoval = pf.getAuthorRecordPage(ob).getPublicationCount();
			test.log(LogStatus.INFO, "Asserting with the Initial Publication count");
			Assert.assertEquals(pubCountAfterRemoval, pubCountBeforeRemoval - 5);
			test.log(LogStatus.PASS, "Number of publication deleted matches with the displayed count");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Publication count before and after not matching", "Curation_issue");
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
