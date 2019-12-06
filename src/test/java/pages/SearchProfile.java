package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class SearchProfile extends TestBase {

	/**
	 * Search results people count
	 */
	static int peopleCount = 0;
	static String followUnfollowLableBefore;
	static String followUnfollowLableAfter;

	PageFactory pf;

	public SearchProfile(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();

	}

	public void enterSearchKeyAndClick(String searchKey) throws Exception {
		waitForElementTobePresent(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()), 200);
		pf.getBrowserActionInstance(ob).clear(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS, searchKey);
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS); 
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
		waitForElementTobeVisible(ob,By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_RESULTS_LINK_CSS.toString()),80);
		BrowserWaits.waitTime(4);
	}

	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception, When People are not present/Disabled
	 */
	public int getPeopleCount() throws Exception {
		String listPeople = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PEOPLE_CSS).get(3).findElement(By.tagName("span"))
				.getText();
		peopleCount = Integer.parseInt(listPeople);
		logger.info("Total People search results-->" + peopleCount);
		return peopleCount;
	}

	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception, When People are not present/Disabled
	 */
	public int validatePeopleSearchResults() throws Exception {
		String listPeople = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PEOPLE_CSS).get(2).findElement(By.tagName("span"))
				.getText();
		peopleCount = Integer.parseInt(listPeople);
		System.out.println("Total People search results-->" + peopleCount);
		return peopleCount;
	}

	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception, When People are not present/Disabled
	 */
	public void clickPeople() throws Exception {
		BrowserWaits.waitTime(5);
		pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PEOPLE_CSS).get(3).click();
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TICKMARK_CSS.toString()), 90);
	}

	/**
	 * Follow/UnFollow other Profile from Search Page itself
	 * 
	 * @throws Exception
	 */
	public void followProfileFromSeach() throws Exception {
		BrowserWaits.waitTime(2);
		WebElement followUnFollowCheck = pf.getBrowserActionInstance(ob).getElement(
				OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TICKMARK_CSS);
		followUnfollowLableBefore = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1).getAttribute("data-uib-tooltip");
		System.out.println("Follow/Unfollow Label Before-->" + followUnfollowLableBefore);
		//followUnFollowCheck.click();
		pf.getBrowserActionInstance(ob).jsClick(followUnFollowCheck);
		BrowserWaits.waitTime(4);
		followUnfollowLableAfter = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_PROFILE_TOOLTIP_CSS).get(1).getAttribute("data-uib-tooltip");
		System.out.println("Follow/Unfollow Label After-->" + followUnfollowLableAfter);

		if (followUnfollowLableBefore.equalsIgnoreCase(followUnfollowLableAfter)) {
			throw new Exception("unable to follow other profile from search screen");
		}
	}

	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception, When People are not present/Disabled
	 */
	public void hcrProfileBadgeValidation() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
				OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HCR_BADGE_CSS);
		String hcrAttr = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HCR_BADGE_CSS).get(1).findElement(By.cssSelector("span[class*='wui-icon--hcr']"))
				.getAttribute("data-uib-tooltip");
		logger.info("hcr profile badge-->"+hcrAttr);
		if (!hcrAttr.contains("Highly Cited Researcher")) {
			throw new Exception("HCR Profile should display with Badge");
		}
	}  
	
	/**
	 * Method for Click People after searching an profile
	 * 
	 * @throws Exception, When People are not present/Disabled
	 */
	public void selectProfile(String profileName) throws Exception {
		List<WebElement> searchPeoples=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_PEOPLE_TITLE_CSS);
		for(WebElement searchPeople:searchPeoples){
			if(searchPeople.getText().trim().equalsIgnoreCase(profileName)) {
				searchPeople.click();
				break;
			}
		}
	}

}
