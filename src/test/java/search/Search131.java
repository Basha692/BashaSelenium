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

public class Search131 extends TestBase {

	static int status = 1;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest113() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory(
				"Search suite");
	}

	@Test
	public void testcaseB() throws Exception {

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
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("post");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(3);
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);
			Thread.sleep(2000);
			waitForAllElementsToBePresent(ob, By.cssSelector("div[class='panel-heading']"), 30);
			String fname = ob.findElement(By.cssSelector(" h4[class='panel-title'] div[class='ng-scope'] span"))
					.getText();
			if (compareStrings("Institutions", fname))
				test.log(LogStatus.PASS, "Institution filter is present in Posts search result page ");// extent
			// reports

			else {
				test.log(LogStatus.FAIL, "Institution filter is not present in post search result page");// extent
				// reports
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "Filters_not_present")));// screenshot

			}

			List<WebElement> filter_list = ob.findElements(By.cssSelector("div[class='panel-heading']"));
			filter_list.get(0).click();
			BrowserWaits.waitTime(4);
			waitForElementTobeVisible(ob,
					By.xpath("//button[@class='search-result-refine-menu__load-button ng-scope']"), 30);
			jsClick(ob, ob.findElement(By.xpath("//button[@class='search-result-refine-menu__load-button ng-scope']")));
			BrowserWaits.waitTime(2);

			// System.out.println(ob.findElement(By.xpath("//button[@class='load-more-button ng-scope']")).getText());

			String temp1 = ob.findElement(
					By.xpath("//button[@class='search-result-refine-menu__load-button ng-scope']")).getText();
			if (!compareStrings("Less", temp1)) {

				test.log(LogStatus.FAIL, "Less link not working");// extent
				// reports
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_more_link_not_working")));// screenshot

			}
		
			jsClick(ob, ob.findElement(By.xpath("//button[@class='search-result-refine-menu__load-button ng-scope']")));
			BrowserWaits.waitTime(2);
			String temp2 = ob.findElement(
					By.xpath("//button[@class='search-result-refine-menu__load-button ng-scope']")).getText();

			if (!compareStrings("More", temp2)) {

				test.log(LogStatus.FAIL, "More link not working");// extent
				// reports
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_less_link_not_working")));// screenshot

			}
			
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
			BrowserWaits.waitTime(2);
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
				test.log(LogStatus.PASS, "Reset button for post search results should work fine");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Reset button for post search results not working as expected");
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
