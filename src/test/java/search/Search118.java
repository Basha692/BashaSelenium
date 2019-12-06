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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search118 extends TestBase {

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
	public void testcaseB118() throws Exception {

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
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(search_query);
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			waitForElementTobeVisible(ob,By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_PATENTS_CSS.toString()),30);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()), 30);

			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).click();
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_SORTDROPDOWN_TIMECITED_XPATH.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_SORTDROPDOWN_TIMECITED_XPATH.toString())).click();

			waitForAjax(ob);

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()), 30);
			List<WebElement> filterPanelHeadingList;
			WebElement documentTypePanelHeading;
			// Capturing panel heading for filters
			filterPanelHeadingList = ob.findElements(By.cssSelector("div[class=panel-heading]"));
			documentTypePanelHeading = filterPanelHeadingList.get(0);

			// Expand the document type filter by clicking it again
			documentTypePanelHeading.click();
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("filter_checkbox")), 40);
			Thread.sleep(5000);
			List<WebElement> filterValues = ob.findElements(By.xpath(OR.getProperty("filter_checkbox")));
			filterValues.get(0).click();
			waitForAjax(ob);
			// Re-capturing filter values
			filterValues = ob.findElements(By.xpath(OR.getProperty("filter_checkbox")));
			filterValues.get(1).click();
			waitForAjax(ob);

			List<WebElement> searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			// System.out.println("search results-->"+searchResults.size());
			ArrayList<String> al1 = new ArrayList<String>();
			for (int i = 0; i < searchResults.size(); i++) {
				al1.add(searchResults.get(i).getText());
			}

			jsClick(ob, searchResults.get(8));
			waitForPageLoad(ob);
			Thread.sleep(10000);
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_patent_record_view_css")), 50);

			// ob.navigate().back();
			JavascriptExecutor js = (JavascriptExecutor) ob;
			js.executeScript("window.history.back();");
			waitForPageLoad(ob);
			waitForAjax(ob);
			Thread.sleep(6000);
			List<WebElement> searchResults2 = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			// System.out.println("search results2-->"+searchResults2.size());
			ArrayList<String> al2 = new ArrayList<String>();
			for (int i = 0; i < searchResults2.size(); i++) {
				al2.add(searchResults2.get(i).getText());
			}
			System.out.println("list1-->" + al1);
			System.out.println("list2-->" + al2);

			try {
				Assert.assertTrue(al1.equals(al2));
				test.log(LogStatus.PASS, "Correct sorted and filtered documents getting displayed");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Incorrect and filtered documents getting displayed");// extent reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " +
				// test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"_incorrect_documents_getting_displayed")));//screenshot

			}

			String option = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).getText().substring(8);
			System.out.println(option);
			if (!compareStrings("Times Cited", option)) {

				test.log(LogStatus.FAIL, "Incorrect sorting option getting displayed");// extent reports
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " +
				// test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"_incorrect_sorting_option_getting_displayed")));//screenshot

			}

			// waitForElementTobeVisible(ob,
			// By.xpath("//button[@class='btn dropdown-toggle ne-search-dropdown-btn ng-binding']"), 30);
			// String text = ob.findElement(
			// By.xpath("//button[@class='btn dropdown-toggle ne-search-dropdown-btn ng-binding']")).getText();
			// // System.out.println("Text="+text);
			//
			// if (!compareStrings("Patents", text)) {
			//
			// test.log(LogStatus.FAIL,
			// "Search drop down option not retained when user navigates back to Patent search results page from record
			// view page");// extent
			// // reports
			// status = 2;// excel
			// // test.log(LogStatus.INFO, "Snapshot below: " +
			// //
			// test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"_search_drop_down_option_not_retained")));//screenshot
			//
			// }

			try {
				WebElement element = ob
						.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_PATENTS_CSS.toString()));
				boolean text3 = element.findElement(By.xpath("parent::li")).getAttribute("class").contains("active");
				Assert.assertTrue(text3);
				test.log(LogStatus.PASS,
						"Left navigation panel content type is retained in patent search result page after page navigation");
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL,
						"Left navigation panel content type is not retained in patent search result page after page navigation");// extent
																																	// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " +
				// test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"_incorrect_documents_getting_displayed")));//screenshot

			}
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
			t.printStackTrace();
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
