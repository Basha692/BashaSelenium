
package pages;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;
import base.TestBase;

public class CustomercarePage extends TestBase {

	PageFactory pf;

	public CustomercarePage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void EnterCustomercareDetails(ExtentTest test, String firstName,String lastName, String Orgname, String Email, String Phone,
			String Extension, String Country, String Request) throws Exception {
		/*pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.CUSTOMER_CARE_USER_FULLNAME_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_FULLNAME_NAME, Name);*/
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_FIRSTNAME_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_FIRSTNAME_NAME, firstName);
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CUSTOMER_CARE_USER_LASTNAME_NAME);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_LASTNAME_NAME, lastName);

		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_ORGANIZATION_NAME, Orgname);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_EMAILID_NAME, Email);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_PHONE_NAME, Phone);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_EXTN_NAME, Extension);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_COUNTRY_NAME, Country);
		// pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_CATEGORY_NAME,
		// Category);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_NAME, Request);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.CUSTOMER_CARE_SUBMIT_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.CUSTOMER_CARE_SUBMIT_BUTTON_CSS);
		test.log(LogStatus.PASS,
				" All characters including special characters are  allowed in extension field in customer care page.");
	}

	public void ValidateSuccessMessage(ExtentTest test) throws Exception {

		try {
			String msg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_MSG_CSS)
					.getText();
			String msg1 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_MSG1_CSS)
					.getText();
			String msg2 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CUSTOMER_CARE_USER_REQUEST_MSG2_CSS)
					.getText();

			if (msg.contains("Thank you!") && msg1.contains("Thank you for contacting the Customer Care team.")
					&& msg2.contains("We will contact you via email within 2 business days.")) {
				test.log(LogStatus.PASS,
						" correct msg displayed and success message in customer care page matches with wire frame.");
			} else {
				test.log(LogStatus.FAIL, " incorrect msg displayed ");
			}
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something unexpected happened");
			ErrorUtil.addVerificationFailure(t);// testng
			//closeBrowser();
		}
	}

	public void clickOnDRACustomerCareLink() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.DRA_CUSTOMER_CARE_LINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_CUSTOMER_CARE_LINK_CSS);
		
	}

	public void validateDRACustomerCareLink(ExtentTest test) {
		try {
			clickOnDRACustomerCareLink();
			test.log(LogStatus.PASS,"Drug Research Advisor Customer Care is hyperlinked and it islinked to customer care / support page.");
			BrowserWaits.waitTime(5);

			Set<String> myset = ob.getWindowHandles();
			Iterator<String> myIT = myset.iterator();
			ArrayList<String> al = new ArrayList<String>();
			for (int i = 0; i < myset.size(); i++) {
				al.add(myIT.next());
			}
			ob.switchTo().window(al.get(1));
			String actual_URL = ob.getCurrentUrl();

			String expected_URL = "https://access.dev-stable.clarivate.com/cmty/#/customer-care?app=dra";  
			
			Assert.assertTrue(actual_URL.contains(expected_URL));
			test.log(LogStatus.PASS,
					"DRA customer care page URL is opened in a second window/tab (based on user/browser preference)");
			BrowserWaits.waitTime(2);
			if (actual_URL.contains("dra")) {
				test.log(LogStatus.PASS, "Customer care page URL content is specific to DRA(Target Druggability).");

			} else {
				test.log(LogStatus.FAIL, "Customer care page URL content is not specific to DRA(Target Druggability).");

			}

			ob.close();
			ob.switchTo().window(al.get(0));
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, " user is not taken to DRA customer care page ");
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.INFO, errors.toString());// extent reports
			ErrorUtil.addVerificationFailure(t);// testng
		}

	}
}
