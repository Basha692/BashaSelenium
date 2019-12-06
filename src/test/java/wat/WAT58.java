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
 * /**
 * Class for validate Privacy link under Profile Menu
 * @author UC202376
 */
 
public class WAT58 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception
	 *             , When Something unexpected
	 */

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("WAT");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	@Parameters({"username","password"})
	public void testLoginWATApp(String username,String password) throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host + CONFIG.getProperty("appendWATAppUrl"));
			test.log(LogStatus.INFO, "Login to WAT Applicaton with valid login Credentails");
			pf.getLoginTRInstance(ob).loginToWAT(username, password, test);
			pf.getSearchAuthClusterPage(ob).validateAuthorSearchPage(test);
			
		} catch (Throwable t) {
			logFailureDetails(test, t, "Login Fail", "login_fail");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
	}
	
	/**
	 * Method for validate WAT Profile flyout Privacy link
	 * @param proflieFlyoutLinks
	 * @throws Exception, When Profile flyout Privacy link is not working 
	 */
	@Test(dependsOnMethods = "testLoginWATApp")
	@Parameters("privacyLink")
	public void validateprivacyLink(String privacyLink) throws Exception {
		try {
			test.log(LogStatus.INFO, "WAT Profile Flyout - Privacy link validation");
			pf.getWatProfilePage(ob).validateProflileFlyoutLinks(test,privacyLink);
			test.log(LogStatus.PASS, "WAT Profile Flyout - Privacy link validaton Successful");
			closeBrowser();
		} catch (Throwable t) {
			logFailureDetails(test, t, "Profile flyout Privacy link is not working", "flyout_Privacy_is_not_working");
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
