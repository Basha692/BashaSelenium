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
 * Verify that following fields are getting displayed for each patents in the watchlist page: a)Times cited b)Comments
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist012 extends TestBase {

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
	@Parameters({"postName"})
	public void testDisplayedFieldsForPostsInWatchlist(String postName) throws Exception {

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
			createWatchList("private", newWatchlistName, "This is my test watchlist.");

			// Searching for post
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(postName);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.xpath("//a[@class='ng-binding']"), 60);
			Thread.sleep(3000);
			ob.findElement(By.xpath("//a[@class='wui-side-menu__link' and contains(text(),'Posts')]")).click();
			Thread.sleep(3000);

			// Getting watch button list for posts
			List<WebElement> watchButtonList = ob.findElements(By.xpath(OR.getProperty("search_watchlist_image")));

			// Watching 10 posts to a particular watch list
			for (WebElement watchButton : watchButtonList) {

				watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);
				((JavascriptExecutor) ob).executeScript("arguments[0].scrollIntoView(true);", watchButton);
				BrowserWaits.waitTime(2);
			}

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);
			BrowserWaits.waitTime(3);
			List<WebElement> labelsDisplayedList = ob
					.findElements(By.xpath("//div[@class='doc-info ng-scope']/span[2]"));

			boolean flag = Boolean.TRUE;
			String actualLabel = "";
			String expectedLabelLikes = "Likes";
			String expectedLabelComments = "Comments";

			try {
				for (WebElement label : labelsDisplayedList) {
					actualLabel = label.getText();
					if (flag) {

						flag = Boolean.FALSE;
						Assert.assertEquals(actualLabel, expectedLabelLikes);
					} else {

						flag = Boolean.TRUE;
						Assert.assertEquals(actualLabel, expectedLabelComments);
					}
				}
				test.log(LogStatus.PASS,
						"Following fields are getting displayed for each post in the watchlist page: a)Likes b)Comments");// extent
			} catch (Error e) {

				ErrorUtil.addVerificationFailure(e);
				test.log(LogStatus.FAIL,
						"Following fields are not getting displayed for each post in the watchlist page: a)Likes b)Comments");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_Following_fields_are_not_getting_displayed_for_each_post_in_the_watchlist_page:a)Likes b)Comments")));
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
