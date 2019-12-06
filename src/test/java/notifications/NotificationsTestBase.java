package notifications;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import base.TestBase;
import pages.PageFactory;
import util.BrowserWaits;
import util.RowData;
import util.TestUtil;

public class NotificationsTestBase extends TestBase {

	protected static final String TOKENIZER_DOUBLE_PIPE = "||";
	PageFactory pf = new PageFactory();
	int count = 0;
	protected static boolean suiteRunmode = false;
	protected static Map<String, RowData> testcase = new HashMap<String, RowData>();
	private static final int TESTDATA_COLUMN_COUNT = 4;
	private static final Object EMPTY_STRING = "";
	protected RowData rowData = null;

	@BeforeSuite
	public void beforeSuite(ITestContext ctx) throws Exception {
		logger.info("Notification UI automation starting time - " + new Date());
		String suiteName = ctx.getSuite().getName();
		initialize();
		suiteRunmode = TestUtil.isSuiteRunnable(suiteXls, "Notifications");
		logger.info("Notifications Suit runmode is - " + suiteRunmode);
		logger.info("Notification UI automation Running environment - " + host);
		readNotifications();
		logger.info(testcase.size());
		if (!StringUtils.containsIgnoreCase(host, "https://projectne.thomsonreuters.com")
				&& !(suiteName.equals("Sanity suite"))) {
			try {
				 createNewUsers();
			} catch (Exception e) {

			}

			if (StringUtils.containsIgnoreCase(host, "https://dev-stable.1p.thomsonreuters.com")) {
				if (user1 == null) {
					user1 = "41zbbp+5s285rol6idz4@sharklasers.com";
					fn1 = "ynowdhms";
					ln1 = "ynowdhmsbi";
					count++;
				}
				if (user2 == null) {
					user2 = "41zbhb+2hb552i1rsv3w@sharklasers.com";
					fn2 = "iuybquao";
					ln2 = "iuybquaoek";
					count++;
				}
				if (user3 == null) {
					user3 = "41zbn1+3qz12vzogmfcg@sharklasers.com";
					fn3 = "xouoygfm";
					ln3 = "xouoygfmyk";
					count++;
				}
			}
			if (count < 3)
				followUsers();
		}
	}
	@BeforeClass
	public void  beforeClass(){
		logger=  LogManager.getLogger(this.getClass().getSimpleName());
	}

	protected boolean getTestRunMode(String testRunmode) {
		// String testRunmode = testcase.get(simpleName).getTestcaseRunmode();
		if ("Y".equals(testRunmode)) {
			return true;
		} else {
			return false;
		}

	}

