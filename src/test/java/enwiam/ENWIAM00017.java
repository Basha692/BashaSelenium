package enwiam;

import org.openqa.selenium.By;
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

public class ENWIAM00017 extends TestBase {

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

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			steamLocked();
            closeBrowser();
			pf.clearAllPageObjects();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Unexpected error");// extent
			ErrorUtil.addVerificationFailure(t);
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void steamLocked() throws Exception {
		String str = "Your account has been locked.";
		String locked = "";
		ob.navigate().to(host + CONFIG.getProperty("appendENWAppUrl"));

		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString()))
				.sendKeys(LOGIN.getProperty("ENWIAM00015steamUser"));

		for (int i = 0; i <= 9; i++) {
			ob.findElement(By.name("loginPassword")).sendKeys("asdfgh");
			ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS.toString()))
					.sendKeys("asdfgh");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			Thread.sleep(2000);
		}
		//BrowserWaits.waitTime(3);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString()), 30);
		locked = ob.findElement(By.cssSelector(OnePObjectMap.ENW_UNVERIFIED_MESSAGE_BUTTON_CSS.toString())).getText();
		//BrowserWaits.waitTime(2);
		if (locked.equalsIgnoreCase(str)) {
			test.log(LogStatus.PASS, "The locked string is displayed, the account got locked on ENW");
		}

		else {
			test.log(LogStatus.FAIL, "The locked string is not displayed, the account is not locked on ENW");
		}

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
