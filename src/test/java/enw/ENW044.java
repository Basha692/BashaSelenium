package enw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
public class ENW044 extends TestBase {
	static int status = 1;
	// Verify that the the body of the Post should be displayed under the Abstract field On 
	//Reference page Optional Fields in EndNote, after post record exported
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}
	@Test
	public void testcaseENW044() throws Exception {
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
			//NavigatingToENW();
			//BrowserWaits.waitTime(8);
			ob.get(host);
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("USEREMAIL044"),
					LOGIN.getProperty("USERPASSWORD044"));
			pf.getLoginTRInstance(ob).clickLogin();
			pf.getSearchResultsPageInstance(ob).searchArticle("Neon Testing2");
			pf.getSearchResultsPageInstance(ob).clickOnPostTab();
			waitForElementTobeClickable(ob, By.cssSelector(OR.getProperty("tr_search_results_post_title_css")), 60);
			// Navigating to record view page
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_post_title_css"))).click();
			//BrowserWaits.waitTime(3);
			pf.getpostRVPageInstance(ob).clickSendToEndnoteRecordViewPage();
			HashMap<String, String> neonValues = new HashMap<String, String>();
			neonValues.put("expectedName",
			ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_POST_NAME_XPATH.toString())).getText());
			neonValues.put("expectedAbstract",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_POST_ABSTRACT_XPATH.toString())).getText());
			logout();
			//BrowserWaits.waitTime(3);
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			ob.navigate().refresh();
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL044"),(LOGIN.getProperty("USERPASSWORD044")));
			//BrowserWaits.waitTime(8);
			test.log(LogStatus.PASS, "User navigate to End note");
			//BrowserWaits.waitTime(5);
			try {
				if (ob.findElements(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).size() != 0) {
					ob.findElement(By.xpath(OnePObjectMap.ENW_HOME_CONTINUE_XPATH.toString())).click();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//BrowserWaits.waitTime(5);
			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				//sortReferences();
				BrowserWaits.waitTime(5);
				List<WebElement> list = ob.findElements(By.xpath("//*[@title='Go to reference']"));
				 for(int i=0;i<list.size();){
					 if(list.get(i).getText().equals(neonValues.get("expectedName")))
						 System.out.println("List items in ENW:"+list.get(i).getText());
					 	System.out.println("List items from Neon:"+neonValues.get("expectedName"));
						 list.get(i).click();
					 break;
				 }
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			//BrowserWaits.waitTime(4);
	 		//pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_RECORD_LINK_XPATH);
			HashMap<String, String> endNoteDetails = new HashMap<String, String>();
						endNoteDetails.put("TitleofEntry",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_TITLE_ENTRY_XPATH.toString())).getText());
						endNoteDetails.put("Abstract",
								ob.findElement(By.cssSelector(OnePObjectMap.ENW_RECORD_ABSTRACT_VALUE_CSS.toString())).getText());
						//BrowserWaits.waitTime(4);
						
			if (!(endNoteDetails.get("TitleofEntry").equals(neonValues.get("expectedName")))) {
				//Assert.assertEquals(true, false);
				test.log(LogStatus.FAIL, "Abstract content is not matching between Neon and endnote");
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
				}
			else
			{
			test.log(LogStatus.PASS, "Post Name is matching and both are same after exporting the post data");
			}
						
//			
		
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
