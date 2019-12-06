package enw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;

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

// Verify that user who is navigated to the community enabled version of 
//Endnote via RID navigation in page, upon clicking Account,Profile, feedback and Project Neon link in Profileflyout user 
//will be taken to ENW login page and after entering credentials it should take  to correct destination pages.
public class ENW021 extends TestBase {

	static int status = 1;
	String expectedUrl = "https://app.qc.endnote.com/EndNoteWeb.html";

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW021() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

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
			ob.navigate().to("https://rid-qa.researcherid.com/");
			logger.info(Inet4Address.getLocalHost().getHostAddress());
			loginToRID("MARKETUSEREMAIL", "MARKETUSERPASSWORD");
			//BrowserWaits.waitTime(3);
			try {
				if (!Inet4Address.getLocalHost().getHostAddress().startsWith("10.29")) {
					CessarNotEntiteledWithIP();
				} else {
					CessarEntiteledWithIP();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
			closeBrowser();

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
	}

	private void CessarNotEntiteledWithIP() throws Exception {
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.RID_ENDNOTE_LINK_XPATH.toString())));
		EndNoteSeesion(ob);
		pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin1(LOGIN.getProperty("MARKETUSEREMAIL"),
				(LOGIN.getProperty("MARKETUSERPASSWORD")));
		//BrowserWaits.waitTime(4);
		//BrowserWaits.waitTime(8);
		if (ob.getCurrentUrl().contains(expectedUrl)) {
			if (!ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).isDisplayed()) {
				test.log(LogStatus.FAIL, "Expected page is not displayed");
				Assert.assertEquals(true, false);
			} else {
				test.log(LogStatus.PASS, "Expected page is displayed and  Navigating to the proper Page.");
			}
		} else {
			test.log(LogStatus.FAIL, "Expected page is not displayed");
			Assert.assertEquals(true, false);
		}

	}

	private void CessarEntiteledWithIP() throws Exception {
		logger.info("CLicking the profile");
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.RID_ENDNOTE_LINK_XPATH.toString())));
		EndNoteSeesion(ob);
		// EndNoteSeesion(ob);
		//BrowserWaits.waitTime(8);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.IMAGE_USER_XPATH.toString())));
		try {
			loginAs("MARKETUSEREMAIL", "MARKETUSERPASSWORD");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//BrowserWaits.waitTime(8);
		if (ob.getCurrentUrl().contains(expectedUrl)) {
			if (!ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).isDisplayed()) {
				test.log(LogStatus.FAIL, "Expected page is not displayed");
				Assert.assertEquals(true, false);
			} else {
				test.log(LogStatus.PASS, "Expected page is displayed and  Navigating to the proper Page.");
			}
		} else {
			test.log(LogStatus.FAIL, "Expected page is not displayed");
			Assert.assertEquals(true, false);
		}
	}

	private void EndNoteSeesion(WebDriver ob) throws Exception {
		String newWindow = switchToNewWindow(ob);
		logger.info("Before Success1");
		if (newWindow != null) {
			logger.info("Before Success2");
			if (ob.getCurrentUrl().contains("app.qc.endnote.com") && ob.getTitle().contains("EndNote")) {
				logger.info("Success with the url");
				logger.info("ENW Landing page is opened in the new window ");
			}
		} else {
			test.log(LogStatus.FAIL, "New window is not displayed and content is not matching");
			throw new Exception("New window is not displayed and content is not matching");
		}

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
