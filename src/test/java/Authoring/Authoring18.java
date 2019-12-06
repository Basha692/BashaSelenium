package Authoring;

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
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring18 extends TestBase {

	static int status = 1;
	PageFactory pf = new PageFactory();

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");

	}

	@Test
	public void testFlagForCommentUserAuthoredThemselves() throws Exception {

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
			maximizeWindow();
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			loginAs("USERNAME16", "PASSWORD16");
			String PROFILE_NAME = LOGIN.getProperty("PROFILE16");
			//pf.getHFPageInstance(ob).searchForText("Physics");
			pf.getSearchResultsPageInstance(ob).searchArticle("Physics");
			//BrowserWaits.waitTime(5);
			pf.getSearchResultsPageInstance(ob).chooseArticle();
			//BrowserWaits.waitTime(5);
			scrollingToElementofAPage();
			pf.getpostRVPageInstance(ob).createComment("test flag in own comment");
			// Commented by KR
			//BrowserWaits.waitTime(10);
			waitForAllElementsToBePresent(ob,
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()), 200);
			List<WebElement> commentsList = ob
					.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
			String commentText;
			for (WebElement we : commentsList) {
				commentText = we.getText();
				if (commentText.contains(PROFILE_NAME)) {

					try {
						if (we.findElement(
								By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()))
								.isDisplayed()) {
							test.log(LogStatus.FAIL, "Flag is displayed for comment user authored themselves");
							status = 2;
							test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
									this.getClass().getSimpleName() + "Flag_validation_for_comments_failed")));// screenshot

						}

					} catch (Exception e) {

						test.log(LogStatus.PASS, "Flag is not displayed for comments user authored themselves");
					}
					break;
				}

			}
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			status = 2;// excel
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng

			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		/*
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()),
		 * "FAIL"); else TestUtil.reportDataSetResult(authoringxls, "Test Cases"
		 * , TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()),
		 * "SKIP");
		 */
	}

}
