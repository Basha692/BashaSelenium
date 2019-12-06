package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * This class contains all the methods related to Post record view page
 * 
 * @author uc205521
 *
 */
public class PostRecordViewPage extends TestBase {

	PageFactory pf;

	public PostRecordViewPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method to click on SHARE button in post record view
	 */
	public void clickOnShareButton() {
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS.toString()), 180);
		jsClick(ob, ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS.toString())));

	}

	/**
	 * Method to click on Share on Facebook link under share menu in post record
	 * view
	 */
	public void clickOnFacebookUnderShareMenu() {
		// clickOnShareButton();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_FACEBOOK_CSS.toString()), 180);
		jsClick(ob, ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_FACEBOOK_CSS.toString())));
	}

	/**
	 * Method to click on Share on LinkedIn Link under share menu in post record
	 * view
	 */
	public void clickOnLinkedInUnderShareMenu() {
		// clickOnShareButton();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_LINKEDIN_CSS.toString()), 180);
		jsClick(ob, ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_LINKEDIN_CSS.toString())));
	}

	/**
	 * Method to click on Share on Twitter link under share menu in post record
	 * view
	 */
	public void clickOnTwitterUnderShareMenu() {
		// clickOnShareButton();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_TWITTER_CSS.toString()), 180);
		jsClick(ob,
				ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_SHARE_TWITTER_CSS.toString())));
	}

	/**
	 * Method to click on Watch in post record view
	 */
	public void clickOnWatchButton() {
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_WATCH_CSS.toString()),
				180);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_WATCH_CSS.toString())).click();
	}

	/**
	 * Method to click on EDIT post icon in post record view
	 * 
	 * @throws InterruptedException
	 */
	public void clickOnEditButton() throws InterruptedException {
		// Commented by KR
		// waitForElementTobeVisible(ob,
		// By.xpath(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_EDIT_XPATH.toString()),
		// 180);
		//BrowserWaits.waitTime(5);
		waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_EDIT_XPATH.toString()), 200);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_EDIT_XPATH.toString())));
	}

	/**
	 * Method to click on Stop Watching button in post record view
	 */
	public void clickOnStopWatchingButton() {
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_STOP_WATCH_CSS.toString()), 180);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_STOP_WATCH_CSS.toString())).click();
	}

	/**
	 * This method performs like or Un-like operations on posts and validate the
	 * count increment.
	 * 
	 * @param test
	 * @return
	 * @throws InterruptedException
	 */
	public boolean validateLikeAndUnlikePostActions(ExtentTest test) throws InterruptedException {
		boolean result;
		WebElement appreciationButton;
		int countBefore, countAfter;
		waitForAjax(ob);
		BrowserWaits.waitTime(10);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()), 180);
		countBefore = Integer.parseInt(ob
				.findElement(
						By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()))
				.getText().replaceAll(",", "").trim());
		appreciationButton = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_CSS.toString()));

		if (!appreciationButton.getAttribute("class").contains("active")) {
			appreciationButton.click();
			Thread.sleep(15000);// After clicking on like button wait for status
								// to change and count update
			new Actions(ob).moveByOffset(100, 200).build().perform();
			countAfter = Integer.parseInt(ob
					.findElement(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()))
					.getText().replaceAll(",", "").trim());
			try {
				Assert.assertEquals(countAfter, countBefore + 1);
				test.log(LogStatus.PASS, "Appreciation action on post is working as expected");
				result = true;
			} catch (Throwable e) {
				result = false;
				test.log(LogStatus.FAIL, "Appreciation action on post is not working as expected");
			}

		} else {
			appreciationButton.click();
			Thread.sleep(15000);// After clicking on unlike button wait for
								// status to change and count update
			new Actions(ob).moveByOffset(100, 200).build().perform();
			countAfter = Integer.parseInt(ob
					.findElement(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()))
					.getText().replaceAll(",", "").trim());
			try {
				Assert.assertEquals(countAfter, countBefore - 1);
				test.log(LogStatus.PASS, "un Appreciation action on post is working as expected");
				result = true;
			} catch (Throwable e) {
				result = false;
				test.log(LogStatus.FAIL, "un Appreciation action on post is not working as expected");
			}

		}

		return result;

	}

	/**
	 * Method to Validate Post title and Profile meta data in record view page
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validatePostTitleAndProfileMetadata(String post, List<String> profileInfo) throws Exception {

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS);
		BrowserWaits.waitTime(1);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_METADATA_CSS);

		String postTitle = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS).getText();
		waitForPageLoad(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_METADATA_CSS.toString()), 180);
		logger.info("post -->" + post + "post title-->" + postTitle);

		Assert.assertEquals(post, postTitle);
		BrowserWaits.waitTime(5);
		String postRVProfileTitle = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_TILE_CSS).getText();
		logger.info("Post RV title-->" + postRVProfileTitle);
		logger.info("Profile info-->" + profileInfo);

		String profileData = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_METADATA_CSS).getText();

		logger.info("Profile metadata-->" + profileData);
		if (!(/* profileInfo.contains(profileData) && */ profileInfo.contains(postRVProfileTitle))) {
			throw new Exception("Profile info mismatching in Record view page of a Post");
		}

	}

	/**
	 * This method is to validate the Post Creation date in Post record view
	 * page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean verifyPostCreationDate() throws Exception {
		boolean isDisplayed = false;
		waitForPageLoad(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TIMESTAMP_CSS.toString()), 180);

		List<WebElement> timestamp = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TIMESTAMP_CSS);
		String time;
		for (WebElement we : timestamp) {
			time = we.getText();
			if (time.contains("POSTED") && time.contains("|") && (time.contains("AM") || time.contains("PM"))) {
				isDisplayed = true;
				break;
			}

		}

		return isDisplayed;
	}

	/**
	 * This method is to validate the Post modification date in Post record view
	 * page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean verifyPostEditedDate() throws Exception {
		boolean isDisplayed = false;
		BrowserWaits.waitTime(5);
		waitForPageLoad(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TIMESTAMP_CSS.toString()), 180);
		List<WebElement> timestamp = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TIMESTAMP_CSS);
		String time;
		for (WebElement we : timestamp) {
			time = we.getText();
			if (time.contains("EDITED") && time.contains("|") && (time.contains("AM") || time.contains("PM"))) {
				isDisplayed = true;
				break;
			}

		}

		return isDisplayed;
	}

	/**
	 * This method captures and returns the post title from Post record view
	 * page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPostTitle() throws Exception {
		waitForPageLoad(ob);
//		pf.getBrowserWaitsInstance(ob)
//				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS.toString()), 300);
		return pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS)
				.getText();
	}

	/**
	 * This method captures and returns the post content from Post record view
	 * page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPostContent() throws Exception {
//		pf.getBrowserWaitsInstance(ob)
//				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS.toString()), 300);
		return pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS)
				.getText();
	}

	/**
	 * This method captures and returns the post video attachment url from Post
	 * record view page
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getPostVideoUrl() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_VIDEO_BOX_XPATH);
		String VideoUrl = ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_VIDEO_BOX_XPATH.toString()))
				.getAttribute("src");
		return VideoUrl.substring(2, VideoUrl.indexOf("?"));

	}

	/**
	 * This method takes the link as as argument and validates that the post
	 * content contains the specified link.
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public boolean validatePostContentForExternalLink(String string) throws Exception {
		BrowserWaits.waitTime(6);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);
		WebElement content = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);
		if (content.findElements(By.linkText(string)).size() != 0)
			return true;
		else
			return false;

	}

	/**
	 * This method takes the link as as argument and click on it.
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 */
	public void clickExternalLinkInPostContent(String url) throws Exception {
		waitForPageLoad(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);
		WebElement content = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);
		content.findElement(By.linkText(url)).click();
		waitForPageLoad(ob);
	}

	/**
	 * This method validates the page URL
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public boolean validateURL(String url) throws Exception {
		boolean result = false;
		waitForPageLoad(ob);
		BrowserWaits.waitTime(6);
		String PARENT_WINDOW = ob.getWindowHandle();
		Set<String> child_window_handles = ob.getWindowHandles();
		// System.out.println("window
		// hanles-->"+child_window_handles.size());
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				maximizeWindow();
				BrowserWaits.waitTime(10);

				// ob.switchTo().window(PARENT_WINDOW);
			}
			if (ob.getCurrentUrl().toLowerCase().contains(url.toLowerCase())) {
				result = true;
				ob.switchTo().window(PARENT_WINDOW);
			}
		}
		return result;

	}

	/**
	 * Method to click on post author name in Post record view page.
	 * 
	 * @throws Exception
	 */
	public void clickOnAuthorName() throws Exception {
		waitForPageLoad(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_TILE_CSS);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_PROFILE_TILE_CSS.toString())));
	}

	/**
	 * Method to check whether comment count is displayed in Post record view
	 * page
	 * 
	 * @return
	 */
	public boolean isCommentCountDisplayed() {
		boolean result = false;
		try {

			waitForElementTobeVisible(ob,
					By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_COMMENTS_COUNT_XPATH.toString()), 60);
			if (ob.findElement(
					By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_COMMENTS_COUNT_XPATH.toString()))
					.isDisplayed())
				result = true;
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	/**
	 * Method to check whether like count is displayed in Post record view page
	 * 
	 * @return
	 */
	public boolean isLikeCountDisplayed() {
		boolean result = false;
		try {

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()), 60);
			if (ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_COUNT_CSS.toString()))
					.isDisplayed())
				result = true;
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	/**
	 * Method to check whether Like button is displayed in Post record view page
	 * 
	 * @return
	 */
	public boolean isLikeButtonDisplayed() {
		boolean result = false;
		try {

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_CSS.toString()), 60);
			if (ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_APPRECIATION_CSS.toString()))
					.isDisplayed())
				result = true;
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	/**
	 * Method to check whether Follow/Un-follow button is displayed in Post
	 * record view page
	 * 
	 * @return
	 */
	public boolean IsFollowOrUnfollowButtonDispayed() {
		boolean result = false;
		try {

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()), 120);
			if (ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
					.isDisplayed())
				result = true;
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	/**
	 * This method validates the follow or un follow function in Post record
	 * view page.
	 * 
	 * @param test
	 * @throws InterruptedException
	 */
	public void validateFollowOrUnfollow(ExtentTest test) throws InterruptedException {
		BrowserWaits.waitTime(10);
		waitForPageLoad(ob);
		waitForAjax(ob);
		String attribute = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
				.getAttribute("data-uib-tooltip");

		if (attribute.equalsIgnoreCase("Follow this person")) {
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
					.click();

			BrowserWaits.waitTime(10);
			attribute = ob
					.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
					.getAttribute("data-uib-tooltip");

			Assert.assertTrue(attribute.equalsIgnoreCase("Unfollow this person"));
			test.log(LogStatus.PASS, "Follow functionality is working fine in view post record page");

		} else {
			ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
					.click();
			BrowserWaits.waitTime(10);
			attribute = ob
					.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_FOLLOW_BUTTON_CSS.toString()))
					.getAttribute("data-uib-tooltip");

			Assert.assertTrue(attribute.equalsIgnoreCase("Follow this person"));
			test.log(LogStatus.PASS, "UnFollow functionality is working fine in view post record page");

		}
	}

	/**
	 * Metod returns the count of the comments on post record view page.
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getCommentCount() throws Exception {
		BrowserWaits.waitTime(5);
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_COMMENTS_COUNT_XPATH.toString()), 90);
		String count = ob
				.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_COMMENTS_COUNT_XPATH.toString()))
				.getText().replaceAll(",", "").trim();

		return Integer.parseInt(count);
	}

	/**
	 * Method validates that specified comment is displayed in Post record view
	 * page.
	 * 
	 * @param comment
	 * @param test
	 */
	public void validateCommentNewlyAdded(String comment, ExtentTest test) {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString()), 180);

		String actComment = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString())).get(0)
				.getText();

		Assert.assertTrue(actComment.contains(comment));
		test.log(LogStatus.PASS, "Newly added comment on post is available");

	}

	/**
	 * Method to share the records on Facebook
	 * 
	 * @param fbusername
	 * @param fbpassword
	 * @throws Exception
	 */
	public void shareRecordOnFB(String fbusername, String fbpassword) throws Exception {
		/*
		 * waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS.toString()), 80);
		 * jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS .toString())));
		 */
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_FB_CSS.toString()), 40);
		String PARENT_WINDOW = ob.getWindowHandle();
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_FB_CSS.toString())));
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_USERNAME_NAME);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_USERNAME_NAME, fbusername);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_PASSWORD_XPATH, fbpassword);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_LOGIN_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_SHARE_LINK_CSS);
				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_FB_SHARE_LINK_CSS);
				ob.switchTo().window(PARENT_WINDOW);
			}
		}
	}

	public void shareRecordOnGoogle(String fbusername, String fbpassword) throws Exception {
		/*
		 * waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS.toString()), 80);
		 * jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS .toString())));
		 */
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_GOOGLE_CSS.toString()), 40);
		String PARENT_WINDOW = ob.getWindowHandle();
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_GOOGLE_CSS.toString())));
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS, fbusername);
				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_NEXT_CSS);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_PASSWORD_CSS, fbpassword);
				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_LOGIN_CSS);
				waitForElementTobeClickable(ob,
						By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_SHARE_XPATH.toString()),
						90);

				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_SHARE_XPATH);
				ob.switchTo().window(PARENT_WINDOW);
			}
		}
	}

	/**
	 * Method to share the records on LinkedIn
	 * 
	 * @param liusername
	 * @param lipassword
	 * @throws Exception
	 */
	public void shareOnLI(String liusername, String lipassword) throws Exception {
		/*
		 * waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS.toString()), 80);
		 * jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.
		 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_CSS .toString())));
		 */
		String PARENT_WINDOW = ob.getWindowHandle();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_LI_CSS.toString()), 40);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_LI_CSS.toString())));
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CSS.toString()),
		// 60);
		BrowserWaits.waitTime(5);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_LI_SHARE_MODAL_SHARE_BUTTON_CSS.toString())));

		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		System.out.println("window hanles-->" + child_window_handles.size());
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				System.out.println("child window--->" + ob.getTitle());
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_USERNAME_CSS);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_USERNAME_CSS, liusername);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_PASSWORD_CSS, lipassword);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_LOGIN_CSS);
				ob.switchTo().window(PARENT_WINDOW);
				waitForElementTobeVisible(ob,
						By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_LI_SHARE_MODAL_CANCEL_BUTTON_CSS.toString()), 40);
				jsClick(ob, ob.findElement(
						By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_LI_SHARE_MODAL_CANCEL_BUTTON_CSS.toString())));
			}
		}

	}

	/**
	 * Method to share the records on Twitter
	 * 
	 * @param tusername
	 * @param tpassword
	 * @throws Exception
	 */
	public void shareOnTwitter(String tusername, String tpassword) throws Exception {

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_TWITTER_CSS.toString()),
				40);
		String PARENT_WINDOW = ob.getWindowHandle();
		String rvPageurl = ob.getCurrentUrl();
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_TWITTER_CSS.toString())));
		// ob.manage().window().maximize();
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				ob.manage().window().maximize();
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWITTER_USERNAME_CSS);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWITTER_USERNAME_CSS, tusername);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWITTER_PASSWORD_CSS, tpassword);
				jsClick(ob, ob.findElement(By.cssSelector(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWITTER_LOGIN_CSS.toString())));
				// waitForPageLoad(ob);
				BrowserWaits.waitTime(10);
				// waitForElementTobeVisible(ob,
				// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWEET_DESC_CSS.toString()),
				// 180);
				// String
				// articleDesc=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWEET_DESC_CSS).getText();
				// System.out.println("Article Desc-->"+articleDesc+"page
				// url-->"+rvPageurl);
				// if(!articleDesc.contains(rvPageurl)){
				// throw new Exception("Sharing Article Description not
				// populated on Twitter Page");
				// }
				/*
				 * waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.
				 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWEET_CSS.toString()),
				 * 180); pf.getBrowserActionInstance(ob).click(OnePObjectMap.
				 * HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_TWEET_CSS);
				 */
				BrowserWaits.waitTime(10);
				ob.switchTo().window(PARENT_WINDOW);
				BrowserWaits.waitTime(10);
			}
		}
	}

	/**
	 * click post link
	 * 
	 * @throws Exception
	 */
	public void clickPostLike() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_LIKE_XPATH);
		BrowserWaits.waitTime(2);
	}

	/**
	 * Method to validate whether a commented is flagged or unflagged.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void validateFlagAndUnflagActionOnPost(ExtentTest test) throws Exception {
		waitForAjax(ob);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString()),
				60);
		String attribute = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString()))
				.getAttribute("class");
		if (attribute.contains("fa-flag-o")) {
			flagOrUnflagAPost();
			BrowserWaits.waitTime(6);
			attribute = ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString()))
					.getAttribute("class");
			Assert.assertTrue(!attribute.contains("fa fa-flag-o"));
			test.log(LogStatus.PASS, "User is able to flag the post");

		} else {
			flagOrUnflagAPost();
			BrowserWaits.waitTime(3);
			attribute = ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString()))
					.getAttribute("class");
			Assert.assertTrue(attribute.contains("fa fa-flag"));
			test.log(LogStatus.PASS, "User is able to Unflag the post");

		}

	}

	/**
	 * Method to click on FLAG or UNFLAG on the comment
	 * 
	 * @throws InterruptedException
	 */
	private void flagOrUnflagAPost() throws InterruptedException {
		BrowserWaits.waitTime(2);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString())).click();
		BrowserWaits.waitTime(3);
		// Commented by KR
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CSS.toString()),
		// 40);

		ob.findElement(By.xpath("//span[ng-transclude[span[text()='Other']]]/preceding-sibling::span")).click();

		// jsClick(ob, ob
		// .findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CHECKBOX_CSS.toString())));
		BrowserWaits.waitTime(3);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_FLAG_BUTTON_CSS.toString())));
	}

	/**
	 * Method to delete the post from Post record view page
	 * 
	 * @throws Exception
	 */
	public void deletePost() throws Exception {
		waitForAjax(ob);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_DELETE_BUTTON_CSS.toString()),
				180);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_DELETE_BUTTON_CSS.toString())));

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_DELETE_CONFIRMATION_BUTTON_CSS.toString()), 180);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_VIEW_POST_DELETE_CONFIRMATION_BUTTON_CSS.toString())));
		waitForAjax(ob);
	}

	/**
	 * Method to add links to the comments.
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void addExternalLinkComments(String url) throws Exception {
		BrowserWaits.waitTime(15);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTAREA_CSS.toString()),
				40);
		ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTAREA_CSS.toString())).click();
		BrowserWaits.waitTime(4);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_COMMENTS_INSERT_LINK_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_COMMENTS_INSERT_LINK_CSS.toString())).click();
		scrollingToElementofAPage();
		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_URL_TEXT_BOX_CSS)
				.sendKeys(url);

		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_URL_INSERT_CSS).click();
		BrowserWaits.waitTime(5);
	}

	/**
	 * Method to validate a specified link added in the comment.
	 * 
	 * @param url
	 * @throws Exception
	 */
	public boolean validateCommentForExternalLink(String url) throws Exception {
		BrowserWaits.waitTime(6);
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_RECORD_COMMENTS_DIV_CSS.toString()),
				90);
		String link = ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_RECORD_COMMENTS_DIV_CSS.toString()))
				.getText();
		if (link.equalsIgnoreCase(url))
			return true;
		else
			return false;
	}

	/**
	 * Method to click on specified link added in the comment.
	 * 
	 * @param url
	 * @throws Exception
	 */
	public void clickExternalLinkInComments(String url) throws Exception {
		BrowserWaits.waitTime(6);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_RECORD_COMMENTS_DIV_CSS.toString()),
				30);
		String link = ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_RECORD_COMMENTS_DIV_CSS.toString()))
				.getText();
		if (link.equalsIgnoreCase(url))
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_RECORD_COMMENTS_DIV_CSS.toString())));
		waitForPageLoad(ob);
	}

	/**
	 * Method loads up to 40 comments in a record view page.
	 */
	public void loadComments() {
		boolean isPresent = true;
		WebElement more;
		int count = 0;
		while (isPresent && count < 4) {
			try {
				waitForElementTobeVisible(ob,
						By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_SHOW_MORE_LINK_CSS.toString()), 40);

				more = ob.findElement(
						By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_SHOW_MORE_LINK_CSS.toString()));
				Point point = more.getLocation();
				int y = point.getY() + 100;
				String script = "scroll(0," + y + ");";
				((JavascriptExecutor) ob).executeScript(script);
				jsClick(ob, more);
				waitForAjax(ob);
				count++;
			} catch (Exception e) {
				e.printStackTrace();
				isPresent = false;
			}
		}
	}

	/**
	 * Method to click FlAG button on the comments in record view page
	 * 
	 * @param currentuser
	 */
	public int clickOnFlagOfOtherUserComments(String currentuser) {
		waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()),
				80);
		List<WebElement> commentsList = ob
				.findElements(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_XPATH.toString()));
		String commentText;
		int commentsCount = 0;
		WebElement flagWe;
		for (int i = 0; i < commentsList.size(); i++) {
			commentText = commentsList.get(i).getText();
			if (!commentText.contains(currentuser)) {
				try {
					flagWe = commentsList.get(i).findElement(
							By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString()));
					if (flagWe.getAttribute("class").contains("fa-flag-o")) {
						jsClick(ob, commentsList.get(i).findElement(
								By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_DYNAMIC_FLAG_XPATH.toString())));
						commentsCount = i;
						break;
					}
				} catch (Exception e) {
					continue;
				}
			}
		}
		return commentsCount;
	}

	public boolean isFlagButtonDispalyedForOthersPost() {
		boolean result = false;
		try {

			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString()),
					60);
			if (ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_VIEW_POST_FLAG_BUTTON_CSS.toString())).isDisplayed())
				result = true;
		} catch (Exception e) {
			return false;
		}

		return result;
	}

	public boolean validateCommentForLink(String url) throws InterruptedException {
		BrowserWaits.waitTime(10);

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()),
				40);
		WebElement commentArea = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()));

		if (commentArea.findElements(By.linkText(url)).size() != 0)
			return true;
		else
			return false;

	}

	public boolean validateFormatOptions(ExtentTest test) {
		boolean result = true;
		try {

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_BOLD_ICON_CSS.toString()), 40);
			if (!ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_BOLD_ICON_CSS.toString()))
					.isEnabled()) {
				test.log(LogStatus.FAIL, "Bold format option is not enable for comments");
				result = false;
			}
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_ITALIC_ICON_CSS.toString()), 40);
			if (!ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_ITALIC_ICON_CSS.toString()))
					.isEnabled()) {
				test.log(LogStatus.FAIL, "Italics format option is not enable for comments");
				result = false;
			}

			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_COMMENTS_INSERT_LINK_CSS.toString()), 40);
			if (!ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_COMMENTS_INSERT_LINK_CSS.toString()))
					.isEnabled()) {
				test.log(LogStatus.FAIL, "Insert link format option is not enable for comments");
				result = false;
			}

		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public void selectReasonInFlagModal() {

		waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_XPATH.toString()), 180);
		jsClick(ob, ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CHECKBOX_CSS.toString())));
	}

	public void clickCancelButtonInFlagModal() {

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CANCEL_BUTTON_CSS.toString()), 180);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_CANCEL_BUTTON_CSS.toString())));
	}

	public void clickFlagButtonInFlagModal() throws InterruptedException {
		BrowserWaits.waitTime(4);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_FLAG_BUTTON_CSS.toString()), 180);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_FLAG_BUTTON_CSS.toString())));
	}

	public void createComment(String comment) throws InterruptedException {
		// Commented by KR
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()),
		// 40);
		// jsClick(ob,
		// ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString())));
		// ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()))
		// .sendKeys(comment);
		// jsClick(ob, ob.findElement(
		// By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_ADD_COMMENT_BUTTON_CSS.toString())));
		BrowserWaits.waitTime(5);
		fluentwaitforElement(ob, By.xpath("//textarea[@placeholder='Join the discussion']"), 100);
		WebElement commentArea = ob.findElement(By.xpath("//textarea[@placeholder='Join the discussion']"));
		commentArea.click();
		WebElement innerTextBox = ob.findElement(By.xpath("//div[@class='fr-element fr-view']"));
		innerTextBox.clear();
		for (int i = 0; i < comment.length(); i++) {
			innerTextBox.sendKeys(comment.charAt(i) + "");
			Thread.sleep(100);
		}
		BrowserWaits.waitTime(5);
		pf.getPostCommentPageInstance(ob).clickAddCommentButton();
		Thread.sleep(100);// after entering the comments wait for submit button
							// to get enabled or disabled
	}

	public String getRecordType() {
		waitForElementTobeVisible(ob,
				By.cssSelector("div[class='ne-publication__header'] h3[class*='wui-super-header']"), 80);
		return ob.findElement(By.cssSelector("div[class='ne-publication__header'] h3[class*='wui-super-header']"))
				.getText();
	}

	public String clickSendToEndnoteRecordViewPage() throws InterruptedException {
		WebElement button = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_SENDTOENDNOTE_BUTTON_CSS.toString()));
		button.click();

		BrowserWaits.waitTime(5);

		List<WebElement> list = button.findElements(By.cssSelector("span"));

		for (WebElement we : list) {
			if (we.isDisplayed()) {
				return we.getText();

			}
		}
		return "";
	}

	public boolean validateProfanityWordsMaskedForPostTitle(List<String> profanityWord, ExtentTest test)
			throws Exception {
		boolean result = false;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS);

		String postTitle = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS).getText();
		for (String str : profanityWord) {

			if (postTitle.contains(str)) {
				test.log(LogStatus.INFO, "Profanity filter not working for: " + str);
				result = true;
			}

		}

		return result && postTitle.contains("**");
	}

	public boolean validateProfanityWordsMaskedForPostContent(List<String> profanityWord, ExtentTest test)
			throws Exception {
		boolean result = false;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS);

		String postcontent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_CONTENT_CSS).getText();

		for (String str : profanityWord) {

			if (postcontent.contains(str)) {
				test.log(LogStatus.INFO, "Profanity filter not working for: " + str);
				result = true;
			}

		}

		return result && postcontent.contains("**");
	}

	public void validateDocumentInRecordViewPage(String documentTitle) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS);
		String docTitle = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS).getText();
		pf.getBrowserWaitsInstance(ob).waitUntilText(docTitle);
		Assert.assertEquals(documentTitle, docTitle);
	}

	public String getPatentRecordDetails(String title) throws Exception {
		String details = null;
		StringBuilder strBldr = new StringBuilder();
		pf.getHFPageInstance(ob).searchForText(title);
		BrowserWaits.waitTime(10);
		waitForAjax(ob);
		pf.getSearchResultsPageInstance(ob).clickOnPatentsTab();
		waitForAjax(ob);
		List<WebElement> list = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));
		WebElement titleWe;
		for (WebElement we : list) {
			titleWe = we.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_CSS.toString()));
			if (titleWe.getText().equals(title)) {
				details = we
						.findElement(By.cssSelector(
								OnePObjectMap.HOME_PROJECT_NEON_SEARCH_RESULT_PATENT_SOURCE_CSS.toString()))
						.getText().trim();

				strBldr.append(details);
				details = we
						.findElements(By.cssSelector(
								OnePObjectMap.HOME_PROJECT_NEON_SEARCH_RESULT_PATENT_PUBLICATION_CSS.toString()))
						.get(0).getText().trim();

				strBldr.append(details);
				strBldr.append(" ");
				details = we
						.findElements(By.cssSelector(
								OnePObjectMap.HOME_PROJECT_NEON_SEARCH_RESULT_PATENT_PUBLICATION_CSS.toString()))
						.get(1).getText().trim();
				strBldr.append("PATENT#: ");
				strBldr.append(details);
				titleWe.click();
				break;

			}
		}
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_ABSTRACT_CSS.toString()), 60);
		for (WebElement we : ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_ABSTRACT_CSS.toString()))) {
			if (we.isDisplayed()) {
				details = we.getText().trim();
				strBldr.append(details);
				break;
			}
		}
		return strBldr.toString();
	}

	public String getArticleDetails(String title) throws Exception {
		String details = null;
		StringBuilder strBldr = new StringBuilder();
		pf.getHFPageInstance(ob).searchForText(title);
		BrowserWaits.waitTime(10);
		waitForAjax(ob);
		pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
		waitForAjax(ob);
		List<WebElement> list = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));
		WebElement titleWe;
		for (WebElement we : list) {
			titleWe = we.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_CSS.toString()));
			if (titleWe.getText().equals(title)) {
				details = we
						.findElement(
								By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_RESULT_SOURCE_CSS.toString()))
						.getText().trim();

				strBldr.append(details);
				details = we
						.findElement(By
								.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_RESULT_PUBLICATION_CSS.toString()))
						.getText().trim();
				strBldr.append(details);

				titleWe.click();
				break;

			}
		}
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_ABSTRACT_CSS.toString()), 60);
		for (WebElement we : ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_ABSTRACT_CSS.toString()))) {
			if (we.isDisplayed()) {
				details = we.getText().trim();
				strBldr.append(details);
				break;
			}
		}
		return strBldr.toString();
	}

	public List<String> getRecordMetrics() throws Exception {
		List<WebElement> list = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_METRICS_CSS.toString()));
		List<String> metricList = new ArrayList<String>();
		for (WebElement we : list) {
			if (!we.getText().contains("Views") && !we.getText().contains("Comments")
					&& !we.getText().contains("Cited References") && !we.getText().contains("Cited Articles"))
				metricList.add(we.getText());
		}
		return metricList;
	}

}
