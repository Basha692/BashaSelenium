package enwiam;

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

public class ENWIAM50 extends TestBase {

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
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("ENWIAM");
	}

	/**
	 * Method for login into Neon application using TR ID
	 * 
	 * @throws Exception
	 *             , When TR Login is not done
	 */
	@Test
	public void testcaseh10() throws Exception {
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

			ob.navigate().to(host);

			// Verify Neon landing page displays Branding and Marketing copy
			try {

				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.BRANDING_NAME_CSS);
				WebElement b_element = ob.findElement(By.cssSelector(OnePObjectMap.BRANDING_NAME_CSS.toString()));
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_MARKETING_COPY_CSS);
				WebElement m_element = ob.findElement(By.cssSelector(OnePObjectMap.NEON_MARKETING_COPY_CSS.toString()));

				String marketing_Copy = m_element.getText();

				if (b_element.isDisplayed() && m_element.isDisplayed()) {
					Assert.assertEquals(marketing_Copy, "Project Neon");
					test.log(LogStatus.PASS, "Neon Landing page displays Neon branding and marketing copy");
				}

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Neon Landing page doesn't displays Neon branding and marketing copy");
				ErrorUtil.addVerificationFailure(t);

			}

			// verify Neon Icon is displayed on Neon sign in page

			try {
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_CONNECT_ICON_CSS);
				WebElement icon_element = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.NEON_CONNECT_ICON_CSS);
				if (icon_element.isDisplayed())
					test.log(LogStatus.PASS, "Neon Icon is displayed on Neon Landing Page");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Neon Icon is not displayed on Neon Landing Page");
				ErrorUtil.addVerificationFailure(t);

			}
			pf.getLoginTRInstance(ob).enterTRCredentials(LOGIN.getProperty("ENWIAM50"),
					LOGIN.getProperty("ENWIAM50pwd"));
			pf.getLoginTRInstance(ob).clickLogin();
			test.log(LogStatus.PASS, "user has logged in with steam account");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.CLARIVATELOGO_HOMEPAGE_CSS);
			String backcolor=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.CLARIVATELOGO_HOMEPAGE_CSS).getCssValue("background-color");
			try {
				Assert.assertTrue(backcolor.contains("rgba(0, 0, 0, 0")==true ||backcolor.contains("transparent")==true );
				//Assert.assertEquals(backcolor, "rgba(0, 0, 0, 0)");
				test.log(LogStatus.PASS, "Platform header background color is white");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Platform header background color is not white");
				ErrorUtil.addVerificationFailure(t);

			}
			System.out.println(backcolor);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.CLARIVATELOGO_HOMEPAGE_CSS);
			Set<String> myset=ob.getWindowHandles();
			Iterator<String> it=myset.iterator();
			ArrayList<String> arr=new ArrayList<String>();
			for(int i=0;i<myset.size();i++)
			{
				arr.add(it.next());
			}
			ob.switchTo().window(arr.get(1));
			//BrowserWaits.waitTime(6);
			String ActualURL=ob.getCurrentUrl();
			
			try {
				
				Assert.assertEquals(ActualURL, "https://clarivate.com/");
				test.log(LogStatus.PASS, "Clarivate marking site is opened in new tab");

			} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL, "Clarivate marking site is not opened in new tab");
				ErrorUtil.addVerificationFailure(t);

			}

			
			//BrowserWaits.waitTime(2);
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

	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);

	}

}
