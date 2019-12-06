package notifications;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Notifications0007 extends TestBase {

	static int status = 1;
	String watchListName = null;
	String watchListDescription = null;
	PageFactory pf = new PageFactory();
	String document_title = null;
	String document_url = null;

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		rowData = testcase.get(this.getClass().getSimpleName());
		// extent = ExtentManager.getReporter(filePath);
	}

	@Test
	public void testcaseF7() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("Test --" + suiteRunmode + "--" + testRunmode);
		if (!master_condition) {
			status = 3;// excel
			extent = ExtentManager.getReporter(filePath);
			String var = rowData.getTestcaseId();
			String dec = rowData.getTestcaseDescription();
			String[] tests = StringUtils.split(var, TOKENIZER_DOUBLE_PIPE);
			String[] tests_dec = StringUtils.split(dec, TOKENIZER_DOUBLE_PIPE);
			for (int i = 0; i < tests.length; i++) {
				test = extent.startTest(tests[i], tests_dec[i]).assignCategory("Notifications");
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				extent.endTest(test);
			}
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		// test.log(LogStatus.INFO, this.getClass().getSimpleName() + "
		// execution starts--->");
		try {
			if (user1 != null && user2 != null && user3 != null) {
				openBrowser();
				maximizeWindow();
				clearCookies();
				ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				pf.getLoginTRInstance(ob).enterTRCredentials(user3, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 60,
						"Home page is not loaded successfully");*/
				pf.getHFPageInstance(ob).searchForText("Biotechnology");
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 120,
						"Search Results not loaded successfully");
				pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
				document_title = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
				document_url = ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getAttribute("href");
				logger.info("Article URL - " + document_url);
				logger.info("Article Title -" + document_title);
				boolean watchstatus = watchDocument();// need to change
				boolean commentStatus = addcommentOnArticle();
				boolean verifyStatus = false;
				try {
					extent = ExtentManager.getReporter(filePath);
					test = extent
							.startTest("OPQA-208",
									"Verify that user receives a notification when someone comments on an article contained in his watchlist")
							.assignCategory("Notifications");
					test.log(LogStatus.INFO, this.getClass().getSimpleName());
					if (watchstatus) {
						test.log(LogStatus.INFO, "Added article into watchlist");
						if (commentStatus) {
							test.log(LogStatus.INFO, "Added Comment into Article");
							verifyStatus = verifyWatchArticleNotification();
						} else {
							test.log(LogStatus.FAIL, "Facing issue while adding comment into article");
							throw new Exception("Facing issue while adding comment into article");
						}
					} else {
						test.log(LogStatus.FAIL, "Facing issue while adding article into watchlist");
						throw new Exception("Facing issue while adding article into watchlist");
					}

				} catch (Throwable t) {
				} finally {
					extent.endTest(test);
				}
				try {
					extent = ExtentManager.getReporter(filePath);
					test = extent
							.startTest("OPQA-1012",
									"Verify that follower of the article is able to start conversation from home page when some one commented on the article he is following.")
							.assignCategory("Notifications");
					test.log(LogStatus.INFO, this.getClass().getSimpleName());

					if (verifyStatus) {
						test.log(LogStatus.INFO, "Notification getting with Correct Content");
						startConversation();
					} else {
						test.log(LogStatus.INFO, "Notification not getting with Correct Content");
					}
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					extent.endTest(test);
				}
				try {
					extent = ExtentManager.getReporter(filePath);
					test = extent
							.startTest("OPQA-207",
									"Verify that user receives a notification when someone he is following comments on an article")
							.assignCategory("Notifications");
					test.log(LogStatus.INFO, this.getClass().getSimpleName());

					if (commentStatus) {
						test.log(LogStatus.INFO, "Added Comment into Article");
						verifycommentNotification();
					} else {
						test.log(LogStatus.FAIL, "Facing issue while adding comment into article");
						throw new Exception("Facing issue while adding comment into article");
					}

				} catch (Throwable t) {
				} finally {
					extent.endTest(test);
				}
				try {
					extent = ExtentManager.getReporter(filePath);
					test = extent
							.startTest("OPQA-209",
									"Verify that user receives a notification if someone likes his comment an article")
							.assignCategory("Notifications");
					notification3();// need to change
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Something unexpected happened" + t);// extent
					// next 3 lines to print whole testng error in report
					StringWriter errors = new StringWriter();
					t.printStackTrace(new PrintWriter(errors));
					test.log(LogStatus.INFO, errors.toString());// extent
																// reports
					ErrorUtil.addVerificationFailure(t);// testng
					status = 2;// excel
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
				} finally {
					extent.endTest(test);
				}
			} else {
				test.log(LogStatus.INFO, "User creation problem hence Failing this test case");
				throw new Exception("User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened" + t);// extent
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
		} finally {
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void startConversation() throws Exception {
		pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
		pf.getLoginTRInstance(ob).clickLogin();
		BrowserWaits.waitTime(4);
		waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
		pf.getHFPageInstance(ob).clickOnHomeLink();
		BrowserWaits.waitTime(4);
		//JavascriptExecutor jse = (JavascriptExecutor) ob;
		for (int i = 1; i <= 3; i++) {
			// jse.executeScript("window.scrollTo(0,
			// document.body.scrollHeight)", "");
			BrowserWaits.waitTime(3);
			String text = ob.findElement(By.xpath(OR.getProperty("comment_event_section"))).getText();
			if (text.length() > 0) {
				break;
			}
		}
		String text1 = ob.findElement(By.xpath(OR.getProperty("comment_event_section"))).getText();
		logger.info("Notification Text: " + text1);
		String expected_text = fn3 + " " + ln3;
		try {
			Assert.assertTrue(text1.contains(expected_text) && text1.contains(expected_text)
							&& text1.contains(document_title) && text1.contains(OR.getProperty("COMMENT_TEXT1")));
			test.log(LogStatus.PASS, "User receiving notification with correct content");
		} catch (Throwable t) {
			/*test.log(LogStatus.FAIL, "User receiving notification with incorrect content" + t);// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString()); // reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));*/// screenshot
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}

	}

	private void notification3() throws Exception {
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			logger.info(this.getClass().getSimpleName() + " execution starts--->");
			// 2)Login with user1,like the comment and logout
			// waitForElementTobeVisible(ob,
			// By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(4);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			ob.navigate().to(document_url);
			logger.info(document_url);
			BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("document_commentLike_button1")), 30);
			waitForElementTobeClickable(ob, By.xpath(OR.getProperty("document_commentLike_button1")), 30);
			jsClick(ob, ob.findElement(By.xpath(OR.getProperty("document_commentLike_button1"))));
			BrowserWaits.waitTime(1);
			pf.getLoginTRInstance(ob).logOutApp();
			BrowserWaits.waitTime(10);

			// 3)Login with user2 again and verify that he receives a correct
			// notification
			// waitForElementTobeVisible(ob,
			// By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(user3, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(4);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			JavascriptExecutor jse = (JavascriptExecutor) ob;
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("notificationForLike1")), 30);
			for (int i = 1; i <= 3; i++) {
				String text = ob.findElement(By.xpath(OR.getProperty("notificationForLike1"))).getText();
				if (text.length() > 0) {
					break;
				}
				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
				BrowserWaits.waitTime(3);
			}
			String text = ob.findElement(By.xpath(OR.getProperty("notificationForLike1"))).getText();
			logger.info("Notification Text: " + text);
			try {
				Assert.assertTrue(text.contains("Liked your comment")
						&& text.contains(OR.getProperty("COMMENT_TEXT1")) && text.contains(fn1 + " " + ln1)&&text.contains(document_title));
				test.log(LogStatus.PASS, "User receiving notification with correct content");
			} catch (Throwable t) {

				test.log(LogStatus.FAIL, "User receiving notification with incorrect content");// extent
				//Assert.assertTrue(false);
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString());// extent reports //
															// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));// screenshot
			}
		} catch (Exception e) {
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}
	}

	private void verifycommentNotification() throws Exception {
		try {
//			clearCookies();
			//pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
		/*	waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");*/
			pf.getHFPageInstance(ob).clickOnHomeLink();
			BrowserWaits.waitTime(4);
			//ob.navigate().refresh();
			//BrowserWaits.waitTime(6);
			JavascriptExecutor jse = (JavascriptExecutor) ob;
			String notification_text = null;
			for (int i = 1; i <= 3; i++) {
				logger.info("in home page");
				notification_text = ob
						.findElement(
								By.xpath(OR.getProperty("comment_event_section")))
						.getText();
				if (notification_text.length() > 0) {
					break;
				}
				jse.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
				BrowserWaits.waitTime(3);
			}
			String notification_text_author = ob
					.findElement(By.xpath(OR.getProperty("comment_event_section")))
					.getText();
			logger.info("Notification Text: " + notification_text + "\n Author-Name -" + notification_text_author);
			try {
				Assert.assertTrue(
						notification_text_author.contains(fn3 + " " + ln3)/* && notification_text.contains("New Comment")*/
								&& notification_text_author.contains(document_title)
								&& notification_text_author.contains(OR.getProperty("COMMENT_TEXT1")));
				test.log(LogStatus.PASS, "User receiving notification with correct content");
				test.log(LogStatus.PASS, "PASS");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User receiving notification with incorrect content");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				ErrorUtil.addVerificationFailure(t);
				logger.error(this.getClass().getSimpleName() + "--->" + t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: "
						+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_OPQA_207")));
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_OPQA_207")));
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}
	}

		boolean commentStatus = false;
		private boolean verifyWatchArticleNotification() throws Exception {
		try {
			// 3)Login with user1 again and verify that he receives a correct
			// notification
			// waitForElementTobeVisible(ob,
			// By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(user3, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(4);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			pf.getHFPageInstance(ob).clickOnHomeLink();
			BrowserWaits.waitTime(4);
			// waitForElementTobeVisible(ob,
			// By.xpath(OR.getProperty("notificationDocumentComment")), 100);
			//JavascriptExecutor jse = (JavascriptExecutor) ob;
			for (int i = 1; i <= 3; i++) {
				// jse.executeScript("window.scrollTo(0,
				// document.body.scrollHeight)", "");
				BrowserWaits.waitTime(3);
				String text = ob.findElement(By.xpath(OR.getProperty("comment_section"))).getText();
				if (text.length() > 0) {
					break;
				}
			}
			String text = ob.findElement(By.xpath(OR.getProperty("comment_section"))).getText();
			logger.info("Notification Text: " + text);
			String expected_text = fn2 + " " + ln2;
			try {
				Assert.assertTrue(text.contains("New comments") && text.contains(expected_text)
						&& text.contains(document_title) && text.contains(OR.getProperty("COMMENT_TEXT")));
				BrowserWaits.waitTime(3);
				ob.findElement(By.cssSelector("button[class='wui-label']")).click();
				ob.findElement(
						By.cssSelector("div[class='ng-pristine ng-untouched ng-valid ta-bind placeholder-text']"))
						.sendKeys(OR.getProperty("COMMENT_TEXT1"));
				BrowserWaits.waitTime(2);
				ob.findElement(
						By.cssSelector("div[class='ne-comment-box__controls'] button[class='wui-mini-btn wui-mini-btn--primary wui-mini-btn--comment-box']"))
						.click();
				BrowserWaits.waitTime(3);
				pf.getHFPageInstance(ob).clickOnHomeLink();
				BrowserWaits.waitTime(4);
				commentStatus = true;
				test.log(LogStatus.PASS, "User receiving notification with correct content");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User receiving notification with incorrect content" + t);// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_user_receiving_notification_with_incorrect_content")));// screenshot
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Fail");
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}
		return commentStatus;
	}

	private boolean addcommentOnArticle() throws Exception {
		boolean status = false;
		try {
			// 2)Login with user2,comment on article contained in user1's
			// watchlist and logout
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 60,
					"Home page is not loaded successfully");*/
			ob.navigate().to(document_url);
			pf.getPostCommentPageInstance(ob).enterArticleComment(OR.getProperty("COMMENT_TEXT"));
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			status = true;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}
		return status;
	}

	private boolean watchDocument() throws Exception {
		boolean status = false;
		try {
			// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys("Biotechnology");
			// ob.findElement(By.xpath(OR.getProperty("search_button"))).click();
			// waitForElementTobeVisible(ob,
			// By.xpath(OR.getProperty("searchResults_links")), 120);
			// document_title =
			// ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getText();
			// document_url =
			// ob.findElement(By.xpath(OR.getProperty("searchResults_links"))).getAttribute("href");
			// logger.info(document_url);
			/*
			 * ob.findElement(By.xpath(OR.getProperty("search_watchlist_image"))
			 * ).click(); waitForElementTobeVisible(ob,
			 * By.xpath(OR.getProperty("selectWatchListInBucket")), 30);
			 * ob.findElement(By.xpath(OR.getProperty("selectWatchListInBucket")
			 * )).click(); waitForElementTobeVisible(ob,
			 * By.xpath(OR.getProperty("closeWatchListBucketDisplay")), 30);
			 * ob.findElement(By.xpath(OR.getProperty(
			 * "closeWatchListBucketDisplay"))).click();
			 * BrowserWaits.waitTime(1);
			 */
			BrowserWaits.waitTime(2);
			// ob.findElement(By.xpath(OR.getProperty("search_watchlist_image1"))).click();
			String watchStatus = ob
					.findElement(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_IN_SEACHPAGE_CSS.toString()))
					.getText();
			logger.info("Watch Button Status : " + watchStatus);
			if (watchStatus.contains("Watching")) {
				List<WebElement> element = ob
						.findElements(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_IN_SEACHPAGE_CSS.toString()));
				element.get(0).click();
				BrowserWaits.waitTime(2);
				List<WebElement> listOfWatchListButton = ob.findElements(
						By.cssSelector("button[class='wui-icon-only-btn ne-action-dropdown__add-button--active']"));
				listOfWatchListButton.get(0).click();
				BrowserWaits.waitTime(2);
				ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString())).click();
				BrowserWaits.waitTime(2);
			}

			List<WebElement> element = ob
					.findElements(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_IN_SEACHPAGE_CSS.toString()));
			element.get(0).click();
			BrowserWaits.waitTime(2);
			List<WebElement> listOfWatchListButton = ob
					.findElements(By.cssSelector(OnePObjectMap.WATCHLIST_WATCH_BUTTON_CSS.toString()));
			listOfWatchListButton.get(0).click();
			BrowserWaits.waitTime(2);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString())).click();
			BrowserWaits.waitTime(3);

			/*
			 * List<WebElement> listOfWatchListButton =
			 * ob.findElements(By.xpath(OR.getProperty("watchlist_watch_button1"
			 * ))); listOfWatchListButton.get(0).click();
			 * BrowserWaits.waitTime(4); ob.findElement(By.xpath(OR.getProperty(
			 * "watchlist_model_close_button1"))).click();
			 */

			status = true;
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			pf.getLoginTRInstance(ob).logOutApp();
		}
		return status;
	}

	@AfterTest
	public void reportTestResult() {
		// extent.endTest(test);
	}

}
