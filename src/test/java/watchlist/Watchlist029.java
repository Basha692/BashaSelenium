package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that user is able to watch a post(user generated content) to a
 * particular watchlist from notification in home page||Verify that user is able
 * to unwatch a post from watchlist from notification in home page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist029 extends TestBase {

	static int status = 1;
	PageFactory pf = new PageFactory();

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
	public void testWatchUnwatchPostFromHomePage() throws Exception {

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
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);

			// 2)Login with user2 and and try to watch the post from
			// notification panel
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINSTEAMUSERNAME29"), LOGIN.getProperty("LOGINSTEAMUSERNAME29PWD"));
			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");
			// Navigating to the home page
			ob.findElement(By.xpath(OR.getProperty("home_link"))).click();
			// Add post to watchlist fron Newsfeed page
			ob.navigate().refresh();
			BrowserWaits.waitTime(8);
			String docTitle = pf.getNewsfeedPageInstance(ob).getPostTitle();
			pf.getNewsfeedPageInstance(ob).addFirstPostToWatclist(newWatchlistName);

			logger.info("post title in watchlist page-->" + docTitle);
			BrowserWaits.waitTime(10);
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {
				logger.info("text-->" + watchedItems.get(i).getText());
				if (watchedItems.get(i).getText().equals(docTitle)
						|| (StringUtils.contains(watchedItems.get(i).getText(), "Post Added by member"))) {
					count++;
				}
			}
			if (compareNumbers(1, count)) {
				test.log(LogStatus.PASS, "User is able to add a post into watchlist from home page");

			} else {
				test.log(LogStatus.FAIL, "User not able to add a post into watchlist from home page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_unable_to_add_post_into_watchlist_from_home_page")));// screenshot
				return;
			}
			BrowserWaits.waitTime(2);
			// Navigating to the home page
			ob.findElement(By.xpath(OR.getProperty("home_link"))).click();
			// Unwatching the post to a particular watch list
			ob.navigate().refresh();
			BrowserWaits.waitTime(8);
			pf.getNewsfeedPageInstance(ob).addFirstPostToWatclist(newWatchlistName);
			navigateToParticularWatchlistPage(newWatchlistName);
			try {
				WebElement defaultMessage = ob.findElement(By.xpath(OR.getProperty("default_message_watchlist")));

				if (defaultMessage.isDisplayed()) {

					test.log(LogStatus.PASS, "User is able to remove a post from watchlist in home page");// extent
				} else {
					test.log(LogStatus.FAIL, "User not able to remove a post from watchlist in home page");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_user_unable_to_remove_post_from_watchlist_in_home_page")));// screenshot
				}
			} catch (NoSuchElementException e) {

				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
				count = 0;
				for (int j = 0; j < watchedItems.size(); j++) {

					if (watchedItems.get(j).getText().equals(docTitle))
						count++;

				}
			//Assert.assertEquals(count, 0);
			}
			// Deleting the watch list
			deleteParticularWatchlist(newWatchlistName);
			pf.getLoginTRInstance(ob).logOutApp();
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
		 * if (status == 1) TestUtil.reportDataSetResult(suiteExls, "Test Cases"
		 * , TestUtil.getRowNum(suiteExls, this.getClass().getSimpleName()),
		 * "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(suiteExls, "Test Cases",
		 * TestUtil.getRowNum(suiteExls, this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(suiteExls, "Test Cases",
		 * TestUtil.getRowNum(suiteExls, this.getClass().getSimpleName()),
		 * "SKIP");
		 */
	}

}
