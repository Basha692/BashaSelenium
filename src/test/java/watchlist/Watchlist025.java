package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
 * Verify that same patent can be added to multiple watchlists
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist025 extends TestBase {

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
	public void testWatchPatentToMultipleWatchlist() throws Exception {

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
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("watchlist_link")), 30);
			ob.findElement(By.cssSelector(OR.getProperty("watchlist_link"))).click();
			BrowserWaits.waitTime(4);

			for (int i = 1; i <= 2; i++) {
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("createWatchListButton")), 30);
				ob.findElement(By.xpath(OR.getProperty("createWatchListButton"))).click();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListNameTextBox")), 30);
				ob.findElement(By.xpath(OR.getProperty("newWatchListNameTextBox")))
						.sendKeys(newWatchlistName + "_" + i);
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListDescriptionTextArea")), 30);
				ob.findElement(By.xpath(OR.getProperty("newWatchListDescriptionTextArea")))
						.sendKeys("This is my newly created watch list.");
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox")), 30);
				jsClick(ob, ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox"))));
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListCreateButton")), 30);
				ob.findElement(By.xpath(OR.getProperty("newWatchListCreateButton"))).click();
				waitForElementTobeVisible(ob, By.xpath("//a[contains(text(),'" + newWatchlistName + "_" + i + "')]"),
						30);
			}

			// Searching for patent
			String patentName = "biology";

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(patentName);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 60);

			// Watching an patent to a multiple watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			// Watch the patent to multiple watch list
			for (int i = 1; i <= 2; i++) {
				watchOrUnwatchItemToAParticularWatchlist(newWatchlistName + "_" + i, watchButton);
			}

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);

			// Selecting the document name
			String documentName = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			BrowserWaits.waitTime(4);
			int count;
			List<WebElement> watchedItems;
			for (int i = 1; i <= 2; i++) {
				count = 0;
				// Navigate to a particular watch list page
				navigateToParticularWatchlistPage(newWatchlistName + "_" + i);
				watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
				for (int j = 0; j < watchedItems.size(); j++) {
					if (watchedItems.get(j).getText().equals(documentName))
						count++;

				}
				if (compareNumbers(1, count)) {
					test.log(LogStatus.INFO, "User is able to add the patent into watchlist" + i);

				} else {
					test.log(LogStatus.FAIL, "User is not able to add the patent into watchlist" + i);// extent
					// reports
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_user_unable_to_add_patent_into_watchlist" + i)));// screenshot
				}
			}
			// Deleting watch list
			deleteParticularWatchlist(newWatchlistName + "_1");
			BrowserWaits.waitTime(2);
			deleteParticularWatchlist(newWatchlistName + "_2");

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
