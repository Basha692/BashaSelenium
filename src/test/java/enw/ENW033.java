package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

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

public class ENW033 extends TestBase {

	static int status = 1;
	static boolean fail = false;
	static boolean skip = false;
	static int time = 30;
	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not

	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseA13() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		// Verify that TERMS OF USE and PRIVACY STATEMENT links are working correctly in Singn In Page


		try {
			openBrowser();
			maximizeWindow();
			ob.navigate().to(host);
			

			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("fbuserenw033"),
					LOGIN.getProperty("fbpwdenw033"));
			
			pf.getBrowserWaitsInstance(ob)
			.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
	pf.getHFPageInstance(ob).clickOnAccountLink();
	waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ACCOUNT_LINK_BUTTON_XPATH.toString()), 60);
	ob.findElement(By.xpath(OnePObjectMap.ACCOUNT_LINK_BUTTON_XPATH.toString())).click();

	waitForElementTobeVisible(ob, By.name(OnePObjectMap.LINK_LOGIN_NAME.toString()), 120);
	//BrowserWaits.waitTime(3);
	ob.findElement(By.name("email")).sendKeys(LOGIN.getProperty("steamuserenw033"));
	ob.findElement(By.name("password")).sendKeys(LOGIN.getProperty("steampwdenw033"));

	ob.findElement(By.xpath(OnePObjectMap.DONE_BUTTON_CLICK_XPATH.toString())).click();
	//BrowserWaits.waitTime(8);
	try {
		
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAlTEXT_CSS);
		String actual_msg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.SEARCH_RESULTS_PAGE_LINKIINGMODAlTEXT_CSS)
				.getText();
		if (actual_msg.contains("We're sorry. We are unable to link your accounts."))
			test.log(LogStatus.PASS,
					"Another  facebook account cannot be linked with steam account which is already with facebook account");
	}

	catch (Throwable t) {

		test.log(LogStatus.FAIL, "Another  facebook account is linked with steam account which is already with facebook account");// extent
		// reports
		status = 2;// excel
		test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this
				.getClass().getSimpleName()
				+ "_unable_to_link")));// screenshot
		ErrorUtil.addVerificationFailure(t);

	}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

		}  catch (Throwable t) {

			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
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
