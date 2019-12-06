package Authoring;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.OnePObjectMap;

/**
 * 
 * This class contains common methods that used in authoring tests.
 *
 */
public class Authoring extends TestBase {

	static int commentSizeBeforeAdd;
	static int commentSizeAfterAdd;
	PageFactory pf;
	static int time = 15;

	public Authoring(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	/**
	 * Method to return comment count from article record view
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public int getCommentCount() throws InterruptedException {
		BrowserWaits.waitTime(2);
		//Commented by KR
		waitForPageLoad(ob);
		//waitForAjax(ob);
		//scrollingToElementofAPage();
		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString()),
				180);
		String commentSizeBeforeAdd = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_COUNT_CSS.toString())).getText()
				.replaceAll(",", "").trim();
		return Integer.parseInt(commentSizeBeforeAdd);
	}

	/**
	 * Method to enter the text into comment text area
	 * 
	 * @param addComments
	 * @throws InterruptedException
	 */
	public void enterArticleComment(String addComments) throws InterruptedException {
		commentSizeBeforeAdd = getCommentCount();
		System.out.println("Before-->" + commentSizeBeforeAdd);
		BrowserWaits.waitTime(8);

//		WebElement commentArea = ob
//				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()));
//		System.out.println("Attribute-->" + commentArea.getAttribute("fr-placeholder"));
//		// jsClick(ob,commentArea);
//
//		// Used points class to get x and y coordinates of element.
//		Point point = commentArea.getLocation();
//		// int xcord = point.getX();
//		int ycord = point.getY();
//		ycord = ycord + 200;
//		JavascriptExecutor jse = (JavascriptExecutor) ob;
//		jse.executeScript("scroll(0," + ycord + ");");
//		BrowserWaits.waitTime(2);
//		jsClick(ob, commentArea);
//		commentArea.clear();
		
		WebElement commentArea = ob.findElement(By.xpath("//textarea[@placeholder='Join the discussion']"));
		commentArea.click();
		BrowserWaits.waitTime(2);
		WebElement innerTextBox = ob.findElement(By.xpath("//div[@class='fr-element fr-view']"));
		String comment = addComments + RandomStringUtils.randomNumeric(3);
		innerTextBox.sendKeys(comment);
		// new
		// Actions(ob).moveToElement(commentArea).sendKeys(addComments).build().perform();
		Thread.sleep(1000);// after entering the comments wait for submit button
							// to get enabled or disabled
	}

	public void enterArticleComments(String addComments) throws InterruptedException {
		commentSizeBeforeAdd = getCommentCount();
		System.out.println("Before-->" + commentSizeBeforeAdd);
		scrollingToElementofAPage();
		BrowserWaits.waitTime(5);
		//commented by KR
//		ob
//		.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTAREA_CSS.toString())).click();
//		WebElement commentArea = ob
//				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()));
//		System.out.println("Attribute-->" + commentArea.getAttribute("placeholder"));
//		commentArea.clear();
		
		// Added by KR
		WebElement commentArea = ob.findElement(By.xpath("//textarea[@placeholder='Join the discussion']"));
		commentArea.click();
		BrowserWaits.waitTime(1);
		WebElement innerTextBox = ob.findElement(By.xpath("//div[@class='fr-element fr-view']"));
		innerTextBox.clear();
		for(int i=0;i<addComments.length();i++){
			innerTextBox.sendKeys(addComments.charAt(i)+"");
			Thread.sleep(10);
			}
		
		Thread.sleep(100);// after entering the comments wait for submit button
							// to get enabled or disabled
	}
	
	public void cancelComment(){
		waitForElementTobeClickable(ob,
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_CANCEL_COMMENT_BUTTON_CSS.toString()), 60);
		ob
		.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_CANCEL_COMMENT_BUTTON_CSS.toString())).click();	
	}

	public void clickAddCommentButton() throws InterruptedException {
		scrollingToElementofAPage();
		waitForElementTobeClickable(ob,
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_ADD_COMMENT_BUTTON_CSS.toString()), 60);
		WebElement addCommentElement = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_ADD_COMMENT_BUTTON_CSS.toString()));
		JavascriptExecutor executor = (JavascriptExecutor) ob;
		executor.executeScript("arguments[0].click();", addCommentElement);
		waitForAjax(ob);
	}

	public void validateCommentAdd(ExtentTest test, int expCommentCount) throws Exception {
		int commentCount = getCommentCount();

		//Commented by KR
		//System.out.println("After-->" + commentSizeAfterAdd);
		System.out.println("After-->" + commentCount);
		test.log(LogStatus.INFO, "before-->" + expCommentCount);
		test.log(LogStatus.INFO, "After-->" + commentCount);
		if (!(commentCount > expCommentCount)) {
			throw new Exception("Entered Comment not updated");
		} else {
			test.log(LogStatus.PASS, "Comment count validation passed");
		}
	}

	public void updateComment(ExtentTest test, String steComment) throws Exception {
		//commented by KR
		BrowserWaits.waitTime(3);
		scrollingToElementofAPage();
		waitForElementTobeVisible(ob,
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_BUTTON_CSS.toString()), 40);
		WebElement editCommentElement = ob
				.findElement(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_BUTTON_CSS.toString()));
		JavascriptExecutor exe = (JavascriptExecutor) ob;
		exe.executeScript("arguments[0].click();", editCommentElement);

		List<WebElement> commentArea = ob
				.findElements(By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_TEXTBOX_CSS.toString()));
		System.out.println("no of comment areas enabled-->" + commentArea.size());
		commentArea.get(1).clear();
		commentArea.get(1).sendKeys(steComment);
		
		//commented by KR
		BrowserWaits.waitTime(2);
		List<WebElement> subButtons = ob.findElements(
				By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_EDIT_SUBMIT_BUTTON_CSS.toString()));
		System.out.println("Buttons available---2--->" + subButtons);
		for (WebElement subButton : subButtons) {
			System.out.println("Button Text-->" + subButton.getText());
			if (subButton.getText().trim().equalsIgnoreCase("Submit")) {
				JavascriptExecutor executor = (JavascriptExecutor) ob;
				//Added by KR
				//scrollElementIntoView(ob,subButton);
				
				executor.executeScript("arguments[0].click();", subButton);
				waitForPageLoad(ob);
				break;
			}
		}
		BrowserWaits.waitTime(3);
		test.log(LogStatus.INFO, "Comment is updated");
	}

	public void validateUpdatedComment(String updatedComments) throws Exception {
		scrollingToElementofAPage();
		String commentText = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString())).get(0)
				.getText();
		System.out.println("Commentary Text-->" + commentText);
		if (!(commentText.contains(updatedComments) && commentText.contains("EDITED"))) {
			new Authoring1().status = 2;
			throw new Exception("Updated " + updatedComments + " not present");
		}
	}

	/**
	 * Method for Validate the Article Appreciation functionality
	 * 
	 * @throws Exception,
	 *             When Validation not done
	 */
	public void validateAppreciationComment(ExtentTest test) throws Exception {
		waitForAjax(ob);
		waitForAllElementsToBePresent(ob,
				By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString()), 120);
		List<WebElement> apprDivs = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString()));
		System.out.println("size of total elemntes-->" + apprDivs.size());
		WebElement apprSubDivs;
		for (int i = 0; i < apprDivs.size(); i++) {
			try {

				apprSubDivs = apprDivs.get(i)
						.findElement(By.cssSelector("div[class='ne-comment-list__comment-footer']"))
						.findElement(By.cssSelector("div[class^='ne-comment-list__comment-footer-item']"));

				// List<WebElement>
				// apprSubDivs=apprDivs.get(0).findElements(By.cssSelector("div.row")).get(0).findElements(By.cssSelector("div[class^='col-xs-']"));
				String count = apprSubDivs
						.findElement(
								By.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_MATRICS_COUNT_CSS.toString()))
						.getText();
				System.out.println("app sub divs-->" + count);
				scrollingToElementofAPage();

			} catch (Exception e) {
				continue;
			}

			int apprEarCount = Integer
					.parseInt(apprSubDivs
							.findElement(By
									.cssSelector(OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_MATRICS_COUNT_CSS.toString()))
							.getText().replaceAll(",", "").trim());
			System.out.println("Before count-->" + apprEarCount);

			String attrStatus = apprSubDivs.findElement(By.tagName("button")).getAttribute("ng-click");
			System.out.println("Attribute Status-->" + attrStatus);

			if (attrStatus.contains("DOWN")) {
				scrollingToElementofAPage();
				// apprSubDivs.findElement(By.tagName("button")).click();
				JavascriptExecutor exe = (JavascriptExecutor) ob;
				exe.executeScript("arguments[0].click();", apprSubDivs.findElement(By.tagName("button")));
				Thread.sleep(4000);// After clicking on like button wait for
									// status to change and count update
				int apprAftCount = Integer
						.parseInt(apprSubDivs
								.findElement(By.cssSelector(
										OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_MATRICS_COUNT_CSS.toString()))
								.getText().replaceAll(",", "").trim());
				System.out.println("Already liked  After count-->" + apprAftCount);
				if (!(apprAftCount < apprEarCount)) {
					// status=2;
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "Comment Appreciation not happended")));
					throw new Exception("Comment Appreciation not happended");
				} else {
					test.log(LogStatus.PASS, "Apreciate functionality working fine for comments");
				}
			} else if (attrStatus.contains("UP")) {
				scrollingToElementofAPage();
				// apprSubDivs.findElement(By.tagName("button")).click();
				JavascriptExecutor exe = (JavascriptExecutor) ob;
				exe.executeScript("arguments[0].click();", apprSubDivs.findElement(By.tagName("button")));

				Thread.sleep(4000);// After clicking on unlike button wait
									// for status to change and count update
				int apprAftCount = Integer
						.parseInt(apprSubDivs
								.findElement(By.cssSelector(
										OnePObjectMap.RECORD_VIEW_PAGE_COMMENTS_MATRICS_COUNT_CSS.toString()))
								.getText().replaceAll(",", "").trim());
				System.out.println("Not liked --After count-->" + apprAftCount);
				if (!(apprAftCount > apprEarCount)) {
					test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(
							captureScreenshot(this.getClass().getSimpleName() + "Comment Appreciation not happended")));
					throw new Exception("Comment Appreciation not happended");
				} else {
					test.log(LogStatus.PASS, "Un- apreciate functionality working fine for comments");
				}
			}
			break;
		}
	}

	public void validateViewComment(ExtentTest test, String addComments) throws Exception {
		String commentText = ob
				.findElements(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_VIEW_POST_COMMENT_CSS.toString())).get(0)
				.getText();
		if (!commentText.contains(addComments)) {
			throw new Exception("Entered " + addComments + " not present");
		} else {

			test.log(LogStatus.PASS, "Comment Text validation passed");
		}
	}

	public void scrollingToElementofAPage() throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 250);");
	}

	public void scrollingToElement(WebElement element) throws InterruptedException {
		((JavascriptExecutor) ob).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * Method to Validate Prevent Bot Comment
	 * 
	 * @throws Exception
	 */
	public void validatePreventBotComment() throws Exception {
		waitForAjax(ob);
		pf.getBrowserWaitsInstance(ob)
				.waitUntilElementIsDisplayed(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS);
		String preventBotText = pf.getBrowserActionInstance(ob)
				.getElement(OnePObjectMap.HOME_PROJECT_NEON_AUTHORING_PREVENT_BOT_COMMENT_CSS).getText();
		pf.getBrowserWaitsInstance(ob).waitUntilText(preventBotText);
		Assert.assertEquals("We are still processing your previous comment. Please try again.", preventBotText);
	}

	public void selectArtcleWithComments() {
		waitForAllElementsToBePresent(ob, By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()), 180);
		List<WebElement> itemList;

		while (true) {
			itemList = ob.findElements(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_CSS.toString()));
			int commentsCount, itr = 1;
			String strCmntCt;
			boolean isFound = false;
			for (int i = (itr - 1) * 10; i < itemList.size(); i++) {
				strCmntCt = itemList.get(i)
						.findElement(
								By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_COMMENTS_COUNT_CSS.toString()))
						.getText().replaceAll(",", "").trim();
				commentsCount = Integer.parseInt(strCmntCt);
				if (commentsCount != 0) {
					jsClick(ob, itemList.get(i)
							.findElement(By.cssSelector(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_CSS.toString())));
					isFound = true;
					break;
				}

			}

			if (isFound)
				break;
			itr++;
			((JavascriptExecutor) ob).executeScript("javascript:window.scrollBy(0,document.body.scrollHeight-150)");
			waitForAjax(ob);
		}
	}

	public void waitForTRHomePage() throws InterruptedException {
		waitForPageLoad(ob);
	}

	public void searchArticle(String article) throws InterruptedException {

		waitForElementTobeVisible(ob, By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString()), 90);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString())).clear();
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString())).sendKeys(article);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_CLICK_CSS.toString())).click();
		waitForPageLoad(ob);
		ob.findElement(By.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_SEARCH_BOX_CSS.toString())).clear();
		BrowserWaits.waitTime(3);
	}

	public void chooseArticle() throws InterruptedException {
		BrowserWaits.waitForAllElementsToBePresent(ob,
				By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString()), 180);
		jsClick(ob, ob.findElement(By.xpath(OnePObjectMap.SEARCH_RESULTS_PAGE_ITEM_TITLE_XPATH.toString())));
		waitForPageLoad(ob);
	}
	
	public void waitUntilTextPresent(String locator, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(ob, time);
			//wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(locator), text));
		} catch (TimeoutException e) {
			throw new TimeoutException("Failed to find element Locator , after waiting for " + time + "ms");
		}
	}

}
