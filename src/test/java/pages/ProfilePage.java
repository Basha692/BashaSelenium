package pages;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class ProfilePage extends TestBase {

	public ProfilePage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Search results people count
	 */
	static int peopleCount = 0;
	static String PARENT_WINDOW_HANDLE = null;
	String profileTitle;
	String profileMetadata;
	static String followUnfollowLableBefore;
	static String followUnfollowLableAfter;
	static String metadata[];
	static int followingBefore;
	static int followersBefore;
	static String watchTextBefore;
	static List<WebElement> topicTypeahead;
	static List<WebElement> profileTabsRecords;
	static boolean profileIncomplete;

	/**
	 * Method for Validate Profile Search with last name
	 * 
	 * @param lastName
	 * @throws Exception
	 */
	public void validateProfileLastName(String lastName) throws Exception {
		List<WebElement> profilesLastname = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_NAME_CSS);
		if (profilesLastname.size() > 0) {
			for (WebElement profileLastname : profilesLastname) {
				if (!StringUtils.containsIgnoreCase(profileLastname.getText(), lastName)) {
					throw new Exception("Profile serach not verifying with Last Name");
				}
			}
		} else
			logger.info("Profile Search Results are not available with \t" + lastName + "\t last Name");

	}

	/**
	 * Method for Validate Profile Search with Role/Primary Institution/Country
	 * 
	 * @param metaData
	 * @throws Exception
	 */
	public void validateProfileMetaData(String metaData) throws Exception {
		List<WebElement> profilesLastname = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_NAME_CSS);
		if (profilesLastname.size() > 0) {
			List<WebElement> profilesMetaData = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_PROFILE_METADATA_TAG);
			logger.info("Profile metadata--->" + profilesMetaData.size());
			for (WebElement profileMetaData : profilesMetaData) {
				logger.info("Meta Data-->" + profileMetaData.getText());
				if (!StringUtils.containsIgnoreCase(profileMetaData.getText(), metaData)) {
					throw new Exception("Profile search not verifying with Role/Primary Institution/Country	");
				}
			}
		} else
			logger.info("No Profile Search Results are not available with \t" + metaData
					+ "\t role/Primary Institution/Country");
	}

	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception,
	 *             When People are not present/Disabled
	 */
	public void clickPeople() throws Exception {
		pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PEOPLE_CSS).get(3).click();
		BrowserWaits.waitTime(8);
	}

	/**
	 * Method for Validate Profile Search with Interests and Skills
	 * 
	 * @param lastName
	 * @throws Exception
	 */
	public void validateProfileInterest(String interestAndSkill) throws Exception {
		List<WebElement> profilesname = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_NAME_CSS);
		if (profilesname.size() > 0) {
			for (WebElement profilename : profilesname) {
				profilename.findElement(By.tagName("a")).click();
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_INTEREST_AND_SKILLS_CSS);
				Thread.sleep(6000);
				break;
			}

			List<WebElement> interestsSkills = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_INTEREST_AND_SKILLS_CSS);
			List<String> interests = new ArrayList<String>();
			for (WebElement intSkill : interestsSkills) {
				interests.add(intSkill.getText());
			}

			logger.info("interests and skills-->" + interests);

			if (!interests.contains(interestAndSkill)) {
				throw new Exception("Profile Search not happening with Interests and Skill " + interestAndSkill);
			}
		} else
			logger.info(
					"Profile Search Results are not available with \t" + interestAndSkill + "\t Interests and Skills");

	}

	/**
	 * Method for Validate Apps links for redirecting different pages
	 * 
	 * @param appLinks
	 * @throws Exception,
	 *             When App links not present
	 */
	public void validateAppsLinks(String appLinks) throws Exception {
		String[] totalAppLinks = appLinks.split("\\|");
		for (int i = 0; i < totalAppLinks.length; i++) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_ONEP_APPS_CSS);
			BrowserWaits.waitTime(4);
			PARENT_WINDOW_HANDLE = ob.getWindowHandle();
			List<WebElement> apps = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_APP_SWITCHER_LINKS_CSS);
			for (WebElement app : apps) {
				logger.info("apps name-->" + app.getText());
				if (app.getText().contains(totalAppLinks[i])) {
					app.findElement(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_APP_SWITCHER_BAU_LINK_GO_CSS.toString()))
							.click();
					break;
				}
			}
			ob.manage().window().maximize();
			waitForNumberOfWindowsToEqual(ob, 2);
			Set<String> child_window_handles = ob.getWindowHandles();
			logger.info("child windows count-->" + child_window_handles.size());
			for (String child_window_handle : child_window_handles) {
				if (!child_window_handle.equals(PARENT_WINDOW_HANDLE)) {
					ob.switchTo().window(child_window_handle);
					logger.info("page info" + ob.getTitle());
					if (!StringUtils.containsIgnoreCase(ob.getTitle(), totalAppLinks[i])) {
						throw new Exception(totalAppLinks[i] + "  page is not opened");
					}
					ob.close();
					ob.switchTo().window(PARENT_WINDOW_HANDLE);
				} // if
			} // for
		} // for
	}

	/**
	 * Method for click on profile
	 * 
	 * @throws Exception,
	 *             When profile link not present
	 */
	public void clickProfileLink() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Interests and Skills", "Posts", "Comments", "Followers",
				"Following");
	}

	/**
	 * Method for get profile title
	 * 
	 * @throws Exception
	 */
	public void getProfileTitle() throws Exception {
		profileTitle = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TITLE_CSS).get(1).getText();
		logger.info("profile title-->" + profileTitle);
	}

	/**
	 * Method for get Profile Meta Data
	 * 
	 * @throws Exception
	 */
	public void getProfileMetadata() throws Exception {
		profileMetadata = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_METADATA_CSS).get(1).getText();
		logger.info("profile metadata-->" + profileMetadata);
	}

	/**
	 * Method for click on first profile of search people page
	 * 
	 * @throws Exception
	 */
	public void clickProfile() throws Exception {
		getProfileTitle();
		getProfileMetadata();
		// pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TITLE_CSS.toString());
		pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TITLE_CSS).get(1)
				.click();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS);
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS.toString()),
		// 120);
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ROLE_METADATA_CSS.toString()),
		// 90);
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIMARYINSTITUTION_METADATA_CSS.toString()),
		// 90);
	}

	/**
	 * Method for Validate Profile Title and Profile Metadata
	 * 
	 * @throws Exception,
	 *             When Profile title or metadata mismatches
	 */
	public void validateProfileTitleAndMetadata() throws Exception {
		String title = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS)
				.getText();
		Assert.assertEquals(profileTitle, title);
		String role = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ROLE_METADATA_CSS).getText();
		String priInstitution = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIMARYINSTITUTION_METADATA_CSS).getText();
		String location = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_LOCATION_METADATA_CSS).getText();

		if (!(profileMetadata.contains(role) && profileMetadata.contains(priInstitution)
				&& profileMetadata.contains(location))) {
			throw new Exception("Profile Metadata not matching");
		}

	}

	/**
	 * Method for Validate user should not edit other profiles
	 * 
	 * @throws Exception,
	 *             When Other profiles having edit option
	 */
	public void validateOtherProfileEdit() throws Exception {
		boolean otherProfileEdit = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS).isDisplayed();
		logger.info("profile edit-->" + otherProfileEdit);
		if (otherProfileEdit) {
			throw new Exception("Edit option should not available for others profile");
		}

	}

	/**
	 * Method for follow/unfollow other profile from their profile page
	 * 
	 * @throws Exception,
	 *             When user not able to follow
	 */
	public void followOtherProfileFromProfilePage() throws Exception {
		BrowserWaits.waitTime(10);
		waitForElementTobePresent(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS.toString()), 200);
		WebElement followUnFollowCheck = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS);
		followUnfollowLableBefore = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1)
				.getAttribute("data-uib-tooltip");
		logger.info("Follow/Unfollow Label Before-->" + followUnfollowLableBefore);
		// followUnFollowCheck.click();
		pf.getBrowserActionInstance(ob).jsClick(followUnFollowCheck);
		BrowserWaits.waitTime(2);
		followUnfollowLableAfter = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1)
				.getAttribute("data-uib-tooltip");
		logger.info("Follow/Unfollow Label After-->" + followUnfollowLableAfter);

		if (followUnfollowLableBefore.equalsIgnoreCase(followUnfollowLableAfter)) {
			throw new Exception("unable to follow other profile from search screen");
		}
	}

	/**
	 * Method for follow other profile from their profile page
	 * 
	 * @throws Exception,
	 *             When user not able to follow
	 */
	public void followOtherUserFromProfilePage() throws Exception {
//		BrowserWaits.waitTime(2);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS.toString()), 100);
		WebElement followUnFollowCheck = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS);
		followUnfollowLableBefore = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1)
				.getAttribute("data-uib-tooltip");
		logger.info("Follow/Unfollow Label Before-->" + followUnfollowLableBefore);
		if (followUnfollowLableBefore.contains("Follow")) {
			pf.getBrowserActionInstance(ob).jsClick(followUnFollowCheck);
//			BrowserWaits.waitTime(2);
			fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS.toString()), 100);
			followUnfollowLableAfter = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1)
					.getAttribute("data-uib-tooltip");
			logger.info("Follow/Unfollow Label After-->" + followUnfollowLableAfter);

			if (followUnfollowLableBefore.equalsIgnoreCase(followUnfollowLableAfter)) {
				throw new Exception("unable to follow other profile from search screen");
			}
		} else {
			logger.info("user is already followed");
		}

	}

	/**
	 * Method for Validate user should have edit option to edit profile and
	 * profile name should match with profile image title
	 * 
	 * @throws Exception,
	 *             When Other profiles having edit option
	 */
	public void validateOwnrProfile() throws Exception {
		boolean otherProfileEdit = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS).isDisplayed();

		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();

		if (!(otherProfileEdit || profileIncomplete)) {
			throw new Exception("Edit option should be available for own profile");
		}

		if (profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
		}

		String profileName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText();
		logger.info("profile name-->" + profileName);
		String profileImageText = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS).findElement(By.tagName("img"))
				.getAttribute("title");
		logger.info("profile image text-->" + profileImageText);

		Assert.assertEquals(profileName, profileImageText);

	}

	/**
	 * Method for Validate Edit Cancel Button
	 * 
	 * @throws Exception,
	 *             When user not able to click cancel
	 */
	public void clickEditCancel() throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
		}
	}

	/**
	 * Method for Validate user should have edit option to edit profile and
	 * profile name should match with profile image title
	 * 
	 * @throws Exception,
	 *             When Other profiles having edit option
	 */
	public void editUserOwnProfileMetadata(String profileMetadata) throws Exception {

		metadata = profileMetadata.split("\\|");
		boolean otherProfileEdit = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS).isDisplayed();
		logger.info("profile edit-->" + otherProfileEdit);
		if (!(otherProfileEdit || profileIncomplete)) {
			throw new Exception("Edit option should be available for own profile");
		}

		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS,
				metadata[0]);

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS,
				metadata[1]);

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS,
				metadata[2]);

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS,
				metadata[3]);

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS,
				metadata[4]);
	}

	/**
	 * Method for Validate Edit update Button
	 * 
	 * @throws Exception,
	 *             When user not able to click update button
	 */
	public void clickEditUpdate() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS.toString()),
				90);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_LOCATION_METADATA_CSS.toString()), 90);
	}

	/**
	 * Method for Validate profile metadata
	 * 
	 * @throws Exception,
	 *             When data not matching
	 */
	public void validateProfileMetadata() throws Exception {
		String country = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_LOCATION_METADATA_CSS).get(0).getText();
		logger.info("country-->" + country);
		if (country.contains(metadata[3])) {
			throw new Exception("profile meta data not updated");
		}
	}

	/**
	 * Method for Click profile comment tab
	 * 
	 * @throws Exception,
	 *             comment tab is not click able
	 */
	public void clickCommentsTab() throws Exception {
		BrowserWaits.waitTime(10);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENTS_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENTS_CSS);
		waitForAjax(ob);
	}

	/**
	 * Method for Click profile Following tab
	 * 
	 * @throws Exception,
	 *             Following tab is not click able
	 */
	public void clickFollowingTab() throws Exception {
		BrowserWaits.waitTime(15);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWING_CSS);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWING_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWING_CSS);
		waitForAjax(ob);
	}

	/**
	 * Method for Click profile Followers tab
	 * 
	 * @throws Exception,
	 *             Followers tab is not click able
	 */
	public void clickFollowersTab() throws Exception {
		BrowserWaits.waitTime(10);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWERS_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWERS_CSS);
		waitForAjax(ob);
	}

	/**
	 * Method for Click Posts Following tab
	 * 
	 * @throws Exception,
	 *             Posts tab is not click able
	 */
	public void clickPostsTab() throws Exception {
		BrowserWaits.waitTime(10);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_POSTS_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_POSTS_CSS);
		waitForAjax(ob);
	}

	/**
	 * Method for Click tab
	 * 
	 * @throws Exception,
	 *             Watchlists tab is not click able
	 */
	public void clickWatchlistTab() throws Exception {
		BrowserWaits.waitTime(10);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS);
		waitForAjax(ob);
	}

	/**
	 * Method for validate Like own profile comment
	 * 
	 * @throws Exception,
	 *             comment like not done
	 */
	public void commentAppreciation() throws Exception {
		BrowserWaits.waitTime(15);
		waitForElementTobeClickable(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS.toString()), 120);
		String tooltipBeforeAppreciate = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS)
				.getAttribute("data-uib-tooltip");
		String countBeforeAppreciate = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS).getText();
		logger.info("Appreciate tooltip-->" + tooltipBeforeAppreciate);
		logger.info("Appreciate count-->" + countBeforeAppreciate);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS);
		BrowserWaits.waitTime(4);
		String tooltipAfterAppreciate = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS)
				.getAttribute("data-uib-tooltip");
		String countAfterAppreciate = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_COMMENT_APPRECIATE_CSS).getText();
		logger.info("Appreciate tooltip after-->" + tooltipAfterAppreciate);
		logger.info("Appreciate count after-->" + countAfterAppreciate);
		if (tooltipBeforeAppreciate.equalsIgnoreCase(tooltipAfterAppreciate)
				&& countBeforeAppreciate.equalsIgnoreCase(countAfterAppreciate)) {
			throw new Exception("comment appreciation not happend");
		}
	}

	/**
	 * Method for Click profile Following tab
	 * 
	 * @throws Exception,
	 *             Following tab is not click able
	 */
	public int getFollowingCount() throws Exception {
		String followingCountBefore = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWING_CSS).getText();
		String followingCount[] = followingCountBefore.split(" ");
		followingBefore = Integer
				.parseInt(followingCount[1].contains(",") ? followingCount[1].replace(",", "") : followingCount[1]);
		return followingBefore;
	}

	/**
	 * Method for Click profile Following tab
	 * 
	 * @throws Exception,
	 *             Following tab is not click able
	 */
	public int getWatchlistsCount() throws Exception {
		String watchlistsCount = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS).getText();
		String followingCount[] = watchlistsCount.split(" ");
		followingBefore = Integer.parseInt(followingCount[1]);
		return followingBefore;
	}

	/**
	 * Method for Validate Following count
	 * 
	 * @throws Exception,
	 *             validation fails
	 */
	public void validateFollowingCount() throws Exception {
		int followingAfter = getFollowingCount();
		if ((followingBefore > followingAfter) || (followingBefore < followingAfter)) {
			throw new Exception("Following count should increase or decrease");
		}
	}

	/**
	 * Method to click on Publish A Post button in the profile page
	 * 
	 * @throws InterruptedException
	 */
	public void clickOnPublishPostButton() throws InterruptedException {
		waitForPageLoad(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PUBLISH_A_POST_BUTTON_CSS.toString()), 40);
		BrowserWaits.waitTime(4);
		jsClick(ob, ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PUBLISH_A_POST_BUTTON_CSS.toString())));

	}

	/**
	 * Method to click on add video to a post in profile page
	 * 
	 * @throws InterruptedException
	 */
	public void AddVideoAndPublishAPost(String url) throws InterruptedException {
		Actions act = new Actions(ob);
		act.moveToElement(ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ADD_VIDEO_BUTTON_XPATH.toString())))
				.click().perform();
		act.moveToElement(ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ADD_URL_BUTTON_XPATH.toString())))
				.click().perform();
		act.moveToElement(ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ADD_URL_BUTTON_XPATH.toString())))
				.sendKeys(url);
		act.moveToElement(ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_INSERT_URL_XPATH.toString()))).click()
				.perform();
		waitForPageLoad(ob);
	}

	/**
	 * Method to click on add Image button and add an image to a post
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void AddImageToPost() throws InterruptedException, IOException {
		ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ADD_IMAGE_BTN_XPATH.toString())).click();
		BrowserWaits.waitTime(1);
		ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_IMAGE_SELECTOR_BTN_XPATH.toString())).click();
		String imgPath = System.getProperty("user.dir") + "\\images\\" + "myimage2" + ".jpg";
		Runtime.getRuntime().exec("autoit_scripts/imageUpload2.exe" + " " + imgPath);
		BrowserWaits.waitTime(10);

	}

	/**
	 * Method to validate various error messages while creating the post
	 * 
	 * @param expErrorMsg
	 * @return
	 * @throws Exception
	 */
	public boolean validatePostErrorMessage(String expErrorMsg) throws Exception {
		boolean result = false;
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
		// OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_ERROR_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_ERROR_CSS,60);
		String actErrorMessage = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_ERROR_CSS.toString()))
				.getText();
		if (expErrorMsg.equalsIgnoreCase(actErrorMessage)) {
			result = true;
		}
		return result;
	}

	/**
	 * Method to enter the specified text to post title box in post creation
	 * modal
	 * 
	 * @param tilte
	 * @throws InterruptedException
	 */
	public void enterPostTitle(String tilte) throws Exception {
		BrowserWaits.waitTime(5);
		waitForElementTobeClickable(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_TITLE_CSS.toString()), 90);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_TITLE_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_TITLE_CSS.toString()))
				.clear();
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_TITLE_CSS.toString()))
				.sendKeys(tilte);
	}

	/**
	 * Method to enter the specified text to post content box in post creation
	 * modal
	 * 
	 * @param tilte
	 * @throws Exception
	 */
	public void enterPostContent(String content) throws Exception {
		WebElement wb = ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()));
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()), 90);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
		// OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS);
		wb.clear();
		for (int i = 0; i < content.length(); i++) {
			wb.sendKeys(content.charAt(i) + "");
			// Thread.sleep(1);
		}
		// ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()))
		// .sendKeys(content);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()))
				.sendKeys(Keys.ENTER);

	}

	/**
	 * Method to click on publish button in post creation modal
	 * 
	 * @throws Exception
	 */
	public void clickOnPostPublishButton() throws Exception {
		BrowserWaits.waitTime(5);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_PUBLISH_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_PUBLISH_CSS);
		BrowserWaits.waitTime(5);
	}

	/**
	 * Method to click on cancel button in post creation modal
	 * @throws Exception 
	 */
	public void clickOnPostCancelButton() throws Exception {
		BrowserWaits.waitTime(6);
		// Commented by KR
		// waitForElementTobeVisible(ob,
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS.toString()),
		// 120);
		pf.getBrowserWaitsInstance(ob)
		.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS.toString())));
