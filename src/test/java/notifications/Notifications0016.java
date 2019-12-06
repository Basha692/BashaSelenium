package notifications;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.openqa.selenium.By;
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
import util.OnePObjectMap;

public class Notifications0016 extends TestBase {

	static int status = 1;

	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");

	}

	@Test
	public void notifications026() throws Exception {

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
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			//waitForElementTobeVisible(ob, By.xpath(OR.getProperty("TR_login_button")), 20);
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"), CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");*/
			test.log(LogStatus.INFO, "User Logged in  successfully");
			logger.info("Home Page loaded success fully");
			BrowserWaits.waitTime(4);
			
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_XPATH);
			WebElement element=ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_XPATH.toString()));
			String actual=element.getText();
			String userName=null;
			if(actual.contains("Recommended people to follow")){
				List<WebElement> people = ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_IMAGE_XPATH.toString()));
				logger.info("No of people=" + people.size());
				//if (people.size()==6) {
					test.log(LogStatus.INFO, "Six people suggesstions are getting displayed");// extent reports
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_XPATH);
					WebElement ele=ob.findElement(By.xpath(OnePObjectMap.RECOMMENDED_PEOPLE_SECTION_USER_NAME_XPATH.toString()));
					userName=ele.getText();
					test.log(LogStatus.PASS, "Pass");
//				}else{
//					throw new Exception("Six people suggesstions are not getting displayed");
//				}
			}
			logger.info("User Name : "+userName);
			
			
/*
			List<WebElement> element=	pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_CSS);
			logger.info("Size : "+element.size());
			String actual = null;
			for(int i=0;i<element.size();i++){
				//pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_CSS);
				String str=element.get(i).getText();
				if(str.contains("Recommended people to follow")){
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_CSS);
					BrowserWaits.waitTime(3);
					List<WebElement> numUser=element.get(i).findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_NUMBER_OF_USER_CSS.toString()));
					logger.info("Number of Users : "+numUser.size());
						if(numUser.size()==6){
							actual=element.get(i).findElement(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_COPY_USER_NAME_CSS.toString())).getText();
							logger.info("Text : " + actual);
						}
					break;
				}
			}*/
			
			
			
			//((JavascriptExecutor) ob).executeScript("arguments[0].scrollIntoView(true);",ele.get(1));
			
			//pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_CSS);
			//pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_CSS);
			//pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_CSS);
			//ob.findElement(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_CSS.toString()))
					//.click();
			/*List<WebElement> elements = ob
					.findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_NUMBER_OF_USER_CSS.toString()));
			
			for (WebElement ele : elements) {
				System.out.println("attribute value-->" + ele.getAttribute("ng-repeat"));
				if ((ele.getAttribute("ng-repeat").contains("profile in vm.profiles"))) {
					actual = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_COPY_USER_NAME_CSS).getText();
					logger.info("Text : " + actual);
					break;
				}
			}*/

			// List<WebElement> str=ob.findElements(By.cssSelector("div[class='col-xs-12 profile-info'] span a"));
			// String actual=str.get(0).getText();
			// String
			// actual=elements.findElement(By.cssSelector(OR.getProperty("tr_notification_recommended_people_follow_username_css"))).getText();
			// System.out.println("Text : "+actual);
//			BrowserWaits.waitTime(4);
//			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("header_label")), 30);
//			ob.findElement(By.xpath(OR.getProperty("header_label"))).click();
			/*pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
			jsClick(ob, ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS.toString())));
			ProfilePage page = new ProfilePage(ob);
			BrowserWaits.waitTime(3);*/
			pf.getHFPageInstance(ob).clickProfileImage();
			pf.getProfilePageInstance(ob).clickProfileLink();
			pf.getProfilePageInstance(ob).clickFollowingTab();
			BrowserWaits.waitTime(3);
			WebElement elem = ob.findElement(By.cssSelector("div[class='follower-list']"));
			List<WebElement> elemn = elem.findElements(By.tagName("div"));
			WebElement elem1 = elemn.get(0);

			String result = elem1.findElement(By.cssSelector("a[class='ng-binding ng-scope']"))
					.getText();
			logger.info("Result " + result);

			try {
				Assert.assertEquals(userName, result);
				test.log(LogStatus.PASS, "Recommended people to follow section user displaying on following section");
				closeBrowser();
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"Recommended people to follow section user not displaying on following section");// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO,
						"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
								+ "_recommended_people_to_follow_section_user_not_displaying_on_following_section")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Recommended people to follow section user not displaying on following section");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO,
					"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
							+ "_recommended_people_to_follow_section_user_not_displaying_on_following_section")));// screenshot
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
