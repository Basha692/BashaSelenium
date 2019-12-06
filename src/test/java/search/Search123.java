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

public class Search123 extends TestBase {

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
	public void testcaseB123() throws Exception {

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

			ob.navigate().to(host);
			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 50);
			// Searching for people
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("john");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(4);
			pf.getSearchResultsPageInstance(ob).clickOnPeopleTab();
			BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.xpath("//span[contains(text(),'Institutions')]"), 50);
			ob.findElement(By.xpath("//span[contains(text(),'Institutions')]")).click();
			BrowserWaits.waitTime(2);

			boolean condition11 = false;

			waitForAllElementsToBePresent(ob, By.xpath("//span[@class='wui-checkbox__visible']"), 50);
			List<WebElement> list = ob.findElements(By.cssSelector("span[class='wui-checkbox__visible']"));
			for (WebElement we : list) {
				if (we.isDisplayed()) {
					jsClick(ob, we);
					break;
				}

			}

			BrowserWaits.waitTime(5);
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()), 50);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString())).click();
			Thread.sleep(3000);

			ob.navigate().back();
			Thread.sleep(2000);

			waitForAllElementsToBePresent(ob, By.xpath("//span[@class='wui-checkbox__visible']"), 50);

			boolean backgrundValue = ob.findElement(By.cssSelector("span[class='wui-checkbox__visible']"))
					.getCssValue("color").contains("rgba(42, 45, 53, 1)");
			if (backgrundValue) {
				test.log(LogStatus.INFO, "check box is selected");
				condition11 = true;
			} else {
				test.log(LogStatus.INFO, "check box is not selected by default");
				status = 2;
			}

			logger.info(condition11);

			try {

				Assert.assertTrue(condition11);
				test.log(LogStatus.PASS,
						"Filtering retained when user navigates back to PEOPLE search results page from profile page");// extent
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Filtering not retained when user navigates back to PEOPLE search results page from profile page");// extent
				// reports

				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_filtering_not_retained")));// screenshot

			}

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
