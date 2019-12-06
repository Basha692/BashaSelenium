package watchlist;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

/**
 * Class for Verify that user is able to comment on an item contained in public
 * watchlist of some other user
 * 
 * 
 * @author Jagadeesh
 */
public class Watchlist035 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Watchlist");
	}

	@Test
	@Parameters({ "articleName","user" })
	
	public void testCommentOnHisWatchlistItems(String articleName, String user) throws Exception {

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

			ob.navigate().to(host);
			loginAsSpecifiedUser(LOGIN.getProperty("Watchlist028_User"), LOGIN.getProperty("Watchlist028_PWD"));

			// Create watch list
			String newWatchlistName = this.getClass().getSimpleName() + "_" + getCurrentTimeStamp();
			createWatchList("public", newWatchlistName, "This is my test watchlist.");

			// Searching for article
			ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(articleName);
			ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			 waitForAjax(ob);
			//waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 65);

			BrowserWaits.waitTime(6);
			waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("searchResults_links")), 90);
			String document_title = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();

			logger.info("doc title --->" + document_title);

			// Watching an article to a particular watch list
			WebElement watchButton = ob.findElement(By.xpath(OR.getProperty("search_watchlist_image")));
			watchOrUnwatchItemToAParticularWatchlist(newWatchlistName, watchButton);

			// Navigate to a particular watch list page
			navigateToParticularWatchlistPage(newWatchlistName);

			List<WebElement> watchedItems = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
			for (int i = 0; i < watchedItems.size(); i++) {
				if (watchedItems.get(i).getText().equals(document_title)) {
					test.log(LogStatus.PASS, "successfully added the atricle in particular watchlist");
				} else {
					logFailureDetails(test, "user is not able to added item to the Particular watchlist",
							"_user_unable_to_add_item_to_particularwatchlist");
					test.log(LogStatus.FAIL, "user cannot able to added item to the particular watachlist ");
				}
			} //for 
			pf.getLoginTRInstance(ob).logOutApp();
			ob.close();

				try {

					openBrowser();
					maximizeWindow();
					clearCookies();

					ob.navigate().to(host);

					// user2 login
					loginAsSpecifiedUser(LOGIN.getProperty("LOGINUSERNAME2"), LOGIN.getProperty("LOGINPASSWORD2"));
					
					pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(user);
					
					pf.getSearchProfilePageInstance(ob).clickPeople();

					pf.getProfilePageInstance(ob).clickProfile();
					pf.getProfilePageInstance(ob).clickWatchlistTab();
					
					
					//Wait for watchlist page to load
					ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).click();
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.SEARCH_RESULTS_PAGE_DOCUMENT_ADD_TO_GROUP_CSS);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEWSFEED_POST_CARD_POST_TITLE_ADD_TO_WATCHLIST_CSS);
					pf.getBrowserWaitsInstance(ob).waitUntilText("Comments");
					
					

					List<WebElement> watchedItems2 = ob.findElements(By.xpath(OR.getProperty("searchResults_links")));
					pf.getBrowserWaitsInstance(ob).waitUntilText("Comments");
					//BrowserWaits.waitTime(6);
					

					logger.info("watched Itemsss size:" + watchedItems2.size());
					for (int j = 0; j < watchedItems2.size(); j++) {

						logger.info("watched item:" + watchedItems2.get(j).getText());
						if (watchedItems2.get(j).getText().equals(document_title)) {
							watchedItems2.get(j).click(); 
							
							String beforecommentcountText = ob
									.findElements(By.cssSelector(
											OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()))
									.get(0).getText();
							int beforecommentcount = Integer.parseInt(beforecommentcountText);
							logger.info("Before comment count:" + beforecommentcount);
							test.log(LogStatus.INFO, "Before commentcount" + beforecommentcount);

							waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_comment_textbox_patent")),
									90);
							ob.findElement(By.xpath(OR.getProperty("document_comment_textbox_patent")))
									.sendKeys("Automation Script Comment: Watchlist035 test");
							
							pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
									OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
							
							pf.getBrowserActionInstance(ob)
									.scrollToElement(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
							pf.getBrowserActionInstance(ob)
									.jsClick(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS);
							BrowserWaits.waitTime(6);
							
							waitForElementTobeVisible(ob,
									By.cssSelector(
											OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()),
									30);
							String aftercommentcountText = ob.findElements(By.cssSelector(
									OnePObjectMap.HOME_PROJECT_NEON_RVIEW_ARTICLE_COMMENTCOUNT_CSS.toString()))

									.get(0).getText();
							int aftercommentscount = Integer.parseInt(aftercommentcountText);
							logger.info("After comment count:" + aftercommentscount);
							test.log(LogStatus.INFO, "After commentcount" + aftercommentscount);

							if (aftercommentscount > beforecommentcount) {
								test.log(LogStatus.PASS,
										"user is able to comment on   public watchlist items of some other user");

							} else {
								logFailureDetails(test,
										"user is not able to comment on  public watchlist itemsof some other User",
										"_user_unable_to_comment_on_public_watchlist_items_of_someotheruseer");
							}
							closeBrowser();

						} else {
							logFailureDetails(test, "not navigate particular watchlistItem", "watchlistfail");
						}
						closeBrowser();
					}

				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Something unexpected happened");// extent

					StringWriter errors = new StringWriter();
					t.printStackTrace(new PrintWriter(errors));
					test.log(LogStatus.INFO, errors.toString());
					ErrorUtil.addVerificationFailure(t);
					status = 2;
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
					closeBrowser();
				}
		}

		catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");

			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
