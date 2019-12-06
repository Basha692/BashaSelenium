package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import base.TestBase;

/**
 * This class contains all the method related to account page
 * @author uc205521
 *
 */
public class AccountPage extends TestBase {

	PageFactory pf;

	public AccountPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}


	public boolean verifyLinkedAccount(String accountType, String emailId) {
		boolean result = false;
		waitForAllElementsToBePresent(ob, By.cssSelector("div[class='account-option-item ng-scope']"), 60);
		List<WebElement> list = ob.findElements(By.cssSelector("div[class='account-option-item ng-scope']"));

		for (WebElement element : list) {
			String type = null;
			List<WebElement> elementList = element
					.findElements(By.cssSelector("div[class='account-option-item ng-scope'] h6 a"));
			for (WebElement we : elementList) {
				if (we.isDisplayed()) {
					type = we.getText();
					break;
				}
			}

			if ((accountType.equalsIgnoreCase("Facebook") && type.equalsIgnoreCase("change password"))
					|| accountType.equalsIgnoreCase(type.trim())) {
				String emailid = null;
				emailid = element.findElement(By.cssSelector("span[class='ng-binding']")).getText();
				if (emailid.equalsIgnoreCase(emailId))
					result = true;
				break;
			}

		}
		return result;
	}


	public boolean validateAccountsCount(int accountCount) {
		waitForAllElementsToBePresent(ob, By.cssSelector("div[class='account-option-item ng-scope']"), 60);
		List<WebElement> list = ob.findElements(By.cssSelector("div[class='account-option-item ng-scope']"));
		
		return accountCount==list.size();
	}

}
