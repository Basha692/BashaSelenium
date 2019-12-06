package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
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

public class Search112 extends TestBase {

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
	public void testcaseB112() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		List<String> profileOrderBeforeNavigation = new ArrayList<String>();
		List<String> profileOrderAfterNavigation = new ArrayList<String>();

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
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 50);
			// Searching for people
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("John");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(4);
			pf.getSearchResultsPageInstance(ob).clickOnPeopleTab();
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString())).click();
			BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.xpath("//a[contains(text(),'Registration Date')]"), 30);
			ob.findElement(By.xpath("//a[contains(text(),'Registration Date')]")).click();
			waitForAjax(ob);
			test.log(LogStatus.PASS, "Selected Registration Date as sort option");

			List<WebElement> webElementOrderBeforeNavigation = ob.findElements(By
					.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()));
			Iterator<WebElement> iterator = webElementOrderBeforeNavigation.iterator();
			while (iterator.hasNext()) {
				profileOrderBeforeNavigation.add(iterator.next().getText());
			}

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()), 30);

			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString())).click();

			waitForElementTobeVisible(ob, By.xpath("//h3[contains(text(),'Interests')]"), 15);
			test.log(LogStatus.PASS, "Record view page is opened");
			BrowserWaits.waitTime(4);
			ob.navigate().back();
			BrowserWaits.waitTime(4);
			// checking for Sort option
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()), 30);
			BrowserWaits.waitTime(4);
			String sortOptionSelected = ob
					.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_DROPDOWN_CSS.toString()))
					.getText().substring(8);
			System.out.println(sortOptionSelected);
			BrowserWaits.waitTime(3);
			List<WebElement> webElementOrderAfterNavigation = ob.findElements(By
					.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()));
			BrowserWaits.waitTime(5);
			Iterator<WebElement> itr = webElementOrderAfterNavigation.iterator();
			while (itr.hasNext()) {
				profileOrderAfterNavigation.add(itr.next().getText());
			}

			try {
				Assert.assertTrue(sortOptionSelected.equals("Registration Date")
						&& profileOrderBeforeNavigation.equals(profileOrderAfterNavigation));
				test.log(LogStatus.PASS,
						"Sort option selected is retained after navigating back to people search results page");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Sort option is not retained");// extent
				// reports
				// next 3 lines to print whole testng error in report
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports
				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
				// captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
				closeBrowser();
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
			// test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
			// captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
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
