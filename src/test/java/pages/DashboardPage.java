package pages;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.BrowserAction;
import util.BrowserWaits;
import util.OnePObjectMap;

public class DashboardPage extends TestBase {

	PageFactory pf;
	BrowserWaits browserWait;
	BrowserAction browserAction;

	public DashboardPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
		browserWait = new BrowserWaits(ob);
		browserAction = new BrowserAction(ob);

	}

	public void clickOnPatentFoundIcon() throws Exception {

		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_BOARD_PATENT_FOUND_ICON_CSS);
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.NEON_IPA_DASH_BOARD_PATENT_FOUND_ICON_CSS);

	}

	public List<String> getAllPatentRecords() throws Exception {
		List<String> recordList = new ArrayList<>();
		loadAllPatentRecords();
		List<WebElement> patentList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_RECORD_LIST_PAGE_PATENT_TITLE_CSS);

		for (WebElement we : patentList) {

			recordList.add(we.getText().trim());
		}

		return recordList;
	}

	public String clickOnNthPatentRecords(int index) throws Exception {
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.NEON_IPA_RECORD_LIST_PAGE_PATENT_TITLE_CSS.toString()), 60);
		List<WebElement> patentList = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_RECORD_LIST_PAGE_PATENT_TITLE_CSS);
		String title = patentList.get(index - 1).getText();
		patentList.get(index - 1).click();
		return title;
	}

	public void loadAllPatentRecords() throws InterruptedException {
		scrollAndWait();
		while (isSpinnerDisplayed()) {

			scrollAndWait();
		}

	}

	private void scrollAndWait() throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 250);");
		Thread.sleep(1000);

	}

	private boolean isSpinnerDisplayed() {
		try {
			return pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_TO_ENW_BACKTOENDNOTE_PAGELOAD_CSS)
					.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public int getPatentCount() throws Exception {
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_DASH_BOARD_PATENT_FOUND_COUNT_CSS);
		String count = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.NEON_IPA_DASH_BOARD_PATENT_FOUND_COUNT_CSS).getText();
		count = count.replaceAll(",", "");
		return Integer.parseInt(count);

	}

	public void SearchTermEnter(String searchType, String searchTerm) throws Exception {
		boolean switched = false;
		if (searchType.equalsIgnoreCase("company")) {
			if (!ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS.toString() + ">hr"))
					.getAttribute("class").contains("__active")) {
				browserAction.jsClick(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS);
				switched = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_COMPANY_LINK_CSS.toString() + ">hr"))
						.getAttribute("class").contains("__active");
			} else
				switched = true;
		} else {
			if (!ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS.toString() + ">hr"))
					.getAttribute("class").contains("__active")) {
				browserAction.jsClick(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS);
				switched = ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_TECHNOLOGY_LINK_CSS.toString() + ">hr"))
						.getAttribute("class").contains("__active");
			} else
				switched = true;
		}
		if (!switched)
			throw new Exception("Desired Seach Type Not selected");

		browserAction.enterFieldValue(OnePObjectMap.NEON_IPA_SEARCH_TEXTBOX_CSS, searchTerm);
		browserWait.waitUntilElementIsDisplayed(OnePObjectMap.NEON_IPA_SUGGESTION_COINTAINER_CSS);
	}

	// 1:2:2&&1:1:2:2
	public void addCompanyTerms(String hierarchy) throws Exception {

		String Parent = "li[index='$Parent']";// >ul>li:nth-of-type(2)>div
		String Child = ">ul>li:nth-of-type($Child)";
		String[] Company = hierarchy.split("&&");
		for (int x = 0; x < Company.length; x++) {
			String[] Index = Company[x].split(":");
			String Xpath = "";
			if (Index.length > 0) {
				Xpath = Parent.replace("$Parent", "" + (Integer.valueOf(Index[0]) - 1));
				for (int i = 1; i < Index.length; i++) {
					Xpath = Xpath + Child.replace("$Child", Index[i]);
				}
				Xpath = Xpath + ">div>button";
			}
			browserAction.jsClick(ob.findElement(By.cssSelector(Xpath)));

		}

	}

	public String getSelectedViewByOption() throws Exception {

		List<WebElement> opt = ob.findElements(By.cssSelector("button[class*='btn btn-default active']"));
		WebElement ele = null;
		if (opt.size() == 2)
			ele = opt.get(1);
		else
			ele = opt.get(0);
		return ele.getText();

	}

	public void addTechnologyTerms(String Term, int count, Boolean freetext, Boolean shollall) throws Exception {
		String suggestiontext = OnePObjectMap.NEON_IPA_TECH_SUG_TEXT_VAR_CSS.toString();
		String hits = OnePObjectMap.NEON_IPA_TECH_SUG_HITS_VAR_CSS.toString();
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
			String row = suggestiontext.replace("$index", String.valueOf(0));
			String button = add.replace("$index", String.valueOf(0));
			ob.findElement(By.cssSelector(button)).click();
		}

		if (shollall)
			browserAction.jsClick(OnePObjectMap.NEON_IPA_SHOW_ALL_LINK_XPATH);

	}

	public void exploreSearch() throws Exception {
		browserAction.jsClick(OnePObjectMap.NEON_IPA_EXPLORE_BUTTON_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);

		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}
	}

	public void validateTechCompitetor(ExtentTest test) throws Exception {
		List<company> companies = new ArrayList<company>();
		Set<String> Companylist = new HashSet<String>();
		String Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_X_CSS).getText();
		if (!Temp.trim().equalsIgnoreCase("Technology diversity")){
				logFailureDetails(test, "X axis is Label is not as expected Actual:" + Temp.trim() + "But Expected:Breadth", "fail");
		}else{
			test.log(LogStatus.PASS, "X axis is Label as expected \"Technology diversity\"");
		}
			
		Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_Y_CSS).getText();
		if (!Temp.trim().equalsIgnoreCase("Average patent strength")){
			logFailureDetails(test, "Y axis is Label is not as expected Actual:" + Temp.trim() + "But Expected:Breadth", "fail");
		}else
			test.log(LogStatus.PASS, "Y axis is Label as expected \"Average patent strength\"");

		// List<WebElement> TopIPC =
		// browserAction.getElements(OnePObjectMap.NEON_IPA_DASH_TECH_COM_TOP_IPC_CSS);
		Map<String, String> TOP10IPC = new HashMap<String, String>();
		String css = OnePObjectMap.NEON_IPA_DASH_TECH_COM_TOP_IPC_CSS.toString();

		for (int i = 1; i < 11; i++) {
			String row = css.replace("$index", String.valueOf(i));
			WebElement webelement = ob.findElement(By.cssSelector(row));
			TOP10IPC.put(webelement.getText(), ob.findElement(By.cssSelector(
					"svg.nvd3-svg g[class*='nvd3 nv-legend'] g[class='nv-series']:nth-of-type(" + i + ") circle"))
					.getCssValue("stroke"));

		}

		try {

			for (int i = 1; i < 11; i++) {
				WebElement webElement = ob.findElement(
						By.cssSelector("g[class*='nv-point-clips']>clipPath:nth-of-type(" + i + ") circle"));
				Actions actionBuilder = new Actions(ob);
				actionBuilder.moveToElement(ob.findElement(
						By.cssSelector("g[class*='nv-point-clips']>clipPath:nth-of-type(" + i + ") circle"))).click().build().perform();
				Thread.sleep(3000);
				companies.add(new company(ob.findElement(By.cssSelector("div[class='wui-charts-company-name']")).getText(), "",
						"", "", ""));

			}
		} catch (Exception e) {
			System.out.println(e);

		}
		for (company company : companies) {
			String companyName = "";
			if (company.Company.length() > 20)
				companyName = company.Company.substring(0, 20) + "...";
			else
				companyName = company.Company;
			String color = TOP10IPC.get(companyName);
			Companylist.add(companyName);
			/*
			 * String ActualColor = color.substring(4, color.length() - 1);
			 * String ExpectedColor = company.Color.substring(5,
			 * company.Color.lastIndexOf(",")); if
			 * (ActualColor.trim().equalsIgnoreCase(ExpectedColor.trim()))
			 * test.log(LogStatus.PASS,
			 * "Expected and Actual color displayed for IPC:" +
			 * company.getCompany() + " Count: " + company.PublicationCount +
			 * " size: " + company.Size + " are same"); else
			 * test.log(LogStatus.FAIL, "ExpectedColor:" + ExpectedColor +
			 * "but ActualColor" + ActualColor + " for IPC:" +
			 * company.getCompany() + " are not same");
			 */
		}
		if (companies.size() == TOP10IPC.size())
			test.log(LogStatus.PASS, "All IPCs are displayed Properly");
		else{
			logFailureDetails(test, "All IPCs are not displayed Properly", "fail");
		}
			

	}

	public void validateTechTrending(ExtentTest test) throws Exception {
		validateTechTrending(test, false);
	}

	public void validateTechTrending(ExtentTest test, boolean yearvalidate) throws Exception {
		try {
			Set<String> TOPLine = new HashSet<String>();
			Set<String> Line = new HashSet<String>();
			String Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_X_CSS).getText();
			if (!Temp.trim().equalsIgnoreCase("Year")){
				logFailureDetails(test, "X axis is Label is not as expected Actual:" + Temp.trim() + "But Expected:Breadth", "fail");
			}else
				test.log(LogStatus.PASS, "X axis is Label as expected \"Year\"");
			Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_X_MIN_CSS).getText();
			int min = Integer.valueOf(Temp);
			Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_X_MAX_CSS).getText();
			int max = Integer.valueOf(Temp);
			if (max > min)
				test.log(LogStatus.PASS, "X axis Values started from Min value and ended Max Value");
			else
				test.log(LogStatus.FAIL, "X axis Values not  started from Min value and ended Max Value");
			if (yearvalidate) {
				Calendar now = Calendar.getInstance(); // Gets the current date
														// and
														// time
				int year = now.get(Calendar.YEAR);
				if (max == year)
					test.log(LogStatus.PASS, "X axis graph plotted to the current year");
				else
					test.log(LogStatus.FAIL, "X axis graph not plotted to the current year");
				if (min == (year - 10))
					test.log(LogStatus.PASS, "X axis graph started from the current year-10");
				else{
					
					logFailureDetails(test, "X axis graph not started from the current year-10", "fail");
				}
					

			}
			Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_Y_CSS).getText();
			if (!Temp.trim().equalsIgnoreCase("Volume")){
				logFailureDetails(test, "Y axis is Label is not as expected Actual:" + Temp.trim() + "But Expected:Breadth", "fail");
				
			}
				
			else
				test.log(LogStatus.PASS, "Y axis is Label as expected \"Volume\"");
			Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_Y_MIN_CSS).getText();
			min = Integer.valueOf(Temp);
			Temp = browserAction.getElement(OnePObjectMap.NEON_IPA_DASH_TECH_COM_Y_MAX_CSS).getText();
			max = Integer.valueOf(Temp);
			if (max > min)
				test.log(LogStatus.PASS, "Y axis Values started from Min value and ended Max Value");
			else
				logFailureDetails(test, "Y axis Values not started from Min value and ended Max Value", "fail");
				

			String css = OnePObjectMap.NEON_IPA_DASH_TECH_COM_TOP_IPC_CSS.toString();
			try {
				for (int i = 1; i < 11; i++) {
					String row = css.replace("$index", String.valueOf(i));
					WebElement webelement = ob.findElement(By.cssSelector(row));
					TOPLine.add(webelement.getText());
				}
			} catch (Exception ex) {
				logFailureDetails(test, "IPC Info not displayed in the visualization", "fail");
			}
			try {
				String Text = "";
				int i = 1;
				for (int z = 1; z < 11; z++) {
					try {
						WebElement ele = ob.findElement(By.cssSelector(
								"g[clip-path*='url'] >g[class='nv-groups']>g:nth-of-type(" + z + ")>path"));
						Actions actionBuilder = new Actions(ob);
						actionBuilder.click(ele).build().perform();
					} catch (Exception e) {
						System.out.println("I tried");
					}
				}
				List<WebElement> elements = ob.findElements(By.cssSelector("g[class='nv-point-paths']>path"));
				for (int itr = 0; itr < elements.size(); itr++) {
					WebElement ele = ob
							.findElement(By.cssSelector("g[class='nv-point-paths']>path:nth-of-type(" + i + ")"));
					Actions actionBuilder = new Actions(ob);
					actionBuilder.moveToElement(ele).build().perform();
					try {
						Text = "";
						Text = ob.findElement(By.cssSelector("div[class*='ipa-charts__tooltip--header']")).getText();
						if (!Text.isEmpty())
							Line.add(Text);
					} catch (Exception ex) {
					}
					i++;
				}

			} catch (Exception e) {
				System.out.println(e);

			}
			int count = 0;
			for (String data : Line) {
				for (String Expected : TOPLine) {
					if (data.trim().contains(Expected.replaceAll(".", ""))) {
						count++;
						break;
					}
				}
			}
			if (count == 10)
				test.log(LogStatus.PASS, "Tech Trending Graph Plotted Sucessfully");
			else
				test.log(LogStatus.FAIL, "Tech Trending Graph Not Plotted Sucessfully");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void selectTechTrendingTAB() throws Exception {
		browserAction.jsClick(OnePObjectMap.NEON_IPA_DASH_TECH_TEC_TAB_CSS);

		try {
			pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}

	}

	public void selectTechCompetitorsTAB() throws Exception {
		browserAction.jsClick(OnePObjectMap.NEON_IPA_DASH_TECH_COM_TAB_CSS);

		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_TECH_COM_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}

	}

	public void selectTechPatentTAB() throws Exception {
		browserAction.jsClick(OnePObjectMap.NEON_IPA_DASH_TECH_PAN_TAB_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);

		try {
			waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PAT_SEL_DD_CSS.toString()), 30);
			ob.findElement(By.cssSelector(OnePObjectMap.NEON_IPA_DASH_PATENTS_FOUND_CNT_CSS.toString()));
		} catch (Exception e) {
			// throw new Exception("Explore not working");
		}

	}

	public void validateCompanyKeyInformationPanel(ExtentTest test) throws Exception {
		try {

			test.log(LogStatus.INFO, "Key Information Validation for Company Search");
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_LINK_CSS);
			pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob,
					By.cssSelector(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS.toString()), 30);
			boolean flag = pf.getBrowserActionInstance(ob)
					.getElement(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS).isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL, "Key Information Panel " + (flag ? "" : "not ")
					+ "Displayed after clicking on KeyInformation Click");
			if (!flag)
				throw new Exception("Key Information Panel not dispalyed after clicking on Key Information Link");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_PUB_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Publication Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_PUB_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Publication Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_LIVE_PATENT_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"LIVE PATENT Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_LIVE_PATENT_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"LIVE PATENT Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_FAMILIES_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"FAMILIES Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_FAMILIES_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"FAMILIES Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_STRENGTH_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"AVG.STRENGTH Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_STRENGTH_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"AVG.STRENGTH Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_CUR_OWNER_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"CUR OWNER Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_CUR_OWNER_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"CUR OWNER Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_PARENT_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"PARENT Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_PARENT_VAL_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"PARENT Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_TOP_TEC_TERMS_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Top Technology Terms Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			List<WebElement> Terms = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.NEON_IPA_DASH_COM_KEY_TOP_TEC_TERMS_VALS_XPATH);
			int count = 0;
			for (WebElement webElement : Terms) {
				if (webElement.isDisplayed())
					count++;
			}
			flag = count == 10;
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Top Technology Terms Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_TOP_COUNTRIES_XPATH)
					.isDisplayed();
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Top Technology Terms Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
			Terms = pf.getBrowserActionInstance(ob)
					.getElements(OnePObjectMap.NEON_IPA_DASH_COM_KEY_TOP_COUNTRIES_VALS_XPATH);
			count = 0;
			for (WebElement webElement : Terms) {
				if (webElement.isDisplayed())
					count++;
			}
			flag = count == 5;
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
					"Top Technology Terms Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_LINK_CSS);
			pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
			Thread.sleep(100);
			// .waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS);
			WebElement ele = null;
			try {
				ele = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS);
				flag = (!ele.isDisplayed());				
			} catch (Exception e) {
				flag = true;
			}
			test.log(flag ? LogStatus.PASS : LogStatus.FAIL, "Key Information Panel " + (flag ? "not " : "")
					+ "Displayed after clicking on KeyInformation Click When Panel is Dispalyed");
		} catch (Exception ex) {

		}
	}

	public void validateTechnologyKeyInofrmation(ExtentTest test) throws Exception {
		int count = 0;
		test.log(LogStatus.INFO, "Key Information Validation for Technology Search");
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS.toString()), 30);
		boolean flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Key Information Panel " + (flag ? "" : "not ") + "Displayed after clicking on KeyInformation Click");
		if (!flag)
			throw new Exception("Key Information Panel not dispalyed after clicking on Key Information Link");

		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_STRENGTH_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"AVG.STRENGTH Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_STRENGTH_VAL_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"AVG.STRENGTH Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_APP_LAST5_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Application in last 5 years Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_APP_LAST5_VAL_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Application in last 5 years Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_COM_KEY_TOP_TEC_TERMS_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Top Fillers Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
		List<WebElement> Terms = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_TOPFILERS_VALS_XPATH);
		count = 0;
		for (WebElement webElement : Terms) {
			if (webElement.isDisplayed())
				count++;
		}
		flag = count == 5;
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Top Fillers Terms Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
		count = 0;
		flag = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_TOPTECH_TERMS_XPATH)
				.isDisplayed();
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Top Technology Terms Label " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");
		Terms = pf.getBrowserActionInstance(ob)
				.getElements(OnePObjectMap.NEON_IPA_DASH_TECH_KEY_TOPTECH_TERMS_VALS_XPATH);

		for (WebElement webElement : Terms) {
			if (webElement.isDisplayed())
				count++;
		}
		flag = count == 10;
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL,
				"Top Technology Terms Values " + (flag ? "" : "not ") + "displayed in KeyInformation Panel");

		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_LINK_CSS);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsNotDisplayed(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		WebElement ele = null;
		try {
			ele = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_KEYINFORMATION_PANEL_CSS);
			flag = (!ele.isDisplayed());
		} catch (Exception e) {
			flag = true;
		}
		test.log(flag ? LogStatus.PASS : LogStatus.FAIL, "Key Information Panel " + (flag ? "not " : "")
				+ "Displayed after clicking on KeyInformation Click When Panel is Dispalyed");

	}

	public int getDisplayedCount(OnePObjectMap neonIpaDashTechComCss, ExtentTest logger) throws Exception {
		int count = 0;
		List<WebElement> Elements = pf.getBrowserActionInstance(ob).getElements(neonIpaDashTechComCss);
		for (WebElement webElement : Elements) {
			if (webElement.isDisplayed())
				count++;
		}
		if (Elements.size() == count)
			logger.log(LogStatus.PASS, neonIpaDashTechComCss.name() + " Mandatory field displayed in all records");
		else
			logger.log(LogStatus.FAIL, neonIpaDashTechComCss.name() + " Mandatory field not displayed in all records");
		return count;

	}

	public List<Double> getRelavencesList(OnePObjectMap LocatorName) throws Exception {
		List<Double> str = new ArrayList<Double>();
		try {
			List<WebElement> elements = pf.getBrowserActionInstance(ob).getElements(LocatorName);
			for (WebElement webElement : elements) {
				String Temp = webElement.getText().replaceAll("%", ""); // here
																		// the
																		// value
																		// is
																		// correct
																		// (625.30)
				Temp = Temp.replaceAll("RELEVANCE: ", "");
				str.add(Double.valueOf(Temp));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	public List<Date> getDateList(OnePObjectMap neonIpaResultlistRelavencesCss) {
		List<Date> str = new ArrayList<Date>();
		try {
			List<WebElement> elements = pf.getBrowserActionInstance(ob).getElements(neonIpaResultlistRelavencesCss);
			for (WebElement webElement : elements) {
				String Temp = webElement.getText();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = df.parse(Temp);
				str.add(startDate);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	public List<String> getStringList(OnePObjectMap neonIpaResultlistAssigneesCss) {
		List<String> str = new ArrayList<String>();
		try {
			List<WebElement> elements = pf.getBrowserActionInstance(ob).getElements(neonIpaResultlistAssigneesCss);
			for (WebElement webElement : elements) {
				String Temp = webElement.getText().replaceAll("ASSIGNEE: ", "").replaceAll("ULTIMATE PARENT: ", "");
				str.add(Temp);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	public List<Double> getPatentStrength(int i) {
		List<Double> str = new ArrayList<Double>();
		try {

			for (int x = 0; x < i; x++) {
				WebElement elements = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.NEON_IPA_RECORDVIEW_STRENGTH_CSS);
				str.add(Double.valueOf(elements.getText()));
				String before = pf.getBrowserActionInstance(ob)
						.getElement(OnePObjectMap.NEON_IPA_RECORDVIEW_CURRENTPAGE_CSS).getText();
				pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_RECORDVIEW_NEXT_CSS);
				for (int z = 0; z < 10; z++) {
					String after = pf.getBrowserActionInstance(ob)
							.getElement(OnePObjectMap.NEON_IPA_RECORDVIEW_CURRENTPAGE_CSS).getText();
					if (!before.equals(after))
						break;
					Thread.sleep(1000);
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	public List<Date> getExpiryDates(int i) {
		List<Date> str = new ArrayList<Date>();
		try {

			List<WebElement> elements = ob.findElements(By.cssSelector("span[ng-hide*='expiration']"));
			for (int x = 0; x < i; x++) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = df.parse(elements.get(x).getText().split(":")[1]);
				str.add(startDate);
				/*
				 * String before = pf.getBrowserActionInstance(ob)
				 * .getElement(OnePObjectMap.NEON_IPA_RECORDVIEW_CURRENTPAGE_CSS
				 * ).getText();
				 * pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.
				 * NEON_IPA_RECORDVIEW_NEXT_CSS);
				 * 
				 * for (int z = 0; z < 10; z++) { String after =
				 * pf.getBrowserActionInstance(ob)
				 * .getElement(OnePObjectMap.NEON_IPA_RECORDVIEW_CURRENTPAGE_CSS
				 * ).getText(); if (!before.equals(after)) break;
				 * Thread.sleep(1000); }
				 */

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	public String getMD5() throws Exception {
		String pagesource = ob.getPageSource();

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(pagesource.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		System.out.println("Digest(in hex format):: " + hexString.toString());
		return hexString.toString();
	}

	public void sortByText(String string) throws Exception {
		String before = getMD5();
		new Select(pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.NEON_IPA_DASH_PAT_SEL_DD_CSS))
				.selectByVisibleText(string);
		int i = 0;
		do {
			Thread.sleep(5000);
			String after = getMD5();
			if (!before.equals(after))
				break;
			i++;
		} while (i < 20);

		before = getMD5();
		i = 0;
		do {
			Thread.sleep(5000);
			String after = getMD5();
			if (before.equals(after))
				break;
			i++;
		} while (i < 20);
		pf.getBrowserWaitsInstance(ob).waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.NEON_IPA_RESULTLIST_PDF_LINKS_CSS.toString()), 30);
		Thread.sleep(10000);

	}

	public void selectTab(String string) throws Exception {
		String css = "div[event-category*='" + string + "']";
		pf.getBrowserActionInstance(ob).jsClick(ob.findElement(By.cssSelector(css)));
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		try {
			pf.getBrowserWaitsInstance(ob).waitForElementTobeInvisible(ob,
					By.cssSelector("div[class*='wui-spinner__circle wui-spinner__circle--delayed']"), 120);
		} catch (Exception ex) {
			System.out.println("visualization not loaded in 2 mins");
		}
	}

	public void selectViewBy(String string) throws Exception {
		if (!string.equals("Basic")) {
			pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.NEON_IPA_VIEWBY_Publication_CSS);
			pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		}
		String css = "button[ng-class*='" + string + "']";
		pf.getBrowserActionInstance(ob).jsClick(ob.findElement(By.cssSelector(css)));
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);

	}

	public String deleteAnySearchTerm() throws Exception {
		String css = OnePObjectMap.NEON_IPA_DASH_SEARCHTERMS_CSS.toString();
		String text = css + ">div:nth-of-type(1)";
		String Delete = css + ">div:nth-of-type(2)";
		String SelectedText = "";
		List<WebElement> ele = ob.findElements(By.cssSelector(Delete));
		List<WebElement> ele1 = ob.findElements(By.cssSelector(text));
		int ran = new Random().nextInt(ele.size());
		SelectedText = ele1.get(ran).getText();
		pf.getBrowserActionInstance(ob).jsClick(ele.get(ran));
		return SelectedText;
	}

	public int getSearchCount() throws Exception {
		return pf.getBrowserActionInstance(ob).getElements(OnePObjectMap.NEON_IPA_DASH_SEARCHTERMS_CSS).size();
	}

	public void addSearchTerm(String Term) throws Exception {
		pf.getBrowserActionInstance(ob).enterFieldValue(OnePObjectMap.NEON_IPA_SEARCH_TAB_TEXT_CSS, Term);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		String xpath = OnePObjectMap.NEON_IPA_SEARCH_TAB_ADD_TERM_XPATH.toString().replace("$value", Term);
		pf.getBrowserActionInstance(ob).jsClick(ob.findElement(By.xpath(xpath)));
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);

	}
}

class company {
	String Company;
	String Value;
	String PublicationCount;
	String Color;
	String Size;

	public String getCompany() {
		return Company;
	}

	public company(String company, String value, String publicationCount, String color, String size) {
		Company = company;
		Value = value;
		PublicationCount = publicationCount;
		Color = color;
		Size = size;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public String getPublicationCount() {
		return PublicationCount;
	}

	public void setPublicationCount(String publicationCount) {
		PublicationCount = publicationCount;
	}
}
