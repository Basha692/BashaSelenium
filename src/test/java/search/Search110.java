package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search110 extends TestBase {

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
	public void testcase_B99() throws Exception {

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
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			//BrowserWaits.waitTime(3);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()), 30);

			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).click();
			BrowserWaits.waitTime(2);
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_SORTDROPDOWN_TIMECITED_XPATH.toString()),30);
			ob.findElement(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_SORTDROPDOWN_TIMECITED_XPATH.toString())).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector("div[data-uib-tooltip='Times Cited']"), 30);
			
			List<WebElement> times_cited_labels = ob.findElements(By.cssSelector("div[data-uib-tooltip='Times Cited']"));
			
			ArrayList<Integer> counts = new ArrayList<Integer>();
			String temp;
			for (int i = 0; i < times_cited_labels.size(); i++) {

				temp = times_cited_labels.get(i).getText().substring(0, times_cited_labels.get(0).getText().indexOf(" ")).trim();
				counts.add(Integer.parseInt(temp));
			 System.out.println(counts.get(i));
			}

			ArrayList<Integer> mylist = new ArrayList<Integer>();
			mylist.addAll(counts);
			// System.out.println(mylist);

			Collections.sort(mylist);
			Collections.reverse(mylist);

			try {

				Assert.assertTrue(counts.equals(mylist));
				test.log(LogStatus.PASS,"Records are sorted based on time cited option");
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Patents are not sorted correctly as per TIMES CITED field in PATENTS content type");// extent
																												// reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_incorrect_TIMES_CITED_sorting_in_PATENTS_content_type")));// screenshot

			}

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
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
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
