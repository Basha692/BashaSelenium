package enwiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.TestUtil;

public class ENWIAM005 extends TestBase {

	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;

	// Checking whether this test case should be skipped or not
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
		runmodes = TestUtil.getDataSetRunmodes(enwiamxls, this.getClass().getSimpleName());
	}

	@Test(dataProvider = "getTestData")
	public void testcaseA6(String charLength,
			String validity) throws Exception {

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

			test.log(LogStatus.INFO, "Runmode for test set data set to no " + (count + 1));
			skip = true;
			// TestUtil.reportDataSetResult(iamxls,
			// this.getClass().getSimpleName(), count+2, "SKIP");
			throw new SkipException("Runmode for test set data set to no " + (count + 1));
		}

		try {

			String characterLength = charLength.substring(0, 2);
			Double d = new Double(Double.parseDouble(characterLength));
			int i = d.intValue();
			test.log(LogStatus.INFO,
					this.getClass().getSimpleName() + " execution starts for data set #" + (count + 1) + "--->");
			test.log(LogStatus.INFO, characterLength + " -- " + validity);

			logger.info("Character Length : " + i);
			String last_name = generateRandomName(i);
			logger.info("Last Name : " + last_name);

			// selenium code
			openBrowser();
			//
			try {
				maximizeWindow();
			} catch (Throwable t) {

				logger.info("maximize() command not supported in Selendroid");
			}
			clearCookies();
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob, By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitUntilText("Sign up");
			waitForElementTobeVisible(ob, By.name(OR.getProperty("signup_lastName_textbox")), 30);
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox"))).sendKeys(last_name);
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox"))).click();

			if (validity.equalsIgnoreCase("YES")) {
				closeBrowser();
			}else{
			waitUntilText("Last name is too long.");
			closeBrowser();
			}
			
			/*BrowserWaits.waitTime(2);
			List<WebElement> errorList = ob.findElements(By.xpath(OR.getProperty("reg_error_label")));
			logger.info("Error List Size : " + errorList.size());
			if (validity.equalsIgnoreCase("YES")) {

				// verifying that error message is not getting displayed
				for (WebElement text : errorList) {
					if (text.getText().equals("Last name is too long.")) {

						fail = true;// excel
						test.log(LogStatus.FAIL, "Error message getting displayed unnecessarily");// extent
																									// report
						test.log(LogStatus.INFO,
								"Snapshot below: "
										+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
												+ "_error_message_getting_displayed_unnecessarily_" + (count + 1))));
						closeBrowser();
						return;
					}

				}
			}

			else {
				for (WebElement text : errorList) {
					if (!text.getText().equals("Last name is too long.")) {

						fail = true;// excel
						test.log(LogStatus.FAIL, "Error message not getting displayed");// extent
																						// report
						test.log(LogStatus.INFO,
								"Snapshot below: "
										+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
												+ "_error_message_not_getting_displayed_" + (count + 1))));
						closeBrowser();
						return;
					}
				}
			}

			closeBrowser();*/

		}

		catch (Throwable t) {

			status = 2;// excel-main testcase
			fail = true;// excel-dataset
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
																		// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO,
				this.getClass().getSimpleName() + " execution ends for data set #" + (count + 1) + "--->");
	}

	@AfterMethod
	public void reportDataSetResult() {
		if (skip)
			TestUtil.reportDataSetResult(enwiamxls, this.getClass().getSimpleName(), count + 2, "SKIP");

		else if (fail) {

			status = 2;
			TestUtil.reportDataSetResult(enwiamxls, this.getClass().getSimpleName(), count + 2, "FAIL");
		} else
			TestUtil.reportDataSetResult(enwiamxls, this.getClass().getSimpleName(), count + 2, "PASS");

		skip = false;
		fail = false;

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

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(enwiamxls, this.getClass().getSimpleName());
	}

}
