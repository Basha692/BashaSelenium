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
 * Class for testing all static contents in Author cluster search page
 * 
 * @author UC225218
 *
 */

public class WAT03 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";
	static String sar_labs_text = "SaR Labs";
	static String search_link_text = "Search";
	static String welcome_text = "Welcome to SaR Labs, a place where you can try new functionality in development by the Scientific and Scholarly Research group of Clarivate Analytics.\n"
			+ "\n"
			+ "We are currently working on an improved author search tool for Web of Science. We welcome your feedback.";
	static String search_suggestion_text = "Enter the author's name, ORCiD or RID to begin your search against Web of Science article groups.";
	static String name_search_button_text = "Name search";
	static String orcid_search_button_text = "ORCiD search";
	static String last_name_inner_text = "Last name";
	static String first_name_inner_text = "First name and middle initial(s)";
	static String add_alt_name_text = "Include alternative name";
	static String find_btn_text = "Find";

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
	public void testStaticPageContent() throws Exception {

		// NOTE - Assertion ERRORs are caught in EXCEPTION block just for
		// Reporting purpose
		try {
			try {
				// Verify WOS title text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).isDisplayed(),
						"WOS title is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(), wos_title,
						"WOS title text not matching");
				test.log(LogStatus.PASS, "WOS title text is matching in WOS Author Search page");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "WOS title text is not present or not matching in WOS Author Search page");
				logFailureDetails(test, e, "WOS title is not displayed", "wos_title_fail");
			}

			try {
				// Verify SaR Labs Text
				Assert.assertTrue(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SAR_LABS_TEXT_XPATH).isDisplayed(),
						"SaR Labs Text is not present");
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SAR_LABS_TEXT_XPATH).getText(),
						sar_labs_text, "SaR Labs text not matching");
				test.log(LogStatus.PASS, "SaR Labs text matching in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "SaR Labs text not matching in Author search page.");
				logFailureDetails(test, e, "SaR Labs Text is not present", "sar_label_fail");
			}

			try {
				// Verify Search Link
				Assert.assertTrue(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_LINK_XPATH).isDisplayed(),
						"Search link not present");
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_LINK_XPATH).getText(),
						search_link_text, "Search link text not matching");
				test.log(LogStatus.PASS, "Search link is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Search link is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Search link not present", "search_link_fail");
			}

			try {
				// Verify Name search button is present
				Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_NAME_SEARCH_BUTTON_XPATH)
						.isDisplayed(), "Name Search button is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_NAME_SEARCH_BUTTON_XPATH).getText(), name_search_button_text,
						"Name search button text not matching");
				test.log(LogStatus.PASS, "Name search button is present in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Name search button is not present or button text not matching in Author search page.");
				logFailureDetails(test, e, "Name Search button is not displayed", "name_button_fail");
			}

			try {
				// Verify ORCid search button is present
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_ORCiD_SEARCH_BUTTON_XPATH).isDisplayed(),
						"ORCiD Search button is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_ORCiD_SEARCH_BUTTON_XPATH).getText(), orcid_search_button_text,
						"Name search button text not matching");
				test.log(LogStatus.PASS, "ORCiD search button is present in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"ORCiD search button is not present or button text not matching in Author search page.");
				logFailureDetails(test, e, "ORCiD Search button is not displayed", "orcid_button_fail");
			}

			try {
				// Verify Welcome Text
				Assert.assertTrue(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WELCOME_TEXT_XPATH).isDisplayed(),
						"Welcome text is not displayed");
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WELCOME_TEXT_XPATH).getText(),
						welcome_text, "Welcome text not matching");
				test.log(LogStatus.PASS, "Welcome text is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				e.printStackTrace();
				test.log(LogStatus.FAIL,
						"Welcome text is not present or its not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Welcome text is not displayed", "welcome_text_fail");
			}

			try {
				// Verify search suggestion text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_SEARCH_SUGGESTION_TEXT_XPATH).isDisplayed(),
						"Search suggestion text is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_SEARCH_SUGGESTION_TEXT_XPATH).getText(), search_suggestion_text,
						"Search suggestion text not matching");
				test.log(LogStatus.PASS,
						"Search suggestion text is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Search suggestion text is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Search suggestion text is not displayed", "search_suggestion_text_fail");
			}

			try {
				// Verify Lastname textbox inner text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_INNERTEXT_XPATH).isDisplayed(),
						"Lastname textbox is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_INNERTEXT_XPATH).getText(), last_name_inner_text,
						"Lastname textbox inner text not matching");
				test.log(LogStatus.PASS,
						"Lastname textbox inner text is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Lastname textbox inner text is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Lastname textbox is not displayed", "ln_textbox_fail");
			}

			try {
				// Verify Firstname textbox inner text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_FIRSTSTNAME_INNERTEXT_XPATH).isDisplayed(),
						"Firstname textbox is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_FIRSTSTNAME_INNERTEXT_XPATH).getText(),
						first_name_inner_text, "Firstname textbox inner text not matching");
				test.log(LogStatus.PASS,
						"Firstname textbox inner text is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Firstname textbox inner text is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Firstname textbox is not displayed", "fn_textbox_fail");
			}

			try {
				// Verify Add alternate name inner text
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH).isDisplayed(),
						"Add alternate name button is not displayed");
				Assert.assertEquals(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH).getText(), add_alt_name_text,
						"Add alternate name inner text not matching");
				test.log(LogStatus.PASS,
						"Include alternate name inner text is present and is displayed as expected in Author search page.");
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Include alternate name inner text is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Add alternate name button is not displayed", "alt_name_btn_fail");
			}

			try {
				// Verify Find button
				Assert.assertTrue(
						pf.getBrowserActionInstance(ob)
								.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isDisplayed(),
						"Find button is not displayed");
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob)
								.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).getText(),
						find_btn_text, "Find button text not matching");
				test.log(LogStatus.PASS,
						"Find button text is present and is displayed as expected in Author search page.");
				pf.getBrowserActionInstance(ob).closeBrowser();
			} catch (AssertionError e) {
				test.log(LogStatus.FAIL,
						"Find button text is not present or is not displayed as expected in Author search page.");
				logFailureDetails(test, e, "Find button is not displayed", "find_btn_fail");
			}
		} catch (Exception e) {
			logFailureDetails(test, e, "Author Search Fail", "author_search_fail");
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
