package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

//Call Delete API method  
//Login to Neon Application using Steam account 
//Logout from application 
// Login with social with same Steam account.
//1)Clicking[x] on Neon side Linking Modals-OPQA-2329  - ENWIAM011
//2)Clicking 'not now' from Neon side Linking Modals-OPQA-2331
// 3)Clicking outside the Modal from Neon side Linking Modals-OPQA-2292
public class ENW023 extends TestBase {
	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW023() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		String expected_URL = "https://access.dev-stable.clarivate.com/#/login?referrer=%252Fcmty%252F%2523%252Fhome&app=cmty&pageview=";
		String uRl="";
		//String neonHomePage = "https://dev-stable.1p.thomsonreuters.com/#/home";
		//String Expected_Result = "Sign in";
		if (!master_condition) {
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("STEAMUSEREMAIL"));
			Assert.assertTrue(statuCode.equalsIgnoreCase("200")||statuCode.equalsIgnoreCase("400"));
			
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("STEAMUSEREMAIL"),
					LOGIN.getProperty("STEAMUSERPASSWORD"));
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		    pf.getLoginTRInstance(ob).clickLogin();
			//closeOnBoardingModal();
			pf.getLoginTRInstance(ob).logOutApp();
			//BrowserWaits.waitTime(5);
			pf.getLoginTRInstance(ob).loginWithFBCredentials1(LOGIN.getProperty("STEAMUSEREMAIL"),
					LOGIN.getProperty("STEAMUSERPASSWORD"));
				//BrowserWaits.waitTime(5);
				//ob.findElement(By.xpath("//div[@class='modal-content ng-scope']")).isDisplayed();
				uRl=ob.findElement(By.xpath("//h3[@class='wui-modal__title']")).getText();
				Assert.assertEquals(uRl, "Did you know?");
//				ob.findElement(By.xpath("//h3[@class='wui-modal__title']")).click(); 
				//BrowserWaits.waitTime(5);
				ob.findElement(By.xpath("//button[@class='wui-modal__close-btn']")).click();
				String exp_uRl=ob.getCurrentUrl().toString();
				logger.info(exp_uRl);
				try {
				Assert.assertEquals(exp_uRl, expected_URL);
				test.log(LogStatus.PASS, "Expected page is displayed and  Navigating to the proper URL.");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Expected page is not displayed and  URL is wrong.");// extent
				ErrorUtil.addVerificationFailure(t); // reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Neon is not displayed and content is not matching")));// screenshot
			}
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
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		closeBrowser();
	}
}
