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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Verify that user is able to add an Post from Record View page to a particular watchlist
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist009 extends TestBase {

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
	public void testWatchPostFromPostRecordViewPage() throws Exception {

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
			// runOnSauceLabsFromLocal("Windows","Chrome");

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
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("Demonetization");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString()), 60);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString())));
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_search_results_post_title_css")), 80);
			// Navigating to record view page
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css"))).click();
			Thread.sleep(5000);

			// Watching the post to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("document_watchlist_button")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the post name
			waitForElementTobeClickable(ob, By.cssSelector("h2[class^='wui-content-title']"), 60);

			String documentName = ob.findElement(By.cssSelector("h2[class^='wui-content-title']")).getText();
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob
					.findElements(By.cssSelector(OR.getProperty("tr_search_results_post_title_css")));

			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {

				if (watchedItems.get(i).getText().equals(documentName))
					count++;

			}

			if (!compareNumbers(1, count)) {

				test.log(LogStatus.FAIL, "User not able to add an post into watchlist from Record view page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_unable_to_add_post_into_watchlist_Record_view_page")));// screenshot

			}

			// Step2: Unwatching the document from record view page
			// Searching for post
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("test");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString()), 60);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString())).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_search_results_post_title_css")), 60);
			// Navigating to record view page
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css"))).click();
			Thread.sleep(5000);
			// Unwatching the post to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("document_watchlist_button")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the post name
			documentName = ob.findElement(By.cssSelector("h2[class^='wui-content-title']")).getText();
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			try {

				WebElement defaultMessage = ob.findElement(By.cssSelector("h2[class^='wui-content-title']"));

				if (defaultMessage.isDisplayed()) {

					test.log(LogStatus.PASS, "User is able to remove an post from watchlist in Post record view page");// extent
				} else {
					test.log(LogStatus.FAIL, "User not able to remove an Post from watchlist in Post record view page");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_user_unable_to_remove_post_from_watchlist_in_Post_record_view_page")));// screenshot
				}
			} catch (NoSuchElementException e) {

				watchedItems = ob.findElements(By.cssSelector(OR.getProperty("tr_search_results_post_title_css")));
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
