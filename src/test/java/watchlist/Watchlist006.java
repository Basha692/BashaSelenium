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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that user is able to add a Patent from Patents content search results page to a particular watchlist||Verify
 * that user is able to unwatch a Patent from watchlist page||Verify that user is able to unwatch a Patent from Patents
 * content search results page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist006 extends TestBase {

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
	@Parameters({"patentName"})
	public void testWatchPatentFromPatentContentSearchResult(String patentName) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		// String email = "linkedinloginid@gmail.com";
		// String password = "1Pproject";
		try {

			// Opening browser
			openBrowser();
			maximizeWindow();
			clearCookies();
			
			ob.navigate().to(host);

			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));
			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			pf.getHFPageInstance(ob).searchForText(patentName);
			BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			// Getting watch button list for patents
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_watchlist_image")), 60);
			List<WebElement> watchButtonList = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image")));
			WebElement watchButton;
			
			// Watching  patents to a particular watch list
			watchButton = watchButtonList.get(0);
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
			((JavascriptExecutor) ob).executeScript("arguments[0].scrollIntoView(true);", watchButton);
			BrowserWaits.waitTime(5);

			// Selecting the patent name
			String firstDocumentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			logger.info("First Document Name : " + firstDocumentName);

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			waitForPageLoad(ob);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));

			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {

				if (watchedItems.get(i).getText().equals(firstDocumentName))
					count++;

			}

			if (!compareNumbers(1, count)) {

				test.log(LogStatus.FAIL,
						"User not able to add an patent into watchlist from Patents content search results page");// extent
				// reports
				status = 2;// excel
				ErrorUtil.addVerificationFailure(new Exception("Item count mismatch"));// testng
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_user_unable_to_add_patent_into_watchlist_from_Patents_content_searchResults_page")));// screenshot

			}

			// Steps2: Removing the first item from watch list page
			firstDocumentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			logger.info("First Document Name : " + firstDocumentName);
			// Unwatching the first document from results
			watchButton = ob.findElement(By.xpath("//button[contains(.,'Watching')]"));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
			BrowserWaits.waitTime(5);
			ob.navigate().refresh();
			List<WebElement> watchlistPatents=ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			if(watchlistPatents.size()>0) {
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);
			}else {
				pf.getBrowserWaitsInstance(ob).waitUntilText("Add articles, patents and posts to your watchlist for easy reference");
			}
			
			count = 0;
			if(watchlistPatents.size()>0) { 
				// Checking if first document still exists in the watch list
				List<WebElement> documentList = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
				String documentTitle;
				for (WebElement document : documentList) {
					documentTitle = document.getText();
					if (documentTitle.equals(firstDocumentName))
						count++;
				 }
			}

			try {
				Assert.assertEquals(0, count);
				test.log(LogStatus.PASS, "User is able to unwatch a Patent from watchlist page");
			} catch (Throwable e) {
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_user_unable_to_remove_patent_from_watchlist_in_Patent_content_searchResults_page")));// screenshot
				ErrorUtil.addVerificationFailure(e);// testng
				test.log(LogStatus.FAIL, "User is unable to unwatch a Patent from watchlist page");
			}

			// Steps3: Unwatching a patent from patent content result page

			// Searching for patents
			pf.getHFPageInstance(ob).searchForText("hello");
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();

			// Watching a patent to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
			BrowserWaits.waitTime(4);
			// Unwatching a patent to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the patent name
			String documentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			try {
				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
				if(watchedItems.size() > 0) {
				
				for (int i = 0; i < watchedItems.size(); i++) {

					if (watchedItems.get(i).getText().equals(documentName))
						count++;

					}
				}
				Assert.assertEquals(count, 0);
				test.log(LogStatus.PASS,
						"User is able to remove an patent from watchlist in Patent content search results page");// extent

			} catch (Throwable e) {

				test.log(LogStatus.FAIL,
						"User not able to remove an patent from watchlist in Patent content search results page");// extent
				// reports
				status = 2;// excel
				ErrorUtil.addVerificationFailure(e);// testng
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_user_unable_to_remove_patent_from_watchlist_in_Patent_content_searchResults_page")));// screenshot
			}
			// Delete the watch list
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
