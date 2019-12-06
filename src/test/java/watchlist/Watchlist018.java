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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

/**
 * Verify that anyone can see the public watchlists of a user on user's profile page||Verify that user1 is able to see a
 * watchlist on user2's profile page, once user2's private watchlist is made to public.
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist018 extends TestBase {

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
	public void testPublicWatchListVisibleFromOthersProfile() throws Exception {

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

			// 1)Login as User2 and create watch list
			openBrowser();
			maximizeWindow();
			clearCookies();
			// ob.get(host);
			ob.navigate().to(host);
			// loginAsSpecifiedUser(user2, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME2_18"), LOGIN.getProperty("LOGINPASSWORD2_18"));
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			String newWatchListDescription = "This is my newly created watch list";
			// Create private watch list
			createWatchList("private", newWatchlistName, newWatchListDescription);
			// Making the watch list from private to public
			// ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox"))).click();
			BrowserWaits.waitTime(6);
			ob.findElement(By.xpath(OR.getProperty("edit_watch_list_button1"))).click();
			BrowserWaits.waitTime(6);
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox1")), 60);
			ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox1"))).click();
			BrowserWaits.waitTime(2);
			ob.findElement(By.xpath(OR.getProperty("watchListUpdateButton"))).click();
			BrowserWaits.waitTime(6);
			logout();
//			closeBrowser();
//			// 2)Login as User1 and navigate to the user2 profile page
//			openBrowser();
//			maximizeWindow();
//			clearCookies();
//			// ob.get(host);
			ob.navigate().to(host);
			BrowserWaits.waitTime(6);
			// loginAsSpecifiedUser(user1, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));
			fn2 = LOGIN.getProperty("FN2");
			ln2 = LOGIN.getProperty("LN2");
			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_type_dropdown")), 30);
			// Searching for article
			// selectSearchTypeFromDropDown("People");
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchBox_textBox")), 60);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(fn2 + " " + ln2);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(5);
			ob.findElement(By.xpath(OR.getProperty("searchPepole"))).click();
			waitForElementTobeClickable(ob, By.linkText(fn2 + " " + ln2), 60);

			// Navigating to the first user profile page
			jsClick(ob,ob.findElement(By.linkText(fn2 + " " + ln2)));
			waitForPageLoad(ob);
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page1")), 60);
			waitForAjax(ob);
			// Navigating to the watch list tab
			BrowserWaits.waitTime(8);
			jsClick(ob, ob.findElement(By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page1"))));
			waitForAjax(ob);
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")), 60);
			List<WebElement> watchlists = ob
					.findElements(By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")));
			int count = 0;
			for (WebElement watchlist : watchlists) {
				if (watchlist.getText().equals(newWatchlistName)) {
					count++;
					break;
				}
			}

			try {
				Assert.assertEquals(count, 1);
				test.log(LogStatus.PASS, "Others can see the public watchlists of a user on user's profile page");
			} catch (Throwable e) {
				e.printStackTrace();
				status = 2;
				ErrorUtil.addVerificationFailure(e);
				test.log(LogStatus.FAIL, "Others unable to see the public watchlists of a user on user's profile page");
			}
			logout();
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
