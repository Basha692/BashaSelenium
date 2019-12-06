package watpages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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
 * Class for Perform Browser Actions
 * 
 * @author UC225218
 *
 */
public class SearchAuthorClusterPage extends TestBase {

	int k = 1;
	static String wos_title = "Web of Science: Author search";
	static String Org_drpdwn_text = "Select an organization where this author has published.";
	static String Country_drpdwn_text = "Select a country/territory where this author has published.";
	static String orcid_Welcome_text = "Enter the author's name or ORCiD to begin your search against Web of Science article groups.";
	static String ORCid = "0000-0002-6423-7213";
	static String Suggestion_text = "We've found a large number of records matching your search.\n"
			+ "For the most relevant results, please select at least one location where this author has published, or select Find to view all results.";

	List<String> unSortedOrgname = new ArrayList<String>();
	List<String> SortedOrgname = new ArrayList<String>();
	List<String> unSortedCountryname = new ArrayList<String>();
	List<String> SortedCountryname = new ArrayList<String>();
	
	public SearchAuthorClusterPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}
	
	/**
	 * Method for Select author using one country and one org
	 * @param LastName
	 * @param country
	 * @param org
	 * @param test
	 * @throws Exception
	 */
	public void SearchAuthorClusterLastName(String LastName,String country, String org,ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(LastName, test);
			cliclFindBtn(test);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				selectCountryofAuthor(test,country);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				List<WebElement> orgName = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				if (orgName.size() != 0) {
					selectOrgofAuthor(test,org);
					cliclFindBtn(test);
				}else{
				test.log(LogStatus.INFO, "Org name selection is not required as the searched user has only one org. ");
				}
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and land in Author search result page.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author cluster and land in Author search result page.");
			throw new Exception();
		}
	}
	
	/**
	 * Method for Search an  author using last name and country
	 * @param LastName
	 * @param country
	 * @param test
	 * @throws Exception
	 */
	public void SearchAuthorClusterUsingLastnameAndCountry(String LastName,String country,ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(LastName, test);
			cliclFindBtn(test);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				selectCountryofAuthor(test,country);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				cliclFindBtn(test);
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and land in Author search result page.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author cluster and land in Author search result page.");
			throw new Exception();
		}
	}

	/**
	 * Common method to search for an author cluster with only LastName
	 * 
	 * @param LastName
	 * @author UC225218
	 * @throws Exception
	 * 
	 */
	public void SearchAuthorClusterLastName(String LastName, String Country1,String Country2, String org1, String org2,ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(LastName, test);
			cliclFindBtn(test);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				selectCountryofAuthor(test,Country1, Country2);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				List<WebElement> orgName = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				if (orgName.size() != 0) {
					selectOrgofAuthor(test,org1,org2);
					cliclFindBtn(test);
				}else{
				test.log(LogStatus.INFO, "Org name selection is not required as the searched user has only one org. ");
				}
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and land in Author search result page.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author cluster and land in Author search result page.");
			throw new Exception();
		}
	}

	/**
	 * @param LastName
	 * @throws Exception
	 * @throws InterruptedException
	 */
	
	@SuppressWarnings("static-access")
	public void testFindButtonFunctionality2(String LastName) throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
		pf.getBrowserWaitsInstance(ob).waitTime(4);
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).isDisplayed(),
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED");
		Assert.assertEquals(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).getText(), Suggestion_text,
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS NOT MATCHING");
		test.log(LogStatus.INFO, "AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED AND MATCHING");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
		if (ele.size() != 0) 
		{
			test.log(LogStatus.INFO, "Country Dropdown is disaplayed when search results are more than 50 clusters");
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
					"Find button is not enabled when country dropdown is diaplayed");
			test.log(LogStatus.INFO, "Find button is enabled when country dropdown is diaplayed");
			pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and land in Author search result page.");
			test.log(LogStatus.PASS, "Able to click Find button when no country option is clicked and successfully land in Author search result page");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
		else throw new Exception("Unable to click Find button when no country option is clicked");
	}
	
	public void cliclFindBtn(ExtentTest test) throws Exception, InterruptedException {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH,10);
		test.log(LogStatus.INFO, "Clicking find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
		} else {
			throw new Exception("FIND button not clicked");
		}
	}

	/**
	 * Common method to search for an author cluster with LastName and FirstName
	 * 
	 * @throws @author
	 *             UC225218
	 * @param LastName
	 *            FirstName
	 * @throws Exception
	 */
	public void SearchAuthorClusterLastAndFirstName(String LastName, String FirstName, String Country1, String Country2,String org1, String org2, ExtentTest test)
			throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(LastName, test);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
			enterAuthorFirstName(FirstName, test);
			cliclFindBtn(test);
			BrowserWaits.waitTime(2);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0)
				{
				selectCountryofAuthor(test,Country1, Country2);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				List<WebElement> orgName = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				if (orgName.size() != 0) {
					selectOrgofAuthor(test,org1,org2);
					cliclFindBtn(test);
				}else{
				test.log(LogStatus.INFO, "Org name selection is not required as the searched user has only one org. ");
				}
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
				
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and landed in Author search result page.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author and land in Author search result page.");
			throw new Exception(e);
		}
	}

	
	/**
	 * Common method to search for an author cluster with LastName and FirstName using one country and one org
	 * 
	 * @throws @author
	 *             UC202376
	 * @param LastName
	 *            FirstName
	 * @throws Exception
	 */
	public void SearchAuthorClusterLastAndFirstName(String lastName, String firstName, String country,String org, ExtentTest test)
			throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(lastName, test);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
			enterAuthorFirstName(firstName, test);
			cliclFindBtn(test);
			BrowserWaits.waitTime(2);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0)
				{
				selectCountryofAuthor(test,country);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				List<WebElement> orgName = pf.getBrowserActionInstance(ob)
						.getElements(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				if (orgName.size() != 0) {
					selectOrgofAuthor(test,org);
					cliclFindBtn(test);
				}else{
				test.log(LogStatus.INFO, "Org name selection is not required as the searched user has only one org. ");
				}
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
				
			}
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results", "Unable to search for an author and landed in Author search result page.");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author and land in Author search result page.");
			throw new Exception(e);
		}
	}

	
	/**
	 * Method to enter the author search name (Last name) character by
	 * character.
	 * 
	 * @throws Exception
	 * @param Name
	 *            element Typeahead
	 * 
	 */
	public void enterAuthorLastName(String LastName, ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
			for (int i = 0; i < LastName.length(); i++) {
				char c = LastName.charAt(i);
				pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH,
						String.valueOf(c));
				BrowserWaits.waitTime(0.25);
			}
			selectLastNameFromTypeahead(LastName, test);
		} catch (Exception e) {
			e.getMessage();
			test.log(LogStatus.FAIL, "Author Last name not entered");
		}
	}

	/**
	 * Method to enter the author search name (Last name) character by
	 * character. This method is special case for Alternate name typeahead
	 * search scenario.
	 * 
	 * @throws Exception
	 * @param Name
	 *            element Typeahead
	 * 
	 */
	public void enterAuthorLastName(OnePObjectMap path, String LastName, ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).getElement(path).clear();
			for (int i = 0; i < LastName.length(); i++) {
				char c = LastName.charAt(i);
				String s = new StringBuilder().append(c).toString();
				pf.getBrowserActionInstance(ob).enterFieldValue(path, s);
				BrowserWaits.waitTime(0.5);
			}
			selectLastNameFromTypeahead(LastName, test);
		} catch (Exception e) {
			e.getMessage();
			test.log(LogStatus.FAIL, "Author Last name not entered");
		}
	}

	/**
	 * Method to enter the author search name (First name) character by
	 * character.
	 * 
	 * @throws Exception
	 * @param Name
	 * 
	 */
	public void enterAuthorFirstName(String FirstName, ExtentTest test) throws Exception {

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		for (int i = 0; i < FirstName.length(); i++) {
			char c = FirstName.charAt(i);
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH,
					String.valueOf(c));
			BrowserWaits.waitTime(0.5);
		}
		selectFirstNameFromTypeahead(FirstName, test);
		test.log(LogStatus.INFO, "Author First name entered");
	}

	/**
	 * Method to enter the author search name (First name) character by
	 * character. This method is special case for Alternate name typeahead
	 * search scenario.
	 * 
	 * @throws Exception
	 * @param Name
	 * 
	 */
	public void enterAuthorFirstName(OnePObjectMap path, String FirstName, ExtentTest test) throws Exception {

		pf.getBrowserActionInstance(ob).getElement(path).clear();
		for (int i = 0; i < FirstName.length(); i++) {
			char c = FirstName.charAt(i);
			String s = new StringBuilder().append(c).toString();
			pf.getBrowserActionInstance(ob).enterFieldValue(path, s);
			BrowserWaits.waitTime(0.5);
		}
		selectFirstNameFromTypeahead(FirstName, test);
		test.log(LogStatus.INFO, "Author First name entered");
	}

	/**
	 * Method to select value from typeahead, This will loop for 5 times incase
	 * if typeahead suggestions are not displayed.(Configurable)
	 * 
	 * @throws Exception
	 * 
	 * @param lnTypeahead
	 *            selectionText
	 * 
	 */
	public void selectLastNameFromTypeahead(String selectionText, ExtentTest test) throws Exception {
		while (k < 2) {
			List<WebElement> web = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_LASTNAME_TYPEAHEAD_XPATH);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
			List<WebElement> lastNameSuggestions = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_LASTNAME_TYPEAHEAD_OPTION_XPATH);

			if (web.size() != 0) {
				for (int j = 0; j < lastNameSuggestions.size(); j++) {
					if (lastNameSuggestions.get(j).getText().equals(selectionText)) {
						lastNameSuggestions.get(j).click();
						test.log(LogStatus.INFO, "Typeahead is present for last name");
						test.log(LogStatus.INFO, "Author Last name entered");
						break;
					}
				}
				break;
			} else if (ele.size() == 0 && pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isDisplayed()) {
				test.log(LogStatus.INFO, "Author Last name entered");
				test.log(LogStatus.INFO,
						"No Typeahead displayed, Might be due to exact matching of provided last name with name in typeahead suggestion, Hence directly clicking FIND button");
				break;
			} else if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH)
					.isDisplayed()) {
				test.log(LogStatus.FAIL, "Entered name is incorrect, Please enter a valid last name ");
				break;
			} else {
				if (k == 2)
					throw new Exception("No Typeahead suggestion displayed even after 2 attempts");
				k++;
				enterAuthorLastName(selectionText, test);
			}
		}
	}

	/**
	 * Method to select value from typeahead, This will loop for 5 times incase
	 * if typeahead suggestions are not displayed.(Configurable)
	 * 
	 * @throws Exception
	 * 
	 * @param lnTypeahead
	 *            selectionText
	 * 
	 */
	public void selectFirstNameFromTypeahead(String selectionText, ExtentTest test) throws Exception {
		while (k < 2) {
			List<WebElement> web = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_TYPEAHEAD_XPATH);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
			List<WebElement> firstNameSuggestions = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_TYPEAHEAD_OPTION_XPATH);

			if (web.size() != 0) {
				for (int j = 0; j < firstNameSuggestions.size(); j++) {
					if (firstNameSuggestions.get(j).getText().equals(selectionText)) {
						firstNameSuggestions.get(j).click();
						test.log(LogStatus.INFO, "Typeahead is present for first name");
						test.log(LogStatus.INFO, "Author First name entered");
						break;
					}
				}
				break;
			} else if (ele.size() == 0 && pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isDisplayed()) {
				test.log(LogStatus.INFO, "Author Last name entered");
				test.log(LogStatus.INFO,
						"No Typeahead displayed, Might be due to exact matching of provided first name with name in typeahead suggestion, Hence directly clicking FIND button");
				break;
			} else if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH)
					.isDisplayed()) {
				test.log(LogStatus.FAIL, "Entered name is incorrect, Please enter a valid First name ");
				break;
			} else {
				if (k == 2)
					throw new Exception("No Typeahead suggestion displayed even after 2 attempts");
				k++;
				enterAuthorLastName(selectionText, test);
			}
		}
	}
	
	/**
	 * @param LastName
	 * @param CountryName1
	 * @param CountryName2
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void testFindButtonFunctionality(String LastName, String CountryName1, String CountryName2)
			throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
		pf.getBrowserWaitsInstance(ob).waitTime(4);
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).isDisplayed(),
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED");
		Assert.assertEquals(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).getText(), Suggestion_text,
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS NOT MATCHING");
		test.log(LogStatus.INFO, "AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED AND MATCHING");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
		if (ele.size() != 0) 
		{
			test.log(LogStatus.INFO, "Country Dropdown is disaplayed when search results are more than 50 clusters");
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
					"Find button is not enabled when country dropdown is diaplayed");
			test.log(LogStatus.INFO, "Find button is enabled when country dropdown is diaplayed");
			pf.getSearchAuthClusterPage(ob).selectCountryofAuthor(test,CountryName1, CountryName2);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
					"Find button is not enabled when country option is clicked");
			test.log(LogStatus.PASS, "Find button is enabled when country option is clicked");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
		else throw new Exception("Find button is not enabled when country option is clicked");
	}

	/**
	 * Method to select Country value for further filtering of author cluster.
	 * 
	 * @author UC225218
	 * @throws Exception
	 * 
	 * 
	 */
	public void selectCountryofAuthor(ExtentTest test,String ...CountryName) throws Exception {
		for (String country : CountryName) {
			selectCountryofAuthor(test, country);
		}
		
	}
	
	/**
	 * @param LastName
	 * @throws Exception
	 * @throws InterruptedException
	 */
	@SuppressWarnings("static-access")
	public void testFindButtonFunctionality(String LastName) throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
		pf.getBrowserWaitsInstance(ob).waitTime(4);
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).isDisplayed(),
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED");
		Assert.assertEquals(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).getText(), Suggestion_text,
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS NOT MATCHING");
		test.log(LogStatus.INFO, "AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED AND MATCHING");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
		if (ele.size() != 0) 
		{
			test.log(LogStatus.INFO, "Country Dropdown is disaplayed when search results are more than 50 clusters");
			Assert.assertTrue(pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled(),
					"Find button is not enabled when country dropdown is diaplayed");
			test.log(LogStatus.PASS, "Find button is enabled when country dropdown is diaplayed");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
		else throw new Exception("Find button is not enabled when country dropdown is diaplayed");
	}
	
	public void selectCountryofAuthor(ExtentTest test,String country) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob,
				By.xpath(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH.toString()), 3,
				"Country dropdown is not present in Author search page");
		try {
			pf.getBrowserActionInstance(ob).moveToElement(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			test.log(LogStatus.INFO,
					"Country name selected as the searched user resulted in more than 50 clusters... ");
			List<WebElement> ctry = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_OPTION_XPATH);
			if (ctry.size() != 0) {
				for (int j = 0; j < ctry.size(); j++) {
					if (ctry.get(j).getText().equals(country.trim())) {
						String xpath = OnePObjectMap.WAT_COUNTRY_CHECKBOX_XPATH.toString();
						ob.findElement(By.xpath(xpath.replace("Country", country))).click();
						test.log(LogStatus.INFO, "Country name clicked");
						break;
					}
				}
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to select country name from the list.");
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("static-access")
	public void testCountryDropdownFunctionality(String LastName) throws Exception, InterruptedException {
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
		pf.getBrowserWaitsInstance(ob).waitTime(4);
		Assert.assertTrue(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).isDisplayed(),
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED");
		Assert.assertEquals(pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_DRILL_DOWN_SUGGESTION_TEXT_XPATH).getText(), Suggestion_text,
				"AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS NOT MATCHING");
		test.log(LogStatus.INFO, "AUTHOR SEARCH DRILL DOWN SUGGESTION TEXT IS DISPLAYED AND MATCHING");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
		if (ele.size() != 0) 
		{
			test.log(LogStatus.PASS, "Country Dropdown is disaplayed when search results are more than 50 clusters");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
		else throw new Exception("Country Dropdown is not disaplayed when search results are more than 50 clusters");
	}
	
	/**
	 * @param LastName
	 * @param CountryName1
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void orgOrderTest(ExtentTest test, String LastName, String CountryName1) throws Exception, InterruptedException {
		try {
			// Verify whether control is in Author Search page
			Assert.assertEquals(
					pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
					wos_title, "Control is not in WOS Author Search page");
			test.log(LogStatus.INFO, "Control is in WOS Author Search page");

			// Search for an author cluster with only Last name
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
			pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				pf.getSearchAuthClusterPage(ob).selectCountryofAuthor(test,CountryName1);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				List<WebElement> OrgList = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_INNER_XPATH);
				ListIterator<WebElement> litr = OrgList.listIterator();
				while(litr.hasNext())
				{
					String temp = litr.next().getText();
					unSortedOrgname.add(temp);
					SortedOrgname.add(temp);
				}
				Collections.sort(SortedOrgname);
				for(int i=0;i<unSortedOrgname.size();i++)
				{
					Assert.assertEquals(SortedOrgname.get(i), unSortedOrgname.get(i),"Org Names are not sorted as expected");
				}
				test.log(LogStatus.PASS, "Org Names are sorted as expected");
		    pf.getBrowserActionInstance(ob).closeBrowser();
			}
		} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "Org Names are not sorted as expected");
				logFailureDetails(test, e, "Org Names are not sorted as expected", "Org_sorting_fail");
				pf.getBrowserActionInstance(ob).closeBrowser();
			}
	}
	
	/**
	 * @param LastName
	 * @param CountryName1
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void CountryOrderTest(ExtentTest test, String LastName, String CountryName1) throws Exception, InterruptedException {
		try {
			// Verify whether control is in Author Search page
			Assert.assertEquals(
					pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
					wos_title, "Control is not in WOS Author Search page");
			test.log(LogStatus.INFO, "Control is in WOS Author Search page");

			// Search for an author cluster with only Last name
			test.log(LogStatus.INFO, "Entering author name... ");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
			pf.getSearchAuthClusterPage(ob).cliclFindBtn(test);
			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				List<WebElement> CountryList = pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_INNER_XPATH);
				ListIterator<WebElement> litr = CountryList.listIterator();
				while(litr.hasNext())
				{
					String temp = litr.next().getText();
					unSortedCountryname.add(temp);
					SortedCountryname.add(temp);
				}
				Collections.sort(SortedCountryname);
				for(int i=0;i<unSortedCountryname.size();i++)
				{
					Assert.assertEquals(SortedCountryname.get(i), unSortedCountryname.get(i),"Country Names are not sorted as expected");
				}
				test.log(LogStatus.PASS, "Country Names are sorted as expected");
		    pf.getBrowserActionInstance(ob).closeBrowser();
			}
		} catch (AssertionError e) {
				test.log(LogStatus.FAIL, "Country Names are not sorted as expected");
				logFailureDetails(test, e, "Country Names are not sorted as expected", "Country_sorting_fail");
				pf.getBrowserActionInstance(ob).closeBrowser();
			}
	}
	
	/**
	 * Method to select Country value for further filtering of author cluster.
	 * 
	 * @author UC225218
	 * @throws Exception
	 * 
	 * 
	 */
	public void selectOrgofAuthor(ExtentTest test,String ...orgName) throws Exception {
		
		for (String org : orgName) {
			selectOrgofAuthor(test, org);
		}
		
	}

	/**
	 * Method to select organization value for further filtering of author
	 * cluster.
	 * 
	 * @author UC225218
	 * @throws Exception
	 * 
	 * 
	 */
	public void selectOrgofAuthor(ExtentTest test,String orgName) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH, 5);
		try {
			test.log(LogStatus.INFO, "Selecting relavent organization... ");
			pf.getBrowserActionInstance(ob).moveToElement(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
			List<WebElement> org = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_ORG_OPTION_XPATH);
			if (org.size() != 0) {
				for (int j = 0; j < org.size(); j++) {
					if (org.get(j).getText().equals(orgName.trim())) {
						String xpath = OnePObjectMap.WAT_ORG_CHECKBOX_XPATH.toString();
						ob.findElement(By.xpath(xpath.replace("OrgName", orgName))).click();
						test.log(LogStatus.INFO, "Org name clicked");
						break;
					}
				}
			}

		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to select Org name from the list.");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param test
	 * @throws Exception
	 */
	public void validateAuthorSearchPage(ExtentTest test) throws Exception {
		Assert.assertTrue(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).isDisplayed(),
				"Lastname text box not visible");
		test.log(LogStatus.INFO, "User is able to see last name textbox and can be used for author cluster search");

		Assert.assertTrue(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).isDisplayed(),
				"Firstname text box not visible");
		test.log(LogStatus.INFO, "User is able to see First name textbox and can be used for author cluster search");
	}

	/**
	 * Method for Search Author cluster using last name
	 * 
	 * @param LastName
	 * @param test
	 * @throws Exception,
	 *             When unable to search for author using last name
	 */
	public void SearchAuthorCluster(String LastName, ExtentTest test) throws Exception {
		try {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
			enterAuthorLastName(LastName, test);
			pf.getBrowserWaitsInstance(ob)
					.waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
			test.log(LogStatus.INFO, "Find button clicked");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to search for an author using lastname");
			throw new Exception(e);
		}
	}

	/**
	 * @param LastName
	 * @param FirstName
	 * @param CountryName
	 * @param OrgName
	 * @throws Exception
	 */
	public void searchAuthorClusterLastandFirstname(String LastName, String FirstName, String CountryName1,String CountryName2,
			String OrgName1, String OrgName2,ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).SearchAuthorClusterLastAndFirstName(LastName, FirstName, CountryName1, CountryName2,OrgName1,OrgName2, test);
		test.log(LogStatus.PASS, "Successfully searched for an author and landed in Author search result page.");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	
	public void searchAuthorClusterOnlyLastName(String lastName, String countryName, String orgName, ExtentTest test)
			throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with only Last name
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).SearchAuthorClusterLastName(lastName, countryName,orgName, test);
		test.log(LogStatus.PASS,
				"Successfully searched for an author using only Last name and landed in Author search result page.");
		//pf.getBrowserActionInstance(ob).closeBrowser();
	}
	
	/**
	 * Method for select author using two countries and two org's
	 * @param LastName
	 * @param CountryName1
	 * @param CountryName2
	 * @param OrgName1
	 * @param OrgName2
	 * @param test
	 * @throws Exception
	 */
	public void searchAuthorClusterOnlyLastName(String LastName, String CountryName1, String CountryName2,String OrgName1, String OrgName2,ExtentTest test)
			throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with only Last name
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).SearchAuthorClusterLastName(LastName, CountryName1, CountryName2, OrgName1,OrgName2, test);
		test.log(LogStatus.PASS,
				"Successfully searched for an author using only Last name and landed in Author search result page.");
		//pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterWithOnlyFirstName(String FirstName, ExtentTest test)
			throws Exception, InterruptedException {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with only FIRST name
		test.log(LogStatus.INFO, "Entering author First name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		for (int i = 0; i < FirstName.length(); i++) {
			char c = FirstName.charAt(i);
			String s = new StringBuilder().append(c).toString();
			pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, s);
			BrowserWaits.waitTime(0.5);
		}
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			test.log(LogStatus.FAIL, "Able to search for author cluster with only First name");
		}
		test.log(LogStatus.PASS, "User cant search for Author cluster with only first name");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterLastNameTypeahead(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author name... ");

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, "U");
		if (pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_AUTHOR_LASTNAME_TYPEAHEAD_XPATH))
				.isDisplayed()) {
			test.log(LogStatus.PASS, "Typeahead displayed for minimum 1 Letter");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterFirstNameTypeahead(String LastName, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author name... ");

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, "J");
		if (pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_AUTHOR_FIRSTNAME_TYPEAHEAD_XPATH))
				.isDisplayed()) {
			test.log(LogStatus.PASS, "First name Typeahead displayed for minimum 1 Letter - Firststname");
			pf.getBrowserActionInstance(ob).closeBrowser();
		}
	}

	public void searchAuthorClusterBlankLastName(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author First name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, " ");
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			test.log(LogStatus.FAIL, "Able to search for author cluster with blank last name");
			throw new Exception("User is able to search for author cluster with blank last name");
		}
		test.log(LogStatus.PASS, "User cant search for Author cluster with blank last name");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterWithSymbolsLastName(String Symbols, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with symbols in last name
		test.log(LogStatus.INFO, "Entering author last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();

		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, Symbols);
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			test.log(LogStatus.PASS, "Unable to search for author cluster with special character in Last name");
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with special character in Last name OR Find button is enabled for Symbol search");
			throw new Exception(
					"User is able to search for Author cluster with special character in Last name OR Find button is enabled for Symbol search");
		}
	}

	public void searchAuthorClusterWithOnlyFirstName(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, "10");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			test.log(LogStatus.PASS, "Unable to search for author cluster with numbers in Last name");
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with numbers in Last name OR Find button is enabled for number search");
			throw new Exception(
					"User is able to search for Author cluster with numbers in Last name OR Find button is enabled for number search");
		}
	}

	public void searchAuthorClusterWithAlphaneumericLastName(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, "Test007");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			test.log(LogStatus.PASS, "Unable to search for author cluster with alphanumeric characters in Last name");
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with alphanumeric characters in Last name OR Find button is enabled for alphanumeric characters search");
			throw new Exception(
					"User is able to search for Author cluster with alphanumeric characters in Last name OR Find button is enabled for alphanumeric characters search");
		}
	}

	public void searchAuthorClusterBlankFirstName(String LastName, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, " ");
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH, 2);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()
				&& pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH)
						.isEnabled()) {
			test.log(LogStatus.PASS, "User is able to search for author cluster with blank First name");
		}
	}

	public void searchAuthorClusterWithSymbolsLastName(String Symbols, String errorMessage, ExtentTest test)
			throws Exception {
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, Symbols);
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH)
					.getText().equals(errorMessage)) {
				test.log(LogStatus.INFO, "Error text matching for symbol -----------    " + Symbols);
				test.log(LogStatus.PASS, "Unable to search for author cluster with special character in First name");
			}
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with special character in First name OR Find button is enabled for Symbol search");
			throw new Exception(
					"Test User is able to search for Author cluster with special character in First name OR Find button is enabled for Symbol search");
		}
	}

	public void searchAuthorClusterNumberFirstName(String LastName, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, "10");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			test.log(LogStatus.PASS, "Unable to search for author cluster with numbers in Last name");
			pf.getBrowserActionInstance(ob).closeBrowser();
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with numbers in Last name OR Find button is enabled for number search");
			throw new Exception(
					"User is able to search for Author cluster with numbers in Last name OR Find button is enabled for number search");

		}
	}

	public void searchAuthorClusterAlphanuemricFirstName(String LastName, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, "Test007");
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		pf.getBrowserWaitsInstance(ob).waitForPageLoad(ob);
		if (ele.size() != 0 && !pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH).isEnabled()) {
			test.log(LogStatus.PASS, "Unable to search for author cluster with alphanumeric characters in First name");
		} else {
			test.log(LogStatus.FAIL,
					"User is able to search for Author cluster with alphanumeric characters in First name OR Find button is enabled for alphanumeric characters search");
			throw new Exception(
					"User is able to search for Author cluster with alphanumeric characters in First name OR Find button is enabled for alphanumeric characters search");
		}
	}

	public void typeaheadMultipleAltNameLastName(String LastName, String Lastname2, String Lastname3, ExtentTest test)
			throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH);

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME2_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(OnePObjectMap.WAT_AUTHOR_LASTNAME2_XPATH, Lastname2, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH);

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME3_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(OnePObjectMap.WAT_AUTHOR_LASTNAME3_XPATH, Lastname3, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
	}

	public void typeaheadMultipleAltNameFirstName(String LastName, String FirstName, String Lastname2, String Lastname3,
			String Firstname2, String Firstname3, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering first set of Last & First name");
		// Last and first name 1st time
		test.log(LogStatus.INFO, "Entering author Last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorFirstName(FirstName, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH);
		test.log(LogStatus.INFO, "Entering second set of Last & First name");
		// Last and first name 2nd time
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME2_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME2_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(OnePObjectMap.WAT_AUTHOR_LASTNAME2_XPATH, Lastname2, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME2_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME2_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorFirstName(OnePObjectMap.WAT_AUTHOR_FIRSTNAME2_XPATH, Firstname2,
				test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH);
		test.log(LogStatus.INFO, "Entering third set of Last & First name");
		// Last and first name 3rd time
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME3_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME3_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(OnePObjectMap.WAT_AUTHOR_LASTNAME3_XPATH, Lastname3, test);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME3_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME3_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorFirstName(OnePObjectMap.WAT_AUTHOR_FIRSTNAME3_XPATH, Firstname3,
				test);
		test.log(LogStatus.INFO, "Editing first name entered in round 1");
		//// Only First name last time
		pf.getSearchAuthClusterPage(ob).enterAuthorFirstName(FirstName, test);
		test.log(LogStatus.PASS, "Firstname Typeahead displayed during multiple Alternate name search");
	}

	public void altNameLinkGreyedNoLastname(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author name... ");

		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.INFO, "Add alternate name button not enabled when no data entered into Lastname field");
		} else {
			test.log(LogStatus.FAIL, "Add alternate name button is enabled when no data entered into Lastname field");
			throw new Exception("Add alternate name button is enabled when no data entered into Lastname field");
		}
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, "U");
		if (pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.PASS,
					"Add alternate name button enabled after entering a single character into Lastname field");
		} else {
			test.log(LogStatus.FAIL,
					"Add alternate name button is not enabled even after entering a single character into Lastname field");
			throw new Exception(
					"Add alternate name button is not enabled even after entering a single character into Lastname field");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void altNameLinkGreyedOnlyFirstname(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author name... ");

		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.INFO, "Add alternate name button not enabled when no data entered into Firstname field");
		} else {
			test.log(LogStatus.FAIL, "Add alternate name button is enabled when no data entered into Firstname field");
			throw new Exception("Add alternate name button is enabled when no data entered into Firstname field");
		}
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, "U");
		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.PASS,
					"Add alternate name button not enabled even after entering a single character into Firstname field");
		} else {
			test.log(LogStatus.FAIL,
					"Add alternate name button is enabled after entering a single character into Firstname field");
			throw new Exception(
					"Add alternate name button is enabled after entering a single character into Firstname field");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterLastNameTypeahead(String LastName, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering author name... ");

		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.INFO, "Add alternate name button not enabled when no data entered into Lastname field");
		} else {
			test.log(LogStatus.FAIL, "Add alternate name button is enabled when no data entered into Lastname field");
			throw new Exception("Add alternate name button is enabled when no data entered into Lastname field");
		}
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		if (pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.INFO, "Add alternate name button enabled after entering a value into Lastname field");
			pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
			test.log(LogStatus.INFO, "Cleared value from Lastname field");
		} else {
			test.log(LogStatus.FAIL,
					"Add alternate name button is not enabled even after entering value into Lastname field");
			throw new Exception(
					"Add alternate name button is not enabled even after entering value into Lastname field");
		}

		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.PASS, "Add alternate name button not enabled after clearing Lastname field");
		} else {
			test.log(LogStatus.FAIL,
					"Add alternate name button is enabled evenafter clearing data from Lastname field");
			throw new Exception("Add alternate name button is enabled evenafter clearing data from Lastname field");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void clientSideValidationLastName(String InvalidText, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering invalid/ No result author last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, InvalidText);
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		if (ele.size() != 0) {
			test.log(LogStatus.PASS,
					"Error message displayed when there is no typeahead suggestions are available for the user entered last name");
		} else {
			test.log(LogStatus.FAIL,
					"Error message not displayed when there is no typeahead suggestions are available for the user entered last name");
			throw new Exception(
					"Error message not displayed when there is no typeahead suggestions are available for the user entered last name");
		}
	}

	public void clientSideValidationLastName(String LastName, String InvalidText, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		test.log(LogStatus.INFO, "Entering author last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_FIRSTNAME_XPATH, InvalidText);
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		if (ele.size() != 0) {
			test.log(LogStatus.PASS,
					"Error message displayed when there is no typeahead suggestions are available for the user entered First name");
		} else {
			test.log(LogStatus.FAIL,
					"Error message not displayed when there is no typeahead suggestions are available for the user entered First name");
			throw new Exception(
					"Error message not displayed when there is no typeahead suggestions are available for the user entered First name");
		}
	}

	public void findButtonFunctionality(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		if (!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			test.log(LogStatus.PASS, "FIND button disabled when there is no First or Last name entered by user");
		} else {
			throw new Exception("FIND button enabled when there is no First or Last name entered by user");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void countryDropdownStaticText(String LastName, ExtentTest test) throws Exception, InterruptedException {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with only Last name
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
		test.log(LogStatus.INFO, "Clicking find button... ");
		BrowserWaits.waitTime(2);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			Assert.assertEquals(
					pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_PAGE_COUNTRY_DROPDOWN_TEXT_XPATH).getText(),
					Country_drpdwn_text);
		} else {
			throw new Exception("FIND button not clicked");
		}
		test.log(LogStatus.PASS, "Text above country dropdown matches the expectation.");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void countryDropdownStaticText(String LastName, String CountryName1, String CountryName2,ExtentTest test)
			throws Exception, InterruptedException {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster with only Last name
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).enterAuthorLastName(LastName, test);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);
		test.log(LogStatus.INFO, "Clicking find button... ");
		BrowserWaits.waitTime(2);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH)
				.isEnabled()) {
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_NAME_FIND_BTN_XPATH);

			List<WebElement> ele = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.WAT_AUTHOR_COUNTRY_DROPDOWN_XPATH);
			if (ele.size() != 0) {
				pf.getSearchAuthClusterPage(ob).selectCountryofAuthor(test,CountryName1,CountryName2);
				pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_ORG_DROPDOWN_XPATH);
				Assert.assertEquals(
						pf.getBrowserActionInstance(ob)
								.getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_PAGE_ORG_DROPDOWN_TEXT_XPATH).getText(),
						Org_drpdwn_text);
			} else {
				test.log(LogStatus.INFO,
						"Country name selection is not required as the searched user resulted in less than 50 clusters... ");
			}

		} else {
			throw new Exception("FIND button not clicked");
		}
		test.log(LogStatus.PASS, "Text above org dropdown matches the expectation.");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void searchAuthorClusterLessthan50DIAS(String LastName, String FirstName, String CountryName1, String CountryName2,String OrgName1,String OrgName2,
			ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		// Search for an author cluster
		test.log(LogStatus.INFO, "Entering author name... ");
		pf.getSearchAuthClusterPage(ob).SearchAuthorClusterLastAndFirstName(LastName, FirstName, CountryName1, CountryName1,OrgName1, OrgName2,test);
		test.log(LogStatus.PASS,
				"Successfully searched for an author who has only one country and one org and landed in Author search result page.");
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void altNameLinkGreyedWhenErrors(String InvalidText, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");
		test.log(LogStatus.INFO, "Entering invalid/ No result author last name... ");
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH);
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH).clear();
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_AUTHOR_LASTNAME_XPATH, InvalidText);
		List<WebElement> ele = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.WAT_AUTHOR_NAME_NOT_FOUND_ERROR_XPATH);
		test.log(LogStatus.INFO, "Trying to click find button... ");
		if (ele.size() != 0) {
			test.log(LogStatus.PASS,
					"Error message displayed when there is no typeahead suggestions are available for the user entered last name");
		} else {
			test.log(LogStatus.FAIL,
					"Error message not displayed when there is no typeahead suggestions are available for the user entered last name");
			throw new Exception(
					"Error message not displayed when there is no typeahead suggestions are available for the user entered last name");
		}
		if (!pf.getBrowserActionInstance(ob).getElement((OnePObjectMap.WAT_ADD_ALT_NAME_BTN_TEXT_XPATH)).isEnabled()) {
			test.log(LogStatus.PASS, "Add alternate name button not enabled when there is an error.");
		} else {
			test.log(LogStatus.FAIL, "Add alternate name button is enabled even when there is an error displayed.");
			throw new Exception("Add alternate name button is enabled even when there is an error displayed.");
		}
	}

	public void orcidSearch(String example_orcid, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);

		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LOGO_XPATH).isDisplayed());
		test.log(LogStatus.PASS, "ORCiD logo present");

		Assert.assertTrue(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH).isDisplayed());
		test.log(LogStatus.PASS, "ORCiD text box present");

		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_SEARCH_WELCOME_TEXT_XPATH).getText(),
				orcid_Welcome_text, "Welcome text not matching");
		test.log(LogStatus.PASS, "Welcome text matching");

		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH)
				.getAttribute("placeholder").equals(example_orcid)) {
			test.log(LogStatus.PASS, "Example text is displayed for orcid field");
		} else {
			throw new Exception("Example text is not displayed for orcid field");
		}
	}

	public void findButtonFunctionalityORCIDSearch(ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);

		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LOGO_XPATH).isDisplayed());
		test.log(LogStatus.INFO, "ORCiD logo present");

		if (!pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_ORCID_FIND_BTN_XPATH)
				.isEnabled()) {
			test.log(LogStatus.PASS, "FIND button disabled in the begining in ORCid search page");
		} else {
			throw new Exception("FIND button enabled in the begining in ORCid search page");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void oRCIDSearchError(String InvalidORCid, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);

		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LOGO_XPATH).isDisplayed());
		test.log(LogStatus.INFO, "ORCiD logo present");

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH).sendKeys(InvalidORCid);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH).isDisplayed()) {
			test.log(LogStatus.PASS, "Error displayed when the ORCid or format is invalid");
		} else {
			throw new Exception("Error not displayed when the ORCid or format is invalid");
		}
	}

	public void oRCIDAuthorClusterSearch(ExtentTest test) throws Exception, InterruptedException {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);

		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LOGO_XPATH).isDisplayed());
		test.log(LogStatus.INFO, "ORCiD logo present");
		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH).sendKeys(ORCid);
		BrowserWaits.waitTime(2);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_ORCID_FIND_BTN_XPATH)
				.isEnabled()) {
			test.log(LogStatus.INFO, "FIND button is enabled for author search in ORCid search page");
			pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_ORCID_FIND_BTN_XPATH);
			pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH);
			Assert.assertEquals(
					(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_SEARCH_RESULTS_TEXT_XPATH).getText()),
					"Search Results",
					"Unable to search for an author from ORCid Search page and landed in Author search result page.");
			test.log(LogStatus.PASS, "User is able to search for an author cluster using ORCiD");
		} else {
			throw new Exception("FIND button not enabled to search for author in ORCid search page");
		}
		pf.getBrowserActionInstance(ob).closeBrowser();
	}

	public void oRCIDToNameSearch(String InvalidORCid, ExtentTest test) throws Exception {
		// Verify whether control is in Author Search page
		Assert.assertEquals(
				pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH).getText(),
				wos_title, "Control is not in WOS Author Search page");
		test.log(LogStatus.INFO, "Control is in WOS Author Search page");

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);

		Assert.assertTrue(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_LOGO_XPATH).isDisplayed());
		test.log(LogStatus.INFO, "ORCiD logo present");

		pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH).sendKeys(InvalidORCid);
		// pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_WOS_AUTHOR_SEARCH_TITLE_XPATH);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH).isDisplayed()) {
			test.log(LogStatus.INFO, "Error displayed when the ORCid or format is invalid");
		} else {
			throw new Exception("Error not displayed when the ORCid or format is invalid");
		}

		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_TO_NAME_SEARCH_LINK_XPATH);
		if (pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_NAME_SEARCH_BUTTON_XPATH).isDisplayed()) {
			test.log(LogStatus.PASS, "Successfully navigated to Name search page");
		} else {
			throw new Exception("Didnt navigate to Name search page");
		}
	}
	
	/**
	 * Method for ORCID Search
	 * @param test
	 * @throws Exception
	 */
	public void ORCIDSearch(String orcid,ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_ORCID_LOGO_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_LOGO_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH,orcid);
		waitForAjax(ob);
		boolean findButtonStatus=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_ORCID_FIND_BTN_XPATH).isEnabled();
		if (findButtonStatus) {
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_ORCID_FIND_BTN_XPATH);
		} else {
			throw new Exception("FIND button is disabled for valid ORCID");
		}
	}
	
	
	
	/**
	 * Method for Baloon Popover display and close using double click on popover link
	 * @param test
	 * @throws Exception
	 */
	public void ballonPopoverOpenAndClose() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_FADE_IN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		
	}
	
	
	/**
	 * Method for Baloon Popover display using close(x) button
	 * @param test
	 * @throws Exception
	 */
	public void ballonPopoverClose() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_FADE_IN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		
	}
	
	/**
	 * Method for Baloon Popover display using outside click
	 * @param test
	 * @throws Exception
	 */
	public void ballonPopoverOutsideClose() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_FADE_IN_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsNotDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CLOSE_CSS);
		
	}
	
	/**
	 * Method for Baloon Popover content validation
	 * @param test
	 * @throws Exception
	 */
	public void ballonPopoverContent(String header,String content) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_FADE_IN_CSS);
		String popover_Header= pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS).getAttribute("wui-popover-header");
		logger.info("Popover_Header-->" + popover_Header);
		String popover_Content= pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS).getAttribute("wui-popover-content");
		logger.info("Popover_Content-->" + popover_Content);
		if(!(popover_Header.equals(header)&&popover_Content.equals(popover_Content))){
			throw new Exception("Popover Header and Content is not expected");
		}
		
	}
	
	/**
	 * Method for RID Search
	 * @param test
	 * @throws Exception
	 */
	public void RIDSearch(String rid,ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_RID_SEARCH_BTN_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_RID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_RID_TEXTBOC_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_RID_TEXTBOC_XPATH,rid);
		waitForAjax(ob);
		boolean findButtonStatus=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_RID_FIND_BTN_XPATH).isEnabled();
		if (findButtonStatus) {
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.WAT_AUTHOR_SEARCH_BY_RID_FIND_BTN_XPATH);
		} else {
			throw new Exception("FIND button is disabled for valid RID Search");
		}
	}
	
	/**
	 * Method for Invalid RID Pattern  Search Error Messages
	 * @param test
	 * @throws Exception
	 */
	public void InvaidRIDSearchErrorMsgVaidation(String invalidRID,String errMsg,ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_RID_SEARCH_BTN_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_RID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_RID_TEXTBOC_XPATH);
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.WAT_RID_TEXTBOC_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_RID_TEXTBOC_XPATH,invalidRID);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH);
		String ridErrMsg=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH).getText();
		logger.info("RID Error Message-->"+ridErrMsg);
		waitForAjax(ob);
		if(!ridErrMsg.equals(errMsg)){
			throw new Exception("Invalid RID Search Error Message");
		}
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_LINK_XPATH);
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Name search","ORCiD search","ResearcherID search");
	}
	
	/**
	 * Method for Invalid RID Pattern  Search Error Messages
	 * @param test
	 * @throws Exception
	 */
	public void InvaidORCiDSearchErrorMsgVaidation(String invalidORCiD,String errMsg,ExtentTest test) throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsClickable(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_ORCID_SEARCH_BTN_XPATH);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH);
		pf.getBrowserActionInstance(ob).clickAndClear(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH);
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.WAT_ORCID_TEXTBOC_XPATH,invalidORCiD);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_AUTHOR_SEARCH_POPOVER_POPUP_CSS);
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH);
		String orcidErrMsg=pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.WAT_ORCID_SEARCH_ERROR_TEXT_XPATH).getText();
		logger.info("ORCID Error Message-->"+orcidErrMsg);
		waitForAjax(ob);
		if(!orcidErrMsg.equals(errMsg)){
			throw new Exception("Invalid ORCID Search Error Message");
		}
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.WAT_SEARCH_LINK_XPATH);
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob).waitUntilText("Name search","ORCiD search","ResearcherID search");
	}

}
