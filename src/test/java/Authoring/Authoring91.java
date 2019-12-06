package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;

public class Authoring91 extends TestBase {
	
	static int status = 1;
	static int time = 90;
	static int totalCommentsBeforeDeletion = 0;
	static int totalCommentsAfterDeletion = 0;
	static String url = "http://www.metacafe.com/watch/1837388/thomson_reuters_improves_searchability_for_blogs_publishers_wit/";
	
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		
	}
	
	@Test
	public void testAddNotSupportedVideosToPost() throws Exception {
		
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
			//BrowserWaits.waitTime(5);
			test.log(LogStatus.INFO, "Navigated to Profile Page");
			
			pf.getProfilePageInstance(ob).clickOnPublishPostButton();
			String title = RandomStringUtils.randomAlphabetic(20);
			pf.getProfilePageInstance(ob).enterPostTitle(title);
			String content = RandomStringUtils.randomAlphabetic(20);
			pf.getProfilePageInstance(ob).enterPostContent(content);
			pf.getProfilePageInstance(ob).AddVideoAndPublishAPost(url);
			
			try {
				ob.findElement(By.xpath("//span[@class='fr-video fr-fvc fr-dvb fr-draggable']/iframe")).isDisplayed();
					
			}catch(NoSuchElementException e){
				test.log(LogStatus.PASS, "User is not able to add video apart from Youtube and Vimeo");
				closeBrowser();
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