package ipaiam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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

public class IPAIAM052 extends TestBase {

	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPAIAM");
	}

	/**
	 * 
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseDRA4() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		
		try {
			String statuCode = deleteUserAccounts(LOGIN.getProperty("USERDRA052"));
			
			if (!(statuCode.equalsIgnoreCase("200") || statuCode.equalsIgnoreCase("400"))) {
				// test.log(LogStatus.FAIL, "Delete accounts api call failed");
				throw new Exception("Delete API Call failed");
			}


		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Delete accounts api call failed");// extent
			ErrorUtil.addVerificationFailure(t);
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).loginWithFBCredentials(LOGIN.getProperty("USERDRA052"),
					LOGIN.getProperty("USERPWDDRA052"));
			test.log(LogStatus.PASS, "user has logged in with social account in Neon");
			pf.getHFPageInstance(ob).clickOnAccountLink();
			pf.getLoginTRInstance(ob).logOutApp();
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);
			//BrowserWaits.waitTime(5);

			try {
				ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
				ob.navigate().refresh();
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_BRANDING_NAME_CSS);
				pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("USERDRA052"),
						LOGIN.getProperty("USERPWDDRA052"));
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
				//BrowserWaits.waitTime(2);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NOT_NOW_BUTTON_CSS);
				pf.getLinkingModalsInstance(ob).clickOnNotNowButton();
				//BrowserWaits.waitTime(2);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
				test.log(LogStatus.PASS, "User is able to click on not now on the modal");	
				pf.getDraPageInstance(ob).clickOnAccountLinkDRA();
				validateAccounts(1,"Clarivate Analytics");
				pf.getDraPageInstance(ob).logoutDRA();
			} catch (Throwable t) {
				closeBrowser();
				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is not able to click on notnow on the modal");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Not_able_to_link")));
			}
			

			//BrowserWaits.waitTime(2);
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

	
	private void validateAccounts(int accountCount, String linkName) throws Exception {
		try {
			Assert.assertTrue(verifyLinkedAccount(linkName, LOGIN.getProperty("USERDRA052")));
			Assert.assertTrue(validateAccountsCount(accountCount));
			//test.log(LogStatus.PASS,  " account is available and is not linked");
			test.log(LogStatus.PASS, "Single " + linkName + " account is available and is not linked");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Linked accounts are available in accounts page");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}
	
	public boolean validateAccountsCount(int accountCount) {
		waitForAllElementsToBePresent(ob, By.cssSelector("div[class='account-option-item__info-header'] span"), 60);
		List<WebElement> list = ob.findElements(By.cssSelector("div[class='account-option-item__info-header'] span"));
		
		return accountCount==list.size();
	}
	
	public boolean verifyLinkedAccount(String accountType, String emailId) {
		boolean result = false;
		waitForAllElementsToBePresent(ob, By.cssSelector("div[class='account-option-item__info-header'] span"), 60);
		List<WebElement> list = ob.findElements(By.cssSelector("div[class='account-option-item__info-header'] span"));

		for (WebElement element : list) {
			String type = element.getText();
			if ((accountType.equalsIgnoreCase("Neon") && type.equalsIgnoreCase("Thomson Reuters | Project Neon"))
					|| accountType.equalsIgnoreCase(type.trim())) {
			
				//String emailid = null;
				String emailid = ob.findElement(By.cssSelector("div[class='account-option-item__app-details'] span[class='ng-binding']")).getText();
				//String emailid="cattle6@b9x45v1m.com";
				if (emailid.equalsIgnoreCase(emailId))
					result = true;
				
				break;
			}

		}
		return result;
		
		
	}
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
