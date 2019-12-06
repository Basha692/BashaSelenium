package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;
import base.TestBase;

public class IPApage extends TestBase {

	PageFactory pf;

	public IPApage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void loginToIPA(String username, String password) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_USERNAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_USERNAME_CSS, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_PASSWORD_CSS, password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_IPA_SIGNIN_CSS);

	}

	public void clickOnSaveButton() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_CSS);

	}

	public void enterSavedatatitle(String datatiltle) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_TITLE_CSS);
		// pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_TITLE_CSS);
		pf.getBrowserActionInstance(ob).clear(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_TITLE_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_TITLE_CSS,
				datatiltle);
	}

	public void enterSavedatadesc(String datadesc) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_DESC_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_DESC_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_DESC_CSS,
				datadesc);
	}

	public void SaveDataInfo(String title, String desc) throws Exception {
		enterSavedatatitle(title);
		enterSavedatadesc(desc);

	}

	public void clickOnSaveData() throws Exception {

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_SAVE_BUTTON_CSS);
		// jsClick(ob,
		// ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_SAVE_BUTTON_CSS.toString())));
	}

	public void clickOnCancelData() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_CANCEL_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_SAVE_MODAL_CANCEL_BUTTON_CSS);
	}

	// Method to click on App switcher
	public void clickOnAppswitcher() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_APP_SWITCHER_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_APP_SWITCHER_CSS);

	}

	// Method to click on Project Neon
	public void clickOnNeonfromAppswitcher() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_PROJECTNEON_APP_SWITCHER_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_PROJECTNEON_APP_SWITCHER_CSS);
		BrowserWaits.waitTime(3);

	}

	public void changepwdIPA(String currentpw, String newpw) throws Exception {
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_CURRENTPW_FIELD_CSS,
				currentpw);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_NEWPW_FIELD_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_NEWPW_FIELD_CSS,
				newpw);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.IPA_ACCOUNTSETTINGS_CHANGEPWD_SUBMIT_CSS);

	}

	/**
	 * Method for to check IPA landing screen displayed or not
	 * 
	 * @throws Exception,When
	 *             IPA landing screen not displayed
	 */
	public void landingScreenIPA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
	}

	public void steamLockedIPA(String steamAccount) throws Exception {

		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).sendKeys(steamAccount);

		for (int i = 0; i <= 9; i++) {
			BrowserWaits.waitTime(3);
			// ob.findElement(By.name("loginPassword")).sendKeys("asdfgh");
			ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS.toString()))
					.sendKeys("asdfgh");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

		}

	}

	public void clickLoginIPA() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_NEW_SEARCH_LINK_CSS);

	}

	public void validateIPAInactiveErrorMsg(ExtentTest test) throws Exception {
		try {
			String errormsg_title = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_CSS).getText();
			String msg1 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_MSG1_CSS)
					.getText();
			String msg2 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_MSG2_XPATH)
					.getText();

			System.out.println(msg2);
			if (errormsg_title.contains("Thank you for your interest")
					&& msg1.contains("IP Analytics is currently available to customers in our early access program.")
					&& msg2.contains("Questions? Contact IPA.support@thomsonreuters.com.")) {
				test.log(LogStatus.PASS, " correct msg displayed ");
			} else {
				test.log(LogStatus.FAIL, " incorrect msg displayed ");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}
	}

	public void clickIPALink() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.IPA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.IPA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_LOGO_CSS);
	}

	public String getProfileNameIPA() throws Exception {
		clickOnProfileImageIPA();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		String ProfileName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS)
				.getText();
		return ProfileName;
	}

	public void clickOnProfileImageIPA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.IPA_PROFILE_FLYOUT_IMAGE_CSS);

	}

	public void validateIPAStepUPModalTitle(ExtentTest test) {

		try {
			String stepup_expectedtitle = "Sign in to IP Analytics";
			String stepup_actualtitle = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TITLE_CSS).getText();
			Assert.assertEquals(stepup_actualtitle, stepup_expectedtitle);
			test.log(LogStatus.PASS, "user is able to see correct Step up Auth modal title");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "user is not able to see correct Step up Auth modal title");

		}

	}

	public void validateFirstTextOnIPAStepUp(ExtentTest test) throws Exception {
		try {
			String stepup_expectedtext1 = "IP Analytics is currently available to customer in our early access program. Contact";
			String stepup_actualtext1 = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.IPA_STEPUPAUTHMODAL_TEXT1_CSS).getText();
			if (stepup_actualtext1.contains(stepup_expectedtext1)
					&& stepup_actualtext1.contains("IPA.support@thomsonreuters.com")
					&& stepup_actualtext1.contains("to learn more.")) {
				test.log(LogStatus.PASS,
						"user is able to see correct Step up Auth modal text1 : Thank you for your interest.....");
			} else {
				test.log(LogStatus.FAIL,
						"user is able to see incorrect Step up Auth modal text1 : Thank you for your interest.....");
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"user is not able to see Step up Auth modal text1 : Thank you for your interest.....");
		}
	}

	public void validateSecondTextOnIPAStepUP(ExtentTest test) throws Exception {

		try {
			String stepup_expectedtext2 = "To provide the highest level of security, we require you to sign in with your IP Analytics credentials.";
			String stepup_actualtext2 = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TEXT2_XPATH).getText();
			Assert.assertEquals(stepup_actualtext2, stepup_expectedtext2);
			test.log(LogStatus.PASS,
					"user is able to see correct Step up Auth modal text2 : To provide the highest level.....");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"user is not able to see correct Step up Auth modal text2 : To provide the highest level.....");
		}
	}

	public void validateThirdTextOnIPAStepUp(ExtentTest test) throws Exception {
		try {
			String stepup_expectedtext3 = "IP Analytics shares sign in credentials with other products you may use:";
			String stepup_expectedtext4 = " Please sign in using the email address and password you use to access any of the above mentioned products.";
			String stepup_actualtext3 = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TEXT3_XPATH).getText();
			if (stepup_actualtext3.contains(stepup_expectedtext3) && stepup_actualtext3.contains("Key Pathway Advisor")
					&& stepup_actualtext3.contains("EndNote") && stepup_actualtext3.contains("InCites")
					&& stepup_actualtext3.contains("Thomson Innovation")
					&& stepup_actualtext3.contains("Web of Science")
					&& stepup_actualtext3.contains(stepup_expectedtext4)) {
				test.log(LogStatus.PASS,
						"user is able to see correct Step up Auth modal text3 : Target Druggability shares sign in .....");
			} else {
				test.log(LogStatus.FAIL,
						"user is able to see incorrect Step up Auth modal text3 : Target Druggability shares sign in .....");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "user is not able to see Step up Auth modal thrid text");
		}

	}

	public boolean validateCustomerCareNameErrorMessage() throws Exception {
		String ErrorMsg = "Please enter at least 2 characters for name";
		waitForAjax(ob);

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.IPA_CC_NAME_CSS.toString()), 60);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_ORG_NAME_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_NAME_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_NAME_CSS.toString())).sendKeys("");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_ORG_NAME_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_NAME_CSS);

		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.IPA_CC_NAME_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.getText().trim().equalsIgnoreCase(ErrorMsg);
				return true;
			}
		}
		return false;
	}

	public boolean validateCustomerCareOrgNameErrorMessage() throws Exception {
		String ErrorMsg = "Please enter at least 2 characters for organization name";

		waitForAjax(ob);

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.IPA_CC_ORG_NAME_CSS.toString()), 60);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_ORG_NAME_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_ORG_NAME_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_ORG_NAME_CSS.toString())).sendKeys("a");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_ORG_NAME_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_NAME_CSS);

		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.IPA_CC_ORG_NAME_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.getText().trim().equalsIgnoreCase(ErrorMsg);
				return true;
			}
		}
		return false;
	}

	public boolean validateCustomerCareEmailErrorMessage() throws Exception {
		String ErrorMsg = "Incorrect email address format. Please try again";
		waitForAjax(ob);

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_EMAIL_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_PHONE_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_EMAIL_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_EMAIL_CSS.toString())).sendKeys("a.com");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_PHONE_CSS);

		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.IPA_CC_EMAIL_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.getText().trim().equalsIgnoreCase(ErrorMsg);
				return true;
			}
		}
		return false;
	}
	public void validateSearchResultMsg(ExtentTest test, String DRAProfileName) throws Exception {
		try {
			String Result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DR_SEARCH_RESULT_CSS).getText();
			String ResultExpected = "Your search for kavita manaz found no matches.";
			if (ResultExpected.contains(Result)) {
				test.log(LogStatus.PASS, " User's account is non onboarded and non discoverable ");
			} else {
				test.log(LogStatus.FAIL, " User's account  non discoverable ");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}

	}
	public boolean validateCustomerCareNumberErrorMessage() throws Exception {
		String ErrorMsg = "Invalid format. Only numbers (minimum 7 digits), spaces, and special characters + ( ) - allowed";
		waitForAjax(ob);

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_EMAIL_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_CC_PHONE_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_PHONE_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.IPA_CC_PHONE_CSS.toString())).sendKeys("a.com");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_CC_PHONE_CSS);

		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.IPA_CC_PHONE_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.getText().trim().equalsIgnoreCase(ErrorMsg);
				return true;
			}
		}
		return false;
	}
}
