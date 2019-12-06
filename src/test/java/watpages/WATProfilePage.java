package watpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pages.PageFactory;
import pages.ProfilePage;
import util.BrowserWaits;
import util.OnePObjectMap;

public class WATProfilePage extends ProfilePage {

	
	public WATProfilePage(WebDriver ob) {
		super(ob);
		this.ob = ob;
		pf = new PageFactory();
	}
	
	//#################WAT-Profile flyout#################
	/**
	 * Method for Validate Application Header and Footer links Header Link - Help link Footer Links - Cookie
	 * Policy,Privacy Statement,Terms of Use
	 * 
	 * @throws Exception, When validation not done and Element Not found
	 */
	@Override
	public void validateProflileFlyoutLinks(ExtentTest test,String proflieFlyoutLinks) throws Exception {
		
		try {
			String flyoutLinks[] = proflieFlyoutLinks.split("\\|");
			for (int i = 0; i < flyoutLinks.length; i++) {

				pf.getHFPageInstance(ob).clickDRAProfileFlyoutImage();

				if (flyoutLinks[i].equalsIgnoreCase("Privacy")) {
					test.log(LogStatus.INFO, "WAT Proflie flyout  "+flyoutLinks[i]+"Links validation");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PRIVACY_STATEMENT_LINK);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();
					
					for(String winHan : windows){
						ob.switchTo().window(winHan);
					}
					pf.getBrowserWaitsInstance(ob).waitUntilText("Scope", "Highlights", "Full Privacy Statement","Cookies","Last updated:");
					pf.getBrowserWaitsInstance(ob).waitUntilText(flyoutLinks[i]);
					String psText = ob.findElement(By.tagName("h2")).getText();
					logger.info("ps text-->"+psText+"-->"+ob.getCurrentUrl());
					if(!((StringUtils.containsIgnoreCase(psText, flyoutLinks[i])) && (StringUtils.containsIgnoreCase(ob.getCurrentUrl(), "/#/privacy-statement-app")))) {
						logFailureDetails(test, psText+"Page not opened", "WAT-Privacy Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS, "WAT Proflie flyout  "+flyoutLinks[i]+" Link validation is Successful");
				}

				else if (flyoutLinks[i].equalsIgnoreCase("Terms of Use")){ 
					test.log(LogStatus.INFO, "WAT Proflie flyout  "+flyoutLinks[i]+"Links validation");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TERMS_OF_USE_LINK);
					System.out.println(ob.getWindowHandles().size());
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();
					
					for(String winHan : windows){
						ob.switchTo().window(winHan);
					}
					pf.getBrowserWaitsInstance(ob).waitUntilText(
							"These Terms of Use shall govern your use of this online service","Last updated:");
					pf.getBrowserWaitsInstance(ob).waitUntilText(flyoutLinks[i]);
					String tcText = ob.findElement(By.tagName("h2")).getText();
					logger.info("Terms of Use text-->"+tcText);
					if(!((StringUtils.equalsIgnoreCase(tcText, flyoutLinks[i])) && (StringUtils.containsIgnoreCase(ob.getTitle(), "/#/terms-of-use-app")))) {
						logFailureDetails(test, tcText+"Page not opened", "WAT-Terms of Use Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS, "WAT Proflie flyout  "+flyoutLinks[i]+" Link validation is Successful");
				}
				else if (flyoutLinks[i].equalsIgnoreCase("Help")){ 
					test.log(LogStatus.INFO, "WAT Proflie flyout  "+flyoutLinks[i]+"Link validation");
					String helpLinkAddressURL=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HELP_LINK).getAttribute("href");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HELP_LINK);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();
					
					for(String winHan : windows){
						ob.switchTo().window(winHan);
					}
					BrowserWaits.waitTime(6);//to load pdf
					String currentPageUrl=ob.getCurrentUrl();
					logger.info("Help page url-->"+currentPageUrl);
					logger.info("Help link address-->"+helpLinkAddressURL); 
					if(!StringUtils.equalsIgnoreCase(currentPageUrl, helpLinkAddressURL)) {
						logFailureDetails(test, flyoutLinks[i]+"PDF Page not opened", "WAT-Help Fail");
					}
					
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS, "Proflie flyout  "+flyoutLinks[i]+" Link validation is Successful");
				}
				else if (flyoutLinks[i].equalsIgnoreCase("Feedback")){ 
					test.log(LogStatus.INFO, "WAT Proflie flyout  "+flyoutLinks[i]+"Links validation");
					String feedbankLinkAddressURL=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_FEEDBACK_XPATH).getAttribute("href");
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_FEEDBACK_XPATH);
					String main = ob.getWindowHandle();
					Set<String> windows = ob.getWindowHandles();
					
					for(String winHan : windows){
						ob.switchTo().window(winHan);
					}
					pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs Feedback","To contact the SaR Labs team, please email");
					String currentPageUrl=ob.getCurrentUrl();
					logger.info("Feedback page url-->"+currentPageUrl);
					logger.info("Feedback link address-->"+feedbankLinkAddressURL);
					if(!StringUtils.containsIgnoreCase(currentPageUrl, feedbankLinkAddressURL)) {
						logFailureDetails(test, flyoutLinks[i]+"Survey Page not opened", "WAT -Feedback Fail");
					}
					ob.close();
					ob.switchTo().window(main);
					test.log(LogStatus.PASS, "WAT Proflie flyout  "+flyoutLinks[i]+" Link validation is Successful & Feedback Survey page Opened");
				}
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			ob.navigate().back();			
			logFailureDetails(test, "WAT Profile Flyouts links not responded", "WAT Profile flyouts Fail");
		}
	}
	/**
	 * Method for check all info should present under profile flyout
	 * @param draProfileFlyout
	 * @throws Exception, When flyout not having sufficient info
	 */
	public void validateWATProfileFlyout(String watProfileFlyout,ExtentTest test) throws Exception {
		String watProfile[]=watProfileFlyout.split("\\|");
		List<String> watFlyout=Arrays.asList(watProfile);
		List<String> profileFlyout= new ArrayList<String>();
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		BrowserWaits.waitTime(3);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		String profileTitle=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS).getText();
		String profileMetadata=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_METADATA_CSS).getText();
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account","Privacy","Terms of Use","Help","Feedback","Sign out");
		
		List<WebElement> watProfileFlyoutLinks=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS).findElements(By.tagName("a"));
		for(WebElement flyout:watProfileFlyoutLinks) {
			profileFlyout.add(flyout.getText());
		}
		logger.info("profile status-->"+profileMetadata);
		logger.info("profile title-->"+profileTitle);
		logger.info("profile flyout1 input-->"+watFlyout);
		logger.info("profile flyout2-->"+profileFlyout);
		
		boolean isImagePresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_HEADER_IMAGE_CSS).isEnabled();
		
		if (!((isImagePresent && StringUtils.isNotEmpty(profileTitle) && profileFlyout.containsAll(watFlyout))
				&& (StringUtils.isEmpty(profileMetadata) || StringUtils.isNotEmpty(profileMetadata)))) {
			//throw new Exception("Profile Flyout info not displayed");
			logFailureDetails(test, "Profile Flyout info not displayed", "Profile_flyout_info_not_displayed");
		}
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
	}
	
	
	/**
	 * Method for Click proifle title in Profile flyout
	 * @throws Exception
	 */
	public void clickProfileFlyout() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_FOOTER_LINKS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account","Privacy","Terms of Use","Feedback","Sign out");
	}
	
	/**
	 * Method for Click proifle title in Profile flyout
	 * @throws Exception
	 */
	public void clickProfileTitleLink() throws Exception {
		clickProfileFlyout();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile","Update","Cancel");
	}
	
	/**
	 * Method for Validate DRA Profile page
	 * @throws Exception,When Profile page not landing properly
	 */
	public void validateDRAProfilePageAndClose(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Profile","Update","Cancel");
		boolean isFirstNamePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS).isEnabled();
		boolean isLastNamePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS).isEnabled();
		boolean isRolePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS).isEnabled();
		boolean isPIPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PRIINS_CSS).isEnabled();
		boolean isCountryPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS).isEnabled();
		boolean isCancelPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_UPDATE_CSS).isEnabled();
		boolean isUpdatePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_CANCEL_CSS).isEnabled();
		boolean isImageEditPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_IMAGE_EDIT_CSS).isEnabled();
		boolean isImageDeletePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_IMAGE_DELETE_CSS).isDisplayed();
		
		if (!(isFirstNamePresent && isLastNamePresent && isRolePresent && isCountryPresent && isCancelPresent && isPIPresent
				&& isUpdatePresent && isImageEditPresent && isImageDeletePresent)) {
			//throw new Exception("Profile page not landing properly");
			logFailureDetails(test, "Profile page not landing properly", "profile_info_not_displayed");
		}
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_CANCEL_CSS);
		BrowserWaits.waitTime(2);
	}
	
	
	/**
	 * Method for Validate Account modal
	 * @throws Exception
	 */
	public void validateAccountLinkModalAndClose() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account","Connected Accounts");
		pf.getBrowserWaitsInstance(ob).waitUntilText("Last sign in:","Change password");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS);
		String accountText=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.MATCHING_STEAM_MODALTITLE_CSS).getText();
		if(!(accountText.equals("Account"))) {
			throw new Exception("Account modal is not displayed");
		}
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS);
		
		
	}
	
	//#################DRA-Profile flyout#################
	
	//************************Authoring Related *********************************
	/**
	 * Method to return comment count from article record view
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int getCommentCount() throws InterruptedException {
		BrowserWaits.waitTime(2);
		//Commented by KR
		waitForPageLoad(ob);
		//waitForAjax(ob);
		//scrollingToElementofAPage();
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString()),
				180);
		String commentSizeBeforeAdd = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString())).getText()
				.replaceAll(",", "").trim();
		return Integer.parseInt(commentSizeBeforeAdd);
	}
}
