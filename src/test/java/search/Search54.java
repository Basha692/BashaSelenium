package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class Search54 extends TestBase {

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
	public void testcaseB54() throws Exception {

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
			clearCookies();
			maximizeWindow();

			// Navigating to the NEON login page
			ob.navigate().to(host);

			// login using TR credentials autocompleteTile
			login();

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("b");
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("i");
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("o");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("autocompleteTile")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("l");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("autocompleteTile")), 30);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString()), 30);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS);
			// ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString())).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_RECORDS_TITLE_CSS.toString()), 90);
			JavascriptExecutor jse = (JavascriptExecutor) ob;

			for (int i = 1; i <= 5; i++) {

				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
			waitForAjax(ob);

			}

			List<WebElement> tileTags = ob.findElements(By
					.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_RECORDS_TITLE_CSS.toString()));
			int count = 0;
			for (int i = 0; i < tileTags.size(); i++) {
				if (tileTags.get(i).getText().equals("ARTICLE"))
					count++;
			}

			if (!compareNumbers(tileTags.size(), count)) {

				test.log(
						LogStatus.FAIL,
						"Items other than articles also getting displayed in the summary page when user searches using ARTICLES content type in search drop down");// extent
																																									// report
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_items_other_than_articles_also_getting_displayed_in_the_summary_page_when_user_searches_using_ARTICLES_content_type_in_search_drop_down")));// screenshot

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
