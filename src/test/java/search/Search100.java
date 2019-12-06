package search;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search100 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Search suite");
	}

	@Test
	public void testcaseB100() throws Exception {

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
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()),60);
			// Type into the search box and get search results
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SEARCH_TEXTBOX_CSS.toString())).sendKeys("bio");
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()), 120);
			List<WebElement> searchResults1 = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));

			JavascriptExecutor jse = (JavascriptExecutor) ob;

			for (int i = 1; i <= 5; i++) {

				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
				waitForAjax(ob);

			}

			List<WebElement> searchResults2 = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));

			// System.out.println(searchResults1.size());
			// System.out.println(searchResults2.size());

			try {

				Assert.assertTrue(searchResults2.size() > searchResults1.size());
				test.log(LogStatus.PASS,
						"More search results get displayed when user scrolls down in Patent search results page");// extent
																													// reports
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"More search results do not get displayed when user scrolls down in Patent search results page");// extent
																															// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_more_search_results_do_not_get_displayed_when_user_scrolls_down_in_ALL_search_results_page")));// screenshot

			}

			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		// if(status==1)
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls,this.getClass().getSimpleName()), "PASS");
		// else if(status==2)
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls,this.getClass().getSimpleName()), "FAIL");
		// else
		// TestUtil.reportDataSetResult(searchxls, "Test Cases",
		// TestUtil.getRowNum(searchxls,this.getClass().getSimpleName()), "SKIP");

	}

}
