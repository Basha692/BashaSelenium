package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class Authoring94 extends TestBase {
	
	static int status = 1;
	static int time = 90;
	static int totalCommentsBeforeDeletion = 0;
	static int totalCommentsAfterDeletion = 0;
	static String url = "https://www.youtube.com/watch?v=kP88lNAmHXA";
	
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		
	}
	
	@Test
	public void testAddVideosToPost() throws Exception {
		
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
			// Navigate to TR login page and login with valid TR credentials
			ob.navigate().to(host);
			loginAs("LOGINUSERNAME1", "LOGINPASSWORD1");
			test.log(LogStatus.INFO, "Logged in to NEON");
			pf.getHFPageInstance(ob).clickOnProfileLink();
			BrowserWaits.waitTime(5);
			test.log(LogStatus.INFO, "Navigated to Profile Page");
			int postCountBefore = pf.getProfilePageInstance(ob).getPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountBefore);
			pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			String title = RandomStringUtils.randomAlphabetic(20);
			pf.getProfilePageInstance(ob).enterPostTitle(title);
			String content = RandomStringUtils.randomAlphabetic(20);
			pf.getProfilePageInstance(ob).enterPostContent(content);
			while(true)
			{
				if (ob.findElement(By.xpath(OnePObjectMap.HOME_PROJECT_NEON_ADD_IMAGE_BTN_XPATH.toString())).isDisplayed())
					{
						pf.getProfilePageInstance(ob).AddImageToPost();
						break;
					}
				else
				{
					ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_CREATE_POST_CONTENT_CSS.toString()))
					.sendKeys(Keys.ENTER);
				}				
			}
			BrowserWaits.waitTime(5);
			pf.getProfilePageInstance(ob).clickOnPostPublishButton();
			waitForAjax(ob);
			BrowserWaits.waitTime(3);
			//ob.navigate().refresh();
			pf.getHFPageInstance(ob).clickOnProfileLink();
			BrowserWaits.waitTime(5);
			int postCountAfter = pf.getProfilePageInstance(ob).getPostsCount();
			test.log(LogStatus.INFO, "Post count:" + postCountAfter);

			try {
				Assert.assertEquals(postCountBefore + 1, postCountAfter);
				test.log(LogStatus.PASS, "Post count is incremented after the new post creation");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "Post count is not incremented after the new post creation");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_count_validation_failed")));// screenshot

			}
			pf.getProfilePageInstance(ob).clickOnFirstPost();
			BrowserWaits.waitTime(10);
			try {
				Assert.assertEquals(pf.getpostRVPageInstance(ob).getPostTitle(), title);
				Assert.assertEquals(pf.getpostRVPageInstance(ob).getPostContent(), content);
				//Commented as this is blocked as of now
//				if(ob.findElement(By.xpath("//div[p[text()=content]]/descendant::img]")).isDisplayed())
//					test.log(LogStatus.PASS, "Published post has image");
				closeBrowser();

			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "User is not able to publish the post with a Image");
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "Post_title_with_Image_validation_failed")));// screenshot
			}
			
			
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
		
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}

}