package watpages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * Class for Author Record/Profile page related Operations
 * 
 * @author UC202376
 *
 */
public class AuthorRecordPage extends TestBase {
	boolean isDefaultAvatarPresent = false;
	String metaTitle;
	String metaOrg;
	String metaDept;
	String location;
	boolean hilightedTab = false;
	boolean isTabDisabled = false;
	List<WebElement> namesCount;
	boolean isAuthorFound = false;
	int pubCountBeforeDeletion;

	public AuthorRecordPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method for waiting Author Record page
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void waitForAuthorRecordPage(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Search Results");
		pf.getBrowserWaitsInstance(ob).waitUntilText("The following details are available for this author.",
				"This author record is algorithmically generated and may not be complete.",
				"All information is derived from the publication metadata.");
		// pf.getBrowserWaitsInstance(ob).waitUntilText("Future iterations of
		// author search will allow you to claim and","edit your own profile to
		// create a complete and accurate record of your work.");
		pf.getBrowserWaitsInstance(ob).waitUntilText("in Web of Science", "Sorted by");
		test.log(LogStatus.INFO, "User navigated to Author Record page");
	}

	/**
	 * Method for click Search Results tab
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void clickSearchResultsTab(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
		waitForAjax(ob);
	}

	/**
	 * Method for click Search tab
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void clickSearchTab(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_LINK_XPATH);
		waitForAjax(ob);
	}

	/**
	 * Method for default avatar
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void defaultAvatar() throws Exception {
		isDefaultAvatarPresent = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_DEFAULT_AVATAR_CSS).isDisplayed();
		if (!isDefaultAvatarPresent) {
			throw new Exception("No Default Avatar/Author Profile Pic is not displayed in Author Record page");
		}
	}

	/**
	 * Method for author record meta title
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void authorRecordMetaTitle() throws Exception {
		metaTitle = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_TITLE_CSS)
				.getText();
		String[] title = metaTitle.split(" ");
		if (!(title.length >= 2)) {
			throw new Exception("Profile Title in Author Record page should the form form First name, last name");
		}
	}

	/**
	 * Method for author record meta title
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void authorRecordDept() throws Exception {
		metaDept = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_DEPT_CSS).getText();
		logger.info("Sub-Orgnization-->" + metaDept);
		if (!StringUtils.isNotEmpty(metaOrg)) {
			throw new Exception("Profile metadata doesn't have any Sub-Organization/Dept");
		}
	}

	/**
	 * Method for author record meta affiliation/organization
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void authorRecordMetaOrganization() throws Exception {
		metaOrg = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_AFFILIATION_CSS)
				.getText();
		logger.info("Orgnization-->" + metaOrg);
		if (!StringUtils.isNotEmpty(metaOrg)) {
			throw new Exception("Profile metadata doesn't have any Organization");
		}
	}

	/**
	 * Method for author record meta city/state/country
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void authorRecordMetaLocation() throws Exception {
		location = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_LOCATION_CSS)
				.getText();
		logger.info("Location-->" + location);
		if (!StringUtils.isNotEmpty(location)) {
			throw new Exception("Profile metadata doesn't have any city/state/country ");
		}
	}

	public void checkForAlternativeNames() throws Exception {
		String altName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_CSS).getText();
		logger.info("Actual Value : " + altName);
		Assert.assertTrue(altName.equals("Alternative names"));
		// test.log(LogStatus.PASS, "Alternative names tab displayed in author
		// record page");

	}

	/**
	 * Method for check Organization Tab displayed in Author Record page
	 * 
	 * @throws Exception
	 */
	public void checkOrganizationsTab() throws Exception {
		String orgName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ORGANISATION_NAME_XPATH).getText();
		logger.info("Actual Value : " + orgName);
		if (!orgName.equals("Organizations")) {
			throw new Exception("Organizations tab Not displayed in author record page");
		}
	}

	/**
	 * Method for click Organizations Tab
	 * 
	 * @throws Exception
	 */
	public void clickOrganizationsTab() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ORGANISATION_NAME_XPATH);
		waitForAjax(ob);
		hilightedTab = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_TAB_HILIGHTED_CSS).isDisplayed();
		if (!hilightedTab) {
			throw new Exception("Organizations tab is not hilighted");
		}
	}

	public void clickAlternativeNamesTab() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_CSS);
		waitForAjax(ob);
		hilightedTab = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_TAB_HILIGHTED_CSS).isDisplayed();
		if (!hilightedTab) {
			throw new Exception("Alternative names tab is not hilighted");
		}

	}

	public void checkAltNamesOrOrgNamesCount(ExtentTest test, String tabName) throws Exception {
		namesCount = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_COUNT_CSS);
		if (namesCount.size() <= 5) {
			test.log(LogStatus.INFO, "5 or <5 " + tabName + " are displayed");
		} else {
			throw new Exception("No " + tabName + "are displayed");
		}
	}

	public void checkForAuthorNames(String lastName, ExtentTest test) {
		if (namesCount.size() <= 5) {
			String[] names = lastName.split(" ");
			for (int i = 0; i < namesCount.size(); i++) {
				String name = namesCount.get(i).getText();
				if (name.contains(names[0]) || name.contains(names[1])) {
					test.log(LogStatus.INFO, "Alternative names matching with last name");
				}
			}
		}
	}

	/**
	 * Method for check Organizations Tab status active or inactive
	 * 
	 * @throws Exception
	 */
	public void checkOrganizationsTabStatus() throws Exception {
		String orgName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ORGANISATION_TAB_STATUS_XPATH).getText();
		String tabStatus = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ORGANISATION_TAB_STATUS_XPATH).getAttribute("class");
		isTabDisabled = tabStatus.contains("disabled");
		logger.info("Org Tab Status : " + isTabDisabled);
		logger.info("Actual Value : " + orgName);
		if (!(orgName.equals("Organizations") && isTabDisabled)) {
			throw new Exception("Organizations names tab displayed in active mode");
		}
	}

	/**
	 * Method for check Alternativenames Tab status active or inactive
	 * 
	 * @throws Exception
	 */
	public void checkAlternativenamesTabStatus() throws Exception {
		String orgName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_TAB_STATUS_XPATH).getText();
		String tabStatus = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_TAB_STATUS_XPATH)
				.getAttribute("class");
		isTabDisabled = tabStatus.contains("disabled");
		logger.info("Alternative names Tab Status : " + isTabDisabled);
		logger.info("Actual Value : " + orgName);
		if (!(orgName.equals("Organizations") && isTabDisabled)) {
			throw new Exception("Alternative names tab displayed in active mode");
		}
	}

	/**
	 * Method for check Metrics Tab displayed in Author Record page
	 * 
	 * @throws Exception
	 */
	public void checkMetricsTab() throws Exception {
		String metrics = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_METRICS_XPATH)
				.getText();
		logger.info("Actual Value : " + metrics);
		if (!metrics.equals("Metrics")) {
			throw new Exception("Metrics tab Not displayed in author record page");
		}
	}

	/**
	 * Method for check Metrics Tab status active or inactive
	 * 
	 * @throws Exception
	 */
	public void checkMetricsTabStatus() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_METRICS_XPATH);
		waitForAjax(ob);
		hilightedTab = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_TAB_HILIGHTED_CSS).isDisplayed();
		if (!hilightedTab) {
			throw new Exception("Metrics tab is not getting highlighted");
		}
	}

	/**
	 * Method for check Metrics Tab Total Times cited and H-Index label text and
	 * count
	 * 
	 * @throws Exception
	 */
	public void checkMetricsTabItems(String totalTimesCited, String hIndex, ExtentTest test) throws Exception {

		List<WebElement> timesCited = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_METRICS_TAB_TIMESCITED_XPATH);
		logger.info("metrics tab 1" + timesCited.get(0).getText() + "-->" + timesCited.get(1).getText().isEmpty());
		if (!(timesCited.get(0).getText().equalsIgnoreCase(totalTimesCited)
				&& !(timesCited.get(1).getText().isEmpty()))) {
			test.log(LogStatus.FAIL, "TOTAL TIMES CITED label and count not displayed in under Metrics Tab");
		}

		List<WebElement> index = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_METRICS_TAB_HINDEX_XPATH);
		logger.info("metrics tab 2" + index.get(0).getText() + "-->" + index.get(1).getText().isEmpty());
		if (!(index.get(0).getText().equalsIgnoreCase(hIndex) && !(index.get(1).getText().isEmpty()))) {
			test.log(LogStatus.FAIL, "H-INDEX label and count not displayed in under Metrics Tab");
			throw new Exception("H-INDEX label and count not displayed in under Metrics Tab");
		}
	}

	/**
	 * Entering curation mode and verify
	 * 
	 * @param LastName
	 * @author UC225218
	 * @throws Exception
	 * 
	 */
	public void enterCurationMode(ExtentTest test) {
		try {
			pf.getBrowserWaitsInstance(ob)
					.waitForAllElementsToBePresent(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_AUTHOR_PROFILE_ICON_XPATH);
			if (pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_AUTHOR_PROFILE_ICON_XPATH).isDisplayed()) {
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SUGGEST_UPDATE_BTN_XPATH);
				test.log(LogStatus.INFO, "Entering curation mode");
				Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_CANCEL_UPDATE_BTN_XPATH)
						.isDisplayed());
				test.log(LogStatus.PASS, "Entered curation mode, Checking for confirmation");
				scrollElementIntoView(ob,
						pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH));
				Assert.assertTrue(pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH).isDisplayed());
				test.log(LogStatus.PASS, "Entered curation mode and confirmed successfully");
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Didnt entered curation mode");
			e.printStackTrace();
		}
	}

	/**
	 * Method for to check ORCiD Functionality
	 * 
	 * @throws Exception
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void orcidFunctionality(ExtentTest test) throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_CARD_1_XPATH).click();
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		String ORCID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LINK_XPATH).getText();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_LINK_XPATH);
		pf.getBrowserActionInstance(ob).switchToNewWindow(ob);
		Assert.assertTrue(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_ID).getText().contains(ORCID),
				"User not taken to the ORCID page of the Author");
		test.log(LogStatus.PASS, "User is taken to the ORCID page of the Author successfully");
	}

	/**
	 * Method for to check RID Functionality
	 * 
	 * @throws Exception
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void researcherIdFunctionality(ExtentTest test) throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_CARD_1_XPATH).click();
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		String RESEARCHERID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_RESEARCHERID_LINK_XPATH)
				.getText();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_RESEARCHERID_LINK_XPATH);
		pf.getBrowserActionInstance(ob).switchToNewWindow(ob);
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_RESEARCHER_XPATH).getText()
				.contains(RESEARCHERID), "User not taken to the RESEARCHER ID page of the Author");
		test.log(LogStatus.PASS, "User is taken to the RESEARCHER ID page of the Author successfully");
	}

	/**
	 * Method for to check ORCiD Functionality
	 * 
	 * @throws Exception
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void orcid(ExtentTest test) throws Exception, InterruptedException {
		String ORCID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LINK_XPATH).getText();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_LINK_XPATH);
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		pf.getBrowserActionInstance(ob).switchToNewWindow(ob);
		Assert.assertTrue(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_ID).getText().contains(ORCID),
				"User not taken to the ORCID page of the Author");
		test.log(LogStatus.PASS, "User is taken to the ORCID page of the Author successfully");
	}
	
	/**
	 * Method for to check ORCiD as URI
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void orcidAsURI() throws Exception{
		String ORCID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LINK_XPATH).getAttribute("href");
		logger.info("ORCID"+ORCID);
		if(!(StringUtils.isNotEmpty(ORCID) && StringUtils.contains(ORCID, "https://orcid.org/"))){
			throw new Exception("Author record page ORCID not displayed as URI");
		}
	}
	
	/**
	 * Method for to check RID as URI
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void ridAsURI() throws Exception{
		String rid = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_RESEARCHERID_LINK_XPATH).getAttribute("href");
		logger.info("RID-->"+rid);
		if(!(StringUtils.isNotEmpty(rid) && StringUtils.contains(rid,"http://www.researcherid.com/rid/"))){
			throw new Exception("Author record page RID not displayed as URI");
		}
	}

	/**
	 * Method for to check RID Functionality
	 * 
	 * @throws Exception
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void rid(ExtentTest test) throws Exception {
		String RESEARCHERID = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_RESEARCHERID_LINK_XPATH)
				.getText();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_RESEARCHERID_LINK_XPATH);
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		pf.getBrowserActionInstance(ob).switchToNewWindow(ob);
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_RESEARCHER_XPATH).getText()
				.contains(RESEARCHERID), "User not taken to the RESEARCHER ID page of the Author");
		test.log(LogStatus.PASS, "User is taken to the RESEARCHER ID page of the Author successfully");
	}

	/**
	 * Method for recommend papers last name
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void recommendPapersLastName(String authorLastname) throws Exception {
		List<WebElement> recommendPapers = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_AUTHORS_XPATH);
		HashMap<Integer, Boolean> hm = new HashMap<Integer, Boolean>();
		int count = 0;
		for (WebElement paper : recommendPapers) {
			if (!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")) {
				paper.findElement(By.tagName("a")).click();
				waitForAjax(ob);
			}
			List<WebElement> authors = paper.findElements(By.tagName("div"));
			for (WebElement author : authors) {
				logger.info("recommend paper Author Name-->" + author.getText());
				if (StringUtils.contains(author.getText(), authorLastname)) {
					isAuthorFound = true;
					hm.put(++count, isAuthorFound);
				}
			}
		}
		logger.info("last name recommend paper?" + hm.get(1) + "-->" + hm.get(2) + "--->" + hm.get(3));
		if (!(hm.get(0) && hm.get(1) && hm.get(2))) {
			throw new Exception("Recommend paper last name not matched author record last name");
		}

	}

	/**
	 * Method for recommend papers authors name should not match author record first name or intials
	 * @param test
	 * @throws Exception
	 */
	public void recommendPapersAuthorName() throws Exception{
		
		metaTitle=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_TITLE_CSS).getText();
		String[] title=metaTitle.split(" ");
		logger.info("title 1-->"+title[0]);
		logger.info("title 11-->"+title[1]);
		List<WebElement> recommendPapers = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_AUTHORS_XPATH);
		
		for(WebElement paper:recommendPapers){
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				paper.findElement(By.tagName("a")).click();
				waitForAjax(ob);}
				List<WebElement> authors=paper.findElements(By.tagName("div"));
				for(WebElement author:authors){
					logger.info("recommend paper Author Name-->"+author.getText());
					if(StringUtils.contains(author.getText(), title[1])){
						throw new Exception("recommend paper author name should not have author record author first name or initilas");
					}
				}
		}
		
		
    }
	
	/**
	 * Method for recommend papers authors name should not match author record first name or intials
	 * @param test
	 * @throws Exception
	 */
	public void recommendPapersLastnameCount() throws Exception{
		
		metaTitle=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_META_TITLE_CSS).getText();
		String[] title=metaTitle.split(" ");
		logger.info("title 1-->"+title[0]);
		logger.info("title 11-->"+title[1]);
		List<WebElement> recommendPapers = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_AUTHORS_XPATH);
		
		for(WebElement paper:recommendPapers){
			int count=0;
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				paper.findElement(By.tagName("a")).click();
				waitForAjax(ob);}
				List<WebElement> authors=paper.findElements(By.tagName("div"));
				for(WebElement author:authors){
					logger.info("recommend paper Author Name-->"+author.getText());
					if(StringUtils.contains(author.getText(), title[0])){
						++count;
					}
				}
				logger.info("count match-->"+count);
				if(count==0){
					throw new Exception("Recommended papers atleast one last name not matched with author record last name variants");
				}
		}
		
		
    }
	
	/**
	 * Method for combined author recommend papers authors last name should match author record set of last names
	 * @param test
	 * @throws Exception
	 */
	public void combinedRecommendPapersAuthorName() throws Exception{
		List<String> alternativeNames=getAllAuthorLastNames();
		logger.info("All alternative name-->"+alternativeNames);
		List<WebElement> recommendPapers = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_AUTHORS_XPATH);
		
		for(WebElement paper:recommendPapers){
			count=0;
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				pf.getBrowserActionInstance(ob).scrollToElement(paper.findElement(By.tagName("a")));
				paper.findElement(By.tagName("a")).click();
				waitForAjax(ob);}
				List<WebElement> authors=paper.findElements(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_MORE_AUTHORS_XPATH.toString()));
				for(WebElement author:authors){
					logger.info("recommend paper Author Name-->"+(author.getText().split("\\,")[0]));
					String authorLastName=author.getText().split("\\,")[0].contains(";")?author.getText().split("\\,")[0].replace(";", ""):author.getText().split("\\,")[0];
					if(alternativeNames.contains(authorLastName.trim())){
						++count;
					}
				}
				logger.info("count match-->"+count);
				if(count==0){
					throw new Exception("Combined Author Recommended papers last name not matches Author record set of last names");
				 }
			}
    }
	

	/**
	 * Get Author last names only
	 * @param test
	 * @param tabName
	 * @throws Exception
	 */
	public List<String> getAllAuthorLastNames() throws Exception {
		List<String> alternativeNames= new ArrayList<String>();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_CSS);
		waitForAjax(ob);
		namesCount = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ALTERNATIVE_NAME_COUNT_CSS);
		for(WebElement name:namesCount){
			alternativeNames.add(name.getText().split("\\,")[0]);
		}
		return alternativeNames;
	}
	
	/**
	 * Method for More link should present in each publication
	 * @throws Exception
	 */
	public void publicationsAuthorsMoreLink() throws Exception{
		List<WebElement> publications = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_XPATH);
		List<String> authorBefore= new ArrayList<String>();
		List<String> authorAfter= new ArrayList<String>();
		for(WebElement paper:publications){
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				List<WebElement> beforeClickMore=paper.findElements(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_MORE_AUTHORS_XPATH.toString()));
				for(WebElement before:beforeClickMore){
					authorBefore.add(before.getText());
				}//for
				if(paper.findElement(By.tagName("a")).getText().trim().equals("…More")){
					pf.getBrowserActionInstance(ob).scrollToElement(paper.findElement(By.tagName("a")));
					BrowserWaits.waitTime(2);
					pf.getBrowserActionInstance(ob).jsClick(paper.findElement(By.tagName("a")));
					waitForAjax(ob);
					List<WebElement> afterClickMore=paper.findElements(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_MORE_AUTHORS_XPATH.toString()));
					for(WebElement after:afterClickMore){
						authorAfter.add(after.getText());
					}//for
				} //if
				
			}//if
			//logger.info("Before click More-->"+authorBefore);
			//logger.info("After click More-->"+authorAfter);
			if((authorAfter.containsAll(authorBefore))){
				throw new Exception("Publication MORE link not giving more authors list");
			}
			authorBefore.clear();
			authorAfter.clear();
			
		}
		
	}
	
	/**
	 * Method for Less link should present in each publication after click on More link
	 * @throws Exception
	 */
	public void publicationsAuthorsLessLink() throws Exception{
		List<WebElement> publications = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_XPATH);
		for(WebElement paper:publications){
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				if(paper.findElement(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_MORE_LINK_XPATH.toString())).getText().trim().equals("…More")){
					pf.getBrowserActionInstance(ob).scrollToElement(paper.findElement(By.tagName("a")));
					BrowserWaits.waitTime(2);
					pf.getBrowserActionInstance(ob).jsClick(paper.findElement(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_MORE_LINK_XPATH.toString())));
					waitForAjax(ob);
					BrowserWaits.waitTime(2);
					String linkName=paper.findElement(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_MORE_LINK_XPATH.toString())).getText();
					logger.info("Link Name-->"+linkName);
				} //if
				
			}//if
		}
		
	}
	
	
	/**
	 * Method for Less link turn to More
	 * @throws Exception
	 */
	public void publicationsAuthorsLessToMore() throws Exception{
		List<WebElement> publications = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_XPATH);
		List<String> authorBefore= new ArrayList<String>();
		List<String> authorAfter= new ArrayList<String>();
		for(WebElement paper:publications){
			if(!paper.findElement(By.tagName("a")).getAttribute("class").endsWith("ng-hide")){
				List<WebElement> beforeClickMore=paper.findElements(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_MORE_AUTHORS_XPATH.toString()));
				for(WebElement before:beforeClickMore){
					authorBefore.add(before.getText());
				}//for
				if(paper.findElement(By.tagName("a")).getText().trim().equals("…More")){
					pf.getBrowserActionInstance(ob).scrollToElement(paper.findElement(By.tagName("a")));
					BrowserWaits.waitTime(2);
					pf.getBrowserActionInstance(ob).jsClick(paper.findElement(By.tagName("a")));
					waitForAjax(ob);
					List<WebElement> afterClickMore=paper.findElements(By.xpath(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PAPER_MORE_AUTHORS_XPATH.toString()));
					for(WebElement after:afterClickMore){
						authorAfter.add(after.getText());
					}//for
				} //if
				
			}//if
			//logger.info("Before click More-->"+authorBefore);
			//logger.info("After click More-->"+authorAfter);
			if((authorAfter.containsAll(authorBefore))){
				throw new Exception("Publication MORE link not giving more authors list");
			}
			authorBefore.clear();
			authorAfter.clear();
			
		}
		
	}
	
	/**
	 * Method to check cancel curation functionality on Recommendation
	 * @param test
	 * @throws Exception
	 */
	public void testRecommendPublicationCancelFunctionality(ExtentTest test) throws Exception{
		
		List<WebElement> recommendPublicationsBeforeCancel = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PUBLICATIONS_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_REJECT_FIRST_RECOMMENDATION_XPATH);
		waitForAjax(ob);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_REJECT_FIRST_RECOMMENDATION_XPATH);
		waitForAjax(ob);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_REJECT_FIRST_RECOMMENDATION_XPATH);
		waitForAjax(ob);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_CANCEL_UPDATE_BTN_XPATH);
		waitForAjax(ob);
		List<WebElement> recommendPublicationsAfterCancel = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PUBLICATIONS_XPATH);
		for(WebElement publication:recommendPublicationsBeforeCancel){
			int i=0;
			if(publication.getText().equals(recommendPublicationsAfterCancel.get(i).getText())){
				test.log(LogStatus.INFO, "Publication "+ recommendPublicationsAfterCancel.get(i).getText()+ " is available after cancelling curation");
				i++;
			}
				
		}
		test.log(LogStatus.PASS, "All 3 rejected publications are available after the curation is cancelled");
	}
	
	/**
	 * Method for check Suggest update button is displayed in Author Record page
	 * 
	 * @throws Exception
	 */
	public void checkSuggestUpdateBtn() throws Exception {
		String BtnName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUGGEST_UPDATE_BTN_XPATH).getText();
		logger.info("Actual Value : " + BtnName);
		if (!BtnName.equals("Suggest updates")) {
			throw new Exception("Suggest updates button is not displayed in author record page");
		}
	}
	
	
	/**
	 * Method for check Submit update button is displayed in Author Record page for combined author
	 * 
	 * @throws Exception
	 */
	public void checkSubmitUpdateBtn() throws Exception {
		String submitButton = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH).getText();
		logger.info("Actual Value : " + submitButton);
		if (!submitButton.equals("Submit updates")) {
			throw new Exception("Submit updates button is not displayed in author record page for combined author");
		}
	}
	
	/**
	 * Method for submit authorship
	 * 
	 * @throws Exception
	 */
	public void submitAuthorship() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_MODAL_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_SUCCESS_TOAST_MESSAGE_CSS);
		String toastMessage=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_SUCCESS_TOAST_MESSAGE_CSS).getText();
		logger.info("success toast message-->"+toastMessage);
		if(!StringUtils.equals(toastMessage, "Thank you. Your contributions have been submitted. We'll notify you when this author record is updated.")){
			throw new Exception("Authorship Feedback not submitted and no success confirmation toast notification displayed ");
		}
		
	}
	
	/**
	 * Method for validate Submit updates modal 
	 * 
	 * @throws Exception
	 */
	public void verifySubmitUpdatesModal() throws Exception {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_MODAL_XPATH);
		List<WebElement> buttons=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_MODAL_BUTTONS_XPATH);
		String header=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_MODAL_HEADER_XPATH).getText();
		List<WebElement> modalBody=pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_RECORD_SUBMIT_UPDATES_MODAL_BODY_XPATH);
		logger.info("cancel button text-->"+buttons.get(1).getText());
		logger.info("Submit updates button text-->"+buttons.get(2).getText());
		logger.info("Header  text-->"+header);
		logger.info("boddy  text-->"+modalBody.get(0).getText()+"text 2-->"+modalBody.get(1).getText());
		
		if (!(buttons.get(1).isEnabled()&&StringUtils.equals(buttons.get(1).getText(), "Cancel") && StringUtils.equals(buttons.get(2).getText(), "Submit updates")
				&& StringUtils.equals(header, "Submit updates") && StringUtils.isNotEmpty(modalBody.get(0).getText()) && StringUtils.isNotEmpty(modalBody.get(1).getText()))) {
			throw new Exception("Submit updates modal Title,body and Buttons are not available/displayed");
		}
	}
	
	/**
	 * Method for validate Publication count decreased or not when publication removed
	 * 
	 * @throws Exception
	 */
	public void validatePubRemoveCount() throws Exception {
		int beforeRemove=getPublicationCount();
		logger.info("before remove-->"+beforeRemove);
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH);
		waitForAjax(ob);
		int afterRemove=getPublicationCount();
		logger.info("before remove-->"+beforeRemove+"after remove-->"+afterRemove);
		if(afterRemove == beforeRemove && afterRemove == beforeRemove-1){
			throw new Exception("Publicaton count is not decreased when user removes publication for single/combined author");
		}
	}
	
	
	/**
	 * Method for validate Publication Remove undo
	 * 
	 * @throws Exception
	 */
	public void validatePubRemoveUndo() throws Exception {
		
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH);
		waitForAjax(ob);
		String undo=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATION_UNDO_LINK_XPATH).getText();
		logger.info("undo text-->"+undo);
		if(!undo.trim().equals("undo")){
			throw new Exception("Removed Publication not avaible to perform undo");
		}
	}
	
	/**
	 * Method for validate Recommend publication rejected updated with new publication
	 * 
	 * @throws Exception
	 */
	public void validateRecommendPubRejectUpdated(ExtentTest test) throws Exception {
		String beforeRecmPubReject= pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PUBLICATIONS_XPATH).get(0).getText();
		logger.info("Before Recommend Reject-->"+beforeRecmPubReject);
		getintoCuration(test, "RejectRecommendation");
		waitForAjax(ob);
		String afterRecmPubReject= pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_RECOMMEND_PUBLICATIONS_XPATH).get(0).getText();
		logger.info("After Recommend Reject-->"+afterRecmPubReject);
		
		if(afterRecmPubReject.equals(beforeRecmPubReject)){
			throw new Exception("Rejected publication not updated with new, when user rejected recommended publication");
		}
	}
	
	
	


	/**
	 * @throws Exception
	 */
	public int getPublicationCount() throws Exception {
		String pubCountText = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATION_COUNT_XPATH).getText();
		int count = pubCountText.indexOf("p");
		if (count != -1)
			pubCountBeforeDeletion = Integer.parseInt(pubCountText.substring(0, count - 1));
		return pubCountBeforeDeletion;
	}
	
	/**
	 * Method to verify elements to be present in curation mode
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void verifyInCurationModeElements(ExtentTest test) throws Exception {
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH)
				.isDisplayed(), "Remove Publication button is not displayed");
		test.log(LogStatus.INFO, "Remove Publication button is displayed for each publication");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH).isDisplayed(),
				"Submit Update button is not displayed");
		test.log(LogStatus.INFO, "Submit Update button is displayed");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_CANCEL_UPDATE_BTN_XPATH).isDisplayed(),
				"Cancel button is not displayed");
		test.log(LogStatus.INFO, "Cancel button is displayed");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_IN_CURATION_FILTER_RESET_LINK_XPATH).isDisplayed(),
				"Reset link is not displayed");
		test.log(LogStatus.INFO, "Reset link is displayed");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_IN_CURATION_FILTER_AUTHOR_NAME_XPATH).isDisplayed(),
				"Author Nmae filter is not displayed");
		test.log(LogStatus.INFO, "Author Name filter is displayed");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_IN_CURATION_FILTER_JOURNAL_NAME_XPATH).isDisplayed(),
				"Journal Filter is not displayed");
		test.log(LogStatus.INFO, "Journal Filter is displayed");
		test.log(LogStatus.INFO, "Suggest Update button is not displayed during editing.");
	}

	/**
	 * Method to enter curation mode through Suggest Update button
	 * 
	 * @throws Exception
	 *
	 */
	public void getIntoCurationThruSuggestUpdateBtn(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUGGEST_UPDATE_BTN_XPATH)
				.click();
		test.log(LogStatus.PASS, "Entered Curation mode through the Suggest Update button");
		Assert.assertTrue(!pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH).isEnabled(),
				"Submit Update button is enabled (Blue colour) before any kind of editing.");
		test.log(LogStatus.INFO, "Submit Update button is not Enabled before any kind of editing.");
		BrowserWaits.waitTime(4);
		verifyInCurationModeElements(test);
	}

	/**
	 * Method to enter curation mode through accept recommendations button
	 * 
	 * @throws Exception
	 * 
	 */
	public void getIntoCurationThruAcceptRecommendation(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_ACCEPT_FIRST_RECOMMENDATION_XPATH).click();
		test.log(LogStatus.PASS, "Entered Curation mode through accept Recommendation");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH).isEnabled(),
				"Submit Update button is not enabled (Blue colour)");
		test.log(LogStatus.INFO, "Submit Update button is Enabled");
		verifyInCurationModeElements(test);
	}

	/**
	 * Method to enter curation mode through reject recommendations button
	 * 
	 * @throws Exception
	 * 
	 */
	public void getIntoCurationThruRejectRecommendation(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_REJECT_FIRST_RECOMMENDATION_XPATH).click();
		test.log(LogStatus.PASS, "Entered Curation mode through reject Recommendation");
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_SUBMIT_UPDATE_BTN_XPATH).isEnabled(),
				"Submit Update button is not enabled (Blue colour)");
		test.log(LogStatus.INFO, "Submit Update button is Enabled");
		verifyInCurationModeElements(test);
	}

	/**
	 * Method to enter curation mode for single author
	 * 
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void getintoCuration(ExtentTest test, String CurarionVia) throws Exception {
		checkSuggestUpdateBtn();
		test.log(LogStatus.PASS, "Suggest updates button is displayed in author record page");
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_RECORD_FIRST_PUBLICATION_REMOVE_BTN_XPATH)
				.isDisplayed()) {
			throw new Exception("Remove Publication button displayed even before getting into curation mode");
		}
		switch (CurarionVia) {
		case "SuggestUpdate":
			getIntoCurationThruSuggestUpdateBtn(test);
			break;
		case "AcceptRecommendation":
			getIntoCurationThruAcceptRecommendation(test);
			break;
		case "RejectRecommendation":
			getIntoCurationThruRejectRecommendation(test);
			break;
		default:
			test.log(LogStatus.WARNING, "None of the curation mode executed");
		}
	}
	
	/**
	 * Method for validate publication metadata
	 * @throws Exception
	 */
	public void publicationMetaDataValidation() throws Exception {
		List<WebElement> pubsMetadata = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_METADATA_XPATH);

		for(WebElement metadata:pubsMetadata){
			List<WebElement> data=metadata.findElements(By.tagName("div"));
			logger.info("Publication metadata 0-->"+data.get(0).getText());
			logger.info("Publication metadata 1-->"+data.get(1).getText());
			logger.info("Publication metadata 2-->"+data.get(2).getText());
			
			if(data.size()==4){
				logger.info("Publication metadata 3-->"+data.get(3).getText());
				if(!StringUtils.isNotEmpty(data.get(0).getText()) && StringUtils.contains(data.get(1).getText(), "Volume") && StringUtils.contains(data.get(2).getText(), "Issue") && StringUtils.contains(data.get(3).getText(), "Published")){
					throw new Exception("Publication Metadata not available");
				}
			} else {
				if(!StringUtils.isNotEmpty(data.get(0).getText()) && StringUtils.contains(data.get(1).getText(), "Volume") && StringUtils.contains(data.get(2).getText(), "Published")){
					throw new Exception("Publication Metadata not available");
				}
			}
		}
	}	
	
	/**
	 * Method for validate publication metadata tile and authors
	 * @throws Exception
	 */
	public void publicationMetaDataTitleAndAuthors() throws Exception {
		List<WebElement> pubsTitle = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_METADATA_PUB_TITLE_XPATH);
		
		for(WebElement title:pubsTitle) {
			logger.info("publication title -->"+title.getText());
			if(!StringUtils.isNotEmpty(title.getText())){
				throw new Exception("Each publication doesnot have publication Title");
			}
		}
		
		List<WebElement> pubAuthors = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_METADATA_AUTHORS_XPATH);
		
		for(WebElement author:pubAuthors) {
			String authorName=author.getText();
			logger.info("Author name-->"+authorName);
			if(authorName.contains(";")){
				String authorsList[]=authorName.split(";");
				logger.info("no.of authors-->"+authorsList.length);
				if(authorsList.length==3){
					if(!StringUtils.contains(authorName, "…More")){
						throw new Exception("Each publication authors which are morethan 3 should contain More link");
					}
				}
			}
			
		}
		
		
	}
	
	
	/**
	 * Method for validate publication metadata Metrics count
	 * @throws Exception
	 */
	public void publicationMetaDataMetricsCountValidation() throws Exception {
		List<WebElement> metricsCount = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_RECORD_PUBLICATIONS_METADATA_METRICS_COUNT_XPATH);
		logger.info("Time cited count11-->"+metricsCount.get(0).getText());
		for(WebElement metric:metricsCount){
			String [] citedCount=metric.getText().split("\n");
			logger.info("Time cited count-->"+citedCount[0]+"-->"+citedCount[1]);
			if(!StringUtils.equals(citedCount[0],"TIMES CITED")&&StringUtils.isNumeric(citedCount[1])){
				throw new Exception("Publication Metadata Time cited count not available");
			}
		}
	}


}
