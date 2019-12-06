package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;

public class EnwReference extends TestBase {
	PageFactory pf;

	public EnwReference(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void enwContinue() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		// jsClick(ob,
		// ob.findElement(By.className(OR.getProperty("Enw_continue_button"))));
		BrowserWaits.waitTime(5);
	}

	public void logout() throws Exception {

		jsClick(ob, ob.findElement(By.xpath(OR.getProperty("header_label"))));
		Thread.sleep(5000);
		jsClick(ob, ob.findElement(By.xpath(OR.getProperty("signOut_link"))));
	}
	// ENW FB credentials

	public void loginWithFBCredentialsENW(WebDriver ob, String username, String pwd)
			throws InterruptedException, Exception {

		BrowserWaits.waitTime(3);
		waitForElementTobeVisible(ob, By.cssSelector(OR.getProperty("FB_login_button")), 30);
		ob.findElement(By.cssSelector(OR.getProperty("FB_login_button"))).click();

		signInToFacebookENW(ob, username, pwd);
		BrowserWaits.waitTime(3);
	}

	public void signInToFacebookENW(WebDriver ob, String username, String pwd) throws InterruptedException {
		waitForElementTobeVisible(ob, By.name(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString()), 30);

		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString())).sendKeys(username);
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_FB_PASSWORD_TEXT_BOX_NAME.toString())).sendKeys(pwd);
		// ob.findElement(By.name(OnePObjectMap.ENW_FB_LOGIN_BUTTON_XPATH.toString())).click();
		// BrowserWaits.waitTime(2);

		jsClick(ob, ob.findElement(By.cssSelector("label#loginbutton")));
		BrowserWaits.waitTime(2);
		// Thread.sleep(3000);

	}

	// ENW Did you know link modal
	public void didYouKnow() throws InterruptedException {

		BrowserWaits.waitTime(3);
		// ob.findElement(By.xpath("//input[@class='wui-input-with-label__input
		// ng-pristine ng-untouched ng-invalid
		// ng-invalid-required']")).sendKeys(LOGIN.getProperty("Password19"));
		ob.findElement(By
				.xpath("//input[@class='wui-input-with-label__input ng-pristine ng-untouched ng-invalid ng-invalid-required']"))
				.sendKeys("Kanda@123");
		BrowserWaits.waitTime(3);
		ob.findElement(By.xpath(OR.getProperty("DID_YOU_KNOW_LETS_GO_BUTTON"))).click();
		// DID_YOU_KNOW_LETS_GO_BUTTON
		BrowserWaits.waitTime(3);
		// Clicking Continuous button
		ob.findElement(By.className("btn-common")).click();
	}

	// ENW logout
	public void logOutApp1() throws InterruptedException {
		BrowserWaits.waitTime(10);
		ob.findElement(By.xpath(OR.getProperty("ENE_FB_PROFILE_FLYOUT_CIRCLE"))).click();
		BrowserWaits.waitTime(3);
		ob.findElement(By.xpath(OR.getProperty("ENW_FB_SIGNOUT"))).click();

		// jsClick(ob,
		// ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS_ENW.toString())));
		// BrowserWaits.waitUntilText("Sign out");
		// jsClick(ob,
		// ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK.toString())));
		// BrowserWaits.waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

	}

	public void clickOnProjectNeonLink() {
		// TODO Auto-generated method stub
		WebElement navigate_to_header = ob
				.findElement(By.partialLinkText((OnePObjectMap.ENW_HOME_HEADER_NEON_PLINK.toString())));
		jsClick(ob, navigate_to_header);

	}

	public boolean validateNavigationToEnw() throws Exception {
		boolean endnote_logo = checkElementIsDisplayed(ob, By.cssSelector(OnePObjectMap.ENDNOTE_LOGO_CSS.toString()));
		return endnote_logo;

	}

}
