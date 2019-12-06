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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that user is able to add a Patent from Record View page to a particular watchlist||Verify that user is able to
 * unwatch a Patent from Record View page
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist007 extends TestBase {

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
	public void testWatchPatentFromPatentsRecordViewPage(String patentName) throws Exception {

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
			loginAsSpecifiedUser(LOGIN.getProperty("Watchlist007_User"), LOGIN.getProperty("Watchlist007_PWD"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Searching for patents
			// selectSearchTypeFromDropDown("Patents");
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + patentName + "\"");
			// ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + patentName + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(8);
			ob.findElement(By.xpath(OR.getProperty("searchPatents"))).click();

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links1")), 30);

			// Navigating to record view page
			ob.findElement(By.xpath(OR.getProperty("searchResults_links1"))).click();
		//	waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_watchlist_image1")), 30);
			BrowserWaits.waitTime(3);
			// Watching the patent to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image1")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the patent name
			String documentName = ob.findElement(By.xpath(OR.getProperty("article_documentName_in_record_page")))
					.getText();
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links1")));

			int count = 0;
			for (int i = 0; i < watchedItems.size(); i++) {

				if (watchedItems.get(i).getText().equals(documentName))
					count++;

			}

			if (!compareNumbers(1, count)) {

				test.log(LogStatus.FAIL, "User not able to add an patent into watchlist from Record view page");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_user_unable_to_add_patent_into_watchlist_Record_view_page")));// screenshot

			}

			// Step2: Unwatching the document from record view page
			// Searching for patents
			// selectSearchTypeFromDropDown("Patents");
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + patentName + "\"");
			// ob.findElement(By.xpath(OR.getProperty("search_button"))).click();

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + patentName + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(10);
			ob.findElement(By.xpath(OR.getProperty("searchPatents"))).click();

			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchResults_links1")), 30);

			// Navigating to record view page
			ob.findElement(By.xpath(OR.getProperty("searchResults_links1"))).click();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_watchlist_image1")), 30);

			// Unwatching the patent to a particular watch list
			watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image1")));
			BrowserWaits.waitTime(2);
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Selecting the patent name
			documentName = ob.findElement(By.xpath(OR.getProperty("article_documentName_in_record_page"))).getText();
			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			try {

				WebElement defaultMessage = ob.findElement(By.xpath(OR.getProperty("default_message_watchlist1")));

				if (defaultMessage.isDisplayed()) {

					test.log(LogStatus.PASS,
							"User is able to remove an patent from watchlist in Patent record view page");// extent
				} else {
					test.log(LogStatus.FAIL,
							"User not able to remove an patent from watchlist in Patent record view page");// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_user_unable_to_remove_patent_from_watchlist_in_Patent_record_view_page")));// screenshot
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
