package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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

public class Search103 extends TestBase {

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
	public void testcaseB10() throws Exception {
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

			String search_query = "biology";

			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(host);
			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(search_query);
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			List<WebElement> filterPanelHeadingList;
			WebElement documentTypePanelHeading;
			// Capturing panel heading for filters
			filterPanelHeadingList = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_FILTER_PANELHEADING_CSS.toString()));
			documentTypePanelHeading = filterPanelHeadingList.get(0);

			// Expand the document type filter by clicking it again
			documentTypePanelHeading.click();
			BrowserWaits.waitTime(5);
			List<WebElement> filterValues = ob.findElements(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_FILTERPANEL_CHECKBOXES_XPATH.toString()));
			filterValues.get(0).click();
			BrowserWaits.waitTime(8);
			// Re-capturing filter values
			filterValues = ob.findElements(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_FILTERPANEL_CHECKBOXES_XPATH.toString()));
			filterValues.get(1).click();
			waitForAjax(ob);

			List<WebElement> searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			BrowserWaits.waitTime(4);
			System.out.println("Search Results-->" + searchResults.size());
			ArrayList<String> al1 = new ArrayList<String>();
			for (int i = 0; i < searchResults.size(); i++) {
				al1.add(searchResults.get(i).getText());
			}

			System.out.println("al1-->" + al1.size());
			jsClick(ob, searchResults.get(8));
			Thread.sleep(5000);

			JavascriptExecutor js = (JavascriptExecutor) ob;
			js.executeScript("window.history.back();");
			waitForPageLoad(ob);
			waitForAjax(ob);
			List<WebElement> searchResults2 = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			BrowserWaits.waitTime(4);
			System.out.println("Search Results-->" + searchResults.size());
			ArrayList<String> al2 = new ArrayList<String>();
			for (int i = 0; i < searchResults2.size(); i++) {

				al2.add(searchResults2.get(i).getText());

			}

			System.out.println("al2--->" + al2.size());

			try {
				Assert.assertTrue(al1.equals(al2));
				test.log(LogStatus.PASS,
						"Correct filtered search results getting displayed when user navigates back to patent search results page from record view page");
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Incorrect filtered search results getting displayed when user navigates back to patent search results page from record view page");// extent
				// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
				// this.getClass().getSimpleName() + "_incorrect_filtered_search_results_getting_displayed")));//
				// screenshot

			}

			filterValues = ob.findElements(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_FILTERPANEL_CHECKBOXES_XPATH.toString()));

			boolean filtering_condition = filterValues.get(0).getCssValue("color").contains("rgba(42, 45, 53, 1)")
					&& filterValues.get(1).getCssValue("color").contains("rgba(42, 45, 53, 1)");

			try {
				Assert.assertTrue(filtering_condition);
				test.log(LogStatus.PASS,
						"Filters are retained when user navigates back to Patent search results page from record view page");
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Filters are not retained when user navigates back to Patent search results page from record view page");// extent
				// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
				// .getSimpleName()
				// +
				// "_filters_not_retained_when_user_navigates_back_to_patent_search_results_page_from_record_view_page")));//
				// screenshot

			}

			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			// captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
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
