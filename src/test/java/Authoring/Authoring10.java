package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;
import util.TestUtil;

/**
 * Class for Performing Authoring Comments Validation with Unsupported HTML tags except <br>
 * ,<b>,<i> and
 * <p>
 * 
 * @author UC202376
 *
 */
public class Authoring10 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static boolean master_condition;

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
		try {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			master_condition = suiteRunmode && testRunmode;

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

			// selenium code
			openBrowser();
			//runOnSauceLabsFromLocal("Windows", "Chrome");
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
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Article_Search_not_happening")));
			// closeBrowser();
		}
	}

	@Test(dependsOnMethods = "testOpenApplication")
	@Parameters({"username", "password", "article", "completeArticle"})
	public void chooseArtilce(String username,
			String password,
			String article,
			String completeArticle) throws Exception {
		try {
			// waitForTRHomePage();
			loginAs(username, password);
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
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_Article_Search_not_happening")));
			//closeBrowser();
		}
	}

	@Test(dependsOnMethods = "chooseArtilce", dataProvider = "getTestData")
	public void unSupportedTagsCommentsCheck(String htmlTags,
			String errorMessage) throws Exception {
		try {

			test.log(LogStatus.INFO, this.getClass().getSimpleName()
					+ "  UnSupported HTML Tags execution starts for data set #" + (count + 1) + "--->");
			pf.getPostCommentPageInstance(ob).enterArticleComments(htmlTags);
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS.toString()), 60);
			String unSupporteTagErrorMessage = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS).getText();
			// System.out.println("Profanity Word Error
			// Message--->"+profanityErrorMessage);
			logger.info("unsupported tags err msg-->"+unSupporteTagErrorMessage);
			pf.getBrowserWaitsInstance(ob).waitUntilText(unSupporteTagErrorMessage);
			
			// Assert.assertEquals(unSupporteTagErrorMessage, errorMessage);
			if (!unSupporteTagErrorMessage.equalsIgnoreCase(errorMessage)) {
				throw new Exception("UnSupported_HTML_tags_doesnot_allow_comments_validation");
			}
			
			pf.getPostCommentPageInstance(ob).cancelComment();

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "UnExpected Error");
			status = 2;
			fail = true;
			// print full stack trace
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(e);
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
					this.getClass().getSimpleName() + "_UnSupported_HTML_tags_doesnot_allow_comments_validation")));
			// closeBrowser();
		} finally {
			reportDataSetResult();
			++count;
		}

	}

	public void reportDataSetResult() {
		/*
		 * if(skip) { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(), count+2, "SKIP"); }
		 * else if(fail) { status=2; TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "FAIL"); } else { TestUtil.reportDataSetResult(authoringxls, this.getClass().getSimpleName(),
		 * count+2, "PASS"); }
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
		if (master_condition)
			closeBrowser();
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(authoringxls, this.getClass().getSimpleName());
	}

}
