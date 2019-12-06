package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

/**
 * Class for Performing Authoring Min and Max Length Comments Validation
 * 
 * @author UC202376
 *
 */
public class Authoring46 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		runmodes = TestUtil.getDataSetRunmodes(authoringxls, this.getClass().getSimpleName());
	}

	@Test
	public void testOpenApplication() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		try {
			if (!master_condition) {
				status = 3;
				test.log(LogStatus.SKIP,
						"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
				throw new SkipException(
						"Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
			}

			// test the runmode of current dataset
			count++;
			if (runmodes[count].equalsIgnoreCase("N")) {
				test.log(LogStatus.INFO, "Runmode for test set data set to no " + count);
				skip = true;
				throw new SkipException("Runmode for test set data set to no " + count);
			}
			test.log(LogStatus.INFO,
					this.getClass().getSimpleName() + " execution starts for data set #" + count + "--->");
			// selenium code

			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(System.getProperty("host"));
			// ob.get(CONFIG.getProperty("testSiteName"));
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Prevent_Bots_functionaliy_not_giving_expected_Result")));
			closeBrowser();
		}
	}

	@Test(dependsOnMethods = "testOpenApplication")
	@Parameters({"username", "password", "article", "completeArticle"})
	public void chooseArtilce(String username,
			String password,
			String article,
			String completeArticle) throws Exception {
		try {

			loginAs("USERNAME4", "PASSWORD4");
			pf.getSearchResultsPageInstance(ob).searchArticle(article);
			pf.getSearchResultsPageInstance(ob).clickOnArticleTab();
			pf.getSearchResultsPageInstance(ob).chooseArticle();
			pf.getPostCommentPageInstance(ob).enterArticleComments("test");
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Prevent_Bots_functionaliy_not_giving_expected_Result")));
			closeBrowser();
		}
	}

	@Test(dependsOnMethods = "chooseArtilce", dataProvider = "getTestData")
	public void commentMinMaxValidation(String minCharCount,
			String expMinComment,
			String maxCharCount,
			String expMaxComment) throws Exception {
		try {
			test.log(LogStatus.INFO, "Min and Max Length Comment Validation");
			// System.out.println("MinCharCount-->"+(minCharCount.substring(0,1)));
			//BrowserWaits.waitTime(10);
			//Commented by KR
//			waitForElementTobeVisible(ob,
//					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_BUTTON_CSS.toString()), 180);
//			WebElement editCommentElement = ob
//					.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_BUTTON_CSS.toString()));
//			JavascriptExecutor exe = (JavascriptExecutor) ob;
//			exe.executeScript("arguments[0].click();", editCommentElement);
			
			//Added by KR
			waitForElementTobePresent(ob, By.xpath("//button[@class='wui-mini-btn wui-mini-btn--secondary']"), 200);
			WebElement editCommentElement = ob
					.findElement(By.xpath("//button[@class='wui-mini-btn wui-mini-btn--secondary']"));
			JavascriptExecutor exe = (JavascriptExecutor) ob;
			exe.executeScript("arguments[0].click();", editCommentElement);

			test.log(LogStatus.INFO, "minCharCount:" + minCharCount);
			//Added by KR
			scrollingToElementofAPage();
			
			WebElement commentArea = ob
					.findElement(By.xpath("//div[@class='fr-box fr-basic fr-bottom']/descendant::div[@class='fr-element fr-view']"));
			commentArea.clear();
			commentArea.sendKeys(RandomStringUtils.randomAlphabetic(Integer.parseInt(minCharCount.substring(0, 1))));
			//Added by KR to fix issue - Error was not displaying, so adding 1 number and deleting it.
			commentArea.sendKeys(Keys.NUMPAD6, Keys.BACK_SPACE);
			
			//BrowserWaits.waitTime(5);
			//Added by KR
			scrollingToElementofAPage();
			
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_ERROR_MESSAGE_CSS.toString()), 30);
			String minValidErrMsg = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_ERROR_MESSAGE_CSS).getText();
			// System.out.println("Min Validation Error
			// Message--->"+minValidErrMsg);
			pf.getBrowserWaitsInstance(ob).waitUntilText(minValidErrMsg);
			Assert.assertEquals(minValidErrMsg, expMinComment);
			System.out.println("MaxCharCount-->" + (maxCharCount.substring(0, 4)));
//			commentArea = ob
//					.findElement(By.xpath(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_TEXTBOX_XPATH.toString()));
			commentArea.clear();
			commentArea.sendKeys(RandomStringUtils.randomAlphabetic(Integer.parseInt(maxCharCount.substring(0, 4))));
			Thread.sleep(2000);// after entering the comments wait for submit
								// button to get enabled or disabled
			//BrowserWaits.waitTime(5);
			//Added by KR
			scrollingToElementofAPage();
			
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_ERROR_MESSAGE_CSS.toString()), 30);
			String maxValidErrMsg = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_ERROR_MESSAGE_CSS).getText();
			// System.out.println("Max Validation Error
			// Message--->"+maxValidErrMsg);
			pf.getBrowserWaitsInstance(ob).waitUntilText(maxValidErrMsg);
			Assert.assertEquals(maxValidErrMsg, expMaxComment);
			test.log(LogStatus.PASS, "Error validation for min and max length passed");
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

		} catch (Throwable e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_Prevent_Bots_functionaliy_not_giving_expected_Result")));
			closeBrowser();
		}

	}

	@Test(dependsOnMethods = "commentMinMaxValidation")
	public void reportDataSetResult() {
		/*
		 * if(skip) TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2, "SKIP"); else
		 * if(fail) { status=2; TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2,
		 * "FAIL"); } else TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2, "PASS");
		 */

		skip = false;
		fail = false;

	}

	@AfterTest
	public void reportTestResult() {

		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases" ,
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(authoringxls, this.getClass().getSimpleName());
	}

}
