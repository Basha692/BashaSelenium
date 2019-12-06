package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

/**
 * Class for Performing Authoring Comments Validation with Unsupported HTML tags
 * except <br>
 * ,<b>,<i> and
 * <p>
 * 
 * @author UC202376
 *
 */
public class Authoring79 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static boolean master_condition;

	static int time = 30;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		runmodes = TestUtil.getDataSetRunmodes(authoringxls, "deek_linking");
	}

	@Test
	public void testOpenApplication() throws Exception {
		try {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			master_condition = suiteRunmode && testRunmode;

			if (!master_condition) {
				status = 3;
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				throw new SkipException(
						"Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
			}

			// test the runmode of current dataset
			count++;
			if (runmodes[count].equalsIgnoreCase("N")) {
				test.log(LogStatus.INFO, "Runmode for test set data set to no " + count);
				skip = true;
				throw new SkipException("Runmode for test set data set to no " + count);
			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Article_Search_not_happening")));
			// closeBrowser();
		}
	}

	public void reportDataSetResult() {
		/*
		 * if(skip) { TestUtil.reportDataSetResult(authoringxls,
		 * this.getClass().getSimpleName(), count+2, "SKIP"); } else if(fail) {
		 * status=2; TestUtil.reportDataSetResult(authoringxls,
		 * this.getClass().getSimpleName(), count+2, "FAIL"); } else {
		 * TestUtil.reportDataSetResult(authoringxls,
		 * this.getClass().getSimpleName(), count+2, "PASS"); }
		 */

		skip = false;
		fail = false;

	}

	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases"
		 * , TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()),
		 * "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(authoringxls, "Test Cases"
		 * , TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()),
		 * "SKIP");
		 */
	}

	@Test(dependsOnMethods = "testOpenApplication", dataProvider = "getTestData")
	public void test(String url, String recordType) throws Exception {
		WebDriver driver = null;
		try {

			test.log(LogStatus.INFO, this.getClass().getSimpleName()
					+ "  UnSupported HTML Tags execution starts for data set #" + (count + 1) + "--->");

			driver = LoginTR.launchBrowser();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.get(host + url);

			pf.getLoginTRInstance(driver).loginWithFBCredentials(driver, "test_dahbrrq_user@tfbnw.net",
					"auth123*");
			//BrowserWaits.waitTime(10);
			waitForPageLoad(driver);
			// Temprorary solution for micro url
			//String hosts = "https://apps.dev-stable.clarivate.com";
			try {
				String hosts = "https://apps.dev-stable.clarivate.com";
				Assert.assertEquals(driver.getCurrentUrl(), hosts + url);
				test.log(LogStatus.PASS, "Deep linking url is matching after login for " + recordType);
				Assert.assertEquals(getRecordType(driver), recordType);
				test.log(LogStatus.PASS, "Deep linking is redirecting to the appropriate page for " + recordType);
				BrowserWaits.waitTime(20);
				jsClick(driver, driver
						.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
				new WebDriverWait(driver, time)
						.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign out")));
				jsClick(driver, driver
						.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString())));

				//BrowserWaits.waitTime(3);

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Deep linking not working for" + recordType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Article_Search_not_happening")));
			// closeBrowser();
		} finally {
			if (driver != null)
				driver.quit();
			reportDataSetResult();
			++count;
		}
	}

	public String getRecordType(WebDriver driver) {
		waitForElementTobeVisible(driver,
				By.cssSelector("div[class='ne-publication__header'] h3[class*='wui-super-header']"), 80);
		return driver.findElement(By.cssSelector("div[class='ne-publication__header'] h3[class*='wui-super-header']"))
				.getText();
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(authoringxls, "deek_linking");
	}

}
