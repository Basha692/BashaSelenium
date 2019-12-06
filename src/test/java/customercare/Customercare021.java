package customercare;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.ExtentManager;
import util.OnePObjectMap;

public class Customercare021 extends TestBase{
	
	static int count = -1;

	static boolean fail = false;
	static boolean skip = false;
	static int status = 1;
	static String followBefore = null;
	static String followAfter = null;
	String alertMessage = "Invalid format. Only numbers (minimum 7 digits), spaces, and special characters + ( ) - allowed";
	String err = "";
	String[] invalidChars = {"#", "!", "$", "@", "%", "^", "&"};
    String[] validchar={"+", "(",")","-" };

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
	public void CustomercareSupport021() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);
		String number = " 8054 893 459";
		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
		
		try {

			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host + CONFIG.getProperty("appendDRACCUrl"));
			
//            			pf.getBrowserWaitsInstance(ob)
//			.waitUntilElementIsClickable(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_HELP_CSS);
//	        pf.getBrowserActionInstance(ob).click(OnePObjectMap.CUSTOMER_CARE_SUPPORTLINK_HELP_CSS);  
			Validate();
			ob.navigate().refresh();
			Validate1();
	        
	      //  String number = " 8054 893 459";
	         for (String invalid : invalidChars) {
	        	 pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).clear();
	        	 pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).sendKeys(number + invalid);
	        	pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap. CUSTOMER_CARE_USER_REQUEST_NAME).click();
	          //  ob.findElement(By.name("request")).click();
	            err = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_PHONE_NAME_ERROR_MESSAGE_XPATH).getText();
	               //BrowserWaits.waitTime(1);
	               pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_CLEARBTN_XPATH);
	               pf.getBrowserActionInstance(ob).getElement(OnePObjectMap. CUSTOMER_CARE_CLEARBTN_XPATH).click();
	            System.out.println(invalid);
	      }
	         if (alertMessage.equals(err)) {
	            	Assert.assertEquals(alertMessage, err);
	            	test.log(LogStatus.PASS,
							"Special characters are not allowed for phone number format on Support Request form");
	            } else {
	            	test.log(LogStatus.FAIL,
							"Phone number format accepting special characters on Support Request form");
	            }
	         validate3();
	         BrowserWaits.waitTime(5);
	         
			closeBrowser();

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

	private void validate3() throws Exception {
		String firstName = "Srinivas";
		String lastName ="clarivate";
		String Orgname = "Clarivate Analytics";
		String Email = "srinivas.bms@clarivate.com";
		String Phone = "+ 3 (123) 456-789";
		String Extension = "+91@*%*#";
		Select dropdown = new Select(ob.findElement(By.name("country")));
		dropdown.selectByIndex(10);
		String Country = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME).getAttribute("value");
				
		String Request = "Please test the above scenario --ts0001";
		pf.getCustomercarePage(ob).EnterCustomercareDetails(test, firstName, lastName, Orgname, Email, Phone, Extension, Country, Request);
		test.log(LogStatus.PASS, "user is able to submit the form in DRA Customer care page and Phone number field allowed only  "
				+ "special characters + ( ) - ");
		BrowserWaits.waitTime(3);
		pf.getCustomercarePage(ob).ValidateSuccessMessage(test);
		test.log(LogStatus.PASS,
				"Success message is displayed that confirms submission and allows user to raise a new ticket.");
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.CUSTOMER_CARE_USER_OK_BUTTON_CSS);
		
	}
	private void Validate() throws Exception {
		String charact="56789101112131415161718192021222324252627";
		 pf.getBrowserActionInstance(ob)
			.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).clear();
 	 pf.getBrowserActionInstance(ob)
			.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).sendKeys(charact);
 	pf.getBrowserActionInstance(ob)
			.getElement(OnePObjectMap. CUSTOMER_CARE_USER_REQUEST_NAME).click();
   //  ob.findElement(By.name("request")).click();
     err = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_PHONE_NAME_ERROR_MESSAGE_XPATH).getText();
        BrowserWaits.waitTime(1);
        pf.getBrowserActionInstance(ob)
			.getElement(OnePObjectMap. CUSTOMER_CARE_CLEARBTN_XPATH).click();
	 if (alertMessage.equals(err)) {
     	Assert.assertEquals(alertMessage, err);
     	test.log(LogStatus.PASS,
					"phone number format not allowed more than 40 characters  on Support Request form");
     } else {
     	test.log(LogStatus.FAIL,
					"Phone number format accepting more than 40 characters on support Request form");
     }
	}
	private void Validate1() throws Exception{
		String charact="123456";
		pf.getBrowserActionInstance(ob)
		.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).clear();
	 pf.getBrowserActionInstance(ob)
		.getElement(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME).sendKeys(charact);
	pf.getBrowserActionInstance(ob)
		.getElement(OnePObjectMap. CUSTOMER_CARE_USER_REQUEST_NAME).click();
//  ob.findElement(By.name("request")).click();
 err = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_PHONE_NAME_ERROR_MESSAGE_XPATH).getText();
   	 if (alertMessage.equals(err)) {
	     	Assert.assertEquals(alertMessage, err);
	     	test.log(LogStatus.PASS,
						"phone number format not allowed  less than 6 characters on Support Request form");
	     } else {
	     	test.log(LogStatus.FAIL,
						"Phone number format accepting more less than 6 characters on Support Request form");
	     }
	}

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
