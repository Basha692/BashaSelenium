package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class DRAIAMSSO008 extends TestBase{
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception , When Something unexpected
	 */

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");
	}

	/**
	 * User is already associated with a subscription for a given customer for a given app (UnP, tries SSO).
	 * @throws Exception 
	 */
  @Test
  public void testcaseDRASSO8() throws Exception {
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
		String actualErroeMsg="Please sign in to Drug Research Advisor with the email address and password used to create your Clarivate Analytics account.";
		String contactCustomercareMsg="Questions? Contact Drug Research Advisor Customer Care.";
     
      try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("dra_sso_url"));
			test.log(LogStatus.PASS, "Sucessfully launched DRASSO landing page.");
			pf.getDraSSOPageInstance(ob).loginDRAApp(LOGIN.getProperty("DRASSOSUBFLW008"),LOGIN.getProperty("DRASSOPASSWORD"));
			//Verify Error message when user is associated with more active UNP Subscription.
			try{
				waitForElementTobePresent(ob, By.xpath(OnePObjectMap.DRA_SSO_LOGIN_PAGE_WITH_SUBSCRIPTION.toString()), 60);
				
				String expectedString=ob.findElement(By.xpath(OnePObjectMap.DRA_SSO_LOGIN_PAGE_WITH_SUBSCRIPTION.toString())).getText();
				Assert.assertEquals(actualErroeMsg, expectedString);
			String expectedContCustomecare=ob.findElement(By.xpath(OnePObjectMap.DRA_SSO_LOGIN_PAGE_QUESTIONCONTACT.toString())).getText();
			Assert.assertEquals(contactCustomercareMsg, expectedContCustomecare);
			test.log(LogStatus.PASS, "Actaul and Expected erro message are matching.");}
			catch(Exception e){
				test.log(LogStatus.FAIL, "Actaul and Expected error message are not matching.");
			}		
			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

}
}
