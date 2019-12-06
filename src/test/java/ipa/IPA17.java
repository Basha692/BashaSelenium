package ipa;

import java.util.List;

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

public class IPA17 extends TestBase {

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
	public void saveCompanySearchData() throws Exception {

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
			String ddesc = this.getClass().getSimpleName() + "_Save_Desc_" + RandomStringUtils.randomAlphanumeric(170);
			String searchtype = "company";
			String searchTerm = "samsung";
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			//pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("IPATESTUSER111"),LOGIN.getProperty("IPATESTUSER111pwd"));
			pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"),LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getSearchPageInstance(ob).SearchTermEnter(searchtype, searchTerm);
			List<String> list=pf.getSearchPageInstance(ob).addCompanyTerms("1");
			pf.getSearchPageInstance(ob).checkForTextInSearchTermList(list.get(0));
			test.log(LogStatus.PASS, "Search term is matching");
			pf.getSearchPageInstance(ob).exploreSearch();
			waitForAjax(ob);
			pf.getIpaPage(ob).clickOnSaveButton();
			pf.getIpaPage(ob).SaveDataInfo(dtitle, ddesc);
			test.log(LogStatus.PASS, "Title and desc has been entered to save data");
			pf.getIpaPage(ob).clickOnSaveData();
			test.log(LogStatus.PASS, "Searched data has been saved");
			BrowserWaits.waitTime(3);
			pf.getIpaSavedSearchpage(ob).clickOnSavedWork();
			test.log(LogStatus.PASS, "navigated to saved data page");
			Assert.assertTrue(pf.getIpaSavedSearchpage(ob).validateSavedDataInfo(dtitle, searchtype));
			test.log(LogStatus.PASS, "Save data tile search type and title are Matching");

			pf.getIpaSavedSearchpage(ob).clickOnTitle(dtitle);
			test.log(LogStatus.PASS, "Explored the saved search");
		    waitForAjax(ob);
			pf.getSearchPageInstance(ob).checkForTextInSearchTermList(list.get(0));
			test.log(LogStatus.PASS, "Search term is matching after exploring saved search");
			pf.getDraPageInstance(ob).logoutDRA();
			closeBrowser();

		} catch (Exception e) {
			logFailureDetails(test, "User is not able to perform saved search", "Screenshot for login");
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
