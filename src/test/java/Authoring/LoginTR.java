package Authoring;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserAction;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * class for Project Neon Login with TR Credentials
 */
public class LoginTR extends TestBase {

	static int time = 15;
	BrowserWaits browserWait;
	BrowserAction browserAction;

	final WebDriver ob;

	public LoginTR(WebDriver ob) {
		this.ob = ob;
		browserWait = new BrowserWaits(ob);
		browserAction = new BrowserAction(ob);

	}

	/**
	 * Method for wait TR Home Screen
	 * 
	 * @throws Exception
	 */
	public void waitForTRHomePage() throws Exception {
		//fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS.toString()), 100);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Project Neon");
	}

	/**
	 * Method for enter Application Url and enter Credentials
	 * 
	 * @throws InterruptedException
	 */
	public void enterTRCredentials(String userName, String password) throws InterruptedException {
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).sendKeys(userName);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS.toString())).sendKeys(password);
	}

	public void clickLogin() throws Exception {
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS.toString()), 100);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);

		//closeOnBoardingModal();
		try {
			ob.findElement(By.xpath("//h3[@class='wui-modal__title']")).isDisplayed();
			ob.findElement(By.xpath("//a[text()='Not now']")).click();
		} catch (Exception e) {
			System.out.println("Loging in to Neon without linking popup");
		}
	}

	public void closeOnBoardingModal() throws Exception, InterruptedException {
       // waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS.toString()), 200);
		List<WebElement> onboardingStatus = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_MODAL_CSS);
		logger.info("onboarding status-->" + onboardingStatus.size());

		if (onboardingStatus.size() == 1) {

			String header = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_TEXT_CSS).getText();
			if (header.contains("Hello")) {
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_WELCOME_MODAL_CSS);
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
				pf.getBrowserActionInstance(ob)
						.scrollToElement(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_ONBOARDING_PROFILE_MODAL_CSS);
				BrowserWaits.waitTime(4);
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS);
			}
		}
	}

	public void searchArticle(String article) throws InterruptedException {
		ob.findElement(By.cssSelector(OR.getProperty("tr_search_box_css"))).sendKeys(article);
		ob.findElement(By.cssSelector("div[class='ne-main-nav'] button[title='Search'] i[class='fa fa-search']"))
				.click();
		waitForAjax(ob);
		waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchResults_links")), 90);
	}

	public void chooseArticle(String linkName) throws InterruptedException {
		BrowserWaits.waitForAllElementsToBePresent(ob, By.xpath(OR.getProperty("searchResults_links")), 180);
		jsClick(ob, ob.findElement(By.xpath(OR.getProperty("searchResults_links"))));
	}

	public void waitUntilTextPresent(String locator, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(ob, time);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(locator), text));
		} catch (TimeoutException e) {
			throw new TimeoutException("Failed to find element Locator , after waiting for " + time + "ms");
		}
	}

	public void logOutApp() throws Exception {
		//BrowserWaits.waitTime(10);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
		browserWait.waitUntilText("Sign out");
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_XPATH.toString())));
		// browserWait.waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

	}

	public void loginWithLinkedInCredentials(String username, String pwd) throws InterruptedException, Exception {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_LI_SIGN_IN_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_LI_SIGN_IN_BUTTON_CSS.toString())).click();

		signInToLinkedIn(username, pwd);
		closeOnBoardingModal();
	}

	public void signInToLinkedIn(String username, String pwd) throws Exception {
		waitForElementTobeVisible(ob, By.name(OnePObjectMap.LOGIN_PAGE_LI_EMAIL_TEXT_BOX_ID.toString()), 30);

		// Verify that existing LI user credentials are working fine
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_EMAIL_TEXT_BOX_ID.toString())).sendKeys(username);
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_PASSWORD_TEXT_BOX_ID.toString())).sendKeys(pwd);
		// BrowserWaits.waitTime(2);
		ob.findElement(By.name(OnePObjectMap.LOGIN_PAGE_LI_ALLOW_ACCESS_BUTTON_ID.toString())).click();
		try {
			ob.findElement(By.xpath("//h3[@class='wui-modal__title']")).isDisplayed();
			ob.findElement(By.xpath("//a[text()='Not now']")).click();
		} catch (Exception e) {
			System.out.println("Loging in to Neon without linking popup");
		}
	}

	/**
	 * Method for Login Neon application using FB valid login credentials
	 * 
	 * @param username
	 * @param pwd
	 * @throws InterruptedException
	 * @throws Exception,
	 *             When user is not able to login using FB
	 */
	public void loginWithFBCredentials(String username, String pwd) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS);
		signInToFacebook(username, pwd);
		closeOnBoardingModal();
	}

	public void loginWithFBCredentials1(String username, String pwd) throws InterruptedException, Exception {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString())).click();
		signInToFacebook(username, pwd);
		closeOnBoardingModal();
	}

	/**
	 * Method for Login with FB login credentials
	 * 
	 * @param username
	 * @param pwd
	 * @throws Exception,
	 *             not able to login FB account
	 */
	public void signInToFacebook(String username, String pwd) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_FB_PASSWORD_TEXT_BOX_NAME, pwd);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_FB_LOGIN_BUTTON_NAME);
		BrowserWaits.waitTime(4);
	}

	public void loginWithFBCredentials(WebDriver driver, String username, String pwd) {

		waitForElementTobeVisible(driver, By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString()),
				30);
		driver.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_FB_SIGN_IN_BUTTON_CSS.toString())).click();

		waitForElementTobeVisible(driver, By.id(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString()), 30);

		// Verify that existing LI user credentials are working fine
		driver.findElement(By.id(OnePObjectMap.LOGIN_PAGE_FB_EMAIL_TEXT_BOX_NAME.toString())).sendKeys(username);
		driver.findElement(By.id(OnePObjectMap.LOGIN_PAGE_FB_PASSWORD_TEXT_BOX_NAME.toString())).sendKeys(pwd);
		// BrowserWaits.waitTime(2);
		driver.findElement(By.name(OnePObjectMap.LOGIN_PAGE_FB_LOGIN_BUTTON_NAME.toString())).click();
	}

	public static WebDriver launchBrowser() throws Exception {
		WebDriver driver = null;
		logger.info("Env status-->" + StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER")));
		if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setBrowserName(System.getenv("SELENIUM_BROWSER"));
			logger.info("Selenium Browser Name-->" + System.getenv("SELENIUM_BROWSER"));
			desiredCapabilities.setVersion(System.getenv("SELENIUM_VERSION"));
			logger.info("Selenium Version-->" + System.getenv("SELENIUM_VERSION"));
			logger.info("Selenium Plaform-->" + System.getenv("SELENIUM_PLATFORM"));
			desiredCapabilities.setCapability(CapabilityType.PLATFORM, System.getenv("SELENIUM_PLATFORM"));
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true); //
			desiredCapabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS, true);
			driver = new RemoteWebDriver(
					new URL("http://mohana-yalamarthi:4cc2c2ac-35f6-4cc3-a751-9c6758fffe96@ondemand.saucelabs.com:80/wd/hub"),
					desiredCapabilities);
			String waitTime = CONFIG.getProperty("defaultImplicitWait");
			String pageWait = CONFIG.getProperty("defaultPageWait");
			driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			try {
				driver.manage().timeouts().implicitlyWait(Long.parseLong(pageWait), TimeUnit.SECONDS);
			} catch (Throwable t) {
				logger.info("Page Load Timeout not supported in safari driver");
			}
			// else part having local machine configuration
		} else {
			if (CONFIG.getProperty("browserType").equals("FF")) {
				driver = new FirefoxDriver();
			} else if (CONFIG.getProperty("browserType").equals("IE")) {
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
				driver = new InternetExplorerDriver(capabilities);
			} else if (CONFIG.getProperty("browserType").equalsIgnoreCase("Chrome")) {
				DesiredCapabilities capability = DesiredCapabilities.chrome();
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
				driver = new ChromeDriver(capability);
			} else if (CONFIG.getProperty("browserType").equalsIgnoreCase("Safari")) {
				DesiredCapabilities desiredCapabilities = DesiredCapabilities.safari();
				SafariOptions safariOptions = new SafariOptions();
				safariOptions.setUseCleanSession(true);
				desiredCapabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
				driver = new SafariDriver(desiredCapabilities);
			}
			String waitTime = CONFIG.getProperty("defaultImplicitWait");
			String pageWait = CONFIG.getProperty("defaultPageWait");
			driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			try {
				driver.manage().timeouts().pageLoadTimeout(Long.parseLong(pageWait), TimeUnit.SECONDS);
			} catch (Throwable t) {
				logger.info("Page Load Timeout not supported in safari driver");
			}
		}

		return driver;
	}

	public String clickOnLinkButtonInLoginPage() {
		waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_FLAG_REASON_MODAL_XPATH.toString()), 60);
		// waitForElementTobeVisible(ob,
		// By.cssSelector("div[class='modal-dialog']"), 60);
		// WebElement linkButton =
		// ob.findElement(By.cssSelector("button[ng-click='vm.callSocialLogin()']"));
		WebElement linkButton = ob.findElement(By.cssSelector(OnePObjectMap.SIGNIN_USING_FB_BUTTON_CSS.toString()));
		String linkName = linkButton.getText();
		linkButton.click();

		if (linkName.contains("Facebook"))
			return "Facebook";
		else if (linkName.contains("LinkedIn"))
			return "LinkedIn";
		else
			return null;
	}

	public void socialLinking() throws Exception {

//		waitForElementTobeVisible(ob,
//				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SOCIAL_LINKING_ONBOARDING_MODAL_CSS.toString()), 30);
//		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SOCIAL_LINKING_ONBOARDING_MODAL_CSS.toString()))
//				.click();
		BrowserWaits.waitTime(3);
		ob.findElement(By.xpath("//input[@name='steamPassword']"))
				.sendKeys(LOGIN.getProperty("PWDUserFBENWIAM80"));
		ob.findElement(
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SOCIAL_LINKING_ONBOARDING_MODAL_BUTTON_CSS.toString()))
				.click();

	}

	public void checkLinking() throws Exception {
		waitForElementTobeClickable(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString()),
				20);
		jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
		browserWait.waitUntilText("Account");
		jsClick(ob, ob.findElement(By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK.toString())));

	}

	public void clickNotnowButtonLinkingModal() throws Exception {

		// waitForElementTobeVisible(ob,By.cssSelector("div[class='modal-content
		// ng-scope']"), 60);
		// boolean Modal=
		// ob.findElement(By.cssSelector("div[class='modal-content
		// ng-scope']")).isDisplayed();

		// if (Modal)

		ob.findElement(By.cssSelector("a[ng-click='vm.callSkipLinking()']")).click();

	}

	/**
	 * Method for click Not now link if its present
	 * 
	 * @throws Exception,
	 *             When not ablet to click link
	 */
	public void clickNotnowLink() throws Exception {
		List<WebElement> alreadyHaveAccount = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NOT_NOW_BUTTON_CSS);
		if (alreadyHaveAccount.size() == 1) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.NOT_NOW_BUTTON_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NOT_NOW_BUTTON_CSS);
		}

	}

	public boolean loginToIPA(String username, String password) throws Exception {
		WebElement Element = null;
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_USERNAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_USERNAME_CSS, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_PASSWORD_CSS, password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_NEW_SEARCH_LINK_CSS);
			Element = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_NEW_SEARCH_LINK_CSS.toString()));
		} catch (Exception ex) {
		}
		return Element != null;

	}

	/**
	 * Method for Login to WAT application
	 * 
	 * @param username
	 * @param password
	 * @throws Exception,
	 *             When user is not able to login Successfully
	 */
	public void loginToWAT(String username, String password, ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs", "Sign in", "Forgot password?");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_USERNAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_USERNAME_CSS, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_PASSWORD_CSS, password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Search", "Web of Science: Author search");
		test.log(LogStatus.INFO, "Logged into WAT Applicaton Successfully");

	}
	
	
	/**
	 * Method for WAT login page
	 * @throws Exception, When login page not displayed after succesfully logout or when launch the application 
	 */
	public void waitForloginToWATPage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs", "Sign in", "Forgot password?");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_USERNAME_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SIGNIN_CSS);
	}

}