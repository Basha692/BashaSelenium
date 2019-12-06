package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class Search4 extends TestBase {

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

	/**
	 * Verify that NESTING WITH PARENTHESIS rule is working correctly
	 */
	@Test
	public void testcaseB4() throws Exception {

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

			String search_query = "+(cat dog bull) goat sheep animal";

			openBrowser();
			clearCookies();
			maximizeWindow();

			ob.navigate().to(host);
			// login using TR credentials
			login();

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()),30);
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString()), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(search_query);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS);
			waitForAjax(ob);
			BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);

			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()), 30);

			// Put the urls of all the search results documents in a list and test whether documents contain searched
			// keyword or not
			List<WebElement> searchResults = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 0; i < searchResults.size(); i++) {

				urls.add(searchResults.get(i).getAttribute("href"));
			}
			boolean condition1;
			String pageText;
			ArrayList<Integer> error_list = new ArrayList<Integer>();
			int count = 0;
			for (int i = 0; i < urls.size(); i++) {

				ob.navigate().to(urls.get(i));
				Thread.sleep(5000);
				WebElement myE = ob.findElement((By.cssSelector(OnePObjectMap.SEARCH_RECORD_VIEW_PAGE_DETAILS_LINK_CSS.toString())));
				pf.getBrowserActionInstance(ob).scrollElementIntoView(ob, myE);
				JavascriptExecutor executor = (JavascriptExecutor) ob;
				executor.executeScript("arguments[0].click();", myE);

				Set<String> myset = ob.getWindowHandles();
				Iterator<String> myIT = myset.iterator();
				ArrayList<String> mylist55 = new ArrayList<String>();

				for (int k = 0; k < myset.size(); k++) {

					mylist55.add(myIT.next());

				}

				ob.switchTo().window(mylist55.get(1));
				Thread.sleep(15000);

				pageText = ob.getPageSource().toLowerCase();
				condition1 = pageText.contains("cat") || pageText.contains("dog") || pageText.contains(" bull");
				System.out.println(condition1);
				if (condition1) {

					count++;
				} else {

					error_list.add(i + 1);
				}

				ob.close();
				ob.switchTo().window(mylist55.get(0));

			}
			String message = "";
			for (int i = 0; i < error_list.size(); i++) {

				message = message + error_list.get(i) + ",";

			}

			if (!compareNumbers(urls.size(), count)) {

				test.log(LogStatus.FAIL, "NESTING WITH PARENTHESIS rule not working correctly");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Issues are in the following documents:" + message);// extent reports
			}

			closeBrowser();
		} 
		catch (Throwable t) {
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

}
