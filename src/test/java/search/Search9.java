package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;

public class Search9 extends TestBase {

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
	public void testcaseB9() throws Exception {
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

			String search_query = "cat dog";

			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(CONFIG.getProperty("testSiteName"));
			// ob.navigate().to(host);

			// login using TR credentials
			login();
			//
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(search_query);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			//
			waitForElementTobeVisible(ob, By.id(OR.getProperty("sortDropdown_button")), 30);

			ob.findElement(By.id(OR.getProperty("sortDropdown_button"))).click();
			Thread.sleep(1000);
			ob.findElement(By.linkText(OR.getProperty("sortDropdown_timesCitedOption_link"))).click();
			Thread.sleep(10000);
			//
			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);

			List<WebElement> searchResults = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			// System.out.println("search results-->"+searchResults.size());
			ArrayList<String> al1 = new ArrayList<String>();
			for (int i = 0; i < searchResults.size(); i++) {
				al1.add(searchResults.get(i).getText());
			}

			// System.out.println("list-->"+al1);

			// searchResults.get(8).click();
			jsClick(ob, searchResults.get(7));
			Thread.sleep(5000);

			// ob.navigate().back();
			JavascriptExecutor js = (JavascriptExecutor) ob;
			js.executeScript("window.history.back();");
			Thread.sleep(8000);
			List<WebElement> searchResults2 = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			// System.out.println("search results2-->"+searchResults2.size());
			ArrayList<String> al2 = new ArrayList<String>();
			for (int i = 0; i < searchResults2.size(); i++) {
				al2.add(searchResults2.get(i).getText());
			}

			// System.out.println("list2-->"+al2);

			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			for (int i = 0; i < al1.size(); i++) {

				System.out.println(al1.get(i));
			}

			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			for (int i = 0; i < al2.size(); i++) {

				System.out.println(al2.get(i));
			}

			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

			try {
				Assert.assertTrue(al1.equals(al2));
				test.log(LogStatus.PASS, "Correct sorted documents getting displayed");
			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "Incorrect documents getting displayed");// extent reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_incorrect_documents_getting_displayed")));// screenshot

			}

			String option = ob.findElement(By.id(OR.getProperty("sortDropdown_button"))).getText().substring(9);
			if (!compareStrings("Times Cited", option)) {

				test.log(LogStatus.FAIL, "Incorrect sorting option getting displayed");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_incorrect_sorting_option_getting_displayed")));// screenshot

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
		//
	}

}
