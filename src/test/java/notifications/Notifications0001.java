package notifications;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

/**
 * The {@code Notifications0001} for testing test case of Verify that user is able to view top commenter's information
 * in home page. by someone
 *
 * @author Avinash Potti
 */
public class Notifications0001 extends TestBase {

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
	public void testcaseF1() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		int scrollCount = 0;
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
			if (user1 == null) {
				// login with default user
				pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
						CONFIG.getProperty("defaultPassword"));
			} else {
				// login with user1
				pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
			}
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			//waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 60,"Home page is not loaded successfully");
			test.log(LogStatus.INFO, "User Logged in  successfully");
			logger.info("Home Page loaded success fully");
			test.log(LogStatus.INFO, " Scrolling down to find most viewed documents");
			List<WebElement> elements = null;
			JavascriptExecutor jse = (JavascriptExecutor) ob;
			while (scrollCount < 30) {
				BrowserWaits.waitTime(3);
				elements = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NEWSFEED_RECOMMEND_ARTICLE_XPATH);
				if (elements.size() > 0)
					break;
				scrollCount++;
				jse.executeScript("scroll(0,10000)");
			}
			// Login with first user and check if notification is present
			try {
				Assert.assertTrue(elements.size() == 1);
				test.log(LogStatus.INFO, "User is able to view top commenters information in home page");
				test.log(LogStatus.PASS, "PASS");
				pf.getLoginTRInstance(ob).logOutApp();
				closeBrowser();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL,
						"User is not able to view top commenters information in home page afer 30 scrolls");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				ErrorUtil.addVerificationFailure(t);
				logger.error(this.getClass().getSimpleName() + "--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_user is not able to view top commenters information in home page afer 30 scrolls")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something happened");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			try {
				test.log(LogStatus.INFO, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
			} catch (Throwable t1) {
				t1.printStackTrace();
			}
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}

}
