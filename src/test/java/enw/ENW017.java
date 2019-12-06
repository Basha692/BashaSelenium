package enw;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
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

public class ENW017 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENW");
	}
	@Test
	public void testcaseENW017() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {

			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			//String header_Expected = "Thomson Reuters";
			//String expected_URL = "Thank You";
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			pf.getOnboardingModalsPageInstance(ob).ENWSTeamLogin(LOGIN.getProperty("MARKETUSEREMAIL"),(LOGIN.getProperty("MARKETUSERPASSWORD")));
			//BrowserWaits.waitTime(5);
			//pf.getBrowserWaitsInstance(ob).waitUntilText("Thomson Reuters", "EndNote", "Downloads", "Options");

//			String actual_result = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.ENW_HEADER_XPATH).getText();
//			logger.info("Header Text displayed as:" + actual_result);
//				logger.info("Actual result displayed as :" + actual_result
//					+ " text without the hot link and not allow user to Navigate to Neon");
//				
//			try {
//				Assert.assertEquals(header_Expected, actual_result);
//				test.log(LogStatus.PASS, "community enabled version of ENDNOTE logo has been displayed for Market users");
//			} catch (Throwable t) {
//				test.log(LogStatus.FAIL, "community enabled version of ENDNOTE logo is not displayed for Market users");// extent
//				ErrorUtil.addVerificationFailure(t);// testng reports
//				status = 2;// excel
//				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
//						this.getClass().getSimpleName() + "Header Text is displayed wrongly and its Hyperlinked")));// screenshot
//			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_PROFILE_USER_ICON_XPATH.toString())));
			//BrowserWaits.waitTime(3);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.ENW_FEEDBACK_XPATH);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENW_FEEDBACK_XPATH.toString())));
			//ob.findElement(By.partialLinkText("Send feedback")).click();
			//BrowserWaits.waitTime(3);
			Set<String> st =ob.getWindowHandles();
			Iterator<String> I1= st.iterator();
			while(I1.hasNext())
			{
				String parentWindow_Id =I1.next();
				String childWindo_ID =I1.next();
	            ob.switchTo().window(childWindo_ID);
	            if (childWindo_ID != null) {
					if (ob.getCurrentUrl().contains("https://app.qc.endnote.com/EndNoteWeb.html?func=feedBack&")) {

						logger.info(
								"feedBack is opened in the new Window and it takes user to the existing (BAU) version of "
										+ "the Endnote Feedback");
					}
				} else {
					test.log(LogStatus.FAIL, "New window is not displayed and content is not matching");
					Assert.assertEquals(true, false);
				}
				try {
					test.log(LogStatus.PASS, "Expected page is displayed and  Navigating to the proper URL.");
				} catch (Throwable t) {
					test.log(LogStatus.FAIL, "Expected page is not displayed and  URL is wrong.");// extent
					ErrorUtil.addVerificationFailure(t); // reports
					status = 2;// excel
					test.log(LogStatus.INFO,
							"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "Feedback New window is not displayed and content is not matching")));// screenshot
				}
	            ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH_ENDNOTE.toString()))
				.sendKeys("Feedback sending");
		       // BrowserWaits.waitTime(2);
	           // pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH_ENDNOTE);
		        jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH_ENDNOTE.toString())));
		        //BrowserWaits.waitTime(8);
		        waitForElementTobeVisible(ob, By.xpath("//div[contains(text(),'Thank')]"), 60);
				if (!ob.findElement(By.xpath("//div[contains(text(),'Thank')]")).isDisplayed()) {
					test.log(LogStatus.FAIL, "Feedback not sent");
					logger.info("Sorry, but we couldn't deliver your submission. Please fix these issues and try again:");
					Assert.assertEquals(true, false);
				} else {
					test.log(LogStatus.PASS, "Feedback has been sent successfully.");
					ob.close();
					//Thread.sleep(2000);
				}
		        ob.switchTo().window(parentWindow_Id);
			}
//			ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_COMMENTS_XPATH.toString()))
//					.sendKeys("Feedback sending");
//			BrowserWaits.waitTime(2);
//			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.COMMON_FEEDBACK_SUBMIT_BTN_XPATH.toString())));
			
//			BrowserWaits.waitTime(8);
//			jsClick(ob,ob.findElement(By.xpath(OnePObjectMap.ENW_SEND_FEEDBACK_LINK_XPATH.toString())));
//			BrowserWaits.waitTime(3);
//			String newWindow = switchToNewWindow(ob);
			
			closeBrowser();
			test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
		
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		
	}
}
