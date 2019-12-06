package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class Search117 extends TestBase {

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
	public void testcaseB117() throws Exception {

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

			// Navigating to the NEON login page
			ob.navigate().to(host);
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));

			// login using TR credentials
			login();
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_box_css")), 120);

			String post = "post";
			String sortBy = "Relevance";
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(post);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();

			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_sortby_button_css"))).click();
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_results_sortby_menu_css")), 120);
			List<WebElement> postDropdownmenus = ob
					.findElement(By.cssSelector(OR.getProperty("tr_search_results_sortby_menu_css")))
					.findElements(By.tagName("li"));
			for (WebElement postDropdownmenu : postDropdownmenus) {
				if (postDropdownmenu.getText().trim().equalsIgnoreCase(sortBy)) {
					postDropdownmenu.click();
					waitForAjax(ob);
					waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_results_post_title_css")),
							120);
					break;
				}
			}

			List<String> postTitlesdata = new ArrayList<String>();
			List<String> postTitlesFromRVdata = new ArrayList<String>();
			List<WebElement> postTitles = ob
					.findElements(By.cssSelector(OR.getProperty("tr_search_results_post_title_css")));
			for (WebElement postTitle : postTitles) {
				postTitlesdata.add(postTitle.getText().trim());
			}

			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css"))).click();
			waitForAjax(ob);
			BrowserWaits.waitTime(6);

			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_patent_record_view_watch_share_css")),
					120);
			ob.navigate().back();
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_results_sortby_button_css")), 120);
			String sortByFromRV = ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_sortby_button_css")))
					.getText();

			System.out.println("sortby value From record view page-->" + sortByFromRV);
			if ((sortByFromRV.trim().equalsIgnoreCase(sortBy))) {
				throw new Exception(
						"sorting is not retained when user navigates back to POSTS search results page from record view page");
			}

			List<WebElement> postTitlesRV = ob
					.findElements(By.cssSelector(OR.getProperty("tr_search_results_post_title_css")));
			for (WebElement postTitleRV : postTitlesRV) {
				postTitlesFromRVdata.add(postTitleRV.getText().trim());
			}

			if (!(postTitlesFromRVdata.containsAll(postTitlesdata))) {
				throw new Exception(
						"Search dropdown content type is not retained when user navigates back to POSTS search results page from record view page");
			}

			closeBrowser();

		}

		catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_sortby_retain_post_search_results_failed")));// screenshot
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
