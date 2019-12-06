package pages;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class Watchlist extends TestBase {

	PageFactory pf;

	public Watchlist(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();

	}

	public void clearWatchlist() throws Exception {
		pf.getHFPageInstance(ob).clickOnWatchLink();
		List<WebElement> watchLists = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.HOME_PROJECT_NEON_WATCHLIST_RECORDS_CSS);
		if (watchLists.size() > 0) {
			for (WebElement watchList : watchLists) {
				watchList.click();
				BrowserWaits.waitTime(2);
			}
		}
	}

	public void validateWatchlist(String watchlist) throws Exception {
		String expWatchlistPost = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_WATCHLIST_CSS).getText();
		// System.out.println("postwatch-->"+expWatchlistPost);
		if (!expWatchlistPost.equalsIgnoreCase(watchlist)) {
			throw new Exception("Post is not adding into my watchlist");
		}
	}
	
	
	public String getDocumentTitleInWatchlist(String documenType) throws Exception {
		String title = null;
		List<WebElement> docs=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS);
		for(WebElement doc:docs) {
			String docType=doc.findElement(By.cssSelector("span[class='ng-binding ng-scope']")).getText();
			logger.info("Type-->"+docType);
			if(docType.equalsIgnoreCase(documenType)) {
				 title=doc.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString())).getText();
				 break;
			}
		}
		return title;
	}
	
	public void addDoucmentToGroupFromWatchlist(String groupTitle,String documentTitle) throws Exception {
		
		List<WebElement> docs=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS);
		for(WebElement doc:docs) {
			String title;
				title=doc.findElement(By.tagName("a")).getText();
				logger.info("title"+title);
				if(title.equalsIgnoreCase(documentTitle)) {
					WebElement groupElement= doc.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_DOCUMENT_ADD_TO_GROUP_CSS.toString()));
					addDocumentToGroup(groupTitle, groupElement);
					break;
				}
				else {
					title=doc.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString())).getText();
					if(title.equalsIgnoreCase(documentTitle)) {
					WebElement groupElement= doc.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_DOCUMENT_ADD_TO_GROUP_CSS.toString()));
					addDocumentToGroup(groupTitle, groupElement);
					break; 
				}
			}
		}
	}
	
	
	public void addDocumentToGroup(String groupTitle,WebElement groupWebElement) throws Exception {
		scrollElementIntoView(ob, groupWebElement);
		jsClick(ob, groupWebElement);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_CSS);
		List<WebElement> groupDropdownListItems=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_LIST_CSS);
		for(WebElement groupDropdownListItem: groupDropdownListItems) {
			logger.info("Group Title-->"+groupDropdownListItem.findElement(By.tagName("span")).getText());
			if(groupDropdownListItem.findElement(By.tagName("span")).getText().equalsIgnoreCase(groupTitle)) {
				//checking it's already added to group or not, if added no need to add 
				String addedToGroup=groupDropdownListItem.findElement(By.tagName("button")).getAttribute("class");
				if(!(StringUtils.contains(addedToGroup, "--active"))) {
					//groupDropdownListItem.findElement(By.tagName("button")).click();
					jsClick(ob, groupDropdownListItem.findElement(By.tagName("button")));
					BrowserWaits.waitTime(4);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
					String addedGroup=groupDropdownListItem.findElement(By.tagName("button")).getAttribute("class");
					logger.info("Added to group or not-->"+addedGroup);
					if(!(StringUtils.contains(addedGroup, "--active"))) {
						throw new Exception("Document not added to Group from Search results page");
					}
					break;
				} else {
					throw new Exception("no group availble from Add to Group dropdown with or Already added to group"+groupTitle);
				}
			}
				
		}
	}
	
public void addDoucmentToMutipleGroupsFromWatchlist(List<String> groupTitle,String documentTitle) throws Exception {
		
		List<WebElement> docs=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS);
		for(WebElement doc:docs) {
			String title;
				title=doc.findElement(By.tagName("a")).getText();
				logger.info("title"+title);
				if(title.equalsIgnoreCase(documentTitle)) {
					WebElement groupElement= doc.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_DOCUMENT_ADD_TO_GROUP_CSS.toString()));
					addDocumentToMultipleGroups(groupTitle, groupElement);
					break;
				}
				else {
					title=doc.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_POST_TITLE_CSS.toString())).getText();
					if(title.equalsIgnoreCase(documentTitle)) {
					WebElement groupElement= doc.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_DOCUMENT_ADD_TO_GROUP_CSS.toString()));
					addDocumentToMultipleGroups(groupTitle, groupElement);
					break; 
				}
			}
		}
	}
	
	
	public void addDocumentToMultipleGroups(List<String> groupTitles,WebElement groupWebElement) throws Exception {
		scrollElementIntoView(ob, groupWebElement);
		jsClick(ob, groupWebElement);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_CSS);
		List<WebElement> groupDropdownListItems=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_ADD_TO_GROUP_DROPDOWN_LIST_CSS);
		for(String groupTitle:groupTitles) {
		for(WebElement groupDropdownListItem: groupDropdownListItems) {
			logger.info("Group Title-->"+groupDropdownListItem.findElement(By.tagName("span")).getText());
			if(groupDropdownListItem.findElement(By.tagName("span")).getText().equalsIgnoreCase(groupTitle)) {
				//checking it's already added to group or not, if added no need to add 
				String addedToGroup=groupDropdownListItem.findElement(By.tagName("button")).getAttribute("class");
				if(!(StringUtils.contains(addedToGroup, "--active"))) {
					scrollElementIntoView(ob, groupDropdownListItem.findElement(By.tagName("button")));
					jsClick(ob, groupDropdownListItem.findElement(By.tagName("button")));
					BrowserWaits.waitTime(4);
					pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS);
					String addedGroup=groupDropdownListItem.findElement(By.tagName("button")).getAttribute("class");
					logger.info("Added to group or not-->"+addedGroup);
					if(!(StringUtils.contains(addedGroup, "--active"))) {
						throw new Exception("Document not added to Group from Search results page");
					}
				} else {
					throw new Exception("no group availble from Add to Group dropdown with or Already added to group"+groupTitle);
				}
			}
				
		}
	 }
	}
	
}