//		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS.toString()))
//				.click();
	}

	/**
	 * Method to get the count of posts in a profile
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int getPostsCount() throws Exception {
		BrowserWaits.waitTime(5);
		waitForAjax(ob);
		// commented by KR
		//pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_COUNT_CSS);
		// BrowserWaits.waitTime(5);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
		// OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_COUNT_CSS);

		// int count = Integer.parseInt(ob.findElement(
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_COUNT_CSS.toString())).getText());
		BrowserWaits.waitTime(5);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_COUNT_CSS);
		//scrollingToElementofAPage();
		String commentsCount = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_COUNT_CSS.toString()))
				.getText();
		if (commentsCount.contains(",")) {
			count = Integer.parseInt(commentsCount.replace(",", ""));
		} else {
			count = Integer.parseInt(commentsCount);
		}
		return count;
	}

	public int getDraftPostsCount() throws Exception {
		waitForPageLoad(ob);
		waitForAjax(ob);
		BrowserWaits.waitTime(15);
		String strCount;
		try {
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()), 30);
			strCount = ob
					.findElement(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()))
					.getText().replaceAll(",", "");
		} catch (Exception e) {
			strCount = "0";
		}

		int count = Integer.parseInt(strCount);
		return count;
	}

	/**
	 * Method to get the count of Comments count in a profile
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int getCommentsCount() throws Exception {
		// BrowserWaits.getBrowserWaitsInstance(ob).waitTime(10);
		waitForAjax(ob);
		int count = 0;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_COMMENTS_COUNT_CSS);
		String commentsCount = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_COMMENTS_COUNT_CSS).getText();
		if (commentsCount.contains(",")) {
			count = Integer.parseInt(commentsCount.replace(",", ""));
		} else {
			count = Integer.parseInt(commentsCount);
		}
		return count;
	}

	/**
	 * Method to get the title of the most recent post in the profile.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public String getFirstPostTitle() throws Exception {
		BrowserWaits.waitTime(8);
		waitForAjax(ob);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString()), 20);

		String postTitle = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString())).get(0)
				.getText();
		return postTitle;
	}

	/**
	 * Method for Click profile Followers tab
	 * 
	 * @throws Exception,
	 *             Following tab is not click able
	 */
	public int getFollowersCount() throws Exception {
		String followerCountBefore = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_FOLLOWERS_CSS).getText();
		String followerCount[] = followerCountBefore.split(" ");
		followersBefore = Integer.parseInt(followerCount[1]);
		logger.info("FOLLOWERS BEFORE-->" + followersBefore);
		return followersBefore;
	}

	/**
	 * Method for Validate Followers count
	 * 
	 * @throws Exception,
	 *             validation fails
	 */
	public void validateFollowersCount(int beforeFollowCount) throws Exception {
		logger.info("before count-->" + beforeFollowCount);

		int followerAfter = getFollowersCount();
		logger.info("after count-->" + followerAfter);
		if ((beforeFollowCount == followerAfter)) {
			throw new Exception("Followers count should increase or decrease while other follow/unfollow");
		}
	}

	/**
	 * Method for Add Topics into Interest and Skills
	 * 
	 * @throws Exception,
	 *             unable to add topic
	 */
	public void addTopicForInterestAndSkills(String topics) throws Exception {
		List<WebElement> addedTopics = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
		if (addedTopics.size() > 0) {
			for (WebElement addedTopic : addedTopics) {
				addedTopic.click();
				BrowserWaits.waitTime(2);
			}
		}
		String topicLists[] = topics.split("\\|");
		for (String topicList : topicLists) {
			for (int i = 0; i < topicList.length(); i++) {
				char topic = topicList.charAt(i);
				pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS,
						String.valueOf(topic));
				BrowserWaits.waitTime(2);
			}
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
			List<WebElement> topicTypeahead = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
			BrowserWaits.waitTime(2);
			pf.getBrowserActionInstance(ob)
					.jsClick(topicTypeahead.get(Integer.parseInt(RandomStringUtils.randomNumeric(1))));
			// topicTypeahead.get(Integer.parseInt(RandomStringUtils.randomNumeric(1))).click();
			BrowserWaits.waitTime(2);
		}

		List<WebElement> newlyAddedTopics = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
		if (!(newlyAddedTopics.size() == topicLists.length)) {
			throw new Exception("Topics not added for Interests and Skills");
		}
	}

	public int getLengthOfTitleFromPostCreationModal() {

		int count = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_TITLE_CSS.toString()))
				.getAttribute("value").length();
		return count;
	}

	public void clickOnFirstPost() throws Exception {
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS);
		jsClick(ob, ob.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString()))
				.get(0));
	}

	/**
	 * Method to click on publish button in post creation modal
	 * 
	 * @throws Exception
	 */
	public void clickPublishAPost() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PUBLISH_A_POST_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PUBLISH_A_POST_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Publish a Post");
		pf.getBrowserWaitsInstance(ob)
				.waitUntilText("We only support video links from YouTube and Vimeo at this time.");
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS);
	}

	/**
	 * Method to click on publish button cancel button
	 * 
	 * @throws Exception
	 */
	public void clickPublishAPostCancel() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_CSS);
		pf.getBrowserActionInstance(ob).click((OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PUBLISH_A_POST_DISCARD_CSS));
		pf.getBrowserWaitsInstance(ob).waitUntilText("Interests and Skills", "Posts", "Comments", "Followers",
				"Following");
	}

	/**
	 * Method to validate Post Title
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validatePostTitle(String postTitle) throws Exception {
		String enteredPost = getFirstPostTitle();
		if (!(enteredPost.equalsIgnoreCase(postTitle))) {
			throw new Exception("Post is not published");
		}

	}

	/**
	 * Method to validate Published post counts
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validatePostCount(int postCount) throws Exception {
		int totPosts = getPostsCount();
		if (totPosts == postCount) {
			throw new Exception("Post count not getting updated");
		}
	}

	/**
	 * Method for get Profile information
	 * 
	 * @throws Exception,
	 *             When unable to get info
	 */
	public List<String> getProfileTitleAndMetadata() throws Exception {
		profileIncomplete = isProfileIncomplete();
		logger.info("is profile in incomple mode-->" + profileIncomplete);
		if (profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			BrowserWaits.waitTime(2);
		}

		List<String> profileInfo = new ArrayList<String>();
		try {
			profileInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText());
		} catch (Exception e) {// do nothing

		}
		try {
			profileInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ROLE_METADATA_CSS).getText());
		} catch (Exception e1) {// do nothing

		}

		try {
			profileInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIMARYINSTITUTION_METADATA_CSS).getText());
		} catch (Exception e2) {// do nothing

		}
		try {
			profileInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_LOCATION_METADATA_CSS).getText());
		} catch (Exception e3) {// do nothing

		}

		return profileInfo;
	}

	/**
	 * Method to get the title of the most recent post in the profile.
	 * 
	 * @return
	 */
	public void clickFirstPostTitle() throws Exception {
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS);
		// pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS).click();
		BrowserWaits.waitTime(8); // Added wait due to toast notifications
									// occured in sauce labs
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_POST_TITLE_CSS);
	} 

	/**
	 * Method for validate profile posts, posts are more than 10, by default 10
	 * posts should display
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validateProfilePostTab() throws Exception {
		int totPosts = getPostsCount();
		if (totPosts >= 10) {
			List<WebElement> postsTimeStamp = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TIMESTAMP_XPATH);
			List<WebElement> postLike = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_DETAILS_LIKE_XPATH);
			/*List<WebElement> postWatch = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_DETAILS_WATCH_CSS);*/
			if (!(postsTimeStamp.size() == 10 && postLike.size() == 10 )) {
				throw new Exception("Post's count by default should be 10 if Post tab having more than 10 posts");
			}

		}
	}

	public void addPostToWatchlist() throws Exception {
		watchTextBefore = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_DETAILS_WATCH_CSS)
				.findElement(By.tagName("span")).getText();
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_DETAILS_WATCH_CSS);
		waitForAjax(ob);
		// BrowserWaits.getBrowserWaitsInstance(ob).waitTime(2);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_WATCHLIST_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_POST_WATCH_CLOSE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_WATCHLIST_CSS);
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS);
		BrowserWaits.waitTime(2);
	}

	public void postWatchLabelValidation() throws Exception {
		String watchTextAfter = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_DETAILS_WATCH_CSS)
				.findElement(By.tagName("span")).getText();
		logger.info("watch text before-->" + watchTextBefore);
		logger.info("watch text after-->" + watchTextAfter);
		if (watchTextBefore.equalsIgnoreCase(watchTextAfter)) {
			throw new Exception("post watch label not getting changed");
		}
	}

	public void addExternalLinkToPostContent(String url) throws Exception {

		BrowserWaits.waitTime(5);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
				OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_BUTTON_CSS);
		pf.getBrowserActionInstance(ob)
				.click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_BUTTON_CSS);
		BrowserWaits.waitTime(5);
		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_URL_TEXT_BOX_CSS)
				.sendKeys(url);

		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_INSERT_LINK_URL_INSERT_CSS).click();
		;
		BrowserWaits.waitTime(5);
	}

	public boolean validateProfileDetails(List<String> details) throws Exception {
		// BrowserWaits.getBrowserWaitsInstance(ob).waitTime(6);
		waitForPageLoad(ob);
		waitForAjax(ob);
		List<String> expected = getProfileTitleAndMetadata();
		while (true) {

			if (!expected.contains("")) {
				break;
			}
			expected.remove("");
		}
		while (true) {

			if (!details.contains("")) {
				break;
			}
			details.remove("");
		}
		return (expected.toString().equals(details.toString()));
	}

	/*
	 * Method to click on cancel button in post creation modal
	 */
	public void clickOnPostCancelDiscardButton() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_DISCARD_XPATH);
		ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_DISCARD_XPATH.toString()))
				.click();
	}

	public void clickOnPostCancelKeepDraftButton() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
				OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_KEEP_DRAFT_XPATH);
		jsClick(ob, ob.findElement(
				By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_KEEP_DRAFT_XPATH.toString())));
		BrowserWaits.waitTime(5);
	}

	public void clickOnDraftPostsTab() throws InterruptedException {
		BrowserWaits.waitTime(10);
		waitForPageLoad(ob);
		BrowserWaits.waitTime(5);
		// waitForElementTobeClickable(ob,
		// By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()),
		// 40);
		jsClick(ob, ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString())));
		waitForAjax(ob);
	}

	public boolean validatePublishButton() throws Exception {
		BrowserWaits.waitTime(5);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_PUBLISH_CSS);

		return ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_PUBLISH_CSS.toString()))
				.isEnabled();

	}

	/**
	 * Method for Validate profile Primary Institution typeahead options should
	 * display while enter min 2 characters
	 * 
	 * @throws Exception,
	 *             When Typeahead options not occured
	 */
	public void primaryInstitutionTypeaheadOptions(String oneChar, String twoChar) throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS).isDisplayed();
		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
		// enter single character check typeahead options should not display
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS, oneChar);
		BrowserWaits.waitTime(2);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsNotDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS);

		// enter two characters check typeahead options should display
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS, twoChar);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS);
		BrowserWaits.waitTime(4);
		List<WebElement> piTypeaheads = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).findElements(By.tagName("li"));
		if (!(piTypeaheads.size() > 0))
			throw new Exception("Primary Instituion Type ahead options are not displayed while enter two characters");

	}

	/**
	 * Method for Validate profile Primary Institution
	 * 
	 * @throws Exception,
	 *             When Typeahead options not occured
	 */
	public void selectProfilePITypeAhead(String typeAheadOption) throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS,
				typeAheadOption);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS);
		BrowserWaits.waitTime(4);
		List<WebElement> piTypeaheads = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).findElements(By.tagName("li"));
		for (WebElement typeAhead : piTypeaheads) {
			if (StringUtils.containsIgnoreCase(typeAhead.getText(), typeAheadOption.trim())) {
				typeAhead.click();
				BrowserWaits.waitTime(2);
				break;
			}
		}
		clickEditUpdate();
	}

	public boolean validateProfilePI(String typeAheadOption) throws Exception {
		// BrowserWaits.getBrowserWaitsInstance(ob).waitTime(6);
		String actualPI = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIMARYINSTITUTION_METADATA_CSS).getText();
		return (StringUtils.containsIgnoreCase(actualPI, typeAheadOption));
	}

	/**
	 * Method for Select Country from predefined type ahead list
	 * 
	 * @throws Exception,
	 *             When Type ahead options not occurred
	 */
	public void selectProfileCountryTypeAhead(String countyTypeahead, String fullCountry) throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS,
				countyTypeahead);
		BrowserWaits.waitTime(4);
		List<WebElement> countyTypeaheads = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).get(1)
				.findElements(By.tagName("li"));
		for (WebElement typeAhead : countyTypeaheads) {
			if (typeAhead.getText().equalsIgnoreCase(fullCountry)) {
				// typeAhead.click();
				jsClick(ob, typeAhead);
				BrowserWaits.waitTime(2);
				break;
			}
		}
		clickEditUpdate();
	}

	/**
	 * Method for validate selected profile typeahead country
	 * 
	 * @param country
	 * @return
	 * @throws Exception
	 */
	public boolean validateProfileCountry(String country) throws Exception {
		// BrowserWaits.getBrowserWaitsInstance(ob).waitTime(6);
		String actualCountry = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_COUNTRY_METADATA_CSS).getText();
		return (actualCountry.trim().equalsIgnoreCase(country));
	}

	/**
	 * Method for Validate profile Country typeahead options should display
	 * while enter min 2 characters
	 * 
	 * @throws Exception,
	 *             When Typeahead options not occurred
	 */
	public void countryTypeaheadOptionsMinChars(String oneChar, String twoChar) throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
		// enter single character check typeahead options should not display
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS,
				oneChar);
		BrowserWaits.waitTime(2);
		List<WebElement> countryTyeahead = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).get(1)
				.findElements(By.tagName("li"));
		if (countryTyeahead.size() > 0) {
			throw new Exception("Country typeahead options should display while enter min 2 chars");
		}

		// enter two characters check typeahead options should display
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS,
				twoChar);
		BrowserWaits.waitTime(4);
		List<WebElement> piTypeaheads = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).get(1)
				.findElements(By.tagName("li"));
		if (!(piTypeaheads.size() > 0))
			throw new Exception("Country Type ahead options are not displayed while enter two characters");

	}

	/**
	 * Method for Validate topic typeahead options should display while enter
	 * min 2 characters
	 * 
	 * @throws Exception,
	 *             When Typeahead options not displayed
	 */
	public void topicTypeaheadOptionsMinChars(String oneChar, String twoChar) throws Exception {
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS, oneChar);
		topicTypeahead = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
		logger.info("topic typeahed options before 2 characters-->" + topicTypeahead.size());
		if (topicTypeahead.size() > 0) {
			throw new Exception("topic typeahead options should display only while enter min 2 characters");
		}

		// pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS, twoChar);
		BrowserWaits.waitTime(4);
		topicTypeahead = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
		logger.info("topic typeahed options after 2 characters-->" + topicTypeahead.size());
		if (!(topicTypeahead.size() > 0)) {
			throw new Exception("topic typeahead options should display while enter min 2 characters");
		}
	}

	/**
	 * Method for validate profile tab focus, tab focus should be POST tab only
	 * 
	 * @throws Exception,
	 *             When tab focus on other than POST tab
	 */
	public void profileTabFocus() throws Exception {
		List<WebElement> profileTabs = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_CSS);
		String tabFocus = profileTabs.get(0).getAttribute("class");
		if (!tabFocus.contains("active")) {
			throw new Exception("Tab focus should be on POST Tab");
		}
	}

	public void postTabScroll() throws Exception {
		int totalPosts = getPostsCount();
		if (totalPosts > 10) {
			profileTabInfiniteScroll("Post");
		}
	}

	public void commentsTabScroll() throws Exception {
		clickCommentsTab();
		int totalComments = getCommentsCount();
		if (totalComments > 10) {
			profileTabInfiniteScroll("Comments");
		}
	}

	public void followersTabScroll() throws Exception {
		clickFollowersTab();
		int totalFollowers = getFollowersCount();
		if (totalFollowers > 10) {
			profileTabInfiniteScroll("Followers");
		}
	}

	public void followingTabScroll() throws Exception {
		clickFollowingTab();
		int totalFollowing = getFollowingCount();
		if (totalFollowing > 10) {
			profileTabInfiniteScroll("Following");
		}
	}

	public void watchlistTabScroll() throws Exception {
		clickWatchlistTab();
		int totalWatchlists = getWatchlistsCount();
		logger.info("watchlist tabs-->" + totalWatchlists);
		if (totalWatchlists > 10) {
			profileTabInfiniteScroll("Watchlist");
		}
	}

	/**
	 * Method for validate profile tab focus, tab focus should be POST tab only
	 * 
	 * @throws Exception,
	 *             When tab focus on other than POST tab
	 */
	public void profileTabInfiniteScroll(String tabName) throws Exception {
		profileTabsRecords = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_RECORDS_CSS);

		int beforeScroll = profileTabsRecords.size();
		((JavascriptExecutor) ob).executeScript("javascript:window.scrollBy(0,document.body.scrollHeight-150)");
		waitForAjax(ob);
		BrowserWaits.waitTime(4);

		profileTabsRecords = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_RECORDS_CSS);

		logger.info("before " + tabName + " scroll-->" + beforeScroll);
		int firstScroll = profileTabsRecords.size();
		logger.info(" first " + tabName + " scroll-->" + firstScroll);

		if (tabName.equalsIgnoreCase("Watchlist")) {
			if (!((firstScroll > beforeScroll) || (firstScroll > 10))) {
				throw new Exception(
						tabName + " Records/Records Count should be increase while do page scrolldown 1234");
			}
		} else {
			if (!(firstScroll > beforeScroll)) {
				throw new Exception(tabName + " Records/Records Count should be increase while do page scrolldown");
			}
		}
	}

	public int getPostLikeCount() throws Exception {
		String likeCount = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_LIKE_CSS).getText();
		return Integer.parseInt(likeCount);
	}

	/**
	 * Method to validate Post TimeStamp
	 * 
	 * @throws Exception,
	 *             When Post doesn't have any title
	 */
	public void validatePostTimeStamp() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("h:mm a");
		// get current date time with Date()
		Date date = new Date();
		String current_date = dateFormat.format(date).toString();
		String postCreationDate = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TIMESTAMP_XPATH).getText();
		logger.info("current date-->" + current_date);
		logger.info("post creation date-->" + postCreationDate);
		if (StringUtils.containsIgnoreCase(current_date, postCreationDate)) {
			throw new Exception("Post creation date and System date should match");
		}

	}

	public void deleteDraftPost(String postString) throws InterruptedException {
		waitForPageLoad(ob);
		waitForAjax(ob);
		BrowserWaits.waitTime(10);
		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob,
				By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_XPATH.toString()
						.replaceAll("TITLE", postString)),
				90);

		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_XPATH.toString()
				.replaceAll("TITLE", postString))));
		
