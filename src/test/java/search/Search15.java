package search;

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

import util.ErrorUtil;
import util.ExtentManager;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class Search15 extends TestBase {

	static int status = 1;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory(
				"Search suite");
	}

	@Test
	public void testcaseB15() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
					+ " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {

			openBrowser();
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));
			login();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_search_box_css")), 20);
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_box_css"))).sendKeys("biology");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 20);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("tr_search_results_item_xpath")), 60);
			List<WebElement> resultList = ob.findElements(By.xpath(OR.getProperty("tr_search_results_item_xpath")));
			System.out.println("result size" + resultList.size());
			List<WebElement> timeCiteList;
			List<WebElement> viewsList;
			List<WebElement> commentsList;
			int timeCiteCount = 0, viewsCount = 0, commentsCount = 0;
			waitForAjax(ob);
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("tr_timecited_search_results_xpath")), 40);
			timeCiteList = ob.findElements(By.xpath(OR.getProperty("tr_timecited_search_results_xpath")));
			System.out.println("time cite list" + timeCiteList.size());
			if (timeCiteList.size() != 0) {
				for (WebElement timeCite : timeCiteList) {
					if (!timeCite.isDisplayed())
						timeCiteCount++;
				}
			} else {
				viewsCount = -1;
			}

			try {
				Assert.assertTrue(resultList.size() >= timeCiteList.size() && timeCiteCount == 0);
				test.log(LogStatus.PASS, "Time cite field are displayed for  articles which are time cited");
			} catch (Throwable t) {
				if (timeCiteCount == -1)
					test.log(LogStatus.FAIL, "Time cite field is not displayed for any articles");
				test.log(LogStatus.FAIL,
						String.format("Time cite field is not displayed for %d articles", timeCiteCount));
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "TimeCite for search results validation failed")));// screenshot

			}
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("tr_views_search_results_xpath")), 40);
			viewsList = ob.findElements(By.xpath(OR.getProperty("tr_views_search_results_xpath")));
			if (viewsList.size() != 0) {
				for (WebElement views : viewsList) {
					if (!views.isDisplayed())
						viewsCount++;
				}
			} else {
				viewsCount = -1;
			}

			try {
				Assert.assertTrue(resultList.size() >= viewsList.size() && viewsCount == 0);
				test.log(LogStatus.PASS, "views field is displayed for articles");
			} catch (Throwable t) {
				if (viewsCount == -1)
					test.log(LogStatus.FAIL, "views field is not displayed for articles");
				else
					test.log(LogStatus.FAIL, String.format("views field is not displayed %d documents", viewsCount));
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "Viewsfor search results validation failed")));// screenshot

			}
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("tr_comments_search_results_xpath")),60);
			commentsList = ob.findElements(By.xpath(OR.getProperty("tr_comments_search_results_xpath")));
			if (commentsList.size() != 0) {
				for (WebElement comments : commentsList) {
					if (!comments.isDisplayed())
						commentsCount++;
				}
			} else {
				commentsCount = -1;
			}

			try {
				Assert.assertTrue(resultList.size() >= commentsList.size() && commentsCount == 0);
				test.log(LogStatus.PASS, "Comments field is displayed for documents ");
			} catch (Throwable t) {
				if (commentsCount == -1)
					test.log(LogStatus.FAIL, "Comments field is not displayed for articles");
				else
					test.log(LogStatus.FAIL,
							String.format("Comments field is not displayed for %d articles", commentsCount));
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "Comments for search results validation failed")));// screenshot

			}
			// logout();
			closeBrowser();
		}

		catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something went wrong");// extent reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		// if (status == 1)
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls, this.getClass().getSimpleName()), "PASS");
		// else if (status == 2)
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls, this.getClass().getSimpleName()), "FAIL");
		// else
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls, this.getClass().getSimpleName()), "SKIP");

	}
}
