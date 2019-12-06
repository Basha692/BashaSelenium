package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that user is able to see the watchlist items by content type
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist021 extends TestBase {

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
	public void testWatchlistItemsDisplayedByContentType() throws Exception {

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

			String search_query = "hello";

			// Opening browser
			openBrowser();
			maximizeWindow();
			clearCookies();

			// ob.get(host);
			ob.navigate().to(host);
			loginAsSpecifiedUser(LOGIN.getProperty("USERNAME10"), LOGIN.getProperty("PASSWORD10"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Searching for article
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("tab_articles_result")), 30);

			// Watching an article to a particular watch list
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			WebElement watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(0);
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

//			watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(1);
//			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
			
			// Watching a patents to a particular watch list
			/*pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(0);
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);*/
			
//			watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(1);
//			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
			
			// Watching a posts to a particular watch list
			/*pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(0);
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);*/
//			watchButton = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image"))).get(1);
//			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			waitForPageLoad(ob);

//			List<WebElement> watchedItems = ob
//					.findElements(By.cssSelector("div[class='wui-card__header-left ng-binding']"));
			
			List<WebElement> watchedItems = ob.findElements(By.xpath("//span[@class='ng-binding ng-scope']"));
			List<String> actualWatchedItems = new ArrayList<String>();
			for (WebElement we : watchedItems) {

				actualWatchedItems.add(we.getText());
			}
			logger.info("total watchlists by content type-->"+actualWatchedItems);
			List<String> expectedWatchedItems = Arrays
					.asList(new String[] {"ARTICLE"});

			if (!actualWatchedItems.equals(expectedWatchedItems)) {
				logFailureDetails(test, "Watchlist items are not displayed by content type", this.getClass().getSimpleName()+"_watchlist_items_are_not_displayed_by_content_type");
			} else {
				test.log(LogStatus.PASS, "Watchlist items are displayed by content type");
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
