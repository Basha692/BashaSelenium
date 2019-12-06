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

public class Notifications0009 extends TestBase {

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
	public void testcaseF9() throws Exception {
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
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			// Logging in with default user
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			ob.navigate().refresh();
			BrowserWaits.waitTime(9);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 60,
					"Home page is not loaded successfully");*/
			test.log(LogStatus.INFO, "User Logged in  successfully");
			logger.info("Home Page loaded success fully");

			waitForElementTobeVisible(ob, By.xpath(
					OnePObjectMap.NEWSFEED_TRENDINDING_MENU_XPATH.toString().replaceAll("FILTER_TYPE", "Topics")), 30);
			jsClick(ob, ob.findElement(By.xpath(
					OnePObjectMap.NEWSFEED_TRENDINDING_MENU_XPATH.toString().replaceAll("FILTER_TYPE", "Topics"))));
			List<WebElement> listOfPostsLinks = null;
			for (int i = 0; i < 3; i++) {
				logger.info(i);
				listOfPostsLinks = ob
						.findElements(By.xpath(OnePObjectMap.NEWSFEED_TRENDINDING_CATEGORIES_LINKS_XPATH.toString()));
				if (listOfPostsLinks.size() > 0) {
					break;
				} else {
					BrowserWaits.waitTime(5);
				}
			}
			waitForElementTobeVisible(ob,
					By.xpath(OnePObjectMap.NEWSFEED_TRENDINDING_CATEGORIES_LINKS_XPATH.toString()), 30,
					"Topics are not loaded successfully");
			if (listOfPostsLinks.size() > 0) {
				WebElement element = ob
						.findElement(By.xpath(OnePObjectMap.NEWSFEED_TRENDINDING_TOPICS_LINKS_XPATH.toString()));
				String specialCharacterRemovedoutput = element.getText().replaceAll("[^\\dA-Za-z ]", " ");
				logger.info(specialCharacterRemovedoutput);
				String expectedTitle = specialCharacterRemovedoutput.replaceAll("( )+", " ");
				logger.info("Expectged Title : "+expectedTitle);
				element.click();
				BrowserWaits.waitTime(5);
				//
				waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()),
						30, "Unable to find search text box");
				String searchText = ob
						.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()))
						.getAttribute("value");
				logger.info(searchText);
				try {
					Assert.assertTrue(searchText.contains(expectedTitle));
					test.log(LogStatus.PASS, "selected topic is presented in users type ahead");
					test.log(LogStatus.PASS, "Pass");
					closeBrowser();
				} catch (Throwable t) {

					test.log(LogStatus.FAIL, t.getMessage());
					test.log(LogStatus.FAIL, "Title selected is not same in search text box");// extent
					logger.error(this.getClass().getSimpleName() + "--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;// excel
					test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
							this.getClass().getSimpleName() + "_Title selected is not same in search text box")));// screenshot
					closeBrowser();
				}
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
			test.log(LogStatus.FAIL, "Title selected is not same in search text box");// extent
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Title selected is not same in search text box")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
