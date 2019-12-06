package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class Search89 extends TestBase {

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
	public void testcaseB89() throws Exception {
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
			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			// Searching for patents
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("synthetic biology");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			// BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()), 60);

			// Navigating to record view page

			ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click();
			// BrowserWaits.waitTime(7);

			String titleName = "";
			try {

				boolean titlePresent = ob.findElements(
						By.xpath("//div/h2[@class='wui-content-title wui-content-title--ne-publication ng-binding']"))
						.size() != 0;
				titleName = ob.findElement(
						By.xpath("//div/h2[@class='wui-content-title wui-content-title--ne-publication ng-binding']"))
						.getText();
				if (titlePresent) {
					test.log(LogStatus.PASS, "Title is present in patent record view page");
				} else {
					status = 2;
					test.log(LogStatus.FAIL, "Title is not present in patent record view page");
				}
			} catch (NoSuchElementException e) {
				status = 2;
				test.log(LogStatus.FAIL, "Title is not displayed present in patent record view page");
				ErrorUtil.addVerificationFailure(e);
			}

			try {
				List<WebElement> detailsLink = ob.findElements(By
						.cssSelector("a[class='wui-btn wui-btn--secondary wui-btn--view-in-ti']"));
				// Clicking on the details link

				if (detailsLink.size() != 0) {
					BrowserWaits.waitTime(3);
					String name = ob.findElement(
							By.cssSelector("a[class='wui-btn wui-btn--secondary wui-btn--view-in-ti']")).getText();
					if (name.contains("Derwent Innovation"))
						test.log(LogStatus.PASS, "View in Derwent Innovation link is present in the record view page");
				} else {
					test.log(LogStatus.FAIL,
							"View in Derwent Innovation is displayed multiple times in the record view page");
				}
			} catch (NoSuchElementException e) {

				status = 2;
				test.log(LogStatus.FAIL, "View in Thomson Innovation link is not displayed");// extent
				ErrorUtil.addVerificationFailure(e);
				return;
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
