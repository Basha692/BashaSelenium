package ipa;

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

public class IPA21 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPA");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	@Parameters({"username","password"})
	public void testLoginIPAApp(String username,String password) throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			test.log(LogStatus.INFO, "Login to IPA Applicaton with valid login Credentails");
			pf.getIpaPage(ob).loginToIPA(username, password);
			test.log(LogStatus.PASS, "User is logged to IPA application successfully");
			
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
	 * Method for validate IPA Profile flyout links
	 * @param proflieFlyoutLinks
	 * @throws Exception, When Profile flyout links are not working 
	 */
	@Test(dependsOnMethods = "testLoginIPAApp")
	@Parameters("proflieFlyoutLinks")
	public void validateIPAProfileFlyout(String proflieFlyoutLinks) throws Exception {
		try {
			test.log(LogStatus.INFO, "IPA Profile Flyout Info - Header and Footer validation");
			pf.getProfilePageInstance(ob).validateDRAProfileFlyout(proflieFlyoutLinks,test);
			test.log(LogStatus.PASS, "IPA Profile Flyout Info - Header and Footer validation Successful");
			
			test.log(LogStatus.INFO, "Validate IPA Profile page from Profile flyout");
			pf.getProfilePageInstance(ob).clickProfileTitleLink();
			pf.getProfilePageInstance(ob).validateDRAProfilePageAndClose(test);
			
			test.log(LogStatus.PASS, "IPA Profile page landing succefully from Profile flyout");
			

			test.log(LogStatus.INFO, "Validate IPA Account link from Profile flyout");
			pf.getDraPageInstance(ob).clickOnAccountLinkDRA();
			pf.getProfilePageInstance(ob).validateAccountLinkModalAndClose();
			test.log(LogStatus.PASS, "Validate IPA Account link from Profile flyout is Successful");
			
			pf.getDraPageInstance(ob).logoutDRA();
			pf.getIpaPage(ob).landingScreenIPA();
			test.log(LogStatus.PASS, "Logout from IPA Successfully");
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
