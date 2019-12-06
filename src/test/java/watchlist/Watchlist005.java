package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
 * Verify that user is able to add an Article from Record View page to a particular watchlist||Verify that user is able
 * to unwatch an Article from Record View page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist005 extends TestBase {

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
	public void testWatchArticleFromArticleRecordViewPage() throws Exception {

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
		//	runOnSauceLabsFromLocal("Windows", "Chrome");
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			logger.info("New WatchList Name : " + newWatchlistName);
			createWatchList("private", newWatchlistName, "This is my test watchlist.");
			BrowserWaits.waitTime(4);
		//	pf.getHFPageInstance(ob).searchForText("hello");
			pf.getHFPageInstance(ob).searchForText("Green Chemistry");
			BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 60);
			// Navigating to record view page
			ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click();

			BrowserWaits.waitTime(6);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_watchlist_button")), 30);
			// Watching the article to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("document_watchlist_button")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the article name
			String documentName = ob.findElement(By.xpath(OR.getProperty("article_documentName_in_record_page")))
					.getText();
			logger.info("Article Document Name : " + documentName);
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));

			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {

				if (watchedItems.get(i).getText().equals(documentName))
					count++;

			}

			if (!compareNumbers(1, count)) {

				test.log(LogStatus.FAIL,
						"Verify that user is able to add an Article from Record View page to a particular watchlist||Verify that user is able to unwatch an Article from Record View page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_user_unable_to_add_article_into_watchlist_Record_view_page")));// screenshot
			}
			// Step2: Unwatching the document from record view page
			// Searching for article
			// selectSearchTypeFromDropDown("Articles");
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("hello");
			// ob.findElement(By.xpath(OR.getProperty("search_button"))).click();

			pf.getHFPageInstance(ob).searchForText(documentName);

			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 60);

			// Navigating to record view page
			// ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click();
			BrowserWaits.waitTime(6);
			ob.findElement(By.linkText(documentName)).click();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_watchlist_button")), 60);

			// Unwatching the article to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("document_watchlist_button")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the article name
			documentName = ob.findElement(By.xpath(OR.getProperty("article_documentName_in_record_page"))).getText();
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			try {

				WebElement defaultMessage = ob.findElement(By.xpath(OR.getProperty("default_message_watchlist1")));

				if (defaultMessage.isDisplayed()) {

					test.log(LogStatus.PASS,
							"User is able to remove an article from watchlist in Article record view page");// extent
				} else {
					test.log(LogStatus.FAIL,
							"User not able to remove an article from watchlist in Article record view page");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_user_unable_to_remove_article_from_watchlist_in_Article_record_view_page")));// screenshot
				}
			} catch (NoSuchElementException e) {

				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links1")));
				count = 0;
				for (int i = 0; i < watchedItems.size(); i++) {

					if (watchedItems.get(i).getText().equals(documentName))
						count++;

				}
				Assert.assertEquals(count, 0);
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
