package notifications;

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
import pages.ProfilePage;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class Notifications0019 extends TestBase {

	static int status = 1;

	PageFactory pf = new PageFactory();
	String postString = null;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");

	}

	@Test
	public void testcaseF19() throws Exception {

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
			openBrowser();
			maximizeWindow();
			clearCookies();

			ob.navigate().to(host);
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(4);
			ob.findElement(By.cssSelector("span[class='featured-post-link ng-isolate-scope']")).click();
			postString = "PostCreationTest" + RandomStringUtils.randomNumeric(10);
			logger.info("Publish Post-->" + postString);
			boolean poststatus = publishPost();
			BrowserWaits.waitTime(2);
			String str = null;
			if (poststatus) {
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("header_label")), 30);
				ob.findElement(By.xpath(OR.getProperty("header_label"))).click();
				ProfilePage page = new ProfilePage(ob);
				page.clickProfileLink();
				BrowserWaits.waitTime(4);
				str = ob.findElement(
						By.cssSelector("div[class='profile-tab-wrapper post-display'] a[class='ng-binding']"))
						.getText();
				logger.info("Publish Post -->" + str);
			}

			try {
				Assert.assertEquals(postString, str);
				test.log(LogStatus.PASS,
						"User is able to publish post by clicking Publish a post of your own link Feature post section on Home page.");
				logout();
				closeBrowser();
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"User is able to publish post by clicking Publish a post of your own link Feature post section on Home page.");// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "User_is_able_to_publish_post_by_clicking_Publish_a_post_of_your_own_link_Feature_post_section_on_Home_page.")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"User is able to publish post by clicking Publish a post of your own link Feature post section on Home page.");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
					.getSimpleName()
					+ "User_is_able_to_publish_post_by_clicking_Publish_a_post_of_your_own_link_Feature_post_section_on_Home_page")));// screenshot
			closeBrowser();
		}

	}

	private boolean publishPost() throws Exception {
		boolean status = false;
		try {

			// waitForElementTobeVisible(ob, By.xpath(OR.getProperty("home_page_publish_post_link")), 3000);
			// ob.findElement(By.xpath(OR.getProperty("home_page_publish_post_link"))).click();
			// ob.findElement(By.cssSelector("span[class='featured-post-link ng-isolate-scope']"));
			pf.getProfilePageInstance(ob).enterPostTitle(postString);
			logger.info("Entered Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postString);
			logger.info("Entered Post Content");
			pf.getProfilePageInstance(ob).clickOnPostPublishButton();
			logger.info("Published the post");
			BrowserWaits.waitTime(3);
			status = true;
		} catch (Exception e) {
			logger.error("Post creation problem" + e.getMessage());
			throw new Exception("Post creation problem" + e.getMessage());
		} /*
			 * finally { pf.getLoginTRInstance(ob).logOutApp(); }
			 */
		return status;
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if (status == 1) TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "PASS"); else if (status == 2)
		 * TestUtil.reportDataSetResult(notificationxls, "Test Cases", TestUtil.getRowNum(notificationxls,
		 * this.getClass().getSimpleName()), "FAIL"); else TestUtil.reportDataSetResult(notificationxls, "Test Cases",
		 * TestUtil.getRowNum(notificationxls, this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
