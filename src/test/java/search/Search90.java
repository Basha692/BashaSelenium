package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
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

public class Search90 extends TestBase {

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
	public void testcaseB90() throws Exception {
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
						// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("bio");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()), 4);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).click();
				waitForElementTobeVisible(ob,
					By.cssSelector("a[class='wui-dropdown__link ng-binding ng-scope']"),60);
			List<WebElement> sortByValuesList = ob
					.findElements(By.cssSelector("a[class='wui-dropdown__link ng-binding ng-scope']"));
			List<String> expectedDropDownValues = Arrays.asList(new String[] {"Relevance", "Times Cited",
					"Publication Date (Newest)", "Publication Date (Oldest)"});
			List<String> actualDropDownValues = new ArrayList<String>();

			for (WebElement sortByValue : sortByValuesList) {

				actualDropDownValues.add(sortByValue.getText());
			}

			if (actualDropDownValues.equals(expectedDropDownValues)) {
				test.log(LogStatus.PASS, "All the sort by values are displayed properly in article search result page");
			} else {
				status = 2;
				test.log(LogStatus.FAIL,
						"All the sort by values are not displayed properly in article search result page");
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_sort_by_values_not_displayed_properly_in_article_results_page")));// screenshot
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
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

			}

}
