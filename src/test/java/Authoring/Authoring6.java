package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.TestUtil;

/**
 * Class for Performing Authoring prevent comment flooding by bots with same article
 * 
 * @author UC202376
 *
 */
public class Authoring6 extends TestBase {

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

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		// test the runmode of current dataset
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			test.log(LogStatus.INFO, "Runmode for test set data set to no " + count);
			skip = true;
			throw new SkipException("Runmode for test set data set to no " + count);
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts for data set #" + count + "--->");
		// selenium code
		try {
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
	@Parameters({"username", "password", "article", "completeArticle", "addComments"})
	public void validatePreventBotsComments(String username,
			String password,
			String article,
			String completeArticle,
			String addComments) throws Exception {
		try {
			// waitForTRHomePage();
			pf.getLoginTRInstance(ob).enterTRCredentials(username, password);
			pf.getLoginTRInstance(ob).clickLogin();
			pf.getSearchResultsPageInstance(ob).searchArticle(article);
			pf.getSearchResultsPageInstance(ob).chooseArticle();

			pf.getPostCommentPageInstance(ob).enterArticleComment(addComments);
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			ob.navigate().refresh();
			waitForAjax(ob);
			pf.getPostCommentPageInstance(ob).enterArticleComment(addComments);
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			pf.getPostCommentPageInstance(ob).validatePreventBotComment();

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

//	public void enterArticleComment(String addComments) throws InterruptedException {
//		BrowserWaits.waitTime(5);
//
//		WebElement commentArea = ob
//				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()));
//		System.out.println("Attribute-->" + commentArea.getAttribute("placeholder"));
//		// jsClick(ob,commentArea);
//
//		// Used points class to get x and y coordinates of element.
//		Point point = commentArea.getLocation();
//		// int xcord = point.getX();
//		int ycord = point.getY();
//		ycord = ycord + 200;
//		JavascriptExecutor jse = (JavascriptExecutor) ob;
//		jse.executeScript("scroll(0," + ycord + ");");
//		BrowserWaits.waitTime(2);
//		jsClick(ob, commentArea);
//		commentArea.clear();
//		String comment = addComments + RandomStringUtils.randomNumeric(3);
//		commentArea.sendKeys(comment);
//		// new
//		// Actions(ob).moveToElement(commentArea).sendKeys(addComments).build().perform();
//		Thread.sleep(3000);// after entering the comments wait for submit button
//							// to get enabled or disabled
//	}

}
