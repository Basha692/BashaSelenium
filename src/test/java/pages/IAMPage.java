package pages;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class IAMPage extends TestBase {

	public IAMPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void sendEamilToTextBox(String email) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString())).sendKeys(email);
		// ob.findElement(By.cssSelector(OnePObjectMap.DRA_STEPUPAUTHMODAL_FORGOTPW_PAGE_CSS.toString())).click();

	}

	public void validateTextInForgotPasswordPage() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_TEXT_CSS);
		WebElement element = ob
				.findElement(By.cssSelector(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_TEXT_CSS.toString()));
		String forgotPassText = element.findElement(By.tagName("h3")).getText();
		logger.info("Title : " + forgotPassText);
		String text = element.findElement(By.tagName("p")).getText();
		logger.info("Text : " + text);
		Assert.assertTrue(forgotPassText.contains("Forgot password"));
		Assert.assertTrue(text.contains(
				"Please enter your email address. We'll send you an email that will allow you to reset your password."));
	}

	public void clickSendEmailButton() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_SEND_EMAIL_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_SEND_EMAIL_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Email Sent");
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS);
	}

	public void checkEmailSentText(String email) throws Exception {
		// pf.getBrowserWaitsInstance(ob)
		// .waitUntilElementIsDisplayed(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_TEXT_CSS);
		String emailSentText = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_TEXT_CSS).get(1).getText();
		logger.info("EMAILSENTTEXT-->" + emailSentText);
		/*
		 * String emailSentText = element.findElement(By.tagName("h3")).getText(); logger.info("Title : " +
		 * emailSentText); List<WebElement> list = element.findElements(By.tagName("p")); logger.info("Size : " +
		 * list.size()); String messageText = list.get(0).getText(); logger.info("Message Text : " + messageText);
		 */
		String messageContent = "An email with password reset instructions has been sent to " + email + ".";
		logger.info("Text123 : " + messageContent);

		/*
		 * String checkFolder = list.get(1).getText(); logger.info("Email verification folder : " + checkFolder);
		 */
		Assert.assertTrue(emailSentText.contains("Email Sent"));
		Assert.assertTrue(emailSentText.contains(messageContent));
		Assert.assertTrue(emailSentText.contains("You may need to check your spam folder."));

	}

	public void clickOkButton() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS);
		// ob.findElement(By.cssSelector(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS.toString())).click();
		// pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_OK_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Project Neon");

	}

	public void checkLoginPage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Sign in");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_LANDING_PAGE_LOGGIN_BANNER_CSS);
		// String loginPageAppName = ob.findElement(By.cssSelector(OnePObjectMap.DRA_BGCOLOR_CLASS_CSS.toString()))
		// .getText();
		// Assert.assertTrue(loginPageAppName.contains("Project Neon"));

	}

	public void openGurillaMail() throws InterruptedException {
		ob.get("https://www.guerrillamail.com");
	}

	public void clickReceivedMail(String message) throws InterruptedException {
		List<WebElement> email_list = ob.findElements(By.xpath(OnePObjectMap.GURILLA_LIST_EMAIL_IDS_XPATH.toString()));
		WebElement myE = email_list.get(0);
		JavascriptExecutor executor = (JavascriptExecutor) ob;
		executor.executeScript("arguments[0].click();", myE);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Click here to reset your password");
		String subjectTitle = ob.findElement(By.cssSelector(OnePObjectMap.GURILLA_RECEIVED_MAIL_SUBJECT_CSS.toString()))
				.getText();
		String expectedSubjectTitle = message + " password reset";
		Assert.assertEquals(subjectTitle, expectedSubjectTitle);
	}

	public void clickResetPasswordLink() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_MAIL_BODY_PASSWORD_RESET_LINK_XPATH);
		WebElement reset_link_element = ob
				.findElement(By.xpath(OnePObjectMap.GURILLA_MAIL_BODY_PASSWORD_RESET_LINK_XPATH.toString()));
		String reset_link_url = reset_link_element.getAttribute("href");
		ob.get(reset_link_url);

	}

	public void checkAllowedCharacters(String string,
			int i) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RESET_PASSWORD_PAGE_CHECK_VALIDATIONS_CSS);
		WebElement element = ob
				.findElements(By.cssSelector(OnePObjectMap.RESET_PASSWORD_PAGE_CHECK_VALIDATIONS_CSS.toString()))
				.get(i);
		element.findElement(By.cssSelector(OnePObjectMap.RESET_PASSWORD_PAGE_CHECK_SUCCESS_TICK_MARK_CSS.toString()));
		// String message = element
		// .findElement(By
		// .cssSelector(OnePObjectMap.RESET_PASSWORD_PAGE_CHECK_SUCCESS_TICK_MARK_MESSAGE_CSS.toString()))
		// .getText();
	}

	public void clickForgotPasswordLink() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_FORGOT_PASSWORD_LINK_CSS);
		String forgotPassText = ob
				.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_FORGOT_PASSWORD_LINK_CSS.toString())).getText();
		logger.info("Fogot Password Text : " + forgotPassText);
		Assert.assertEquals(forgotPassText, "Forgot password?");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.LOGIN_PAGE_FORGOT_PASSWORD_LINK_CSS);

	}

	public void checkApplicationName(String appName) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_LIST_EMAIL_PROJECT_TITLE_CSS);
		String emailAppName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.GURILLA_LIST_EMAIL_PROJECT_TITLE_CSS).getText();
		logger.info("EmailAppName--->" + emailAppName);
		Assert.assertEquals(appName, emailAppName);

	}

	public void clickResetYourPasswordLink() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.EMAIL_BODY_PASSWORD_RESET_LINK_CSS);
		WebElement reset_link_element = ob
				.findElement(By.cssSelector(OnePObjectMap.EMAIL_BODY_PASSWORD_RESET_LINK_CSS.toString()));
		String reset_link_url = reset_link_element.getAttribute("href");
		ob.get(reset_link_url);

	}

	public void checkExternalPasswordPageText(String resetPass,
			String newPass) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_CSS);
		WebElement element = ob.findElement(By.cssSelector(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_CSS.toString()));

		List<WebElement> listOfElements = element.findElements(By.tagName("p"));
		String resetPassText = element.findElement(By.tagName("h3")).getText();
		// String resetPassText = listOfElements.get(0).getText();
		logger.info("Title : " + resetPassText);
		String newPassText = listOfElements.get(0).getText();
		logger.info("wxpireTimeText : " + newPassText);
		// String resetPassText = element.findElement(By.tagName("h2")).getText();
		// logger.info("Title : " + resetPassText);
		// String newPassText = element.findElement(By.tagName("p")).getText();
		// logger.info("Title : " + newPassText);
		Assert.assertTrue(resetPassText.contains(resetPass));
		Assert.assertTrue(newPassText.contains(newPass));
	}

	public void checkTextBox(String newPassword) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.PASSWORD_RESET_PAGE_PASSWORD_TEXT_BOX_CSS);
		ob.findElement(By.cssSelector(OnePObjectMap.PASSWORD_RESET_PAGE_PASSWORD_TEXT_BOX_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.PASSWORD_RESET_PAGE_PASSWORD_TEXT_BOX_CSS.toString()))
				.sendKeys(newPassword);
	}

	public void clickResetButton() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_RESET_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_RESET_BUTTON_CSS);

	}

	public void checkChangedPasswordMailSubject(String message) throws InterruptedException {
		List<WebElement> email_list = ob.findElements(By.xpath(OnePObjectMap.GURILLA_LIST_EMAIL_IDS_XPATH.toString()));
		WebElement myE = email_list.get(0);
		JavascriptExecutor executor = (JavascriptExecutor) ob;
		executor.executeScript("arguments[0].click();", myE);
		String subjectTitle = ob.findElement(By.cssSelector(OnePObjectMap.GURILLA_RECEIVED_MAIL_SUBJECT_CSS.toString()))
				.getText();
		String expectedSubjectTitle = message + " password changed";
		Assert.assertEquals(subjectTitle, expectedSubjectTitle);
	}

	public void login(String email,
			String newPassword) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS);

		pf.getBrowserActionInstance(ob).clear(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS, email);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.LOGIN_PAGE_PASSWORD_TEXT_BOX_CSS, newPassword);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
	}

	public void checkAlreadyUsedMailSubject(String message) throws InterruptedException {
		List<WebElement> email_list = ob.findElements(By.xpath(OnePObjectMap.GURILLA_LIST_EMAIL_IDS_XPATH.toString()));
		logger.info(email_list.size());
		WebElement myE = email_list.get(1);
		JavascriptExecutor executor = (JavascriptExecutor) ob;
		executor.executeScript("arguments[0].click();", myE);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Click here to reset your password");
		String subjectTitle = ob.findElement(By.cssSelector(OnePObjectMap.GURILLA_RECEIVED_MAIL_SUBJECT_CSS.toString()))
				.getText();
		String expectedSubjectTitle = message + " password reset";
		Assert.assertEquals(subjectTitle, expectedSubjectTitle);

	}

	public void checkInvalidPasswordResetPage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_CSS);
		WebElement element = ob.findElement(By.cssSelector(OnePObjectMap.RESET_YOUR_PASSWORD_PAGE_CSS.toString()));
		// String resetPassText = element.findElement(By.tagName("h2")).getText();
		// logger.info("Title : " + resetPassText);
		List<WebElement> ListOfElements = element.findElements(By.tagName("p"));
		String resetPassText = element.findElement(By.tagName("h3")).getText();
		// String resetPassText = ListOfElements.get(0).getText();
		logger.info("Title : " + resetPassText);
		String expireTimeText = ListOfElements.get(0).getText();
		logger.info("wxpireTimeText : " + expireTimeText);
		String resentMailText = ListOfElements.get(1).getText();
		logger.info("ResentMailText : " + resentMailText);
		String invalidPassPageTitle = "Password reset link has expired";
		String expireTime = "To protect your security, the link to reset your password expired after 24 hours.";
		String resentText = "To resend the email" + "," + " please enter your email address.";
		Assert.assertTrue(invalidPassPageTitle.contains(resetPassText));
		Assert.assertTrue(expireTimeText.contains(expireTime));
		Assert.assertTrue(resentMailText.contains(resentText));

	}

	public void checkPrepopulatedText(String email) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS);
		JavascriptExecutor js = (JavascriptExecutor) ob;
		String populatText = (String) (js.executeScript("return arguments[0].value",
				ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString()))));
		logger.info("PopulatText--->" + populatText);

		/*
		 * String textValues = (String)(js.executeScript("return angular.element(arguments[0]).scope().vm.email",
		 * ob.findElement(By.cssSelector(OnePObjectMap.LOGIN_PAGE_EMAIL_TEXT_BOX_CSS.toString()))));
		 * System.out.println("PopulatText--->"+textValues);
		 */

		Assert.assertEquals(email, populatText);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_LANDING_PAGE_LOGGIN_BANNER_CSS);
	}

	public void clickResendEmailButton() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_RESEND_EMAIL_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_RESEND_EMAIL_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Sign in");

	}

	public void clickCancelButton() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_PAGE_CALCEL_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_LOGIN_PAGE_FORGOT_PASSWORD_PAGE_CALCEL_BUTTON_CSS);
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Sign in");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_LANDING_PAGE_LOGGIN_BANNER_CSS);
	}

	public void checkErrorMessage(String errorMessage) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS);
		String errorMessaes = ob.findElement(By.cssSelector(OnePObjectMap.LOGING_PAGE_ERROR_MESSAGE_CSS.toString()))
				.getText();
		Assert.assertTrue(errorMessage.contains(errorMessaes));
	}

	public void checkAgreeAndContinueButton() throws Exception {
		try {
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_AGREE_CSS);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
		} catch (Exception e) {
			// pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			List<WebElement> onboarding_modals = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			if (onboarding_modals.size() != 0) {
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.ENW_HOME_CONTINUE_XPATH);
			}

		}
		BrowserWaits.waitTime(4);
	}

	public void checkENWApplicationName(String appName) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_LIST_EMAIL_ENW_PROJECT_TITLE_CSS);
		String emailAppName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.GURILLA_LIST_EMAIL_ENW_PROJECT_TITLE_CSS).getText();
		logger.info("EmailAppName--->" + emailAppName);
		Assert.assertEquals(appName, emailAppName);

	}

	public void openCCURL(String url) {
		ob.get(url);
	}

	public void loginCustomerCare(String userName,
			String password) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_NAME_XPATH);
		ob.findElement(By.id(OnePObjectMap.CUSTOMER_CARE_USER_NAME_XPATH.toString())).sendKeys(userName);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_PASSWORD_XPATH);
		ob.findElement(By.id(OnePObjectMap.CUSTOMER_CARE_PASSWORD_XPATH.toString())).sendKeys(password);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CLICK_LOGIN_BUTTON_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CARE_CLICK_LOGIN_BUTTON_XPATH);
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Customer Care Version");
	}

	public void openMenuPanel() throws Exception {
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_MENU_PANEL_NAME);
		WebElement ele = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_MENU_PANEL_NAME);
		ob.switchTo().frame(ele);
	}

	public void closeMenuPanel() throws Exception {
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_MENU_PANEL_NAME);
		ob.switchTo().defaultContent();
	}

	public void openMainPanel() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_MAIN_PANEL_NAME);
		WebElement ele = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_MAIN_PANEL_NAME);
		ob.switchTo().frame(ele);
	}

	public void closeMainPanel() throws Exception {
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_MAIN_PANEL_NAME);
		ob.switchTo().defaultContent();
	}

	public void clickUserManagement() throws InterruptedException {
		ob.findElement(By.linkText("User Management")).click();
	}

	public void clickCreateUser() throws InterruptedException {
		ob.findElement(By.linkText("Create User")).click();
		BrowserWaits.waitTime(3);
	}

	public void registerNewUser(String email) throws Exception {

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_FIRSTNAME_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_FIRSTNAME_NAME,
				"duster");

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_LASTNAME_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_LASTNAME_NAME, "man");

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_APPLICATIONID_ID);
		WebElement selectApp = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_APPLICATIONID_ID);

		Select dropdown = new Select(selectApp);
		dropdown.selectByVisibleText("Neon");

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_SET_PASSWORD_ID);
		WebElement selectPass = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_SET_PASSWORD_ID);

		Select dropdown1 = new Select(selectPass);
		dropdown1.selectByVisibleText("Set Password");

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_PASSWORD_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_PASSWORD_NAME,
				"Neon@123");
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_CONFORM_PASSWORD_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_CONFORM_PASSWORD_NAME,
				"Neon@123");

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME, email);

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_REGISTER_BUTTON_NAME);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_REGISTER_BUTTON_NAME);
		pf.getBrowserWaitsInstance(ob).waitUntilText("The user was successfully registered. User Id is");
	}

	public void clickAssociateAndDisassociate() {
		ob.findElement(By.linkText("Associate/Disassociate")).click();
	}

	public void clickUserToClimeTicket() throws InterruptedException {
		ob.findElement(By.linkText("User to ClaimTicket")).click();
		BrowserWaits.waitTime(4);
	}

	public void enterEmailField(String email,
			String climeTicket) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME, email);
		ob.findElements(By.name(OnePObjectMap.CUSTOMER_FIND_USER_NAME.toString())).get(0).click();
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_ENTER_CLIME_TICKET_NAME, climeTicket);
		ob.findElements(By.name(OnePObjectMap.CUSTOMER_FIND_USER_NAME.toString())).get(1).click();
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CLICK_ASSOCIATE_BUTTON_NAME);
		pf.getBrowserWaitsInstance(ob).waitUntilText("successfully associated with the claimTicket");
	}

	public void openHeaderPanel() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_HEADER_PANEL_NAME);
		WebElement ele = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_HEADER_PANEL_NAME);
		ob.switchTo().frame(ele);
	}

	public void closeHeaderPanel() throws Exception {
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_HEADER_PANEL_NAME);
		ob.switchTo().defaultContent();
	}

	public String getEmail() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_MAIL_TEXT_ID);
		String emailText = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.GURILLA_MAIL_TEXT_ID).getText();
		return emailText;
	}

	public void logoutCustomerCare() {
		ob.findElement(By.linkText("Log Out")).click();
		Alert alertOK = ob.switchTo().alert();
		alertOK.accept();
	}

	public void checkCCLoginPage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_LOGIN_PAGE_NAME);
	}

	public void checkDRAApplicationName(String appName) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_LIST_EMAIL_DRA_PROJECT_TITLE_CSS);
		String emailAppName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.GURILLA_LIST_EMAIL_DRA_PROJECT_TITLE_CSS).getText();
		logger.info("EmailAppName--->" + emailAppName);
		Assert.assertEquals(appName, emailAppName);
	}

	public void checkIPAApplicationName(String appName) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_LIST_EMAIL_IPA_PROJECT_TITLE_CSS);
		String emailAppName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.GURILLA_LIST_EMAIL_IPA_PROJECT_TITLE_CSS).getText();
		logger.info("EmailAppName--->" + emailAppName);
		Assert.assertEquals(appName, emailAppName);

	}

	
	public void checkForgotPasswordPageCALogo() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.LOGIN_PAGE_LOGO_IMG_XPATH);
		String companyName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.LOGIN_PAGE_LOGO_IMG_XPATH)
				.getAttribute("alt");
		Assert.assertEquals(companyName, "Clarivate Logo");
	}

	public void checkAppName(String expectedAppName) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_BRANDING_NAME_CSS);
		String appName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.IPA_BRANDING_NAME_CSS).getText();
		Assert.assertEquals(appName, expectedAppName);		
	}

	public void checkDRAAppname(String expectedAppName) throws Exception{
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_BRANDING_NAME_CSS);
		String appName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.DRA_BRANDING_NAME_CSS).getText();
		Assert.assertEquals(appName, expectedAppName);
	}

	public void checkOnBoarding() throws Exception {
		
		List<WebElement> element=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.DRA_ONBOARDING_CSS);
		if(element.size()!=0){
			element.get(1).click();
			BrowserWaits.waitTime(5);
		}
	}
	
	public void checkWATApplicationName(String appName) throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.GURILLA_LIST_EMAIL_WAT_PROJECT_TITLE_CSS);
		String emailAppName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.GURILLA_LIST_EMAIL_WAT_PROJECT_TITLE_CSS).getText();
		logger.info("EmailAppName--->" + emailAppName);
		Assert.assertEquals(appName, emailAppName);

	}
	
	public void checkWATAppname(String expectedAppName) throws Exception{
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_BRANDING_NAME_CSS);
		String appName = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_BRANDING_NAME_CSS).getText();
		Assert.assertEquals(appName, expectedAppName);
	}

	public void checkEmilBodySupportContactEmail(String supportContactEmail) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SUPPORT_CONTACT_EMAIL_CSS);
		String contactEmail = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SUPPORT_CONTACT_EMAIL_CSS).getText();
		Assert.assertEquals(contactEmail, supportContactEmail);
	}

	public void checkFooterSupportContactEmail(String contactEmail) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SUPPORT_CONTACT_FOOTER_EMAIL_CSS);
		String footerContactEmail = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SUPPORT_CONTACT_FOOTER_EMAIL_CSS).getText();
		Assert.assertEquals(footerContactEmail, contactEmail);
		
	}

	public void enterEmailFieldToDisUser(String email,
			String climeTicket) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_CREATE_USER_MAIL_NAME, email);
		ob.findElements(By.name(OnePObjectMap.CUSTOMER_FIND_USER_NAME.toString())).get(0).click();
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_ENTER_CLIME_TICKET_NAME, climeTicket);
		ob.findElements(By.name(OnePObjectMap.CUSTOMER_FIND_USER_NAME.toString())).get(1).click();
		BrowserWaits.waitTime(4);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CLICK_DISASSOCIATE_BUTTON_NAME);
		pf.getBrowserWaitsInstance(ob).waitUntilText("successfully disassociated from the claimticket");
	}

	public void clickFindUser() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_FINDUSER_LINK_CSS);
		ob.findElement(By.linkText("Regular User")).click();
	}

	public void findUser(String email) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("REGULAR USER");
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_FINDUSER_PAGE_EMAIL_CSS, email);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_FINDUSER_PAGE_FIND_BUTTON_CSS);
			
	}

	public void clickEditButton() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Primary Role");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_FINDUSER_PAGE_BUTTON_BUTTON_CSS);
	}
	
	public void checkClaimTickets() throws Exception{
		pf.getBrowserWaitsInstance(ob).waitUntilText("EDIT USER");
		List<WebElement> list=ob.findElements(By.cssSelector(OnePObjectMap.CUSTOMER_EDITUSER_PAGE_CSS.toString()));
		if(list.size()==0){
			logger.info("No Claim ticket associated");
		}else{
			logger.info("Claim ticket associated to the user");
			throw new Exception("Claim tickets assiciated to the user");
		}
	}
	
	public String getUserId(){
		String userId=ob.findElement(By.cssSelector(OnePObjectMap.CUSTOMER_GET_USER_ID_CSS.toString())).getText();
		return userId;
		
	}

	public void checkErrorMessage(){
		pf.getBrowserWaitsInstance(ob).waitUntilText("No Matching Records Found");
	}
	
	
	
}
