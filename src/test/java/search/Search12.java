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

public class Search12 extends TestBase {

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
	public void testcaseB8() throws Exception {

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

			String search_query = "biology";

			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(CONFIG.getProperty("testSiteName"));
			// ob.navigate().to(host);

			// login using TR credentials
			login();

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("contentType_span")), 30);

			List<WebElement> content_types = ob.findElements(By.xpath(OR.getProperty("contentType_span")));
			BrowserWaits.waitTime(5);
			String text1 = content_types.get(0).getText();
			String text2 = content_types.get(1).getText();
			String text3 = content_types.get(2).getText();
			String text4 = content_types.get(3).getText();
			String text5 = content_types.get(4).getText();

			String temp1 = getPurifiedString(text1);
			String temp2 = getPurifiedString(text2);
			String temp3 = getPurifiedString(text3);
			String temp4 = getPurifiedString(text4);
			String temp5 = getPurifiedString(text5);

			int all_count = Integer.parseInt(temp1);
			int article_count = Integer.parseInt(temp2);
			int patent_count = Integer.parseInt(temp3);
			int people_count = Integer.parseInt(temp4);
			int post_count = Integer.parseInt(temp5);

			if (!compareNumbers(all_count, article_count + patent_count + people_count + post_count)) {

				test.log(LogStatus.FAIL,
						"Count of ALL content type is not equal to the sum of counts of other content types");// extent
																												// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "count_of_ALL_content_type_not_equal_to_sum_of_counts_of_other_content_types")));// screenshot

			}

			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent reports
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

	public String getPurifiedString(String text) {

		String[] arr = text.split(",");
		String purifiedString = "";

		for (int i = 0; i < arr.length; i++) {

			purifiedString = purifiedString + arr[i];
		}

		return purifiedString;
	}

}
