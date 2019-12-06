package pages;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;

public class DRAPage extends TestBase {

	public DRAPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method for logout DRA application
	 * 
	 * @throws Exception,When
	 *             not able to login
	 */
	public void logoutDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
		BrowserWaits.waitTime(3);
	}

	public void clickLoginDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		//fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.DRA_SEARCH_BOX_CSS.toString()), 100);
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_SEARCH_BOX_CSS);

	}

	public void clickOnAccountLinkDRA() throws Exception {
		pf.getProfilePageInstance(ob).clickProfileFlyout();
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_ACCOUNT_LINK);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILEFLYOUT_ACCOUNTLINK_CSS);
	}

	public void clickOnChangePwLinkDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_LINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_LINK_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_CURRENTPW_FIELD_CSS);
	}

	public void changepwd(String currentpw, String newpw) throws Exception {
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_CURRENTPW_FIELD_CSS,
				currentpw);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_NEWPW_FIELD_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_NEWPW_FIELD_CSS,
				newpw);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_SUBMIT_CSS);

	}

	public void clickCancelOnChangepwd() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_CANCEL_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_CANCEL_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.DRA_ACCOUNTSETTINGS_CHANGEPWD_LINK_CSS);
	}

	public void clickOnSignInWithFBOnDRAModal() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.DRA_LINKINGMODAL_FB_SIGN_IN_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_LINKINGMODAL_FB_SIGN_IN_BUTTON_CSS);
	}

	public void clickOutsideTheDRAModal() throws Exception {

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.DRA_LINKINGMODAL_FB_SIGN_IN_BUTTON_CSS);

		Dimension dimesions = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_LINKINGMODAL_FB_SIGN_IN_BUTTON_CSS).getSize();
		logger.info("Width : " + dimesions.width);
		logger.info("Height : " + dimesions.height);
		int x = dimesions.width;
		int y = dimesions.height;

		Actions builder = new Actions(ob);
		builder.moveToElement(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_LINKINGMODAL_FB_SIGN_IN_BUTTON_CSS),
				x + 150, y).build().perform();
		builder.click().build().perform();
	}

	/**
	 * Method for to check DRA landing screen displayed or not
	 * 
	 * @throws Exception,When
	 *             DRA landing screen not displayed
	 */
	public void landingScreenDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_LOGO_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Drug Research Advisor");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
	}

	/**
	 * Method for login DRA Application with valid login credentials
	 * 
	 * @param userName
	 * @param password
	 * @throws Exception,
	 *             When login not happend
	 */
	public void loginToDRAApplication(String userName, String password) throws Exception {
		
		pf.getBrowserWaitsInstance(ob).waitUntilText("Drug Research Advisor");

		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS, userName);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS, password);

		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.DRA_SEARCH_BOX_CSS.toString()), 200);
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_SEARCH_BOX_CSS);

	}

	public void validateInactiveaccount(ExtentTest test) throws Exception {
		try {
			WebElement element = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_CSS);

			if (!(element.isDisplayed())) {
				test.log(LogStatus.FAIL,
						"user is allowed to access DRA as the STeAM account is not tied to an active subscription");
			}
			test.log(LogStatus.PASS,
					"user is not allowed to access DRA as the STeAM account is not tied to an active subscription with the 'DRA_TARGET_DRUG' entitlement (SKU).");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}
	}

	/**
	 * Method for to check invalid error message displayed or not
	 * 
	 * @throws Exception,When
	 *             entered invalid credentials
	 */
	public void validateInvalidCredentialsErrorMsg(ExtentTest test) throws Exception {
		try {
			if (!((pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_INVALIDCREDENTIALS_ERRORMSG_CSS)
					.isDisplayed()))) {
				test.log(LogStatus.FAIL, "Unexpected login happened");// extent
																		// //
																		// report
				closeBrowser();

			} else {
				test.log(LogStatus.PASS,
						" 'Invalid Email/Password. Please try again.' message is displayed correctly ");// extent
																										// report
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}
	}

	public void validateDRAInactiveErrorMsg(ExtentTest test) throws Exception {
		try {
			String errormsg_title = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_CSS).getText();
			String msg1 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_MSG1_CSS)
					.getText();
			String msg2 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_SUBSCRIPTION_INACTIVE_MSG2_XPATH)
					.getText();

			if (errormsg_title.contains("Thank you for your interest")
					&& msg1.contains("Drug Research Advisor is a subscription product.")
					&& msg2.contains("Questions? Learn more or contact Drug Research Advisor Customer Care.")) {
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

	public void validateAccount(int accountCount, String linkName, String accountId, ExtentTest test) throws Exception {
		try {
			Assert.assertTrue(pf.getAccountPageInstance(ob).verifyLinkedAccount(linkName, accountId));
			Assert.assertTrue(pf.getAccountPageInstance(ob).validateAccountsCount(accountCount));
			test.log(LogStatus.PASS, "Single Steam account is available and is not linked to Social account");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Linked accounts are available in accounts page");
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_failed")));// screenshot
		}
	}

	public void validateIncorrectPwdErrorMsg(ExtentTest test) throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);
		WebElement errormsg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);

		if (!(errormsg.isDisplayed())) {
			test.log(LogStatus.FAIL, "Incorrect password error message is not displayed");
		} else {
			String actualerrortext = errormsg.getText();
			String expectederrortext = "Incorrect password. Please try again.";
			Assert.assertEquals(actualerrortext, expectederrortext);
			test.log(LogStatus.PASS, "Incorrect password error message is displayed");
		}
	}

	public void validateCurrentPwdErrorMsg(ExtentTest test) throws Exception {
		try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);
			WebElement errormsg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);
			String actualerrortext1 = errormsg.getText();
			String expectederrortext1 = "New password should not match current password";
			Assert.assertEquals(actualerrortext1, expectederrortext1);
			test.log(LogStatus.PASS, "'New password should not match current password' error message is displayed");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Current password error message is not displayed");
		}
	}

	public void validateNewPwdErrorMsg(ExtentTest test) throws Exception {
		try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);
			WebElement errormsg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_CHANGEPW_ERROR_MSG_CSS);
			String actualerrortext2 = errormsg.getText();
			String expectederrortext2 = "New password should not match previous 4 passwords";
			Assert.assertEquals(actualerrortext2, expectederrortext2);
			test.log(LogStatus.PASS, "'New password should not match previous 4 passwords' error message is displayed");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "New password error message is not displayed");
		}
	}

	public void clickProfileLink() throws Exception {
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.IPA_PROFILE_FLYOUT_IMAGE_CSS.toString()), 100);
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.IPA_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_NAME_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_PROFILE_FLYOUT_NAME_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_EDIT_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_TITLE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Update", "Cancel", "Profile");
	}

	public String getProfileNameDRA() throws Exception {
		clickOnProfileImageDRA();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS);
		String ProfileName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_PROFILE_FLYOUT_INFO_CSS)
				.getText();
		return ProfileName;
	}

	public void clickOnProfileImageDRA() throws Exception {
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS.toString()), 100);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_PROFILE_FLYOUT_IMAGE_CSS);
	}

	public void clickOnFeedbackDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_FEEDBACKLINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_FEEDBACKLINK_CSS);
	}
	
	public void clickOnHelpDRA() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_HELPLINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_HELPLINK_CSS);
	}

	public void steamLockedDRA(String steamAccount) throws Exception {

		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).sendKeys(steamAccount);

		for (int i = 0; i <= 9; i++) {

			BrowserWaits.waitTime(3);
			ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS.toString()))
					.sendKeys("asdfgh");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

		}

	}

	public void loginTofbSuspended() throws Exception {

		pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("ENWIAM00015UserSuspended"),
				LOGIN.getProperty("ENWIAM00015SuspendedPWD"));
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);

	}

	public boolean verifyLinkedAccountInDRA(String accountType, String emailId) {
		boolean result = false;
		waitForAllElementsToBePresent(ob, By.cssSelector("div[class='account-option-item ng-scope']"), 60);
		List<WebElement> list = ob.findElements(By.cssSelector("div[class='account-option-item ng-scope']"));

		for (WebElement element : list) {

			String type1 = null;
			List<WebElement> elementList1 = element
					.findElements(By.cssSelector("span[class='wui-subtitle ng-binding']"));
			for (WebElement we1 : elementList1) {

				if (we1.isDisplayed()) {
					type1 = we1.getText();
					break;
				}

			}

			if ((accountType.equalsIgnoreCase("Steam") && type1.contains("Clarivate Analytics")
					|| type1.contains("Thomson Reuters | Project Neon"))
					|| accountType.equalsIgnoreCase(type1.trim())) {
				String emailid = null;
				emailid = element.findElement(By.cssSelector("span[class='ng-binding']")).getText();
				if (emailid.equalsIgnoreCase(emailId))
					result = true;
				break;
			}

		}
		return result;
	}

	public void SearchDRAprofileName(String title) throws Exception {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()), 30);
		WebElement titleField = ob
				.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()));
		titleField.clear();
		titleField.sendKeys(title);
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.DR_SEARCH_CLICK_CSS.toString()), 30);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DR_SEARCH_CLICK_CSS);
	}

	public void validateSearchResultMsg(ExtentTest test, String DRAProfileName) throws Exception {
		try {
			String Result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DR_SEARCH_RESULT_CSS).getText();
			String ResultExpected = "Your search for imara das found no matches.";
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

	public void clickDRALink() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_LOGO_CSS);
	}

	public void clickDRALinkInSteam() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_ONEP_APPS_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_APP_SWITCHER_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_SEARCH_BOX_CSS);
	}

	public void validateNavigationDRAApp(ExtentTest test) throws Exception {
		WebElement element = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_SEARCH_BOX_CSS);
		if (element.isDisplayed()) {
			test.log(LogStatus.PASS, "user is navigated seemlessly to DRA when logged in with steam credentials");
		} else {
			test.log(LogStatus.FAIL, "user is not navigated seemlessly to DRA");
		}

	}

	public void clickOnForgotPwLinkOnStepUpAuthModal() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_LINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_LINK_CSS);
	}

	public void validateForgotPwOnStepup(ExtentTest test) throws Exception {
		clickOnForgotPwLinkOnStepUpAuthModal();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_PAGE_CSS);
		WebElement forgotpage = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_PAGE_CSS);
		if (forgotpage.isDisplayed()) {
			test.log(LogStatus.PASS,
					"step up authentication modal includes a link to initiate the EndNote supported forgot password flow.");
		} else {
			test.log(LogStatus.FAIL,
					"step up authentication modal doesn't include a link to initiate the EndNote supported forgot password flow.");
		}

	}

	public void validateThirdTextOnStepUp(ExtentTest test) throws Exception {
		try {
			String stepup_expectedtext3 = "Target Druggability shares sign in credentials with other products you may use:";
			String stepup_expectedtext4 = "Please sign in using the email address and password you use to access any of the above mentioned products.";
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

	public void validateSecondTextOnStepUP(ExtentTest test) throws Exception {

		try {
			String stepup_expectedtext2 = "To provide the highest level of security, we require you to sign in with your Target Druggability credentials.";
			String stepup_actualtext2 = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TEXT2_XPATH).getText();
			System.out.println(stepup_actualtext2);
			Assert.assertEquals(stepup_actualtext2, stepup_expectedtext2);
			test.log(LogStatus.PASS,
					"user is able to see correct Step up Auth modal text2 : To provide the highest level.....");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"user is not able to see correct Step up Auth modal text2 : To provide the highest level.....");
		}
	}

	public void validateFirstTextOnStepUp(ExtentTest test) throws Exception {
		try {
			String stepup_expectedtext1 = "Thank you for your interest. Target Druggability is a subscription product. Don't yet have a subscription?";
			String stepup_actualtext1 = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TEXT1_CSS).getText();
			if (stepup_actualtext1.contains(stepup_expectedtext1) && stepup_actualtext1.contains("Learn more")) {
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

	public void validateStepUPModalTitle(ExtentTest test) {

		try {
			String stepup_expectedtitle = "Sign in to Target Druggability";
			String stepup_actualtitle = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.DRA_STEPUPAUTHMODAL_TITLE_CSS).getText();
			Assert.assertEquals(stepup_actualtitle, stepup_expectedtitle);
			test.log(LogStatus.PASS, "user is able to see correct Step up Auth modal title");

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "user is not able to see correct Step up Auth modal title");

		}

	}

	public void clickOnLearnMoreLink() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_STEPUPAUTHMODAL_LEARNMORE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_STEPUPAUTHMODAL_LEARNMORE_CSS);

	}
	
	public void clickOnNotNowButton() throws Exception {

		List<WebElement> ele=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NOT_NOW_BUTTON_CSS) ;
		if(ele.size() == 1) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.NOT_NOW_BUTTON_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NOT_NOW_BUTTON_CSS);
		}
		
	}

	public void clickOnSupportLinkFeedback() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_FEEDBACK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_FEEDBACK_CSS);

	}

	public void clickOnSupportLinkHelp() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_HELP_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_HELP_CSS);

	}
	public void clickDRAStepUpAuthLoginNotEntitledUser(ExtentTest test, String DRAUserName) throws Exception {
		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_PASSWORD_CSS.toString())).sendKeys(DRAUserName);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}
	}

	public void clickDRAStepUpAuthLoginSteam(ExtentTest test, String DRAUserName, String DRApwd) throws Exception {
		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString()), 30);

			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_USERNAME_CSS.toString())).sendKeys(DRAUserName);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_PASSWORD_CSS.toString())).sendKeys(DRApwd);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			closeBrowser();
		}
	}

	public void validateProductOverviewPage(ExtentTest test) {
		try {
			clickOnLearnMoreLink();
			BrowserWaits.waitTime(5);

			Set<String> myset = ob.getWindowHandles();
			Iterator<String> myIT = myset.iterator();
			ArrayList<String> al = new ArrayList<String>();
			for (int i = 0; i < myset.size(); i++) {
				al.add(myIT.next());
			}
			ob.switchTo().window(al.get(1));
			String actual_URL = ob.getCurrentUrl();
			String expected_URL = "http://clarivate.com/life-sciences/discovery-and-preclinical-research/drug-research-advisor/";
			Assert.assertTrue(actual_URL.contains(expected_URL));
			test.log(LogStatus.PASS,
					" user is taken to the target application product overview page in the seperate browser when User clicks on Learn more");
			BrowserWaits.waitTime(2);
			ob.close();
			ob.switchTo().window(al.get(0));
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, " user is not taken to the target application product overview page");
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
		}

	}

	public void validateFeedbackPageDRA(ExtentTest test) {
		try {

		
			/*Set<String> myset = ob.getWindowHandles();
			Iterator<String> myIT = myset.iterator();
			ArrayList<String> al = new ArrayList<String>();
			for (int i = 0; i < myset.size(); i++) {
				al.add(myIT.next());
			}
			ob.switchTo().window(al.get(2));*/
			String actual_URL = ob.getCurrentUrl();
			System.out.println(actual_URL);
			String expected_URL = "https://apps.clarivate.com/cmty/#/customer-care?app=dra";
			Assert.assertTrue(actual_URL.contains(expected_URL));
			test.log(LogStatus.PASS,
					" user is taken to the customer care page in the seperate browser when User clicks on support link");
			BrowserWaits.waitTime(2);
			//ob.close();
			//ob.switchTo().window(al.get(0));
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, " user is not taken to the customer care page ");
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
		}

	}

	public void clickModalLink() throws Exception{
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_STEPUPAUTHMODAL_PAGE_CSS);
	}

	
}