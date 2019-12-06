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

public class Search14 extends TestBase {

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
	public void testcaseB13() throws Exception {

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

			String search_query = "cat and dog";

			openBrowser();
			clearCookies();
			maximizeWindow(); 
			ob.navigate().to(host);

			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()), 30);

			// Type into the search box and get search results
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(search_query);
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			//waitForAjax(ob);
			
			BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()), 30);

			// Put the urls of all the search results documents in a list and test whether documents contain searched
			// keyword or not
			List<WebElement> searchResults = ob.findElements(
					By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()));
			ArrayList<String> urls = new ArrayList<String>();
			for (int i = 0; i < searchResults.size(); i++) {

				urls.add(searchResults.get(i).getAttribute("href"));
			}
			boolean condition1, condition2, condition3, masterSearchCondition;
			String pageText;
			ArrayList<Integer> error_list = new ArrayList<Integer>();
			int count = 0;
			for (int i = 0; i < urls.size(); i++) {

				ob.navigate().to(urls.get(i));
				Thread.sleep(5000);
				// String link55=ob.findElement(By.xpath(OR.getProperty("details_link"))).getAttribute("href");
				// ob.get(link55);
				WebElement myE = ob.findElement(By.xpath(OnePObjectMap.SEARCH_RESULT_PAGE_DETAIL_LINK_XPATH.toString()));
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
				condition1 = pageText.contains("cat");
				condition2 = pageText.contains("dog");
				condition3 = pageText.contains("and");
				masterSearchCondition = condition1 && condition2 && condition3;
				System.out.println(masterSearchCondition);
				if (masterSearchCondition) {

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

				test.log(LogStatus.FAIL, "NOT is treated as a boolean");// extent reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Issues are in the following documents:\n" + message);// extent reports
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

}
