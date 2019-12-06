package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * This class contains all the method related to account page
 * 
 * @author uc205521
 *
 */
public class GroupsPage extends TestBase {

	PageFactory pf;

	public GroupsPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();//
	}

	public void clickOnGroupsLink() throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 0);");
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_GROUPS_LINK__CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_GROUPS_LINK__CSS);
		waitForAjax(ob);
	}
	public void clickOnGroupsTab() throws Exception {
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_GROUPTAB_HEADER_XPATH);
		//BrowserWaits.waitTime(6);
		fluentwaitforElement(ob, By.xpath(OnePObjectMap.RCC_GROUP_GROUPTAB_HEADER_XPATH.toString()), 300);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.RCC_GROUP_GROUPTAB_HEADER_XPATH);
		waitForAjax(ob);
		/*
		 * pf.getBrowserWaitsInstance(ob)
		 * .waitUntilElementIsNotDisplayed(OnePObjectMap.
		 * NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
		 */
	}

	public void clickOnCreateNewGroupButton() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_CREATE_NEW_GROUP_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_CREATE_NEW_GROUP_BUTTON_CSS);

	}

	public int getGroupsCount() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_GROUPS_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUP_GROUPS_COUNT_CSS);
		int groupsCount = Integer.parseInt(count.getText());
		return groupsCount;
	}

	public int getInvitationsCount() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_INVITATIONS_COUNT_CSS);
		WebElement count = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.RCC_GROUP_INVITATIONS_COUNT_CSS);
		int invitationsCount = Integer.parseInt(count.getText());
		return invitationsCount;
	}

	public void switchToInvitationTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_INVITATIONLINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_INVITATIONLINK_CSS);
		waitForAjax(ob);
	}

	public void switchToGroupTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPLINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPLINK_CSS);
		waitForAjax(ob);
	}

	public int countGroupsTabOverlay() throws InterruptedException {
		int i = 0;
		BrowserWaits.waitTime(2);
		List<WebElement> element = ob
				.findElements(By.cssSelector(OnePObjectMap.RCC_COUNT_GROUP_OVERLAY_CSS.toString()));
		if (element.size() == 1 && !element.get(0).getText().equalsIgnoreCase("")) {
			int countGroupsTabOverlay1 = Integer.parseInt(element.get(0).getText());
			i = countGroupsTabOverlay1;
		}
		return i;
	}

	public String defaultselectedTab() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_DEFAULT_SELECTED_INVITATION_TAB_CSS);
		WebElement element = ob
				.findElement(By.cssSelector(OnePObjectMap.RCC_DEFAULT_SELECTED_INVITATION_TAB_TEXT_CSS.toString()));
		String str = element.getText();
		return str;
	}

	public void acceptInvitation() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPINVITATIONS_ACCEPT_INVITAION_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPINVITATIONS_ACCEPT_INVITAION_BUTTON_CSS);
	}

	public void declineInvitation() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUPINVITATIONS_DECLINE_INVITAION_BUTTON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUPINVITATIONS_DECLINE_INVITAION_BUTTON_CSS);
	}

	public void clickOnInvitationTab() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_LEFT_NAV_PANE_CSS);
		ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULT_PAGE_SORT_LEFT_NAV_PANE_CSS.toString())).get(1)
		 		.click();
	}

	public void listOfPendingInvitaions() throws Exception {
		BrowserWaits.waitTime(3);
		List<WebElement> groupsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.RCC_GROUPSLIST_GROUP_CARD_CSS);
		for (int i = 0; i < groupsList.size(); i++) {
			declineInvitation();
			waitForAjax(ob);
		}
	}

	public void CilckGroupTab() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.RCC_GROUP_BUTTON_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.RCC_GROUP_BUTTON_XPATH);
	}

	public void clickOnGroupsTabFirstRecord() throws Exception {

		waitForAjax(ob);
		waitForAllElementsToBePresent(ob, By.xpath(OnePObjectMap.RCC_DESC_MSG_TEXT_XPATH.toString()), 60);
		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.RCC_DESC_MSG_TEXT_XPATH);
		for (WebElement we : list) {
			if (we.isDisplayed()) {
				we.click();
				return;
			}
		}
		throw new Exception("Edit button is not displaye in group details page");
	}
}
