package notifications;

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

public class Notifications0025 extends TestBase {

	static int status = 1;
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
	public void testcaseF10() throws Exception {
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
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);
			// Logging in with Default user
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*
			 * waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 300,
			 * "Home page is not loaded successfully");
			 */
			test.log(LogStatus.INFO, "User Logged in  successfully");
			logger.info("Home Page loaded success fully");
			jsClick(ob, ob.findElement(By.xpath(
					OnePObjectMap.NEWSFEED_TRENDINDING_MENU_XPATH.toString().replaceAll("FILTER_TYPE", "Articles"))));
			BrowserWaits.waitTime(6);
			test.log(LogStatus.INFO, "User clicked articles tab successfully");
			List<WebElement> listOfArticles = ob
					.findElements(By.cssSelector(OnePObjectMap.TRENDING_SECTION_ARTICLE_FROM_NEWSFEED_CSS.toString()));
			BrowserWaits.waitTime(3);
			logger.info("Size : " + listOfArticles.size());
			try {
				Assert.assertTrue(listOfArticles.size() == 5);
				String articleTitleInTrendingSeciton = listOfArticles.get(0).getText();
				logger.info("Title : " + articleTitleInTrendingSeciton);
				listOfArticles.get(0).click();
				BrowserWaits.waitTime(3);
				String aritcleTitle = ob
						.findElement(By.xpath(OnePObjectMap.DOCUMENT_TITLE_IN_RECORDVIEW_PAGE_XPATH.toString()))
						.getText();
				logger.info("Title : " + aritcleTitle);
				Assert.assertEquals(articleTitleInTrendingSeciton, aritcleTitle);
				test.log(LogStatus.INFO,
						"Record view page is open while clicking article in trending section from Newsfeed page");
				test.log(LogStatus.PASS, "PASS");
				logout();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"Record view page is not open while clicking article in trending section from Newsfeed page");
				// reports
				throw new Exception("Maximum count on the trending list is not 5 per page");
			}
		} catch (Throwable t) {
			// reports
			test.log(LogStatus.FAIL, t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Title selected is not same in search text box")));// screenshot

		} finally {
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
