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
import util.ErrorUtil;
import util.ExtentManager;

public class IPA15 extends TestBase {

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
	public void validateSavedDataTitlesInfo() throws Exception {

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
			String ddesc = this.getClass().getSimpleName() + "_Save_Desc_" + RandomStringUtils.randomAlphanumeric(200);
			String newtitle = this.getClass().getSimpleName() + "_Updated_Save_Title" + "_"
					+ RandomStringUtils.randomAlphanumeric(20) + "-" + getCurrentTimeStamp();
			String searchtype = "technology";
			String searchTerm = "laser";
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			//pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("IPATESTUSER115"),LOGIN.getProperty("IPATESTUSER115pwd"));
			pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"),LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getSearchPageInstance(ob).SearchTermEnter(searchtype, searchTerm);
			pf.getSearchPageInstance(ob).exploreSearch();
			waitForAjax(ob);
			pf.getIpaPage(ob).clickOnSaveButton();
			pf.getIpaPage(ob).enterSavedatatitle(dtitle);
			test.log(LogStatus.PASS, "Title  has been entered for save data");
			pf.getIpaPage(ob).clickOnSaveData();
			test.log(LogStatus.PASS, "Searched data has been saved with title and without Descripton");
			BrowserWaits.waitTime(3);
			pf.getIpaSavedSearchpage(ob).clickOnSavedWork();
			pf.getIpaSavedSearchpage(ob).clickOnEditButton(dtitle);
			pf.getIpaPage(ob).SaveDataInfo(newtitle,ddesc);
			pf.getIpaSavedSearchpage(ob).clickOnSaveButtonInTile();
			test.log(LogStatus.PASS, "Title is updated with new title and desc");
			BrowserWaits.waitTime(3);
			try{
				Assert.assertTrue(pf.getIpaSavedSearchpage(ob).lenghtOfIileInfo(newtitle));	
				test.log(LogStatus.PASS,"Length of title and desc are matching ");
			}
			catch(Exception ex){
				ex.printStackTrace();
				test.log(LogStatus.FAIL,"Length of title is not matching: Expected value is 50 char but Actual value is 100 char ");
				ErrorUtil.addVerificationFailure(ex);
			}
			
			pf.getIpaSavedSearchpage(ob).clickOnDeleteButton(newtitle);
			test.log(LogStatus.PASS, "Selected Title is deleted in saved data list");
			pf.getDraPageInstance(ob).logoutDRA();
			closeBrowser();

		} catch (Exception e) {
			e.printStackTrace();
			logFailureDetails(test, "User is not able to update title", "Screenshot for login");
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
