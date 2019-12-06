package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.BrowserWaits;
import util.OnePObjectMap;
import base.TestBase;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class IPARecordViewPage extends TestBase {

	public IPARecordViewPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	public String getTitle() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(OnePObjectMap.IPA_RECORD_VIEW_PAGE_PATENT_TITLE_CSS);
		String title = pf.getBrowserActionInstance(ob).getElement(OnePObjectMap.IPA_RECORD_VIEW_PAGE_PATENT_TITLE_CSS)
				.getText();
		return title;

	}

	public void validateAdditionalInfoTile(ExtentTest test) throws Exception {

		String[] elname = {"DWPI Accession Number", "Application Number", "Application Date", "Assignee", "Current",
				"Ultimate Parent", "Patent Strength", "Priority Date", "Expiry Date (Est)"};
		pf.getBrowserWaitsInstance(ob).waitUntilText("ADDITIONAL INFORMATION");
		List<WebElement> list1 = ob.findElements(By.cssSelector(OnePObjectMap.IPA_RECORD_VIE_PAGE_PATENTS_INFO_CSS
				.toString()));
		int i = 0;
		for (WebElement we : list1) {

			List<WebElement> eleSPan = we.findElements(By.tagName("span"));
			String str = eleSPan.get(0).getText();
			String str1 = eleSPan.get(1).getText();
			if (str.contains(elname[i]) && !str1.equals("null")) {
				test.log(LogStatus.PASS, "Additional info field like " + str + " is dispalying in record view page");
				i = i + 1;
			} else
				i = i + 1;

		}
		List<WebElement> ll = ob.findElements(By.cssSelector(OnePObjectMap.IPA_RECORD_VIEW_PAGE_PANTENTS_RHS_TILE_CSS
				.toString()));
		if (ll.get(1).getText().equalsIgnoreCase("LEGAL STATUS") && ll.get(2).getText().equalsIgnoreCase("ORIGINAL")) {
			test.log(LogStatus.PASS, "Legal Status  and Original Tiles are present in Patent Record view page");
		} else
			test.log(LogStatus.FAIL, "Legal Status  and Original Tiles are not present in Patent Record view page");

	}

	public String clickOnPatentTitle(int val) throws Exception {
		ob.findElements(By.cssSelector(OnePObjectMap.NEON_IPA_RECORD_LIST_PAGE_PATENT_TITLE_CSS.toString()));
		List<WebElement> list = pf.getBrowserActionInstance(ob).getElements(
				OnePObjectMap.NEON_IPA_RECORD_LIST_PAGE_PATENT_TITLE_CSS);
		    val=val-1;
		if (val >= 0 && val <= list.size()) {
			String title = "title"; //list.get(val).getText();
			list.get(val).click();
			return title;
		} else
			throw new Exception("Patent Titles are not found");
	}

	public void clickOnOriginalPatent(ExtentTest test) throws Exception {
		String pubNum = ob.findElement(By.xpath("//span[@class='ng-binding'][1]")).getText();
		String[] tmp = pubNum.split(": ");
		pubNum = tmp[1];
		int i;
		
		for(i=pubNum.length()-1; i>=0; i--){
			if(Character.isAlphabetic(pubNum.charAt(i))){
				break;
			}
		}
				
		String Num = pubNum.substring(0, i);
		
		pf.getBrowserWaitsInstance(ob).waitUntilText("ORIGINAL");
		String winHandleBefore = ob.getWindowHandle();
		pf.getBrowserActionInstance(ob).click(OnePObjectMap.IPA_RECORD_VIEW_PAGE_PATENT_PDF_LINK_CSS);
		pf.getBrowserWaitsInstance(ob).waitForAjax(ob);
		for (String winHandle : ob.getWindowHandles()) {
			ob.switchTo().window(winHandle);
		}

		BrowserWaits.waitTime(6);
		String url = ob.findElement(By.cssSelector("embed[id='plugin']")).getAttribute("src");
		
		if(url.contains(Num))
			test.log(LogStatus.INFO, "PDF is opened successfully ");
		else
			test.log(LogStatus.FAIL, "PDF is not opened successfully ");
		ob.close();
		ob.switchTo().window(winHandleBefore);

	}

	public boolean checkInfo() throws Exception {
	
		pf.getBrowserActionInstance(ob).scrollToElement(OnePObjectMap.IPA_RECORD_VIEW_MORE_LINK);
		List<WebElement> ll = ob.findElements(By.cssSelector(OnePObjectMap.IPA_RECORD_VIE_PAGE_VALIDATE_TEXT_INFO_CSS
				.toString()));
		if (ll.get(0).getText().equalsIgnoreCase("Abstract") && ll.get(2).getText().equalsIgnoreCase("Claims")
				&& ll.get(3).getText().equalsIgnoreCase("Description)"))
			return true;
		else
			return false;
	}
}
