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
import util.OnePObjectMap;

/**
 * Verify that user is able to watch a patent to a particular watchlist from notification in home page||Verify that user
 * is able to unwatch a patent from watchlist from notification in home page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist028 extends TestBase {

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
	public void testWatchUnwatchPatentFromHomePage() throws Exception {

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

			// login with user 2 and follow user1 to get the notifications
			loginAsSpecifiedUser(LOGIN.getProperty("Watchlist028_User"), LOGIN.getProperty("Watchlist028_PWD"));
			BrowserWaits.waitTime(5);
			pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(LOGIN.getProperty("PROFILE8"));
			pf.getSearchProfilePageInstance(ob).clickPeople();
			pf.getSearchProfilePageInstance(ob).selectProfile(LOGIN.getProperty("PROFILE8"));
			
			pf.getProfilePageInstance(ob).followOtherProfile();
			
			pf.getLoginTRInstance(ob).logOutApp();

			// 1)Login as user1 and comment on some patent
			loginAsSpecifiedUser(LOGIN.getProperty("USERNAME8"), LOGIN.getProperty("PASSWORD8"));
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 90);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("Steel wire patenting process");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchResults_links")), 90);
			String document_title = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			logger.info("patent doc title" + document_title);

			ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click(); 

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_comment_textbox_patent")), 90);
			ob.findElement(By.xpath(OR.getProperty("document_comment_textbox_patent")))
					.sendKeys("Automation Script Comment: Watchlist028 test");
			BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
			BrowserWaits.waitTime(4);
			logout();

			// 2)Login with user2 and and try to watch the patent from
			// notification panel
			loginAsSpecifiedUser(LOGIN.getProperty("Watchlist028_User"), LOGIN.getProperty("Watchlist028_PWD"));
			
			ob.navigate().refresh(); //adding refresh code to load page properly
			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Navigating to the home page
			pf.getNewsfeedPageInstance(ob).clickNewsfeedLink();
			//Add patent to watchlist
			//ob.navigate().refresh();
			BrowserWaits.waitTime(10);
			pf.getNewsfeedPageInstance(ob).addPatentToWatchlist(newWatchlistName, document_title);

			logger.info("document title in watchlist page-->" + document_title);
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {

				if (watchedItems.get(i).getText().equals(document_title))
					count++;

			}

			if (compareNumbers(1, count)) {
				test.log(LogStatus.PASS, "User is able to add an patent into watchlist from home page");

			} else {
				test.log(LogStatus.FAIL, "User not able to add an patent into watchlist from home page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_unable_to_add_patent_into_watchlist_from_home_page")));// screenshot
				return;
			}

			// Navigating to the home page
			pf.getNewsfeedPageInstance(ob).clickNewsfeedLink();
			
			// Unwatching the patent to a particular watch list
			pf.getNewsfeedPageInstance(ob).addPatentToWatchlist(newWatchlistName, document_title);

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			try {

				WebElement defaultMessage = ob.findElement(By.xpath(OR.getProperty("default_message_watchlist1")));

				if (defaultMessage.isDisplayed()) {

					test.log(LogStatus.PASS, "User is able to remove an patent from watchlist in home page");// extent
				} else {
					test.log(LogStatus.FAIL, "User not able to remove an patent from watchlist in home page");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_user_unable_to_remove_patent_from_watchlist_in_home_page")));// screenshot
				}
			} catch (NoSuchElementException e) {

				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links1")));
				count = 0;
				for (int i = 0; i < watchedItems.size(); i++) {

					if (watchedItems.get(i).getText().equals(document_title))
						count++;

				}
				Assert.assertEquals(count, 0);
			}

			// Deleting the watch list
			BrowserWaits.waitTime(5);
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
		 * if (status == 1) TestUtil.reportDataSetResult(suiteExls, "Test Cases" , TestUtil.getRowNum(suiteExls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(suiteExls,
		 * "Test Cases", TestUtil.getRowNum(suiteExls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(suiteExls, "Test Cases", TestUtil.getRowNum(suiteExls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
