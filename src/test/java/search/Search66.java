package search;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class Search66 extends TestBase {

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
	public void testcaseB66() throws Exception {
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

			// Navigating to the NEON login page
			ob.navigate().to(host);
			// login using TR credentials
			login();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("drugs");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			BrowserWaits.waitTime(4);
			waitForAjax(ob);
			String all_text = ob.findElement(
					By.xpath("//a[contains(@class,'wui-side-menu__link') and contains(text(),'All')]")).getText();
			// System.out.println(all_text);
			int all_num = Integer.parseInt(all_text.substring(3, 4));
			System.out.println(all_num);
			boolean cond1 = all_num != 0;
			System.out.println(cond1);

			String articles_text = ob.findElement(
					By.xpath("//a[contains(@class,'wui-side-menu__link') and contains(text(),'Articles')]")).getText();
			int articles_num = Integer.parseInt(articles_text.substring(8, 9));
			System.out.println(articles_num);
			boolean cond2 = articles_num != 0;
			System.out.println(cond2);

			String patents_text = ob.findElement(
					By.xpath("//a[contains(@class,'wui-side-menu__link') and contains(text(),'Patents')]")).getText();
			int patents_num = Integer.parseInt(patents_text.substring(7, 8));
			System.out.println(patents_num);
			boolean cond3 = patents_num != 0;
			System.out.println(cond3);

			String people_text = ob.findElement(
					By.xpath("//a[contains(@class,'wui-side-menu__link') and contains(text(),'People')]")).getText();
			int people_num = Integer.parseInt(people_text.substring(6, 7));
			System.out.println(people_num);
			boolean cond4 = people_num != 0;
			System.out.println(cond4);

			String posts_text = ob.findElement(
					By.xpath("//a[contains(@class,'wui-side-menu__link') and contains(text(),'Posts')]")).getText();
			int posts_num = Integer.parseInt(posts_text.substring(5, 6));
			System.out.println(posts_num);
			boolean cond5 = posts_num != 0;
			System.out.println(cond5);

			boolean master_cond = cond1 && cond2 && cond3 && cond4 && cond5;
			System.out.println("Master condition=" + master_cond);

			try {

				Assert.assertTrue(master_cond);
				test.log(
						LogStatus.PASS,
						"Counts of search results of all the content types getting displayed irrespective of the content type chosen for searching");// extent
																																						// report
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Counts of search results of all the content types not getting displayed");// extent
																													// report

				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_counts_of_search_results_of_all_the_content_types_not_getting_displayed")));// screenshot

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
	}

}
