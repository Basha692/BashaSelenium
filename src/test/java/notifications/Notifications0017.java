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

public class Notifications0017 extends TestBase {

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
	public void testcaseF17() throws Exception {

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
			//waitForElementTobeVisible(ob, By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(4);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_ARTICLE_CSS);
			List<WebElement> element = ob.findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLES_SECTION_ARTICLE_CSS.toString()));
			BrowserWaits.waitTime(2);
			String actual = null;
			for (WebElement elem : element) {
				if (elem.getAttribute("ng-repeat").contains("article in vm.articles track by")) {
					List<WebElement> elment = elem.findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_ARTICLE_TITLE_CSS.toString()));
					actual = elment.get(0).getText();
					logger.info("Actual--> " + actual);
					jsClick(ob, elment.get(0));
					//elment.get(0).click();
					BrowserWaits.waitTime(2);
					break;
				}
			}

			BrowserWaits.waitTime(4);
			
			String result = ob.findElement(By.cssSelector(OnePObjectMap.ARTICLE_TITLE_IN_RECORD_VIEW_PAGE_CSS.toString())).getText();
			logger.info("Result--> " + result);
			try {
				Assert.assertEquals(actual, result);
				test.log(LogStatus.PASS,
						"User is able to navigate record view page by clicking article title from Recommended articles section on Home page");
				logout();
				closeBrowser();
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"User is not able to navigate record view page by clicking article title from Recommended articles section on Home page");// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "User_is_not_able_to_navigate_record_view_page_by_clicking_article_title_from_Recommended_articles_section_on_Home_page")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"User is not able to navigate record view page by clicking article title from Recommended articles section on Home page");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
					.getSimpleName()
					+ "User_is_not_able_to_navigate_record_view_page_by_clicking_article_title_from_Recommended_articles_section_on_Home_page")));// screenshot
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
