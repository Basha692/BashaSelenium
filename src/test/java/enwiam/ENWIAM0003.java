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

public class ENWIAM0003 extends TestBase {

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
			loginTofb();
			closeBrowser();
			pf.clearAllPageObjects();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Unexpected error");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void steamLocked() throws Exception {
		String str = "Your account has been locked.";
		String locked = "";
		ob.navigate().to(host);
		ob.findElement(By.name("loginEmail")).sendKeys(LOGIN.getProperty("UserLockedENWIAM0003"));
		for (int i = 0; i <= 10; i++) {
			ob.findElement(By.name("loginPassword")).sendKeys("asdfgh");
			ob.findElement(By.xpath("//span[contains(text(),'Sign in')]")).click();
			Thread.sleep(2000);
		}
		BrowserWaits.waitTime(2);
		locked = ob.findElement(By.xpath(OnePObjectMap.LOCKED_MSG_XPATH.toString())).getText();
		BrowserWaits.waitTime(2);
		if (locked.equalsIgnoreCase(str)) {
			test.log(LogStatus.PASS, "The locked string is displayed, the account got locked on Neon");
		}

		else {
			test.log(LogStatus.FAIL, "The locked string is not displayed, the account is not locked on Neon");
		}
		waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.LOCKED_OK_BUTTON_XPATH.toString()), 30);
		ob.findElement(By.xpath(OnePObjectMap.LOCKED_OK_BUTTON_XPATH.toString())).click();

	}

	private void loginTofb() throws Exception {

		// Navigate to TR login page and login with valid TR credentials

		String str = "Your account has been locked.";
		String locked = "";
		pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("UserLockedENWIAM0003"),
				LOGIN.getProperty("PWDLockedENWIAM0003"));
		pf.getENWReferencePageInstance(ob).didYouKnow(LOGIN.getProperty("PWDLockedENWIAM0003"));
		BrowserWaits.waitTime(2);
		locked = ob.findElement(By.xpath(OnePObjectMap.LOCKED_MSG_XPATH.toString())).getText();

		if (locked.equalsIgnoreCase(str)) {
			test.log(LogStatus.PASS, "STeAM account is in a locked status and can not be linked");

		} else {
			test.log(LogStatus.FAIL, "STeAM account is not in a locked status");
		}
		waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.LOCKED_OK_BUTTON_XPATH.toString()), 30);
		ob.findElement(By.xpath(OnePObjectMap.LOCKED_OK_BUTTON_XPATH.toString())).click();
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
