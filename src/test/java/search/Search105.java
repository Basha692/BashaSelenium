package search;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

public class Search105 extends TestBase {

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
	public void testcaseB105() throws Exception {

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
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_box_css")), 120);

			String post = "sample post";
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(post);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			waitForAjax(ob);
         pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			waitForAjax(ob);

			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css"))).click();
			waitForAjax(ob);
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_patent_record_view_watch_share_css")),
					120);

			String patentRVTitle = ob.findElement(By.cssSelector(OR.getProperty("tr_patent_record_view_css")))
					.getText();
			// System.out.println(patentRVTitle);
			String patentRVTitleWatchLabel = ob.findElement(
					By.cssSelector(OR.getProperty("tr_patent_record_view_watch_share_css"))).getText();
			// System.out.println(patentRVTitleWatchLabel);

			boolean googleShare = ob
					.findElements(By.cssSelector("div[class='ne-publication-sidebar__social-share'] button")).get(0)
					.getAttribute("data-uib-tooltip").contains("Share on Google");

			boolean twitterShare = ob
					.findElements(By.cssSelector("div[class='ne-publication-sidebar__social-share'] button")).get(2)
					.getAttribute("data-uib-tooltip").contains("Share on Twitter");

			boolean fbShare = ob
					.findElements(By.cssSelector("div[class='ne-publication-sidebar__social-share'] button a")).get(0)
					.getAttribute("data-uib-tooltip").contains("Share on Facebook");

			boolean liShare = ob
					.findElements(By.cssSelector("div[class='ne-publication-sidebar__social-share'] button a")).get(1)
					.getAttribute("data-uib-tooltip").contains("Share on LinkedIn");

			List<WebElement> postCreationAndEdit = ob.findElements(By
					.xpath("//span[contains(@class,'ne-publication__metadata--post')]"));
			boolean postEditCreateDate;
			if (postCreationAndEdit.size() >= 2) {
				postEditCreateDate = postCreationAndEdit.get(0).getText().equalsIgnoreCase("EDITED")
						&& postCreationAndEdit.get(1).getText().equalsIgnoreCase("POSTED");
			} else {
				postEditCreateDate = postCreationAndEdit.get(0).getText().contains("POSTED");
			}

			// System.out.println("post creationstatus-->"+postEditCreateDate);

			String postAuthor = ob.findElement(By.xpath(OR.getProperty("tr_search_people_profilename_link_xpath")))
					.getText();

			String postAuthorMetaData = ob.findElement(
					By.xpath(OR.getProperty("tr_search_people_profile_description_xpath"))).getText();

			// System.out.println("post author-->"+postAuthor);
			// System.out.println("post author metadata-->"+postAuthorMetaData);

			List<WebElement> postSocialShare = ob
					.findElements(By
							.cssSelector(("div[class='ne-publication-sidebar'] span[class='wui-icon-metric__value ng-binding']")));

			int postCommentCount = Integer.parseInt(postSocialShare.get(0).getText());
			int postLikeCount = Integer.parseInt(postSocialShare.get(1).getText());

			boolean socialShareStatus = (postCommentCount >= 0 && postLikeCount >= 0);

			// System.out.println("post comment count-->"+postCommentCount);
			// System.out.println("post like count-->"+postLikeCount);

			boolean patentRVStatus = StringUtils.containsIgnoreCase(patentRVTitle, post)
					&& StringUtils.containsIgnoreCase(patentRVTitleWatchLabel, "Watching") && googleShare
					&& twitterShare && fbShare && liShare;

			// if(!patentRVStatus)
			// throw new Exception("Page is not Navigating to Post Record View Page");

			boolean postFieldsStatus = postEditCreateDate && (!postAuthor.isEmpty()) && (!postAuthorMetaData.isEmpty())
					&& socialShareStatus && patentRVStatus;
			System.out.println("post fields status-->" + postFieldsStatus);

			if (!checkElementPresence("watchlist_button_record_view_page")) {

				test.log(LogStatus.FAIL, "Record view page of a post not getting displayed");// extent
																								// report
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_record_view_page_of_a_post_not_getting_displayed")));// screenshot
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
									+ "_patent_recordview_failed")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
