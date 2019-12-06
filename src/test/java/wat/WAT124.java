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
 * Class for Verify that the system displays (static) publication metadata for each publication
 * @author UC225218
 *
 */
public class WAT124 extends TestBase {

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
	 * Method for Verify that the system displays (static) publication metadata for each publication
	 * 
	 * @param lastName
	 * @throws Exception, When Something unexpected
	 */
	@Test(dependsOnMethods = {"testLoginWATApp"})
	@Parameters({"lastName"})
	public void validateCombinedAuthorPublicationStaticText(String lastName) throws Exception {

		try {
			test.log(LogStatus.INFO, "Search an Author using last name");
			pf.getSearchAuthClusterPage(ob).SearchAuthorCluster(lastName,test);
			pf.getSearchAuthClusterResultsPage(ob).waitForauthorClusterSearchResults(test);
			test.log(LogStatus.INFO, "Combine two Authors");
			pf.getSearchAuthClusterResultsPage(ob).combineAuthorCard(test);
			test.log(LogStatus.INFO, "Verify whether publications are available");
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_MAIN_CARD_XPATH).isDisplayed(), "No Publications available for the user");
			test.log(LogStatus.PASS, "publications are displayed in Author Record page");
			test.log(LogStatus.INFO, "Verify whether publications Names are available");
			pf.getSearchAuthClusterResultsPage(ob).publicationName(test);
			test.log(LogStatus.INFO, "Verify whether Publication Author Names are available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationAuthor(test);
			test.log(LogStatus.INFO, "Verify author count for publication and More&Less link functionality");
			pf.getSearchAuthClusterResultsPage(ob).PublicationAuthorCount(test);
			test.log(LogStatus.PASS, "More & Less link functionality is working");
			test.log(LogStatus.INFO, "Verify whether Publication Category Names are available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationCategory(test);
			test.log(LogStatus.INFO, "Verify whether Publication Volume Text and count are available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationVolume(test);
			test.log(LogStatus.INFO, "Verify whether Publication Issue ext and Count are available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationIssue(test);
			test.log(LogStatus.INFO, "Verify whether Publication Published Year is available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationPublishedYear(test);
			test.log(LogStatus.INFO, "Verify whether Times Cited Text is available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationTimesCited(test);
			test.log(LogStatus.INFO, "Verify whether Times Cited Count is available");
			pf.getSearchAuthClusterResultsPage(ob).PublicationTimesCitedCount(test);
			test.log(LogStatus.PASS, "System displays all publication metadata for each publication");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "System is not displaying all publication metadata for each publication","Static_metadata_for_Publication_fail");
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
