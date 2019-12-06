package iam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class IAM039 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("IAM");
	}

	@Test
	public void testcaseA21() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		if (!master_condition) {

			status = 3;// excel
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports

		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		openBrowser();
		try {
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);
			waitUntilText("Sign in");
			String str = ob.findElements(By.cssSelector(OnePObjectMap.LOGIN_PAGE_CLARIVATE_ANALYTICS_LINK_CSS.toString())).get(0).getText();
			logger.info("Text : " + str);
			jsClick(ob, ob.findElements(By.cssSelector(OnePObjectMap.LOGIN_PAGE_CLARIVATE_ANALYTICS_LINK_CSS.toString())).get(0));
			Set<String> myset = ob.getWindowHandles();
			Iterator<String> myIT = myset.iterator();
			ArrayList<String> al = new ArrayList<String>();
			for (int i = 0; i < myset.size(); i++) {

				al.add(myIT.next());
			}

			ob.switchTo().window(al.get(1));
			String str1 = ob.findElement(By.cssSelector(OnePObjectMap.CLARIVATE_ANALYTICS_PAGE_CSS.toString())).getText();
			logger.info("Text : " + str1);
			Assert.assertEquals(str1, "Clarivate Analytics");
			test.log(LogStatus.PASS, "Link is open another window succssfully");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Link is not open another window succssfully");// extent reports
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "unable_to_open_preference_page")));// screenshot
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);
		} finally {
			closeBrowser();
		}

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS"); else if(status==2)
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL"); else
		 * TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}
}
