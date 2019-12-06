package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search37 extends TestBase {

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

			String search_query = "biology";

			openBrowser();
			clearCookies();
			maximizeWindow();

			// ob.navigate().to(CONFIG.getProperty("testSiteName"));
			ob.navigate().to(host);

			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString()), 30);

			// Clicking on Articles content result set
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString())).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()),
					30);

			// Clicking on the sort by drop down
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).click();
			waitForElementTobeVisible(ob, By.xpath("(//a[@class='wui-dropdown__link ng-binding ng-scope' ])[2]"), 30);

			ob.findElement(By.xpath("(//a[@class='wui-dropdown__link ng-binding ng-scope' ])[2]")).click();
			waitForAllElementsToBePresent(ob,By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()),90);
			JavascriptExecutor jse = (JavascriptExecutor) ob;
			jse.executeScript("scroll(0, 250);");
			
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()),60);

			// Finding out time cited values for the displayed articles in article result page
			List<WebElement> timeCitedCountList = ob.findElements(By.xpath("//results-metrics[div[@class='wui-metric ng-scope']]/div[3]/span[1]"));

			List<Integer> purifiedTimeCitedCountList = getPurifiedTimeCitedCountList(timeCitedCountList);
			List<Integer> sortedTimeCitedCountList = new LinkedList<Integer>(purifiedTimeCitedCountList);

			Collections.sort(sortedTimeCitedCountList);
			Collections.reverse(sortedTimeCitedCountList);

			// Comparing the the label of default sort by value
			if (!sortedTimeCitedCountList.equals(sortedTimeCitedCountList)) {

				test.log(LogStatus.FAIL, "Article results are not sorted by Times Cited");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "article_results_are_not_sorted_by _Relevance_by_default")));// screenshot

			} else {
				test.log(LogStatus.PASS, "Article results are sorted correctly by Times Cited");
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
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}
	private List<Integer> getPurifiedTimeCitedCountList(List<WebElement> timeCitedCountList) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		String purifiedString;
		String[] arr;
		for (WebElement e : timeCitedCountList) {
			arr = e.getText().split("");
			purifiedString = "";

			for (int i = 0; i < arr.length; i++) {

				purifiedString = purifiedString + arr[i];
			}

			list.add(Integer.parseInt(purifiedString));
		}
		return list;
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
