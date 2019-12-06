package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class Search41 extends TestBase {

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
	public void testcaseB41() throws Exception {

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

			String search_query = "biology";

			openBrowser();
			clearCookies();
			maximizeWindow();

			// Navigating to the NEON login page
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));

			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString()), 30);
			// Clicking on Articles content result set
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			waitForAjax(ob);

			// Check the filter is collapsed by default
			collapseFilter();
			// Check if the filter expanded
			expandFilter();
			// Check if filter is collapsible
			collapseFilter();
			test.log(LogStatus.PASS, "Document type filter is collapsible");

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

	private void expandFilter() {
		List<WebElement> filterPanelHeadingList;
		List<WebElement> filterPanelBodyList;
		WebElement documentTypePanelBody;
		WebElement documentTypePanelHeading;
		// Capturing panel heading after expanding document type filter
		waitForAjax(ob);
		filterPanelHeadingList = ob.findElements(By.cssSelector("div[class=panel-heading]"));
		documentTypePanelHeading = filterPanelHeadingList.get(0);
		WebElement upArrow = documentTypePanelHeading.findElement(By
				.cssSelector("i[class='fa pull-right fa-sort-desc']"));

		if (upArrow != null) {
			test.log(LogStatus.PASS, "Down arrow is visible for Document Type filter");
		}

		filterPanelBodyList = ob.findElements(By.cssSelector("div[class='panel-body'] ul"));
		documentTypePanelBody = filterPanelBodyList.get(0);

		if (!documentTypePanelBody.isDisplayed()) {
			test.log(LogStatus.PASS, "Document type filter values are displayed");
		}

		// Collapse the document type filter by clicking it again
		documentTypePanelHeading.click();

	}

	private void collapseFilter() {
		// Finding out the types filer in refine panel
		waitForAjax(ob);
		List<WebElement> filterPanelHeadingList = ob.findElements(By.cssSelector("div[class=panel-heading]"));
		WebElement documentTypePanelHeading = filterPanelHeadingList.get(0);
		WebElement downArrow = documentTypePanelHeading.findElement(By
				.cssSelector("i[class='fa pull-right fa-sort-asc']"));

		if (downArrow != null) {
			test.log(LogStatus.PASS, "UP arrow is visible for Document Type filter");
		}
		List<WebElement> filterPanelBodyList = ob.findElements(By.cssSelector("div[class='panel-collapse collapse']"));
		WebElement documentTypePanelBody = filterPanelBodyList.get(0);

		if (documentTypePanelBody.isDisplayed()) {
			test.log(LogStatus.FAIL, "Document type filter values are not displayed");
		}
		// Expanding the document type filter by clicking it
		documentTypePanelHeading.click();
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
