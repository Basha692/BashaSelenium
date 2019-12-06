package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

/**
 * Class for Performing Article Sharing on LinkedIn
 * 
 * @author UC202376
 *
 */
public class Authoring12 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		runmodes = TestUtil.getDataSetRunmodes(authoringxls, this.getClass().getSimpleName());
	}

	@Test
	public void testOpenApplication() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts for data set #" + count + "--->");
		// selenium code
		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(System.getProperty("host"));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Unable_to_share_the_Article")));
			closeBrowser();
		}
	}

	@Test(dependsOnMethods = "testOpenApplication")
	@Parameters({"username", "password", "article", "completeArticle"})
	public void chooseArtilce(String username,
			String password,
			String article,
			String completeArticle) throws Exception {
		try {
			// waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
			pf.getSearchResultsPageInstance(ob).searchArticle(article);
			pf.getSearchResultsPageInstance(ob).chooseArticle();

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Unable_to_share_the_Article")));
			closeBrowser();
		}
	}

	@Test(dependsOnMethods = "chooseArtilce")
	@Parameters({"liusername", "lipassword"})
	public void shareOnTwitter(String liusername,
			String lipassword) throws Exception {
		try {
			test.log(LogStatus.INFO, "Sharing Article on LinkedIn");
			String PARENT_WINDOW = ob.getWindowHandle();
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_LI_CSS.toString()), 40);
			jsClick(ob, ob.findElement(
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_LI_CSS.toString())));

			// pf.getBrowserActionInstance(ob).click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_SHARE_ON_LI_LINK);
			//waitForElementTobeVisible(ob, By.cssSelector("div[class='modal-dialog']"), 40);
			jsClick(ob, ob.findElement(By.cssSelector(
					"div[class='modal-footer wui-modal__footer ng-scope'] button[data-ng-click='shareModal.close()']")));

			waitForNumberOfWindowsToEqual(ob, 2);
			Set<String> child_window_handles = ob.getWindowHandles();
			System.out.println("window hanles-->" + child_window_handles.size());
			for (String child_window_handle : child_window_handles) {
				if (!child_window_handle.equals(PARENT_WINDOW)) {
					ob.switchTo().window(child_window_handle);
					System.out.println("child window--->" + ob.getTitle());
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
							OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_USERNAME_CSS);
					pf.getBrowserActionInstance(ob).enterFieldValue(
							OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_USERNAME_CSS, liusername);
					pf.getBrowserActionInstance(ob).enterFieldValue(
							OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_PASSWORD_CSS, lipassword);
					pf.getBrowserActionInstance(ob)
							.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_LI_LOGIN_CSS);
					ob.switchTo().window(PARENT_WINDOW);
					waitForElementTobeVisible(ob,
							By.cssSelector(
									"div[class='modal-footer wui-modal__footer ng-scope'] button[data-ng-click='shareModal.cancel()']"),
							40);
					jsClick(ob, ob.findElement(By.cssSelector(
							"div[class='modal-footer wui-modal__footer ng-scope'] button[data-ng-click='shareModal.cancel()']")));
				}
			}

			Thread.sleep(5000);// Wait for new window to close and switch to old
								// window.
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			status = 2;// excel
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Unable_to_Tweet_Article_On_Twitter")));
			closeBrowser();
		}

	}

	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
