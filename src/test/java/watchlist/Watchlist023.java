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
 * Verify that a user's public watchlist is not visible to another user once that particular watchlist is deleted.
 * 
 * @author Prasenjit Patra
 *
 */
public class Watchlist023 extends TestBase {

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
	public void testDeletedWatchlistNotVisibleFromOthersProfile() throws Exception {

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

			// 1) Login as user2
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);
			// loginAsSpecifiedUser(user2, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME2"), LOGIN.getProperty("LOGINPASSWORD2"));
			fn2 = LOGIN.getProperty("FN2");
			ln2 = LOGIN.getProperty("LN2");
			// Navigate to the watch list landing page
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("watchlist_link")), 60);
			ob.findElement(By.cssSelector(OR.getProperty("watchlist_link"))).click();
			waitForPageLoad(ob);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("createWatchListButton")), 60);
			// Creating two public watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			for (int i = 1; i <= 2; i++) {
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("createWatchListButton")), 60);
				ob.findElement(By.xpath(OR.getProperty("createWatchListButton"))).click();
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListNameTextBox")), 80);
				ob.findElement(By.xpath(OR.getProperty("newWatchListNameTextBox")))
						.sendKeys(newWatchlistName + "_" + i);
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListDescriptionTextArea")), 60);
				ob.findElement(By.xpath(OR.getProperty("newWatchListDescriptionTextArea")))
						.sendKeys("This is my newly created watch list.");
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox")), 60);
				jsClick(ob, ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox"))));
				waitForElementTobeClickable(ob, By.xpath(OR.getProperty("newWatchListCreateButton")), 60);
				ob.findElement(By.xpath(OR.getProperty("newWatchListCreateButton"))).click();
				waitForElementTobeClickable(ob, By.xpath("//a[contains(text(),'" + newWatchlistName + "_" + i + "')]"),
						60);
			}
			// Deleting the first watch list
			deleteParticularWatchlist(newWatchlistName + "_1");
			BrowserWaits.waitTime(4);
			closeBrowser();
			// 2)Login as User1 and navigate to the user2 profile page
			openBrowser();
			//runOnSauceLabsFromLocal("Windows", "Chrome");
			maximizeWindow();
			clearCookies();
			// ob.navigate().to(host);
			ob.navigate().to(host);
			// loginAsSpecifiedUser(user1, CONFIG.getProperty("defaultPassword"));
			loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));
			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_type_dropdown")), 30);
			// Searching for article
			// selectSearchTypeFromDropDown("People");
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("searchBox_textBox")), 60);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(fn2 + " " + ln2);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForElementTobeVisible(ob, By.linkText(fn2 + " " + ln2), 80);
			pf.getProfilePageInstance(ob).clickPeople();
			// Navigating to the first user profile page
			ob.findElement(By.linkText(fn2 + " " + ln2)).click();
			waitForPageLoad(ob);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page")), 80);
			BrowserWaits.waitTime(2);
			// Navigating to the watch list tab
			ob.findElement(By.xpath(OR.getProperty("tr_watchlists_tab_in_profile_page"))).click();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")), 80);
			List<WebElement> watchlists = ob
					.findElements(By.xpath(OR.getProperty("tr_watchlist_results_in_profile_page")));
			int count = 0;
			for (WebElement watchlist : watchlists) {
				if (watchlist.getText().equals(newWatchlistName + 2)) {
					count++;
					break;
				}
			}

			try {
				Assert.assertEquals(count, 0);
				test.log(LogStatus.PASS, "Others can not see the deleted watchlist of a user on user's profile page");
			} catch (Error e) {
				status = 2;
				test.log(LogStatus.FAIL, "Others able see the deleted watchlist of a user on user's profile page");
			}

			// Deleting the second watch list
			deleteParticularWatchlist(newWatchlistName + "_2");
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
