package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Authoring63 extends TestBase {

	static int status = 1;
	PageFactory pf = new PageFactory();

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");

	}

	@Test
	public void testEditDraftsFromModalWindow() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			String postString = "PostCreationTest" + RandomStringUtils.randomNumeric(10);
			openBrowser();
			maximizeWindow();
			clearCookies();

			// Navigate to TR login page and login with valid TR credentials
			// ob.navigate().to(host);
			ob.get(CONFIG.getProperty("testSiteName"));

			loginAs("USERNAME3", "PASSWORD3");
			test.log(LogStatus.INFO, "Logged in to NEON");
			pf.getHFPageInstance(ob).clickOnProfileLink();
			//BrowserWaits.waitTime(10);
			pf.getHFPageInstance(ob).clickOnProfileLink();
			test.log(LogStatus.INFO, "Navigated to Profile Page");
			int postCountBefore = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountBefore);
			pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			pf.getProfilePageInstance(ob).enterPostTitle(postString);
			test.log(LogStatus.INFO, "Entered Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postString);
			test.log(LogStatus.INFO, "Entered Post Content");
			pf.getProfilePageInstance(ob).clickOnPostCancelButton();
			pf.getProfilePageInstance(ob).clickOnPostCancelKeepDraftButton();
			test.log(LogStatus.INFO, "Saved the Post as a draft");
			pf.getHFPageInstance(ob).clickOnProfileLink();
			pf.getProfilePageInstance(ob).clickOnDraftPostsTab();
			int postCountAfter = pf.getProfilePageInstance(ob).getDraftPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountAfter);

			String postTitle = ob
					.findElement(By
							.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_FIRST_TITLE_CSS.toString()))
					.getText().trim();
			try {
				Assert.assertTrue(postCountAfter == (postCountBefore + 1) && postString.equals(postTitle));
				test.log(LogStatus.PASS, "Draft Post section is present in the user profile");

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Draft Post section is not present in user profile");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}

			pf.getHFPageInstance(ob).searchForText("Sachin Traveller");
			pf.getSearchProfilePageInstance(ob).clickPeople();
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.HOME_PROJECT_PEOPLE_LINK.toString()), 90);
			ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_PEOPLE_LINK.toString())).click();
			try {
				waitForAjax(ob);
				boolean isPresent = ob
						.findElement(
								By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_DRAFT_POST_COUNT_CSS.toString()))
						.isDisplayed();
				Assert.assertTrue(!isPresent);
				test.log(LogStatus.PASS, "Draft Post section is not present in other user profile");
			} catch (Exception e) {

				test.log(LogStatus.PASS, "Draft Post section is not present in other user profile");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Draft Post section is present in other user profile");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}
			//BrowserWaits.waitTime(10);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			status = 2;// excel
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

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(authoringxls, "Test Cases", TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(authoringxls,
		 * "Test Cases", TestUtil.getRowNum(authoringxls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" , TestUtil.getRowNum(authoringxls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
