package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class Search19 extends TestBase {

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
	public void testcaseB19() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("Categories", "category");
		filters.put("Document Type", "doctype");
		filters.put("Institutions", "institution");
		filters.put("Authors", "author");

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
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()), 30);
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString()), 20);
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys("biology");
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			// waitForAllElementsToBePresent(ob,
			// By.cssSelector(OR.getProperty("tr_search_results_all_refine_checkboxes_css")), 40);

			List<WebElement> ckBoxList;
			for (Map.Entry<String, String> entry : filters.entrySet()) {
				int checkBoxDisplayed = 0;
				scrollElementIntoView(ob,
						ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_expand_xpath")
								.replaceAll("FILTER_TYPE", entry.getKey()))));
				jsClick(ob, ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_expand_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey()))));
				BrowserWaits.waitTime(5);
				ckBoxList = ob.findElements(By.xpath(OR.getProperty("tr_search_results_refine_checkboxes_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey())));

				for (WebElement element : ckBoxList) {
					if (element.isDisplayed())
						checkBoxDisplayed++;
				}

				try {
					Assert.assertTrue(checkBoxDisplayed == 5);
					test.log(LogStatus.PASS, String.format("default filters displayed for %s is 5", entry.getKey()));
				} catch (Throwable t) {
					test.log(LogStatus.FAIL,
							String.format("default filters displayed for %s is not equal to 5", entry.getKey()));
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					// captureScreenshot(this.getClass().getSimpleName() +
					// "left_pane_ is_not_ working_ for_ search_ results")));// screenshot
				}
				scrollElementIntoView(ob,
						ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_more_link_xpath")
								.replaceAll("FILTER_TYPE", entry.getKey()))));
				jsClick(ob, ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_more_link_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey()))));
				BrowserWaits.waitTime(4);
				//waitForAllElementsToBePresent(ob,By.xpath(OR.getProperty("tr_search_results_refine_checkboxes_xpath")) ,60);
				ckBoxList = ob.findElements(By.xpath(OR.getProperty("tr_search_results_refine_checkboxes_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey())));
				checkBoxDisplayed = 0;
				for (WebElement element : ckBoxList) {
					if (element.isDisplayed())
						checkBoxDisplayed++;
				}

				try {
					Assert.assertTrue(checkBoxDisplayed == 10);
					test.log(LogStatus.PASS, String.format("More link should load 10 filters for %s", entry.getKey()));
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, String.format("More link not loding with 10 filters for", entry.getKey()));
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					// captureScreenshot(this.getClass().getSimpleName() +
					// "left_pane_ is_not_ working_ for_ search_ results")));// screenshot

				}
				scrollElementIntoView(ob,
						ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_less_link_xpath")
								.replaceAll("FILTER_TYPE", entry.getKey()))));
				jsClick(ob, ob.findElement(By.xpath(OR.getProperty("tr_search_results_refine_less_link_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey()))));
				BrowserWaits.waitTime(4);
				//waitForAllElementsToBePresent(ob,By.xpath(OR.getProperty("tr_search_results_refine_checkboxes_xpath")) ,60);
				ckBoxList = ob.findElements(By.xpath(OR.getProperty("tr_search_results_refine_checkboxes_xpath")
						.replaceAll("FILTER_TYPE", entry.getKey())));
				checkBoxDisplayed = 0;
				for (WebElement element : ckBoxList) {
					if (element.isDisplayed())
						checkBoxDisplayed++;
				}

				try {
					Assert.assertTrue(checkBoxDisplayed == 5);
					test.log(LogStatus.PASS, String.format("Less link should load 5 filters for %s", entry.getKey()));
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, String.format("Less link not loding with 5 filters for", entry.getKey()));
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					// captureScreenshot(this.getClass().getSimpleName() +
					// "left_pane_is_not_ working_ for_ search_ results")));// screenshot
				}
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
			// test.log(LogStatus.INFO, "Snapshot below: " +
			// test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()+"_something_unexpected_happened")));//screenshot
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
