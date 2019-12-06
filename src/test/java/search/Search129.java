package search;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.ErrorUtil;
import util.ExtentManager;
import util.TestUtil;

public class Search129 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static boolean master_condition;

	static int time = 30;
	public String host1="https://apps.dev-stable.clarivate.com";
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription())
				.assignCategory("Search suite");
	}

	@Test
	public void testOpenApplication() throws Exception {
		try {

			boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
			master_condition = suiteRunmode && testRunmode;
			runmodes = TestUtil.getDataSetRunmodes(searchxls, "deep_linking");

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
			clearCookies();
			maximizeWindow();
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
			closeBrowser();
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
		 * if(status==1) TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(authoringxls, "Test Cases",
		 * TestUtil.getRowNum(authoringxls,this.getClass().getSimpleName()), "SKIP");
		 */

	}

	@Test(dependsOnMethods = "testOpenApplication", dataProvider = "getTestData")
	public void test(String url,
			String recordtype) throws Exception {
		try {
			test.log(LogStatus.INFO, this.getClass().getSimpleName()
					+ "  UnSupported HTML Tags execution starts for data set #" + (count + 1) + "--->");
			clearCookies();
			String Beforeurl =host1+ url;
			System.out.println(Beforeurl);
			ob.navigate().to(Beforeurl);
			pf.getLoginTRInstance(ob).loginWithLinkedInCredentials(LOGIN.getProperty("SOCIALLOGINEMAILDL"),
					LOGIN.getProperty("SOCIALLOGINPASSWORDDL"));

			try {
				Assert.assertEquals(ob.getCurrentUrl(), Beforeurl);
				test.log(LogStatus.PASS, "Deep linking url is matching after login " + recordtype);
				Assert.assertTrue(pf.getSearchResultsPageInstance(ob).isSearchPageDisplayed());
				test.log(LogStatus.PASS, "Deep linking is redirecting to the appropriate page " + recordtype);

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Deep linking not working");
				ErrorUtil.addVerificationFailure(t);
			}
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
		} finally {
			reportDataSetResult();
			++count;
			pf.getLoginTRInstance(ob).logOutApp();
		}
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(searchxls, "deep_linking");
	}

}
