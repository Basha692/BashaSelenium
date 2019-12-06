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

public class Notifications0005 extends TestBase {

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
	public void testcaseF5() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {
			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName());
		try {
			if (user1 != null && user2 != null && user3 != null) {
				openBrowser();
				maximizeWindow();
				clearCookies();
				ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				// Logging in with User1
				pf.getLoginTRInstance(ob).enterTRCredentials(user3, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
						"Home page is not loaded successfully");*/
				test.log(LogStatus.INFO, "User Logged in  successfully");
				logger.info("Home Page loaded success fully");
				watchListName = "Creating WatchList for notification testing" + new Random().nextInt(1000);
				logger.info("Watch List Name : "+watchListName);
				watchListDescription = "Creating Public WatchList for UI notification testing" + RandomStringUtils.randomNumeric(15);
				try {
					createWatchList("public", watchListName, watchListDescription);
					test.log(LogStatus.INFO, "User created watchlist \""+watchListName+"\" successfully");
				} catch (Exception e) {
					throw new Exception("Facing issue while create Watchlist");
				}
				pf.getLoginTRInstance(ob).logOutApp();
				test.log(LogStatus.INFO, "User logged out successfully");
				pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
						"Home page is not loaded successfully");*/
				pf.getHFPageInstance(ob).clickOnHomeLink();
				BrowserWaits.waitTime(4);
				test.log(LogStatus.INFO, "User Logged in successfully for verifying notification");
				
				waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_NOTIFICATION_PUBLIC_WATCHLIST_COMMENT_XPATH.toString()), 30,
						"user did not recevied notification");
				String text = ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_NOTIFICATION_PUBLIC_WATCHLIST_COMMENT_XPATH.toString())).getText();
				logger.info("Notification Text: " + text);
				try {
					Assert.assertTrue(/*text.contains("TODAY") &&  */  text.contains(fn3 + " " + ln3)
							&& text.contains("created a new public watchlist") && text.contains(watchListName)
							&& text.contains(watchListDescription));
					test.log(LogStatus.PASS, "User received notification with correct content");
					pf.getLoginTRInstance(ob).logOutApp();
//					closeBrowser();
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
					StringWriter errors = new StringWriter();
					t.printStackTrace(new PrintWriter(errors));
					test.log(LogStatus.INFO, errors.toString()); // reports
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));
				}
			} else {
				throw new Exception("User creation problem hence throwing exception");
			}
			ob.quit();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());// extent
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
	}
}
