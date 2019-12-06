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

public class Notifications0011 extends TestBase {

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
	public void testcaseF11() throws Exception {
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
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");*/
			ob.navigate().refresh();
			BrowserWaits.waitTime(6);
			test.log(LogStatus.INFO, "User Logged in  successfully");
			List<WebElement> listOfNotifications = null;
			for (int i = 0; i < 3; i++) {
				listOfNotifications = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.NEWSFEED_ALL_NOTIFICATIONS_XPATH);
				if (listOfNotifications.size() > 0) {
					break;
				} else {
					BrowserWaits.waitTime(5);
				}
			}
			
			String text=listOfNotifications.get(0).getText();
			
			
			//String text = listOfNotifications.get(0).getText();
			logger.info("Featured Post Titile in Notifications - " + text);
			logger.info("Text Lenght : "+text.length());
			Assert.assertTrue(text.contains("Featured post"));
			test.log(LogStatus.INFO, "Featured post is at the top of the home page");
			WebElement element=listOfNotifications.get(0);
			//String postTitle=element.findElement(By.xpath("")).getText();
			String postTitle=element.findElement(By.xpath("//h3[@class='wui-content-title wui-content-title--medium wui-content-title--card-heading ng-binding']")).getText();
			logger.info("Post Title : "+postTitle);
			logger.info("Post Title length : "+postTitle.length());
			// li[@class='ne-trending__list-item ng-scope']/a

			List<WebElement> listOfPostsLinks = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.NEWSFEED_TRENDINDING_DOCUMENT_TITLES_XPATH);
			logger.info("Size" + listOfPostsLinks.size());
			String expectedTitle = listOfPostsLinks.get(0).getText();
			logger.info("Top Post Titile in Trending - " + expectedTitle);
			logger.info("Top Post Titile in Trending length - " + expectedTitle.length());

			try {
				if (expectedTitle.length() == postTitle.length()) {
					Assert.assertTrue(text.contains(expectedTitle));
				} else {
					Assert.assertTrue(text.contains(expectedTitle.substring(0, expectedTitle.length() - 5)));
				}
				test.log(LogStatus.PASS, "Featured post is same as the post in trending section");
				pf.getLoginTRInstance(ob).logOutApp();
				closeBrowser();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Featured Post title is not same as the post in the trending section");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				ErrorUtil.addVerificationFailure(t);
				logger.error(this.getClass().getSimpleName() + "--->" + t);
				status = 2;// excel
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Featured Post is not at the top");// extent
			// reports
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
