package search;

import java.io.PrintWriter;
import java.io.StringWriter;
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

public class Search18 extends TestBase {

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
	public void testcaseB18() throws Exception {
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
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();
			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));

			login();
			// waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("")), 20);
			// ob.findElement(By.cssSelector(OR.getProperty("tr_search_box_css"))).sendKeys("biology");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("biology");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 20);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			waitForAllElementsToBePresent(ob, By.cssSelector(OR.getProperty("tr_search_results_refine_expand_css")),
					40);
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_refine_expand_css"))).click();
			waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_FILTER_VALUES_CSS.toString()),
					60);		

			int checkboxesSelected = 0;
			List<WebElement> checkboxList;
			for (int i = 0; i < 2; i++) {
				checkboxList = ob
						.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_FILTER_VALUES_CSS.toString()));
				BrowserWaits.waitTime(1);
				if (checkboxList.get(i).isDisplayed() && !checkboxList.get(i).isSelected())
					jsClick(ob, checkboxList.get(i));
				waitForAjax(ob);

			}

			checkboxList = ob
					.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_FILTER_VALUES_CSS.toString()));
			//BrowserWaits.waitTime(2);
			
			for (WebElement element : checkboxList) {
				if (element.getCssValue("color").contains("rgba(42, 45, 53, 1)"))
					checkboxesSelected++;

			}
			Assert.assertTrue(checkboxesSelected != 0, "No filters is selected");
			WebElement resetButton = ob
					.findElement(By.cssSelector(OR.getProperty("tr_search_results_reset_button_css")));
			jsClick(ob, resetButton);
			waitForAjax(ob);
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_refine_expand_css"))).click();
			checkboxList = ob
					.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_FILTER_VALUES_CSS.toString()));

			checkboxesSelected = 0;
			for (WebElement element : checkboxList) {
				if (element.getCssValue("background").contains("rgb(69, 183, 231)"))
					checkboxesSelected++;

			}

			try {
				Assert.assertTrue(checkboxesSelected == 0);
				test.log(LogStatus.PASS, "Reset button for search results should work fine");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Reset button for search results not working as expected");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
				// this.getClass().getSimpleName() + "reset_ is_not_ working_ for_ search_ results")));// screenshot
			}
			logout();
			closeBrowser();

		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something went wrong");// extent reports
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
