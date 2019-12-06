package enwiam;

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
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

// OPQA-1848 -Verify that,an error message should display as "email activation",when
//User did'nt activate the link in that respective mail after completing the registration process in ENW.


public class ENWIAM0009 extends TestBase {
	
	// Following is the list of status:
		// 1--->PASS
		// 2--->FAIL
		// 3--->SKIP
		// Checking whether this test case should be skipped or not
		static boolean fail = false;
		static boolean skip = false;
		static int status = 1;

		static int time = 30;
		PageFactory pf=new PageFactory();
		
		@BeforeTest
		public void beforeTest() throws Exception {
			extent = ExtentManager.getReporter(filePath);
			rowData = testcase.get(this.getClass().getSimpleName());
			test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

		}
		
		
		@Test
		public void testLogin() throws Exception {
			
			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			boolean master_condition = suiteRunmode && testRunmode;

			if (!master_condition) {

				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

			}
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
			try {
				
				openBrowser();
				maximizeWindow();
				clearCookies();
				String expectedmessage="Please activate your account";
				String expectedbuttontext="Resend activation";
		
//				String expectedOKbuttontext="Sign in ";
			
				ob.navigate().to(host+CONFIG.getProperty("appendENWAppUrl"));
				pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("FBuserENWIAM0009"), LOGIN.getProperty("FBpwdENWIAM0009"));
				
				
				waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAl_CSS.toString()), 300);
				pf.getENWReferencePageInstance(ob).didYouKnow(LOGIN.getProperty("FBpwdENWIAM0009"));
				BrowserWaits.waitTime(5);
				//ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString())).click();
				waitForElementTobeVisible(ob, By.cssSelector("h2[class='login-title']"), 300);
				String actualmessage=pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS).getText();
				String actualbuttontext=pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.ENW_RESEND_ACTIVATION_BUTTON_CSS).getText();
				
				
				
				try{
				Assert.assertEquals(actualmessage, expectedmessage);
				Assert.assertEquals(actualbuttontext, expectedbuttontext);
				//Assert.assertEquals(actualOKbuttontext, expectedbuttontext);
				test.log(LogStatus.PASS,
						"Facebook account is not linked with unverified Steam account ");
				}
				catch (Throwable t) {

					t.printStackTrace();
					test.log(LogStatus.FAIL, "User is not able to see activation message or Resend activation button or OK button");
					ErrorUtil.addVerificationFailure(t);

				}
				
				try{
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_RESEND_ACTIVATION_BUTTON_CSS);
					waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_RESEND_ACTIVATION_DONE_BUTTON_CSS.toString()), 30);
					test.log(LogStatus.PASS,
							"OK button is getting displayed when user clicks on resend activation ");
					}
					catch (Throwable t) {

						t.printStackTrace();
						test.log(LogStatus.FAIL, "User is not able to see  OK button");
						ErrorUtil.addVerificationFailure(t);

					}
				
				
				closeBrowser();
			//	pf.clearAllPageObjects();
				
			} 
			catch (Throwable t) {
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
			}

}
