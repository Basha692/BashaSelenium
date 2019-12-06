package enwiam;

import org.openqa.selenium.By;
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
import util.TestUtil;

//OPQA-3662
//Verify that,user should receive a general error page when user tries to sign into Neon using an evicted account.

public class ENWIAM0004 extends TestBase {

	// Following is the list of status:
	// 1--->PASS
	// 2--->FAIL
	// 3--->SKIP
	// Checking whether this test case should be skipped or not
	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	static int time = 30;
	PageFactory pf = new PageFactory();

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");

	}

	@Test
	public void testLogin() throws Exception {

		boolean testRunmode = TestUtil.isTestCaseRunnable(enwiamxls, this.getClass().getSimpleName());
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
			steamLocked();
			steamEnw();
			ob.close();
			/*
			 * closeBrowser(); pf.clearAllPageObjects();
			 */
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Unexpected error");// extent
			ErrorUtil.addVerificationFailure(t);
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}

	private void steamLocked() throws Exception {
		// LOGIN TO NEON AND CHECKING THE EVICTED ACCONT
		ob.navigate().to(host);
		String str = "Your account has been evicted.";
		ob.findElement(By.name("loginEmail")).sendKeys("enwyogesh@yahoo.com");

		ob.findElement(By.name("loginPassword")).sendKeys("Yogi@123");
		ob.findElement(By.xpath("//span[contains(text(),'Sign in')]")).click();
		test.log(LogStatus.PASS, "user has logged in with steam account");
		//BrowserWaits.waitTime(2);
		waitForElementTobePresent(ob, By.xpath("//h2[contains(text(),'Your account has been evicted.')]"), 60);
		String evict = ob.findElement(By.xpath("//h2[contains(text(),'Your account has been evicted.')]")).getText();

		if (evict.equalsIgnoreCase(str)) {
			test.log(LogStatus.PASS, "The evicted string is displayed, the account got evicted on Neon");

		} else {
			test.log(LogStatus.FAIL, "The evicted string is not displayed");

		}
		//BrowserWaits.waitTime(3);
		waitForElementTobeVisible(ob, By.xpath("//h2[contains(text(),'Your account has been evicted.')]"), 60);
		ob.findElement(By.xpath("//button[contains(text(),'OK')]")).click();
		//BrowserWaits.waitTime(6);
	}

	// LOGIN TO ENW AND CHECKING THE EVICTED ACCONT
	private void steamEnw() throws Exception {
		ob.navigate().refresh();
		ob.navigate().to(host + CONFIG.getProperty("appendENWAppUrl"));
		String str = "Your account has been evicted.";
		ob.findElement(By.name("loginEmail")).sendKeys("enwyogesh@yahoo.com");

		ob.findElement(By.name("loginPassword")).sendKeys("Yogi@123");
		ob.findElement(By.xpath("//span[contains(text(),'Sign in')]")).click();

		//BrowserWaits.waitTime(2);
        waitForElementTobeVisible(ob, By.xpath("//h2[contains(text(),'Your account has been evicted.')]"), 60);
		String evict = ob.findElement(By.xpath("//h2[contains(text(),'Your account has been evicted.')]")).getText();
		// String evict=ob.findElement(By.xpath("//h2[contains(text(),'Your
		// account has been evicted on ENW.')]")).getText();
		if (evict.equalsIgnoreCase(str)) {
			test.log(LogStatus.PASS, "The evicted string is displayed, the account got evicted");

		} else {
			test.log(LogStatus.FAIL, "The evicted string is not displayed");

		}
		//BrowserWaits.waitTime(3);
		waitForElementTobeVisible(ob, By.xpath("//button[contains(text(),'OK')]"), 60);
		ob.findElement(By.xpath("//button[contains(text(),'OK')]")).click();

	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

		/*
		 * if(status==1) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "PASS");
		 * else if(status==2) TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "FAIL");
		 * else TestUtil.reportDataSetResult(iamxls, "Test Cases",
		 * TestUtil.getRowNum(iamxls,this.getClass().getSimpleName()), "SKIP");
		 */
	}

}
