package rcc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

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

public class RCC005 extends TestBase{

	
static int status = 1;
	
	/**
	 * Method for displaying JIRA ID's for test case in specified path of Extent Reports
	 * @throws Exception, When Something unexpected
	 */
	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest(rowData.getTestcaseId(), rowData.getTestcaseDescription()).assignCategory("RCC");
	}

	/**
	 * Method for wait TR Login Screen
	 * 
	 * @throws Exception, When TR Login screen not displayed 
	 */
	@Test
	public void testGroupCreation() throws Exception {

		boolean testRunmode = getTestRunMode(rowData.getTestcaseRunmode());
		boolean master_condition = suiteRunmode && testRunmode;

		if (!master_condition) {
			status = 3;
			test.log(LogStatus.SKIP, "Skipping test case " + this.getClass().getSimpleName()
					+ " as the run mode is set to NO");
			throw new SkipException("Skipping Test Case" + this.getClass().getSimpleName() + " as runmode set to NO");// reports
		}
		
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");

		try {
			
			
			openBrowser();
			clearCookies();
			maximizeWindow();
			ob.navigate().to(host);
			loginAs("RCCTESTUSER027", "RCCTESTUSERPWD027");
			
			pf.getGroupsPage(ob).clickOnGroupsTab();
			pf.getGroupsPage(ob).clickOnCreateNewGroupButton();
			pf.getGroupsListPage(ob).createGroup("group1","This is first group");
			try {
				boolean inviteMemberOption = checkElementIsDisplayed(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_MEM_LIST_MSG_CSS.toString()));
				Assert.assertEquals(inviteMemberOption, true);
				test.log(LogStatus.PASS,
				"Send invitation page is displayed as soon as new group is created");

				} catch (Throwable t) {
				t.printStackTrace();
				test.log(LogStatus.FAIL,
				"Send invitation page is not displayed as soon as new group is created");
				ErrorUtil.addVerificationFailure(t);
				}
			
			boolean cancelButtonStatus=pf.getGroupsListPage(ob).verifyButtonIsEnabled(OnePObjectMap.RCC_INVITEMEMBERS_CANCEL_BUTTON_CSS);
			boolean sendButtonStatus=pf.getGroupsListPage(ob).verifyButtonIsEnabled(OnePObjectMap.RCC_INVITEMEMBERS_SEND_BUTTON_CSS);
			if(!cancelButtonStatus && !sendButtonStatus)
		    test.log(LogStatus.PASS, "Cancel and Send Button is disabled soon after creating a group");	
			else 
			test.log(LogStatus.FAIL, "Cancel or Send Button is not disabled soon after creating a group");
			
			test.log(LogStatus.INFO, "Entering emailid while inviting users");
			
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.RCC_INVITEMEMBERS_TEXTFIELD_CSS, "mubarak2m@gmail.com");
			
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_INVITATION_DESCRIPTION_CSS);
			String expectedErrorMessage="Error! Please select a Neon user from the list.";
			String actualErrorMessage=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_INVITEMEMBERS_ERRORMESSAGE_CSS).getText();
			
			 try
			  {
				  Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
				  
			  test.log(LogStatus.PASS, "Error message displayed is correct after entering emailid in textfield");
			}catch(Throwable t){
				test.log(LogStatus.FAIL, "Error message displayed is incorrect after entering emailid in textfield");
				test.log(
						LogStatus.FAIL,
						"Snapshot below: "
								+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
										+ "_Error_Message_mismatch")));// screenshot
				ErrorUtil.addVerificationFailure(t);
			}
			 
			pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS);
			
			
			test.log(LogStatus.INFO, "Select valid user from the type ahead and click on cancel button");
			Assert.assertTrue(pf.getGroupDetailsPage(ob).selectUserFromList(LOGIN.getProperty("RCCPROFILE13")));
			
			
			 pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_INVITEMEMBERS_CANCEL_BUTTON_CSS);
			 String expectedMessage="Invite other members to join your group";
				String actualMessage=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_PENDING_INVITATIONS_MESSAGE_CSS).getText();
				
				 try
				  {
					  Assert.assertEquals(actualMessage, expectedMessage);
					  
				  test.log(LogStatus.PASS, "Invitation is not sent after clicking on cancel button");
				}catch(Throwable t){
					test.log(LogStatus.FAIL, "Invitation is sent after clicking on cancel button");
					test.log(
							LogStatus.FAIL,
							"Snapshot below: "
									+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
											+ "_Error_Message_mismatch")));// screenshot
					ErrorUtil.addVerificationFailure(t);
				}
				 
				 test.log(LogStatus.INFO, "Inviting list of users at a time from type ahead search");
				 List<String> membersList= Arrays.asList(LOGIN.getProperty("RCCPROFILE13"), LOGIN.getProperty("RCCPROFILE14"), LOGIN.getProperty("RCCPROFILE12"));
				 
				 pf.getGroupDetailsPage(ob).inviteMembers(membersList);
				 
				 waitForAjax(ob);
				 
				 String invitationStatus= pf.getGroupDetailsPage(ob).getInvitationStatus(LOGIN.getProperty("RCCPROFILE13"));
					
			     if(invitationStatus.contains("Invited on"))
			     test.log(LogStatus.PASS, "Invitation Status is shown as 'invited'");
			     else
			     test.log(LogStatus.FAIL, "Invitation Status is not shown as 'invited'");	 
			     pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			     pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			pf.getLoginTRInstance(ob).logOutApp();
			closeBrowser();
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Something went wrong");
			// print full stack trace
			StringWriter errors = new StringWriter();
			t.printStackTrace(new PrintWriter(errors));
			test.log(LogStatus.FAIL, errors.toString());
			ErrorUtil.addVerificationFailure(t);
			status = 2;// excel
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this.getClass().getSimpleName()
									+ "_login_not_done")));// screenshot
			closeBrowser();
		}
		test.log(LogStatus.INFO, this.getClass().getSimpleName() + " execution starts ");
	}
	

	
	
	/**
	 * updating Extent Report with test case status whether it is PASS or FAIL or SKIP
	 * @throws Exception 
	 */
	@AfterTest
	public void reportTestResult() throws Exception {
		extent.endTest(test);
		
	}
	
}
