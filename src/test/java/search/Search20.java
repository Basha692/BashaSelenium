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
import util.ErrorUtil;
import util.ExtentManager;

public class Search20 extends TestBase {

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
	public void testcaseB20() throws Exception {

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
			try {
				maximizeWindow();
			} catch (Throwable t) {

				System.out.println("maximize() command not supported in Selendroid");
			}
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));
			login();
			waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("tr_search_box_css")), 20);
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_box_css"))).sendKeys("biology");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("search_button")), 20);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			List<WebElement> refineBlocks = ob.findElements(By.cssSelector("div[class='search-result-refine-menu']"));
			String filterType = null;
			for (WebElement refineBlock : refineBlocks) {
				filterType = refineBlock.findElement(By.xpath(OR.getProperty("tr_search_results_refine_filter_type")))
						.getText();
				jsClick(ob, refineBlock.findElement(By.xpath(OR
						.getProperty("tr_search_results_refine_expand_or_collapse_path"))));

				// Thread.sleep(4000);
				try {
					Assert.assertTrue(refineBlock.findElement(
							By.xpath(OR.getProperty("tr_search_results_refine_filter_block"))).isDisplayed());
					test.log(LogStatus.PASS, String.format("Refine block expands properly for %s type ", filterType));
				} catch (Throwable t) {
					test.log(LogStatus.FAIL,
							String.format("Refine block does not expand properly for %s type ", filterType));
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					test.log(
							LogStatus.INFO,
							"Snapshot below: "
									+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
											+ "expand_ is_not_ working_ for_refine_search_ results")));// screenshot
				}

				jsClick(ob, refineBlock.findElement(By.xpath(OR
						.getProperty("tr_search_results_refine_expand_or_collapse_path"))));

				waitForElementTobeInvisible(ob, By.xpath(OR.getProperty("tr_search_results_refine_filter_block")), 180);
				try {
					Assert.assertFalse(refineBlock.findElement(
							By.xpath(OR.getProperty("tr_search_results_refine_filter_block"))).isDisplayed());
					test.log(LogStatus.PASS, String.format("Refine block collapse properly for %s type ", filterType));
				} catch (Throwable t) {
					test.log(LogStatus.FAIL,
							String.format("Refine block does not collapse properly for %s type ", filterType));
					test.log(LogStatus.INFO, "Error--->" + t);
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					test.log(
							LogStatus.INFO,
							"Snapshot below: "
									+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
											+ "collapse_ is_not_ working_ for_refine_search_ results")));// screenshot
				}
			}

			logout();
			closeBrowser();

		}

		catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something went wrong");// extent reports
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
