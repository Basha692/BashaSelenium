package enw;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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

public class ENW036 extends TestBase {
	
	static int status = 1; 
	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}
    
	@Test
	public void testcaseENW002() throws Exception {
		
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
			
			ob.get(host);
			String expectedGroupAuthors="TON Team";
			
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("grpAuthUserenw036"),
					LOGIN.getProperty("grpAuthPwdenw036"));
			pf.getLoginTRInstance(ob).clickLogin();
			
			pf.getSearchResultsPageInstance(ob).searchArticle("Dissipation and emission of p-mode in the quiet sun from acoustic imaging with TON data");
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).chooseArticle();
			//BrowserWaits.waitTime(4);
			
			waitForAjax(ob);
			pf.getSearchResultsPageInstance(ob).clickSendToEndnoteSearchPage();
			
			//pf.getHFPageInstance(ob).clickOnEndNoteLink();
			logout();
			//BrowserWaits.waitTime(3);
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			ob.navigate().refresh();
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("grpAuthUserenw036"),(LOGIN.getProperty("grpAuthPwdenw036")));
			//BrowserWaits.waitTime(3);
			
			try {
				if (ob.findElements(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).size() != 0) {
						ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).click();
					}
				test.log(LogStatus.PASS, "User navigate to End note");
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				BrowserWaits.waitTime(4);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_RECORD_LINK_XPATH);
				BrowserWaits.waitTime(4);
			} catch (Exception e) {

				e.printStackTrace();
			}
			//pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_SHOWALLFILEDS_LINK_XPATH);
			String actualGroupAuthors=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_RECORD_AUTHOR_VALUE_CSS).getText();
			try
			{
			Assert.assertEquals(expectedGroupAuthors,actualGroupAuthors);
			test.log(LogStatus.PASS,
					" Group Author for article record displayed correctly");
			}
			catch (Throwable t) {
				test.log(LogStatus.FAIL,
						" Group Author for article record is not displayed correctly");// extent
				// reports
				status = 2;// excel
				test.log(
						LogStatus.INFO,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_GROUPAuthor_not_displayed")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
   			NavigatingToENW();
			closeBrowser();
			} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
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
	private void NavigatingToENW() {
		// ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
		 try {
			//pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL037"),(LOGIN.getProperty("USERPASSWORD037")));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
			//BrowserWaits.waitTime(5);
			waitForElementTobeVisible(ob, By.xpath(".//*[@id='idCheckAllRef']"), 60);
			if ( !ob.findElement(By.xpath(".//*[@id='idCheckAllRef']")).isSelected() )
			{
				ob.findElement(By.xpath(".//*[@id='idCheckAllRef']")).click();
			}			
			//BrowserWaits.waitTime(2);
			waitForElementTobeVisible(ob, By.xpath(".//*[@id='idDeleteTrash']"), 60);
			ob.findElement(By.xpath(".//*[@id='idDeleteTrash']")).click();
			HandleAlert();
			//BrowserWaits.waitTime(4);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH);
			jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH.toString())).click();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	private void HandleAlert() {
		Alert alert=ob.switchTo().alert();		
		String alertMessage=ob.switchTo().alert().getText();		
        System.out.println(alertMessage);			
        alert.accept();	
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}
	
}
