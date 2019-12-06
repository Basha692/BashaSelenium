package notifications;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
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

public class Notifications0014 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");

	}

	@Test
	public void testcaseF14() throws Exception {
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
			ob.navigate().to(host);
			// Logging in with User2
			pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(8);
			test.log(LogStatus.INFO, " User 2 successfully logged in.");
			ob.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.SHIFT + "n");

			Set<String> myset1 = ob.getWindowHandles();
			Iterator<String> myIT1 = myset1.iterator();
			ArrayList<String> al1 = new ArrayList<String>();

			for (int i = 0; i < myset1.size(); i++) {

				al1.add(myIT1.next());

			}
			BrowserWaits.waitTime(4);
			ob.switchTo().window(al1.get(1));
			ob.navigate().to(host);
			test.log(LogStatus.INFO, " User 3 logging in");
			pf.getLoginTRInstance(ob).enterTRCredentials(user3, CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			BrowserWaits.waitTime(8);

			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("home_page_publish_post_link")), 3000);
			ob.findElement(By.xpath(OR.getProperty("home_page_publish_post_link"))).click();
			BrowserWaits.waitTime(5);
			pf.getProfilePageInstance(ob).enterPostTitle(postString);
			test.log(LogStatus.INFO, "Entered Post Title");
			pf.getProfilePageInstance(ob).enterPostContent(postString);
			test.log(LogStatus.INFO, "Entered Post Content");
			pf.getProfilePageInstance(ob).clickOnPostPublishButton();
			test.log(LogStatus.INFO, " User 3 published a post and user2 should get notification at the top");
			BrowserWaits.waitTime(6);
			ob.close();
			test.log(LogStatus.INFO, "Switching to user 2 session to check notification ");
			ob.switchTo().window(al1.get(0));
			BrowserWaits.waitTime(5);
			List<WebElement> listOfNotifications = null;
			for (int i = 1; i <= 3; i++) {
				ob.findElement(By.xpath(OR.getProperty("home_link"))).click();
				BrowserWaits.waitTime(8);
				listOfNotifications = ob.findElements(By.xpath(OR.getProperty("all_notifications_in_homepage")));
				if (listOfNotifications.size() > 0) {
					break;
				}
			}
			String text = listOfNotifications.get(0).getText();
			logger.info(text);

			try {
				Assert.assertTrue(!text.contains("Featured post"));
				test.log(LogStatus.PASS, "New Notification is at the top and featured post is moving down");
				closeBrowser();
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, "New Notification is not at First position");// extent
				// reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "_Title selected is not same in search text box")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something happened");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Title selected is not same in search text box")));// screenshot
			closeBrowser();
		}
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
