package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import Authoring.LoginTR;
import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

public class Search130 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static boolean master_condition;

	static int time = 30;
	public String host1="https://apps.dev-stable.clarivate.com";
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Search suite");
	}

	@Test
	public void testOpenApplication() throws Exception {
		try {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			master_condition = suiteRunmode && testRunmode;
			runmodes = TestUtil.getDataSetRunmodes(searchxls, "deep_linking");

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
		 * if(skip) { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2, "SKIP"); }
		 * else if(fail) { status=2; TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "FAIL"); } else { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "PASS"); }
		 */

		skip = false;
		fail = false;

	}

	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

	@Test(dependsOnMethods = "testOpenApplication", dataProvider = "getTestData")
	public void test(String url,
			String recordType) throws Exception {
		WebDriver driver = null;
		try {

			test.log(LogStatus.INFO, this.getClass().getSimpleName() + "  deeplinking execution starts for data set #"
					+ (count + 1) + "--->");

			driver = LoginTR.launchBrowser();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.get(host1+url);

			pf.getLoginTRInstance(driver).loginWithFBCredentials(driver, LOGIN.getProperty("SEARCHFBSOCIALLOGINEMAIL"),
					LOGIN.getProperty("SEARCHFBSOCIALLOGINPWD"));

			try {
				System.out.println("current host "+driver.getCurrentUrl());
				Assert.assertEquals(driver.getCurrentUrl(), host1+url);
				System.out.println("host url"+host1 + url);
				test.log(LogStatus.PASS, "Deep linking url is matching after login " + recordType);
				Assert.assertTrue(isSearchPageDisplayed(driver));
				test.log(LogStatus.PASS, "Deep linking is redirecting to the appropriate page " + recordType);
				BrowserWaits.waitTime(5);
				jsClick(driver, driver
						.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
				new WebDriverWait(driver, time)
						.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign out")));
				jsClick(driver, driver
						.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString())));

				BrowserWaits.waitTime(3);

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Deep linking not working for");
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

	public boolean isSearchPageDisplayed(WebDriver driver)

	{
		waitForAllElementsToBePresent(driver, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()),
				180);
		List<WebElement> itemList = driver
				.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));
		if (itemList.size() > 0)
			return true;
		else
			return false;
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(searchxls, "deep_linking");
	}

}
