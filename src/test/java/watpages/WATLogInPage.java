package watpages;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.OnePObjectMap;

public class WATLogInPage extends TestBase {

	public WATLogInPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void loginToWAT(String username,
			String password,
			ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("SaR Labs", "Sign in", "Forgot password?");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_USERNAME_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_USERNAME_CSS, username);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_PASSWORD_CSS, password);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SIGNIN_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Search", "Web of Science: Author search");
		test.log(LogStatus.INFO, "Login to WAT Applicaton Successfully");

	}

	public void logoutWAT() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_PROFILE_FLYOUT_IMAGE_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.WAT_PROFILE_FLYOUT_IMAGE_CSS);
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK);
		BrowserWaits.waitTime(3);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_LANDING_PAGE_LOGGIN_BANNER_CSS);
	}
}
