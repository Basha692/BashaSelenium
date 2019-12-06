package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

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

public class Search58 extends TestBase {

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
	public void testcaseB58() throws Exception {

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

			// waitForElementTobeVisible(ob,
			// By.xpath("//button[@class='btn dropdown-toggle ne-search-dropdown-btn ng-binding']"), 30);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("j");
			BrowserWaits.waitTime(2);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("o");
			BrowserWaits.waitTime(2);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("h");
			BrowserWaits.waitTime(2);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("n");
			BrowserWaits.waitTime(3);

			String autocompleted_keyword = ob.findElement(By.xpath(OR.getProperty("autocompleteTile"))).getText();

			try {

				Assert.assertTrue(StringContains(autocompleted_keyword, "john"));
				test.log(LogStatus.PASS, "Autocompleted keyword coming up and it contains the searched keyword");// extent
																													// report
			}

			catch (Throwable t) {

				test.log(LogStatus.FAIL, "Autocompleted keyword doesn't contain the searched keyword");// extent
																										// report

				ErrorUtil.addVerificationFailure(t);// testng
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_autocompleted_keyword_does_not_contain_searched_keyword")));// screenshot

			}

			WebElement myE1 = ob.findElement(By.xpath(OR.getProperty("categoriesTile")));
			String text1 = myE1.getText();

			String[] arr1 = text1.split("\n");
			ArrayList<String> al1 = new ArrayList<String>();
			for (int i = 1; i < arr1.length; i++) {

				al1.add(arr1[i]);
			}

			// for(int i=0;i<al1.size();i++){
			//
			// System.out.println(al1.get(i));
			// }

			if (!compareNumbers(4, al1.size())) {

				test.log(LogStatus.FAIL, "More or less than 4 category suggestions are getting displayed");// extent
																											// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_more_or_less_than_four_category_suggestions_getting_displayed")));// screenshot
			}

			int count1 = 0;
			for (int i = 0; i < al1.size(); i++) {

				if (!al1.get(i).toLowerCase().contains(""))
					count1++;
			}

			if (!compareNumbers(0, count1)) {

				test.log(LogStatus.FAIL, "Category suggestion does not contain the typed keyword");// extent
																									// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_category_suggestion_does_not_contain_typed_keyword")));// screenshot
			}

			WebElement myE2 = ob.findElement(By.xpath(OR.getProperty("articlesTile")));
			String text2 = myE2.getText();

			String[] arr2 = text2.split("\n");

			ArrayList<String> al2 = new ArrayList<String>();
			for (int i = 0; i < arr2.length; i++) {

				al2.add(arr2[i]);
			}

			for (int i = 0; i < al2.size(); i++) {

				System.out.println(al2.get(i));
			}

			if (!compareNumbers(4, al2.size())) {

				test.log(LogStatus.FAIL, "More or less than 4 article suggestions are getting displayed");// extent
																											// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_more_or_less_than_four_article_suggestions_getting_displayed")));// screenshot
			}

			int count2 = 0;
			for (int i = 0; i < al2.size(); i++) {

				if (!al2.get(i).toLowerCase().contains("john"))
					count2++;
			}

			// if (!compareNumbers(0, count2)) {
			//
			// test.log(LogStatus.FAIL, "Article suggestion does not contain the typed keyword");// extent
			// // reports
			// status = 2;// excel
			// test.log(
			// LogStatus.INFO,
			// "Snapshot below: "
			// + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
			// + "_article_suggestion_does_not_contain_typed_keyword")));// screenshot
			// }

			WebElement myE3 = ob.findElement(By.xpath(OR.getProperty("patentsTile")));
			String text = myE3.getText();

			String[] arr3 = text.split("\n");
			ArrayList<String> al3 = new ArrayList<String>();
			for (int i = 0; i < arr3.length; i++) {

				al3.add(arr3[i]);
			}

			if (!compareNumbers(4, al3.size())) {

				test.log(LogStatus.FAIL, "More or less than 4 patent suggestions are getting displayed");// extent
																											// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_more_or_less_than_four_patent_suggestions_getting_displayed")));// screenshot
			}

			int count3 = 0;
			for (int i = 0; i < al3.size(); i++) {

				if (!al3.get(i).toLowerCase().contains("john"))
					count3++;
			}

			if (!compareNumbers(0, count3)) {

				test.log(LogStatus.FAIL, "Patent suggestion does not contain the typed keyword");// extent
																									// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_patent_suggestion_does_not_contain_typed_keyword")));// screenshot
			}

			WebElement myE4 = ob.findElement(By.xpath(OR.getProperty("peopleTile")));
			String text4 = myE4.getText();

			String[] arr4 = text.split("\n");
			ArrayList<String> al4 = new ArrayList<String>();
			for (int i = 0; i < arr4.length; i++) {

				al4.add(arr4[i]);
			}

			if (!compareNumbers(4, al4.size())) {

				test.log(LogStatus.FAIL, "More or less than 4 people suggestions are getting displayed");// extent
																											// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_more_or_less_than_four_people_suggestions_getting_displayed")));// screenshot
			}

			int count4 = 0;
			for (int i = 0; i < al4.size(); i++) {

				if (!al4.get(i).toLowerCase().contains("john"))
					count4++;
			}

			if (!compareNumbers(0, count4)) {

				test.log(LogStatus.FAIL, "People suggestion does not contain the typed keyword");// extent
																									// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_people_suggestion_does_not_contain_typed_keyword")));// screenshot
			}

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).clear();
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("p");
			Thread.sleep(1000);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("o");
			Thread.sleep(1000);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("s");
			Thread.sleep(1000);
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("t");
			Thread.sleep(1000);

			WebElement myE5 = ob.findElement(By.xpath(OR.getProperty("postsTile")));
			String text5 = myE5.getText();

			String[] arr5 = text5.split("\n");
			ArrayList<String> al5 = new ArrayList<String>();
			for (int i = 0; i < arr5.length; i++) {

				al5.add(arr5[i]);
			}

			for (int i = 0; i < al5.size(); i++) {

				System.out.println(al5.get(i));
			}

			if (!compareNumbers(4, al5.size())) {

				test.log(LogStatus.FAIL, "More or less than 4 post suggestions are getting displayed");// extent
																										// reports
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_more_or_less_than_four_post_suggestions_getting_displayed")));// screenshot
			}

			int count5 = 0;
			for (int i = 0; i < al5.size(); i++) {

				if (!al5.get(i).toLowerCase().contains("post"))
					count5++;
			}

			if (!compareNumbers(0, count5)) {

				test.log(LogStatus.FAIL, "Post suggestion does not contain the typed keyword");// extent
																								// reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_post_suggestion_does_not_contain_typed_keyword")));// screenshot
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
			// closeBrowser();
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
