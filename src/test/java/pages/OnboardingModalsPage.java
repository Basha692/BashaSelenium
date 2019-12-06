package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * Class for perform Onboarding modals - Welcome Modal and Profile Modal
 * validations
 * 
 * @author UC202376
 *
 */
public class OnboardingModalsPage extends TestBase {
	
	static String firstName;
	static String lastName;
	static String role;
	static String metaData[];
	static String topicLists[];
	
	public OnboardingModalsPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method for click SignIn button
	 * @throws Exception, When login not happend
	 */
	public void clickLogin() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
	}

	
	/**
	 * Method 
	 * @throws Exception, 
	 */
	public void ENWSTeamLogin(String userName,String password) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("EndNote","Sign in");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS);
		
		pf.getBrowserActionInstance(ob).clear(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS,userName);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS,password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		
		try {
			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS);
			} catch (Exception e) {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		BrowserWaits.waitTime(4);
	}
	
	public void ENWSTeamLogin1(String userName, String password) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS);
		pf.getBrowserActionInstance(ob).clear(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS,userName);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS,password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		try {
			if(ob.findElements(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).size()!=0){
				ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).click();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		BrowserWaits.waitTime(4);
	}
	/**
	 * Method for Validate Onboarding modals,
	 * @throws Exception, When Onboarding modals are not present for first time users
	 */
	public void validateOnboardingModals() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(4);
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed"); 
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
		} catch (Exception e) {
			throw new Exception("Onboarding Modals are not displayed for First time user");
		}

	}
	
	/**
	 * Method for Validate Onboarding modals for second time logged user
	 * @throws Exception, When Onboarding modals are present for second time users
	 */
	public void validateOnboardingModalsForSecondTimeUser() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());
		if(!(onboardingStatus.size()==0)){
			throw new Exception("Onboarding modal are displaying for second time logged user also if user not onboarded successfully first time");
		}
			
	}
	
	/**
	 * Method for Close[X] Welcome onboarding modal ,
	 * @throws Exception, When Welcome onboarding modal not closed
	 */
	public void welcomeOnboardingModalClose() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CLOSE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
		} catch (Exception e) {
			throw new Exception("welcome onboarding modal not closed");
		}

	}
	
	/**
	 * Method for Close[X] profile onboarding modal ,
	 * @throws Exception, When profile onboarding modal not closed
	 */
	public void profileOnboardingModalClose() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CLOSE_CSS);
			BrowserWaits.waitTime(2);
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CLOSE_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
		} catch (Exception e) {
			throw new Exception("Onboarding Modals are not displayed for First time user");
		}

	}
	
	/**
	 * Method for click profile onboarding modal outside,
	 * @throws Exception, When profile onboarding modal not closed
	 */
	public void clickProfileOnboardingModalOutside() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			Dimension dimesions=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS).getSize();
			logger.info("Width : "+dimesions.width);
			logger.info("Height : "+dimesions.height);
			int x=dimesions.width;
			int y=dimesions.height;
			Actions builder = new Actions(ob);  
			builder.moveToElement(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS), x+150, y).build().perform();
			builder.click().build().perform();
			BrowserWaits.waitTime(4);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
		} catch (Exception e) {
			throw new Exception("Onboarding Profile modal outside click not happend");
		}

	}
	
	/**
	 * Method for click Welcome onboarding modal outside,
	 * @throws Exception, When profile onboarding modal not closed
	 */
	public void clickWelcomeOnboardingModalOutside() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			
			Dimension dimesions=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS).getSize();
			logger.info("Width : "+dimesions.width);
			logger.info("Height : "+dimesions.height);
			int x=dimesions.width;
			int y=dimesions.height;
			Actions builder = new Actions(ob);  
			builder.moveToElement(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS), x+150, y).build().perform();
			builder.click().build().perform();
			BrowserWaits.waitTime(4);
			
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
		} catch (Exception e) {
			throw new Exception("Onboarding Welcome modal outside click not happend");
		}

	}
	
	/**
	 * Method for Validate Welcome Onboarding modal,
	 * @throws Exception, When Welcome modal not present
	 */
	public void validateWelcomeOnboardingModal() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			List<WebElement> images=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_FEATURE_IMAGES_CSS);
			if(!(images.size() == 3)) {
				throw new Exception("user-onboarding-modal__feature images are not displayed");
			}
			
		} catch (Exception e) {
			throw new Exception("Onboarding Welcome Modal is not displayed for First time user");
		}

	}
	
	/**
	 * Method for Validate Welcome Onboarding modal Recommended people section,
	 * @throws Exception, When Welcome modal not present
	 */
	public void validateWelcomeOnboardingModalRecommendedPeople() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size()); 

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow"); 
			BrowserWaits.waitTime(8);//giving more wait to load Recommended people section
			List<WebElement> recommended_people=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_RECOMMENDED_PEOPLE_CSS).findElements(By.tagName("img"));
			logger.info("Recommended_people length-->"+recommended_people.size());
			if(!(recommended_people.size() == 8)) {
				throw new Exception("Recommended People count should 8 in Welcome Modal");
			}
			recommended_people.get(0).click();
			String follow_status_before=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_RECOMMENDED_PEOPLE_CSS).findElement(By.tagName("span")).getAttribute("class");
			logger.info("follow_status before-->"+follow_status_before);
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_RECOMMENDED_PEOPLE_CSS).findElement(By.tagName("span")).click();
			String follow_status_after=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_RECOMMENDED_PEOPLE_CSS).findElement(By.tagName("span")).getAttribute("class");;
			BrowserWaits.waitTime(2);
			logger.info("follow_status after-->"+follow_status_after);
			if(follow_status_before.equalsIgnoreCase(follow_status_after)) {
				throw new Exception("user is not able to follow/unfollw from Recommended people section");
			}
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CLOSE_CSS);
			
		} catch (Exception e) {
			throw new Exception(
					"Recommended People count should 8 in Welcome Modal|user is not able to follow/unfollw from Recommended people section");
		}

	}
	
	/**
	 * Method for Validate profile onboarding modal ,
	 * @throws Exception, When profile onboarding modal not having profile info
	 */
	public void validateProfileOnboardingModal() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			profileOnboardingModal();
			
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(2);
			
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
		} catch (Exception e) {
			throw new Exception("Profile Onboarding Modals are not displayed for First time user");
		}
	}
	
	
	/**
	 * Method for Validate profile name profile onboarding modal ,
	 * @throws Exception, When profile name is not updated 
	 */
	public void updateNameOnProfileOnboardingModal(String profileFieldsDefaultText) throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());
		List<String> defaultText= new ArrayList<String>();
		String placeholders[]=profileFieldsDefaultText.split("\\|");
		List<String> profileFieldsPlaceholders=Arrays.asList(placeholders);
		
		try {
			profileOnboardingModal();
			
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
			
			defaultText.add(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS).getAttribute("placeholder"));
			defaultText.add(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS).getAttribute("placeholder"));
			defaultText.add(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS).getAttribute("placeholder"));
			defaultText.add(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS).getAttribute("placeholder"));
			defaultText.add(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS).getAttribute("placeholder"));
			
			logger.info("placeholder input text-->"+profileFieldsPlaceholders);
			logger.info("placeholder app text-->"+defaultText);
			if(!(profileFieldsPlaceholders.equals(defaultText))) {
				throw new Exception("Profile info/metadata fields doesn't have any placeholder values");
			}
			
			 firstName=RandomStringUtils.randomAlphabetic(10);
			 lastName=RandomStringUtils.randomAlphabetic(20);
			
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS, firstName);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS,lastName);
			
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(2);
			
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
			pf.getHFPageInstance(ob).clickOnProfileLink();
			
			boolean isProfileEditable=pf.getProfilePageInstance(ob).isProfileIncomplete();
			logger.info("is Profile editable-->"+isProfileEditable);
			
			if(isProfileEditable) {
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
				BrowserWaits.waitTime(3);
			}
			String title = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS).getText();
			logger.info("Profile title-->"+title);
			if(!(title.contains(firstName) && title.contains(lastName))) {
				throw new Exception("profile onboarding first name and last name is not displayed in profile page");
			}
			
		} catch (Exception e) {
			throw new Exception("Profile Onboarding Modals are not displayed for First time user");
		}

	}
	
	
	
	/**
	 * Method for Validate profile name profile onboarding modal ,
	 * @throws Exception, When profile name is not updated 
	 */
	public void updateMetadataOnProfileOnboardingModal(String profileMetaData,String topics) throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());
		metaData=profileMetaData.split("\\|");
		
		try {
			profileOnboardingModal();
			
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
			
			
			firstName=RandomStringUtils.randomAlphabetic(10);
			lastName=RandomStringUtils.randomAlphabetic(20);
			role=RandomStringUtils.randomAlphabetic(5);
			
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_FIRST_NAME_CSS, firstName);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_LAST_NAME_CSS,lastName);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_ROLE_CSS,role);
			
			////select primary institution
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_PI_CSS,
					metaData[0]);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
					OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS);
			BrowserWaits.waitTime(4);
			List<WebElement> piTypeaheads = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).findElements(By.tagName("li"));
			for (WebElement typeAhead : piTypeaheads) {
				if (StringUtils.containsIgnoreCase(typeAhead.getText(), metaData[0].trim())) {
					typeAhead.click();
					BrowserWaits.waitTime(2);
					break;
				}
			}
			
			//select country
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_COUNTRY_CSS,
					metaData[1]);
			BrowserWaits.waitTime(4);
			List<WebElement> countyTypeaheads = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PI_TYPEAHEAD_CSS).get(1)
					.findElements(By.tagName("li"));
			for (WebElement typeAhead : countyTypeaheads) {
				if (typeAhead.getText().equalsIgnoreCase(metaData[1])) {
					//typeAhead.click();
					jsClick(ob, typeAhead);
					BrowserWaits.waitTime(2); 
					break;
				}
			}
			
			//Add a Topic
			List<WebElement> addedTopics = pf.getBrowserActionInstance(ob).getElements(
					OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
			if (addedTopics.size() > 0) {
				for (WebElement addedTopic : addedTopics) {
					addedTopic.click();
					BrowserWaits.waitTime(2);
				}
			}
			topicLists = topics.split("\\|");
			for (String topicList : topicLists) {
				for(int i=0;i<topicList.length();i++){
					char topic=topicList.charAt(i);
					pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_CSS,
							String.valueOf(topic));
					BrowserWaits.waitTime(2);
			}
				
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
				List<WebElement> topicTypeahead = pf.getBrowserActionInstance(ob).getElements(
						OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ADD_TOPIC_TYPEAHEAD_CSS);
				BrowserWaits.waitTime(2);
				pf.getBrowserActionInstance(ob).jsClick(
						topicTypeahead.get(Integer.parseInt(RandomStringUtils.randomNumeric(1))));
				// topicTypeahead.get(Integer.parseInt(RandomStringUtils.randomNumeric(1))).click();
				BrowserWaits.waitTime(2);
			}

			List<WebElement> newlyAddedTopics = pf.getBrowserActionInstance(ob).getElements(
					OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
			if (!(newlyAddedTopics.size() == topicLists.length)) {
				throw new Exception("Topics not added for Interests and Skills");
			}
			
			
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(8);
			
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
		} catch (Exception e) {
			throw new Exception("Profile Onboarding Modals are not displayed for First time user");
		}

	}
	
	
	/**
	 * Method for Validate profile onboarding modal ,
	 * @throws Exception, When profile onboarding modal not having profile info
	 */
	public void validateProfileOnboardingModalDataOnProfilePage() throws Exception {
		try{
			
			pf.getHFPageInstance(ob).clickOnProfileLink();
		
			boolean isProfileEditable=pf.getProfilePageInstance(ob).isProfileIncomplete();
			logger.info("is Profile editable-->"+isProfileEditable);
			
			if(isProfileEditable) {
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_UPDATE_CSS);
				BrowserWaits.waitTime(6);
			}
			
			List<String> profileData = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata();
			
			logger.info("profile metadata"+profileData);
			
			boolean isMetadataUpdated=(profileData.contains(firstName+" "+lastName) && profileData.contains(role)
						&& profileData.contains(metaData[0]) && profileData.contains(metaData[1]));
			
			logger.info("is profile data updated -->"+isMetadataUpdated);
			List<WebElement> newlyAddedTopics = pf.getBrowserActionInstance(ob).getElements(
					OnePObjectMap.HOME_PROJECT_NEON_PROFILE_REMOVE_TOPIC_CSS);
			logger.info("Topics size-->"+newlyAddedTopics.size());
			if (!((newlyAddedTopics.size() == topicLists.length) && isMetadataUpdated)) {
				throw new Exception("Profile info/metadat and Topics are not updated");
			}
		
		  } catch (Exception e) {
			throw new Exception("Profile Onboarding Modals are not displayed for First time user");
		  }
	}
	
	/**
	 * Method for Validate profile onboarding modal ,
	 * @throws Exception, When profile onboarding modal not having profile info
	 */
	public void profileOnboardingModal() throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow","Next");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_UPLOAD_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_DELETE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_EDIT_CSS);
			
			pf.getBrowserWaitsInstance(ob).waitUntilText("Let’s learn about you","Interests and Skills","Join");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Add a Topic","You can always complete your profile later from  your Profile page.");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Note:", "Your name and any additional information you add will", "be visible to others.");
	}
	
	
	/**
	 * Method for Validate Onboarding modals,
	 * @throws Exception, When Onboarding modals are not present for first time users
	 */
	public void validateENWToNeonOnboardingModals() throws Exception {

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HOME_HEADER_NEON_PLINK);
		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("We’re expanding EndNote -- making it easier for you to:");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(4);
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Trending on Neon", "Posts","Articles","Topics","Recommended");
			
		} catch (Exception e) {
			throw new Exception("Onboarding Modals are not displayed for First time user");
		}

	}
	
	
	/**
	 * Method 
	 * @throws Exception, 
	 */
	public void ENWToNeonNavigationScreenForMarketTestUser() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Thomson Reuters","Project Neon");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_PROFILE_IMAGE_XPATH);
		
	}
	
	/**
	 * Method 
	 * @throws Exception, 
	 */
	public void validateENWProfileFlyout(String enwProfileFlyout) throws Exception {
		String enwProfile[]=enwProfileFlyout.split("\\|");
		List<String> enwFlyout=Arrays.asList(enwProfile);
		List<String> profileFlyout= new ArrayList<String>();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HOME_PROFILE_IMAGE_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_HEADER_CSS);
		String profileMetadata=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_HEADER_CSS).findElement(By.tagName("span")).getText();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_LINKS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Account","Privacy","Terms of use","Feedback","Sign out");
		List<WebElement> enwProfileFlyoutLinks=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_LINKS_CSS).findElements(By.tagName("a"));
		for(WebElement flyout:enwProfileFlyoutLinks) {
			profileFlyout.add(flyout.getText());
		}
		logger.info("profile status-->"+profileMetadata.isEmpty());
		logger.info("profile flyout1 input-->"+enwFlyout);
		logger.info("profile flyout2-->"+profileFlyout);
		
		if(!(!profileMetadata.isEmpty() && profileFlyout.containsAll(enwFlyout))) {
			throw new Exception("Profile Flyout info not displayed in ENW");
		}
	}
	
	
	/**
	 * Method ,
	 * @throws Exception, 
	 */
	public void validateENWToNeonOnboardingModalsUsingProfileLink() throws Exception {

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_HEADER_CSS).findElement(By.tagName("img")).click();
		BrowserWaits.waitTime(8);
		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("We’re expanding EndNote -- making it easier for you to:");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow"); 
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(8);
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Activity", "Interests and Skills", "Posts", "Comments",
					"Followers", "Following", "Watchlists", "Add a Topic");
			logger.info("Navigate to ENW-->"+pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
			Assert.assertEquals("< Back to EndNote", pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
		} catch (Exception e) {
			throw new Exception("Onboarding Modals are not displayed for First time user using Account link for Market Test group");
		}

	}
	
	
	/**
	 * Method ,
	 * @throws Exception, 
	 */
	public void validateENWToNeonOnboardingModalsUsingAccountLink() throws Exception {

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_ACCOUNT_LINK);
		BrowserWaits.waitTime(8);
		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		try {
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("We’re expanding EndNote -- making it easier for you to:");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Follow and discuss research", "Connect with researchers",
					"Discover articles, patents, and community contributions");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Recommended people to follow");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			BrowserWaits.waitTime(4);
			List<WebElement> onboarding_modals=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
			logger.info("onboarding_modals size-->"+onboarding_modals.size());
			if(!(onboarding_modals.size()==0)) {
				throw new Exception("Onboarding Modals are not closed");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK);
			logger.info("Navigate to ENW-->"+pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
			Assert.assertEquals("< Back to EndNote", pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
			pf.getBrowserWaitsInstance(ob).waitUntilText("Account", "Manage accounts", "Communications", "View additional email preferences");
			
		} catch (Exception e) {
			throw new Exception("Onboarding Modals are not displayed for First time user login with Account link for Market test group");
		}

	}
	
	
	/**
	 * Method ,
	 * @throws Exception, 
	 */
	public void validateENWToNeonUsingFlyoutLinks(String enwProfileFlyout,String link) throws Exception {
		List<WebElement> enwProfileFlyoutLinks=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_LINKS_CSS).findElements(By.tagName("a"));
			for(WebElement flyout:enwProfileFlyoutLinks) {
				if(flyout.getText().equalsIgnoreCase(link)){
					flyout.click();
					break;
				}
			}
		BrowserWaits.waitTime(2);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK);
		logger.info("Navigate to ENW-->"+pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
		Assert.assertEquals("< Back to EndNote", pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK).getText());
		if(link.equalsIgnoreCase("Feedback")) {
			pf.getBrowserWaitsInstance(ob).waitUntilText("Feedback or questions about EndNote?", "Send feedback to the EndNote team", "Report a problem or submit a support request");
		} 
		
		if(link.equalsIgnoreCase("Privacy")) {
			pf.getBrowserWaitsInstance(ob).waitUntilText("Privacy Statement", "Scope", "Highlights", "Third party services");
		}
		
		if(link.equalsIgnoreCase("Terms of Use")) {
			pf.getBrowserWaitsInstance(ob).waitUntilText("Terms of Use", "These Terms of Use shall govern your use of the online service known as");
		}
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_LINK);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
		validateENWProfileFlyout(enwProfileFlyout);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HOME_PROFILE_FLYOUT_SIGNOUT_LINK);
		pf.getBrowserWaitsInstance(ob).waitUntilText("EndNote","Sign in");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
	}
	
	/**
	 * Method for Validate profile image on profile onboarding modal ,
	 * @throws Exception, When profile image not uploaded succesfully
	 */
	public void validateImageUploadOnProfileModal(String imagePath) throws Exception {

		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		/*try {*/
			profileOnboardingModal();
			//Profile image modal
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_EDIT_CSS);
			//pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_BROWSE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_SELECT_IMAGE_LINK);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_CLOSE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Profile image","Select an image","(at least 120 px by 120 px and less than 8 MB).");
			//validate update button status
			validateProfilePicUpdateButtonStatus();
			//upload new profile image
			uploadProfileImage(imagePath);
			//wait for profile modal 
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_DELETE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilText("Let’s learn about you","Interests and Skills","Join");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Add a Topic","You can always complete your profile later from  your Profile page.");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Note:", "Your name and any additional information you add will", "be visible to others.");
			
		/*} catch (Exception e) {
			throw new Exception("Profile Image upload not done in Profile modal");
		}*/

	}
	
	/**
	 * Method for Validate profile image Delete on profile onboarding modal ,
	 * @throws Exception, When profile image not Deleted succesfully
	 */
	public void validateImageDeleteOnProfileModal() throws Exception {
		String profileImageTimeStamp = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_UPLOAD_CSS)
				.getAttribute("src");

		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_DELETE_CSS);
		BrowserWaits.waitTime(8);
		String profileImageTimeStampAfterUpload = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_UPLOAD_CSS)
				.getAttribute("src");
		
		if(profileImageTimeStamp.equalsIgnoreCase(profileImageTimeStampAfterUpload)) {
			throw new Exception("Profile image not Deleted successfully On Profile Modal");
		}
		
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
		
	}
	
	public void validateProfilePicUpdateButtonStatus() throws Exception {
		boolean proiflePicUpdateButton=pf.getProfilePageInstance(ob).getProfilePicModalWindowUpdateButtonStatus();
		logger.info("profile pic update button status-->"+proiflePicUpdateButton);
		if(proiflePicUpdateButton) {
			throw new Exception("profile pic update button should be disabled by default");
		}
	}
	
	/**
	 * Method for upload new profile image
	 * 
	 * @throws Exception, When profile image not able to upload	
	 */
	public void uploadProfileImage(String imagePath) throws Exception {
		String profileImageTimeStamp = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_UPLOAD_CSS)
				.getAttribute("src");
		logger.info("image timestamp before upload-->"+profileImageTimeStamp);
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_SELECT_IMAGE_LINK);
		BrowserWaits.waitTime(4);
		Runtime.getRuntime().exec("autoit_scripts/imageUpload2.exe"+" "+imagePath);
		BrowserWaits.waitTime(4);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_UPDATE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_PICTURE_MODAL_WINDOW_UPDATE_CSS);
		//giving more wait to update the profile image
		BrowserWaits.waitTime(8);
		
		String profileImageTimeStampAfterUpload = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_IMAGE_UPLOAD_CSS)
				.getAttribute("src");
		logger.info("image timestamp After upload-->"+profileImageTimeStampAfterUpload);
		
		if(profileImageTimeStamp.equalsIgnoreCase(profileImageTimeStampAfterUpload)) {
			throw new Exception("Profile image not uploaded successfully On Profile Modal");
		}
	}
		
}
