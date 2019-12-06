package watpages;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
import util.ErrorUtil;
import util.OnePObjectMap;

/**
 * Class for Perform Browser Actions
 * 
 * @author UC225218
 *
 */
public class SearchAuthorClusterResultsPage extends TestBase {
	
	boolean ispublicationNamePresent=false;
	boolean isPublicationAuthorPresent=false;
	boolean isPublicationCategoryPresent=false;
	boolean isPublicationVolumePresent=false;
	boolean isPublicationIssuePresent=false;
	boolean isPublicationPublishedYearPresent=false;
	boolean isPublicationTimesCitedPresent=false;
	boolean isPublicationTimesCitedCountPresent=false;
	
	List<WebElement> pubDetailsList;
	List<WebElement> morePublications;
	List<WebElement> recentPublications;
	protected static final String REG_EXP = "^[-+]?\\d+(\\ - \\d+)?$";
	String publications[] = null;
	String years[] = null;
	String topJournals = null;
	List<WebElement> journals;
	int beforeScroll;
	int afterScroll;
	List<Integer> sortByRelevance;
	String subOrg;
	String searchTerms;
	String Checkbox_class_name = "pull-right wui-icon-only-btn wui-icon-only-btn--mini wat-results-selector wat-icon-only-btn--check-selected";
	String Author_card_after_class_name = "wui-card__content wat-card-content-selected";
	String Author_card_before_class_name = "wui-card__content";

	public SearchAuthorClusterResultsPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Wait for Author Cluster Search Results page
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void waitForauthorClusterSearchResults(ExtentTest test) throws Exception {
		pf.getBrowserActionInstance(ob).waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Search terms", "results", "Sorted by", "Relevance");
		pf.getBrowserWaitsInstance(ob)
				.waitForAllElementsToBePresent(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		test.log(LogStatus.INFO, "Author Cluster Search Results page is displayed");
	}

	/**
	 * Verify publication details if publication count is 1
	 * 
	 * @throws Exception
	 */
	public void validatePublicationCount1Details(String lastName, String countryName, String orgName, ExtentTest test)
			throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		String authorMetadata = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS)
				.get(pubDetailsList.size() - 1).getText();

		pf.getBrowserActionInstance(ob)
				.scrollingToElement(pf.getBrowserActionInstance(ob)
						.getElements(
								OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_TITLE_CSS)
						.get(pubDetailsList.size() - 1));

