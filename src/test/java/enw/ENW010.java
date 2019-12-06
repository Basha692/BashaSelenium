package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class ENW010 extends TestBase {

	static int status = 1;
	String header_Expected = "Clarivate Analytics ";

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW010() throws Exception {
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

			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			String actual_result = "";

			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin1(LOGIN.getProperty("NONMARKETUSEREMAIL"),
					LOGIN.getProperty("NONMARKETUSERPASSWORD"));
			
			try {
				String text = ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()))
						.getText();
				if (text.equalsIgnoreCase("Continue")) {
					ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//pf.getBrowserWaitsInstance(ob).waitUntilText("Thomson Reuters", "EndNote", "Downloads", "Options");
			//BrowserWaits.waitTime(10);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH,10);
			ValidateAlternativeHeadr();
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));

			actual_result = ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_NAME_XPATH.toString())).getText();
			// need to move to object map
			String profileLink = ob.findElement(By.cssSelector("li[class='dropdown-header'] a")).getAttribute("class");
			logger.info("is profile image linked -->" + profileLink);
			WebElement ImageFile = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.IMAGE_USER_XPATH);
			Boolean ImagePresent = (Boolean) ((JavascriptExecutor) ob).executeScript(
					"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
					ImageFile);
			try {
				Assert.assertTrue(ImagePresent);
				if (!(StringUtils.isNotEmpty(actual_result) && profileLink.equalsIgnoreCase("inactiveLink"))) {
					throw new Exception("Profile Title and Image and flyout is Hyper linked");
				}

				test.log(LogStatus.PASS, " Image is present and User name is not hyper linked");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, " Image is not present and User is hyperlinked");// extent
				ErrorUtil.addVerificationFailure(t); // reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "mage is not present and User is hyperlinked")));// screenshot
			}

			closeBrowser();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

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
	}

	private void ValidateAlternativeHeadr() throws Exception {
//		BrowserWaits.waitTime(4);
//		String actual_result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HEADER_XPATH).getAttribute("title");
//		logger.info("Header Text displayed as:" + actual_result);
//		String profileLink = ob.findElement(By.cssSelector("h1[class='ne-nav-logo'] a")).getAttribute("class");
//		System.out.println(profileLink);
//		logger.info("is profile image linked -->" + profileLink);
//		logger.info("Actual result displayed as :" + actual_result
//				+ " text without the hot link and not allow user to Navigate to Neon");
//		try {
//			Assert.assertEquals(header_Expected, actual_result);
//		    Assert.assertEquals(profileLink, "inactiveLink");
//			test.log(LogStatus.PASS, " Header Logo text is displayed properly for Non-Market users");
//		} catch (Throwable t) {
//			test.log(LogStatus.FAIL, " Header Logo text is not displayed properly for Non-Market users");// extent
//			ErrorUtil.addVerificationFailure(t);// testng reports
//			status = 2;// excel
//			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
//					this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
//		}
//
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}