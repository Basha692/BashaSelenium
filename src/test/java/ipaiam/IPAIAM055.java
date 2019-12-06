package ipaiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class IPAIAM055 extends TestBase {

	static int status = 1;
	static boolean fail = false;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");

	}

	@Test
	public void testcase() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			String accountType = "Facebook";
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("FBuserENWIAM0009"),
					LOGIN.getProperty("FBpwdENWIAM0009"));
			test.log(LogStatus.PASS, "user has logged in with social account");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);

			pf.getHFPageInstance(ob).clickOnAccountLink();
			BrowserWaits.waitTime(2);

			validateAccountsFB(2, accountType);
			BrowserWaits.waitTime(5);
			pf.getIpaPage(ob).clickIPALink();
			test.log(LogStatus.PASS, "STeAM Step Up Auth Modal is displayed");

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString()), 30);
			WebElement Emailaddress = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_USERNAME_CSS);
			if (Emailaddress.getAttribute("value").equals("jihipe@30wave.com")) {
				test.log(LogStatus.PASS,
						"The STeAM Step Up Auth Modal is presented to the user with a pre-populated email address when user does not have a linked STeAM account.");
			} else {
				test.log(LogStatus.FAIL,
						"The STeAM Step Up Auth Modal is presented to the user without a pre-populated email address when user does not have a linked STeAM account.");
			}
			//pf.getDraPageInstance(ob).validateProductOverviewPage(test);
			pf.getDraPageInstance(ob).clickDRAStepUpAuthLoginNotEntitledUser(test, "sruthi@1");
			
			BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
			test.log(LogStatus.PASS, "IPA Home page displayed after entering steam account details");
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent

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

	private void validateAccountsFB(int accountCount, String linkName) throws Exception {
		try {

			Assert.assertTrue(
					pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, LOGIN.getProperty("FBuserENWIAM0009")));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Social account is linked with steam account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"Linked accounts are available in accounts page : Neon and " + linkName + " accounts");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
}
