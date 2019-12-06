package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class PUBLONSPage extends TestBase {

	public PUBLONSPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void loginPublonsAccont(String email, String pass) {
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).sendKeys(email);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS.toString())).sendKeys(email);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS.toString())));
	}
 //===================================================================================
	/**
	 * Method for Login shared platform application using FB valid login credentials
	 * 
	 * @param username
	 * @param pwd
	 * @throws InterruptedException
	 * @throws Exception,
	 *             When user is not able to login using FB
	 */
	public void loginWithFBCredentialsWithOutLinking(String username, String pwd) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_FB_PASSWORD_TEXT_BOX_NAME, pwd);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_FB_LOGIN_BUTTON_NAME);
			
}
	//===================================================================================
		/**
		 * Method for Login shared platform application using Gmail valid login credentials
		 * 
		 * @param username
		 * @param pwd
		 * @throws InterruptedException
		 * @throws Exception,
		 *             When user is not able to login using FB
		 */
		public void loginWithGmailCredentialsWithOutLinking(String username, String pwd) throws Exception {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_BUTTON_CSS);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_BUTTON_CSS);
			
			fluentwaitforElement(ob, By.name(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_EMAIL_TEXT_NAME.toString()), 60);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_EMAIL_TEXT_NAME, username);
			
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_NEXT_BUTTON_ID);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_NEXT_BUTTON_ID);
			
			fluentwaitforElement(ob, By.name(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_PASSWORD_TEXT_NAME.toString()), 60);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_PASSWORD_TEXT_NAME, pwd);
			
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_SIGNIN_BUTTON_NAME);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_SIGNIN_BUTTON_NAME);
			
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_ACCESS_BUTTON_ID);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_GMAIL_SIGN_IN_ACCESS_BUTTON_ID);
			
			

				
	}
}