		pubDetailsList.get(pubDetailsList.size() - 1).findElement(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_AUTHORS_CSS.toString()))
				.getText();

		String pubTitle = pubDetailsList.get(pubDetailsList.size() - 1).findElement(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_TITLE_CSS.toString()))
				.getText();
		;
		String pubSource = pubDetailsList.get(pubDetailsList.size() - 1).findElement(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_SOURCE_CSS.toString()))
				.getText();
		String pubAuthors = pubDetailsList.get(pubDetailsList.size() - 1).findElement(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_AUTHORS_CSS.toString()))
				.getText();

		if (!(StringUtils.containsIgnoreCase(authorMetadata, lastName)
				&& StringUtils.containsIgnoreCase(authorMetadata, orgName)
				&& StringUtils.containsIgnoreCase(authorMetadata, countryName) && StringUtils.isNotEmpty(pubTitle)
				&& StringUtils.containsIgnoreCase(pubSource, "PUBLISHED") && StringUtils.isNotEmpty(pubAuthors))) {
			throw new Exception("Publication details are not displyaed for count 1");
		}

	}

	/**
	 * Method for match author search results count with publications count
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void matchSearchResultsCountWithPublicationsCount(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		String[] results = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_COUNT_CSS)
				.getText().split(" ");
		int resultsCount = Integer.parseInt(results[0]);
		if (!(pubDetailsList.size() == resultsCount)) {
			throw new Exception("Author Search Results count doesn't match Publications Count");
		}

	}

	/**
	 * Method for Default avatar/profile image available for each publication
	 * cart
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void defaultAvatarEachPublication(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		List<WebElement> defaultAvatars = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_DEFAULT_AVATAR_CSS);
		for (WebElement defaultAvatar : defaultAvatars) {
			if (!defaultAvatar.isDisplayed()) {
				throw new Exception("Default Avatar not displayed for Each publication");
			}

		}
	}

	/**
	 * Verify publication details if publication count is morethan 1
	 * 
	 * @throws Exception
	 */
	public void validateMorePublicationsCount(String lastName, String countryName, String orgName, ExtentTest test)
			throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		String authorMetadata = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS).get(0).getText();

		String primaryName = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_NAME_CSS)
				.getText();

		if (!(StringUtils.containsIgnoreCase(primaryName, lastName)
				&& StringUtils.containsIgnoreCase(authorMetadata, countryName)
				&& StringUtils.containsIgnoreCase(authorMetadata, orgName))) {
			test.log(LogStatus.FAIL, "Author Metadata Mismatching with Search input content");
			ErrorUtil
					.addVerificationFailure(new Exception("Author Metadata details are mismatching with Search input"));
		}

		morePublications = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_MORE_PUBLICATIONS_XPATH);
		if (morePublications.size() > 0) {
			publications = morePublications.get(0).getText().split("\n");
			years = morePublications.get(1).getText().split("\n");
			topJournals = morePublications.get(2).getText();
		}

		if (!(StringUtils.containsIgnoreCase(publications[0].trim(), "Publications")
				&& (publications[1].trim().matches(REG_EXP)))
				&& (years[0].trim().equalsIgnoreCase("Years") && (years[1].trim().matches(REG_EXP)))
				&& (StringUtils.isNotEmpty(topJournals) && topJournals.equalsIgnoreCase("Top Journals"))) {
			test.log(LogStatus.FAIL, "Publications doesn't have any count value, Years and Top Journals");
			throw new Exception("More Publications details fail");
		}

		recentPublications = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_PUBLICATIONS_CSS);
		pf.getBrowserActionInstance(ob).scrollingToElement(recentPublications.get(0));
		if (recentPublications.size() > 0) {
			if (!StringUtils.equalsIgnoreCase(recentPublications.get(0).getText().trim(), "Recent publications")) {
				test.log(LogStatus.FAIL, "Recent Publications link not present in Publication cart");
				throw new Exception("No Recent publicaion link present in cart");
			}

		}
	}

	/**
	 * Method for Click Recent Publications
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void clickRecentPublications(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		recentPublications = pubDetailsList.get(0).findElements(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_PUBLICATIONS_CSS.toString()));
		if (recentPublications.size() > 0) {
			test.log(LogStatus.INFO, "Recent Publications link available in Publication cart");
			pf.getBrowserActionInstance(ob).scrollingToElement(recentPublications.get(0));
			recentPublications.get(0).click();
			test.log(LogStatus.INFO, "Click Recent Publications link and it turns into Hide Publications");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_HIDE_PUBLICATIONS_CSS);
			List<WebElement> topPubs = pubDetailsList.get(0).findElements(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_TOP_PUBLICATIONS_CSS.toString()));

			logger.info("top publications-->" + topPubs.size());

			if (!(topPubs.size() <= 3 && StringUtils.isNotEmpty(topPubs.get(0).getText())
					&& StringUtils.isNotEmpty(topPubs.get(1).getText())
					&& StringUtils.isNotEmpty(topPubs.get(2).getText()))) {
				test.log(LogStatus.FAIL, "Top 3 Recent Publications displayed");
				throw new Exception("Top 3 publications are not displayed while click Recent Publications");
			}

		}
	}
	
	
	/**
	 * Method for validate Recent and Hide Publications
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void recentAndHidePublications(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		recentPublications = pubDetailsList.get(0).findElements(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_HIDE_PUBLICATIONS_CSS.toString()));
		if (recentPublications.size() > 0) {
			test.log(LogStatus.INFO, "Recent Publications link available in Publication cart");
			pf.getBrowserActionInstance(ob).scrollingToElement(recentPublications.get(0));
			pf.getBrowserActionInstance(ob).jsClick(recentPublications.get(0));
			BrowserWaits.waitTime(4);
			test.log(LogStatus.INFO, "Click Recent Publications link and it turns into Hide Publications");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_HIDE_PUBLICATIONS_CSS);
			List<WebElement> topPubs = pubDetailsList.get(0).findElements(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_TOP_PUBLICATIONS_CSS.toString()));
			
			logger.info("pub text-->"+recentPublications.get(1).getText());
			logger.info("top publications-->" + topPubs.size());
			
			if (!((topPubs.size() <= 3) && StringUtils.equals(recentPublications.get(1).getText(),"Hide publications")
					&& StringUtils.isNotEmpty(topPubs.get(0).getText())
					&& StringUtils.isNotEmpty(topPubs.get(1).getText())
					&& StringUtils.isNotEmpty(topPubs.get(2).getText()))) {
				test.log(LogStatus.FAIL, "Top 3 Recent Publications displayed");
				throw new Exception("Top 3 publications are not displayed while click Recent Publications");
			}
			pf.getBrowserActionInstance(ob).jsClick(recentPublications.get(0));
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_PUBLICATIONS_CSS);
			List<WebElement> topPubsAfterHide = pubDetailsList.get(0).findElements(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_TOP_PUBLICATIONS_CSS.toString()));
			logger.info("pub text-->"+recentPublications.get(0).getText());
			logger.info("Hide publications-->" + topPubsAfterHide.size());
			if (!(topPubsAfterHide.size() == 0 && StringUtils.equals(recentPublications.get(0).getText(),"Recent publications"))) {
				test.log(LogStatus.FAIL, "Recent Publications Not hidden after clicking on Hide Publications");
				throw new Exception("Recent Publications Not hidden after clicking on Hide Publications");
			}
		}
	}

	/**
	 * Method for Author Cluster Search Results page fields in each cart
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void eachAuthorClusterSearchResultsFields(String lastName, String countryName, String orgName,
			ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);

		for (WebElement eachCart : pubDetailsList) {
			pf.getBrowserActionInstance(ob).scrollingToElement(eachCart);
			String primaryName = eachCart.findElement(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_PRIMARY_NAME_CSS
							.toString()))
					.getText();
			String altName = eachCart.findElement(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_ALTERNATIVE_NAME_CSS
							.toString()))
					.getText();
			String affiliation = eachCart.findElement(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_AFFILIATION_CSS
							.toString()))
					.getText();
			String location = eachCart.findElement(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_LOCATION_CSS.toString()))
					.getText();

			logger.info("Details-->" + primaryName + "," + altName + "," + affiliation + "," + location);

			if (!(StringUtils.containsIgnoreCase(primaryName, lastName)
					&& StringUtils.containsIgnoreCase(altName, lastName)
					&& StringUtils.containsIgnoreCase(affiliation, orgName)
					&& StringUtils.containsIgnoreCase(location, countryName))) {
				test.log(LogStatus.FAIL, "Author Metadata Mismatching with Search input content");
				throw new Exception("Author Metadata Mismatching with Search input content");
			}

		}

		recentPublications = pubDetailsList.get(0).findElements(By.cssSelector(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_PUBLICATIONS_CSS.toString()));
		if (recentPublications.size() > 0) {
			for (WebElement recentPub : pubDetailsList) {
				recentPub.findElement(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_RECENT_PUBLICATIONS_CSS
								.toString()));
				logger.info("Rec pub link name-->" + recentPub.getText());
				if (!(StringUtils.containsIgnoreCase(recentPub.getText(), "Recent publications")
						&& StringUtils.containsIgnoreCase(recentPub.getText(), "Publications")
						&& StringUtils.containsIgnoreCase(recentPub.getText(), "Years")
						&& StringUtils.containsIgnoreCase(recentPub.getText(), "Top Journals"))) {
					test.log(LogStatus.FAIL,
							"Recent publications link, Publications,Years and Top Journals not displayed if author have morethan 1 publications");
					throw new Exception(
							"Recent publications link, Publications,Years and Top Journals not displayed if author have morethan 1 publications");
				}
			}

		} else {
			for (WebElement eachCart : pubDetailsList) {
				String pubTitle = eachCart.findElement(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_TITLE_CSS
								.toString()))
						.getText();
				;
				String pubSource = eachCart.findElement(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_SOURCE_CSS
								.toString()))
						.getText();
				String pubAuthors = eachCart.findElement(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_PUBLICATION_AUTHORS_CSS
								.toString()))
						.getText();

				if (!(StringUtils.isNotEmpty(pubTitle) && StringUtils.containsIgnoreCase(pubSource, "PUBLISHED")
						&& StringUtils.isNotEmpty(pubAuthors))) {
					test.log(LogStatus.FAIL, "Publication Details are not displayed for count 1");
					throw new Exception("Publication details are not displyaed for count 1");
				}

			}
		}
	}

	/**
	 * Method for Author Cluster Search Results Top Journals should contain max
	 * 3
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchResultsTopJournals(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_PUB_YEARS_JOURNALS_CSS);
		logger.info("total pub_years_journals-->" + pubDetailsList.size());
		if (pubDetailsList.size() > 0) {
			for (WebElement topJournals : pubDetailsList) {
				pf.getBrowserActionInstance(ob).scrollingToElement(topJournals);
				journals = topJournals.findElements(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_TOP_JOURNALS_CSS
								.toString()));
				logger.info("Top journals size-->" + journals.size());
				if (!(journals.size() <= 3)) {
					test.log(LogStatus.FAIL, "Top Journals section should contain max of 3 journal titles");
					throw new Exception("Top Journals section should contain max of 3 journal titles");
				}
			}
		}
	}

	/**
	 * Method for Scroll down for author search results
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchResultsScrollDown(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
		beforeScroll = pubDetailsList.size();
		logger.info("beforeScroll-->" + beforeScroll);
		if (beforeScroll > 10) {
			pf.getBrowserActionInstance(ob).scrollingToElement(pubDetailsList.get(pubDetailsList.size() - 1));
			pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
			pubDetailsList = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);
			afterScroll = pubDetailsList.size();
			logger.info("afterScroll-->" + afterScroll);
			if (beforeScroll == afterScroll) {
				test.log(LogStatus.FAIL, "Search Results count not increase while page scroll down");
				throw new Exception("Search Results count not increase while page scroll down");
			}
		}
	}
	
	/**
	 * Method for Verify SortedBy Dropdown operations 
	 * @param test
	 * @throws Exception
	 */
	public void sortedByDropddown(String dropDownName,ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_RELAVANCE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_DROPDOWN_MENU_CSS);
		List<WebElement> ddValues = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_DROPDOWN_MENU_CSS)
				.findElements(By.tagName("li"));
		for(WebElement value:ddValues){
			logger.info("Dropdown name-->"+value.getText());
			if(value.getText().equals(dropDownName)){
				value.click();
				BrowserWaits.waitTime(3);
				break;
			}
		}
		
		String ddHeader = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_RELAVANCE_CSS).getText();
		logger.info("Dropdown Header Name-->"+ddHeader);
		if(!(ddHeader.contains(dropDownName) || ddHeader.contains("Relevance"))){
			test.log(LogStatus.FAIL, "Sorted by Dropdown not updated with selected dropdown option value");
			throw new Exception("Sorted by Dropdown not updated with selected dropdown option value");
		}

	}


	/**
	 * Method for Verify sorted by relevance count
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void sortByRelevance(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_PUB_YEARS_JOURNALS_PUB_COUNT_CSS);

		sortByRelevance = new ArrayList<Integer>();
		for (WebElement pubCount : pubDetailsList) {
			sortByRelevance.add(Integer.parseInt(pubCount.getText()));
		}
		logger.info("Sort by Relevance data-->" + sortByRelevance);
		int val1 = sortByRelevance.get(0);
		int val2 = sortByRelevance.get(1);
		for (int i = 2; i < sortByRelevance.size(); i++) {
			if (!(val1 >= val2)) {
				test.log(LogStatus.FAIL, "Publicaiton count is not in Sort order");
				throw new Exception("Publicaiton count is not in Sort order");
			}
			val1 = val2;
			val2 = sortByRelevance.get(i);
		}
	}
	
	
	/**
	 * Method for Verify sorted by Publication Years Newest First
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void sortedByPublicationYearsNewestFirst(ExtentTest test) throws Exception {
		List<WebElement> searchResults = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_PUBLICATION_YEARS_NEWEST_FIRST_CSS);
		logger.info("Before scrolling search results count-->" + searchResults.size());
		pf.getBrowserActionInstance(ob).scrollingToElement(searchResults.get(searchResults.size() - 1));
		waitForAjax(ob);
		searchResults = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_PUBLICATION_YEARS_NEWEST_FIRST_CSS);

		logger.info("After scrolling search results count-->" + searchResults.size());
		int nextYear = 0;
		int year1 = getYear(searchResults.get(0).getText());
		int year2 = getYear(searchResults.get(1).getText());
		logger.info("Year 1-->" + year1);
		logger.info("Year 2-->" + year2);

		for (int i = 2; i < searchResults.size(); i++) {
			if (!(year1 >= year2)) {
				test.log(LogStatus.FAIL, "Publicaiton Years Newest First not displayed");
				throw new Exception("Publicaiton Years Newest First not displayed");
			 }//if
			nextYear = getYear(searchResults.get(i).getText());
			year1 = year2;
			year2 = nextYear;
			logger.info("nextYear-->" + nextYear);
		} //for
	}
	
	
	/**
	 * Method for Verify sorted by Publication Years Newest First
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void sortedByPublicationYearsOldestFirst(ExtentTest test) throws Exception {
		List<WebElement> searchResults = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_PUBLICATION_YEARS_NEWEST_FIRST_CSS);
		logger.info("Before scrolling search results count-->" + searchResults.size());
		pf.getBrowserActionInstance(ob).scrollingToElement(searchResults.get(searchResults.size() - 1));
		waitForAjax(ob);
		searchResults = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_PUBLICATION_YEARS_NEWEST_FIRST_CSS);

		logger.info("After scrolling search results count-->" + searchResults.size());
		int nextYear = 0;
		int year1 = getYear(searchResults.get(0).getText());
		int year2 = getYear(searchResults.get(1).getText());
		logger.info("Year 1-->" + year1);
		logger.info("Year 2-->" + year2);

		for (int i = 2; i < searchResults.size(); i++) {
			if (!(year1 <= year2)) {
				test.log(LogStatus.FAIL, "Publicaiton Years Oldest First not displayed");
				throw new Exception("Publicaiton Years Oldest First not displayed");
			 }//if
			nextYear = getYear(searchResults.get(i).getText());
			year1 = year2;
			year2 = nextYear;
			logger.info("nextYear-->" + nextYear);
		} //for
	}
	
	public int getYear(String yearValue){
		int year=0;
		if(yearValue.contains("-")){
			String years[]=yearValue.split("\\-");
			 year=Integer.parseInt(years[1].trim());
		} else {
			year=Integer.parseInt(yearValue.trim());
		}
		return year;
	}
	
	/**
	 * Method for Verify sorted by Publication Years Newest First
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void sortedByDropdownOptions(String ddOptions,ExtentTest test) throws Exception {
		String dropdownOptions[]=ddOptions.split("\\|");
		waitForauthorClusterSearchResults(test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_RELAVANCE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_DROPDOWN_MENU_CSS);
		List<WebElement> ddValues = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SORTEDBY_DROPDOWN_MENU_CSS)
				.findElements(By.tagName("li"));
		for(int i=0;i<ddValues.size();i++){
			logger.info("Dropdown option-->"+ddValues.get(i).getText());
			if(!ddValues.get(i).getText().equals(dropdownOptions[i])){
				throw new Exception("Sorted By dropdown having unsupported options ");
			}
		}
	}
	
	/**
	 * Method for Verify Sub-org field displayed in search results page
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchResultsSubOrgField(ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		WebElement dept = pf.getBrowserActionInstance(ob).getElement(
				OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_AFFILIATION_SUB_ORG_CSS);

		if (!(StringUtils.containsIgnoreCase(dept.getAttribute("class"), "wat-search-results-meta-contact")
				&& StringUtils.isNotEmpty(dept.getText()))) {
			test.log(LogStatus.FAIL, "Department(sub-org) is not displayed under Org name");
			throw new Exception("Department(sub-org) is not displayed under Org name");
		}
	}

	/**
	 * Method for Verify Search Terms are match with Search input data
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchTermsMatchSearchInputData(ExtentTest test, final String... input) throws Exception {
		waitForauthorClusterSearchResults(test);
		for (String each : input) {
			searchTerms(each, test);
		}
	}

	/**
	 * Method for Verify Search Terms are match with Search input data
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchTerms(String searchInput, ExtentTest test) throws Exception {
		searchTerms = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_SEARCH_TERMS_CSS).getText();
		logger.info("SearchTerms-->" + searchTerms);
		if (!StringUtils.containsIgnoreCase(searchTerms, searchInput)) {
			test.log(LogStatus.FAIL, "Search Input doesn't match with Search Terms");
			throw new Exception("Search Input doesn't match with Search Terms");
		}
	}

	/**
	 * Method for Verify Search Tab is highlighted when navigate back to Search
	 * Results page from Author Record page
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void searchTabHighlight(ExtentTest test) throws Exception {
		String tabHighlight = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TAB_HIGHLIGHT_XPATH).getAttribute("class");
		logger.info("tabHighlight-->" + tabHighlight);
		if (!tabHighlight.contains("active")) {
			test.log(LogStatus.FAIL, "Search Results tab not getting Highlighted");
			throw new Exception("Search Results tab not getting Highlighted");
		}
	}

	/**
	 * Method to combine author cards in search author result page.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void combineAuthorCard(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_CARD1_XPATH,
				5);
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_CARD1_XPATH);
			test.log(LogStatus.INFO, "Author card 1 is selected.");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_CARD2_XPATH);
			test.log(LogStatus.INFO, "Author card 2 is selected.");
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_COMBINE_BUTTON_XPATH, 5);
			if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_COMBINE_BUTTON_XPATH).isEnabled()) {
				test.log(LogStatus.INFO, "Author Combine button  is enabled");
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_COMBINE_BUTTON_XPATH);
				test.log(LogStatus.INFO, "Author Combine button is clicked.");
			} else {
				throw new Exception("Combine button not enabled");
			}
			waitForAjax(ob);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_AUTHOR_PROFILE_ICON_XPATH, 5);
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_RECORD_PAGE_AUTHOR_PROFILE_ICON_XPATH).isDisplayed());
			test.log(LogStatus.PASS,
					"2 author records combined and user landed in Combined author record page successfully");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author using lastname");
			throw new Exception(e);
		}
	}

	/**
	 * Method to click on Select all link in search author result page.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void selectAllAuthorCard(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SELECT_ALL_LINK_XPATH, 5);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SELECT_ALL_LINK_XPATH).isDisplayed())
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SELECT_ALL_LINK_XPATH);
		test.log(LogStatus.INFO, "Select all link clicked.");
	}

	/**
	 * Method to click on DeSelect all link in search author result page.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void DeselectAllAuthorCard(ExtentTest test) throws Exception {
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_DESELECT_ALL_LINK_XPATH).isDisplayed())
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_DESELECT_ALL_LINK_XPATH);
		test.log(LogStatus.INFO, "DeSelect all link clicked.");
	}

	/**
	 * Method to verify card selection in search author result page after
	 * clicking select all link.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void verifyCardSelection(ExtentTest test) throws Exception {
		List<WebElement> List = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_CARD_CHECKBOX_AFTER_SELECT_ALL_XPATH);
		ListIterator<WebElement> litr = List.listIterator();
		while (litr.hasNext()) {
			Assert.assertEquals(litr.next().getAttribute("class"), Checkbox_class_name);
		}
		test.log(LogStatus.INFO, "Author card checkboxes are being clicked Successfully.");
	}

	/**
	 * Method to verify card highlight after selection in search author result
	 * page after clicking select all link.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void verifyCardHighlight(ExtentTest test) throws Exception {
		List<WebElement> List = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_CARD_AFTER_SELECTION_XPATH);
		ListIterator<WebElement> litr = List.listIterator();
		while (litr.hasNext()) {
			Assert.assertEquals(litr.next().getAttribute("class"), Author_card_after_class_name);
		}
		test.log(LogStatus.INFO, "All Author cards are Selected/Highlighted.");
	}

	/**
	 * Method to verify card highlight after deselection in search author result
	 * page after clicking select all link.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void verifyCardHighlightafterDeselection(ExtentTest test) throws Exception {
		List<WebElement> List = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_CARD_BEFORE_SELECTION_XPATH);
		ListIterator<WebElement> litr = List.listIterator();
		while (litr.hasNext()) {
			Assert.assertEquals(litr.next().getAttribute("class"), Author_card_before_class_name);
		}
		test.log(LogStatus.INFO, "All Author cards are DeSelected.");
	}

	/**
	 * Method to click on author card in search author result page.
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void clickAuthorCard(ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_CARD_XPATH,
				5);
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_CARD_XPATH);
			test.log(LogStatus.INFO, "Author card 1 is selected.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to click first card");
			throw new Exception(e);
		}
	}
	
	/**
	 *  Method to Verify that the user should be able to further refine the search result based on Subject Category
	 * @param LastName
	 * @param CountryName1
	 * @param CountryName2
	 * @param OrgName1
	 * @param OrgName2
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void verifySubCatFilterFunctionality(ExtentTest test) throws Exception, NumberFormatException, InterruptedException {
		int result_Count_old;
		int result_Count_new;
		
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_SUBCAT_XPATH).isDisplayed(), "Subject categories filter name not displayed in Author search results page");
		test.log(LogStatus.PASS, "Subject categories filter name displayed in Author search results page");
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_SUBCAT_XPATH).isDisplayed(), "Subject categories filter not displayed in Author search results page");
		test.log(LogStatus.PASS, "Subject categories filter displayed in Author search results page");
		result_Count_old = getResultCount();
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_SUBCAT_XPATH);	
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		result_Count_new = getResultCount();
		
		Assert.assertTrue(result_Count_old>result_Count_new, "User is not able to further refine the search result based on Subject Category");
		test.log(LogStatus.INFO, "New result count is less than old result count");			
		test.log(LogStatus.PASS, "User is able to further refine the search result based on Subject Category");

		pf.getBrowserActionInstance(ob).closeBrowser();
	}
	
	/**
	 *  Method to Verify that the user should be able to further refine the search result based on Organization
	 * @param LastName
	 * @param CountryName1
	 * @param CountryName2
	 * @param OrgName1
	 * @param OrgName2
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void verifyOrgFilterFunctionality(ExtentTest test) throws Exception, NumberFormatException, InterruptedException {
		int result_Count_old;
		int result_Count_new;
		
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_ORG_XPATH).isDisplayed(), "Organization filter name not displayed in Author search results page");
		test.log(LogStatus.PASS, "Organization filter name displayed in Author search results page");
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_ORG_XPATH).isDisplayed(), "Organization filter not displayed in Author search results page");
		test.log(LogStatus.PASS, "Organization filter displayed in Author search results page");
		result_Count_old = getResultCount();
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_ORG_XPATH);	
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		result_Count_new = getResultCount();
		
		Assert.assertTrue(result_Count_old>result_Count_new, "User is not able to further refine the search result based on Organization");
		test.log(LogStatus.INFO, "New result count is less than old result count");			
		test.log(LogStatus.PASS, "User is able to further refine the search result based on Organization");

		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	/**
	 *  Method to Verify that the user should be able to further refine the search result based on Author name
	 * @param LastName
	 * @param CountryName1
	 * @param CountryName2
	 * @param OrgName1
	 * @param OrgName2
	 * @throws Exception
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void verifyNameFilterFunctionality(ExtentTest test) throws Exception, NumberFormatException, InterruptedException {
		int result_Count_old;
		int result_Count_new;
		
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_NAME_XPATH).isDisplayed(), "Author name filter not displayed in Author search results page");
		test.log(LogStatus.PASS, "Author name filter displayed in Author search results page");
		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_OPTIONS_NAME_XPATH).isDisplayed(), "Author name filter not displayed in Author search results page");
		test.log(LogStatus.PASS, "Author name filter displayed in Author search results page");
		result_Count_old = getResultCount();
		
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_RESULTS_FILTER_1st_NAME_XPATH);	
		pf.getBrowserWaitsInstance(ob).waitTime(3);
		result_Count_new = getResultCount();
		
		Assert.assertTrue(result_Count_old>result_Count_new, "User is not able to further refine the search result based on Author name");
		test.log(LogStatus.INFO, "New result count is less than old result count");			
		test.log(LogStatus.PASS, "User is able to further refine the search result based on Author name");

		pf.getBrowserActionInstance(ob).closeBrowser();
	}
	
	/**
	 * @return
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public int getResultCount() throws Exception, NumberFormatException {
		int result_Count_old;
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_COUNT_XPATH);
		String[] Search_result_Count_old = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_COUNT_XPATH).getText().split(" ");
		if(Search_result_Count_old[0].contains("Approx"))
				result_Count_old = Integer.parseInt(Search_result_Count_old[1]);
		else
			result_Count_old = Integer.parseInt(Search_result_Count_old[0]);
		return result_Count_old;
	}
	
	/**
	 * Method for Select All option present or not in Search Results Page
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void verifySelectAll(ExtentTest test) throws Exception {
		List<WebElement> selAll=
				pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_SELECTALL_CSS);
		logger.info("size is -->"+selAll.size());
		if(selAll.size()==1){
			test.log(LogStatus.INFO, "Select All option should not display when quantity of search results of an author morethan 50. and Search Results count is only one");
		} else {
			logger.info("Select-->"+selAll.get(0).getText());
			if(selAll.get(0).getText().equals("Select all")){
				test.log(LogStatus.INFO, "Select All option shoulddisplay when quantity of search results of an author lessthan 50.");
			}
		}
			
	}
	
	/**
	 * Method for validate Dept and Organization in Each author cluster search result
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void deptAndOrgInSearchResults(ExtentTest test) throws Exception {
		List<WebElement> depts=
				pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_DEPT_CSS);
		
		List<WebElement> orgs=
				pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_ORG_CSS);
		
		for(int i=0;i<depts.size();i++){
			logger.info("dept and org-->"+depts.get(i).getText()+"-->"+orgs.get(i).getText());
			if (!(StringUtils.isEmpty(depts.get(i).getText()) || StringUtils.isNotEmpty(depts.get(i).getText()))
					&& (StringUtils.isEmpty(orgs.get(i).getText()) || StringUtils.isNotEmpty(orgs.get(i).getText()))) {
				throw new Exception("department name (sub-organization) Not displayed in addition to the institution/org name");
			}
			
		}
		
	}

	/**
	 * Method for publication Name
	 * @param test
	 * @throws Exception
	 */
	public void publicationName(ExtentTest test) throws Exception{
		ispublicationNamePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_NAME_XPATH).isDisplayed();
		if(!ispublicationNamePresent){
			throw new Exception("No publication Name is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "publication Name is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Author 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationAuthor(ExtentTest test) throws Exception{
		isPublicationAuthorPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_AUTHORS_XPATH).isDisplayed();
		if(!isPublicationAuthorPresent){
			throw new Exception("No Publication Author is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Author is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Category 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationCategory(ExtentTest test) throws Exception{
		isPublicationCategoryPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_CATEGORY_XPATH).isDisplayed();
		if(!isPublicationCategoryPresent){
			throw new Exception("No Publication Category is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Category is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Volume 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationVolume(ExtentTest test) throws Exception{
		isPublicationVolumePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_VOLUME_XPATH).isDisplayed();
		if(!isPublicationVolumePresent){
			throw new Exception("No Publication Volume is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Volume is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Issue 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationIssue(ExtentTest test) throws Exception{
		isPublicationIssuePresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_ISSUE_XPATH).isDisplayed();
		if(!isPublicationIssuePresent){
			throw new Exception("No Publication Issue is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Issue is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Published Year 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationPublishedYear(ExtentTest test) throws Exception{
		isPublicationPublishedYearPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_PUBLISHED_XPATH).isDisplayed();
		if(!isPublicationPublishedYearPresent){
			throw new Exception("No Publication Published Year is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Published Year is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Times Cited 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationTimesCited(ExtentTest test) throws Exception{
		isPublicationTimesCitedPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_TIMES_CITED_TEXT_XPATH).isDisplayed();
		if(!isPublicationTimesCitedPresent){
			throw new Exception("No Publication Times Cited is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Times Cited is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Times Cited Count 
	 * @param test
	 * @throws Exception
	 */
	public void PublicationTimesCitedCount(ExtentTest test) throws Exception{
		isPublicationTimesCitedCountPresent=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_TIMES_CITED_COUNT_XPATH).isDisplayed();
		if(!isPublicationTimesCitedCountPresent){
			throw new Exception("No Publication Times Cited Count is displayed in Author Record page");
		}
		test.log(LogStatus.PASS, "Publication Times Cited Count is displayed in Author Record page");
    }
	
	/**
	 * Method for Publication Author Count test
	 * @param test
	 * @throws Exception
	 */
	public void PublicationAuthorCount(ExtentTest test) throws Exception{
		String[] PublicationAuthorCount = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_AUTHORS_XPATH).getText().split(";");
		test.log(LogStatus.INFO, "Author count = "+ PublicationAuthorCount.length);
		if (PublicationAuthorCount.length >= 3)
		{
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_MORE_LINK_XPATH).isDisplayed(), 
					"More link not displayed even though the count of authors for this publication is more than 3");
			test.log(LogStatus.INFO, "More link displayed");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_MORE_LINK_XPATH);
			test.log(LogStatus.INFO, "More Link clicked");
			String[] PublicationAuthorCountAfterMoreLinkClick = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_AUTHORS_XPATH).getText().split(";");
			test.log(LogStatus.INFO, "Author count after clicking More link = "+ PublicationAuthorCountAfterMoreLinkClick.length);
			if (PublicationAuthorCountAfterMoreLinkClick.length > 3)
				pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_LESS_LINK_XPATH);
			Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULT_FIRST_PUBLICATION_MORE_LINK_XPATH).isDisplayed(), 
					"More link not displayed after LESS link is clicked");
		}
		else
			test.log(LogStatus.INFO, "Author count is less than 3");
		}
	
	/**
	 * Select Author name and navigate to selected author record page
	 * @param authorName
	 * @throws Exception
	 */
	public void selectAuthorFromSearchResults(String authorName,ExtentTest test) throws Exception {
		waitForauthorClusterSearchResults(test);
		pubDetailsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_CSS);

		for (WebElement eachCart : pubDetailsList) {
			pf.getBrowserActionInstance(ob).scrollingToElement(eachCart);
			String primaryName = eachCart.findElement(By.cssSelector(
					OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_PRIMARY_NAME_CSS
							.toString()))
					.getText();
			logger.info("Primary author name-->"+primaryName);
			if(primaryName.equals(authorName)){
				WebElement ele=eachCart.findElement(By.cssSelector(
						OnePObjectMap.WAT_AUTHOR_SEARCH_RESULTS_PAGE_PUBLICATIONS_DETAILS_AUTHOR_PRIMARY_NAME_CSS.toString()));
				pf.getBrowserActionInstance(ob).jsClick(ele);		
				pf.getAuthorRecordPage(ob).waitForAuthorRecordPage(test);
				break;
			}
		}
	}
	
}
