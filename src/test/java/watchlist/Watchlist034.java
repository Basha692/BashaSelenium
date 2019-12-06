package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
import util.OnePObjectMap;

/**
 * Class for Verify that user is able to comment on his watchlist items
 * 
 * @author Jagadeesh
 */
public class Watchlist034 extends TestBase {

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
	@Parameters({ "articleName" })
	public void testCommentOnHisWatchlistItems(String articleName) throws Exception {

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
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Searching for article
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(articleName);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.xpath("//a[@class='ng-binding']"), 65);

			BrowserWaits.waitTime(4);
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("searchResults_links")), 90);
			String document_title = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();

			logger.info("doc title --->" + document_title);

			// Watching an article to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			for (int i = 0; i < watchedItems.size(); i++) {
				if (watchedItems.get(i).getText().equals(document_title)) {
					watchedItems.get(i).click();
					String beforecommentcountText = ob
							.findElements(By.cssSelector(
									OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()))
							.get(0).getText();
					int beforecommentcount = Integer.parseInt(beforecommentcountText);

					waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_comment_textbox_patent")), 90);
					ob.findElement(By.xpath(OR.getProperty("document_comment_textbox_patent")))
							.sendKeys("Automation Script Comment: Watchlist034 test");
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
							OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
					pf.getBrowserActionInstance(ob)
							.scrollToElement(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
					BrowserWaits.waitTime(6);
					pf.getBrowserActionInstance(ob)
							.jsClick(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);

					BrowserWaits.waitTime(6);
					waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()), 30);		
					String aftercommentcountText = ob.findElements(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()))

							.get(0).getText();
					int aftercommentscount = Integer.parseInt(aftercommentcountText);
					

					if ((aftercommentscount > beforecommentcount)) {
						test.log(LogStatus.PASS, "user is able to comment on his watchlist items");

					} else {
						logFailureDetails(test, "user is not able to comment on his watchlist items",
								"_user_unable_to_comment_on_his_watchlist_items");
					}

				} else {
					logFailureDetails(test, "not navigate particular watchlist", "watchlistfail");
				}
			}

			// Close the browser.
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
		 */}

}
