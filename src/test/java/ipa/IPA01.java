package ipa;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ExtentManager;

public class IPA01 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IPA");
	}

	@Test
	public void saveTechnologySearchData() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
					+ " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {
			String dtitle = this.getClass().getSimpleName() + "_Save_Title" + "_" + getCurrentTimeStamp();
			String ddesc = this.getClass().getSimpleName() + "_Save_Desc_" + RandomStringUtils.randomAlphanumeric(150);
			String searchtype = "technology";
			String searchTerm = "android";
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			//pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("IPATESTUSER001"),LOGIN.getProperty("IPATESTUSER001pwd"));
			pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"),LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getSearchPageInstance(ob).SearchTermEnter(searchtype, searchTerm);
			pf.getSearchPageInstance(ob).selectSearchTermFromSuggestion(2);
			pf.getSearchPageInstance(ob).exploreSearch();
			pf.getSearchPageInstance(ob).checkForTextInSearchTermList(searchTerm);
			test.log(LogStatus.PASS, "Search term is matching");
			waitForAjax(ob);
			pf.getIpaPage(ob).clickOnSaveButton();
			pf.getIpaPage(ob).SaveDataInfo(dtitle, ddesc);
			test.log(LogStatus.PASS, "Title and desc has been entered to save data");
			pf.getIpaPage(ob).clickOnSaveData();
			test.log(LogStatus.PASS, "Searched data has been saved");
			BrowserWaits.waitTime(4);
			pf.getIpaSavedSearchpage(ob).clickOnSavedWork();
			test.log(LogStatus.PASS, "navigated to saved data page");
			Assert.assertTrue(pf.getIpaSavedSearchpage(ob).validateSavedDataInfo(dtitle, searchtype));
			test.log(LogStatus.PASS, "Save data tile search type and title are Matching");
			pf.getIpaSavedSearchpage(ob).clickOnTitle(dtitle);
			test.log(LogStatus.PASS, "Explored the saved search");
			waitForAjax(ob);
			pf.getSearchPageInstance(ob).checkForTextInSearchTermList(searchTerm);
			test.log(LogStatus.PASS, "Search term is matching after exploring the saved search");
			pf.getDraPageInstance(ob).logoutDRA();
			closeBrowser();

		} catch (Exception e) {
			e.printStackTrace();
			logFailureDetails(test, "User is not able to save data", "Screenshot for login");
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
