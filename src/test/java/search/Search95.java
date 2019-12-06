package search;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class Search95 extends TestBase {

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
	public void testcaseB84() throws Exception {
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
			// ob.navigate().to(CONFIG.getProperty("testSiteName"));

			// login using TR credentials
			login();
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_box_css")), 180);

			String postToSearch = "Post for Testing P9mW5A";

			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(postToSearch);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString())).click();
			waitForAjax(ob);

			String postTitle = ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css")))
					.getText();
			fluentwaitforElement(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString()), 60);
			String postAuthor = ob.findElement(
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_NAME_LINK_CSS.toString())).getText();
			fluentwaitforElement(ob, By.cssSelector("div[class='wui-descriptor wui-descriptor--uppercase']"), 60);

			String postCreationDate = ob.findElement(
					By.cssSelector("div[class='wui-descriptor wui-descriptor--uppercase']")).getText();
			fluentwaitforElement(ob,
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_DESC_LINK_CSS.toString()), 60);

			String profileMetaData = ob.findElement(
					By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_PROFILE_DESC_LINK_CSS.toString())).getText();
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

			waitForElementTobeVisible(ob, By.cssSelector("p[class*='wui-descriptor--snippet ng-binding']"), 30);
			String abst = ob.findElement(By.cssSelector("p[class*='wui-descriptor--snippet ng-binding']")).getText();
			System.out.println("lenght" + abst.length());
			if (abst.length() <= 302)
				test.log(LogStatus.PASS, "Snippet of post is dispalying for posts in search page");// extent
			else
				test.log(LogStatus.FAIL, "Snippet of post is  not dispalying properly for posts in search page");
			System.out.println(abst);

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
									+ "_patent_metadata_failed")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
