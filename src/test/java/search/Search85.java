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

public class Search85 extends TestBase {

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
	public void testcaseB85() throws Exception {
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

			String search_query = "Color-corrected heat-reflecting composite films and glazing products containing the same";

			openBrowser();
			clearCookies();
			maximizeWindow();

			// Navigating to the NEON login page
			ob.navigate().to(host);
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + search_query + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);
			List<WebElement> searchTiles = ob.findElements(By.xpath("//*[@class='wui-card__content']"));
			BrowserWaits.waitTime(10);
			String tileText = searchTiles.get(0).getText();
			System.out.println(tileText);
			String text1 = "PATENT\nColor-corrected heat-reflecting composite films and glazing products containing the same\nASSIGNEE: SOUTHWALL TECHNOLOGIES INC\nUS5071206A PUBLISHED: 1991-12-10\nHOOD THOMAS G • MEYER STEPHEN F • BRAZIL MICHAEL";
			System.out.println(text1);

			try {
				test.log(LogStatus.INFO, tileText.replaceAll("[^a-zA-Z0-9_-]", ""));
				test.log(LogStatus.INFO, text1.replaceAll("[^a-zA-Z0-9_-]", ""));
				Assert.assertTrue(
						tileText.replaceAll("[^a-zA-Z0-9_-]", "").contains(text1.replaceAll("[^a-zA-Z0-9_-]", "")));
				test.log(LogStatus.PASS,
						"Following fields are getting displayed correctly for a patent: a)Title b)Patent number c)Assignees d)Inventors e)Publication date");// extent
																																								// reports
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Some or all of the following fields are not getting displayed correctly for a patent: a)Title b)Patent number c)Assignees d)Inventors e)Publication date");// extent
				// reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_patent_fields_not_getting_displayed_correctly")));// screenshot
			}

			try {

				Assert.assertTrue(tileText.contains("Views"));
				test.log(LogStatus.PASS, "View count gettiung displayed");// extent reports
			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "View count not getting displayed");// extent
				// reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_view_count_not_getting_displayed")));// screenshot
			}

			try {

				Assert.assertTrue(tileText.contains("Comments"));
				test.log(LogStatus.PASS, "Comment count gettiung displayed");// extent reports
			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "Comment count not getting displayed");// extent
				// reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_comment_count_not_getting_displayed")));// screenshot
			}

			try {

				Assert.assertTrue(tileText.contains("Times Cited"));
				test.log(LogStatus.PASS, "Times Cited count gettiung displayed");// extent reports
			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "Times Cited count not getting displayed");// extent
				// reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_times_cited_count_not_getting_displayed")));// screenshot
			}

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ABSTRACTS_CSS.toString()),
					30);
			String abst = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString())).getText();
			System.out.println("lenght" + abst.length());
			if (abst.length() <= 303)
				test.log(LogStatus.PASS, "Snippet of abstract is dispalying for patents search result page");// extent
			else
				test.log(LogStatus.FAIL,
						"Snippet of abstract is  not displaying properly for patents search result page");

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
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_patent_metadata_failed")));// screenshot
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
