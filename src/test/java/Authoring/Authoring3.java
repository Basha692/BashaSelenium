package Authoring;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import util.OnePObjectMap;
import util.TestUtil;

public class Authoring3 extends TestBase {

	String runmodes[] = null;
	static int count = -1;
	static int totalCommentsBeforeDeletion = 0;
	static int totalCommentsAfterDeletion = 0;
	static int time = 15;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	PageFactory pf = new PageFactory();

	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("Authoring");
		runmodes = TestUtil.getDataSetRunmodes(authoringxls, this.getClass().getSimpleName());
	}

	@Test()
	@Parameters({"username", "password", "article", "completeArticle"})
	public void testLoginTRAccount(String username,
			String password,
			String article,
			String completeArticle) throws Exception {

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
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");
		try {

			// selenium code
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(System.getProperty("host"));
			// ob.get(CONFIG.getProperty("testSiteName"));
			// pf.getLoginTRInstance(ob).waitForTRHomePage();
			performAuthoringCommentOperations(username, password, article, completeArticle);
			closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "UnExpected Behaviour");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
	}

	// @Test(dependsOnMethods="testLoginTRAccount",dataProvider="getTestData")
	public void performAuthoringCommentOperations(String username,
			String password,
			String article,
			String completeArticle) throws Exception {
		try {

			// pf.getAuthoringInstance(ob).waitForTRHomePage();
			loginAs("USERNAME6", "PASSWORD6");
			pf.getSearchResultsPageInstance(ob).searchArticle(article);
			pf.getSearchResultsPageInstance(ob).selectArtcleWithComments();
			pf.getPostCommentPageInstance(ob).enterArticleComment("Test comments");
			pf.getPostCommentPageInstance(ob).clickAddCommentButton();
			deleteComments();
			closeBrowser();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(LogStatus.FAIL, "Error: Delete Comments not done" + t);// extent
																			// reports
			ErrorUtil.addVerificationFailure(t);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_profile_data_updation_not_done")));// screenshot
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
		// closeBrowser();
	}

	/**
	 * Method for Delete Article comments and Validate comments deleted or not
	 * 
	 * @throws Exception
	 */
	public void deleteComments() throws Exception {
		try {
			scrollingToElementofAPage();
			totalCommentsBeforeDeletion = pf.getProfilePageInstance(ob).getCommentCount();
			System.out.println("Before Deletion count --->" + totalCommentsBeforeDeletion);
			WebElement deleteCommentButton = ob
					.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_BUTTON_CSS.toString()));
			// System.out.println("is Delete
			// displayed-->"+deleteCommentButton.isDisplayed());
			JavascriptExecutor executor = (JavascriptExecutor) ob;
			executor.executeScript("arguments[0].click();", deleteCommentButton);

			waitUntilText("Delete Comment");
			waitUntilText("Are you sure you wish to delete this comment?");

			IsElementPresent(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_CONFIMATION_OK_BUTTON_CSS.toString());
			jsClick(ob, ob.findElement(By
					.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENT_DELETE_CONFIMATION_OK_BUTTON_CSS.toString())));
			waitForAjax(ob);
			totalCommentsAfterDeletion = pf.getProfilePageInstance(ob).getCommentCount();
			System.out.println("TOTAL COMMENTS AFTER DELETION --->" + totalCommentsAfterDeletion);

			if (!(totalCommentsBeforeDeletion > totalCommentsAfterDeletion)) {
				test.log(LogStatus.FAIL, "Error: Delete Comments not done");// extent
																			// reports
				status = 2;
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
						captureScreenshot(this.getClass().getSimpleName() + "_DeletComments_not_done")));// screenshot
			}
		} catch (Throwable e) {
			test.log(LogStatus.FAIL, "Error: Delete Comments not done" + e);// extent
																			// reports
			ErrorUtil.addVerificationFailure(e);// testng
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_DeletComments_not_done")));// screenshot
			closeBrowser();
		}

	}

	/**
	 * wait for until expected text present
	 * 
	 * @param text
	 */
	public void waitUntilText(final String text) {
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(d.getPageSource().contains(text));
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException("Failed to find text: " + text + ", after waiting for " + time + "ms");
		}
	}

	/**
	 * Method for wait until Element is Present
	 * 
	 * @param locator
	 * @throws Exception, When NoSuchElement Present
	 */
	public void IsElementPresent(String locator) throws Exception {
		try {
			count = ob.findElements(By.cssSelector(locator)).size();
		} catch (NoSuchElementException ne) {
			throw new NoSuchElementException("Failed to find element [Locator = {" + locator + "}]");
		}

		if (!(count > 0)) {
			throw new Exception("Unable to find Element...Element is not present");
		}
	}
}
