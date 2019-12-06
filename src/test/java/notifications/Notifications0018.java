package notifications;

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
import util.OnePObjectMap;

public class Notifications0018 extends TestBase {

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
	public void testcaseF18() throws Exception {
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
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");*/
			test.log(LogStatus.INFO, "User Logged in  successfully");
			BrowserWaits.waitTime(6);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_XPATH);
			List<WebElement> recArticleSection=ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_XPATH.toString()));
			String text=recArticleSection.get(0).getText();
			
			logger.info("Recommended Articel Section Text"+text);
			try{
				Assert.assertTrue(text.contains("Recommended articles"));
				test.log(LogStatus.INFO, "Recommended Article section in home page");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Recommended Article section not found in Newsfeed page.");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
				closeBrowser();
			}
			WebElement elemt=recArticleSection.get(0);
			List<WebElement> element = elemt
					.findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_ARTICLE_CSS.toString()));
			String actual = null;
			String watchstatus = null;
			for (WebElement elem : element) {
				if (elem.getAttribute("ng-repeat").contains("article in vm.articles track by")) {
//					List<WebElement> elment = elem
//							.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_ARTICLE_NAME_XPATH.toString()));
					List<WebElement> elment = elem.findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLE_TITLE_CSS.toString()));
					actual = elment.get(0).getText();
					logger.info("Actual--> " + actual);
					BrowserWaits.waitTime(3);
					List<WebElement> element1 = ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_WATCHLIST_BUTTON_XPATH.toString()));
					element1.get(0).click();
					BrowserWaits.waitTime(3);
					List<WebElement> listOfWatchListButton = ob.findElements(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_CSS.toString()));
					listOfWatchListButton.get(0).click();
					BrowserWaits.waitTime(3);
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_XPATH);
					//ob.findElement(By.cssSelector("div[data-auto-close='outsideClick']")).click();
					//ob.findElement(By.xpath(OR.getProperty("home_link"))).click();
					//ob.findElement(By.xpath(OR.getProperty("watchlist_model_close_button1"))).click();
					BrowserWaits.waitTime(3);
					List<WebElement> element2 = elem.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_WATCHLIST_BUTTON_XPATH.toString()));
					watchstatus = element2.get(0).getText();
					logger.info("Watch Status--> " + watchstatus);
					break;
				}
			}

			BrowserWaits.waitTime(4);
			
			try {
				Assert.assertEquals(watchstatus, "Watching");
				test.log(LogStatus.PASS,
						"User is able to watch article from Recommended articles section on Home page.");
				logout();
				closeBrowser();
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"user is not able to watch article from Recommended articles section on Home page.");// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "user_is_not_able_to_watch_article_from_Recommended_articles_section_on_Home_page.")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"user is not able to watch article from Recommended articles section on Home page.");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO,
					"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
							+ "user_is_not_able_to_watch_article_from_Recommended_articles_section_on_Home_page")));// screenshot
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
