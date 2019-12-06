package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.BrowserWaits;
import util.OnePObjectMap;
import base.TestBase;

/**
 * This class contains all the method related to account page
 * @author uc205521
 *
 */
public class ENWReferencePage extends TestBase {

	PageFactory pf;

	public ENWReferencePage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}



	/**
	 * Method to delete record in  Endnote from Neon
	 * 
	 * 
	 */
	public void clearRecordEndnote() {
		
		ob.findElement(By.xpath("//a[@class='ne-app-switcher-flyout__toggle']/i")).click();
		
		ob.findElement(By.xpath("//li[@class='ne-app-switcher-flyout__list-item'][1]/a")).click();
		ob.findElement(By.xpath("//div[@id='idFolderLink_1']/a")).click();
		ob.findElement(By.cssSelector("input[id='idCheckAllRef']")).click();
		ob.findElement(By.cssSelector("input[id='idDeleteTrash']")).click();
		
		
	}
	
	
	//ENW FB sign in 
	public void loginWithENWFBCredentials(String username, String pwd) throws InterruptedException, Exception {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString())).click();

		signInToFacebook(username, pwd);
		closeOnBoardingModal();
	}
	public void signInToFacebook(String username, String pwd) throws InterruptedException {
		waitForElementTobeVisible(ob, By.name(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString()), 30);

		// Verify that existing LI user credentials are working fine
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString())).sendKeys(username);
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_FB_PASSWORD_TEXT_BOX_NAME.toString())).sendKeys(pwd);
		BrowserWaits.waitTime(2);
		   //ob.findElement(By.xpath(OR.getProperty("ENW_FB_LOGIN_BUTTON"))).click();
		ob.findElement(By.xpath(OnePObjectMap.ENW_FB_LOGIN_BUTTON_XPATH.toString())).click();
		BrowserWaits.waitTime(2);
		  //ob.findElement(By.xpath(OR.getProperty("ENW_CONTINUE_BUTTON"))).click();
		//ob.findElement(By.xpath(OnePObjectMap.ENW_CONTINUE_BUTTON_XPATH.toString())).click();
	}
	
	// Did you know link modal
	
	public void didYouKnow(String passWord ) throws Exception{
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DID_YOU_KNOW_MODAL_STEAMPW_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DID_YOU_KNOW_MODAL_STEAMPW_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.DID_YOU_KNOW_MODAL_STEAMPW_CSS, passWord);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DID_YOU_KNOW_LETS_GO_BUTTON_XPATH);
		
		//ob.findElement(By.xpath("//input[@name='steamPassword']")).click();	
		//BrowserWaits.waitTime(3);
		//ob.findElement(By.xpath("//input[@name='steamPassword']")).sendKeys(passWord);
		//LOGIN.getProperty("Password19")
		//BrowserWaits.waitTime(3);
    	//ob.findElement(By.xpath(OnePObjectMap.DID_YOU_KNOW_LETS_GO_BUTTON_XPATH.toString())).click();
	//DID_YOU_KNOW_LETS_GO_BUTTON
		//BrowserWaits.waitTime(3);
		//Clicking Continuous button
		
	}
	
	//Arvind- Yes,I have an account module
	public void yesAccount(String userName,String passWord) throws InterruptedException
	{
		BrowserWaits.waitTime(2);
		// Clicking Yes,I have an account button
		waitForElementTobeClickable(ob,By.cssSelector(OnePObjectMap.ENW_YES_I_HAVE_AN_ACCOUNT_BUTTON_CSS.toString()),30);
		ob.findElement(By.cssSelector(OnePObjectMap.ENW_YES_I_HAVE_AN_ACCOUNT_BUTTON_CSS.toString())).click();
		BrowserWaits.waitTime(2);
		//Enter arvindkandaswamy STeAM credentials
		ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_EMAIL_XPATH.toString())).click();
		ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_EMAIL_XPATH.toString())).sendKeys(userName);
		ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_PASSWORD_XPATH.toString())).click();
		ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_PASSWORD_XPATH.toString())).sendKeys(passWord);
		//Clicking the "Done" button
		//ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_DONE_BUTTON_XPATH.toString())).click();
		
		jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_LINK_ACCOUNTS_DONE_BUTTON_XPATH.toString())));
		}
	//LOGIN.getProperty("UserName19"),  LOGIN.getProperty("Password19")
	//Arvind- Clicking the Accounts button on ENW Profile-Flyout 
		public void clickAccount() throws Exception	{
			/*try {
				BrowserWaits.waitTime(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FB_PROFILE_IMGCIRCLE_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_IMGCIRCLE_XPATH.toString())).click();
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FB_PROFILE_IMGCIRCLE_ACCOUNT_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_IMGCIRCLE_ACCOUNT_XPATH.toString())).click();
			//BrowserWaits.waitTime(3)
			//closeOnBoardingModal();
			
			/*} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//validateLinkedAccounts(2, accountType);
		}
		
		
		// ENW LinkedIn Login
		public void loginWithENWLnCredentials(String username, String pwd) throws InterruptedException, Exception {

			//waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_LI_SIGN_IN_BUTTON_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_LI_SIGN_IN_BUTTON_CSS.toString())).click();

			signInToENWLinkedIn(username, pwd);
			closeOnBoardingModal();
		}

		public void signInToENWLinkedIn(String username, String pwd) throws InterruptedException {
			waitForElementTobeVisible(ob, By.name(OnePObjectMap.LOGIN_PAGE_LI_EMAIL_TEXT_BOX_ID.toString()), 30);

			// Verify that existing LI user credentials are working fine
			ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_EMAIL_TEXT_BOX_ID.toString())).sendKeys(username);
			ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_PASSWORD_TEXT_BOX_ID.toString())).sendKeys(pwd);
			// BrowserWaits.waitTime(2);
			ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_ALLOW_ACCESS_BUTTON_ID.toString())).click();
			BrowserWaits.waitTime(2);
			//ob.findElement(By.xpath(OR.getProperty("ENW_CONTINUE_BUTTON"))).click();
		}
		
		
		// ENW Back to Endnote button +  Logout
		//modified
		public void logout() throws InterruptedException
		{
//			ob.findElement(By.xpath(OnePObjectMap.ENW_BACK_TO_ENDNOTE_BUTTON_XPATH.toString())).click();
//			BrowserWaits.waitTime(3);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_IMGCIRCLE_XPATH.toString())).click();
			BrowserWaits.waitTime(3);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH.toString())).click();
		}
		
	//Closing the onboarding modal 
	public void closeOnBoardingModal() throws Exception, InterruptedException {
		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		if (onboardingStatus.size() == 1) {

			String header = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_TEXT_CSS).getText();
			if (header.contains("Hello")) {
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
				BrowserWaits.waitTime(4);
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			}
		}
	}

}
