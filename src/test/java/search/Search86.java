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

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Search86 extends TestBase {

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
	public void testcaseB86() throws Exception {

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

			String search_query = "Combinatorial Biology";

			openBrowser();
			clearCookies();
			maximizeWindow();

			// Navigating to the NEON login page
			ob.navigate().to(host);
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));

			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + search_query + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			//ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_PATENTS_CSS.toString())).click();
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 90);
			BrowserWaits.waitTime(5);
			List<WebElement> searchTiles = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			searchTiles.get(0).click();;
			BrowserWaits.waitTime(5);
			String text1 = ob.findElement(By.xpath("//*[@class='ne-publication__header']")).getText();
			BrowserWaits.waitTime(5);
			System.out.println(text1);
			String expectedText1 = "PATENT Combinatorial Biology JARRELL KEVIN A • SHAIR MATTHEW D ASSIGNEE: JARRELL, KEVIN A. • SHAIR, MATTHEW D. PUBLICATION DATE: 2007-11-22 PUBLICATION NUMBER: US20070269858A1";

			if (compareStrings(expectedText1.trim(), text1.trim())) {

				test.log(LogStatus.FAIL,
						"Some or all of the following fields are not getting displayed correctly for a patent: a)Title b)Patent number c)Assignees d)Inventors e)Publication date");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_patent_fields_not_getting_displayed_correctly")));// screenshot
			}

			if (!checkElementPresence("recordView_abstract")) {

				test.log(LogStatus.FAIL, "Abstract not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_abstract_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_IPC")) {

				test.log(LogStatus.FAIL, "IPC code not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_IPC_code_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_VITI")) {

				test.log(LogStatus.FAIL, "VIEW IN  DERWENT INNOVATION button not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_VIEW_IN_TI_button_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_VITI")) {

				test.log(LogStatus.FAIL, "VIEW IN DERWENT INNOVATION button not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_VIEW_IN_TI_button_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_views")) {

				test.log(LogStatus.FAIL, "View count not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_view_count_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_comments")) {

				test.log(LogStatus.FAIL, "Comment count not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_comment_count_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_tc")) {

				test.log(LogStatus.FAIL, "Times cited count not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_tc_count_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_cp")) {

				test.log(LogStatus.FAIL, "Cited patents count not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_cp_count_not_getting_displayed")));// screenshot

			}

			if (!checkElementPresence("recordView_ca")) {

				test.log(LogStatus.FAIL, "Cited articles count not getting displayed");// extent
				// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_ca_count_not_getting_displayed")));// screenshot

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
