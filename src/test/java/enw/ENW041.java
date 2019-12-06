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

public class ENW041 extends TestBase {
	static int status = 1;

	// Following is the list of status:

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW041() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to("http://tsenwqa01.int.westgroup.com:4444/enwservices/supportutility/");
			loginToSupport_utility("SUPPORTUTILITYACCOUNT", "SUPPORTUTILITYACCOUNT_PASSWD");
			String expected = "Login successful";
			String actualText = ob.findElement(By.cssSelector("h3")).getText();
			try {
				Assert.assertEquals(expected, actualText);
				logger.info("Support Utility Home page as displayed as:" + actualText);
				test.log(LogStatus.PASS,
						" User is able to Login to Support Utility Home page by using valid credentials");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, " Header Logo text is not displayed properly for Non-Market users");// extent
				ErrorUtil.addVerificationFailure(t);// testng reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));
				closeBrowser();
			}
			VerifyTheCheckList("SUTILITYCUSTOMER");
			//BrowserWaits.waitTime(4);
			// Test();
			Test1(LOGIN.getProperty("SUTILITYCUSTOMER"));
			//BrowserWaits.waitTime(4);
			LoginToENW();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
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

	}

	private void LoginToENW() throws Exception {
		String header_Expected = "Thomson Reuters Project Neon";
		ob.get("https://dev-stable.1p.thomsonreuters.com/#/login?app=endnote");
		pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("SUTILITYCUSTOMER"),
				(LOGIN.getProperty("SUPPORTUTILITYPWD")));
		//BrowserWaits.waitTime(9);
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Thomson Reuters",
		// "EndNote", "Downloads", "Options");
//		String actual_result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HEADER_XPATH).getText();
//		logger.info("Header Text displayed as:" + actual_result);
//		logger.info("Actual result displayed as :" + actual_result
//				+ " text without the hot link and not allow user to Navigate to Neon");
//		try {
//			Assert.assertEquals(header_Expected, actual_result);
//			test.log(LogStatus.PASS,
//					"community enabled version of ENDNOTE logo has been displayed for the Market users");
//		} catch (Throwable t) {
//			test.log(LogStatus.FAIL, "community enabled version of ENDNOTE logo is not displayed for the Market users");// extent
//			ErrorUtil.addVerificationFailure(t);// testng reports
//			status = 2;// excel
//			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
//					this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
//		}
		//BrowserWaits.waitTime(3);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
		//BrowserWaits.waitTime(3);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH);
		ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH.toString())).click();
		closeBrowser();
	}

	private void Test1(String CnameForSearch) throws Exception {
		String str = ob.findElement(By.xpath("//form[@name='removeForm']//h2")).getText();
		//BrowserWaits.waitTime(4);
		try {
			if (str.contains(CnameForSearch)) {
				logger.info("Support Utility Home page as displayed as:" + str);
				System.out.println("Support Utility" + str);
				test.log(LogStatus.PASS, CnameForSearch + "  is a member of the Market user Customer groups:");
				jsClick(ob, ob
						.findElement(By.xpath("//a[contains(text(),'Logout mohana.yalamarthi@thomsonreuters.com')]")));
			} else {
				test.log(LogStatus.FAIL, CnameForSearch + "  is Not a member of the Market user Customer groups:");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");

			// extent
			ErrorUtil.addVerificationFailure(t);// testng reports
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
			closeBrowser();
		}
	}

	private void VerifyTheCheckList(String CnameForSearch) {
		jsClick(ob, ob.findElement(By.xpath("//a[contains(text(),'Customer Associations')]")));
		waitForElementTobeVisible(ob, By.xpath("//input[@type='text']"), 100);
		ob.findElement(By.xpath("//input[@type='text']")).clear();
		ob.findElement(By.xpath("//input[@type='text']")).sendKeys(LOGIN.getProperty(CnameForSearch));
		jsClick(ob, ob.findElement(By.cssSelector("input[type='button']")));

	}

	// private void Test() throws Exception {
	// String celltext = "";
	// WebElement mytable =
	// ob.findElement(By.xpath("//table[@class='searchResults']"));
	// List<WebElement> rows_table = mytable.findElements(By.tagName("tr"));
	// int rows_count = rows_table.size();
	// // Loop will execute for all the rows of the table
	// for (int row = 0; row < rows_count; row++) {
	// // To locate columns(cells) of that specific row.
	// List<WebElement> Columns_row =
	// rows_table.get(row).findElements(By.tagName("td"));
	// // To calculate no of columns(cells) In that specific row.
	// int columns_count = Columns_row.size();
	// System.out.println("Number of cells In Row " + row + " are " +
	// columns_count);
	// // Loop will execute till the last cell of that specific row.
	// for (int column = 0; column < columns_count; column++) {
	// // To retrieve text from the cells.
	// celltext = Columns_row.get(column).getText();
	// System.out.println(
	// "Cell Value Of row number " + row + " and column number " + column + " Is
	// " + celltext);
	//
	// }
	// }
	// try {
	// Assert.assertEquals(celltext.equalsIgnoreCase("ENW"), "ËNW");
	// logger.info("Support Utility Home page as displayed as:" + celltext);
	// test.log(LogStatus.PASS, " Support Utility Home page as displayed as:" +
	// celltext);
	// } catch (Throwable t) {
	// test.log(LogStatus.FAIL, " Header Logo text is not displayed properly for
	// Non-Market users");// extent
	// ErrorUtil.addVerificationFailure(t);// testng reports
	// status = 2;// excel
	// test.log(LogStatus.INFO, "Snapshot below: " +
	// test.addScreenCapture(captureScreenshot(
	// this.getClass().getSimpleName() + "Header Text is displayed wrongly and
	// its Hyperlinked")));// screenshot
	// }
	//
	// }

	private void loginToSupport_utility(String usernameKey, String pwdKey) {
		waitForElementTobeVisible(ob, By.name("username"), 180);
		ob.findElement(By.cssSelector("input[type='text']")).clear();
		ob.findElement(By.cssSelector("input[type='text']")).sendKeys(LOGIN.getProperty(usernameKey));
		ob.findElement(By.cssSelector("input[type='password']")).sendKeys(LOGIN.getProperty(pwdKey));
		jsClick(ob, ob.findElement(By.cssSelector("input[type='submit']")));

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
