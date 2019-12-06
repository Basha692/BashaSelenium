package ipa;

import java.util.Random;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import util.ExtentManager;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.LogStatus;

public class IPA18 extends TestBase {

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
	public void sortingOperations() throws Exception {

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
			// pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("IPATESTUSER001"),LOGIN.getProperty("IPATESTUSER001pwd"));
			pf.getIpaPage(ob).loginToIPA(LOGIN.getProperty("LOGINUSERNAME1"), LOGIN.getProperty("LOGINPASSWORD1"));
			pf.getIpaSavedSearchpage(ob).clickOnSavedWork();
			test.log(LogStatus.PASS, "navigated to saved data page");
			pf.getBrowserWaitsInstance(ob).waitUntilText("Your saved work");
			try {
				Assert.assertTrue(pf.getIpaSavedSearchpage(ob).verifySortOptions());
				test.log(LogStatus.PASS,
						"Member is able to see Sort by Drop down with options Date Saved and Date Viewed ");

			} catch (Throwable t) {
				logFailureDetails(test, t, "Sort Dropdown menu  is not dispalying correctly", "Drop_down_menu");
				closeBrowser();
			}

			Random rand = new Random();
			int value = rand.nextInt(5);
			pf.getIpaSavedSearchpage(ob).randomTile(value);
			waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_LABEL_CSS.toString()),
					60);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS);
			pf.getIpaSavedSearchpage(ob).clickOnSavedWork();
			pf.getBrowserWaitsInstance(ob).waitUntilText("Your saved work");
			pf.getIpaSavedSearchpage(ob).sortByDataSavedValue();
			test.log(LogStatus.PASS, "Values are sorted by Date Saved");
			pf.getIpaSavedSearchpage(ob).sortByDateviewedValue();
			test.log(LogStatus.PASS, "values are sorted by Date viewed ");
					pf.getDraPageInstance(ob).logoutDRA();
			closeBrowser();
		} catch (Exception e) {
			logFailureDetails(test, "User is not able apply sort in Saved data page", "Screenshot for login");
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}
}
