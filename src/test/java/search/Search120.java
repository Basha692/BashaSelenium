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
import util.ErrorUtil;
import util.ExtentManager;

public class Search120 extends TestBase {

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
	}

	@Test
	public void testcaseB110() throws Exception {
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

			String search_term = "Systems Biology of Cell Behavior";

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("\"" + search_term + "\"");
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();

			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 30);
			waitForAjax(ob);
			String title = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			// System.out.println(search_term);
			// System.out.println(title);

			if (!compareStrings(search_term, title)) {

				test.log(LogStatus.FAIL,
						"Title not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_title_not_getting_displayed_correctly")));// screenshot
			}

			String author = ob.findElement(By.xpath("//div[@ng-hide='vm.record.author.length == 0']")).getText();
			// System.out.println(author);
			// String expected_author="Valeyev, Najl V. • Bates, Declan G. • Umezawa, Yoshinori • et al.";

			if (author.equals("") || author.equals(null)) {

				test.log(LogStatus.FAIL,
						"Author field not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_author_field_not_getting_displayed_correctly")));// screenshot
			}

			String pub_name = ob.findElement(By.xpath("//span[@class='ng-binding' and contains(text(),'BIOLOGY')]"))
					.getText();
			// System.out.println(pub_name);
			// String expected_pub_name="SYSTEMS BIOLOGY IN DRUG DISCOVERY AND DEVELOPMENT: METHODS AND PROTOCOLS";

			if (pub_name.equals("") || pub_name.equals(null)) {

				test.log(LogStatus.FAIL,
						"Publication name not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_publication_name_not_getting_displayed_correctly")));// screenshot
			}

			String pub_date = ob.findElement(By.xpath("//span[@ng-hide='!vm.record.date']")).getText();
			// System.out.println(pub_date);
			// String expected_pub_date="Published: 2010";

			if (pub_date.equals("") || pub_date.equals(null)) {

				test.log(LogStatus.FAIL,
						"Publication date not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_publication_date_not_getting_displayed_correctly")));// screenshot
			}

			String times_cited = ob.findElement(By.xpath("//div[@tooltip='Times Cited']")).getText();
			System.out.println(times_cited);
			// String expected_times_cited="2 Times Cited";

			if (times_cited.equals("") || times_cited.equals(null)) {

				test.log(LogStatus.FAIL,
						"Times Cited information not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_times_cited_information_not_getting_displayed_correctly")));// screenshot
			}

			String comments = ob.findElement(By.xpath("//div[@class='wui-icon-metric ng-scope']")).getText();
			// System.out.println(comments);
			// String expected_comments="3 Comments";

			if (comments.equals("") || comments.equals(null)) {

				test.log(LogStatus.FAIL,
						"Comments information not getting displayed correctly for an article in ALL search results page");// extent
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_comments_information_not_getting_displayed_correctly")));// screenshot
			}

			waitForElementTobeVisible(ob, By.cssSelector("span[class='ng-scope']"), 30);
			List<WebElement> mylist = ob.findElements(By.cssSelector("span[class='ng-scope']"));
			System.out.println(mylist.get(3).getText());
			System.out.println(mylist.size());
			if (mylist.size() <= 3)
				test.log(LogStatus.PASS, " count of authors for articles are displaying properly");
			else if (mylist.get(3).getText().equals("et al.") && mylist.size() == 4)
				test.log(LogStatus.PASS, "count of authors for articles are displaying properly");
			else if (mylist.size() > 4)
				test.log(LogStatus.FAIL, "count of authors for articles are more than 3");

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