//		ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_XPATH.toString()
//				.replaceAll("TITLE", postString))).click();

		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob, By.cssSelector(
				OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_CONFIRMATION_CSS.toString()), 30);

		jsClick(ob, ob.findElement(By
				.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_CONFIRMATION_CSS.toString())));
		/*ob.findElement(By
				.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_DELETE_CONFIRMATION_CSS.toString()))
				.click();*/
	}

	public List<String> getAllDraftPostTitle() {
		List<String> title = new ArrayList<String>();
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString()), 60);
		List<WebElement> drafts = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString()));
		for (WebElement we : drafts) {
			title.add(we.getText());
		}
		return title;
	}

	/**
	 * Method for validate comments time stamp
	 * 
	 * @throws Exception,
	 *             When comments doesn't have any time stamp
	 */
	public void commentsTabTimeStamp() throws Exception {
		clickCommentsTab();
		List<WebElement> commentTs = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_COMMENT_TIMESTAMP_CSS);
		if (!(commentTs.size() > 0)) {
			throw new Exception("None of the comments are having time stamp");
		}
		String timeStamp = commentTs.get(0).getText();
		// logger.info("timestamp-->"+timeStamp);
		if (!(timeStamp.contains("TODAY") || timeStamp.contains("2016") || timeStamp.contains("AM")
				|| timeStamp.contains("PM"))) {
			throw new Exception("Comments timestamp not displaying");
		}
	}

	/**
	 * Method to validate Other profiles Watchlist tab
	 * 
	 * @throws Exception,
	 *             When Post doesn't have any title
	 */
	public void otherProfileWatchlistTab() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS);
		String watchlistTabText = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TAB_WATCHLIST_CSS).getText();
		// logger.info("watchlist tab text-->"+watchlistTabText);
		if (!(watchlistTabText.contains("Watchlists"))) {
			throw new Exception("Other Profiles watchlist tab should be visible");
		}
	}

	public int getPostCommentCount() throws Exception {
		String commentCount = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_LIKE_CSS).get(1).getText();
		return Integer.parseInt(commentCount);
	}

	public void deleteDraftPostFromPostModal(String postString) throws Exception {
		BrowserWaits.waitTime(10);
		waitForAjax(ob);
		waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_EDIT_XPATH
				.toString().replaceAll("TITLE", postString)), 60);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_EDIT_XPATH.toString()
		.replaceAll("TITLE", postString))));
