package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring26 extends TestBase {

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
	public void testMoreButtonForLessThanTenComments() throws Exception {

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
			maximizeWindow();
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));
			loginAs("USERNAME16", "PASSWORD16");
			selectAnArticle();
			/*
			 * String comment = "testFlag"; pf.getAuthoringInstance(ob).enterArticleComment(comment);
			 * pf.getAuthoringInstance(ob).clickAddCommentButton();
			 */
			waitForAllElementsToBePresent(ob,
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()), 80);
			List<WebElement> commentsList = ob.findElements(By
					.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
			try {

				Assert.assertTrue(commentsList.size() < 10);
				test.log(LogStatus.PASS, "More functionality working fine for comments less than 10 ");

			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "More functionality is not working fine for comments lss than 10 ");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "Cancel_Flag_validation_for_comments_failed")));// screenshot
			}

			try {
				ob.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				WebElement more = ob.findElement(By
						.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_SHOW_MORE_LINK_CSS.toString()));
				Assert.assertTrue(more.isDisplayed());
				test.log(LogStatus.FAIL, "More button is displayed for comments less than 10");
				status = 2;
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "Cancel_Flag_validation_for_comments_failed")));// screenshot

			} catch (Throwable t) {
				test.log(LogStatus.PASS, "More button is not displayed for comments less than 10");

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

			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void selectAnArticle() throws Exception {
		pf.getHFPageInstance(ob).searchForText("physics");
		pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()), 80);
		List<WebElement> itemList;
		itemList = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));

		while (true) {
			itemList = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));
			int commentsCount, itr = 1;
			String strCmntCt;
			boolean isFound = false;
			for (int i = (itr - 1); i < itemList.size(); i++) {
				List<WebElement> results = itemList.get(i).findElements(
						By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_COMMENTS_COUNT_CSS.toString()));
				logger.info("comments count in each section-->" + results.size());
				pf.getBrowserActionInstance(ob).scrollToElement(itemList.get(i));
				if (results.size() != 0) {
					strCmntCt = itemList
							.get(i)
							.findElement(
									By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_COMMENTS_COUNT_CSS.toString()))
							.getText().replaceAll(",", "").trim();
					commentsCount = Integer.parseInt(strCmntCt);

					if (commentsCount < 10) {
						jsClick(ob,
								itemList.get(i).findElement(
										By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_CSS.toString())));

						isFound = true;
						break;
					}

				}
			}

			if (isFound)
				break;
			itr++;
			((JavascriptExecutor) ob).executeScript("javascript:window.scrollBy(0,document.body.scrollHeight-150)");
			waitForAjax(ob);
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls, "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" , TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
