package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
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
public class Authoring8 extends TestBase {

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

	@Test(priority=1)
	public void testOpenApplication() throws Exception {
		try {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			boolean master_condition = suiteRunmode && testRunmode;

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
	@Parameters({"username", "password", "sampleArticle", "completeArticle"})
	public void chooseArtilce(String username,
			String password,
			String article,
			String completeArticle) throws Exception {
		try {
			// waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
			pf.getSearchResultsPageInstance(ob).searchArticle(article);
			pf.getSearchResultsPageInstance(ob).chooseArticle();

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
			pf.getPostCommentPageInstance(ob).enterArticleComments(
					RandomStringUtils.randomAlphabetic(Integer.parseInt(minCharCount.substring(0, 1))));
//			waitForElementTobeVisible(ob,
//					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS.toString()), 180);
			//Added by KR
			WebElement innerTextBox = ob.findElement(By.xpath("//div[@class='fr-element fr-view']"));
			innerTextBox.sendKeys("x");
			innerTextBox.sendKeys(Keys.BACK_SPACE);
			BrowserWaits.waitTime(3);
			 //((JavascriptExecutor)ob).executeScript("document.getElementByXpath('//div[@class='fr-element fr-view']').value='Q '");

			pf.getBrowserActionInstance(ob).scrollingPageDown();
			BrowserWaits.waitTime(3);
			String minValidErrMsg = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS).getText();
			// System.out.println("Min Validation Error
			// Message--->"+minValidErrMsg);
			pf.getBrowserWaitsInstance(ob).waitUntilText(minValidErrMsg);
			Assert.assertEquals(minValidErrMsg, expMinComment);
			ob.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_CANCEL_COMMENT_BUTTON_CSS.toString())).click();
			System.out.println("MaxCharCount-->" + (maxCharCount.substring(0, 4)));
			pf.getPostCommentPageInstance(ob).enterArticleComments(
					RandomStringUtils.randomAlphabetic(Integer.parseInt(maxCharCount.substring(0, 4))));
			waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS.toString()), 180);
			String maxValidErrMsg = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS).getText();
			// System.out.println("Max Validation Error
			// Message--->"+maxValidErrMsg);
			pf.getBrowserWaitsInstance(ob).waitUntilText(maxValidErrMsg);
			Assert.assertEquals(maxValidErrMsg, expMaxComment);
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();

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
