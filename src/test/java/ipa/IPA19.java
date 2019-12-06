package ipa;

import java.util.Random;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;

public class IPA19 extends TestBase {

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
	public void validationsOnRecordViewPage() throws Exception {

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
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendIPAAppUrl"));
			pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"),LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getSearchPageInstance(ob).SearchTermEnter("technology", "android");
			pf.getSearchPageInstance(ob).exploreSearch();
			waitForAjax(ob);
			pf.getDashboardPage(ob).selectTechPatentTAB();
			waitForAjax(ob);
			Random rand = new Random();
			int value = rand.nextInt(6);
			pf.getIpaRecordViewPage(ob).clickOnPatentTitle(value);
			pf.getIpaRecordViewPage(ob).validateAdditionalInfoTile(test);
			/*
			 * Assert.assertTrue(pf.getIpaRecordViewPage(ob).checkInfo());
			 * test.log(LogStatus.PASS,"Headings like Abstract,claims,Description are present in record view page");
			 */
			
			pf.getIpaRecordViewPage(ob).clickOnOriginalPatent(test);
			test.log(LogStatus.PASS, "Pdf file for patent is clicked");
			closeBrowser();
			

		} catch (Exception e) {
			e.printStackTrace();
			logFailureDetails(test, "Fields are not matching in record view page", "Screenshot for login");
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
