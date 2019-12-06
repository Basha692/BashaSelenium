package base;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
// import util.ExtentManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.google.common.base.Function;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import pages.PageFactory;
import util.BrowserWaits;
import util.ErrorUtil;
import util.OnePObjectMap;
import util.RowData;
import util.TestUtil;
import util.Xls_Reader;

public class TestBase {

	static int time = 90;
	protected static Logger logger = LogManager.getLogger();
	public static Properties CONFIG = null;
	public static Properties OR = null;
	public static Properties LOGIN = null;
	public static Xls_Reader suiteXls = null;
	public static Xls_Reader iamxls = null;
	public static Xls_Reader searchxls = null;
	public static Xls_Reader authoringxls = null;
	public static Xls_Reader profilexls = null;
	public static Xls_Reader watchlistXls = null;
	public static Xls_Reader notificationxls = null;
	public static Xls_Reader enwxls = null;
	public static Xls_Reader enwiamxls = null;
	public static Xls_Reader pubxls = null;
	public static Xls_Reader rccxls = null;
	public static Xls_Reader draiamxls = null;
	public static Xls_Reader ipaxls = null;
	public static Xls_Reader ipaiamxls = null;
	public static Xls_Reader customercarexls = null;
	public static Xls_Reader watxls = null;
	public static Xls_Reader watiamxls = null;
	public static boolean isInitalized = false;

	public WebDriver ob = null;
	protected ExtentReports extent;
	protected final String filePath = "testReports/test_report.html";
	protected ExtentTest test;

	public static String host = null;
	public static String user1, user2, user3;
	public static String fn1, fn2, ln1, ln2, fn3, ln3;
	private String[][] xData;
	public static int count = 0;
	public static int flag = 0;

	protected static final String TOKENIZER_DOUBLE_PIPE = "||";
	protected static boolean suiteRunmode = false;
	protected static Map<String, RowData> testcase = null;
	private static final int TESTDATA_COLUMN_COUNT = 4;
	private static final Object EMPTY_STRING = "";
	protected RowData rowData = null;
	protected String module = null;

	public PageFactory pf = new PageFactory();

	public String suiteName;

	protected static final String COLON = ":";
	protected static final String HTTP = "http://";
	protected static final String EUREKA_APP_NAME = "name";
	protected static final String EUREKA_HOST_NAME = "hostName";
	protected static final String EUREKA_IP_ADDRESS = "ipAddr";
	protected static final String EUREKA_HOST_PORT = "port";
	protected static final String EUREKA_VIP_ADDRESS = "vipAddress";
	protected static final String EUREKA_DC_NAME = "Amazon";
	protected static final String USER_AGENT = "UI Automation Job";
	protected Map<String, String> appHosts = new HashMap<String, String>();

	public static String mainWindow = "";

