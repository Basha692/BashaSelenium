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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search8 extends TestBase {

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
	public void testcaseB8() throws Exception {
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

			String search_query = "cat dog";

			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(CONFIG.getProperty("testSiteName"));
			// ob.navigate().to(host);

			// login using TR credentials
			login();
			//
			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(search_query);
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			waitForAjax(ob);
			//BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()), 30);

			// Find out that how many search results are visible right now
			List<WebElement> searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			System.out.println("No of search results visible="+searchResults.size());
			int count1 = searchResults.size();

			JavascriptExecutor jse = (JavascriptExecutor) ob;
			jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
			//BrowserWaits.waitTime(6);
			waitForAjax(ob);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
			searchResults.clear();
			searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			System.out.println("No of search results visible="+searchResults.size());
			int count2 = searchResults.size();

			jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
			//BrowserWaits.waitTime(6);
			waitForAjax(ob);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
			searchResults.clear();
			searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			System.out.println("No of search results visible="+searchResults.size());
			int count3 = searchResults.size();

			boolean condition1 = count2 > count1;
			boolean condition2 = count3 > count2;
			boolean condition3 = condition1 && condition2;

			try {
				Assert.assertTrue(condition3);
				test.log(LogStatus.PASS, "Count of visible documents increases as and when user scrolls down the page");
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Count of visible documents doesn't increase as and when user scrolls down the page");// extent
																												// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_visible_document_count_not_increasing_after_scrolling")));// screenshot

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
