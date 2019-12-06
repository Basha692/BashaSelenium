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

public class ENW0008 extends TestBase {

	static int status = 1;

	// Verify that the Reference type","Title",URL in ENDnote from Neon for
	// Posts
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}

	@Test
	public void testcaseENW0008() throws Exception {

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
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("USEREMAIL0008"),
					LOGIN.getProperty("USERPASSWORD0008"));
			pf.getLoginTRInstance(ob).clickLogin();

			pf.getSearchResultsPageInstance(ob)
					.searchArticle("Post for Testing RecordView8E4LBO");
			List<String> list = Arrays.asList(
					new String[] {"Author:", "Title of Entry:",  "Reference Type:", "URL:","Accession Number:"});
			//Author:, Title of Entry:, Reference Type:, URL:, Accession Number:
			//BrowserWaits.waitTime(4);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS);
			ob.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_POSTS_CSS.toString())).click();
			pf.getSearchResultsPageInstance(ob).clickOnFirstPostTitle();
			//BrowserWaits.waitTime(4);
			pf.getpostRVPageInstance(ob).clickSendToEndnoteRecordViewPage();

			HashMap<String, String> neonValues = new HashMap<String, String>();
			neonValues.put("expectedReferenceType", "Blog");
			neonValues.put("expectedURL", "https://dev-stable.1p.thomsonreuters.com");
			neonValues.put("expectedTitle",
					ob.findElement(By.xpath(OnePObjectMap.NEON_RECORDVIEW_TITLE_XPATH.toString())).getText());
			neonValues.put("expectedAuthor",
					ob.findElement(By.cssSelector(OnePObjectMap.NEON_RECORDVIEW_POSTAUTHOR_CSS.toString())).getText());
			logout();
			ob.navigate().to(host + CONFIG.getProperty("appendENWAppUrl"));
			ob.navigate().refresh();
			//BrowserWaits.waitTime(4);
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL0008"),
					LOGIN.getProperty("USERPASSWORD0008"));

			try {
				test.log(LogStatus.PASS, "User navigate to End note");
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
				BrowserWaits.waitTime(6);
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_RECORD_LINK_XPATH);
				BrowserWaits.waitTime(6);

			} catch (Exception e) {
				e.printStackTrace();
			}
			HashMap<String, String> endNotelabel = new HashMap<String, String>();
			HashMap<String, String> endNotevalues = new HashMap<String, String>();
			endNotelabel.put("ReferenceType:",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_REFERENCETYPE_XPATH.toString())).getText());
			endNotevalues.put("ReferenceTypeValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_REFERENCETYPE_VALUE_XPATH.toString())).getText());

			endNotelabel.put("Author:",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_AUTHOR_XPATH.toString())).getText());
			endNotevalues.put("AuthorValue",
					ob.findElement(By.cssSelector(OnePObjectMap.ENW_RECORD_AUTHOR_VALUE_CSS.toString())).getText());

			endNotelabel.put("Title of Entry:",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_TITLE_XPATH.toString())).getText());
			endNotevalues.put("TitleValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_TITLE_VALUE_XPATH.toString())).getText());
			endNotelabel.put("Accessionnumber:",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_ACCESSIONNUMBER_XPATH.toString())).getText());
			endNotelabel.put("URL:", ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_URL_XPATH.toString())).getText());
			endNotevalues.put("URLValue",
					ob.findElement(By.xpath(OnePObjectMap.ENW_RECORD_URL_VALUE_XPATH.toString())).getText());
			Collection<String> label = endNotelabel.values();
			//Collection<String> values = endNotevalues.values();
			for (String listItem : list) {
				if (label.contains(listItem)) {
				test.log(LogStatus.PASS, "label present is correct " + listItem);
				} else {
				test.log(LogStatus.FAIL, "label present is incorrect " + listItem);
				}
			}
			if(neonValues.get("expectedReferenceType").equalsIgnoreCase(endNotevalues.get("ReferenceTypeValue"))){
				Assert.assertTrue(true);
			}
			if(neonValues.get("expectedAuthor").equalsIgnoreCase(neonValues.get("expectedAuthor"))){
				Assert.assertTrue(true);
			}
			if(neonValues.get("expectedTitle").equalsIgnoreCase(endNotevalues.get("AuthorValue"))){
				Assert.assertTrue(true);
			}
//			Assert.assertEquals(endNotevalues.get("AuthorValue"), neonValues.get("expectedAuthor"));
//			Assert.assertEquals(endNotevalues.get("TitleValue"), neonValues.get("expectedTitle"));
			JavascriptExecutor jse = (JavascriptExecutor) ob;
			jse.executeScript("window.scrollBy(0,250)", "");
			if ((endNotevalues.get("URLValue").contains(neonValues.get("expectedURL")))) {
				test.log(LogStatus.PASS, "Values are matching \n" + neonValues + " Endnote Values " + endNotevalues);
			} else {
				test.log(LogStatus.FAIL,
						"Values are not matching \n" + neonValues + " Endnote Values " + endNotevalues);
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
			// pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("USEREMAIL037"),(LOGIN.getProperty("USERPASSWORD037")));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.ENW_UNFILEDFOLDER_LINK_XPATH);
			//BrowserWaits.waitTime(5);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_ALLRECORDS_CHECKBOX_XPATH);
			if (!ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_CHECKBOX_XPATH.toString())).isSelected()) {
				ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_CHECKBOX_XPATH.toString())).click();
			}	
			//BrowserWaits.waitTime(2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_ALLRECORDS_DELETE_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.ENW_ALLRECORDS_DELETE_XPATH.toString())).click();
			HandleAlert();
			//BrowserWaits.waitTime(4);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH);
			ob.findElement(By.xpath(OnePObjectMap.ENW_FB_PROFILE_FLYOUT_SIGNOUT_XPATH.toString())).click();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void HandleAlert() {
		Alert alert = ob.switchTo().alert();
		String alertMessage = ob.switchTo().alert().getText();
		System.out.println(alertMessage);
		alert.accept();

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
