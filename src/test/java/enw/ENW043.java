package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

//OPQA-3590
//Verify that,user can navigate from WOS to ENW,if the user is not affiliated to a Customer in the market test group 
//based on the WOS Customer Check, and should be sent to the alternate version of Endnote.

public class ENW043 extends TestBase {
	static int status = 1;
	String ExpecteduRl = "https://dev-stable.1p.thomsonreuters.com/#/privacy-statement?app=endnote";
	String Expected_text = "Privacy Statement";

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW043() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// String url="https://dev-stable.1p.thomsonreuters.com/#/profile/";
		if (!master_condition) {
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to("http://ua-qa.newisiknowledge.com/");
			loginToWOS("MARKETUSEREMAIL", "MARKETUSERPASSWORD");
			BrowserWaits.waitTime(8);
			InvalidNeonSession();
			//pf.getBrowserWaitsInstance(ob).waitUntilText("Thomson Reuters", "EndNote", "Downloads", "Options");
			BrowserWaits.waitTime(8);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PRIVACY_LINK_XPATH.toString())));
			BrowserWaits.waitTime(3);
			String text = ob.findElement(By.cssSelector("h2[class='wui-title']")).getText();
			Assert.assertEquals(ob.getCurrentUrl(), ExpecteduRl);
			test.log(LogStatus.PASS, "Expected page is displayed and  Navigating to the proper Page.");
			Assert.assertEquals(text, Expected_text);
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void InvalidNeonSession() throws Exception {
		waitForElementTobeClickable(ob, By.cssSelector("a[id='HS_EndNoteLink_signedin']"), 180);
		jsClick(ob, ob.findElement(By.cssSelector("a[id='HS_EndNoteLink_signedin']")));
		try {
		EndNoteSeesion(ob);
		BrowserWaits.waitTime(8);
		if(ob.findElements(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).size()>0){
			ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void EndNoteSeesion(WebDriver ob) throws Exception {
		String newWindow = switchToNewWindow(ob);
		System.out.println("Before Success1");
		if (newWindow != null) {
			System.out.println("Before Success2");
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("MARKETUSEREMAIL"),(LOGIN.getProperty("MARKETUSERPASSWORD")));
			BrowserWaits.waitTime(8);
			if (ob.getCurrentUrl().contains("app.qc.endnote.com") && ob.getTitle().contains("EndNote")) {
				System.out.println("Success with the url");
				logger.info("Neon Landing page is opened in the new window ");
			}
		} else {
			test.log(LogStatus.FAIL, "New window is not displayed and content is not matching");
		}

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		closeBrowser();
	}
}
