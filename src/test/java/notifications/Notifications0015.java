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

public class Notifications0015 extends TestBase {

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
	public void notifications015() throws Exception {

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
			// Logging in with Default user
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 120,
					"Home page is not loaded successfully");*/
			test.log(LogStatus.INFO, "User Logged in  successfully");
			BrowserWaits.waitTime(4);
			
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_XPATH);
			WebElement element=ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_XPATH.toString()));
			String actual=element.getText();
			String userName=null;
			if(actual.contains("Recommended people to follow")){
				List<WebElement> people = ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_IMAGE_XPATH.toString()));
				logger.info("No of people=" + people.size());
				if (people.size()==6) {
					test.log(LogStatus.INFO, "Six people suggesstions are getting displayed");// extent reports
					WebElement ele=ob.findElement(By.xpath(OnePObjectMap.RECOMMENDED_PEOPLE_SECTION_USER_NAME_XPATH.toString()));
					userName=ele.getText();
					ele.click();
					test.log(LogStatus.PASS, "Pass");
				}else{
					throw new Exception("Six people suggesstions are not getting displayed");
				}
			}
			logger.info("User Name : "+userName);
			
			/*List<WebElement> element=	pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_CSS);
			logger.info("Size : "+element.size());
			String actual = null;
			for(int i=0;i<element.size();i++){
				pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_CSS);
				String str=element.get(i).getText();
				if(str.contains("Recommended people to follow")){
					pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_FOLLOW_USER_CSS);
					BrowserWaits.waitTime(3);
					List<WebElement> numUser=element.get(i).findElements(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_NUMBER_OF_USER_CSS.toString()));
					logger.info("Number of Users : "+numUser.size());
						if(numUser.size()==6){
						WebElement	actual1=element.get(i).findElement(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_COPY_USER_NAME_CSS.toString()));
						actual=actual1.getText();
							logger.info("Text : " + actual);
							actual1.click();
						}
					break;
				}
			}*/
			
		/*	WebElement elment=ob.findElement(By.xpath(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_XPATH.toString()));
			String text=elment.getText();
			logger.info("Rcommended People Section Text : "+text);
			try{
				Assert.assertTrue(text.contains("Recommended people to follow"));
				test.log(LogStatus.INFO, "Recommended people to follow section in home page");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Recommended people to follow section not found in Newsfeed page.");// extent
				test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
				test.log(LogStatus.FAIL, "Snapshot below: " + test
						.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
				closeBrowser();
			}*/
			
			/*WebElement recPeople=ob.findElement(By.cssSelector(OnePObjectMap.NEWSFEED_RECOMMENDED_PEOPLE_SECTION_COPY_USER_NAME_CSS.toString()));
			String recPeopleUserNaem=recPeople.getText();
			logger.info("Recommended People Section User Name : "+recPeopleUserNaem);
			recPeople.click();
			*/
//			//ob.findElement(By.cssSelector(OR.getProperty("tr_notification_recommended_people_follow_user_css")));
//			List<WebElement> elements = ob
//					.findElements(By.cssSelector(OR.getProperty("tr_notification_recommended_people_follow_css")));
//			String actual = null;
//			for (WebElement ele : elements) {
//				System.out.println("attribute value-->" + ele.getAttribute("ng-if"));
//				if (!(ele.getAttribute("ng-if").contains("TopUserCommenters"))) {
//					actual = ele
//							.findElement(By.cssSelector(
//									OR.getProperty("tr_notification_recommended_people_follow_username_css")))
//							.getText();
//					System.out.println("Text : " + actual);
//					ele.findElement(
//							By.cssSelector(OR.getProperty("tr_notification_recommended_people_follow_username_css")))
//							.click();
//					break;
//				}
//
//			}
			BrowserWaits.waitTime(4);
			String profilePageUserName=ob.findElement(By.cssSelector(OnePObjectMap.PROFILE_PAGE_AUTOR_NAME_CSS.toString())).getText();
			logger.info("Profile Page User Name :  " + profilePageUserName);

			try {
				Assert.assertEquals(userName, profilePageUserName);
				test.log(LogStatus.PASS,
						"User is displaying profile page by clicking author name in Recommended people to follow section on home page");
				closeBrowser();
			} catch (Throwable t) {

				test.log(LogStatus.FAIL,
						"User is not displaying profile page by clicking author name in Recommended people to follow section on home page");// extent
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				test.log(LogStatus.INFO, errors.toString()); // reports
				test.log(LogStatus.INFO, "Error--->" + t);
				ErrorUtil.addVerificationFailure(t);
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
						.getSimpleName()
						+ "User_is_not_displaying_profile_page_by_clicking_author_name_in_Recommended_people_to_follow_section_on_home_page")));// screenshot
				closeBrowser();
			}

		} catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"User is not displaying profile page by clicking author name in Recommended people to follow section on home page");// extent
			// reports
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass()
					.getSimpleName()
					+ "User_is_not_displaying_profile_page_by_clicking_author_name_in_Recommended_people_to_follow_section_on_home_page")));// screenshot
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
