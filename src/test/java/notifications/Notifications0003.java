package notifications;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

/**
 * The {@code Notifications0003} for testing test case of Verify that user receives a notification when he is followed
 * by someone
 *
 * @author Avinash Potti
 */
public class Notifications0003 extends TestBase {

	static int status = 1;
	PageFactory pf = new PageFactory();

	/**
	 * class for Notification
	 * 
	 * @author UC196983
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");
	}

	@Test
	public void testcaseF3() throws Exception {
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
			if (user1 != null && user2 != null) {
				openBrowser();
				maximizeWindow();
				clearCookies();
				ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				// Login with user2 for verify that to receives a notification with correct data
				pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
						"Home page is not loaded successfully");*/
				logger.info("Home Page loaded success fully");
				test.log(LogStatus.INFO, "User Logged in  successfully");
				pf.getHFPageInstance(ob).clickOnHomeLink();
				BrowserWaits.waitTime(4);
				JavascriptExecutor jse = (JavascriptExecutor) ob;
				for (int i = 1; i <= 3; i++) {
					String text = ob
							.findElement(By.cssSelector(OnePObjectMap.NEWSFEED_NEW_FOLLOWER_NOTITIFICATION_CSS.toString()))
							.getText();
					if (text.length() > 0) {
						break;
					}
					jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
					BrowserWaits.waitTime(3);
				}
				String text = ob
						.findElement(By.cssSelector(OnePObjectMap.NEWSFEED_NEW_FOLLOWER_NOTITIFICATION_CSS.toString()))
						.getText();
				logger.info("Notification Text: " + text);
				try {
					Assert.assertTrue(/*text.contains("TODAY") && */text.contains(fn1 + " " + ln1)
							&& text.contains("Now following you"));
					test.log(LogStatus.PASS, "User receiving notification with correct content");
					test.log(LogStatus.PASS,"PASS");
					closeBrowser();
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "User receiving notification with incorrect content");// extent
					// reports
					test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
					ErrorUtil.addVerificationFailure(t);
					logger.error(this.getClass().getSimpleName() + "--->" + t);
					status = 2;// excel
					test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
				}
			} else {
				throw new Exception("User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}

}
