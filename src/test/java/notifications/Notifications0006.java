package notifications;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
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
import util.OnePObjectMap;

public class Notifications0006 extends TestBase {

	static int status = 1;
	String watchListName = null;
	String watchListDescription = null;
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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");
	}

	@Test
	public void testcaseF6() throws Exception {
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
			if (user1 != null && user2 != null && user2 != null) {
				/*openBrowser();
				maximizeWindow();
				clearCookies();

				ob.navigate().to(host);
				// Logging in with User2
				pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				watchListName = "Creating WatchList for notification testing" + new Random().nextInt(1000);
				watchListDescription = "Creating private WatchList for UI notification testing"
						+ RandomStringUtils.randomNumeric(15);
				logger.info("Notification Text: " + watchListName);
				// creating private watchlist
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " Creating private watchlist");
				createWatchList("private", watchListName, watchListDescription);*/

				openBrowser();
				maximizeWindow();
				clearCookies();
				ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				// Logging in with User1
				pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
						"Home page is not loaded successfully");*/
				test.log(LogStatus.INFO, "User Logged in  successfully");
				logger.info("Home Page loaded success fully");
				watchListName = "Creating WatchList for notification testing" + new Random().nextInt(1000);
				watchListDescription = "Creating Public WatchList for UI notification testing" + RandomStringUtils.randomNumeric(15);
				
				try {
					ob.findElement(By.xpath(OnePObjectMap.WTCHLISH_LINK_XPATH.toString())).click();
					createWatchList("private", watchListName, watchListDescription);
					test.log(LogStatus.INFO, "User created watchlist \""+watchListName+"\" successfully");
				} catch (Exception e) {
					throw new Exception("Facing issue while create Watchlist");
				}
			
				/*waitForElementTobeVisible(ob, By.xpath(OR.getProperty("watchListPrivateTabLink")), 30);
				// making it public
				ob.findElement(By.xpath(OR.getProperty("watchListPrivateTabLink"))).click();*/
				
				ob.findElement(By.xpath(OR.getProperty("watchListPrivateTabLink1"))).click();
				test.log(LogStatus.INFO, this.getClass().getSimpleName() + " making it public watchlist");
				BrowserWaits.waitTime(4);
				ob.findElement(By.xpath(OR.getProperty("edit_watch_list_button1"))).click();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox1")), 30);
				ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox1"))).click();
				ob.findElement(By.xpath(OR.getProperty("watchListUpdateButton"))).click();
				
				/*waitForElementTobeVisible(ob, By.xpath(OR.getProperty("newWatchListPublicCheckBox")), 30);
				ob.findElement(By.xpath(OR.getProperty("newWatchListPublicCheckBox"))).click();*/
				String name=fn2+" "+ln2;
				pf.getLoginTRInstance(ob).logOutApp();
				pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				BrowserWaits.waitTime(4);
				pf.getHFPageInstance(ob).clickOnHomeLink();
				BrowserWaits.waitTime(4);
				//String text = ob.findElement(By.xpath(OR.getProperty("newPublicWatchListNotification"))).getText();
				waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_NOTIFICATION_PUBLIC_WATCHLIST_COMMENT_XPATH.toString()), 30,
						"user did not recevied notification");
				String text = ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_NOTIFICATION_PUBLIC_WATCHLIST_COMMENT_XPATH.toString())).getText();
				logger.info("Notification Text: " + text);
				logger.info("Notification Text: " + text);
				//String userName=ob.findElement(By.xpath("//div[@class='wui-card wui-card--watchlist-event']//a[@class='ng-binding ng-scope']")).getText();
				try {
					Assert.assertTrue( /*text.contains("TODAY") && */name.contains(fn2 + " " + ln2)
							&& text.contains("made a watchlist public") && text.contains(watchListName)
							&& text.contains(watchListDescription));
					test.log(LogStatus.PASS, "User receiving notification with correct content");
					pf.getLoginTRInstance(ob).logOutApp();
					closeBrowser();
				} catch (Throwable t) {

					test.log(LogStatus.FAIL, "User receiving notification with incorrect content:\n" + t);// extent
					StringWriter errors = new StringWriter();
					t.printStackTrace(new PrintWriter(errors));
					test.log(LogStatus.INFO, errors.toString()); // reports
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));// screenshot
					closeBrowser();
				}
			} else {
				throw new Exception("User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "User receiving notification with incorrect content");// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(notificationxls, "Test Cases", TestUtil.getRowNum(notificationxls,
		 * this.getClass().getSimpleName()), "FAIL"); else TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
