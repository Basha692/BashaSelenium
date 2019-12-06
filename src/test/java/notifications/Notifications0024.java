package notifications;

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
import util.OnePObjectMap;

public class Notifications0024 extends TestBase {

	static int status = 1;

	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");

	}

	@Test
	public void testcaseF22() throws Exception {
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
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);
			// Logging in with Default user
			pf.getLoginTRInstance(ob).enterTRCredentials(user1,
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");
			test.log(LogStatus.INFO, "User Logged in  successfully");
			BrowserWaits.waitTime(6);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH);
			WebElement element=ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()));
			String text=element.getText();
			logger.info("Feature Post Section : "+text);
			
			try{
				Assert.assertTrue(text.contains("Featured post"));
				test.log(LogStatus.INFO, "Featured post section in home page");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Featured post section not found in Newsfeed page.");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
				closeBrowser();
			}
			
			WebElement element1=element.findElement(By.cssSelector(OnePObjectMap.FUTUREPOST_WATCHLIST_STATUS_CSS.toString()));
			String watchlistStatus=element1.getText();
			logger.info("Author Name : "+watchlistStatus);
			if(watchlistStatus.contains("Watching")){
				element1.click();
				BrowserWaits.waitTime(2);
				waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.FUTUREPOST_WATCHLIST_WATCHING_CSS.toString()), 30);
				ob.findElement(By.cssSelector(OnePObjectMap.FUTUREPOST_WATCHLIST_WATCHING_CSS.toString())).click();
				BrowserWaits.waitTime(2);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH);
				BrowserWaits.waitTime(2);
			}
			element1.click();
			BrowserWaits.waitTime(4);
			ob.findElement(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_CSS.toString())).click();
			BrowserWaits.waitTime(2);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH);
			BrowserWaits.waitTime(2);
			String watchlistStatus1=element1.getText();
			logger.info("Watch list status : "+watchlistStatus1);
			try{
				Assert.assertTrue(watchlistStatus1.contains("Watching"));
				test.log(LogStatus.INFO, "User able to watch post from Feature post section on Home page.");
			}catch(Throwable t){
				
				test.log(LogStatus.FAIL, "User not able to watch post from Feature post section on Home page.");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
				closeBrowser();
			}
			
		closeBrowser();	

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"user is not able to seeing Home page.");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO,
					"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
							+ "user_is_not_able_seeing_Home_page")));// screenshot
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
