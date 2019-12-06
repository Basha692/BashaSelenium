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
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that no one can see the private watchlists of a user on user's profile page||Verify that user1 is not able to
 * see a watchlist on user2's profile page, once user2's public watchlist is reverted to private.
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist019 extends TestBase {

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
	public void testPrivateWatchlistNotVisibleFromOthersProfile() throws Exception {

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

			// String search_query = "biology";

			// 1) Login as user2
			openBrowser();
			maximizeWindow();
			clearCookies();
			// ob.get(host);
			ob.navigate().to(host);
			// loginAsSpecifiedUser(user2, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME2"), LOGIN.getProperty("LOGINPASSWORD2"));
			// Navigate to the watch list landing page
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("watchlist_link")), 60);
			ob.findElement(By.cssSelector(OR.getProperty("watchlist_link"))).click();

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("createWatchListButton1")), 60);
			// Creating 2 public watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp() + "_";
			String watchListDescription = "This is newly created watchlist.";
			createWatchList("public", newWatchlistName + 1, watchListDescription);
			BrowserWaits.waitTime(10);
			createWatchList("public", newWatchlistName + 2, watchListDescription);
			// Making the last watch list as private
			ob.findElement(By.xpath(OR.getProperty("edit_watch_list_button1"))).click();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox1")), 40);
			ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox1"))).click();
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("watchListUpdateButton")), 40);
			ob.findElement(By.xpath(OR.getProperty("watchListUpdateButton"))).click();
			BrowserWaits.waitTime(2);
			new PageFactory().getLoginTRInstance(ob).logOutApp();
			closeBrowser();
			// 2)Login as User1 and navigate to the user2 profile page
			openBrowser();
			maximizeWindow();
			clearCookies();
			// ob.get(host);
			ob.navigate().to(host);
			// loginAsSpecifiedUser(user1, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));
			fn2 = LOGIN.getProperty("FN2");
			ln2 = LOGIN.getProperty("LN2");
			// Searching for article
			// selectSearchTypeFromDropDown("People");
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchBox_textBox")),40);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(fn2 + " " + ln2);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchPepole")), 40);
			ob.findElement(By.xpath(OR.getProperty("searchPepole"))).click();
			waitForElementTobeClickable(ob, By.linkText(fn2 + " " + ln2), 60);
			// Navigating to the first user profile page
			ob.findElement(By.linkText(fn2 + " " + ln2)).click();
			waitForPageLoad(ob);
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page1")), 60);
			BrowserWaits.waitTime(2);
			// Navigating to the watch list tab
			ob.findElement(By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page1"))).click();
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")),60);
			List<WebElement> watchlists = ob
					.findElements(By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")));
			int count = 0;
			// Checking if private watch list is visible to others or not
			for (WebElement watchlist : watchlists) {
				if (watchlist.getText().equals(newWatchlistName + 2)) {
					count++;
					break;
				}
			}

			try {
				Assert.assertEquals(count, 0);
				test.log(LogStatus.PASS, "Others can not see the private watchlists of a user on user's profile page");
			} catch (Error e) {
				status = 2;
				test.log(LogStatus.FAIL, "Others able to see the private watchlists of a user on user's profile page");
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_others_able_to_see_the_private_watchlists_of_a_user_on_users_profile_page")));// screenshot
			}
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
