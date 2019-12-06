package pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;

/**
 * This class contains all the method related to account page
 * 
 * @author uc205521
 *
 */
public class GroupsListPage extends TestBase {

	PageFactory pf;

	public GroupsListPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public void enterGroupTitle(String title) {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS.toString()),
				30);
		WebElement titleField = ob
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS.toString()));
		titleField.clear();
		titleField.sendKeys(title);
	}

	public void enterGroupDescription(String desc) {

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_DESCRIPTION_CSS.toString()), 30);
		WebElement titleField = ob
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_DESCRIPTION_CSS.toString()));
		titleField.clear();
		titleField.sendKeys(desc);
	}

	public void clickOnSaveGroupButton() {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString()),
				60);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString())).click();
	}

	public void createGroup(String title, String desc) {
		enterGroupTitle(title);
		enterGroupDescription(desc);
		clickOnSaveGroupButton();

	}

	public void createGroup(String title) {
		enterGroupTitle(title);
		clickOnSaveGroupButton();
	}

	public void clickOnCancelGroupButton(ExtentTest test) throws Exception {

		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_CANCEL_GROUP_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_CANCEL_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.click();
				test.log(LogStatus.PASS, "Cancel button is clicked Succesfully");
				return;
			}
		}
		throw new Exception("Cancel button is not displaye in group details page");
	}

	public void clickOnGroupTitle(String title) throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS.toString()), 30);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS);
		for (WebElement we : list) {
			if (we.getText().equalsIgnoreCase(title)) {
				we.click();
				return;
			}
		}
		throw new Exception("Group name not found in Group list");
	}

	public void addCoverPhoto() throws Exception {
		clickOnAddImage();
		String imagePath = System.getProperty("user.dir") + "\\images\\" + "images3" + ".jpg";
		Runtime.getRuntime().exec("autoit_scripts/imageUpload2.exe" + " " + imagePath);
	}

	public void clickOnAddImage() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_COVER_PHOTO_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_COVER_PHOTO_CSS);
	}

	private WebElement getGroupCard(String groupTitle) throws Exception {

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS.toString()), 60);
		List<WebElement> groupsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS);

		String actTitle;
		for (WebElement we : groupsList) {
			actTitle = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(groupTitle)) {
				return we;
			}
		}
		throw new Exception("Group name not found in the group list");
	}

	public boolean verifyItemsCount(int count, String grouptitle) throws Exception {
		WebElement groupRecord = getGroupCard(grouptitle);
		String itemsCount = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_ITEMS_COUNT_CSS.toString())).getText();
		if(itemsCount.contains("Item")){
		itemsCount = itemsCount.substring(0, itemsCount.indexOf("Item")).trim();
		}else itemsCount = itemsCount.substring(0, itemsCount.indexOf("Items")).trim();
		return Integer.parseInt(itemsCount) == count;
	}

	public boolean verifyMembersCount(int count, String grouptitle) throws Exception {
		
		
		WebElement groupRecord = getGroupCard(grouptitle);
		String membersCount = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_MEMBERS_COUNT_CSS.toString())).getText();
		if(membersCount.contains("Member")){
			membersCount = membersCount.substring(0, membersCount.indexOf("Member")).trim();
		}else membersCount = membersCount.substring(0, membersCount.indexOf("Members")).trim();
		return Integer.parseInt(membersCount) == count;
		
	}

	public boolean verifyGroupDescription(String desc, String grouptitle) throws Exception {
		WebElement groupRecord = getGroupCard(grouptitle);
		String groupDesc = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_DESCRIPTION_CSS.toString())).getText().trim();
		if(desc.length()>300) groupDesc=groupDesc.substring(0,300);
		return desc.contains(groupDesc);
	}
	
	public String getGroupDescription(String grouptitle) throws Exception {
		WebElement groupRecord = getGroupCard(grouptitle);
		return groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_DESCRIPTION_CSS.toString())).getText();
		
	}

	public String getGroupOwnerDetails(String grouptitle) throws Exception {
		WebElement groupRecord = getGroupCard(grouptitle);
		String name = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_NAME_CSS.toString())).getText();
		String role = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_ROLE_CSS.toString())).getText();
		return name.trim() + ", " + role.trim();
	}

	public void clickOnGroupOwnerName(String grouptitle) throws Exception {

		WebElement groupRecord = getGroupCard(grouptitle);
		groupRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_NAME_CSS.toString())).click();
	}

	public boolean verifyGroupsortIsDisplayed() throws Exception {

		int count = ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_SORT_CSS.toString())).size();
		// logger.info("Count is " + count);
		if (count == 1) {
			return true;
		}
		return false;
	}

	public void verifyGroupsTabDefaultMessage(ExtentTest test) throws Exception {
		String expectedGroupMessage1 = "You do not have any groups";
		String expectedGroupMessage2 = "Get started by creating a new group. Groups can be shared with others. You can also join groups by accepting invitations sent to you by other Project Neon users.";
		String actualGroupMessage1 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPS_MESSAGE1_CSS)
				.getText();
		String actualGroupMessage2 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPS_MESSAGE2_CSS)
				.getText();

		try {
			Assert.assertEquals(actualGroupMessage1, expectedGroupMessage1);
			Assert.assertEquals(actualGroupMessage2, expectedGroupMessage2);
			test.log(LogStatus.PASS, "Group default message is displayed correctly");
		} catch (Throwable t) {
			test.log(LogStatus.FAIL, "Group default message is not displayed correctly");
			test.log(LogStatus.FAIL, "Snapshot below: " + test
					.addScreenCapture(captureScreenshot(this.getClass().getSimpleName() + "_Group_Message_mismatch")));// screenshot
			ErrorUtil.addVerificationFailure(t);
		}

	}

	public void navigateToGroupRecordPage(String grouptitle) throws Exception {
		WebElement groupcard = getGroupCard(grouptitle);
		groupcard.click();
		pf.getBrowserWaitsInstance(ob).waitUntilText("Groups", "Create new Group", "Articles", "Patents", "Posts",
				"Attached files", "Members");
	}

	public boolean checkForGroup(String groupTitle) throws Exception {
		waitForAjax(ob);
		try{
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS.toString()), 60);
		}catch(Exception e){
			return false;
		}
		List<WebElement> groupsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS);
		if(groupsList.size()==0)return false;
		String actTitle;
		for (WebElement we : groupsList) {
			actTitle = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(groupTitle)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkAddedUserDetails(String title, String userName) throws Exception {
		List<WebElement> userDetails = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS);
		String actTitle = null;
		boolean status = false;
		for (int i = 0; i < userDetails.size(); i++) {
			actTitle = userDetails.get(i).getText();
			if (actTitle.contains(title) && actTitle.contains(userName)) {
				status = true;
				break;
			}
		}
		return status;
	}

	public boolean validateSaveButtonDisabled() throws Exception {
		waitForElementTobePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString()),
				60);
		return ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString()))
				.isEnabled();

	}

	public boolean validateCreateGroupCardErrorMessage() throws Exception {
		String ErrorMsg = "Please provide a title for your group. (Minimum 2 characters)";
		waitForAjax(ob);
		
		waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.RCC_ERROR_MSG_TEXT_XPATH.toString()), 60);

		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_ERROR_MSG_TEXT_XPATH);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.getText().trim().equalsIgnoreCase(ErrorMsg);
				return true;
			}
		}
		return false;
	}
	
	
	public boolean verifyButtonIsEnabled(Object elementName) throws Exception {

		boolean ButtonStatus = pf.getBrowserActionInstance(ob).getElement(elementName).isEnabled();
		return ButtonStatus;

	}

	public boolean verifySortByOptions() throws Exception {
		String opt1 = "Most recent activity", opt2 = "Creation date", opt3 = "Group name";
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.RCC_GROUP_LIST_PAGE_SORT_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_LIST_PAGE_SORT_BUTTON_CSS);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()), 30);
		String val1 = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()))
				.get(0).getText();
		String val2 = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()))
				.get(1).getText();
		String val3 = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()))
				.get(2).getText();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_LIST_PAGE_SORT_BUTTON_CSS);
		if (val1.equalsIgnoreCase(opt1) && val2.equalsIgnoreCase(opt2) && val3.equalsIgnoreCase(opt3))
			return true;
		else
			return false;

	}

	public void selectSortoptions(String optionvalue) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.RCC_GROUP_LIST_PAGE_SORT_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_LIST_PAGE_SORT_BUTTON_CSS);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()), 30);
		List<WebElement> li = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_SORT_BY_MENU_CSS.toString()));
		for (WebElement we : li) {
			if (we.getText().equalsIgnoreCase(optionvalue)) {
				we.click();
				return;
			}
		}
		throw new Exception("Sortion option is not found");
	}

	public void sortByGroupName() throws Exception {
		List<String> beforelist = new ArrayList<String>();
		List<WebElement> li = ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_TITLE_CSS.toString()));
		for (WebElement we : li) {
			String val = we.getText();
			beforelist.add(val);

		}
		Collections.sort(beforelist, new Comparator<String>() {
	    
	    	  public int compare(String s1, String s2) {
	    		try {
	    		
	    			return s1.compareTo(s2);
	            } catch (Exception e) {
	                throw new IllegalArgumentException(e);
	            }
	    	}
	    	});
	
		selectSortoptions("Group name");
		List<String> list2 = new ArrayList<String>();
		List<WebElement> afterli = ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_TITLE_CSS.toString()));
		for (WebElement we : afterli) {
			String val = we.getText();
			list2.add(val);

		}
		 for(int i=0;beforelist.size()==list2.size()&&i<beforelist.size();i++) {
	    	  if(!beforelist.get(i).equals(list2.get(i)))
	    	 throw new Exception("values are not soerted by names");
	      }
	

	}
	
	public boolean getCreateGroupCard(String tilte) throws Exception {
		boolean status = false;
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_CARD_CSS);
		if (list.size() == 1) {
			ob.findElement(By.cssSelector((OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS.toString()))).sendKeys(tilte);
			ob.findElement(By.cssSelector((OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_CARD_CANCEL_BUTTON_CSS.toString())))
					.click();
			status = true;
		}
		return status;
	}

	public String getGroupOwnerDetailsFromCreateGroupCard() throws InterruptedException {
		waitForAjax(ob);
		WebElement groupRecord = ob
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUP_CARD_CSS.toString()));
		String name = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_NAME_CSS.toString())).getText();
		logger.info("User Name : " + name);
		String role = groupRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_ROLE_CSS.toString())).getText();
		logger.info("User role : " + role);
		
		groupRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_OWNER_NAME_CSS.toString())).click();
		return name.trim() + ", " + role.trim();
	}

	public void sortByCreationDate() throws Exception {
		List<String> beforesort =getDate();
		Collections.sort(beforesort, new Comparator<String>() {
	    	  SimpleDateFormat f= new SimpleDateFormat("d MMMMMMM yyyy | hh:mma");
	    
	    	  public int compare(String o1, String o2) {
	    		try {
	                return f.parse(o1).compareTo(f.parse(o2));
	            } catch (Exception e) {
	                throw new IllegalArgumentException(e);
	            }
	    	}
	    	});
		
		selectSortoptions("Creation date");
		List<String> aftersort=getDate();
	      for(int i=0;beforesort.size()==aftersort.size()&&i<beforesort.size();i++) {
	    	  if(!beforesort.get(i).equals(aftersort.get(i)))
	    	  throw new Exception("values are not soerted");
	      }
		}


		public void sortByMostRecentActivity() throws Exception
		 {
			
		     List<String> beforesort=getDate();
		    Collections.sort(beforesort, new Comparator<String>() {
		    	  SimpleDateFormat f= new SimpleDateFormat("d MMMMMMM yyyy | hh:mma");
		    
		    	  public int compare(String o1, String o2) {
		    		try {
		                return -f.parse(o1).compareTo(f.parse(o2));
		            } catch (Exception e) {
		                throw new IllegalArgumentException(e);
		            }
		    	}
		    	});
		   
		      selectSortoptions("Most recent activity");
		    List<String> aftersort=getDate();
		      for(int i=0;beforesort.size()==aftersort.size()&&i<beforesort.size();i++) {
		    	  if(!beforesort.get(i).equals(aftersort.get(i)))
		   		  throw new Exception("values are not soerted");
		    	  
		    	  
		      }
	 
	 }
	
		public List<String> getDate() {
			 Date date=new Date();
			 List<String> datelist=new ArrayList<String>();
			 List<WebElement> li= ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_GROUPS_TILE_DATE_CSS.toString()));
			    for(WebElement  we:li)
			    {
			        
			        SimpleDateFormat formatter = new SimpleDateFormat("d MMMMMMM yyyy | hh:mma");
			        String dateInString=we.getText().substring(8);
			       
					try {
					date = formatter.parse(dateInString);
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					datelist.add(formatter.format(date));
			                
			    }
			    return datelist;
		 }
	
	public void randomUpdate(int val,String title) throws Exception{
		 waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_TITLE_CSS.toString()),30);
		 List<WebElement> list=ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_LIST_PAGE_TITLE_CSS.toString()));
		 if(val<=list.size())
		 {
			 list.get(val).click();
			 pf.getGroupDetailsPage(ob).clickOnEditButton();
			 pf.getGroupDetailsPage(ob).updateGroupTitle(title);
			 pf.getGroupDetailsPage(ob).clickOnSaveButton();
		 }
		 else
			 throw new Exception("Group title is not updated randomly");
	}

	public boolean isCoverPhotoDisplayed() throws Exception{
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_NEWLY_CREATED_GROUP_PHOTO_CSS);
		WebElement image1 = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_NEWLY_CREATED_GROUP_PHOTO_CSS);
		Boolean ImagePresent = image1.isDisplayed();
		return ImagePresent; 
		
	}

	public void deleteAllGroups() throws Exception {
		
		List<WebElement> list= ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS.toString()));
		
		for(int i=0;i<list.size();i++){
			waitForAjax(ob);
			ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_GROUP_TITLE_LINK_CSS.toString())).click();
			BrowserWaits.waitTime(3);
			pf.getGroupDetailsPage(ob).clickOnDeleteButton();
			pf.getGroupDetailsPage(ob).clickOnDeleteButtonInConfirmationMoadl();
			
		}
		
	}

	public boolean verifytheDateandTimeofIvitation(String grouptitle) throws Exception {
		WebElement groupcard = getGroupCard(grouptitle);

		String Timecard = groupcard
				.findElement(By.xpath(OnePObjectMap.RCC_GROUP_INVITATIONS_DETAILS_TIMESTAMP_XPATH.toString()))
				.getText();

		Calendar cal = Calendar.getInstance();
		String OriginaltimeStamp = new SimpleDateFormat("d MMMMMMMMM yyyy").format(cal.getTime());
		if (Timecard.contains(OriginaltimeStamp) && (Timecard.contains("PM") || Timecard.contains("AM")))
			return true;
		else
			return false;

	}

	
	
}
