package search;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

public class Search126 extends TestBase {

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
	public void testcaseB126() throws Exception {
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
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("argentina");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);
			String title1 = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click();
			Thread.sleep(5000);
			waitForPageLoad(ob);
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector("h2[class^='wui-content-title']"), 40);

			String title2 = ob.findElement(By.cssSelector("h2[class^='wui-content-title']")).getText();

			if (!compareStrings(title1, title2)) {

				test.log(LogStatus.FAIL, "Clicking on article title is not redirected to correct record page");// extent
																												// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_search_drop_down_option_not_retained")));// screenshot

			} else {
				test.log(LogStatus.PASS, "Article title is matching");

			}
			checkIfFieldIsDisplyed(
					By.xpath(
							"//div[@class='wui-icon-metric wui-metric--ne-publication-sidebar' and contains(.,'Times Cited')]/descendant::span[@class='wui-icon-metric__label']"),
					"Times Cited");

			checkIfFieldIsDisplyed(
					By.xpath(
							"//div[@class='wui-metric wui-metric--ne-publication-sidebar' and contains(.,'Cited References')]/descendant::span[@class='wui-metric__label']"),
					"Cited References");

			checkIfFieldIsDisplyed(
					By.xpath(
							"//div[@class='wui-icon-metric wui-metric--ne-publication-sidebar' and contains(.,'Comments')]/descendant::span[@class='wui-icon-metric__label']"),
					"Comments");

			checkIfFieldIsDisplyed(By.xpath("//a[@class='wui-btn wui-btn--secondary wui-btn--view-in-wos']"),
					"View in Web of Science");
			checkIfFieldIsDisplyed(By.xpath("//h3[contains(.,'Abstract')]"), "Abstract Heading");

			checkIfFieldIsDisplyed(
					By.xpath(
							"//h3[contains(.,'Abstract')]/following-sibling::p[@class='ne-publication__body wui-large-text wui-large-text--serif ng-binding']"),
					"Abstract Content");
			checkIfFieldIsDisplyed(By.cssSelector("div[class='ne-publication__metadata'] span[class='ng-binding']"),
					"Publication details");

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

	private void checkIfFieldIsDisplyed(By locator,
			String fieldName) throws Exception {
		try {

			if (ob.findElement(locator).isDisplayed()) {
				test.log(LogStatus.PASS, fieldName + " is displayed in article record view page");
			} else
				throw new Exception();

		} catch (Exception e) {

			test.log(LogStatus.FAIL, fieldName + " is displayed in article record view page");
			test.log(LogStatus.INFO, "Error--->" + e);
			ErrorUtil.addVerificationFailure(e);
			status = 2;
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "Record_view_page_field_verification_failed")));// screenshot
		}

	}

}
