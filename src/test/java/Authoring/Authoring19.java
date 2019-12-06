package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class Authoring19 extends TestBase {

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
	public void testFlagSetByOtherUsers() throws Exception {

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
			// ob.get(CONFIG.getProperty("testSiteName"));
			ob.navigate().to(host);
			loginAs("USERNAME16", "PASSWORD16");
			String PROFILE_NAME = LOGIN.getProperty("PROFILE16");
			pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick("Micro Biology");
			//pf.getHFPageInstance(ob).searchForText("Micro Biology");
			waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString()),
					180);
			String articleTitle = ob
					.findElement(By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString())).getText();
			ob.findElement((By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString()))).click();
			String comment = "testFlag";
			pf.getPostCommentPageInstance(ob).enterArticleComment(comment);
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			pf.getLoginTRInstance(ob).logOutApp();
			BrowserWaits.waitTime(3);
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));
			loginAs("USERNAME3", "PASSWORD3");
			//BrowserWaits.waitTime(10);
			//pf.getHFPageInstance(ob).searchForText(articleTitle);
			pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick("Micro Biology");
			waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString()),
					180);
			jsClick(ob, ob.findElement(By.linkText(articleTitle)));
			waitForAjax(ob);
			waitForAllElementsToBePresent(ob,
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()), 40);
			List<WebElement> commentsList = ob
					.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
			System.out.println(commentsList.size());
			String commentText;
			WebElement flagWe;
			for (int i = 0; i < commentsList.size(); i++) {
				commentText = commentsList.get(i).getText();
				if (commentText.contains(PROFILE_NAME)) {
					BrowserWaits.waitTime(5);
					flagWe = commentsList.get(i).findElement(
							By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()));
					if (flagWe.getAttribute("class").contains("fa-flag-o")) {
						scrollElementIntoView(ob, flagWe);
						//BrowserWaits.waitTime(5);
						jsClick(ob, flagWe);
						//BrowserWaits.waitTime(5);
						break;
					}
				}

			}
			scrollingToElementofAPage();
			scrollingToElementofAPage();
			WebElement ele = ob.findElement(By.xpath("//div[@class='modal-body wui-modal__body clearfix ng-scope']/div[1]/descendant::label/span[1]"));
			scrollElementIntoView(ob, ele);
			pf.getpostRVPageInstance(ob).selectReasonInFlagModal();
			pf.getpostRVPageInstance(ob).clickFlagButtonInFlagModal();

			pf.getLoginTRInstance(ob).logOutApp();
			BrowserWaits.waitTime(3);
			ob.navigate().to(host);
			// ob.get(CONFIG.getProperty("testSiteName"));
			loginAs("USERNAME5", "PASSWORD5");
			//BrowserWaits.waitTime(15);
			searchAnArticle(articleTitle);
			//BrowserWaits.waitTime(10);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString()),
					180);
			String commentCount = ob
					.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString()))
					.getText();

			if (commentCount.equals("0")) {
				searchAnArticle(articleTitle);
			}
			waitForAllElementsToBePresent(ob,
					By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()), 80);
			commentsList = ob.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
			boolean flag = false;
			for (WebElement we : commentsList) {
				commentText = we.getText();
				if (commentText.contains(PROFILE_NAME) && commentText.contains(comment)) {

					try {

						if (we.findElement(
								By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()))
								.getAttribute("class").contains("fa-flag-o")) {
							test.log(LogStatus.PASS, "Flag set by other user is not visible to the current user");
							flag = true;
						} else {
							throw new Exception("Flag Validation Failed");
						}

					} catch (Throwable t) {
						test.log(LogStatus.FAIL, "Flag set by other user is visible to the current user");
						test.log(LogStatus.INFO, "Error--->" + t);
						ErrorUtil.addVerificationFailure(t);
						status = 2;
						test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
								this.getClass().getSimpleName() + "Flag_validation_for_comments_failed")));// screenshot

					}
					if (!flag) {
						test.log(LogStatus.FAIL, "Commment is available");
						test.log(LogStatus.INFO, "Error--->");
						status = 2;
						test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
								this.getClass().getSimpleName() + "Flag_validation_for_comments_failed")));// screenshot
					}
					break;
				}
			}
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
		}

		pf.getLoginTRInstance(ob).logOutApp();
		closeBrowser();
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void searchAnArticle(String articleTitle) throws InterruptedException, Exception {
		//pf.getHFPageInstance(ob).searchForText(articleTitle);
		pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(articleTitle);
		waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString()), 120);
		int count = 0;
		boolean found = false;
		while (count < 10) {

			try {
				ob.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				ob.findElement(By.linkText(articleTitle)).click();
				found = true;
				break;

			} catch (Exception e) {

				count++;
			}

			((JavascriptExecutor) ob).executeScript("javascript:window.scrollBy(0,document.body.scrollHeight-150)");
			waitForAjax(ob);
		}
		if (!found)
			throw new Exception("Could not fiind the specified article:" + articleTitle);
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
