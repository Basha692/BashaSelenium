package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that user is able to add a Patent from ALL content search results page to a particular watchlist||Verify that
 * user is able to unwatch a Patent from ALL content search results page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist002 extends TestBase {

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
	public void testWatchPatentFromAllContentSearchResult(String patentName) throws Exception {

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
			// Searching for patent
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + patentName + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.xpath("//a[@class='ng-binding']"), 60);
			Thread.sleep(3000);

			// Watching a patent to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the document name
			String documentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();

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
						"User not able to add an patent into watchlist from ALL content search results page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_user_unable_to_add_patent_into_watchlist_from_all_content_searchResults_page")));// screenshot

			}

			// Searching for patent
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"GLOVE BOX FOR VEHICLE\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.xpath("//a[@class='ng-binding']"), 60);
			Thread.sleep(3000);
			// Watching a patent to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Unwatching a patent to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the document name
			documentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			try {
				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
				count = 0;
				for (int i = 0; i < watchedItems.size(); i++) {

					if (watchedItems.get(i).getText().equals(documentName))
						count++;

				}
				Assert.assertEquals(count, 0);
				test.log(LogStatus.PASS,
						"User is able to remove an patent from watchlist in ALL content search results page");// extent

			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"User not able to remove an patent from watchlist in ALL content search results page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_user_unable_to_remove_patent_from_watchlist_in_all_content_searchResults_page")));// screenshot
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
