package enwiam;

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
import util.TestUtil;

// OPQA-2389 -Verify that,when STeAM account is trying to be linked by the user is in a "locked" status,
//then the link should not be made and the user should be informed that the STeAM account is locked.

public class ENWIAM00018 extends TestBase {

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

	}

	@Test
	public void testLogin() throws Exception {

		boolean testRunmode = TestUtil.isTestCaseRunnable(enwiamxls, this.getClass().getSimpleName());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}
		
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("UserFBENWIAM80"));
			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
				throw new Exception("Delete API Call failed");
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			steamLogin();
			loginTofb();
			closeBrowser();
			pf.clearAllPageObjects();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Unexpected error");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void steamLogin() throws Exception {

		ob.navigate().to(host);
		ob.findElement(By.name("loginEmail")).sendKeys(LOGIN.getProperty("UserFBENWIAM80"));

		ob.findElement(By.name("loginPassword")).sendKeys(LOGIN.getProperty("PWDUserFBENWIAM80"));
		
		ob.findElement(By.xpath("//span[contains(text(),'Sign in')]")).click();
		pf.getLoginTRInstance(ob).closeOnBoardingModal();
		//pf.getLinkingModalsInstance(ob).clickOnNotNowButton();
		logout();
		//BrowserWaits.waitTime(2);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);

	}

	private void loginTofb() throws Exception {

		pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserFBENWIAM80"),
				LOGIN.getProperty("PWDUserFBENWIAM80"));
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_DIFFSTEAMLINKING_LINKINGMODALTEXT_CSS);
		String ModalText = "Your email address " + LOGIN.getProperty("UserFBENWIAM80")
				+ " is already registered with Project Neon.";
				
		String LInkText = ob
				.findElement(By
						.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_DIFFSTEAMLINKING_LINKINGMODALTEXT_CSS.toString()))
				.getText();
		try {
			Assert.assertEquals(LInkText, ModalText);
			test.log(LogStatus.PASS,
					" 'Did you know ...' Modal text is displayed as per wire frames/Conditional Text and Error Messages.");
		}

		catch (Throwable t) {

			test.log(LogStatus.FAIL,
					" 'Did you know ...' Modal text is not displayed as per wire frames/Conditional Text and Error Messages.");
			// reports
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
					.getSimpleName()
					+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot
			ErrorUtil.addVerificationFailure(t);

		}

		//BrowserWaits.waitTime(2);

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS");
		 * else if(status==2) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL");
		 * else TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
