package Profile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class Profile39 extends TestBase {

	static int status = 1;
	static String postTitle;
	static List<String> profileDetails;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * 
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Profile");

	}

	@Test
	@Parameters({"username", "password"})
	public void testLoginTRAccount(String username,
			String password) throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			test.log(LogStatus.INFO, "Login Neon app with TR valid credentials");
			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
			test.log(LogStatus.INFO, "Logged in to NEON");
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");
			status = 2;// excel
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng

			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_loginNotDone")));// screenshot
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "testLoginTRAccount")
	@Parameters({"myPost", "postContent"})
	public void testPublishPost(String myPost,
			String postContent) throws Exception {
		try {
			postContent = postContent + RandomStringUtils.randomNumeric(10);
			test.log(LogStatus.INFO, " Click Publish A Post and Post your article");
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			profileDetails = pf.getProfilePageInstance(ob).getProfileTitleAndMetadata();
			pf.getProfilePageInstance(ob).clickPublishAPost();
			postTitle = myPost + RandomStringUtils.randomAlphanumeric(6);
			pf.getProfilePageInstance(ob).enterPostTitle(postTitle);
			test.log(LogStatus.INFO, "Entered Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postContent);
			test.log(LogStatus.INFO, "Entered Post Content");
			pf.getProfilePageInstance(ob).clickOnPostPublishButton();
			test.log(LogStatus.INFO, "Published the post and Validate Published Post count");
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			pf.getProfilePageInstance(ob).validatePostTitle(postTitle);
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");
			status = 2;// excel
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(e);// testng

			test.log(LogStatus.INFO, "Snapshot below: "
					+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "post_not_increased")));// screenshot
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "testPublishPost")
	public void testPostCommentCount() throws Exception {
		try {
			// int postCommentcount = pf.getProfilePageInstance(ob).getPostCommentCount();
			int postCommentcount = 0;
			pf.getProfilePageInstance(ob).clickFirstPostTitle();
			//BrowserWaits.waitTime(2);
			ob.navigate().refresh();
			//BrowserWaits.waitTime(4);
			pf.getpostRVPageInstance(ob).validatePostTitleAndProfileMetadata(postTitle, profileDetails);

			pf.getPostCommentPageInstance(ob).enterArticleComment("test comments added on post");
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();

			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			int postCommentAftercount = pf.getProfilePageInstance(ob).getPostCommentCount();

			logger.info("before count-->" + postCommentcount);
			logger.info("after count-->" + postCommentAftercount);
			if (postCommentAftercount == postCommentcount) {
				throw new Exception("post comment count failed");
			}

			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL, "Something unexpected happened");
			status = 2;// excel
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(e);// testng

			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "post_comment_count_failed")));// screenshot
			closeBrowser();
		}

	}

	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 */
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "PASS"); else if (status == 2) TestUtil.reportDataSetResult(profilexls,
		 * "Test Cases", TestUtil.getRowNum(profilexls, this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(profilexls, "Test Cases", TestUtil.getRowNum(profilexls,
		 * this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
