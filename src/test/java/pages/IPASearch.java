package pages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserWaits;
import util.OnePObjectMap;

public class IPASearch extends TestBase {

	PageFactory pf;

	public IPASearch(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Following block verifying the Following items - User must have a text
	 * search box unique to Technology - User must be able to search for one or
	 * more technology terms - User must view a instructional text
	 * 
	 * @author uc209280
	 *
	 */
	public void validateTechnologySearch(ExtentTest test) throws Exception {
		//ob.navigate().refresh();
		try{
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
		}catch(Exception e ){
		test.log(LogStatus.FAIL, "Explore Page Not Loaded once logged in successfully");
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_NEW_SEARCH_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS);	
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
		}
		
		WebElement searchTextbox = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS.toString()));
		String searchWatermark = searchTextbox.getAttribute("placeholder");
System.out.println();
		if (searchTextbox.isDisplayed() && searchTextbox.isEnabled()) {
			test.log(LogStatus.PASS, "Search text box found in technology page");
		} else {
			test.log(LogStatus.FAIL, "Search text box not found in technology page");
			throw new Exception("Search text box not found in technology page");

		}

		if (searchWatermark.equals("Enter a technology you're interested in (eg. laser eye surgery)")) {
			test.log(LogStatus.PASS, "Water mark is found in the search text box");

		} else {
			test.log(LogStatus.FAIL, "Search text box not found in technology page");
			throw new Exception("Search text box not found in technology page");
		}

		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS, "carbon");
		
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECH_SEARCHSUGGESTIONS_XPATH);
		List<WebElement> searchTerms = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_TECH_SEARCHSUGGESTIONS_XPATH);

		for (WebElement searchTerm : searchTerms) {
			searchTerm.click();
			BrowserWaits.waitTime(2);
		}

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECH_CLIPBOARD_CSS);

		List<WebElement> addedTerms = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_TECH_CLIPBOARD_CSS);

		if (searchTerms.size() == addedTerms.size()) {
			test.log(LogStatus.PASS, "Search terms added to clipboard successfully");
		} else {
			test.log(LogStatus.FAIL, "Incorrect number of Search terms are added to clipboard. " + searchTerms.size()
					+ " terms should be added but added " + addedTerms.size());
			logFailureDetails(test, "Incorrect number of Search terms are added to clipboard",
					"Incorrect number of Search Terms");
		}

	}

	public void validateCompanySearch(ExtentTest test) throws Exception {
		//ob.navigate().refresh();
		IPALandingPage();

		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS.toString()), 30);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_COMPANYSEARCH_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
		WebElement searchTextbox = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS.toString()));
		String searchWatermark = searchTextbox.getAttribute("placeholder");

		if (searchTextbox.isDisplayed() && searchTextbox.isEnabled()) {
			test.log(LogStatus.PASS, "NEON-574 - Search text box found for company search");
		} else {
			test.log(LogStatus.FAIL, "NEON-574 - Search text box not found for company search");
			throw new Exception("NEON-574 - Search text box not found for company search");

		}

		if (searchWatermark.equals(OnePObjectMap.NEON_IPA_COMPANYWATERMARK_CSS.toString())) {
			test.log(LogStatus.PASS, "NEON-574 - Water mark is found in the search text box --> "
					+ OnePObjectMap.NEON_IPA_COMPANYWATERMARK_CSS.toString());

		} else {
			test.log(LogStatus.FAIL, "NEON-574 - Search text box not found in technology page");
			throw new Exception("NEON-574 - Search text box not found in technology page");
		}

		List<String> termsAdded = new ArrayList<String>();

		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS, "carbon");
		termsAdded = addCompanyTerms("1&&2:2");

		List<WebElement> clipboardTerms = ob
				.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_COMPANYCLIPBOARD_CSS.toString()));

		if (clipboardTerms.size() == termsAdded.size()) {
			for (int i = 0; i < clipboardTerms.size(); i++) {
				if (termsAdded.get(i).equals(clipboardTerms.get(i).getText())) {
					test.log(LogStatus.PASS, "NEON-574 - One or more terms added successfully to clipboard");
				} else {
					test.log(LogStatus.FAIL, "NEON-574 - Terms are not matching");
					System.out.println();
					throw new Exception("NEON-574 - Terms are not matching");
				}
			}

		} else {
			test.log(LogStatus.FAIL, "NEON-574 - Wrong number of terms added to clipboard. Expected "
					+ termsAdded.size() + " Actual " + clipboardTerms.size());
			throw new Exception("NEON-574 - Wrong number of terms added to clipboard");
		}

	}

	public void validateNewSearchLandingPage(ExtentTest test) throws Exception {

		// Initializing the test
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECH_NEWSEARCH_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_TECH_NEWSEARCH_XPATH);

		performTechnologySearch();
		BrowserWaits.waitTime(2);
		returntoNewSearchLandingPage();

		WebElement selection = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_TECHHIGHLIGHT_XPATH);

		if (!(selection == null)) {
			test.log(LogStatus.PASS, "NEON-400 - Technology Search is highlighted");
		} else {
			test.log(LogStatus.FAIL, "NEON-400 - Technology Search is not highlighted");
			throw new Exception("NEON-400 - Technology Search is not highlighted");
		}

		performCompanySearch();
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECHPORTFOLIO_CSS);
		returntoNewSearchLandingPage();
		selection = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_COMPANYHIGHLIGHT_XPATH);

		if (!(selection == null)) {
			test.log(LogStatus.PASS, "NEON-400 - Company Search is highlighted");
		} else {
			test.log(LogStatus.FAIL, "NEON-400 - Company Search is not highlighted");
			throw new Exception("NEON-400 - sCompany Search is not highlighted");
		}
	}

	public void validateIPAnalyticsLandingPage(ExtentTest test) throws Exception {

		performTechnologySearch();
		BrowserWaits.waitTime(2);
		IPALandingPage();

		WebElement selection = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_TECHHIGHLIGHT_XPATH);

		if (!(selection == null)) {
			test.log(LogStatus.PASS, "NEON-438 - Technology Search is highlighted");
		} else {
			test.log(LogStatus.FAIL, "NEON-438 - Technology Search is not highlighted");
			throw new Exception("NEON-438 - Technology Search is not highlighted");
		}

		performCompanySearch();
		//BrowserWaits.waitTime(5);
		//pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_TECHPORTFOLIO_CSS);
		IPALandingPage();
		// BrowserWaits.waitTime(4);

		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob, By.xpath(OnePObjectMap.NEON_IPA_TECHHIGHLIGHT_XPATH.toString()), 30);
		selection = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_TECHHIGHLIGHT_XPATH);

		if (!(selection == null)) {
			test.log(LogStatus.PASS, "NEON-438 - Technology Search is highlighted");
		} else {
			test.log(LogStatus.FAIL, "NEON-438 - Technology Search is not highlighted");
			throw new Exception("NEON-438 - Technology Search is not highlighted");
		}
	}

	public void performTechnologySearch() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS);
		pf.getDashboardPage(ob).SearchTermEnter("technology", "Java");
		pf.getDashboardPage(ob).exploreSearch();
		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}
	}

	public void performCompanySearch() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS);
		pf.getDashboardPage(ob).SearchTermEnter("company", "Thomson");
		pf.getDashboardPage(ob).addCompanyTerms("1");
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_EXPLORE_BUTTON_CSS);
		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}

	}

	public void returntoNewSearchLandingPage() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.NEON_IPA_TECH_NEWSEARCH_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_TECH_NEWSEARCH_XPATH);

	}

	public void IPALandingPage() throws Exception {

		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_HEADER_CSS.toString()), 30);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_HEADER_CSS);

	}

	public List<String> addCompanyTerms(String hierarchy) throws Exception {

		String Parent = "li[index='$Parent']";// >ul>li:nth-of-type(2)>div
		String Child = ">ul>li:nth-of-type($Child)";
		String[] Company = hierarchy.split("&&");
		List<String> termsAdded = new ArrayList<String>();
		for (int x = 0; x < Company.length; x++) {
			String[] Index = Company[x].split(":");
			String Xpath = "";
			String Xpath1 = "";
			if (Index.length > 0) {
				Xpath = Parent.replace("$Parent", "" + (Integer.valueOf(Index[0]) - 1));
				for (int i = 1; i < Index.length; i++) {
					Xpath = Xpath + Child.replace("$Child", Index[i]);
				}
				Xpath1 = Xpath + ">div";
				Xpath = Xpath + ">div>button";
			}
			pf.getBrowserActionInstance(ob).jsClick(ob.findElement(By.cssSelector(Xpath)));
			String termAdded = ob.findElement(By.cssSelector(Xpath1)).getText();
			termsAdded.add(termAdded.replaceAll("\\([,\\d]+\\)", "").trim());
			
		}
		return termsAdded;
	}

	public void SearchTermEnter(String searchType, String searchTerm) throws Exception {
		boolean switched = false;
		if (searchType.equalsIgnoreCase("company")) {
			if (!ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS.toString() + ">hr"))
					.getAttribute("class").contains("__active")) {
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS);
				switched = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS.toString() + ">hr"))
						.getAttribute("class").contains("__active");
			} else
				switched = true;
		} else {
			if (!ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS.toString() + ">hr"))
					.getAttribute("class").contains("__active")) {
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS);
				switched = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS.toString() + ">hr"))
						.getAttribute("class").contains("__active");
			} else
				switched = true;
		}
		if (!switched)
			throw new Exception("Desired Seach Type Not selected");
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS, searchTerm);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SUGGESTION_COINTAINER_CSS);
	}

	public void addTechnologyTerms(String Term, int count, Boolean freetext, Boolean shollall) throws Exception {
		String suggestiontext = OnePObjectMap.NEON_IPA_TECH_SUG_TEXT_VAR_CSS.toString();
		//String hits = OnePObjectMap.NEON_IPA_TECH_SUG_HITS_VAR_CSS.toString();
		String add = OnePObjectMap.NEON_IPA_TECH_SUG_ADD_VAR_CSS.toString();
		int added = 0;

		if (!Term.isEmpty() || (count > 0 && count < 8)) {
			for (int i = 1; i < 8; i++) {

				String row = suggestiontext.replace("$index", String.valueOf(i));
				String button = add.replace("$index", String.valueOf(i));

				if (!Term.isEmpty()) {
					if (ob.findElement(By.cssSelector(row)).getText().trim().equalsIgnoreCase(Term.trim())) {
						ob.findElement(By.cssSelector(button)).click();
						break;
					}
				} else {
					ob.findElement(By.cssSelector(button)).click();
					added++;
					if (added == count)
						break;
				}

			}
		} else if (freetext) {
			//String row = suggestiontext.replace("$index", String.valueOf(0));
			String button = add.replace("$index", String.valueOf(0));
			ob.findElement(By.cssSelector(button)).click();
		}

		if (shollall)
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_SHOW_ALL_LINK_XPATH);

	}

	public void exploreSearch() throws Exception {
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_EXPLORE_BUTTON_CSS);

		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS);
		try {
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			throw new Exception("Explore not working");
		}
	}

	public boolean checkForTextInSearchTermList(String text) throws Exception {
		try{
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_LABEL_CSS.toString()), 60);
		List<WebElement> labelsList = ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_CSS.toString()
				+ " " + OnePObjectMap.NEON_IPA_SEARCH_TERMS_LABEL_CSS.toString()));
		for (WebElement we : labelsList) {

			if (we.getText().trim().equalsIgnoreCase(text)) {
				return true;
			}
		}
		}catch(Exception e){
			return false;
		}
		return false;
	}

	public String selectSearchTermFromSuggestion(int index) {
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_TECH_SUG_DROPDOWN_CSS.toString()), 60);
		String term=ob.findElement(By.cssSelector(
				OnePObjectMap.NEON_IPA_TECH_SUG_LIST_CSS.toString().replace("$index", String.valueOf(index)))).getText();
		ob.findElement(By.cssSelector(
				OnePObjectMap.NEON_IPA_TECH_SUG_ADD_VAR_CSS.toString().replace("$index", String.valueOf(index))))
				.click();
		return term.replaceAll("\\([\\s,\\d]+\\)", "").trim();
	}

	public void clickOnNewSearchLinkInHeader() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_TECH_NEW_SEARCH_HEADER_LINK_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_TECH_NEW_SEARCH_HEADER_LINK_CSS);
	}

	public WebElement getSearchItemElement(String serachTerm) throws Exception {
		if (serachTerm.contains("("))
			serachTerm = serachTerm.substring(0, serachTerm.indexOf("(")).trim();
		System.out.println(serachTerm);
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_CSS.toString()), 60);
		List<WebElement> labelsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_SEARCH_TERMS_CSS);
		String text = "";
		for (WebElement we : labelsList) {
			text = we.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_LABEL_CSS.toString())).getText();

			if (text.trim().equalsIgnoreCase(serachTerm)) {
				return we;
			}
		}
		throw new Exception("Search term not found in list");
	}
	
	public WebElement getSearchItemElementDashboard(String serachTerm) throws Exception {
		if (serachTerm.contains("("))
			serachTerm = serachTerm.substring(0, serachTerm.indexOf("(")).trim();
		
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_DASHBOARD_LABEL_CSS.toString()), 60);
		List<WebElement> labelsList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_SEARCH_TERMS_DASHBOARD_LABEL_CSS);
		
		String text = "";
		for (WebElement we : labelsList) {
			text = we.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_DASHBOARD_CSS.toString())).getText();

			if (text.trim().equalsIgnoreCase(serachTerm)) {
				return we;
			}
		}
		throw new Exception("Search term not found in list");
	}

	public int getselectedSynonymsCount(String searchTerm) throws Exception {
		WebElement term = getSearchItemElement(searchTerm);

		List<WebElement> synms = term
				.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERM_PRESELECTED_SYNMS_CSS.toString()));

		return synms.size();
	}
	
	public int getselectedSynonymsCountDashboard(String searchTerm) throws Exception {
		WebElement term = getSearchItemElementDashboard(searchTerm);
		
		List<WebElement> synms = ob
				.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERM_PRESELECTED_SYNMS_DB_CSS.toString()));

		return synms.size();
	}

	// pass 0 to select all synonyms
	public List<String> selectSearchTermSynms(String searchTerm, int count) throws Exception {
		List<String> synmsList = new ArrayList<>();
		WebElement term = getSearchItemElement(searchTerm);
		if (count == 0)
			count = term.findElements(By.cssSelector("li")).size();

		List<WebElement> list = term.findElements(By.cssSelector("li"));
		WebElement checkBox;
		for (int i = 0; i <= count - 1; i++) {
			synmsList.add(list.get(i).getText());
			checkBox = list.get(i).findElement(By.cssSelector("input[type='checkbox']"));
			if (!checkBox.isSelected())
				checkBox.click();
		}
		return synmsList;
	}

	public List<String> validateSelectedSynonyms(String searchTerm, List<String> synms) throws Exception {
		List<String> synmsList = new ArrayList<>();
		WebElement term = getSearchItemElement(searchTerm);
		List<WebElement> list = term.findElements(By.cssSelector("li"));

		for (String str : synms) {
			for (WebElement we : list) {
				if (we.getText().equals(str)) {
					if (!we.findElement(By.cssSelector("input[type='checkbox']")).isSelected()) {
						synmsList.add(str);
						break;
					}

				}

			}
		}
		return synmsList;

	}
	
	
	public void clickOnSearchTermDropDown(String searchTerm) throws Exception{
		WebElement term=getSearchItemElement(searchTerm);
		term.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERM_DROP_DOWN_ICON_CSS.toString())).click();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SEARCH_TERM_SYNMS_LIST_CSS);
	}
	
	public void clickOnSearchTermDropDownDashboard(String searchTerm) throws Exception{
		WebElement term=getSearchItemElementDashboard(searchTerm);
		term.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERM_DROP_DOWN_ICON_CSS.toString())).click();
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SEARCH_TERM_SYNMS_LIST_DB_CSS);
	}
	
	public boolean checkIfSearchTermDropDownIsDispalyed(String searchTerm) throws Exception{
		WebElement term=getSearchItemElement(searchTerm);
		try{
		return term.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERM_DROP_DOWN_ICON_CSS.toString())).isDisplayed();
		}catch(Exception e){
			return false;
		}
	}
	
	public void removeSearchTerm(String searchTerm) throws Exception{
		WebElement term=getSearchItemElement(searchTerm);
		term.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_SEARCH_TERMS_REMOVE_CSS.toString())).click();
	}
	
	public void deselectSearchTermSynms(String searchTerm,List<String> synms) throws Exception{
		WebElement term=getSearchItemElement(searchTerm);
		List<WebElement> list=term.findElements(By.cssSelector("li"));
		
		for (String str : synms) {
			for (WebElement we : list) {
				if (we.getText().equals(str)) {
					if (we.findElement(By.cssSelector("input[type='checkbox']")).isSelected()){
						we.findElement(By.cssSelector("input[type='checkbox']")).click();
						break;
					}					
				}
			}
		}
	}
	
	public void clickOnShowAllLinkInTypeAhead() throws Exception{
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_TECH_SHOW_ALL_LINK_CSS);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_TECH_SHOW_ALL_LINK_CSS);
	}
	
	public int validatePatentsCountForCompanyInTypeAhead() throws Exception {
		int count=0;
		boolean found=false;
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.NEON_IPA_COMPANY_TYPE_AHAED_LABEL_CSS.toString()), 60);
		
		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NEON_IPA_COMPANY_TYPE_AHAED_LABEL_CSS);
		Pattern pattern = Pattern.compile("\\([,\\d]+\\)");
		Matcher matcher;
		for(int i=1;i<list.size();i++){
				
			matcher = pattern.matcher(list.get(i).getText());

			while (matcher.find()) {
				found=true;
				break;			
			}
			
			if(!found)count++;
		}
		return count;	
	}
}