	@BeforeSuite
	public void beforeSuite(ITestContext ctx) throws Exception {
		suiteName = ctx.getSuite().getName();
		logger.info(suiteName + " ui automation start time - " + new Date());
		if (suiteName.equals("Default suite")) {
			String className = this.getClass().toString();
			if (className.contains("Notifications")) {
				suiteName = "Notifications";
			} else if (className.contains("Profile")) {
				suiteName = "Profile";
			} else if (className.contains("Authoring")) {
				suiteName = "Authoring";
			} else if (className.contains("Search")) {
				suiteName = "Search";
			} else if (className.contains("Watchlist")) {
				suiteName = "Watchlist";
			} else if (className.contains("RCC")) {
				suiteName = "RCC";
			} else if (className.contains("DRAIAM")) {
				suiteName = "DRAIAM";
			} else if (className.contains("Customercare")) {
				suiteName = "customercare";
			} else if (className.contains("PUBLONS")) {
				suiteName = "PUBLONS";
			} else if (className.contains("ENW")
					|| className.contains("ENWIAM")
					|| className.contains("IAM") || className.contains("IPA")
					|| className.contains("WAT")) {
				logger.info("Test - " + className.startsWith("ENW"));

				if (className.contains("ENW")) {
					if (className.contains("IAM")) {
						suiteName = "ENWIAM";
					} else {
						suiteName = "ENW";
					}
				} else if (className.contains("IPA")) {
					if (className.contains("IAM")) {
						suiteName = "IPAIAM";
					} else {
						suiteName = "IPA";
					}
				} else if (className.contains("WAT")) {
					if (className.contains("IAM")) {
						suiteName = "WATIAM";
					} else {
						suiteName = "WAT";
					}
				} else if (className.contains("IAM")) {
					suiteName = "IAM";
				}
			}
		}
		initialize();
		suiteRunmode = TestUtil.isSuiteRunnable(suiteXls, suiteName);
		logger.info(suiteName + " suit runmode is - " + suiteRunmode);
		logger.info(suiteName + " ui automation Running environment - " + host);
		if (!StringUtils.containsIgnoreCase(host,
				"https://projectne.thomsonreuters.com")
				&& !(suiteName.equals("Sanity suite"))) {
			if (suiteName.equals("Notifications")) {
				try {
					createNewUsers();
				} catch (Exception e) {

				}
				if (StringUtils.containsIgnoreCase(host,
						"https://dev-stable.1p.thomsonreuters.com")) {
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
	}

	@BeforeClass
	public void beforeClass() {
		logger = LogManager.getLogger(this.getClass().getSimpleName());
	}

	// @BeforeClass
	// public void beforeClass() {
	// extent = ExtentManager.getReporter(filePath);
	// }

	@AfterSuite
	public void afterSuite() {
		extent.flush();
		logger.info(suiteName + " ui automation end time - " + new Date());
	}

	// initializing the Tests
	public void initialize() throws Exception {
		// logs
		if (!isInitalized) {
			// extent-reports
			// extent = getInstance();
			// config
			CONFIG = new Properties();
			// FileInputStream ip = new
			// FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\config.properties");
			FileInputStream ip = new FileInputStream(
					"src/test/resources/properties/config.properties");
			CONFIG.load(ip);

			OR = new Properties();
			ip = new FileInputStream(
					"src/test/resources/properties/OR.properties");
			OR.load(ip);

			LOGIN = new Properties();
			ip = new FileInputStream(
					"src/test/resources/properties/login.properties");
			LOGIN.load(ip);

			// Getting url
			host = System.getProperty("host");
			logger.info("host name-->" + host);

			// logger.info(host);
			// xls file
			iamxls = new Xls_Reader("src/test/resources/xls/IAM.xlsx");
			searchxls = new Xls_Reader("src/test/resources/xls/Search.xlsx");
			authoringxls = new Xls_Reader(
					"src/test/resources/xls/Authoring.xlsx");
			profilexls = new Xls_Reader("src/test/resources/xls/Profile.xlsx");
			watchlistXls = new Xls_Reader(
					"src/test/resources/xls/Watchlist.xlsx");
			notificationxls = new Xls_Reader(
					"src/test/resources/xls/Notifications.xlsx");
			enwxls = new Xls_Reader("src/test/resources/xls/ENW.xlsx");
			enwiamxls = new Xls_Reader("src/test/resources/xls/ENWIAM.xlsx");
			pubxls = new Xls_Reader("src/test/resources/xls/PUBLONS.xlsx");
			rccxls = new Xls_Reader("src/test/resources/xls/RCC.xlsx");
			draiamxls = new Xls_Reader("src/test/resources/xls/DRAIAM.xlsx");
			ipaxls = new Xls_Reader("src/test/resources/xls/IPA.xlsx");
			ipaiamxls = new Xls_Reader("src/test/resources/xls/IPAIAM.xlsx");
			customercarexls = new Xls_Reader(
					"src/test/resources/xls/customercare.xlsx");
			watxls = new Xls_Reader("src/test/resources/xls/WAT.xlsx");
			watiamxls = new Xls_Reader("src/test/resources/xls/WATIAM.xlsx");
			suiteXls = new Xls_Reader("src/test/resources/xls/Suite.xlsx");
			isInitalized = true;
		}
		testcase = new HashMap<String, RowData>();
		if (suiteName.equals("Notifications")) {
			loadModuleData(notificationxls.path);
		} else if (suiteName.equals("Profile")) {
			loadModuleData(profilexls.path);
		} else if (suiteName.equals("IAM")) {
			loadModuleData(iamxls.path);
		} else if (suiteName.equals("Authoring")) {
			loadModuleData(authoringxls.path);
		} else if (suiteName.equals("Search")) {
			loadModuleData(searchxls.path);
		} else if (suiteName.equals("Watchlist")) {
			loadModuleData(watchlistXls.path);
		} else if (suiteName.equals("ENW")) {
			loadModuleData(enwxls.path);
		} else if (suiteName.equals("ENWIAM")) {
			loadModuleData(enwiamxls.path);
		}else if (suiteName.equals("PUBLONS")) {
			loadModuleData(pubxls.path);
		}  else if (suiteName.equals("RCC")) {
			loadModuleData(rccxls.path);
		} else if (suiteName.equals("DRAIAM")) {
			loadModuleData(draiamxls.path);
		} else if (suiteName.equals("IPA")) {
			loadModuleData(ipaxls.path);
		} else if (suiteName.equals("IPAIAM")) {
			loadModuleData(ipaiamxls.path);
		} else if (suiteName.equals("customercare")) {
			loadModuleData(customercarexls.path);
		} else if (suiteName.equals("WAT")) {
			loadModuleData(watxls.path);
		} else if (suiteName.equals("WATIAM")) {
			loadModuleData(watiamxls.path);
		} else if (suiteName.equals("Sanity suite")) {
			loadModuleData(iamxls.path);
			loadModuleData(searchxls.path);
			loadModuleData(authoringxls.path);
			loadModuleData(profilexls.path);
			loadModuleData(watchlistXls.path);
			loadModuleData(notificationxls.path);
			loadModuleData(enwxls.path);
			loadModuleData(enwiamxls.path);
			loadModuleData(pubxls.path);
			loadModuleData(rccxls.path);
			loadModuleData(draiamxls.path);
			loadModuleData(ipaxls.path);
			loadModuleData(ipaiamxls.path);
			loadModuleData(customercarexls.path);
			loadModuleData(watxls.path);
			loadModuleData(watiamxls.path);
		} else if (suiteName.equals("LocalRun")) {
			loadModuleData(iamxls.path);
			loadModuleData(profilexls.path);
			loadModuleData(enwxls.path);
			loadModuleData(enwiamxls.path);
			loadModuleData(pubxls.path);
			loadModuleData(searchxls.path);
			loadModuleData(rccxls.path);
			loadModuleData(authoringxls.path);
			loadModuleData(draiamxls.path);
			loadModuleData(ipaxls.path);
			loadModuleData(ipaiamxls.path);
			loadModuleData(customercarexls.path);
			loadModuleData(watxls.path);
			loadModuleData(watiamxls.path);
			loadModuleData(watchlistXls.path);
		}

		logger.info(suiteName + "---" + testcase.size());
	}

	protected void loadModuleData(String module) throws IOException {
		XSSFWorkbook workBook = null;
		FileInputStream inputStream = null;
		try {
			int sheetRowCount;
			XSSFSheet sheet = null;
			XSSFRow row = null;
			// String sheetName = null;

			// Read Excel file
			File myxl = new File(module);
			inputStream = new FileInputStream(myxl);
			workBook = new XSSFWorkbook(inputStream);
			sheet = workBook.getSheet("Test Cases");
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
		} catch (

		Exception e) {
			logger.error("Exception while executing the tests:" + e);
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
	}

	protected RowData getRowData(XSSFRow row) throws Exception {

		RowData rowData = new RowData();
		String currentCellData = null;

		for (int currentCell = 0; currentCell < TESTDATA_COLUMN_COUNT; currentCell++) {

			currentCellData = getCellData(row.getCell(currentCell,
					Row.CREATE_NULL_AS_BLANK));

			switch (currentCell) {
			case 0:
				rowData.setTestclassName(currentCellData);
			case 1:
				rowData.setTestcaseId(getJiraId(currentCellData));
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
			throw new RuntimeException("We don't support this cell type: "
					+ type);
		}
		return result.toString();
	}

	// Environment Status returns true scripts will run on Sauce labs otherwise
	// run on
	// local machine configurations
	public void openBrowser() throws Exception {
		logger.info("Environment Status-->"
				+ StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER")));
		if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
			logger.info("Running Environment is Saucelabs");
			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setBrowserName(System
					.getenv("SELENIUM_BROWSER"));
			logger.info("Browser Name-->" + System.getenv("SELENIUM_BROWSER"));
			desiredCapabilities.setVersion(System.getenv("SELENIUM_VERSION"));
			logger.info("Browser Version-->"
					+ System.getenv("SELENIUM_VERSION"));
			logger.info("Platform Version-->"
					+ System.getenv("SELENIUM_PLATFORM"));
			desiredCapabilities.setCapability(CapabilityType.PLATFORM,
					System.getenv("SELENIUM_PLATFORM"));
			desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,
					true); //
			desiredCapabilities.setCapability(CapabilityType.HAS_NATIVE_EVENTS,
					true);
			if (System.getenv("SELENIUM_BROWSER").trim()
					.equalsIgnoreCase("internet explorer")) // to overcome
															// delete cookies
															// producing error
															// in IEBrowser
				desiredCapabilities
						.setCapability("ie.ensureCleanSession", true);
			desiredCapabilities.setCapability("name", this.getClass()
					.getSimpleName());

			if (StringUtils.containsIgnoreCase(host,
					"https://projectne.thomsonreuters.com")) {
				if (System.getenv("SELENIUM_BROWSER")
						.equalsIgnoreCase("Chrome")) {
					ChromeOptions co = new ChromeOptions();
					co.addArguments("--user-agent=" + USER_AGENT);
					desiredCapabilities.setCapability(ChromeOptions.CAPABILITY,
							co);
				}
			}
			ob = new RemoteWebDriver(
					new URL(
							"http://chinna_putha:e58d38dc-3d93-43e8-af25-5e3a1df25e8b@ondemand.saucelabs.com:80/wd/hub"),
					desiredCapabilities);
			String waitTime = CONFIG.getProperty("defaultImplicitWait");
			String pageWait = CONFIG.getProperty("defaultPageWait");
			ob.manage().timeouts()
					.implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			try {
				ob.manage()
						.timeouts()
						.implicitlyWait(Long.parseLong(pageWait),
								TimeUnit.SECONDS);
			} catch (Throwable t) {
				logger.info("Page Load Timeout not supported in safari driver");
			}
			// else part having local machine configuration
		} else {
			logger.info("Running Environment is Local Machine");
			if (CONFIG.getProperty("browserType").equalsIgnoreCase("FF")) {
				System.setProperty("webdriver.gecko.driver",
						"drivers/geckodriver.exe");
				ob = new FirefoxDriver();
			} else if (CONFIG.getProperty("browserType").equalsIgnoreCase("IE")) {
				DesiredCapabilities capabilities = DesiredCapabilities
						.internetExplorer();
				capabilities.setCapability(
						InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				capabilities
						.setCapability(
								InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
								true);
				System.setProperty("webdriver.ie.driver",
						"drivers/IEDriverServer.exe");
				ob = new InternetExplorerDriver(capabilities);
			} else if (CONFIG.getProperty("browserType").equalsIgnoreCase(
					"Chrome")) {
				System.setProperty("webdriver.chrome.driver",
						"drivers/chromedriver.exe");
				DesiredCapabilities capability = DesiredCapabilities.chrome();
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				ChromeOptions co = new ChromeOptions();
				co.addArguments("--user-agent=" + USER_AGENT);
				capability.setCapability(ChromeOptions.CAPABILITY, co);
				ob = new ChromeDriver(capability);
			} else if (CONFIG.getProperty("browserType").equalsIgnoreCase(
					"Safari")) {
				DesiredCapabilities desiredCapabilities = DesiredCapabilities
						.safari();
				SafariOptions safariOptions = new SafariOptions();
				safariOptions.setUseCleanSession(true);
				desiredCapabilities.setCapability(SafariOptions.CAPABILITY,
						safariOptions);
				ob = new SafariDriver(desiredCapabilities);
			}
			String waitTime = CONFIG.getProperty("defaultImplicitWait");
			String pageWait = CONFIG.getProperty("defaultPageWait");
			ob.manage().timeouts()
					.implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
			try {
				ob.manage()
						.timeouts()
						.pageLoadTimeout(Long.parseLong(pageWait),
								TimeUnit.SECONDS);
			} catch (Throwable t) {
				logger.info("Page Load Timeout not supported in safari driver");
			}
		}
	}

	// selenium RC/ Webdriver

	// Opening the desired browser
	/*
	 * public void openBrowser(){
	 * if(CONFIG.getProperty("browserType").equals("FF")){ ob = new
	 * FirefoxDriver(); } else if
	 * (CONFIG.getProperty("browserType").equals("IE")){
	 * System.setProperty("webdriver.ie.driver",
	 * "C:\\Users\\UC201214\\Desktop\\IEDriverServer.exe"); DesiredCapabilities
	 * capabilities = DesiredCapabilities.internetExplorer();
	 * capabilities.setCapability(InternetExplorerDriver.
	 * IE_ENSURE_CLEAN_SESSION, true); System.setProperty("webdriver.ie.driver",
	 * "drivers/IEDriverServer.exe"); ob = new
	 * InternetExplorerDriver(capabilities); } else if
	 * (CONFIG.getProperty("browserType").equalsIgnoreCase("Chrome")){
	 * DesiredCapabilities capability = DesiredCapabilities.chrome();
	 * capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	 * System.setProperty("webdriver.chrome.driver",
	 * "C:\\Users\\UC201214\\Desktop\\compatibility issues\\chromedriver.exe");
	 * System.setProperty("webdriver.chrome.driver",
	 * "drivers/chromedriver.exe"); ob= new ChromeDriver(capability); } else if
	 * (CONFIG.getProperty("browserType").equalsIgnoreCase("Safari")){
	 * DesiredCapabilities desiredCapabilities = DesiredCapabilities.safari();
	 * SafariOptions safariOptions = new SafariOptions();
	 * safariOptions.setUseCleanSession(true);
	 * desiredCapabilities.setCapability(SafariOptions.CAPABILITY,
	 * safariOptions); ob = new SafariDriver(desiredCapabilities); } String
	 * waitTime=CONFIG.getProperty("defaultImplicitWait"); String
	 * pageWait=CONFIG.getProperty("defaultPageWait");
	 * ob.manage().timeouts().implicitlyWait(Long.parseLong(waitTime),
	 * TimeUnit.SECONDS); try{
	 * ob.manage().timeouts().pageLoadTimeout(Long.parseLong(pageWait),
	 * TimeUnit.SECONDS); } catch(Throwable t){ System.out.println(
	 * "Page Load Timeout not supported in safari driver"); } }
	 */

	public void runOnSauceLabsFromLocal(String os, String browser)
			throws Exception {

		String username = "chinna_putha";
		String access_key = "e58d38dc-3d93-43e8-af25-5e3a1df25e8b";
		String url = "http://" + username + ":" + access_key
				+ "@ondemand.saucelabs.com:80/wd/hub";

		DesiredCapabilities caps = null;

		if (os.equalsIgnoreCase("Windows")) {

			if (browser.equalsIgnoreCase("Chrome")) {
				caps = DesiredCapabilities.chrome();
				caps.setCapability("platform", "Windows 7");
				caps.setCapability("version", "61");
				caps.setCapability("name", this.getClass().getSimpleName());
			}

			if (browser.equalsIgnoreCase("FF")) {
				caps = DesiredCapabilities.firefox();
				caps.setCapability("platform", "Windows 7");
				caps.setCapability("version", "44.0");
				caps.setCapability("name", this.getClass().getSimpleName());
			}

			if (browser.equalsIgnoreCase("IE")) {
				caps = DesiredCapabilities.internetExplorer();
				caps.setCapability("platform", "Windows 8.1");
				caps.setCapability("version", "11.0");
				caps.setCapability("name", this.getClass().getSimpleName());
			}

		}

		if (os.equalsIgnoreCase("Mac")) {

			if (browser.equalsIgnoreCase("Chrome")) {

				caps = DesiredCapabilities.chrome();
				caps.setCapability("platform", "OS X 10.11");
				caps.setCapability("version", "48.0");
				caps.setCapability("name", this.getClass().getSimpleName());
			}

			if (browser.equalsIgnoreCase("FF")) {

				caps = DesiredCapabilities.firefox();
				caps.setCapability("platform", "OS X 10.11");
				caps.setCapability("version", "44.0");
				caps.setCapability("name", this.getClass().getSimpleName());
			}

			if (browser.equalsIgnoreCase("Safari")) {

				caps = DesiredCapabilities.safari();
				caps.setCapability("platform", "OS X 10.11");
				caps.setCapability("version", "9.0");
				caps.setCapability("name", this.getClass().getSimpleName());
			}
		}

		if (os.equalsIgnoreCase("iOS")) {

			caps = DesiredCapabilities.iphone();
			caps.setCapability("platform", "OS X 10.10");
			caps.setCapability("version", "9.2");
			caps.setCapability("deviceName", "iPhone 6");
			caps.setCapability("deviceOrientation", "portrait");
			caps.setCapability("name", this.getClass().getSimpleName());
		}

		if (os.equalsIgnoreCase("Android")) {

			caps = DesiredCapabilities.android();
			caps.setCapability("platform", "Linux");
			caps.setCapability("version", "5.1");
			caps.setCapability("deviceName", "Android Emulator");
			caps.setCapability("deviceType", "phone");
			caps.setCapability("deviceOrientation", "portrait");
			caps.setCapability("name", this.getClass().getSimpleName());
		}

		ob = new RemoteWebDriver(new URL(url), caps);

	}

	// Closing the browser
	public void closeBrowser() {
		ob.quit();
	}

	// compare titles
	public boolean compareTitle(String expectedVal) {
		try {
			Assert.assertEquals(ob.getTitle(), expectedVal);
			test.log(LogStatus.PASS, "Correct page title getting displayed");

		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			return false;
		}
		return true;
	}

	// compareStrings
	public boolean compareStrings(String expectedString, String actualString) {
		try {
			Assert.assertEquals(actualString, expectedString);
			test.log(LogStatus.PASS, "Strings matching");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			return false;
		}
		return true;
	}

	// compareStrings by ignoring case
	public boolean compareStringsIgnoringCase(String expectedString,
			String actualString) {

		try {
			Assert.assertEquals(actualString.toLowerCase(),
					expectedString.toLowerCase());
			test.log(LogStatus.PASS, "Strings matching");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			return false;
		}
		return true;
	}

	// compare numbers
	public boolean compareNumbers(int expectedVal, int actualValue) {
		try {
			Assert.assertEquals(actualValue, expectedVal);
			test.log(LogStatus.PASS, "Numbers are matching");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			return false;
		}
		return true;
	}

	// Check whether a particular element is present or not(detecting element
	// via xpath)
	public boolean checkElementPresence(String xpathkey) {
		int count = ob.findElements(By.xpath(OR.getProperty(xpathkey))).size();
		logger.info("Count is " + count);
		try {
			Assert.assertEquals(count, 1);
			test.log(LogStatus.PASS, "Particular element is present");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			logger.info("Error:" + t);
			return false;
		}
		return true;
	}

	// Check whether a particular element is present or not(detecting element
	// via link text)
	public boolean checkElementPresence_link_text(String linkKey) {
		int count = ob.findElements(By.linkText(OR.getProperty(linkKey)))
				.size();
		logger.info("Count is " + count);
		try {
			Assert.assertEquals(count, 1);
			test.log(LogStatus.PASS, "Particular element is present");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			logger.info("Error:" + t);
			return false;
		}
		return true;
	}

	// Check whether a particular element is present or not(detecting element
	// via id)
	public boolean checkElementPresence_id(String id) {
		int count = ob.findElements(By.cssSelector(OR.getProperty(id))).size();
		logger.info("Count is " + count);
		try {
			if (count == 0) {
				List<WebElement> element = ob.findElements(By.cssSelector(OR
						.getProperty("login_error1")));
				if (element.size() == 2 || element.size() == 1) {
					test.log(LogStatus.PASS, "Particular element is present");
				}
			} else {
				Assert.assertEquals(count, 1);
				test.log(LogStatus.PASS, "Particular element is present");
			}
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			logger.info("Error:" + t);
			return false;
		}
		return true;
	}

	// Check whether a particular element is present or not(detecting element
	// via name)
	public boolean checkElementPresence_name(String name_key) {
		int count = ob.findElements(By.name(OR.getProperty(name_key))).size();
		logger.info("Count is " + count);
		try {
			Assert.assertEquals(count, 1);
			test.log(LogStatus.PASS, "Particular element is present");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			logger.info("Error:" + t);
			return false;
		}
		return true;
	}

	// Check whether a particular element has disappeared or not(via xpath)
	public boolean checkElementDisappearance(String xpathKey) {

		int count = ob.findElements(By.xpath(OR.getProperty(xpathKey))).size();
		logger.info("Count is" + count);
		try {

			Assert.assertEquals(count, 0);
			test.log(LogStatus.PASS,
					"Particular element is not present....So,everything is working fine");
		} catch (Throwable t) {
			test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			logger.info("Error:" + t);
			return false;
		}

		return true;
	}

	// maximizing window
	public void maximizeWindow() {
		ob.manage().window().maximize();
	}

	// Clearing all cookies
	public void clearCookies() {
		if (!getBrowserNameonVM().trim().equalsIgnoreCase("internet explorer"))
			ob.manage().deleteAllCookies();
	}

	public String getBrowserNameonVM() {
		Capabilities cap = ((RemoteWebDriver) ob).getCapabilities();
		return cap.getBrowserName().toLowerCase();
	}

	// logging in
	public void login() throws Exception {

		/*
		 * waitForElementTobeVisible(ob, By.name("loginEmail"), 180);
		 * Thread.sleep(3000);
		 */
		pf.getBrowserWaitsInstance(ob).waitUntilText("Sign in");
		ob.findElement(By.name("loginEmail")).sendKeys(
				CONFIG.getProperty("defaultUsername"));
		ob.findElement(By.name("loginPassword")).sendKeys(
				CONFIG.getProperty("defaultPassword"));
		pf.getBrowserActionInstance(ob).jsClick(
				OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		waitUntilText("Newsfeed", "Watchlists", "Groups");

	}

	// logging out
	public void logout() throws Exception {
		pf.getBrowserWaitsInstance(ob).waitUntilElementIsDisplayed(
				OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS);
		jsClick(ob, ob.findElement(By
				.cssSelector(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_IMAGE_CSS
						.toString())));
		waitUntilText("Sign out");
		pf.getBrowserWaitsInstance(ob)
				.waitForElementTobeVisible(
						ob,
						By.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK
								.toString()), 30);
		jsClick(ob, ob.findElement(By
				.linkText(OnePObjectMap.HOME_PROJECT_NEON_PROFILE_SIGNOUT_LINK
						.toString())));
		waitUntilText("Sign in");
	}

	// logging out enw
	public void logoutEnw() throws InterruptedException {
		waitForElementTobeClickable(ob,
				By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH
						.toString()), 30);
		jsClick(ob, ob.findElement(By
				.xpath(OnePObjectMap.ENDNOTE_LOGOUT_HEADER_LABLE_XPATH
						.toString())));
		// Thread.sleep(5000);
		// waitForElementTobeVisible(ob,
		// By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_SIGNOUT_LINK_XPATH.toString()),
		// 30);
		// jsClick(ob,
		// ob.findElement(By.xpath(OnePObjectMap.ENDNOTE_LOGOUT_SIGNOUT_LINK_XPATH.toString())));
		jsClick(ob, ob.findElement(By.xpath(OR.getProperty("signOut_link"))));
		try {
			waitUntilText("Sign in");
		} catch (Throwable t) {
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
		}

	}

	// capturing screenshot
	public String captureScreenshot(String filename) throws Exception {
		// screenshot in base64 format
		// String myP = ((TakesScreenshot)
		// ob).getScreenshotAs(OutputType.BASE64);
		// screenshot in File format
		File myImg = ((TakesScreenshot) ob).getScreenshotAs(OutputType.FILE);
		String myP1 = System.getProperty("user.dir") + "/screenshots/"
				+ filename + ".jpg";
		FileUtils.copyFile(myImg, new File(myP1));
		// return "data:image/jpeg;base64," + myP;
		return myP1;

	}

	// Cleaning up watchlist
	public void cleanWatchlist() throws Exception {

		ob.findElement(By.cssSelector(OR.getProperty("watchlist_link")))
				.click();
		Thread.sleep(4000);

		List<WebElement> mylist = ob.findElements(By.xpath(OR
				.getProperty("searchResults_links")));
		for (int i = 0; i < mylist.size(); i++) {

			ob.findElement(
					By.xpath(OR.getProperty("watchlist_watchlist_image")))
					.click();
			Thread.sleep(2000);
			ob.findElement(By.xpath(OR.getProperty("watchlist_remove_button")))
					.click();
			Thread.sleep(2000);

		}

	}

	protected String email = null;
	boolean status = false;
	String mail = null;
	boolean activationStatus = false;

	// Creates a new TR user
	public String createNewUser(String first_name, String last_name)
			throws Exception {
		status = registrationForm(first_name, last_name);
		if (status) {
			activationStatus = userActivation();

		}
		if (activationStatus) {
			mail = loginActivationMail();
		}

		return mail;

	}

	public boolean registrationForm(String first_name, String last_name)
			throws Exception {
		try {
			ob.get("https://www.guerrillamail.com");
			waitForElementTobeVisible(ob,
					By.id(OR.getProperty("email_textBox")), 30);
			email = ob.findElement(By.id(OR.getProperty("email_textBox")))
					.getText();
			ob.navigate().to(host);
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("signup_email_texbox")), 30);
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
			// .findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.sendKeys(first_name);
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.sendKeys(last_name);
			ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
			waitUntilText("Thank you");
			waitForElementTobeVisible(ob,
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")),
					30);

			String text = ob.findElement(
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")))
					.getText();

			Assert.assertTrue(text.contains(email));
			// test.log(LogStatus.PASS, "Account activation email sent");
			/*
			 * if(text.contains(email)){ test.log(LogStatus.INFO,
			 * "Account activation email sent"); }else{ if (test != null) {
			 * test.log(LogStatus.FAIL, "Account activation email not sent");//
			 * extent // reports test.log(LogStatus.INFO, "Snapshot below: " +
			 * test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() +
			 * "_account_activation_email_not_sent")));// screenshot } } if
			 * (!StringContains(text, email)) { if (test != null) {
			 * test.log(LogStatus.FAIL, "Account activation email not sent");//
			 * extent // reports test.log(LogStatus.INFO, "Snapshot below: " +
			 * test.addScreenCapture(
			 * captureScreenshot(this.getClass().getSimpleName() +
			 * "_account_activation_email_not_sent")));// screenshot } }
			 */

			ob.findElement(
					By.xpath(OR.getProperty("signup_conformatin_button")))
					.click();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
			return false;

		}
		return true;
	}

	public boolean userActivation() throws Exception {
		try {
			// BrowserWaits.waitTime(3);
			ob.get("https://www.guerrillamail.com");
			waitUntilText("Please activate your", "account");
			// BrowserWaits.waitTime(22);
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("email_list")), 30);
			List<WebElement> email_list = ob.findElements(By.xpath(OR
					.getProperty("email_list")));
			WebElement myE = email_list.get(0);
			JavascriptExecutor executor = (JavascriptExecutor) ob;
			executor.executeScript("arguments[0].click();", myE);
			// BrowserWaits.waitTime(3);
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("email_body")), 30);
			WebElement email_body = ob.findElement(By.xpath(OR
					.getProperty("email_body")));
			List<WebElement> links = email_body.findElements(By.tagName("a"));

			ob.get(links.get(0).getAttribute("href"));
			waitUntilText("Success!");
			ob.findElement(
					By.xpath(OR.getProperty("signup_conformatin_button")))
					.click();
			waitUntilText("Sign in");
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
			return false;
		}
		return true;
	}

	public String loginActivationMail() throws Exception {
		try {
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("TR_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("TR_password_textBox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.cssSelector(OR.getProperty("login_button")))
					.click();
			// pf.getLoginTRInstance(ob).closeOnBoardingModal();

			waitUntilText("Welcome", "Next");
			// ob.findElement(By.xpath(OR.getProperty("signup_conformatin_button"))).click();
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_done_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_done_button")))
					.click();
			waitUntilText("learn about you", "Join");
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_join_button")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_join_button")))
					.click();

			waitUntilText("Newsfeed", "Watchlists", "Groups");
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
		}
		return email;
	}

	// verifies whether a particular string contains another string or not
	public boolean StringContains(String MainString, String ToBeCheckedString) {
		try {
			Assert.assertTrue(MainString.contains(ToBeCheckedString),
					"MainString doesn't contain ToBeCheckedString");
			if (test != null)
				test.log(LogStatus.PASS,
						"MainString doesn't contain ToBeCheckedString");
		} catch (Throwable t) {
			if (test != null)
				test.log(LogStatus.INFO, "Error--->" + t);
			ErrorUtil.addVerificationFailure(t);
			return false;
		}
		return true;
	}

	// To verify that a date falls between 2 particular dates
	public boolean checkDate(String date, String minDate, String maxDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

		try {

			Date dateToBeChecked = formatter.parse(date);
			Date lowerValue = formatter.parse(minDate);
			Date upperValue = formatter.parse(maxDate);

			if (dateToBeChecked.equals(lowerValue)
					|| dateToBeChecked.equals(upperValue)
					|| (dateToBeChecked.after(lowerValue) && dateToBeChecked
							.before(upperValue)))
				return true;
			else
				return false;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return true;
	}

	// This method returns a name with specified character length
	public String generateRandomName(int numberOfCharacters) {

		Random rand = new Random(System.currentTimeMillis());

		int min = 97;
		int max = 122;
		char ch;
		int num;

		String random_name = "";

		for (int i = 1; i <= numberOfCharacters; i++) {

			num = min + rand.nextInt(max - min + 1);
			ch = (char) num;
			random_name = random_name + ch;

		}

		return random_name;

	}

	// Added by Chinna

	public WebDriver getOb() {
		return ob;
	}

	public void setOb(WebDriver ob) {
		this.ob = ob;
	}

	public void scrollingToElementofAPage() throws InterruptedException {
		BrowserWaits.waitTime(5);
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("scroll(0, 500);");
		Thread.sleep(4000);
	}

	// Added by Kavya

	/**
	 * Method to wait till the element is visible on the web page
	 * 
	 * @param driver
	 * @param locator
	 * @param time
	 * @return
	 */
	public WebElement waitForElementTobeVisible(WebDriver driver, By locator,
			int time) {

		return new WebDriverWait(driver, time).until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * Method to wait till the element is visible on the web page
	 * 
	 * @param driver
	 * @param locator
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public WebElement waitForElementTobeVisible(WebDriver driver, By locator,
			int time, String Errormsg) throws Exception {
		WebElement element = null;
		try {
			element = waitForElementTobeVisible(driver, locator, time);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(Errormsg);
		}
		return element;
	}

	public boolean waitForElementTobeInvisible(WebDriver driver, By locator,
			int time) {
		Wait<WebDriver> wait = new FluentWait<>(driver)
				.withTimeout(time, TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions
				.invisibilityOfElementLocated(locator));
	}

	/**
	 * Method to wait till the element is present on the web page
	 * 
	 * @param driver
	 * @param locator
	 * @param time
	 * @return
	 */
	public WebElement waitForElementTobePresent(WebDriver driver, By locator,
			int time) {

		return new WebDriverWait(driver, time).until(ExpectedConditions
				.presenceOfElementLocated(locator));
	}

	/**
	 * Method to click on the specified element using java script executor.
	 * 
	 * @param driver
	 * @param element
	 */
	public static void jsClick(WebDriver driver, WebElement element) {

		((JavascriptExecutor) driver).executeScript("arguments[0].click();",
				element);
	}

	/**
	 * Method to scroll the specified element to view.
	 * 
	 * @param driver
	 * @param element
	 * @throws Exception
	 */
	public void scrollElementIntoView(WebDriver driver, WebElement element)
			throws Exception {
		JavascriptExecutor jse = (JavascriptExecutor) ob;
		jse.executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(5000);
	}

	/**
	 * Method to wait till all the elements are present on the web page
	 * 
	 * @param driver
	 * @param locator
	 * @param time
	 * @return
	 */
	public static List<WebElement> waitForAllElementsToBePresent(
			WebDriver driver, By locator, int time) {
		return new WebDriverWait(driver, time).until(ExpectedConditions
				.presenceOfAllElementsLocatedBy(locator));
	}

	/**
	 * This method is to wait for all ajax calls to complete.
	 * 
	 * @param driver
	 */
	public void waitForAjax(WebDriver driver) {
		try {
			for (int i = 0; i < 60; i++) {

				JavascriptExecutor js = (JavascriptExecutor) driver;
				// check for the pending request count and break if count is
				// zero.
				if ((Long) js
						.executeScript("return angular.element(document.body).injector().get(\'$http\').pendingRequests.length") == 0) {
					break;
				}
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void waitForPageLoad(WebDriver driver) {
		try {
			for (int i = 0; i < 90; i++) {

				JavascriptExecutor js = (JavascriptExecutor) driver;
				// check for the pending request count and break if count is
				// zero.
				if (js.executeScript("return document.readyState").equals(
						"complete")) {

					break;
				}
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// waitForAjax(driver);
	}

	public Alert waitForAlertToBePresent(WebDriver driver, int time) {

		return new WebDriverWait(driver, time).until(ExpectedConditions
				.alertIsPresent());
	}

	/**
	 * Method to wait till the element is clickable on the web page
	 * 
	 * @param driver
	 * @param locator
	 * @param time
	 * @return
	 */
	public WebElement waitForElementTobeClickable(WebDriver driver, By locator,
			int time) {

		return new WebDriverWait(driver, time).until(ExpectedConditions
				.elementToBeClickable(locator));
	}

	public boolean checkElementIsDisplayed(WebDriver driver, By locator) {
		boolean result = false;
		try {

			result = ob.findElement(locator).isDisplayed();

		} catch (Exception e) {
			return false;
		}
		return result;
	}

	/**
	 * This is wait condition for multiple windows.
	 * 
	 * @param driver
	 * @param numberOfWindows
	 * @return
	 */
	public ExpectedCondition<Boolean> numberOfWindowsToBe(WebDriver driver,
			final int numberOfWindows) {
		return new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				driver.getWindowHandles();
				return driver.getWindowHandles().size() == numberOfWindows;
			}
		};
	}

	/**
	 * This method is to wait till specified number of windows are open.
	 * 
	 * @param driver
	 * @param numberOfWindows
	 */
	public void waitForNumberOfWindowsToEqual(WebDriver driver,
			final int numberOfWindows) {
		new WebDriverWait(driver, 60).until(numberOfWindowsToBe(driver,
				numberOfWindows));
	}

	/**
	 * Method to switch to new window.
	 * 
	 * @param driver
	 * @return
	 */
	// public String switchToNewWindow(WebDriver driver) {
	// mainWindow = driver.getWindowHandle();
	// String newWindow = "";
	// Set<String> windows = driver.getWindowHandles();
	// // windows.remove(mainWindow);
	// if (!windows.iterator().next().contains(mainWindow))
	// newWindow = windows.iterator().next();
	// driver.switchTo().window(newWindow);
	// return newWindow;
	//
	// }
	public String switchToNewWindow(WebDriver driver) {
		mainWindow = driver.getWindowHandle();
		String newWindow = "";
		for (String windowhandle : driver.getWindowHandles()) {
			newWindow = windowhandle;
			driver.switchTo().window(windowhandle);
		}
		return newWindow;
	}

	public String switchToMainWindow(WebDriver driver) {
		System.out.println("Closing the current browser");
		driver.getWindowHandles().remove(driver.getWindowHandle());
		driver.switchTo().window(mainWindow);
		return mainWindow;
	}

	/**
	 * Method to switch to main window
	 * 
	 * @param driver
	 * @param mainWindowHandle
	 */
	public void switchToMainWindow(WebDriver driver, String mainWindowHandle) {

		driver.switchTo().window(mainWindowHandle);

	}

	// Method to

	// Method to return excel workbook path
	public String returnExcelPath(char ch) {

		if (ch == 'A')
			return "src/test/resources/xls/IAM.xlsx";
		else if (ch == 'B')
			return "src/test/resources/xls/Search.xlsx";
		else if (ch == 'C')
			return "src/test/resources/xls/Authoring.xlsx";
		else if (ch == 'D')
			return "src/test/resources/xls/Profile.xlsx";
		else if (ch == 'E')
			return "src/test/resources/xls/Watchlist.xlsx";
		else if (ch == 'F')
			return "src/test/resources/xls/Notifications.xlsx";
		else if (ch == 'G')
			return "src/test/resources/xls/ENW.xlsx";
		else
			return "No such excel file present";

	}

	// Method to read excel worksheet
	public String xlRead(String sPath, int r, int c) throws Exception {
		int xRows, xCols;
		File myxl = new File(sPath);
		FileInputStream myStream = new FileInputStream(myxl);

		XSSFWorkbook myWB = new XSSFWorkbook(myStream);
		XSSFSheet mySheet = myWB.getSheetAt(0); // Referring to 1st sheet
		xRows = mySheet.getLastRowNum() + 1;
		xCols = mySheet.getRow(0).getLastCellNum();
		logger.info("Rows are " + xRows);
		logger.info("Cols are " + xCols);
		xData = new String[xRows][xCols];
		for (int i = 0; i < xRows; i++) {
			XSSFRow row = mySheet.getRow(i);
			for (int j = 0; j < xCols; j++) {
				XSSFCell cell = row.getCell(j); // To read value from each col
												// in each row
				String value = cellToString(cell);
				xData[i][j] = value;
			}
		}
		return xData[r][c];
	}

	public String xlRead2(String sPath, String cellValue, int c)
			throws Exception {
		int xRows, xCols;
		int r = 0;
		File myxl = new File(sPath);
		FileInputStream myStream = new FileInputStream(myxl);

		XSSFWorkbook myWB = new XSSFWorkbook(myStream);
		XSSFSheet mySheet = myWB.getSheetAt(0); // Referring to 1st sheet
		xRows = mySheet.getLastRowNum() + 1;
		xCols = mySheet.getRow(0).getLastCellNum();
		xData = new String[xRows][xCols];
		for (int i = 0; i < xRows; i++) {
			XSSFRow row = mySheet.getRow(i);
			for (int j = 0; j < xCols; j++) {
				XSSFCell cell = row.getCell(j); // To read value from each col
												// in each row
				String value = cellToString(cell);
				xData[i][j] = value;
				if (xData[i][j].equalsIgnoreCase(cellValue)) {
					r = i;
				}
			}
		}
		return xData[r][c];
	}

	public String cellToString(XSSFCell cell) {
		// This function will convert an object of type excel cell to a string
		// value
		int type = cell.getCellType();
		Object result;
		switch (type) {
		case XSSFCell.CELL_TYPE_NUMERIC: // 0
			result = cell.getNumericCellValue();
			break;
		case XSSFCell.CELL_TYPE_STRING: // 1
			result = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_FORMULA: // 2
			throw new RuntimeException("We can't evaluate formulas in Java");
		case XSSFCell.CELL_TYPE_BLANK: // 3
			result = "-";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN: // 4
			result = cell.getBooleanCellValue();
			break;
		case XSSFCell.CELL_TYPE_ERROR: // 5
			throw new RuntimeException("This cell has an error");
		default:
			throw new RuntimeException("We don't support this cell type: "
					+ type);
		}
		return result.toString();
	}

	/**
	 * 
	 * @param username
	 *            -USERNAME Field from the login.properties file
	 * @param pwd
	 *            - PASSWORD Field from the login.properties file
	 * @throws Exception
	 */
	public void loginAs(String usernameKey, String pwdKey) throws Exception {
		// waitForElementTobeVisible(ob,
		// By.xpath(OR.getProperty("TR_login_button")), 180);
		// jsClick(ob,
		// ob.findElement(By.xpath(OR.getProperty("TR_login_button"))));
		waitForElementTobeClickable(ob, By.name("loginEmail"), 180);
		ob.findElement(By.name("loginEmail")).clear();
		ob.findElement(By.name("loginEmail")).sendKeys(
				LOGIN.getProperty(usernameKey));
		waitForElementTobeClickable(ob, By.name("loginPassword"), 180);
		ob.findElement(By.name("loginPassword")).clear();
		ob.findElement(By.name("loginPassword")).sendKeys(
				LOGIN.getProperty(pwdKey));
		// jsClick(ob,
		// ob.findElement(By.cssSelector("button[class*='login-button']")));
		pf.getBrowserActionInstance(ob).jsClick(
				OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		try {
			ob.findElement(By.xpath("//h3[@class='wui-modal__title']"))
					.isDisplayed();
			ob.findElement(By.xpath("//a[text()='Not now']")).click();
		} catch (Exception e) {
			System.out.println("Loging in to Neon without linking popup");
		}
	}

	public void loginToWOS(String usernameKey, String pwdKey) throws Exception {
		waitForElementTobeClickable(ob, By.name("username"), 180);
		ob.findElement(By.name("username")).clear();
		ob.findElement(By.name("username")).sendKeys(
				LOGIN.getProperty(usernameKey));
		ob.findElement(By.name("password")).sendKeys(LOGIN.getProperty(pwdKey));
		jsClick(ob, ob.findElement(By.xpath(".//*[@name='image']")));
		try {
			if (ob.findElements(
					By.xpath(".//h1[contains(text(),'Thank you for using Web of Science')]"))
					.size() > 0) {
				jsClick(ob,
						ob.findElement(By
								.xpath(".//a[contains(text(),'continue and establish a new session')]")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginToRID(String usernameKey, String pwdKey) throws Exception {
		jsClick(ob, ob.findElement(By.xpath("//a[@id='header_link_login']")));
		waitForElementTobeVisible(ob, By.xpath("//input[@id='email']"), 180);
		ob.findElement(By.xpath("//input[@id='email']")).clear();
		ob.findElement(By.xpath("//input[@id='email']")).sendKeys(
				LOGIN.getProperty(usernameKey));
		ob.findElement(By.xpath("//input[@id='password']")).sendKeys(
				LOGIN.getProperty(pwdKey));
		jsClick(ob, ob.findElement(By.xpath("//*[@id='submitImage']/img")));

	}

	/**
	 * Method to navigate to a particular watch list details page
	 * 
	 * @param selectedWatchlistName
	 *            watch list name
	 * @throws InterruptedException
	 */
	public void navigateToParticularWatchlistPage(String selectedWatchlistName)
			throws InterruptedException {

		// Navigate to the watch list landing page
		waitForElementTobeClickable(ob,
				By.cssSelector(OR.getProperty("watchlist_link")), 60);
		jsClick(ob, ob.findElement(By.cssSelector(OR
				.getProperty("watchlist_link"))));
		// ob.findElement(By.cssSelector(OR.getProperty("watchlist_link"))).click();
		waitForElementTobeVisible(
				ob,
				By.xpath("//aside[@class='watchlist-side-menu__refine-list wui-side-menu']"),
				60);

		// Getting all the watch lists
		List<WebElement> watchLists = ob.findElements(By.xpath(OR
				.getProperty("watchlist_name1")));
		// Finding the particular watch list and navigating to it
		for (int i = 0; i < watchLists.size(); i++) {
			if (watchLists.get(i).getText().equals(selectedWatchlistName)) {
				logger.info("WatchList First Item Name : "
						+ watchLists.get(i).getText());
				// watchLists.get(i).click();
				// Thread.sleep(2000);
				jsClick(ob, watchLists.get(i));
				Thread.sleep(3000);
				// waitForElementTobeVisible(ob,
				// By.xpath(OR.getProperty("watch_list_details_heading1")), 30);
				break;
			}
			// Scrolling down to make the watch list visible
			((JavascriptExecutor) ob).executeScript(
					"arguments[0].scrollIntoView(true);", watchLists.get(i));
		}
	}

	/**
	 * Method for watch or unwatch item for particular watchlist from Search
	 * Results or Article Record page
	 * 
	 * @param watchButton
	 * @param watchListName
	 * @throws InterruptedException
	 */
	public void watchOrUnwatchItemToAParticularWatchlist(String watchListName,
			WebElement watchbutton) throws InterruptedException {

		// watchbutton.click();
		jsClick(ob, watchbutton);

		waitForAllElementsToBePresent(ob,
				By.xpath("//a[@class='ne-action-dropdown__item-content']"), 80);
		Thread.sleep(2000);
		waitForAllElementsToBePresent(ob, By.linkText(watchListName), 80);
		Thread.sleep(3000);
		List<WebElement> list = ob.findElements(By.linkText(watchListName));
		for (WebElement element : list) {

			if (element.isDisplayed()) {
				// element.click();
				jsClick(ob, element);
				break;
			}
		}
		Thread.sleep(3000);
		ob.findElement(By.xpath("//input[@type='text']")).click();

	}

	public void watchOrUnwatchItemToAParticularWatchlist(String watchListName)
			throws InterruptedException {
		ob.findElement(
				By.xpath("//button[@class='wui-icon-btn dropdown-toggle']"))
				.click();
		waitForAllElementsToBePresent(ob,
				By.xpath("//a[@class='ne-action-dropdown__item-content']"), 60);
		Thread.sleep(2000);
		waitForAllElementsToBePresent(ob, By.linkText(watchListName), 60);
		Thread.sleep(3000);
		List<WebElement> list = ob.findElements(By.linkText(watchListName));
		for (WebElement element : list) {

			if (element.isDisplayed()) {
				element.click();
				break;
			}
		}
		Thread.sleep(3000);
		ob.findElement(By.xpath("//input[@type='text']")).click();

	}

	/**
	 * 
	 * @param type
	 *            search type from dropdown
	 * @throws InterruptedException
	 */
	public void selectSearchTypeFromDropDown(String type)
			throws InterruptedException {
		ob.findElement(By.xpath(OR.getProperty("search_type_dropdown")))
				.click();
		waitForElementTobeVisible(ob,
				By.xpath(OR.getProperty("search_type_dropdown_values_panel")),
				5);
		ob.findElement(By.linkText(type)).click();
		Thread.sleep(2000);
	}

	/**
	 * Method for create watchlist
	 * 
	 * @param typeOfWatchList
	 * @param watchListName
	 * @param watchListDescription
	 * @throws Exception
	 */
	public void createWatchList(String typeOfWatchList, String watchListName,
			String watchListDescription) throws Exception {
		waitForElementTobeClickable(ob,
				By.cssSelector(OR.getProperty("watchlist_link")), 90);

		jsClick(ob, ob.findElement(By.cssSelector(OR
				.getProperty("watchlist_link"))));
		// jsClick(ob,
		// ob.findElement(By.cssSelector(OR.getProperty("watchlist_link"))));
		// BrowserWaits.waitTime(10);
		// ob.navigate().refresh();
		waitForElementTobeClickable(ob,
				By.xpath(OR.getProperty("createWatchListButton1")), 80);
		ob.findElement(By.xpath(OR.getProperty("createWatchListButton1")))
				.click();
		waitForElementTobeClickable(ob,
				By.xpath(OR.getProperty("newWatchListNameTextBox")), 80);
		ob.findElement(By.xpath(OR.getProperty("newWatchListNameTextBox")))
				.sendKeys(watchListName);
		waitForElementTobeClickable(ob,
				By.xpath(OR.getProperty("newWatchListDescriptionTextArea")), 80);
		ob.findElement(
				By.xpath(OR.getProperty("newWatchListDescriptionTextArea")))
				.sendKeys(watchListDescription);
		if (typeOfWatchList.equals("public")) {
			waitForElementTobeClickable(ob,
					By.xpath(OR.getProperty("newWatchListPublicCheckBox1")), 80);
			jsClick(ob, ob.findElement(By.xpath(OR
					.getProperty("newWatchListPublicCheckBox1"))));
		}
		waitForElementTobeClickable(ob,
				By.xpath(OR.getProperty("newWatchListCreateButton")), 80);
		ob.findElement(By.xpath(OR.getProperty("newWatchListCreateButton")))
				.click();
		waitForElementTobeVisible(ob,
				By.xpath("//a[contains(text(),'" + watchListName + "')]"), 80);
	}

	/**
	 * Method for Delete watchlist from watchlist page
	 * 
	 * @param watchListName
	 * @throws Exception
	 */
	public void deleteParticularWatchlist(String watchListName)
			throws Exception {
		// Deleting the first watch list
		waitForElementTobeClickable(ob,
				By.cssSelector(OR.getProperty("watchlist_link")), 60);
		ob.findElement(By.cssSelector(OR.getProperty("watchlist_link")))
				.click();
		BrowserWaits.waitTime(4);
		waitForElementTobeVisible(ob,
				By.xpath(OR.getProperty("watchlist_name1")), 60);
		List<WebElement> listOfWatchlist = ob.findElements(By.xpath(OR
				.getProperty("watchlist_name1")));
		for (WebElement watchList : listOfWatchlist) {
			if (watchList.getText().equals(watchListName)) {

				watchList.click();
				waitForElementTobeVisible(ob,
						By.xpath(OR.getProperty("delete_button_image1")), 60);
				ob.findElement(By.xpath(OR.getProperty("delete_button_image1")))
						.click();
				BrowserWaits.waitTime(4);
				ob.findElement(
						By.xpath(OR.getProperty("delete_button_in_popup1")))
						.click();
				BrowserWaits.waitTime(6);
				// waitForElementTobeVisible(ob,
				// By.xpath(OR.getProperty("watchlist_name1")), 60);
				break;
			}
			// Scrolling down to make the watch list visible
			((JavascriptExecutor) ob).executeScript(
					"arguments[0].scrollIntoView(true);", watchList);
		}

	}

	/**
	 * 
	 * @param emailId
	 *            -Login as the specified email id user
	 * @param password
	 *            - The user password
	 * @throws Exception
	 */
	public void loginAsSpecifiedUser(String emailId, String password)
			throws Exception {
		/*
		 * waitForElementTobeVisible(ob,
		 * By.xpath(OR.getProperty("TR_login_button")), 180); jsClick(ob,
		 * ob.findElement(By.xpath(OR.getProperty("TR_login_button"))));
		 * waitForElementTobeVisible(ob,
		 * By.name(OR.getProperty("TR_email_textBox")), 180);
		 * ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
		 * ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).sendKeys(
		 * emailId);
		 * ob.findElement(By.name(OR.getProperty("TR_password_textBox"))).
		 * sendKeys(password); jsClick(ob,
		 * ob.findElement(By.cssSelector(OR.getProperty("login_button"))));
		 */

		waitForElementTobeVisible(ob, By.name("loginEmail"), 180);
		Thread.sleep(3000);
		ob.findElement(By.name("loginEmail")).sendKeys(emailId);
		ob.findElement(By.name("loginPassword")).sendKeys(password);
		ob.findElement(
				By.cssSelector(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS
						.toString())).click();
		// pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.LOGIN_PAGE_SIGN_IN_BUTTON_CSS);
		Thread.sleep(5000);

	}

	public int convertStringToInt(String str) {

		String[] arr = str.split(",");
		String temp = "";
		for (int i = 0; i < arr.length; i++) {

			temp = temp + arr[i];
		}

		int num = Integer.parseInt(temp);

		return num;

	}

	public Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime());
	}

	protected boolean getTestRunMode(String testRunmode) {
		// String testRunmode = testcase.get(simpleName).getTestcaseRunmode();
		if ("Y".equals(testRunmode)) {
			return true;
		} else {
			return false;
		}
	}

	public void createNewUsers() {
		if (!StringUtils.containsIgnoreCase(host,
				"https://projectne.thomsonreuters.com")) {
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
									+ this.getClass().getSimpleName()
									+ "_user_creation_error_screenshot"));// screenshot
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
				pf.getLoginTRInstance(ob).enterTRCredentials(user1,
						CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob,
						By.xpath(OR.getProperty("searchBox_textBox")), 60);
				pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(
						fn2 + " " + ln2);
				// ob.findElement(By.xpath(OR.getProperty("searchBox_textBox"))).sendKeys(fn2
				// + " " + ln2);
				// if (pf.getSearchProfilePageInstance(ob).getPeopleCount() > 0)
				// {
				pf.getSearchProfilePageInstance(ob).clickPeople();
				pf.getSearchProfilePageInstance(ob).followProfileFromSeach();
				// }
				pf.getLoginTRInstance(ob).logOutApp();
				// closeBrowser();
			} else {
				throw new Exception(
						"User creation problem hence throwing exception");
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
				pf.getLoginTRInstance(ob).enterTRCredentials(user2,
						CONFIG.getProperty("defaultPassword"));
				pf.getLoginTRInstance(ob).clickLogin();
				waitForElementTobeVisible(ob,
						By.xpath(OR.getProperty("searchBox_textBox")), 30);
				pf.getSearchProfilePageInstance(ob).enterSearchKeyAndClick(
						fn3 + " " + ln3);
				// if (pf.getSearchProfilePageInstance(ob).getPeopleCount() > 0)
				// {
				pf.getSearchProfilePageInstance(ob).clickPeople();
				pf.getSearchProfilePageInstance(ob).followProfileFromSeach();
				// }
				pf.getLoginTRInstance(ob).logOutApp();
				closeBrowser();
			} else {
				throw new Exception(
						"User creation problem hence throwing exception");
			}
		} catch (Throwable t) {
			logger.info(t.getMessage());
		}
	}

	// Rest Assured configuratoin
	/**
	 * Read host names across all applications for the given environment and
	 * load them to map.
	 * 
	 * @param env
	 *            environment for which the tests connect
	 * @throws Exception
	 */
	protected void getAllAppHostsForGivenEnv(String eurekaURL, String env,
			String local) throws Exception {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();

		String appName = null;
		String hostName = null;
		String port = null;

		URL url = new URL(eurekaURL);
		URLConnection conn = url.openConnection();

		XMLEventReader eventReader = inputFactory.createXMLEventReader(conn
				.getInputStream());

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			// reach the start of an item
			if (event.isStartElement()) {

				StartElement startElement = event.asStartElement();

				// Get app name
				if (startElement.getName().getLocalPart()
						.equals(EUREKA_APP_NAME)) {
					event = eventReader.nextEvent();
					if (!event.asCharacters().getData()
							.equalsIgnoreCase(EUREKA_DC_NAME))
						appName = event.asCharacters().getData();
				}

				// Get IP Address
				if (startElement.getName().getLocalPart()
						.equals(EUREKA_HOST_NAME)
						&& local.equalsIgnoreCase("Y")) {
					event = eventReader.nextEvent();
					hostName = event.asCharacters().getData();
				}

				if (startElement.getName().getLocalPart()
						.equals(EUREKA_IP_ADDRESS)
						&& local.equalsIgnoreCase("N")) {
					event = eventReader.nextEvent();
					hostName = event.asCharacters().getData();
				}

				// Get port
				if (startElement.getName().getLocalPart()
						.equals(EUREKA_HOST_PORT)) {
					event = eventReader.nextEvent();
					port = event.asCharacters().getData();
				}

				// Get vip address
				if (startElement.getName().getLocalPart()
						.equals(EUREKA_VIP_ADDRESS)) {
					event = eventReader.nextEvent();
					if (event.asCharacters().getData().endsWith(env))
						appHosts.put(appName, HTTP + hostName + COLON + port);
				}

			}
		}
	}

	public String deleteUserAccounts(String emailId) throws Exception {

		String local = null;
		if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
			local = "N";
		} else {
			local = "Y";
		}
		if (host.contains("stable")) {
			getAllAppHostsForGivenEnv(
					"http://eureka.us-west-2.dev.oneplatform.build:8080/v2/apps",
					"1p.stable.dev", local);
		} else {
			getAllAppHostsForGivenEnv(
					"http://eureka.us-west-2.dev.oneplatform.build:8080/v2/apps",
					"1p.snapshot.dev", local);
		}

		RequestSpecification reqSpec = given();
		Response resp;
		logger.info("host name-->" + appHosts.get("1PAUTH"));
		resp = reqSpec.when()
				.delete(appHosts.get("1PAUTH") + "/admin/auth/link/accounts/"
						+ emailId);
		return String.valueOf(resp.getStatusCode());

	}

	public String createENWNewUser(String first_name, String last_name)
			throws Exception {

		status = registrationEnwForm(first_name, last_name);
		BrowserWaits.waitTime(2);
		if (status) {
			activationStatus = userActivation();

		}
		if (activationStatus) {
			mail = loginActivatedENWUser();
		}

		return mail;

	}

	public boolean registrationEnwForm(String first_name, String last_name)
			throws Exception {
		try {
			ob.get("https://www.guerrillamail.com");
			waitUntilText("Guerrilla Mail");
			waitForElementTobeVisible(ob,
					By.id(OR.getProperty("email_textBox")), 30);
			email = ob.findElement(By.id(OR.getProperty("email_textBox")))
					.getText();
			// ob.navigate().to(CONFIG.getProperty("enwUrl"));
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("signup_email_texbox")), 30);
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.sendKeys(first_name);
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.sendKeys(last_name);
			ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
			waitUntilText("Thank you");
			waitForElementTobeVisible(ob,
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")),
					30);
			String text = ob.findElement(
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")))
					.getText();

			if (!StringContains(text, email)) {
				if (test != null) {
					test.log(LogStatus.FAIL,
							"Account activation email not sent");// extent
																	// reports
					test.log(
							LogStatus.INFO,
							"Snapshot below: "
									+ test.addScreenCapture(captureScreenshot(this
											.getClass().getSimpleName()
											+ "_account_activation_email_not_sent")));// screenshot
				}
			}

			ob.findElement(
					By.xpath(OR.getProperty("signup_conformatin_button")))
					.click();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.INFO,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
			return false;

		}
		return true;
	}

	public String loginActivatedENWUser() throws Exception {
		try {
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("TR_email_textBox")), 30);
			ob.findElement(By.name(OR.getProperty("TR_email_textBox"))).clear();
			ob.findElement(By.name(OR.getProperty("TR_email_textBox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("TR_password_textBox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.cssSelector(OR.getProperty("login_button")))
					.click();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
		}
		return email;
	}

	public String createTraillingSpaceUser(String first_name, String last_name,
			String email1) throws Exception {

		status = traillingRegistrationEnwForm(first_name, last_name, email1);
		BrowserWaits.waitTime(2);
		if (status) {
			activationStatus = userActivation();

		}
		if (activationStatus) {
			mail = loginActivatedENWUser();
		}

		return mail;

	}

	private boolean traillingRegistrationEnwForm(String first_name,
			String last_name, String email1) throws Exception {
		email = email1;
		try {
			ob.get(host + CONFIG.getProperty("appendENWAppUrl"));
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("signup_email_texbox")), 30);
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.sendKeys(first_name);
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.sendKeys(last_name);
			ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
			waitUntilText("Thank you");
			waitForElementTobeVisible(ob,
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")),
					30);
			ob.findElement(
					By.xpath(OR.getProperty("signup_conformatin_button")))
					.click();
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
			return false;

		}
		return true;
	}

	public String createTraillingSpaceNeonUser(String first_name,
			String last_name, String email1) throws Exception {

		status = traillingRegistrationNeonForm(first_name, last_name, email1);
		if (status) {
			activationStatus = userActivation();

		}
		if (activationStatus) {
			mail = loginActivationMail();
		}

		return mail;

	}

	private boolean traillingRegistrationNeonForm(String first_name,
			String last_name, String email1) throws Exception {
		email = email1;
		try {
			ob.get(host);
			waitUntilText("Sign in");
			waitForElementTobeVisible(ob,
					By.xpath(OR.getProperty("signup_link")), 30);
			ob.findElement(By.xpath(OR.getProperty("signup_link"))).click();
			waitForElementTobeVisible(ob,
					By.name(OR.getProperty("signup_email_texbox")), 30);
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).click();
			// ob.findElement(By.name(OR.getProperty("signup_email_texbox"))).clear();
			ob.findElement(By.name(OR.getProperty("signup_email_texbox")))
					.sendKeys(email);
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_password_textbox")))
					.sendKeys(CONFIG.getProperty("defaultPassword"));
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_firstName_textbox")))
					.sendKeys(first_name);
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.clear();
			ob.findElement(By.name(OR.getProperty("signup_lastName_textbox")))
					.sendKeys(last_name);
			ob.findElement(By.xpath(OR.getProperty("signup_button"))).click();
			waitUntilText("Thank you");
			waitForElementTobeVisible(ob,
					By.cssSelector(OR.getProperty("signup_confom_sent_mail")),
					30);
			ob.findElement(
					By.xpath(OR.getProperty("signup_conformatin_button")))
					.click();
			waitUntilText("Sign in");
		} catch (Throwable t) {
			t.printStackTrace();
			test.log(
					LogStatus.FAIL,
					"Snapshot below: "
							+ test.addScreenCapture(captureScreenshot(this
									.getClass().getSimpleName()
									+ "_user_not_registered")));// screenshot
			closeBrowser();
			return false;

		}
		return true;
	}

	protected String getJiraId(String currentCellData) {
		String jiraid = "";
		if (StringUtils.isNotBlank(currentCellData)) {
			String[] ids = StringUtils.split(currentCellData,
					TOKENIZER_DOUBLE_PIPE);
			for (int i = 0; i < ids.length; i++) {
				if (i > 0) {
					jiraid += TOKENIZER_DOUBLE_PIPE;
				}
				jiraid += "<a href=\"https://jira.clarivate.io/browse/"
						+ ids[i] + "\" target=\"_blank\">" + ids[i] + "</a>";
			}
		}
		return jiraid;
	}

	/**
	 * Method for set status as FAIL and appending screenshot in ExtentReport
	 * 
	 * @param test
	 * @param t
	 * @param message
	 * @param screenShotName
	 * @throws Exception
	 *             , When Report details not captured
	 */
	public void logFailureDetails(ExtentTest test, Throwable t, String message,
			String screenShotName) throws Exception {
		test.log(LogStatus.FAIL, message);
		test.log(
				LogStatus.FAIL,
				"Snapshot below: "
						+ test.addScreenCapture(captureScreenshot(this
								.getClass().getSimpleName() + screenShotName)));// screenshot
		ErrorUtil.addVerificationFailure(t);
	}

	/**
	 * Method for set status as FAIL and appending screenshot in ExtentReport
	 * 
	 * @param test
	 * @param t
	 * @param message
	 * @param screenShotName
	 * @throws Exception
	 *             ,When Report details not captured
	 */
	public void logFailureDetails(ExtentTest test, String message,
			String screenShotName) throws Exception {
		test.log(LogStatus.FAIL, message);
		test.log(
				LogStatus.FAIL,
				"Snapshot below: "
						+ test.addScreenCapture(captureScreenshot(this
								.getClass().getSimpleName() + screenShotName)));// screenshot
		ErrorUtil.addVerificationFailure(new Exception("Test case Failed"));
	}

	public void waitUntilText(final String text) {
		try {
			(new WebDriverWait(ob, time))
					.until(new ExpectedCondition<Boolean>() {

						public Boolean apply(WebDriver d) {
							try {
								return Boolean.valueOf(d.getPageSource()
										.contains(text));
							} catch (Exception e) {
								return Boolean.valueOf(false);
							}
						}
					});
		} catch (TimeoutException te) {
			throw new TimeoutException("Failed to find text: " + text
					+ ", after waiting for " + time + "ms");
		}
	}

	public void waitUntilText(final String... text) {
		for (String each : text) {
			waitUntilText(each);
		}
	}

	public void fluentwaitforElement(WebDriver ob, By element, int time) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(ob)
				.withTimeout(time, TimeUnit.SECONDS)
				.pollingEvery(500, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);

		wait.until(new Function<WebDriver, WebElement>() {

			public WebElement apply(WebDriver driver) {
				return driver.findElement(element);
			}
		});
	}

}
