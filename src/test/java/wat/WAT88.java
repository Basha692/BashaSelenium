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
 * Class for Verify that upon clicking the symbol (-) corresponding LN & FN should be deleted
 * 
 * @author UC225218
 *
 */
public class WAT88 extends TestBase {

	static int status = 1;
	static String wos_title = "Web of Science: Author search";

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
	 * Method to Verify that upon clicking the symbol (-) corresponding LN & FN should be deleted
	 * 
	 * @param LastName, FirstName
	 * @throws Exception,
	 *             When Something unexpected
	 */
	@Test(dependsOnMethods = { "testLoginWATApp" })
	@Parameters({ "LastName", "FirstName"})
	public void testRemoneAltNameBtnFunction(String LastName, String FirstName)
			throws Exception {

		try {
			// Verify whether control is in Author Search page
			Assert.assertEquals(
					pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
					wos_title, "Control is not in WOS Author Search page");
			test.log(LogStatus.INFO, "Control is in WOS Author Search page");

			// Search for an author cluster with only Last name
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH);
			if(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_REMOVE_ALT_NAME_BTN_TEXT_XPATH).isDisplayed())
			{
				test.log(LogStatus.PASS,"Remove Alternate button is displayed and is enabled");
			}
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_REMOVE_ALT_NAME_BTN_TEXT_XPATH).click();
			List<WebElement> ele = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			if (ele.size() == 1)
			{
				test.log(LogStatus.PASS,"Upon clicking the symbol (-) corresponding LN & FN is deleted");
			}
			pf.getBrowserActionInstance(ob).closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Upon clicking the symbol (-) corresponding LN & FN is not deleted", "Remove_altName_Fail");
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