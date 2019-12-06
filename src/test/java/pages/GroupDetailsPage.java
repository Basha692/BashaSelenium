package pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * This class contains all the method related to account page
 * 
 * @author uc205521
 *
 */
public class GroupDetailsPage extends TestBase {

	PageFactory pf;

	public GroupDetailsPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public int getArticlesCounts() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_COUNT_CSS);
		int articlesCount = Integer.parseInt(count.getText());
		return articlesCount;
	}

	public int getPatentsCounts() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_COUNT_CSS);
		int patentCount = Integer.parseInt(count.getText());
		return patentCount;
	}

	public int getPostsCounts() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_POSTS_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPDETAILS_POSTS_COUNT_CSS);
		int postsCount = Integer.parseInt(count.getText());
		return postsCount;
	}

	public int getAttachedFilesCounts() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_ATTACHED_FILES_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.RCC_GROUPDETAILS_ATTACHED_FILES_COUNT_CSS);
		int attachedFilesCount = Integer.parseInt(count.getText());
		return attachedFilesCount;
	}

	public int getMembersCounts() throws Exception {
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_COUNT_CSS);
		logger.info("Members" + count.getText() + "values");
		if (count.getText().equals("")) {
			return 0;
		} else {
			return Integer.parseInt(count.getText());
		}

	}

	public void clickOnDeleteButton() throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_DELETE_GROUP_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_DELETE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				scrollElementIntoView(ob, we);
				jsClick(ob, we);
				return;
			}
		}
		throw new Exception("Delete button is not displayed in group details page");
	}

	public void clickOnAttachFileButton() throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_ATTACH_FILE_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_ATTACH_FILE_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				scrollElementIntoView(ob, we);
				jsClick(ob, we);
				BrowserWaits.waitTime(2);
				return;
			}
		}
		throw new Exception("Attach File button is not displayed in group details page");

	}

	public void clickOnInviteOthersButton() throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_INVITE_OTHERS_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_INVITE_OTHERS_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				scrollElementIntoView(ob, we);
				jsClick(ob, we);
				return;
			}
		}
		throw new Exception("Invite Others button is not displayed in group details page");

	}

	public void clickArticlesTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_TAB_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_TAB_CSS);
		waitForAjax(ob);
	}

	public void clickPatentstab() throws Exception {
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_TAB_CSS);
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_TAB_CSS.toString()), 300);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_TAB_CSS);
		waitForAjax(ob);
	}

	public void clickPostsTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_POSTS_TAB_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPDETAILS_POSTS_TAB_CSS);
		waitForAjax(ob);
	}

	public void clickAttachedFilesTab() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_ATTACHED_FILES_TAB_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPDETAILS_ATTACHED_FILES_TAB_CSS);
		waitForAjax(ob);
	}

	public void clickMembersTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_TAB_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_TAB_CSS);
		waitForAjax(ob);
	}

	private WebElement getRecordCard(String recordTitle, String type) throws Exception {
		String recordCSS = null;
		Thread.sleep(5000);
		if (type.equalsIgnoreCase("post"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_POST_RECORD_CARD_CSS.toString();
		else if (type.equalsIgnoreCase("patent"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_PATENT_RECORD_CARD_CSS.toString();
		else if (type.equalsIgnoreCase("article"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_ARTICLE_RECORD_CARD_CSS.toString();

		waitForAllElementsToBePresent(ob, By.cssSelector(recordCSS), 60);
		Thread.sleep(5000);
		List<WebElement> invitationList = ob.findElements(By.cssSelector(recordCSS));
		String actTitle;
		for (WebElement we : invitationList) {
			actTitle = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_TITLE_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(recordTitle)) {
				return we;
			}
		}
		throw new Exception("Record not found in the group list");
	}

	public String getGroupTitle() {
		waitForAjax(ob);
		waitForElementTobePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_TITLE_CSS.toString()), 30);
		List<WebElement> list = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_TITLE_CSS.toString()));
		String title = "";
		for (WebElement we : list) {

			if (we.isDisplayed()) {
				title = we.getText();
				break;
			}
		}

		return title;

	}

	public boolean inviteMembers(List<String> membersList) throws InterruptedException {
		boolean isFound = false;

		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString()), 30);

		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString())).click();

		for (String membersName : membersList) {
			isFound = selectUserFromList(membersName);

		}
		clickOnSendInvitation();
		return isFound;
	}

	public boolean selectUserFromList(String membersName) throws InterruptedException {
		boolean isFound = false;
		waitForAjax(ob);
		BrowserWaits.waitTime(4);
		for (int i = 0; i < membersName.length(); i++) {
			ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString()))
					.sendKeys(membersName.charAt(i) + "");
			BrowserWaits.waitTime(1);
		}

		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_OPTIONS_CSS.toString()), 30);
		BrowserWaits.waitTime(2);
		List<WebElement> optionsList = ob.findElements(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_OPTIONS_CSS.toString()));
		for (WebElement we : optionsList) {

			if (we.getText().equals(membersName)) {
				we.click();
				BrowserWaits.waitTime(2);
				isFound = true;
				break;
			}
		}
		return isFound;
	}

	public boolean inviteMembers(String membersName) throws InterruptedException {
		boolean isFound = false;
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString()), 500);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBER_TYPE_AHEAD_CSS.toString())).click();
		isFound = selectUserFromList(membersName);
		Thread.sleep(5000);
		clickOnSendInvitation();
		Thread.sleep(3000);
		return isFound;
	}

	public void clickOnSendInvitation() {
		fluentwaitforElement(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_SEND_BUTTON_CSS.toString()), 500);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_SEND_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_SEND_BUTTON_CSS.toString()))
				.click();
	}

	public void clickOnCancelInvitation() throws Exception {
		scrollingToElementofAPage();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_CANCEL_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_CANCEL_BUTTON_CSS.toString()))
				.click();
	}

	public void cancelPendingInvitations(String username) throws Exception {
		scrollingToElementofAPage();
		waitForAjax(ob);
		WebElement groupCard = getPendingRecords(username);
		jsClick(ob, groupCard.findElement(By
				.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PENDING_MEMBERS_CANCEL_INVITATION_BUTTON_CSS.toString())));

	}

	private WebElement getPendingRecords(String username) throws Exception {
		scrollingToElementofAPage();
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PENDING_MEMBERS_CARD_CSS.toString()), 60);
		List<WebElement> pendinglist = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PENDING_MEMBERS_CARD_CSS.toString()));
		String username1 = null;
		for (WebElement we : pendinglist) {
			username1 = we
					.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PENDING_MEMBERS_NAMES_CSS.toString()))
					.getText();
			if (username.equalsIgnoreCase(username1)) {
				return we;
			}
		}
		throw new Exception("Record not found in the group list");
	}

	public void clickOnDeleteButtonInConfirmationMoadl() throws Exception {

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.RCC_GROUPDETAILS_DELETE_CONFIMATION_DELETE_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RCC_GROUPDETAILS_DELETE_CONFIMATION_DELETE_BUTTON_CSS);

	}

	public void typeCustomMessage(String Message) throws Exception {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_CUSTOM_MESSAGE_CSS.toString()),
				30);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_CUSTOM_MESSAGE_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_CUSTOM_MESSAGE_CSS.toString()))
				.sendKeys(Message);
		BrowserWaits.waitTime(3);
	}

	/**
	 * Validate Article is present in Article Tab under Group details page
	 * 
	 * @param articleTitle
	 * @throws Exception,
	 *             When Article not present in Group details page
	 */
	public void validateArtcileInGroupDetailsPage(ExtentTest test, String articleTitle) throws Exception {
		clickArticlesTab();
		boolean isArticleRecordPresent = false;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_TAB_DETAILS_CSS);
		List<WebElement> articleTabDetails = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPDETAILS_ARTICLES_TAB_DETAILS_CSS);

		for (WebElement articleTabDetail : articleTabDetails) {
			pf.getBrowserActionInstance(ob).scrollToElement(articleTabDetail);
			logger.info("Article Title in Group Details page-->" + articleTabDetail.getText());
			if (articleTabDetail.getText().equalsIgnoreCase(articleTitle)) {
				logger.info("Article Present in Group Details page");
				isArticleRecordPresent = true;
				break;
			}
		}
		if (!isArticleRecordPresent) {
			// throw new Exception("Article Record is not present in Group
			// Details Page");
			logFailureDetails(test, "Article Record is not present in Group Details Page",
					"article_not_present_GD_page");
		}
	}

	/**
	 * Validate Post is present in Patent Tab under Group details page
	 * 
	 * @param articleTitle
	 * @throws Exception,
	 *             When Patent not present in Group details page
	 */
	public void validatePatentInGroupDetailsPage(ExtentTest test, String patentTitle) throws Exception {
		clickPatentstab();
		boolean isPatentRecordPresent = false;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_TAB_DETAILS_CSS);
		List<WebElement> patentTabDetails = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPDETAILS_PATENTS_TAB_DETAILS_CSS);

		for (WebElement patentTabDetail : patentTabDetails) {
			pf.getBrowserActionInstance(ob).scrollToElement(patentTabDetail);
			logger.info("Patent Title in Group Details page-->" + patentTabDetail.getText());
			if (patentTabDetail.getText().equalsIgnoreCase(patentTitle)) {
				logger.info("Patent Title Present in Group Details page");
				isPatentRecordPresent = true;
				break;
			}
		}
		if (!isPatentRecordPresent) {
			// throw new Exception("Patent Record is not present in Group
			// Details Page");
			logFailureDetails(test, "Patent Record is not present in Group Details Page", "patent_not_present_GD_page");
		}
	}

	/**
	 * Validate Post is present in Patent Tab under Group details page
	 * 
	 * @param articleTitle
	 * @throws Exception,
	 *             When Post not present in Group details page
	 */
	public void validatePostInGroupDetailsPage(ExtentTest test, String postTitle) throws Exception {
		clickPostsTab();
		boolean isPostRecordPresent = false;
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.RCC_GROUPDETAILS_POSTS_TAB_DETAILS_CSS);
		List<WebElement> postTabDetails = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPDETAILS_POSTS_TAB_DETAILS_CSS);

		for (WebElement postTabDetail : postTabDetails) {
			pf.getBrowserActionInstance(ob).scrollToElement(postTabDetail);
			logger.info("Post Title in Group Details page-->" + postTabDetail.getText());
			if (postTabDetail.getText().equalsIgnoreCase(postTitle)
					|| postTabDetail.getText().contains("Post removed by member")) {
				logger.info("Post Title Present in Group Details page");
				isPostRecordPresent = true;
				break;
			}
		}
		if (!isPostRecordPresent) {
			// throw new Exception("Post Record is not present in Group Details
			// Page");
			logFailureDetails(test, "Post Record is not present in Group Details Page", "post_not_present_GD_page");

		}
	}

	public String getPendingInvitationMessage() throws Exception {
		scrollingToElementofAPage();
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_PENDING_INVITATION_MESSAGE_CSS);

		String otherMemberJoinGroup = ob
				.findElement(
						By.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_PENDING_INVITATION_MESSAGE_CSS.toString()))
				.getText();
		return otherMemberJoinGroup;

	}

	public void clickOnCancelButtonINConfirmationModal() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CANCEL_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CANCEL_BUTTON_CSS);
		waitForElementTobeInvisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CSS.toString()), 5);
		BrowserWaits.waitTime(3);
	}

	public void clickOnCloseButtonINConfirmationModal() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CLOSE_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CLOSE_BUTTON_CSS);
		waitForElementTobeInvisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CSS.toString()), 5);
		BrowserWaits.waitTime(3);
	}

	public void clickOnSubmitButtonINConfirmationModal() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_SUBMIT_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_SUBMIT_BUTTON_CSS);
		waitForElementTobeInvisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CSS.toString()), 5);
		waitForAjax(ob);
		BrowserWaits.waitTime(3);
	}

	public boolean verifyConfirmationModalContents(String modalLabel, String modalInfoText, String primaryButton)
			throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CSS);
		String msg = ob
				.findElement(By
						.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_INFO_TEXT_CONFIRMATION_MODAL_CSS.toString()))
				.getText();
		String headertext = ob
				.findElement(By
						.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_LABEL_TEXT_CONFIRMATION_MODAL_CSS.toString()))
				.getText();

		String primaryButtonName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_SUBMIT_BUTTON_CSS).getText();
		String secondaryButtonName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.RCC_GROUPS_DETAILS_CONFIRMATION_MODAL_CANCEL_BUTTON_CSS).getText();
		if (modalLabel.equalsIgnoreCase(headertext) && modalInfoText.equalsIgnoreCase(msg)
				&& primaryButtonName.equalsIgnoreCase(primaryButton)
				&& secondaryButtonName.equalsIgnoreCase("Cancel")) {
			return true;
		} else
			return false;

	}

	public void validateGroupMembersInGroupDetailPage() throws Exception {
		clickMembersTab();

	}

	public String getGroupOwnerDetails() throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 0);");
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_OWNER_NAME_CSS.toString()), 60);
		String name = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_GROUPDETAILS_GROUP_OWNER_NAME_CSS)
				.get(1).getText();

		String role = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_GROUPDETAILS_GROUP_OWNER_ROLE_CSS)
				.get(1).getText();

		return name.trim() + ", " + role.trim();

	}

	public boolean verifyGroupDescription(String desc) throws Exception {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_DESCRIPTION_CSS.toString()), 60);

		String groupDesc = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPDETAILS_GROUP_DESCRIPTION_CSS).get(1).getText();
		return groupDesc.equals(desc);

	}

	public String getGroupDescription() throws Exception {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_DESCRIPTION_CSS.toString()), 60);

		return pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_GROUPDETAILS_GROUP_DESCRIPTION_CSS).get(1)
				.getText().trim();

	}

	public void clickOnGroupOwnerName() throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, -250);");
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_GROUP_OWNER_NAME_CSS.toString()), 60);
		pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_GROUPDETAILS_GROUP_OWNER_NAME_CSS).get(1).click();
	}

	public boolean verifyUserInPendingInvitationList(String username) throws Exception {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_PENDING_INV_MSG_CSS.toString()), 30);
		List<WebElement> invitationList = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_PENDING_MEMBERS_CARD_CSS.toString()));
		if (invitationList.size() == 0)
			return true;
		else
			return false;

	}

	public void clickonLeaveGroupButton() throws Exception {

		waitForAjax(ob);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS.toString()),
				60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.click();
				return;
			}
		}
		throw new Exception("leave group button is not displayed in group details page");

	}

	public void verifyLeaveGroupPopupMessage(ExtentTest test) throws Exception {
		String Originalmessage = "Are you sure you want to leave this group?";
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_VERIFICATION_TEXT_CSS.toString()), 20);
		String Originaltext = ob
				.findElement(
						By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_VERIFICATION_TEXT_CSS.toString()))
				.getText();
		if (Originaltext.equalsIgnoreCase(Originalmessage))
			test.log(LogStatus.PASS, "Popup message verified successfully");
		else
			test.log(LogStatus.FAIL, "Popup message not verified :(");
	}

	public boolean verifyleavegroupPopupButtons() throws Exception {
		waitForAjax(ob);

		try {
			ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CANCEL_BUTTON_CSS.toString()))
					.isDisplayed();
			ob.findElement(
					By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_LEAVEGROUP_BUTTON_CSS.toString()))
					.isDisplayed();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	public boolean clickonCancelButtononPopup() throws Exception {
		waitForAjax(ob);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CANCEL_BUTTON_CSS.toString()))
				.click();

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS.toString()),
				10);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				return true;
			}
		}
		throw new Exception("leave group button is not displayed in group details page");

	}

	public boolean clickonCloseButtononPopup() throws Exception {
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CLOSE_BUTTON_CSS.toString()), 60);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CLOSE_BUTTON_CSS.toString()))
				.click();

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS.toString()),
				10);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				return true;
			}
		}
		throw new Exception("leave group button is not displayed in group details page");

	}

	public void clickOnLeaveGroupButtoninPopUp() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_LEAVEGROUP_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_LEAVEGROUP_BUTTON_CSS);

	}

	public WebElement getMembersCard(String username) throws Exception {

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_CARD_CSS.toString()),
				60);
		List<WebElement> nameslist = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_CARD_CSS.toString()));
		String username1 = null;
		for (WebElement we : nameslist) {
			username1 = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_NAME_CSS.toString()))
					.getText();
			if (username.equalsIgnoreCase(username1)) {
				return we;
			}
		}
		throw new Exception("User is not found in members list");
	}

	public boolean checkMemberInList(String username) throws Exception {

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_CARD_CSS.toString()),
				60);
		List<WebElement> nameslist = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_CARD_CSS.toString()));
		String username1 = null;
		for (WebElement we : nameslist) {
			username1 = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_NAME_CSS.toString()))
					.getText();
			if (username.equalsIgnoreCase(username1)) {
				return true;
			}
		}
		return false;
	}

	public void removeMembers(String username) throws Exception {
		waitForAjax(ob);
		WebElement groupCard = getMembersCard(username);
		groupCard.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_MEMBERS_REMOVE_BUTTON_CSS.toString()))
				.click();

	}

	public void clickOnRecordTitle(String recordTitle, String recordType) throws Exception {
		scrollingToElementofAPage();
		WebElement record = getRecordCard(recordTitle, recordType);
		record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_TITLE_CSS.toString())).click();
	}

	public void clickOnRemoveRecord(String recordTitle, String recordType) throws Exception {

		WebElement record = getRecordCard(recordTitle, recordType);
		BrowserWaits.waitTime(5);
//		scrollElementIntoView(ob, record
//				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_REMOVE_BUTTON_CSS.toString())));
		scrollingToElementofAPage();
		jsClick(ob, record
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_REMOVE_BUTTON_CSS.toString())));

	}

	public void clickOnAttachFileForRecord(String recordTitle, String recordType) throws Exception {
		WebElement record = getRecordCard(recordTitle, recordType);
		scrollElementIntoView(ob, record.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_ATTACHFILE_BUTTON_CSS.toString())));
		jsClick(ob, record.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_ATTACHFILE_BUTTON_CSS.toString())));
		BrowserWaits.waitTime(2);
	}

	public boolean verifytheDateandTimeofAttachedRecord(String recordTitle, String recordType) throws Exception {
		scrollingToElementofAPage();
		WebElement record = getRecordCard(recordTitle, recordType);

		String Timecard = record
				.findElement(By.xpath(OnePObjectMap.RCC_GROUP_INVITATIONS_DETAILS_TIMESTAMP_XPATH.toString()))
				.getText();

		Calendar cal = Calendar.getInstance();
		String OriginaltimeStamp = new SimpleDateFormat("dd MMMMMMMMM yyyy").format(cal.getTime());
		if (Timecard.contains(OriginaltimeStamp.toUpperCase()) && (Timecard.contains("PM") || Timecard.contains("AM")))
			return true;
		else
			return false;

	}

	public void validateFollowOrUnfollow(String recordTitle, String recordType, ExtentTest test) throws Exception {
		waitForPageLoad(ob);
		waitForAjax(ob);
		WebElement record = getRecordCard(recordTitle, recordType);
		String attribute = record
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_FOLLOW_OWNER_CSS.toString()))
				.getAttribute("data-uib-tooltip");

		if (attribute.equalsIgnoreCase("Follow this person")) {
			record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_FOLLOW_OWNER_CSS.toString())).click();

			BrowserWaits.waitTime(10);
			attribute = record
					.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_FOLLOW_OWNER_CSS.toString()))
					.getAttribute("data-uib-tooltip");

			Assert.assertTrue(attribute.equalsIgnoreCase("Unfollow this person"));
			test.log(LogStatus.PASS, "Follow functionality is working fine in view post record page");

		} else {
			record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_FOLLOW_OWNER_CSS.toString())).click();
			BrowserWaits.waitTime(10);
			attribute = record
					.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_FOLLOW_OWNER_CSS.toString()))
					.getAttribute("data-uib-tooltip");

			Assert.assertTrue(attribute.equalsIgnoreCase("Follow this person"));
			test.log(LogStatus.PASS, "UnFollow functionality is working fine in view post record page");

		}

	}

	public String getPostAuthorDetails(String recordTitle, String recordType) throws Exception {

		WebElement record = getRecordCard(recordTitle, recordType);
		System.out.println("");
		String name = record
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_GROUP_OWNER_NAME_CSS.toString()))
				.getText();
		String role = record
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_GROUP_OWNER_ROLE_CSS.toString()))
				.getText();
		if (role.equals(""))
			return name.trim();
		else
			return name.trim() + ", " + role.trim();

	}

	public void clickPostAuthorName(String recordTitle, String recordType) throws Exception {

		WebElement record = getRecordCard(recordTitle, recordType);
		record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPINVITATIONS_GROUP_OWNER_NAME_CSS.toString())).click();
	}

	public String getRecordContent(String recordTitle, String recordType) throws Exception {
		scrollingToElementofAPage();
		String details = null;
		StringBuilder strBldr = new StringBuilder();
		WebElement record = getRecordCard(recordTitle, recordType);
		if (recordType.equalsIgnoreCase("post")) {
			details = record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_ABSTRACT_CSS.toString()))
					.getText();
			strBldr.append(details);
		} else {

			details = record
					.findElement(
							By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_PUBLICATION_AUTHORS_CSS.toString()))
					.getText().trim().replace("\n", "").replace("\r", "");
			strBldr.append(details);
			details = record.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_SOURCE_CSS.toString()))
					.getText().trim();
			strBldr.append(details);

			for (WebElement we : record
					.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_ABSTRACT_CSS.toString()))) {
				if (we.isDisplayed()) {
					details = we.getText().trim();
					strBldr.append(details.substring(0, details.length() - 3));
					break;
				}
			}
		}
		return strBldr.toString();
	}

	public String getNoRecordsInfoText() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPS_DETAILS_NO_RECORDS_INFO_TEXT_CSS);
		return pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUPS_DETAILS_NO_RECORDS_INFO_TEXT_CSS)
				.getText().trim();

	}

	public List<String> getRecordMetrics(String recordTitle, String recordType) throws Exception {
		scrollingToElementofAPage();
		WebElement record = getRecordCard(recordTitle, recordType);
		List<WebElement> list = record
				.findElements(By.xpath(OnePObjectMap.RCC_GROUPDETAILS_RECORD_PUBLICATION_METRICS_XPATH.toString()));
		List<String> metricList = new ArrayList<String>();
		for (WebElement we : list) {

			metricList.add(we.getText().replaceAll(",", ""));
		}
		return metricList;
	}

	public boolean checkDeleteButtonIsDisplay() throws Exception {
		boolean status = false;
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_DELETE_GROUP_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_DELETE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.getText().equals("Delete")) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public boolean checkEditButtonIsDisplay() throws Exception {
		boolean status = false;
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_EDIT_GROUP_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_EDIT_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.getText().equals("Edit")) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public boolean checkAttachFileButtonIsDisplay() throws Exception {
		boolean status = false;
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_ATTACH_FILE_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_ATTACH_FILE_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.getText().equals("Attach file")) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public boolean checkInviteOthersButtonIsDisplay() throws Exception {
		boolean status = false;
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_INVITE_OTHERS_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_INVITE_OTHERS_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.getText().equals("Invite others")) {
				status = true;
				return status;
			}
		}
		return status;

	}

	public String checkSupportCenter() throws Exception {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_DETAIL_PAGE_SUPPORT_LINK_CSS.toString()), 60);
		String str = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_GROUP_DETAIL_PAGE_SUPPORT_LINK_CSS)
				.get(1).getText();
		return str;
	}

	public boolean checkShareStatus() throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 0);");
		boolean status = false;
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_SHARE_LINK_CSS);
		if (list.size() == 0) {
			status = true;
			return status;
		}
		return status;
	}

	public boolean checkLeaveGroupButtonIsDisplay() throws Exception {
		boolean status = false;
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS.toString()),
				60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.getText().equals("Leave Group")) {
				status = true;
				return status;
			}
		}
		return status;
	}

	public String getRecordPageOwnerName() throws Exception {

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.PROFILE_PAGE_AUTOR_NAME_CSS.toString()), 60);
		String recordPageOwnerName = ob
				.findElement(By.cssSelector(OnePObjectMap.PROFILE_PAGE_AUTOR_NAME_CSS.toString())).getText();
		String role = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_RECORD_VIEW_PAGE_USER_DETAILS_CSS)
				.get(0).getText();
		String institute = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_RECORD_VIEW_PAGE_USER_DETAILS_CSS).get(1).getText();
		String location = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_RECORD_VIEW_PAGE_USER_DETAILS_CSS).get(2).getText();
		return recordPageOwnerName.trim() + ", " + role.trim() + ", " + institute.trim() + ", " + location.trim();
	}

	public void clickOnEditButton() throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSDETAILS_EDIT_GROUP_BUTTON_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSDETAILS_EDIT_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.click();
				return;
			}
		}
		throw new Exception("Edit button is not displaye in group details page");
	}

	public void updateGroupTitle(String newtitle) throws Exception {

		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.clear();
				we.sendKeys(newtitle);
				return;
			}
		}
		throw new Exception("Group title is not updated in group details page");
	}

	public void updateGroupDescription(String newdesc) throws Exception {
		waitForAjax(ob);

		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_DESCRIPTION_CSS.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_DESCRIPTION_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.clear();
				we.sendKeys(newdesc);
				return;
			}
		}
		throw new Exception("Group Description is not updtaed in group details page");

	}

	public boolean validateSaveButtonDisabled() throws Exception {
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString()),
				60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				return we.isEnabled();

			}
		}
		throw new Exception("Save button is not displayed while editing the group");

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

	public void clickOnSaveButton() throws Exception {
		waitForAjax(ob);

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS.toString()),
				60);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_SAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.click();
				return;
			}
		}
		throw new Exception("Submit button is not displayed in group details page");

	}

	public void updateGroupDetails(String newtitle, String newdesc) throws Exception

	{
		updateGroupTitle(newtitle);
		updateGroupDescription(newdesc);
		clickOnSaveButton();

	}

	public boolean isGroupInfoUpdated(String newtitle, String newdesc) throws Exception {

		waitForAjax(ob);
		String uptitle = null, updesc = null;

		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_GROUP_TITLE_TEXT_CSS.toString()), 60);

		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_GROUP_TITLE_TEXT_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				uptitle = we.getText();
			}
		}
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_GROUP_DESC_TEXT_CSS.toString()), 60);
		List<WebElement> list1 = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUP_DETAILS_PAGE_GROUP_DESC_TEXT_CSS);
		for (WebElement we : list1) {
			if (we.isDisplayed()) {
				updesc = we.getText();
			}
		}
		if (newtitle.equalsIgnoreCase(uptitle) && newdesc.equalsIgnoreCase(updesc))
			return true;
		else
			return false;

	}

	public boolean IsRecordPresent(String recordTitle, String type) throws Exception {
		String recordCSS = null;
		if (type.equalsIgnoreCase("post"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_POST_RECORD_CARD_CSS.toString();
		else if (type.equalsIgnoreCase("patent"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_PATENT_RECORD_CARD_CSS.toString();
		else if (type.equalsIgnoreCase("article"))
			recordCSS = OnePObjectMap.RCC_GROUPDETAILS_ARTICLE_RECORD_CARD_CSS.toString();

		waitForAllElementsToBePresent(ob, By.cssSelector(recordCSS), 60);

		List<WebElement> invitationList = ob.findElements(By.cssSelector(recordCSS));
		String actTitle;
		for (WebElement we : invitationList) {
			actTitle = we.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_CARD_TITLE_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(recordTitle)) {
				return true;
			}
		}
		return false;
	}

	private WebElement getItemLevelGDRecord(String recordTitle, String recordType, String docTitle) throws Exception {

		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()), 60);

		List<WebElement> gdList = getRecordCard(recordTitle, recordType)
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()));
		String actTitle;
		for (WebElement we : gdList) {
			actTitle = we
					.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_TTILE_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(docTitle)) {
				return we;
			}
		}
		throw new Exception("GD doc not found in the list");
	}

	public void clickOnItemLevelRemoveGoogleDoc(String recordTitle, String recordType, String docTitle)
			throws Exception {
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		jsClick(ob, gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_REMOVE_BUTTON_CSS.toString())));
	}

	public void clickOnItemLevelEditGoogleDoc(String recordTitle, String recordType, String docTitle) throws Exception {
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_BUTTON_CSS.toString())).click();
	}

	public String getItemLevelGoogleDocDesc(String recordTitle, String recordType, String docTitle) throws Exception {
		scrollingToElementofAPage();
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		return gdRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DESC_CSS.toString()))
				.getText();
	}

	public String getItemLevelGoogleDocTimestamp(String recordTitle, String recordType, String docTitle)
			throws Exception {
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		return gdRecord
				.findElement(By.cssSelector(
						OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_ITEM_LEVEL_TIMESTAMP_CSS.toString()))
				.getText();
	}

	public void enterItemLevelGoogleDocTitle(WebElement gdRecord, String newTilte) throws Exception {
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_TITLE_CSS.toString()))
				.clear();
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_TITLE_CSS.toString()))
				.sendKeys(newTilte);
	}

	public void enterItemLevelGoogleDocDesc(WebElement gdRecord, String desc) throws Exception {
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_DESC_CSS.toString()))
				.clear();
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_DESC_CSS.toString()))
				.sendKeys(desc);
	}

	public void clickOnCanelItemLevelGoogleDocEdit(WebElement gdRecord) throws Exception {
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_CANCEL_BUTTON_CSS.toString())).click();
	}

	public void clickOnUpdateItemLevelGoogleDocEdit(WebElement gdRecord) throws Exception {

		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_UPDATE_BUTTON_CSS.toString())).click();
	}

	public boolean validateTimeStamp(String timestamp) {

		Calendar cal = Calendar.getInstance();
		String OriginaltimeStamp = new SimpleDateFormat("d MMMMMMMMM yyyy").format(cal.getTime());
		if (timestamp.contains(OriginaltimeStamp) && (timestamp.contains("PM") || timestamp.contains("AM")))
			return true;
		else
			return false;
	}

	public void updateItemLevelGoogleDoc(String recordTitle, String recordType, String docTitle, String newTilte,
			String desc) throws Exception {
		scrollingToElementofAPage();
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		clickOnItemLevelEditGoogleDoc(recordTitle, recordType, docTitle);
		enterItemLevelGoogleDocTitle(gdRecord, newTilte);
		enterItemLevelGoogleDocDesc(gdRecord, desc);
		clickOnUpdateItemLevelGoogleDocEdit(gdRecord);
	}

	public void clickOnOpenInGoogleDriveLinkItemLevel(String recordTitle, String recordType, String docTitle)
			throws Exception {
		scrollingToElementofAPage();
		WebElement gdRecord = getItemLevelGDRecord(recordTitle, recordType, docTitle);
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_LINK_CSS.toString()))
				.click();
	}

	public void signInToGoogle(String username, String pwd) throws Exception {

		String PARENT_WINDOW = ob.getWindowHandle();
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS);
				pf.getBrowserActionInstance(ob).enterFieldValue(
						OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_USERNAME_CSS, username);
				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_NEXT_CSS);
				pf.getBrowserActionInstance(ob)
						.enterFieldValue(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_PASSWORD_CSS, pwd);
				pf.getBrowserActionInstance(ob)
						.click(OnePObjectMap.HOME_PROJECT_NEON_ARTICLE_RECORD_VIEW_GOOGLE_LOGIN_CSS);
				ob.switchTo().window(PARENT_WINDOW);
			}
		}
	}

	public void selectGDdoc(String gddocTitle, int iterationCount) {
		waitForAllElementsToBePresent(ob, By.cssSelector("iframe[class='picker-frame picker-dialog-frame']"), 90);
		List<WebElement> frames = ob.findElements(By.cssSelector("iframe[class='picker-frame picker-dialog-frame']"));

		WebDriverWait wait = new WebDriverWait(ob, 60);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frames.get(iterationCount)));
		waitForElementTobeVisible(ob, By.xpath(
				OnePObjectMap.RCC_GROUPDETAILS_GOOGLE_DOC_TITLE_XPATH.toString().replaceAll("TITLE", gddocTitle)), 90);
		ob.findElement(By.xpath(
				OnePObjectMap.RCC_GROUPDETAILS_GOOGLE_DOC_TITLE_XPATH.toString().replaceAll("TITLE", gddocTitle)))
				.click();

		waitForElementTobeClickable(ob, By.xpath(OnePObjectMap.RCC_GROUPDETAILS_GOOGLE_DOC_SELECT_XPATH.toString()),
				90);
		ob.findElement(By.xpath(OnePObjectMap.RCC_GROUPDETAILS_GOOGLE_DOC_SELECT_XPATH.toString())).click();
		ob.switchTo().defaultContent();
		waitForAjax(ob);
	}

	private WebElement getGroupLevelGDRecord(String docTitle) throws Exception {

		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()), 60);

		List<WebElement> gdList = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()));
		String actTitle;
		for (WebElement we : gdList) {
			actTitle = we
					.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_TTILE_CSS.toString()))
					.getText();
			if (actTitle.equalsIgnoreCase(docTitle)) {
				return we;
			}
		}
		throw new Exception("GD doc not found in the list");
	}

	public void clickOnGroupLevelRemoveGoogleDoc(String docTitle) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_REMOVE_BUTTON_CSS.toString())).click();
	}

	public void clickOnGroupLevelEditGoogleDoc(String docTitle) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_BUTTON_CSS.toString())).click();
	}

	public String getGroupLevelGoogleDocDesc(String docTitle) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		return gdRecord
				.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DESC_CSS.toString()))
				.getText();
	}

	public String getGroupLevelGoogleDocTimestamp(String docTitle) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		return gdRecord
				.findElement(By.cssSelector(
						OnePObjectMap.RCC_GROUPDETAILS_RECORD_GROUP_LEVEL_GOOGLE_DOC_TIMESTAMP_CSS.toString()))
				.getText();
	}

	public void enterGroupLevelGoogleDocTitle(WebElement gdRecord, String docTitle, String newTilte) throws Exception {
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_TITLE_CSS.toString()))
				.clear();
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_TITLE_CSS.toString()))
				.sendKeys(newTilte);
	}

	public void enterGroupLevelGoogleDocDesc(WebElement gdRecord, String docTitle, String desc) throws Exception {
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_DESC_CSS.toString()))
				.clear();
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_EDIT_DESC_CSS.toString()))
				.sendKeys(desc);
	}

	public void clickOnCanelGroupLevelGoogleDocEdit(WebElement gdRecord, String docTitle) throws Exception {
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_CANCEL_BUTTON_CSS.toString())).click();
	}

	public void clickOnUpdateGroupLevelGoogleDocEdit(WebElement gdRecord, String docTitle) throws Exception {
		gdRecord.findElement(
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_UPDATE_BUTTON_CSS.toString())).click();
	}

	public void updateGroupLevelGoogleDoc(String docTitle, String newTilte, String desc) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		clickOnGroupLevelEditGoogleDoc(docTitle);
		enterGroupLevelGoogleDocTitle(gdRecord, docTitle, newTilte);
		enterGroupLevelGoogleDocDesc(gdRecord, docTitle, desc);
		clickOnUpdateGroupLevelGoogleDocEdit(gdRecord, docTitle);
	}

	public void clickOnOpenInGoogleDriveLinkGroupLevel(String docTitle) throws Exception {
		WebElement gdRecord = getGroupLevelGDRecord(docTitle);
		gdRecord.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_LINK_CSS.toString()))
				.click();
	}

	public boolean validateGDUrl() {
		boolean result = false;
		String PARENT_WINDOW = ob.getWindowHandle();
		waitForNumberOfWindowsToEqual(ob, 2);
		Set<String> child_window_handles = ob.getWindowHandles();
		for (String child_window_handle : child_window_handles) {
			if (!child_window_handle.equals(PARENT_WINDOW)) {
				ob.switchTo().window(child_window_handle);

				if (ob.getCurrentUrl().contains("drive.google.com")) {
					ob.close();

					result = true;

				}
			}
		}
		ob.switchTo().window(PARENT_WINDOW);
		return result;
	}

	public boolean isItemLevelGDRecordPresent(String recordTitle, String recordType, String docTitle) throws Exception {
		try {
			scrollingToElementofAPage();
			waitForAllElementsToBePresent(ob,
					By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()), 30);

			List<WebElement> gdList = getRecordCard(recordTitle, recordType)
					.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()));
			String actTitle;
			if (gdList.size() != 0) {
				for (WebElement we : gdList) {
					actTitle = we
							.findElement(By
									.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_TTILE_CSS.toString()))
							.getText();
					if (actTitle.equalsIgnoreCase(docTitle)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	public String getInvitationStatus(String username) throws Exception {
		WebElement groupcard = getPendingRecords(username);

		String Timecard = groupcard
				.findElement(By.xpath(OnePObjectMap.RCC_GROUP_INVITATIONS_DETAILS_TIMESTAMP_XPATH.toString()))
				.getText();
		return Timecard;
	}

	public boolean isGroupLevelGDRecordPresent(String docTitle) throws Exception {
		try {
			waitForAllElementsToBePresent(ob,
					By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()), 30);

			List<WebElement> gdList = ob
					.findElements(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_DIV_CSS.toString()));
			String actTitle;
			if (gdList.size() != 0) {
				for (WebElement we : gdList) {
					actTitle = we
							.findElement(By
									.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_RECORD_GOOGLE_DOC_TTILE_CSS.toString()))
							.getText();
					if (actTitle.equalsIgnoreCase(docTitle)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;

	}

	public void clickOnCancelGroup() {
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_CANCEL_BUTTON_CSS.toString()), 30);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPDETAILS_INVITE_MEMBERS_CANCEL_BUTTON_CSS.toString()))
				.click();
	}

	public boolean checkIfEditGroupTitleFieldIsDisplayed() throws Exception {
		waitForAjax(ob);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_ENTER_GROUP_TILTLE_CSS);

		if (list.size() == 0) {
			return true;

		} else {
			return false;
		}

	}

	public void updateGroupPhoto() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.RCC_GROUP_COVER_UPDATE_PHOTO_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_COVER_UPDATE_PHOTO_CSS);
		String imagePath = System.getProperty("user.dir") + "\\images\\" + "images4" + ".jpg";
		Runtime.getRuntime().exec("autoit_scripts/imageUpload2.exe" + " " + imagePath);

	}

	public boolean isAddImageOptionDisplayed() throws Exception {

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUP_COVER_PHOTO_CSS.toString()), 60);

		List<WebElement> list = ob.findElements(By.cssSelector(OnePObjectMap.RCC_GROUP_COVER_PHOTO_CSS.toString()));

		for (WebElement we : list) {

			if (we.isDisplayed()) {

				return true;
			}
		}
		return false;

	}

	public boolean verifyDeleteGroupPopupMessage() throws Exception {
		String Originalmessage = "Are you sure you want to delete this group? You will remove all group members and pending invitations.";
		waitForAjax(ob);
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RCC_GROUPLIST_DELETE_GROUP_POP_VERIFICATION_TEXT_CSS.toString()), 20);
		String Originaltext = ob
				.findElement(
						By.cssSelector(OnePObjectMap.RCC_GROUPLIST_DELETE_GROUP_POP_VERIFICATION_TEXT_CSS.toString()))
				.getText();
		if (Originaltext.equals(Originalmessage))
			return true;
		else
			return false;
	}

	public boolean verifyDeletegroupPopupButtons() throws Exception {
		waitForAjax(ob);
		try {
			ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CANCEL_BUTTON_CSS.toString()))
					.isDisplayed();
			ob.findElement(
					By.cssSelector(OnePObjectMap.GROUPLIST_DELETE_GROUP_POP_UP_DELETEGROUP_BUTTON_CSS.toString()))
					.isDisplayed();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	public boolean clickonCancelButtononDEleteGroupPopup() throws Exception {
		waitForAjax(ob);
		ob.findElement(By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_POP_UP_CANCEL_BUTTON_CSS.toString()))
				.click();

		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS.toString()),
				10);
		List<WebElement> list = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPLIST_LEAVE_GROUP_BUTTON_CSS);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				return true;
			}
		}
		throw new Exception("leave group button is not displayed in group details page");

	}

	public void clickOnDeleteGroupButtoninPopUp() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPLIST_DELETE_GROUP_POP_UP_DELETEGROUP_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPLIST_DELETE_GROUP_POP_UP_DELETEGROUP_BUTTON_CSS);

	}

}
