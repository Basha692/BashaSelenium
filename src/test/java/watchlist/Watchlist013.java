package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

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

/**
 * Verify that document count gets decreased in the watchlist page when a item is deleted from watchlist
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist013 extends TestBase {

	static int status = 1;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Watchlist");

	}

	@Test
	public void testItemsCountInWatchlist() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			// Opening browser
			openBrowser();
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			ob.navigate().to(host);
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Searching for post
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("biology");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.xpath("//a[@class='ng-binding']"), 60);
			Thread.sleep(3000);

			// Getting watch button list for posts
			List<WebElement> watchButtonList = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image")));

			// Watching 5 item to a particular watch list
			for (int i = 0; i < 2; i++) {
				watchButtonList = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image")));
				WebElement watchButton = watchButtonList.get(i);
				watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
				((JavascriptExecutor) ob).executeScript("arguments[0].scrollIntoView(true);", watchButton);
				BrowserWaits.waitTime(2);
			}

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			waitForPageLoad(ob);
			// Getting the items count
			int itemCount = Integer
					.parseInt(ob.findElement(By.xpath(OR.getProperty("itemsCount_in_watchlist"))).getText());

			try {
				Assert.assertEquals(itemCount, 2);
				test.log(LogStatus.INFO, "User is able to watch 2 items into watchlist");
			} catch (Exception e) {
				status = 2;
				test.log(LogStatus.INFO, "User is not able to watch 2 items into watchlist");
			}

			// Unwatching the first 3 document from watch list page
			watchButtonList = ob.findElements(By.xpath("//span[contains(text(),'Watching')]"));
			for (int i = 0; i < 1; i++) {
				watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButtonList.get(i));
				BrowserWaits.waitTime(2);
			}
			ob.navigate().refresh();

			itemCount = Integer.parseInt(ob.findElement(By.xpath(OR.getProperty("itemsCount_in_watchlist"))).getText());

			try {
				Assert.assertEquals(itemCount, 1);
				test.log(LogStatus.PASS, "Items counts is decreased  after unwatching ");
			} catch (Throwable e) {
				status = 2;
				test.log(LogStatus.FAIL, "Items counts is not decreased by  unwatching");
			}

			// Deleting the watch list
			deleteParticularWatchlist(newWatchlistName);

			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
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

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(suiteExls, "Test Cases" , TestUtil.getRowNum(suiteExls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(suiteExls,
		 * "Test Cases", TestUtil.getRowNum(suiteExls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(suiteExls, "Test Cases", TestUtil.getRowNum(suiteExls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
