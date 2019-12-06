package draiam;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
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

public class DRAIAM103 extends TestBase {

	//"Verify that 'EndNote' should be moved within the white area and should be above 'Forgot Password' text and center aligned 
	//Verify that Clarivate Analytics logo should be Placed below the marketing area (centered).".
	static int status = 1;
	static boolean fail = false;
   
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("DRAIAM");
	}
	@Test
	public void testcaseDRAIAM101() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;
		// static boolean fail = false;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP,
					"Skipping test case " + this.getClass().getSimpleName() + " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts--->");

		try {
			openBrowser();
			maximizeWindow();
			clearCookies();
			ob.navigate().to(host);
			test.log(LogStatus.PASS, "Neon Landing page is displayed. ");
			VerifyNeonTxt();
			//BrowserWaits.waitTime(6);
			JavascriptExecutor jse = (JavascriptExecutor)ob;
			jse.executeScript("window.scrollBy(0,250)", "");
			//BrowserWaits.waitTime(5);
			WebElement ImageFile = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.LOGIN_PAGE_LOGO_IMG_XPATH);
//			Boolean ImagePresent = (Boolean) ((JavascriptExecutor) ob).executeScript(
//					"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
//					ImageFile);
			Boolean ImagePresent =ImageFile.isDisplayed();
			try {
				Assert.assertTrue(ImagePresent);
				if (!ImagePresent)
		        {
		        	test.log(LogStatus.FAIL, "Clarivate Logo is not diplayed in EndNote Landing page");
		        }
		        else
		        {
		        	test.log(LogStatus.PASS, "Clarivate Logo is diplayed on Neon Landing page");
		        }

				test.log(LogStatus.PASS, " Image is present and User name is not hyper linked");
			} catch (Throwable t) {
				test.log(LogStatus.FAIL, " Image is not present and User is hyperlinked");// extent
				ErrorUtil.addVerificationFailure(t); // reports
				status = 2;// excel
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(captureScreenshot(
						this.getClass().getSimpleName() + "mage is not present and User is hyperlinked")));// screenshot
			}
			
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
			ErrorUtil.addVerificationFailure(t);
			closeBrowser();
		}

		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution ends--->");
	}
	private void VerifyNeonTxt() throws Exception {
	String str = ob.findElement(By.xpath("//h3[@class='wui-title login-header__app-name ng-binding']")).getText();
	try {
		Assert.assertEquals("Project Neon", str);
		test.log(LogStatus.PASS, " Neon Header is displayed within the white area .");
	} catch (Throwable t) {
		test.log(LogStatus.FAIL, "Project Neon Header is not  displayed within the white area");// extent
		ErrorUtil.addVerificationFailure(t); // reports
		status = 2;// excel
		test.log(LogStatus.INFO,
				"Snapshot below: " + test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
						+ "Feedback New window is not displayed and content is not matching")));// screenshot
	}
		
	}
	@AfterTest
	public void reportTestResult() {
		extent.endTest(test);
		closeBrowser();

	}
}
