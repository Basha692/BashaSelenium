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
import util.OnePObjectMap;

public class Search67 extends TestBase {

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
	public void testcaseB67() throws Exception {

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

			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("bio");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_ALL_CSS.toString()), 30);
		

			String all_text = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_ALL_CSS.toString()))
					.getText();
			String all_temp = all_text.substring(3);
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^");
			System.out.println(all_text);
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^");
			int all_num = convertStringToInt(all_temp);
			System.out.println(all_num);

			String articles_text = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_PAGE_ARTICLES_CSS.toString()))
					.getText();
			String articles_temp = articles_text.substring(8);
			int articles_num = convertStringToInt(articles_temp);
			System.out.println(articles_num);

			String patents_text = ob
					.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_PATENTS_CSS.toString())).getText();
			String patents_temp = patents_text.substring(7);
			int patents_num = convertStringToInt(patents_temp);
			System.out.println(patents_num);

			String people_text = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_PEOPLE_CSS.toString()))
					.getText();
			String people_temp = people_text.substring(6);
			int people_num = convertStringToInt(people_temp);
			System.out.println(people_num);

			String posts_text = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString()))
					.getText();
			String posts_temp = posts_text.substring(5);
			int posts_num = convertStringToInt(posts_temp);
			System.out.println(posts_num);

			int total = articles_num + patents_num + people_num + posts_num;
			System.out.println(total);

			if (!compareNumbers(all_num, total)) {

				test.log(LogStatus.FAIL,
						"ALL search results count is not equal to the count of search results of other content types(ARTICLES+PATENTS+POSTS+PEOPLE)");// extent
																																						// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "_ALL_search_results_count_is_not_equal_to_the_count_of_search_results_of_other_content_types")));// screenshot

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
