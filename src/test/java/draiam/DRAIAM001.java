package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class DRAIAM001 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseDRA1() throws Exception {
		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		logger.info("checking master condition status-->" + this.getClass().getSimpleName() + "-->" + master_condition);

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
			ob.navigate().to(host + CONFIG.getProperty("appendDRAAppUrl"));

			// Verify that DRA Landing page, displays application branding and
			// logo
			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.BRANDING_NAME_CSS);
				WebElement b_element = ob.findElement(By.cssSelector(OnePObjectMap.BRANDING_NAME_CSS.toString()));
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_BRANDING_NAME_CSS);
				
				WebElement brand_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.DRA_BRANDING_NAME_CSS);
			
				String dra_logo = brand_element.getText();
		
				if (brand_element.isDisplayed() && b_element.isDisplayed()) {
					Assert.assertEquals(dra_logo, "Sign in to continue with Drug Research Advisor");
					
					test.log(LogStatus.PASS, "DRA Landing page displays branding and marketing copy");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "DRA Landing page doesn't displays branding and marketing copy");
				ErrorUtil.addVerificationFailure(t);
			}

			// Verifying that DRA Landing page, displays privacy statement and
			// terms of use links
			try {
				WebElement tl_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.DRA_LANDINGPAGE_TERMS_LINK_XPATH);
				WebElement pl_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.DRA_LANDINGPAGE_PRIVACY_LINK_XPATH);

				if (tl_element.isDisplayed() && pl_element.isDisplayed()) {
					test.log(LogStatus.PASS, "DRA Landing page displays terms of use and privacy links");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "DRA Landing page doesn't displays terms of use and privacy links");
				ErrorUtil.addVerificationFailure(t);
			}

			// verifying that DRA Landing page, displays the message and email
			// id OPQA-5194||OPQA-5190
			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_LANDINGPAGE_HELP_MESSAGE_CSS);
				
				WebElement helpmsg = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.DRA_LANDINGPAGE_HELP_MESSAGE_CSS);
				
				String actual_help_text = helpmsg.getText();
		
				if (actual_help_text.contains("Need help signing in? Please contact Drug Research Advisor Customer Care.") ){
					test.log(LogStatus.PASS, "DRA Landing page  displays the message and email");
				}
				
				
			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "DRA Landing page displays the incorrect message and email");
				ErrorUtil.addVerificationFailure(t);
			}
		

			// Verifying DRA marketing copy are displayed.
			try {
				boolean explore_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.DRA_LANDINGPAGE_DESIGN_ICON_CSS.toString()));
				Assert.assertEquals(explore_icon, true);
				test.log(LogStatus.PASS, "Design icon is displayed on DRA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Design icon is not displayed on DRA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean identify_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.DRA_LANDINGPAGE_IDENTIFY_ICON_CSS.toString()));
				Assert.assertEquals(identify_icon, true);
				test.log(LogStatus.PASS, "Identify icon is displayed on DRA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Identify icon is not displayed on DRA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			try {
				boolean validate_icon = checkElementIsDisplayed(ob,
						By.cssSelector(OnePObjectMap.DRA_LANDINGPAGE_OPTIMIZE_ICON_CSS.toString()));
				Assert.assertEquals(validate_icon, true);
				test.log(LogStatus.PASS, "Optimize icon is displayed on DRA landing page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Optimize icon is not displayed on DRA landing page");
				ErrorUtil.addVerificationFailure(t);
			}

			
			// Verifying the Learn more link is displaying
			//pf.getDraPageInstance(ob).validateProductOverviewPage(test);
			/*try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.DRA_LANDINGPAGE_LEARNMORE_LINK_XPATH);
				WebElement lm_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.DRA_LANDINGPAGE_LEARNMORE_LINK_XPATH);
				if(lm_element.isDisplayed())
				{
					//lm_element.click();
					pf.getBrowserActionInstance(ob).click(OnePObjectMap.DRA_LANDINGPAGE_LEARNMORE_LINK_XPATH);
					//BrowserWaits.waitTime(4);	
					Set<String> myset=ob.getWindowHandles();
					Iterator<String> myIT=myset.iterator();
					ArrayList<String> al=new ArrayList<String>();
					for(int i=0; i< myset.size() ;i++)
					{
						al.add(myIT.next());
					}
					
					ob.switchTo().window(al.get(1));
					String actual_URL = ob.getCurrentUrl();
					System.out.println(actual_URL);
					String expected_URL = "https://clarivate.com/products/drug-research-advisor/";
					Assert.assertTrue(actual_URL.contains(expected_URL));
					test.log(LogStatus.PASS,
							"user is taken to the TargetDruggability page");
				}
				

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "User is not able to click and not taken to the TargetDruggability page");
				ErrorUtil.addVerificationFailure(t);
			}

			*/

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
		closeBrowser();
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");

	}
  
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
