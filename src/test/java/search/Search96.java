package search;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
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

public class Search96 extends TestBase {

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
	public void testcaseB84() throws Exception {

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
			waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString()), 180);

			String postToSearch = "Post for Testing P9mW5A";

			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_SEARCH_TEXTBOX_XPATH.toString())).sendKeys(postToSearch);
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
			//waitForAjax(ob);
			BrowserWaits.waitTime(5);

			String postTitle = ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_POST_TITLE_CSS.toString()))
					.getText();
			BrowserWaits.waitTime(3);
			String postAuthor = ob
					.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()))
					.getText();
			BrowserWaits.waitTime(3);
			String postCreationDate = ob
					.findElement(By.cssSelector("div[class='wui-descriptor wui-descriptor--uppercase']")).getText();
			String profileMetaData = ob
					.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_DESC_LINK_CSS.toString()))
					.getText();
			String statsXpath = "div[class='wui-card__footer-content'] results-metrics span";
			String postLikeCount = ob.findElements(By.cssSelector(statsXpath)).get(4).getText();
			String postLikeLabel = ob.findElements(By.cssSelector(statsXpath)).get(5).getText();

			String postCommentCount = ob.findElements(By.cssSelector(statsXpath)).get(2).getText();
			String postCommentLabel = ob.findElements(By.cssSelector(statsXpath)).get(3).getText();

			boolean isPostTitleAvailable = StringUtils.containsIgnoreCase(postTitle, postToSearch);
			boolean isPostedByAuthor = postAuthor.isEmpty();
			boolean ispostCreationDateAndTimeAvailable = StringUtils.containsIgnoreCase(postCreationDate, "2016");
			// && (StringUtils.containsIgnoreCase(postCreationDate, "AM") || StringUtils.containsIgnoreCase(
			// postCreationDate, "PM"));
			boolean ispostAuthorMetadataAvailable = profileMetaData.isEmpty();
			boolean isPostLikeCountAvailable = Integer.parseInt(postLikeCount) >= 0
					&& postLikeLabel.equalsIgnoreCase("Likes");
			boolean isPostCommentCountAvailable = Integer.parseInt(postCommentCount) >= 0
					&& postCommentLabel.equalsIgnoreCase("Comments");

			if (!(isPostTitleAvailable && (!isPostedByAuthor) && ispostCreationDateAndTimeAvailable
					&& (!ispostAuthorMetadataAvailable) && isPostLikeCountAvailable && isPostCommentCountAvailable)) {
				throw new Exception("Post all fiedls are not getting displayed in search results ALL page");
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
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_patent_metadata_failed")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
