package pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import util.OnePObjectMap;
import base.TestBase;

/**
 * This class contains all the method related to header and footer of the application
 * @author uc205521
 *
 */
public class HeaderFooterLinksPage extends TestBase {

	PageFactory pf;

	public HeaderFooterLinksPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method for Validate Application Header and Footer links Header Link - Help link Footer Links - Cookie
	 * Policy,Privacy Statement,Terms of Use
	 * 
	 * @throws Exception, When validation not done and Element Not found
	 */
	public void validateLinks(String appHeadFooterLinks) throws Exception {
		String headerFooterLinks[] = appHeadFooterLinks.split("\\|"); 

		for (int i = 0; i < headerFooterLinks.length; i++) {

			clickProfileImage();

			if (headerFooterLinks[i].equalsIgnoreCase("Privacy")) {  
				
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIVACY_STATEMENT_LINK);
				pf.getBrowserWaitsInstance(ob).waitUntilText("Scope", "Highlights", "Full Privacy Statement","Cookies", "Last updated");
				pf.getBrowserWaitsInstance(ob).waitUntilText(headerFooterLinks[i]);
				String psText = ob.findElement(By.tagName("h2")).getText();
				logger.info("ps text-->"+psText);
				if(!StringUtils.containsIgnoreCase(psText, headerFooterLinks[i])) {
					throw new Exception(psText+" page not opened");
				}
			}

			else if (headerFooterLinks[i].equalsIgnoreCase("Terms of Use")){   
				//pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilText(
						"These Terms of Use shall govern your use of this online service and associated applications","Last updated");
				
				pf.getBrowserWaitsInstance(ob).waitUntilText(headerFooterLinks[i]);
				String tcText = ob.findElement(By.tagName("h2")).getText();
				logger.info("Terms of Use text-->"+tcText);
				Assert.assertEquals(headerFooterLinks[i], tcText);
			}
		}
	}

	/**
	 * Method for validate Help link Content
	 * 
	 * @throws Exception
	 */
	public void helpLinkValidation() throws Exception {
		clickProfileImage();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HELP_LINK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HELP_LINK_CSS);
		waitForPageLoad(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Feedback or questions about Project Neon?");
		pf.getBrowserWaitsInstance(ob).waitUntilText("Send feedback to the Project Neon team");
		pf.getBrowserWaitsInstance(ob).waitUntilText("Report a problem or submit a support request");

	}

	/**
	 * Method for validate Account link Content
	 * 
	 * @throws Exception
	 */
	public void accountLinkValidation() throws Exception {
		clickProfileImage();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK_CSS);
		waitForPageLoad(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account", "Manage accounts", "Communications");
	}

	/**
	 * Method for click profile image
	 * 
	 * @throws Exception, When Profile image not available
	 */
	public void clickProfileImage() throws Exception {
		//waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString()), 180);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString()), 200);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile", "Account", "Help", "Sign out");
	}

	/**
	 * Method to click on WatchList link in header
	 */
	public void clickOnWatchLink() throws Exception {
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_WATCHLIST_CSS.toString()),
				180);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_HEADER_WATCHLIST_CSS);
		// pf.waitTime(4);
		waitForPageLoad(ob);

	}

	/**
	 * Method to click on Home link in header
	 */
	public void clickOnHomeLink() {
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_NEWSFEED_CSS.toString()), 300);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_NEWSFEED_CSS.toString()), 180);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_NEWSFEED_CSS.toString())).click();
	}

	/**
	 * Method to click on Publish A Post link in header
	 */
	public void clickOnPublishAPostLink() {

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_PUBLISH_A_POST_CSS.toString()), 180);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_HEADER_PUBLISH_A_POST_CSS.toString())).click();
	}

	/**
	 * Method to click on Profile link under profile dropdown in header
	 */
	public void clickOnProfileLink() throws Exception {
		waitForPageLoad(ob);
		clickProfileImage();
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CSS.toString()), 180);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CSS.toString())));
		// pf.waitTime(4);
		waitForPageLoad(ob);
	}

	/**
	 * Method to click on Account link under profile dropdown in header
	 */
	public void clickOnAccountLink() throws Exception {
		clickProfileImage();
		waitForElementTobeVisible(ob, By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK.toString()), 180);
		ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK.toString())).click();
	}

	/**
	 * Method to click on SignOut link under profile dropdown in header
	 */
	public void clickOnSignOutLink() throws Exception {
		clickProfileImage();
		waitForElementTobeVisible(ob, By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString()), 180);
		ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString())).click();
	}

	/**
	 * Method to click on Help link under profile dropdown in header
	 */
	public void clickOnHelpLink() throws Exception {
		clickProfileImage();

		waitForElementTobeVisible(ob, By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HELP_LINK.toString()), 180);
		ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_HELP_LINK.toString())).click();
	}

	/**
	 * Method to enter search text in search box
	 * 
	 * @param searchText
	 */
	public void enterSearchText(String searchText) {
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()), 180);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString())).sendKeys(searchText);
	}

	/**
	 * Method to click on search icon
	 * @throws Exception 
	 */
	public void clickOnSearchIcon() throws Exception {

		//waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString()), 180);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable((OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS));
		jsClick(ob,ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())));
		
	}
	/**
	 * Method to search for the given text.
	 * 
	 * @param text
	 */
	public void searchForText(String text) {
		enterSearchText(text);
		try {
			clickOnSearchIcon();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
     
	public void clickOnEndNoteLink() throws Exception{
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_TO_ENW_PLINK);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_TO_ENW_PLINK);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
	}	
	
	
	/**
	 * Method for click profile image
	 * 
	 * @throws Exception, When Profile image not available
	 */
	public void clickDRAProfileFlyoutImage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile", "Account", "Help", "Sign out");
	}
	
}
