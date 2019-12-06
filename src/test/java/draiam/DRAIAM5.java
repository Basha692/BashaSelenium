package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

public class DRAIAM5 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	@Parameters({"username","password"})
	public void testLoginDRAApp(String username,String password) throws Exception {
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

			ob.navigate().to(host + CONFIG.getProperty("appendDRAAppUrl"));
			test.log(LogStatus.INFO, "Login to DRA Applicaton with valid login Credentails");
			pf.getDraPageInstance(ob).loginToDRAApplication(username, password);
			test.log(LogStatus.PASS, "User is logged to DRA application successfully");
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Login not done");// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_login_not_done")));// screenshot
			closeBrowser();
		}
	}
	
	/**
	 * Method for validate DRA Profile flyout links
	 * @param proflieFlyoutLinks
	 * @throws Exception, When Profile flyout links are not working 
	 */
	@Test(dependsOnMethods = "testLoginDRAApp")
	@Parameters("proflieFlyoutLinks")
	public void validateAppHeaderFooterLinks(String proflieFlyoutLinks) throws Exception {
		try {
			
			pf.getProfilePageInstance(ob).validateProflileFlyoutLinks(test,proflieFlyoutLinks);
			
			test.log(LogStatus.INFO, "DRA Profile Flyout - Signout link validaton");
			pf.getDraPageInstance(ob).logoutDRA();
			pf.getDraPageInstance(ob).landingScreenDRA();
			test.log(LogStatus.PASS, "DRA Profile Flyout - Signout link validaton Successful");
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Profile flyout links are not working");
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "flyout_links_are_not_working")));
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
