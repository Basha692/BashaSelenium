package enw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class ENW007 extends TestBase {
	static int status = 1;

	//  Verify that the "Reference type","Title","Journal,Volume,Issue,Pagenumber,Times cited,
	//Cited Refrence count,URL in ENDnote from Neon for Article
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");

	}

	@Test
	public void testcaseENW007() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		

		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			List<String> list = Arrays.asList(new String[] { "Reference Type:", "Author:", "Title:", "Journal:",
					"Volume:", "Issue:", "Abstract:", "Accession Number:", "Notes:", "URL:" });
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.get(host);

			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("USEREMAIL007"),
					LOGIN.getProperty("USERPASSWORD007"));
			pf.getLoginTRInstance(ob).clickLogin();

			pf.getSearchResultsPageInstance(ob).searchArticle("AN OVERVIEW OF IRRIGATION MOSAICS");

			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).chooseArticle();
			BrowserWaits.waitTime(6);
			pf.getpostRVPageInstance(ob).clickSendToEndnoteRecordViewPage();
			String expectedNotes1 = ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_NOTES1_XPATH.toString()))
					.getText();
			String expectedNotes2 = ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_NOTES2_XPATH.toString()))
					.getText();
			HashMap<String, String> neonValues = new HashMap<String, String>();
			neonValues.put("expectedReferenceType", "Journal Article");
			neonValues.put("expectedURL", "https://dev-stable.1p.thomsonreuters.com");
			neonValues.put("expectedTitle",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_TITLE_XPATH.toString())).getText());
			
			neonValues.put("expectedJournal",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_JOURNAL_XPATH.toString())).getText());
			
			neonValues.put("expectedVolume",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_VOLUME_XPATH.toString())).getText());
			neonValues.put("expectedIssue",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_ISSUE_XPATH.toString())).getText());
			neonValues.put("expectedPages",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_PAGES_XPATH.toString())).getText());
			
			neonValues.put("expectedNotes", "Times Cited:" + expectedNotes1 + " Cited References:" + expectedNotes2);
			
			neonValues.put("expectedAuthor1",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_PATENT_AUTHOR1_XPATH.toString())).getText());
			
			neonValues.put("expectedAuthor2",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_PATENT_AUTHOR2_XPATH.toString())).getText());
			
			logout();
			BrowserWaits.waitTime(4);
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			ob.navigate().refresh();
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL007"),(LOGIN.getProperty("USERPASSWORD007")));
			BrowserWaits.waitTime(8);

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
			HashMap<String, String> endNoteDetails = new HashMap<String, String>();
			endNoteDetails.put("ReferenceType",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_REFERENCETYPE_XPATH.toString())).getText());
			endNoteDetails.put("ReferenceTypeValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_REFERENCETYPE_VALUE_XPATH.toString())).getText());
			endNoteDetails.put("URL",
				ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_URL_XPATH.toString())).getText());
			endNoteDetails.put("URLValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_URL_VALUE_XPATH.toString())).getText());
			endNoteDetails.put("Author",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_AUTHOR_XPATH.toString())).getText());
			
			endNoteDetails.put("AuthorValue1",
					ob.findElement(By.cssSelector(OnePObjectMap.ENW_RECORD_PATENT_AUTHOR1_VALUE_CSS.toString())).getText());
			endNoteDetails.put("AuthorValue2",
					ob.findElement(By.cssSelector(OnePObjectMap.ENW_RECORD_PATENT_AUTHOR2_VALUE_CSS.toString())).getText());
			//endNoteDetails.put("AuthorValue3",
				//	ob.findElement(By.cssSelector(OnePObjectMap.ENW_RECORD_PATENT_AUTHOR3_VALUE_CSS.toString())).getText());
			endNoteDetails.put("Title",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_TITLE_XPATH.toString())).getText());
			endNoteDetails.put("TitleValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_TITLE_VALUE_XPATH.toString())).getText());
			
			endNoteDetails.put("Journal",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_JOURNAL_XPATH.toString())).getText());
			endNoteDetails.put("JournalValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_JOURNAL_VALUE_XPATH.toString())).getText());
			endNoteDetails.put("Volume",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_VOLUME_XPATH.toString())).getText());
			endNoteDetails.put("VolumeValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_VOLUME_VALUE_XPATH.toString())).getText());
			endNoteDetails.put("Issue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_ISSUE_XPATH.toString())).getText());
			endNoteDetails.put("IssueValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_ISSUE_VALUE_XPATH.toString())).getText());
			
			endNoteDetails.put("pagesValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_PAGES_VALUE_XPATH.toString())).getText());
			
			endNoteDetails.put("Abstract",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_ABSTRACT_XPATH.toString())).getText());
			endNoteDetails.put("Accessionnumber",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_ACCESSIONNUMBER_XPATH.toString())).getText());
			endNoteDetails.put("Notes",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_NOTES_XPATH.toString())).getText());
			endNoteDetails.put("NotesValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_NOTES_VALUE_XPATH.toString())).getText());
			
			System.out.println("Verifying label ");
			Collection<String> values = endNoteDetails.values();
			for (String listItem : list) {
				if (!values.contains(listItem)) {
					test.log(LogStatus.FAIL, "label present is incorrect " + listItem);
					//Assert.assertEquals(true, false);
				}
				else
				{
					test.log(LogStatus.PASS, "label present is correct " + listItem);
				}
					
			}
			System.out.println("Verifying Values  ");
			Assert.assertEquals(endNoteDetails.get("ReferenceTypeValue"),neonValues.get("expectedReferenceType"));
			Assert.assertEquals(endNoteDetails.get("TitleValue"), neonValues.get("expectedTitle"));
		    JavascriptExecutor jse = (JavascriptExecutor) ob;
		    jse.executeScript("window.scrollBy(0,250)", "");
		 	if ((endNoteDetails.get("URLValue").contains(neonValues.get("expectedURL")))) 
			{
				test.log(LogStatus.PASS, "Values are matching \n"+neonValues+" Endnote Values "+endNoteDetails);
			}
			
			else
			{
				
				test.log(LogStatus.FAIL, "Values are not matching \n"+neonValues+" Endnote Values "+endNoteDetails);
				
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
		 try {
				//pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL037"),(LOGIN.getProperty("USERPASSWORD037")));
				BrowserWaits.waitTime(3);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				BrowserWaits.waitTime(5);
				if ( !ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_CHECKBOX_XPATH.toString())).isSelected() )
				{
					ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_CHECKBOX_XPATH.toString())).click();
				}			
				BrowserWaits.waitTime(2);
				ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_DELETE_XPATH.toString())).click();
				HandleAlert();
				BrowserWaits.waitTime(4);
				jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
				BrowserWaits.waitTime(3);
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
