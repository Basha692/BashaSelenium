package util;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.TestBase;

/**
 * Class for Perform Browser Waits
 * 
 * @author CP00421684
 *
 */
public class BrowserWaits extends TestBase {

	/**
	 * wait until Element Present
	 * 
	 * @param ElementPath
	 * @return boolean
	 */
	static int time = 90;
	static String locatorType;
	static String locatorText;

	// WebDriver ob;
	public BrowserWaits(WebDriver ob) {
		this.ob = ob;
	}
	/**
	 * An expectation for checking that there is at least one element present on a web page.
	 * @param elementName
	 * @throws Exception, Something unexpected 
	 */
	public void waitForAllElementsToBePresent(Object elementName) throws Exception {
		List<WebElement> elements = getLocators(((Enum<?>) elementName).name(), elementName.toString());
		if (!(elements.size() > 0)) {
			throw new Exception("Unable to find Element...Element is not present");
		}
	}

	/**
	 * wait until desired element is displayed
	 * 
	 * @param locator
	 * @throws Exception
	 */
	public void waitUntilElementIsDisplayed(Object elementName) throws Exception {
		WebElement element = getLocator(((Enum<?>) elementName).name(), elementName.toString());
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(element != null && element.isDisplayed());
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Failed to find element [Locator = {" + locatorText + "}], after waiting for " + time + "ms");
		}

	}

	/**
	 * wait until desired element is displayed with mentioned time
	 * 
	 * @param locator
	 * @throws Exception
	 */
	public void waitUntilElementIsDisplayed(Object elementName,final int time) throws Exception {
		WebElement element = getLocator(((Enum<?>) elementName).name(), elementName.toString());
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(element != null && element.isDisplayed());
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Failed to find element [Locator = {" + locatorText + "}], after waiting for " + time + "ms");
		}

	}

	
	/**
	 * wait until desired element is Clickable
	 * 
	 * @param locator
	 * @throws Exception
	 */
	public void waitUntilElementIsClickable(Object elementName) throws Exception {
		WebElement element = getLocator(((Enum<?>) elementName).name(), elementName.toString());
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(element != null && element.isEnabled());
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Failed to find element [Locator = {" + locatorText + "}], after waiting for " + time + "ms");
		}
	}

	/**
	 * wait until desired element is Clickable with Time
	 * 
	 * @param locator
	 * @throws Exception
	 */
	public void waitUntilElementIsClickable(Object elementName, int time) throws Exception {
		WebElement element = getLocator(((Enum<?>) elementName).name(), elementName.toString());
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(element != null && element.isEnabled());
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Failed to find element [Locator = {" + locatorText + "}], after waiting for " + time + "ms");
		}
	}

	/**
	 * wait until desired element is not displayed
	 * 
	 * @param locator
	 * @throws Exception
	 */
	public void waitUntilElementIsNotDisplayed(Object elementName) throws Exception {
		WebElement element = getLocator(((Enum<?>) elementName).name(), elementName.toString());
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(element == null || !element.isDisplayed());
					} catch (Exception e) {
						return Boolean.valueOf(true);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Failed to find element [Locator = {" + locatorText + "}], after waiting for " + time + "ms");
		}

	}

	/**
	 * Wait for Expected PageTitle
	 * 
	 * @param title
	 */
	public void waitUntilPageTitle(final String title) {
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(d.getTitle().trim().equals(title));
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException("Page title mismatch. Expected: " + title + "; actual: " + ob.getTitle()
					+ ", after waiting for " + time + "ms");
		}
	}

	public void waitUntilText(final String... text) {
		for (String each : text) {
			waitUntilText(each);
		}
	}

	/**
	 * wait for until expected text present
	 * 
	 * @param text
	 */
	public void waitUntilText(final String text) {
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(d.getPageSource().contains(text));
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException("Failed to find text: " + text + ", after waiting for " + time + "ms");
		}
	}
	
	
	public void waitUntilText(final String text,int time) {
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(d.getPageSource().contains(text));
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException("Failed to find text: " + text + ", after waiting for " + time + "ms");
		}
	}

	/***********************
	 * Wait until specified text is not present
	 **********************/

	public void waitUntilNotText(final String... text) throws Exception {
		for (String each : text) {
			waitUntilNotText(each, time);
		}
	}

	public void waitUntilNotText(final String text, final int time) {
		try {
			(new WebDriverWait(ob, time)).until(new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver d) {
					try {
						return Boolean.valueOf(!d.getPageSource().contains(text));
					} catch (Exception e) {
						return Boolean.valueOf(false);
					}
				}
			});
		} catch (TimeoutException te) {
			throw new TimeoutException(
					"Text: " + text + " is present which is not expected, after waiting for " + time + "ms");
		}
	}

	/**
	 * wait for provided time
	 * 
	 * @param secs
	 */
	public static void waitTime(final int secs) throws InterruptedException {
		Thread.sleep(secs * 1000);
	}

	/**
	 * wait for provided time
	 * 
	 * @author UC225218
	 * @param secs
	 */
	public static void waitTime(final double secs) throws InterruptedException {
		Thread.sleep((long) (secs * 1000));
	}

	public WebElement getLocator(String locatorType, String locatorText) throws Exception {
		WebElement ele = null;

		try {
			if (locatorType.endsWith("_CSS")) {
				ele = ob.findElement(By.cssSelector(locatorText));
			} else if (locatorType.endsWith("_XPATH")) {
				ele = ob.findElement(By.xpath(locatorText));
			} else if (locatorType.endsWith("_LINK")) {
				ele = ob.findElement(By.linkText(locatorText));
			} else if (locatorType.endsWith("_PLINK")) {
				ele = ob.findElement(By.partialLinkText(locatorText));
			} else if (locatorType.endsWith("_ID")) {
				ele = ob.findElement(By.id(locatorText));
			} else if (locatorType.endsWith("_CLASS")) {
				ele = ob.findElement(By.className(locatorText));
			} else if (locatorType.endsWith("_TAG")) {
				ele = ob.findElement(By.tagName(locatorText));
			} else if (locatorType.endsWith("_NAME")) {
				ele = ob.findElement(By.name(locatorText));
			} else {
				throw new Exception("Unable to handle the locator type: " + locatorType
						+ ". Locator name should end with _ID/_NAME/" + "_CLASS/_CSS/_LINK/_PLINK/_TAG/_XPATH");
			}
		} catch (NoSuchElementException nse) {
			throw new NoSuchElementException("Unable to locate the element" + locatorType + "=" + locatorText);
		}
		return ele;

	}
	
	
	public List<WebElement> getLocators(String locatorType, String locatorText) {
		List<WebElement> ele = null;

		try {
			if (locatorType.endsWith("_CSS")) {
				ele = ob.findElements(By.cssSelector(locatorText));
			} else if (locatorType.endsWith("_XPATH")) {
				ele = ob.findElements(By.xpath(locatorText));
			} else if (locatorType.endsWith("_LINK")) {
				ele = ob.findElements(By.linkText(locatorText));
			} else if (locatorType.endsWith("_PLINK")) {
				ele = ob.findElements(By.partialLinkText(locatorText));
			} else if (locatorType.endsWith("_ID")) {
				ele = ob.findElements(By.id(locatorText));
			} else if (locatorType.endsWith("_CLASS")) {
				ele = ob.findElements(By.className(locatorText));
			} else if (locatorType.endsWith("_TAG")) {
				ele = ob.findElements(By.tagName(locatorText));
			} else if (locatorType.endsWith("_NAME")) {
				ele = ob.findElements(By.name(locatorText));
			} else {
				throw new NoSuchElementException("Unable to handle the locator type: " + locatorType
						+ ". Locator name should end with _ID/_NAME/" + "_CLASS/_CSS/_LINK/_PLINK/_TAG/_XPATH");
			}

		} catch (NoSuchElementException nse) {
			throw new NoSuchElementException("Unable to locate the element" + locatorType + "=" + locatorText);
		}
		return ele;
	}

	public void waitForTRHomePage() throws InterruptedException {
		waitForPageLoad(ob);
	}

	public void waitUntilTextPresent(String locator, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(ob, time);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(locator), text));
		} catch (TimeoutException e) {
			throw new TimeoutException("Failed to find element Locator , after waiting for " + time + "ms");
		}
	}
}