	private void readNotifications() throws Exception {
		XSSFWorkbook workBook = null;
		FileInputStream inputStream = null;
		try {
			int sheetRowCount;
			XSSFSheet sheet = null;
			XSSFRow row = null;
			// String sheetName = null;

			// Read Excel file
			File myxl = new File(notificationxls.path);
			inputStream = new FileInputStream(myxl);
			workBook = new XSSFWorkbook(inputStream);
			int totalSheets = workBook.getNumberOfSheets();
			// Loop through each sheet in the Excel
			for (int currentSheet = 0; currentSheet < totalSheets; currentSheet++) {

				// Get current sheet information
				sheet = workBook.getSheetAt(currentSheet);
				// sheetName = workBook.getSheetName(currentSheet);
				sheetRowCount = sheet.getLastRowNum();
				// Loop through all test case records of current sheet, start
				// with 1 to leave header.
				for (int i = 1; i <= sheetRowCount; i++) {
					// Get current row information
					row = sheet.getRow(i);
					rowData = getRowData(row);
					// logger.info(rowData);
					testcase.put(rowData.getTestclassName(), rowData);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while executing the tests:" + e);
			e.printStackTrace();
		} finally {
			inputStream.close();
		}

	}

	/**
	 * This function will convert an object of type excel cell to a string value
	 * 
	 * @param cell excel cell
	 * @return the cell value
	 */
	protected String getCellData(XSSFCell cell) {
		int type = cell.getCellType();
		Object result;
		switch (type) {
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				result = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA:
				throw new RuntimeException("We can't evaluate formulas in Java");
			case Cell.CELL_TYPE_BLANK:
				result = EMPTY_STRING;
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				result = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:
				throw new RuntimeException("This cell has an error");
			default:
				throw new RuntimeException("We don't support this cell type: " + type);
		}
		return result.toString();
	}

	protected RowData getRowData(XSSFRow row) throws Exception {

		RowData rowData = new RowData();
		String currentCellData = null;

		for (int currentCell = 0; currentCell < TESTDATA_COLUMN_COUNT; currentCell++) {

			currentCellData = getCellData(row.getCell(currentCell, Row.CREATE_NULL_AS_BLANK));

			switch (currentCell) {
				case 0:
					rowData.setTestclassName(currentCellData);
				case 1:
					rowData.setTestcaseId(currentCellData);
				case 2:
					rowData.setTestcaseDescription(currentCellData);
				case 3:
					rowData.setTestcaseRunmode(currentCellData);
				case 4:
					rowData.setTestResults(currentCellData);
			}
		}
		return rowData;
	}

	@AfterSuite
	public void afterSuite() {
		extent.flush();
		logger.info("Notification UI automation ending time - " + new Date());
	}

	public void followUsers() {
		// user1 following user2
		try {
			if (user1 != null && user2 != null) {
				openBrowser();
				maximizeWindow();
				clearCookies();
				ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				// login with user1
				pf.getLoginTRInstance(ob).enterTRCredentials(user1, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(fn2 + " " + ln2);
				// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(fn2
				// + " " + ln2);
				if (pf.getSearchProfilePageInstance(ob).getPeopleCount() > 0) {
					pf.getSearchProfilePageInstance(ob).clickPeople();
					pf.getSearchProfilePageInstance(ob).followProfileFromSeach();
				}
				pf.getLoginTRInstance(ob).logOutApp();
				// closeBrowser();
			} else {
				throw new Exception("User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			logger.info(t.getMessage());
		}

		try {
			if (user2 != null && user3 != null) {
				// openBrowser();
				// maximizeWindow();
				// clearCookies();
				// ob.navigate().to(host);
				pf.getLoginTRInstance(ob).waitForTRHomePage();
				pf.getLoginTRInstance(ob).enterTRCredentials(user2, CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob, By.xpath(OR.getProperty("searchBox_textBox")), 30);
				pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(fn3 + " " + ln3);
				if (pf.getSearchProfilePageInstance(ob).getPeopleCount() > 0) {
					pf.getSearchProfilePageInstance(ob).clickPeople();
					pf.getSearchProfilePageInstance(ob).followProfileFromSeach();
				}
				pf.getLoginTRInstance(ob).logOutApp();
				closeBrowser();
			} else {
				throw new Exception("User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			logger.info(t.getMessage());
		}
	}

	public void createNewUsers() {
		if (!StringUtils.containsIgnoreCase(host, "https://projectne.thomsonreuters.com")) {
			if (TestUtil.isSuiteRunnable(suiteXls, "Notifications")) {
				while (flag < 3) {
					try {
						if (user1 != null && user2 != null && user2 != null) {
							flag = 3;
						}
						// 1)Creating User1
						if (user1 == null) {
							logger.info("Started user1 creation");
							openBrowser();
							maximizeWindow();
							clearCookies();
							fn1 = generateRandomName(8);
							ln1 = generateRandomName(10);
							user1 = createNewUser(fn1, ln1);
							logger.info("User1 Mail id:" + user1);
							logger.info("User1 Name:" + fn1 + " " + ln1);
							BrowserWaits.waitTime(3);
							logout();
							closeBrowser();
							logger.info("user1 created successfully");
						}
						// Creating User2
						if (user2 == null) {
							logger.info("Started user2 creation");
							openBrowser();
							maximizeWindow();
							clearCookies();
							fn2 = generateRandomName(8);
							ln2 = generateRandomName(10);
							user2 = createNewUser(fn2, ln2);
							logger.info("User2 Mail id:" + user2);
							logger.info("User2 Name:" + fn2 + " " + ln2);
							BrowserWaits.waitTime(3);
							logout();
							closeBrowser();
							logger.info("user2 created successfully");
						}
						// Creating User3
						if (user3 == null) {
							logger.info("Started user3 creation");
							openBrowser();
							maximizeWindow();
							clearCookies();
							fn3 = generateRandomName(8);
							ln3 = generateRandomName(10);
							user3 = createNewUser(fn3, ln3);
							logger.info("User3 Mail id:" + user3);
							logger.info("User3 Name:" + fn3 + " " + ln3);
							BrowserWaits.waitTime(3);
							logout();
							closeBrowser();
							logger.info("user3 created successfully");
						}
					} catch (Throwable t) {
						logger.error("There is some problem in the creation of users");
						logger.error(t);
						t.printStackTrace();
						// StringWriter errors = new StringWriter();
						// t.printStackTrace(new PrintWriter(errors));
						try {
							test.addScreenCapture(captureScreenshot("UserCreationError"
									+ this.getClass().getSimpleName() + "_user_creation_error_screenshot"));// screenshot
						} catch (Throwable t1) {
							logger.error("There is some problem in the taking sereenshoot");
							logger.error(t);
							t.printStackTrace();
						}
						closeBrowser();
					}
					flag++;
				}

			}
		}

	}

}