//		ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_RECORD_VIEW_DRAFT_POST_EDIT_XPATH.toString()
//				.replaceAll("TITLE", postString))).click();

		clickOnPostCancelButton();
		clickOnPostCancelDiscardButton();

	}

	public boolean isDraftPostTabDispalyed() {
		try {
			waitForAjax(ob);
			waitForElementTobePresent(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()), 30);
			return ob
					.findElement(
							By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()))
					.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validateExternalLinkInPostModal(String url) throws Exception {
		BrowserWaits.waitTime(10);
		waitForElementTobeClickable(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()), 90);

		WebElement content = ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()));
		if (content.findElements(By.linkText(url)).size() != 0)
			return true;
		else
			return false;
	}

	/**
	 * Method for follow profile from their profile page
	 * 
	 * @throws Exception,
	 *             When user not able to follow
	 */
	public void followOtherProfile() throws Exception {
		// ob.navigate().to("https://dev-stable.1p.thomsonreuters.com/#/profile/59f15292-a2d0-4555-bfc8-4fe37b95fa60");
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS);
		BrowserWaits.waitTime(6);
		String followInfo = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS)
				.getAttribute("data-uib-tooltip");
		logger.info("Follow/Unfollow Label Before-->" + followInfo);

		if (!followInfo.equals("Unfollow this person")) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_OTHER_PROFILE_TICKMARK_CSS);
			BrowserWaits.waitTime(2);
		}

	}

	/**
	 * Method for Validate HCR Badge in Profile page
	 * 
	 * @throws Exception,
	 *             When HCR Badge not displayed in Profile page
	 */
	public void hcrBadgeValidation(String hcrProfile) throws Exception {
		String profileName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText();
		logger.info("profile name-->" + profileName);
		String hcrProfileTooltip = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS)
				.findElement(By.cssSelector("span[class*='wui-icon--hcr']")).getAttribute("data-uib-tooltip");
		logger.info("HCR profile-->" + hcrProfileTooltip);

		if (!(profileName.equalsIgnoreCase(hcrProfile)
				&& hcrProfileTooltip.equalsIgnoreCase("Highly Cited Researcher"))) {
			throw new Exception("HCR Badge is not displayed in HCR profile page");
		}
	}

	/**
	 * Method for Validate First Name and Last Names fields
	 * 
	 * @throws Exception,
	 *             Validation not done
	 */
	public void validateFirstNameAndLastNameFields(String namesErrorMessages) throws Exception {

		String[] errorMessages = (String[]) namesErrorMessages.split("\\|");
		List<String> errorMsgList = Arrays.asList(errorMessages);

		List<String> namesInfo = new ArrayList<String>();

		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();

		logger.info("is Profile incomplete -->" + profileIncomplete);
		if (!profileIncomplete) {

			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);

			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);

			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);

			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);

			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS)
					.getAttribute("placeholder"));
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS)
					.getAttribute("placeholder"));
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_ERROR_MESSAGE_CSS).getText());
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_ERROR_MESSAGE_CSS).getText());

			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);

		}

		else {
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);

			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);

			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS)
					.getAttribute("placeholder"));
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS)
					.getAttribute("placeholder"));
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_ERROR_MESSAGE_CSS).getText());
			namesInfo.add(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_ERROR_MESSAGE_CSS).getText());

			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		logger.info("list data-->" + errorMsgList);
		logger.info("list data2-->" + namesInfo);
		if (!namesInfo.containsAll(errorMsgList)) {
			throw new Exception("First Name and Last Names Fields are not displaying any Error messages");
		}

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);

	}

	/**
	 * Method for update first name and last name
	 * 
	 * @throws Exception,
	 *             When First and last names are not updated
	 */
	public void updateFirstNameAndLastName(String firstName, String LastName) throws Exception {

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS,
				firstName);
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS,
				LastName);

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		BrowserWaits.waitTime(4);

		String firstLastName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText();
		logger.info("first and last name-->" + firstLastName);
		if (!(StringUtils.containsIgnoreCase(firstLastName, firstName)
				&& StringUtils.containsIgnoreCase(firstLastName, LastName))) {
			throw new Exception("First name and last names are not getting updated in Profile page ");
		}
	}

	/**
	 * Method for Validate First Name and Last Names fields with Max length 50
	 * characters
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validateFirstNameAndLastNameFieldsMaxLength(int firstNameLength, int LastNameLength, int totCount)
			throws Exception {

		if (!isProfileIncomplete()) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);
		BrowserWaits.waitTime(3);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS,
				RandomStringUtils.randomAlphabetic(firstNameLength));
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);
		BrowserWaits.waitTime(3);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS,
				RandomStringUtils.randomAlphabetic(LastNameLength));

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		BrowserWaits.waitTime(3);
		String firstLastName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText();
		logger.info("title-->" + firstLastName + "provided count-->" + totCount);
		int nameLength = firstLastName.trim().replace(" ", "").length();
		logger.info("First name and Last name length-->" + nameLength);
		if (!(nameLength == totCount)) {
			throw new Exception("First name and last name length should not exceed morethan 100 characters");
		}
	}

	/**
	 * Method for Verify user profile is complete or incomplete
	 * 
	 * @throws Exception,
	 *             When profile page not displayed
	 */
	public boolean isProfileIncomplete() throws Exception {
		profileIncomplete = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS).isDisplayed();
		return profileIncomplete;
	}

	/**
	 * Method for Click Profile picture Edit button
	 * 
	 * @throws Exception,
	 *             When Profile picture Edit button not able to click
	 */
	public void profilePicModalWindow() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_BUTTON_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_BROWSE_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Profile
		// Picture","Select Image File: ","(Images must be no more than 1024px
		// or 256KB in size)");
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile image", "Select an image",
				"(at least 120 px by 120 px and less than 8 MB).");
	}

	/**
	 * Method for get profile pic update button status
	 * 
	 * @throws Exception,
	 *             When update button not available
	 */
	public boolean getProfilePicModalWindowUpdateButtonStatus() throws Exception {
		return pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_UPDATE_CSS).isEnabled();

	}

	/**
	 * Method for Verify profile pic update button status, button should be in
	 * disabled state
	 * 
	 * @throws Exception,
	 *             When update button is Enabled
	 */
	public void validateProfilePicUpdateButtonStatus() throws Exception {
		profilePicModalWindow();
		boolean proiflePicUpdateButton = getProfilePicModalWindowUpdateButtonStatus();
		logger.info("profile pic update button status-->" + proiflePicUpdateButton);
		if (proiflePicUpdateButton) {
			throw new Exception("profile pic update button should be disabled by default");
		}
	}

	/**
	 * Method for close profile picture modal window
	 * 
	 * @throws Exception,
	 *             When profile pic modal window not closed
	 */
	public void closeProfilePicModalWindow() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilNotText("Profile
		// Picture","Select Image File: ","(Images must be no more than 1024px
		// or 256KB in size)");
		pf.getBrowserWaitsInstance(ob).waitUntilNotText("Profile image", "Select an image",
				"(no more than 1024 px or 256 KB in size).");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
	}

	/**
	 * Method for cancel profile picture modal window
	 * 
	 * @throws Exception,
	 *             When profile pic modal window not cancel
	 */
	public void cancelProfilePicModalWindow() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CANCEL_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilNotText("Profile
		// Picture","Select Image File: ","(Images must be no more than 1024px
		// or 256KB in size)");
		pf.getBrowserWaitsInstance(ob).waitUntilNotText("Profile image", "Select an image",
				"(no more than 1024 px or 256 KB in size).");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
	}

	/**
	 * Method for upload new profile image
	 * 
	 * @throws Exception,
	 *             When profile image not able to upload
	 */
	public void uploadProfileImage() throws Exception {
		String profileImageTimeStamp = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_BUTTON_CSS).findElement(By.tagName("img"))
				.getAttribute("src");
		logger.info("image timestamp before upload-->" + profileImageTimeStamp);
		String imgPath = System.getProperty("user.dir") + "\\images\\" + "myimage" + ".jpg";
		logger.info("image path-->" + imgPath);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_SELECT_IMAGE_PLINK);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_SELECT_IMAGE_PLINK);
		BrowserWaits.waitTime(4);
		Runtime.getRuntime().exec("autoit_scripts/imageUpload2.exe" + " " + imgPath);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_UPDATE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_UPDATE_CSS);
		BrowserWaits.waitTime(8);

		String profileImageTimeStampAfterUpload = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_BUTTON_CSS).findElement(By.tagName("img"))
				.getAttribute("src");
		logger.info("image timestamp After upload-->" + profileImageTimeStampAfterUpload);

		if (profileImageTimeStamp.equalsIgnoreCase(profileImageTimeStampAfterUpload)) {
			throw new Exception("Profile image not uploaded successfully");
		}
	}

	public void clickOnPostModalCloseButton() throws Exception {
		// Commented by KR
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
		// OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_MODAL_CLOSE_BUTTON_CSS);
		BrowserWaits.waitTime(5);
		ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_MODAL_CLOSE_BUTTON_CSS.toString()))
				.click();
	}

	public boolean validateCancelModalControlsEditPost(ExtentTest test) {
		waitForElementTobeVisible(ob,
				By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_PUBLISH_XPATH.toString()), 60);
		boolean cond1 = ob
				.findElement(
						By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_DISCARD_XPATH.toString()))
				.isDisplayed();
		boolean cond2;
		try {
			cond2 = ob
					.findElement(By.xpath(
							OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_KEEP_DRAFT_XPATH.toString()))
					.isDisplayed();
		} catch (Exception e) {
			cond2 = false;
		}

		boolean cond3 = ob
				.findElement(
						By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_PUBLISH_XPATH.toString()))
				.isDisplayed();

		if (!cond1)
			test.log(LogStatus.INFO, "Discard button not present");
		if (cond2)
			test.log(LogStatus.INFO, "Keep draft button is present");
		if (!cond3)
			test.log(LogStatus.INFO, "Publish button not present");

		return cond1 && !cond2 && cond3;

	}

	public boolean validateCancelModalControls(ExtentTest test) {
		waitForElementTobeVisible(ob,
				By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_PUBLISH_XPATH.toString()), 60);

		boolean cond1 = ob
				.findElement(
						By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_DISCARD_XPATH.toString()))
				.isDisplayed();
		boolean cond2 = ob
				.findElement(By
						.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_KEEP_DRAFT_XPATH.toString()))
				.isDisplayed();
		boolean cond3 = ob
				.findElement(
						By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CANCEL_PUBLISH_XPATH.toString()))
				.isDisplayed();

		if (!cond1)
			test.log(LogStatus.INFO, "Discard button not present");
		if (!cond1)
			test.log(LogStatus.INFO, "Keep draft button not present");
		if (!cond1)
			test.log(LogStatus.INFO, "Publish button not present");

		return cond1 && cond2 && cond3;

	}

	/**
	 * Method for Verify profile CTA in a whitebox displayed for country field
	 * if profile first/last name, title/role, country is showing empty
	 * 
	 * @throws Exception,
	 *             country whitebox not getting displayed
	 */
	public void validateProfileCTACountryField() throws Exception {
		profileIncomplete = isProfileIncomplete();

		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS);
		BrowserWaits.waitTime(2);
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
		BrowserWaits.waitTime(2);
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
		BrowserWaits.waitTime(2);
		String ctaCountryText = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CTA_WHITE_BOX_CSS).get(1).getText();
		logger.info("CTA in whitebox Country field-->" + ctaCountryText);
		pf.getBrowserWaitsInstance(ob).waitUntilText(ctaCountryText);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		BrowserWaits.waitTime(2);
		String ctaTextAfterSave = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CTA_WHITE_BOX_CSS).get(0).getText();
		logger.info("CTA in whitebox Country field after updated-->" + ctaTextAfterSave);

		if (!ctaCountryText.equalsIgnoreCase(ctaTextAfterSave)) {
			throw new Exception("Profile CTA country fied White box not getting updated");
		}

	}

	/**
	 * Method for Verify profile CTA in a whitebox displayed for country field
	 * if profile Summary is showing empty
	 * 
	 * @throws Exception,
	 *             Summary whitebox not getting displayed
	 */
	public void validateProfileCTASummaryField() throws Exception {
		profileIncomplete = isProfileIncomplete();

		if (!profileIncomplete) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CANCEL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		}

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_SUMMARY_CSS);
		BrowserWaits.waitTime(2);
		String ctaCountryText = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CTA_WHITE_BOX_CSS).get(2).getText();
		logger.info("CTA in whitebox Summary field-->" + ctaCountryText);
		pf.getBrowserWaitsInstance(ob).waitUntilText(ctaCountryText);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		BrowserWaits.waitTime(2);
		String ctaTextAfterSave = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CTA_WHITE_BOX_CSS).get(3).getText();
		logger.info("CTA in whitebox Summary field after updated-->" + ctaTextAfterSave);

		if (!ctaCountryText.equalsIgnoreCase(ctaTextAfterSave)) {
			throw new Exception("Profile CTA Summary fied White box not getting updated");
		}

	}

	/**
	 * Method for Verify profile CTA in a whitebox displayed for Add a Topic
	 * field if profile Topics are showing empty
	 * 
	 * @throws Exception,
	 *             Add a Topic whitebox not getting displayed
	 */
	public void validateProfileCTATopicField() throws Exception {
		List<WebElement> addedTopics = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
		if (addedTopics.size() > 0) {
			for (WebElement addedTopic : addedTopics) {
				addedTopic.click();
				BrowserWaits.waitTime(2);
			}
		}

		String ctaTopicText = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CTA_WHITE_BOX_CSS).get(4).getText();
		logger.info("CTA in whitebox Topic field if no Interests and Skills-->" + ctaTopicText);
		if (!ctaTopicText.equalsIgnoreCase("Add your skills and interests to help connect you with others.")) {
			throw new Exception("Profile CTA 'Add a Topic' field White box not getting updated");
		}

	}

	/**
	 * Method for Neon To ENW using App links
	 * 
	 * @throws Exception,
	 *             When ENW page is not displayed
	 */
	public void neonToENWUsingAppLinks() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_TO_ENW_PLINK);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_TO_ENW_PLINK);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);

		try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		} catch (Exception e) {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		}
		BrowserWaits.waitTime(4);

		// Navigate to ENW page using Non-Market user
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_PROFILE_IMAGE_XPATH);
	}

	/**
	 * Method for Verify profile tabs should contain default messages when tab
	 * count is 0
	 * 
	 * @param defaultMsg
	 * @throws Exception,
	 *             When default message not getting displayed
	 */
	public void profileTabsDefaultMessages(ExtentTest test, String tabName, String defaultMsg) throws Exception {

		if (tabName.equalsIgnoreCase("Posts")) {
			int totalPosts = getPostsCount();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS);
			String postTabDefaultText = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS)
					.findElement(By.tagName("p")).getText();
			logger.info("post tab default message-->" + postTabDefaultText);
			if (!(totalPosts == 0 && postTabDefaultText.equalsIgnoreCase(defaultMsg))) {
				logFailureDetails(test, "No default Post Tab messagae", "default_post_tab_msg_fail");
			}
		} else if (tabName.equalsIgnoreCase("comments")) {
			clickCommentsTab();
			int totalComments = getCommentsCount();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS);
			String commentsTabDefaultText = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS)
					.findElement(By.tagName("p")).getText();
			logger.info("comments tab default message-->" + commentsTabDefaultText);
			if (!(totalComments == 0 && commentsTabDefaultText.equalsIgnoreCase(defaultMsg))) {
				logFailureDetails(test, "No default Comments Tab messagae", "default_comments_tab_msg_fail");
			}
		} else if (tabName.equalsIgnoreCase("Followers")) {
			clickFollowersTab();
			int totalFollowers = getFollowersCount();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS);
			String followersTabDefaultText = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS)
					.findElement(By.tagName("p")).getText();
			logger.info("followers tab default message-->" + followersTabDefaultText);
			if (!(totalFollowers == 0 && followersTabDefaultText.equalsIgnoreCase(defaultMsg))) {
				logFailureDetails(test, "No default Followers Tab messagae", "default_followers_tab_msg_fail");
			}
		} else if (tabName.equalsIgnoreCase("Following")) {
			clickFollowingTab();
			int totalFollowing = getFollowingCount();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS);
			String followingTabDefaultText = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS)
					.findElement(By.tagName("p")).getText();
			logger.info("following tab default message-->" + followingTabDefaultText);
			BrowserWaits.waitTime(2);
			if (!(totalFollowing == 0 && followingTabDefaultText.equalsIgnoreCase(defaultMsg))) {
				logFailureDetails(test, "No default Following Tab messagae", "default_following_tab_msg_fail");
			}
		} else if (tabName.equalsIgnoreCase("Watchlists")) {
			clickWatchlistTab();
			int totalWatchlists = getWatchlistsCount();
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS);
			String watchlistTabDefaultText = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TABS_DEFAULT_MESSAGE_CSS).getText();
			logger.info("watchlist tab default message-->" + watchlistTabDefaultText);
			logger.info("watchlist tab default message2-->" + defaultMsg);
			if (!(totalWatchlists == 0 && watchlistTabDefaultText.equalsIgnoreCase(defaultMsg))) {
				logFailureDetails(test, "No default Watchlist Tab messagae", "default_watchlist_tab_msg_fail");
			}
		} else {
			throw new Exception("Invalid Profile Tab details suppllied");
		}

	}

	// #################DRA-Profile flyout#################
	/**
	 * Method for Validate Application Header and Footer links Header Link -
	 * Help link Footer Links - Cookie Policy,Privacy Statement,Terms of Use
	 * 
	 * @throws Exception,
	 *             When validation not done and Element Not found
	 */
	public void validateProflileFlyoutLinks(ExtentTest test, String proflieFlyoutLinks) throws Exception {

		try {
			String flyoutLinks[] = proflieFlyoutLinks.split("\\|");
			for (int i = 0; i < flyoutLinks.length; i++) {

				pf.getHFPageInstance(ob).clickDRAProfileFlyoutImage();

				if (flyoutLinks[i].equalsIgnoreCase("Privacy")) {
					test.log(LogStatus.INFO, "DRA Proflie flyout  " + flyoutLinks[i] + "Links validation");
					pf.getBrowserActionInstance(ob)
							.click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIVACY_STATEMENT_LINK);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();

					for (String winHan : windows) {
						ob.switchTo().window(winHan);
					}
					pf.getBrowserWaitsInstance(ob).waitUntilText("Scope", "Highlights", "Full Privacy Statement",
							"Cookies", "Last updated:");
					pf.getBrowserWaitsInstance(ob).waitUntilText(flyoutLinks[i]);
					String psText = ob.findElement(By.tagName("h2")).getText();
					logger.info("ps text-->" + psText);
					if (!StringUtils.containsIgnoreCase(psText, flyoutLinks[i])) {
						logFailureDetails(test, psText + "Page not opened", "DRA/IPA-Privacy Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS,
							"DRA/IPA Proflie flyout  " + flyoutLinks[i] + " Link validation is Successful");
				}

				else if (flyoutLinks[i].equalsIgnoreCase("Terms of Use")) {
					test.log(LogStatus.INFO, "DRA Proflie flyout  " + flyoutLinks[i] + "Links validation");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK);
					System.out.println(ob.getWindowHandles().size());
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();

					for (String winHan : windows) {
						ob.switchTo().window(winHan);
					}
					pf.getBrowserWaitsInstance(ob).waitUntilText(
							"These Terms of Use shall govern your", "Last updated:");
					pf.getBrowserWaitsInstance(ob).waitUntilText(flyoutLinks[i]);
					String tcText = ob.findElement(By.tagName("h2")).getText();
					logger.info("Terms of Use text-->" + tcText);
					if (!StringUtils.equalsIgnoreCase(tcText, flyoutLinks[i])) {
						logFailureDetails(test, tcText + "Page not opened", "DRA-Terms of Use Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS,
							"DRA/IPA Proflie flyout  " + flyoutLinks[i] + " Link validation is Successful");
				} else if (flyoutLinks[i].equalsIgnoreCase("Help")) {
					test.log(LogStatus.INFO, "DRA/IPA Proflie flyout  " + flyoutLinks[i] + "Link validation");
					String helpLinkAddressURL = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HELP_LINK)
							.getAttribute("href");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HELP_LINK);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();

					for (String winHan : windows) {
						ob.switchTo().window(winHan);
					}
					BrowserWaits.waitTime(10);// to load pdf
					String currentPageUrl = ob.getCurrentUrl();
					logger.info("Help page url-->" + currentPageUrl);
					logger.info("Help link address-->" + helpLinkAddressURL);
					if (!StringUtils.equalsIgnoreCase(currentPageUrl, helpLinkAddressURL)) {
						logFailureDetails(test, flyoutLinks[i] + "Page not opened", "DRA-Help Fail");
					}

					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS, "Proflie flyout  " + flyoutLinks[i] + " Link validation is Successful");
				} else if (flyoutLinks[i].equalsIgnoreCase("Feedback")) {
					test.log(LogStatus.INFO, "DRA/IPA Proflie flyout  " + flyoutLinks[i] + "Links validation");
					String feedbankLinkAddressURL = pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.ENW_FEEDBACK_XPATH).getAttribute("href");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_FEEDBACK_XPATH);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();

					for (String winHan : windows) {
						ob.switchTo().window(winHan);
					}
					BrowserWaits.waitTime(4);
					// pf.getBrowserWaitsInstance(ob).waitUntilText("To provide
					// feedback","Next");
					String currentPageUrl = ob.getCurrentUrl();
					logger.info("Feedback page url-->" + currentPageUrl);
					logger.info("Feedback link address-->" + feedbankLinkAddressURL);
					if (!StringUtils.containsIgnoreCase(currentPageUrl, feedbankLinkAddressURL)) {
						logFailureDetails(test, flyoutLinks[i] + "Page not opened", "DRA/IPA -Feedback Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS,
							"DRA/IPA Proflie flyout  " + flyoutLinks[i] + " Link validation is Successful");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ob.navigate().back();
			logFailureDetails(test, "Flyouts links not responded", "Profile flyouts Fail");
		}
	}

	/**
	 * Method for check all info should present under profile flyout
	 * 
	 * @param draProfileFlyout
	 * @throws Exception,
	 *             When flyout not having sufficient info
	 */
	public void validateDRAProfileFlyout(String draProfileFlyout, ExtentTest test) throws Exception {
		String draProfile[] = draProfileFlyout.split("\\|");
		List<String> draFlyout = Arrays.asList(draProfile);
		List<String> profileFlyout = new ArrayList<String>();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		Thread.sleep(3000);
		String profileTitle = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS)
				.getText();
		String profileMetadata = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS)
				.get(1).getText();

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account", "Privacy", "Terms of Use", "Feedback", "Sign out");

		List<WebElement> draProfileFlyoutLinks = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS).findElements(By.tagName("a"));
		for (WebElement flyout : draProfileFlyoutLinks) {
			profileFlyout.add(flyout.getText());
		}
		logger.info("profile status-->" + profileMetadata);
		logger.info("profile title-->" + profileTitle);
		logger.info("profile flyout1 input-->" + draFlyout);
		logger.info("profile flyout2-->" + profileFlyout);

		boolean isImagePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_HEADER_IMAGE_CSS).isEnabled();

		if (!((isImagePresent && StringUtils.isNotEmpty(profileTitle) && profileFlyout.containsAll(draFlyout))
				&& (StringUtils.isEmpty(profileMetadata) || StringUtils.isNotEmpty(profileMetadata)))) {
			// throw new Exception("Profile Flyout info not displayed");
			logFailureDetails(test, "Profile Flyout info not displayed", "Profile_flyout_info_not_displayed");
		}
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
	}

	/**
	 * Method for Click proifle title in Profile flyout
	 * 
	 * @throws Exception
	 */
	public void clickProfileFlyout() throws Exception {
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS.toString()), 200);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account", "Privacy", "Terms of Use", "Feedback", "Sign out");
	}

	/**
	 * Method for Click proifle title in Profile flyout
	 * 
	 * @throws Exception
	 */
	public void clickProfileTitleLink() throws Exception {
		clickProfileFlyout();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile", "Update", "Cancel");
	}

	/**
	 * Method for Validate DRA Profile page
	 * 
	 * @throws Exception,When
	 *             Profile page not landing properly
	 */
	public void validateDRAProfilePageAndClose(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile", "Update", "Cancel");
		boolean isFirstNamePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS).isEnabled();
		boolean isLastNamePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS).isEnabled();
		boolean isRolePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS).isEnabled();
		boolean isPIPresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PRIINS_CSS).isEnabled();
		boolean isCountryPresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS).isEnabled();
		boolean isCancelPresent = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_UPDATE_CSS)
				.isEnabled();
		boolean isUpdatePresent = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_CANCEL_CSS)
				.isEnabled();
		boolean isImageEditPresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_PROFILE_IMAGE_EDIT_CSS).isEnabled();
		boolean isImageDeletePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_PROFILE_IMAGE_DELETE_CSS).isDisplayed();

		if (!(isFirstNamePresent && isLastNamePresent && isRolePresent && isCountryPresent && isCancelPresent
				&& isPIPresent && isUpdatePresent && isImageEditPresent && isImageDeletePresent)) {
			// throw new Exception("Profile page not landing properly");
			logFailureDetails(test, "Profile page not landing properly", "profile_info_not_displayed");
		}

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_CANCEL_CSS);
		BrowserWaits.waitTime(2);
	}

	/**
	 * Method for Validate Account modal
	 * 
	 * @throws Exception
	 */
	public void validateAccountLinkModalAndClose() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account", "Connected Accounts");
		pf.getBrowserWaitsInstance(ob).waitUntilText("Last sign in:", "Change password");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS);
		String accountText = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS)
				.getText();
		if (!(accountText.equals("Account"))) {
			throw new Exception("Account modal is not displayed");
		}
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS);

	}

	// #################DRA-Profile flyout#################

	// ************************Authoring Related
	// *********************************
	/**
	 * Method to return comment count from article record view
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int getCommentCount() throws InterruptedException {
		BrowserWaits.waitTime(2);
		// Commented by KR
		waitForPageLoad(ob);
		// waitForAjax(ob);
		// scrollingToElementofAPage();
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString()),
				180);
		String commentSizeBeforeAdd = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString())).getText()
				.replaceAll(",", "").trim();
		return Integer.parseInt(commentSizeBeforeAdd);
	}
}
