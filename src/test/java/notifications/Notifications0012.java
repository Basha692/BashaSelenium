package notifications;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class Notifications0012 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Notifications");
	}

	@Test
	public void testcaseF12() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {
			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName());
		logger.info(" execution starts--->");
		try {
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			pf.getLoginTRInstance(ob).waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(CONFIG.getProperty("defaultUsername"),
					CONFIG.getProperty("defaultPassword"));
			pf.getLoginTRInstance(ob).clickLogin();
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
			/*waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEWSFEED_FEATURED_POST_XPATH.toString()), 60,
					"Home page is not loaded successfully");*/
			test.log(LogStatus.INFO, "User Logged in  successfully");
			logger.info("Home Page loaded success fully");
			/*for (int i = 0; i < 3; i++) {
				List<WebElement> mylist = ob
						.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_XPATH.toString()));
				if (mylist.size() > 0) {
					break;
				} else {
					BrowserWaits.waitTime(5);
				}
			}
			List<WebElement> mylist = ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_LABEL_XPATH.toString()));
			WebElement myE = null;
			String text;
			for (int i = 0; i < mylist.size(); i++) {
				text = mylist.get(i).getText();
				if (text.contains("Recommended people to follow")) {
					logger.info(text);
					myE = mylist.get(i);
					logger.info("MyList : "+myE);
					break;
				}
			}*/
			pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_IMAGE_XPATH);
			BrowserWaits.waitTime(5);
			List<WebElement> people = ob.findElements(By.xpath(OnePObjectMap.NEWSFEED_RECOMMEND_PEOPLE_IMAGE_XPATH.toString()));
			logger.info("No of people=" + people.size());
			if (people.size()==6) {
				test.log(LogStatus.INFO, "Six people suggesstions are getting displayed");// extent reports
				test.log(LogStatus.PASS, "Pass");
			}else{
				throw new Exception("Six people suggesstions are not getting displayed");
			}
			closeBrowser();
		} catch (Throwable t) {
			
			test.log(LogStatus.FAIL, "Error--->" + t.getMessage());
			ErrorUtil.addVerificationFailure(t);
			logger.error(this.getClass().getSimpleName() + "--->" + t);
			status = 2;// excel
			test.log(LogStatus.FAIL, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Something happened")));// screenshot
			closeBrowser();
		}
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
	}

}
