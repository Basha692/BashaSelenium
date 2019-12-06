package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Customercare003 extends TestBase{
	
	String runmodes[] = null;
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;

	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent
	 * Reports
	 * 
	 * @throws Exception
	 *             , When Something unexpected
	 */

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("customercare");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseDRA73_01() throws Exception {
		
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		// test the runmode of current dataset
		
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendDRACCUrl"));

			
				pf.getBrowserWaitsInstance(ob)
						.waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_FIRSTNAME_NAME);
				
				JavascriptExecutor jse = (JavascriptExecutor)ob;
				jse.executeScript("window.scrollBy(0,250)", "");
				//BrowserWaits.waitTime(5);
				
			//closeBrowser();

		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}

	
	@Test(dataProvider = "getData")
	public void testcaseDRA73_02(String Ctry1,String Ctry2,String weektime, String language,String Countries) throws Exception {
		
		try{
		count++;
		test.log(LogStatus.INFO,
				this.getClass().getSimpleName() + " execution starts for data set #" + (count + 1) + "--->");
		test.log(LogStatus.INFO, weektime + " -- " + language);
        test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		WebElement ele=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_CALLUS_DROPDOWN_CSS);
		Select se=new Select(ele);
		se.selectByVisibleText(Countries);
		
		String actualCtry1=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_CALLUS_CTRY1_CSS).getText();
		String actualCtry2=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_CALLUS_CTRY2_CSS).getText();
			
		String actualWeektime=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_HRS_XPATH).getText();
		String actualLanguage=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_LANGUAGE_XPATH).getText();
			
		
		//System.out.println(ob.findElement(By.cssSelector("p[class='cc-content ng-binding ng-scope']")).getText());
		try{
		Assert.assertEquals(actualCtry1, Ctry1);
		test.log(LogStatus.PASS,
				"DRA Customer care page:Ctry1 is displaying correctly ");
		}
		catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"DRA Customer care page:Ctry1 is not displaying required fields ");
		}
		try{
		Assert.assertEquals(actualCtry2, Ctry2);
		test.log(LogStatus.PASS,
				"DRA Customer care page:Ctry2 is displaying correctly ");
		}
		catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"DRA Customer care page::Ctry2  is not displaying required fields ");
		}
		try
		{
		Assert.assertEquals(actualWeektime, weektime);
		test.log(LogStatus.PASS,
				"DRA Customer care page:Weektime is displaying correctly ");
		}
		catch (Throwable t) {
			test.log(LogStatus.FAIL,
					"DRA Customer care page::weektime is not displaying required fields ");
		}
		try
		{
		Assert.assertEquals(actualLanguage, language);
		test.log(LogStatus.PASS,
				"DRA Customer care page:language is displaying correctly ");

	} catch (Throwable t) {
		test.log(LogStatus.FAIL,
				"DRA Customer care page:language is not displaying required fields ");
	}
	
		}
		catch(Throwable t)
		{
			test.log(LogStatus.FAIL, "Something unexpected happened");// extent
			// reports
			// next 3 lines to print whole testng error in report
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
			test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
					captureScreenshot(this.getClass().getSimpleName() + "_something_unexpected_happened")));// screenshot
			closeBrowser();
		}
	//ob.close();
	}
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		closeBrowser();
	}
	
	@DataProvider
	public Object[][] getData() {
		//Rows - Number of times your test has to be repeated.
		//Columns - Number of parameters in test data.
		Object[][] data = new Object[3][5];

		// 1st row
		data[0][0] ="+18003364474 [Toll free US & Canada]";
		data[0][1] = "Latin America: +18882164101";
		data[0][2] = "Monday -- Friday 9.00am - 8.00pm UTC-5";
		data[0][3] = "English";
		data[0][4] = "Americas Offices";

		// 2nd row
		data[1][0] ="Australia: 1-800007214 [Toll free]";
		data[1][1] = "New Zealand: 0800443162 [Toll free]";
		data[1][2] = "Monday -- Friday 7.00am - 7.00pm UTC+10";
		data[1][3] = "English";
		data[1][4] = "Australia and New Zealand";
		
		// 3nd row
		data[2][0] ="Hong Kong: 800905720 [Toll free]";
		data[2][1] = "Taiwan: 00806651914 [Toll free]";
		data[2][2] = "Monday -- Friday 9.00am - 6.00pm UTC+8";
		data[2][3] = "Mandarin, English";
		data[2][4] = "Hong Kong and Taiwan";
				
		
		

		return data;
	}

}
