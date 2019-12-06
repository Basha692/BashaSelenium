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

public class DRAIAM6 extends TestBase {

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
			test.log(LogStatus.INFO, "DRA Profile Flyout Info - Header and Footer validation");
			pf.getProfilePageInstance(ob).validateDRAProfileFlyout(proflieFlyoutLinks,test);
			test.log(LogStatus.PASS, "DRA Profile Flyout Info - Header and Footer validation Successful");
			
			test.log(LogStatus.INFO, "Validate DRA Profile page from Profile flyout");
			pf.getProfilePageInstance(ob).clickProfileTitleLink();
			pf.getProfilePageInstance(ob).validateDRAProfilePageAndClose(test);
			
			test.log(LogStatus.PASS, "DRA Profile page landing succefully from Profile flyout");
			

			test.log(LogStatus.INFO, "Validate DRA Account link from Profile flyout");
			pf.getDraPageInstance(ob).clickOnAccountLinkDRA();
			pf.getProfilePageInstance(ob).validateAccountLinkModalAndClose();
			test.log(LogStatus.INFO, "Validate DRA Account link from Profile flyout is Successful");
			
			pf.getDraPageInstance(ob).logoutDRA();
			pf.getDraPageInstance(ob).landingScreenDRA();
			test.log(LogStatus.PASS, "Logout from DRA Successfully");
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Profile flyout Info Validation Fail");
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "flyout_validation_fail")));
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
