package util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

import base.TestBase;

public class ExtentManager {

	private static ExtentReports extent;

	public synchronized static ExtentReports getReporter(String filePath) {
		if (extent == null) {
			extent = new ExtentReports(filePath, true,DisplayOrder.OLDEST_FIRST);
			extent.loadConfig(new File("src/test/resources/extent.xml"));
			if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
				extent.addSystemInfo("Browser Name & Version", System.getenv("SELENIUM_BROWSER") + System.getenv("SELENIUM_VERSION"));
				extent.addSystemInfo("Platform & Version", System.getenv("SELENIUM_PLATFORM"));
			} else {
				extent.addSystemInfo("Browser Name", TestBase.CONFIG.getProperty("browserType"));
				extent.addSystemInfo("Platform & Version", "Windows 7");
			}
			/*if(System.getProperty("host").contains("stable")) {
				extent.addSystemInfo("Environment", "Dev-Stable");
			} else if (System.getProperty("host").contains("snapshot")){
				extent.addSystemInfo("Environment", "Dev-Snapshot");
			} else {
				extent.addSystemInfo("Environment", "Production");
			}*/
		}

		return extent;
	}

	public synchronized static ExtentReports getReporter() {
		return extent;
	}
}