package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class GmailLoginPage extends TestBase {

	PageFactory pf;

	public GmailLoginPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();

	}

	public void gmailLogin(String username, String password) throws Exception {

		ob.get("https://accounts.google.com/ServiceLogin#identifier");
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.RCC_GMAIL_LOGIN_USERNAME_ID, username);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GMAIL_LOGIN_NEXT_BUTTON_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.RCC_GMAIL_LOGIN_PASSWORD_XPATH, password);
		ob.findElement(By.xpath(OnePObjectMap.RCC_GMAIL_LOGIN_SUBMIT_XPATH.toString())).sendKeys(Keys.ENTER);
	}

	public void clickonMail() {
		ob.findElement(By.xpath(OnePObjectMap.RCC_GMAIL_CLICK_EMAIL_XPATH.toString())).click();
	}

	public void clickViewInvitationLink() {
		ob.findElement(By.linkText("View invitation")).click();
	}

	public void MailWindowHandle() {
		String PARENT_WINDOW = ob.getWindowHandle();
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
			}
		}
	}

	public void mailProtectclick() {

		try {
			ob.findElement(By.xpath(OnePObjectMap.RCC_CLICK_ON_GMAIL_SECURITY_LINK_XPATH.toString())).click();
		} catch (Throwable t) {

		}
	}

	public void clickonSwitchtoaccountinGooglepage() throws Exception {

		String PARENT_WINDOW = ob.getWindowHandle();
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				
				
				
         pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GMAIL_SWITCH_TO_ACCOUNT_GOOGLE_XPATH);
         
         //ob.switchTo().window(PARENT_WINDOW);
	}
		}
	}
	public boolean verifyEmailSubject() throws Exception {
		String OriginalSubject = "Project Neon Group invitation";
		BrowserWaits.waitTime(5);

		String MailSubject = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GMAIL_SUBJECT_CONTENT_XPATH)
				.getText();

		if (OriginalSubject.equals(MailSubject))
			return true;
		else
			return false;

	}

	public void LogoutGmail() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GMAIL_PROFILE_CLICK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GMAIL_LOGOUT_BUTTON_CSS);
	}

	public boolean verifyEmailContent(String ReceiverUname, String Title, String SenderUserName) throws Exception {
		boolean result = false;
		String[] emailcontent = { "Project Neon", "Project Neon Group Invitation", "Thomson Reuters Project Neon",
				ReceiverUname, "you've been invited to join a project", SenderUserName, Title,
				"I've created a group for our team to share research findings.", "View invitation",
				"The Project Neon team" };

		List<String> list = Arrays.asList(emailcontent);

		String content = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GMAIL_WHOLE_CONTENT_CSS)
				.getText();

		for (String nw : list) {

			if (!content.contains(nw))
				return false;

			result = true;
		}
		return result;
	}
	
	public void clickonGoogleContinue() throws Exception
	{
		ob.findElement(By.xpath(OnePObjectMap.RCC_GMAIL_CLICK_CONTINUE_XPATH.toString())).click();
		BrowserWaits.waitTime(5);
	}
	
	public void signinGoogleWithoutSwitchingWindow(String username,String pwd) throws Exception
	{
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
				OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(
				OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS, username);
		pf.getBrowserActionInstance(ob)
				.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_NEXT_CSS);
		pf.getBrowserActionInstance(ob)
				.enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_PASSWORD_CSS, pwd);
		pf.getBrowserActionInstance(ob)
				.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_LOGIN_CSS);
	}

	public boolean validateGDUrlWithoutSwitchingWindow() throws InterruptedException {
		BrowserWaits.waitTime(5);
		boolean result=false;
		
					if(ob.getCurrentUrl().contains("drive.google.com"))
					{
				
				result= true;	
		}
	
		return result;
	}
	
	public String switchToMainWindow(WebDriver driver)  {
//		System.out.println("Closing the current browser");
//		driver.getWindowHandles().remove(driver.getWindowHandle());
		Set<String> myset = driver.getWindowHandles();
		Iterator<String> myIT = myset.iterator();
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < myset.size(); i++) {

			al.add(myIT.next());
		}

		driver.switchTo().window(al.get(1));
		driver.close();
		driver.switchTo().window(al.get(0));
		return al.get(0);
	}
	
}