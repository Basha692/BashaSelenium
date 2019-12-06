package enwiam;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ExtentManager;
import util.OnePObjectMap;

public class ENWIAM018 extends TestBase {

	static int status = 1;

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	@Test
	public void testCaseA22() throws Exception {

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
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob, By.name("loginEmail"), 180);
			ob.findElement(By.name("loginEmail")).sendKeys("userendnote@gmail.com");
			ob.findElement(By.name("loginPassword")).sendKeys("Neon@123");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
			String text = ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString()))
					.getText();
			if (text.equalsIgnoreCase("Continue")) {
				ob.findElement(By.cssSelector(OnePObjectMap.ENDNOTE_LOGIN_CONTINUE_BUTTON_CSS.toString())).click();
			}
			waitUntilText("Getting Started","Find","Collect");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString()), 30);
			jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH.toString())));
			waitUntilText("Account");
			waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString()), 30);
			ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_ACCOUNT_LINK_XPATH.toString())).click();
			waitUntilText("View additional email preferences");
			String cssValue = ob
					.findElement(By.cssSelector(OR.getProperty("tr_search_results_all_refine_checkboxes_css")))
					.getCssValue("background");
			logger.info("Css Value : "+cssValue);
			if (cssValue.contains("rgb(69, 183, 231)")) {
				test.log(LogStatus.INFO, "check box is selected");
			} else {
				test.log(LogStatus.INFO, "check box is not selected by default");
				status = 2;
			}
			// unchecking the check box n checking if it is working
			ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_all_refine_checkboxes_css"))).click();
			cssValue = ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_all_refine_checkboxes_css")))
					.getCssValue("background");

			logger.info("Css Value : "+cssValue);
			if (cssValue.contains("rgb(255, 255, 255)")) {
				test.log(LogStatus.INFO, "unchecking is working fine");
				ob.findElement(By.cssSelector(OR.getProperty("tr_search_results_all_refine_checkboxes_css"))).click();
			} else {
				test.log(LogStatus.INFO, "Unchecking is not working");
				status = 2;
			}
			logout();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "unexpected_something happpened");// extent reports
			status = 2;// excel
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "unable_to_open_preference_page")));// screenshot

			System.out.println("maximize() command not supported in Selendroid");
			closeBrowser();
		}
		closeBrowser();
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
