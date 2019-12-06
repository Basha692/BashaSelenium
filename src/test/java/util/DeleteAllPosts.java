package util;

import base.TestBase;
import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;

public class DeleteAllPosts extends TestBase {

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest("Delete Watchlists and Groups", "Delete all watchlists and Groups using user truid")
				.assignCategory("1PAuthoring");
	}

	/**
	 * Method for Delete all public and private watchlist using truid's
	 * 
	 * @param truid
	 * @throws Exception, When watchlists are not getting deleted
	 */

	@Test
	@Parameters({"truid"})
	public void deleteAllPostlist(String truid) throws Exception {
		String truids[] = truid.split("\\|");
		System.out.println("lenght---->" + truids.length);
		for (int j = 0; j < truids.length; j++) {
			try {
				String local = null;
				if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
					local = "N";
				} else {
					local = "Y";
				}
				getAllAppHostsForGivenEnv("http://eureka.us-west-2.dev.oneplatform.build:8080/v2/apps",
						"1p.stable.dev", local);

				RequestSpecification reqSpec = given();
				Response resp;
				Map<String, String> hm = new HashMap<String, String>();
				hm.put("X-1P-User", truids[j]);
				hm.put("Content-Type", "application/json");
				reqSpec.headers(hm);
				logger.info("host name-->" + appHosts.get("1PAUTHORING"));
				resp = reqSpec.when().get(appHosts.get("1PAUTHORING") + "/posts/user/" + truid + "?size=40");
				System.out.println("response code-->" + resp.getStatusCode());
				String jsonResponse = resp.asString();
				System.out.println("response-->" + jsonResponse);
				JsonPath jsonPath = new JsonPath(jsonResponse);
				System.out.println(jsonPath);
				String postCount = jsonPath.getString("size");
				logger.info("Post Count-->" + postCount);

				// Delete posts for the user
				for (int i = 0; i <= Integer.parseInt(postCount); i++) {
					String id = jsonPath.getString("posts[" + i + "].id");
					logger.info("post  id-->" + id);
					reqSpec.when().delete(appHosts.get("1PAUTHORING") + "/posts/post/" + id);
					logger.info("post is deleted-->");
				}

				test.log(LogStatus.PASS, "1PAUTHORING API Called Successfully for truid -->" + truids[j]);
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "1PContainer API Call Failed for truid-->" + truids[j]);
				ErrorUtil.addVerificationFailure(e);
			}
		}
	}

	@Test
	@Parameters({"truid"})
	public void deleteAllDraftlist(String truid) throws Exception {
		String truids[] = truid.split("\\|");
		System.out.println("lenght---->" + truids.length);
		for (int j = 0; j <= truids.length; j++) {
			try {
				String local = null;
				if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
					local = "N";
				} else {
					local = "Y";
				}
				getAllAppHostsForGivenEnv("http://eureka.us-west-2.dev.oneplatform.build:8080/v2/apps",
						"1p.stable.dev", local);

				RequestSpecification reqSpec = given();
				Response resp;
				Map<String, String> hm = new HashMap<String, String>();
				hm.put("X-1P-User", truids[j]);
				hm.put("Content-Type", "application/json");
				reqSpec.headers(hm);
				logger.info("host name-->" + appHosts.get("1PAUTHORING"));
				resp = reqSpec.when().get(appHosts.get("1PAUTHORING") + "/drafts/user/" + truid + "?size=40");
				System.out.println("response code-->" + resp.getStatusCode());
				String jsonResponse = resp.asString();
				System.out.println("response-->" + jsonResponse);
				JsonPath jsonPath = new JsonPath(jsonResponse);
				System.out.println(jsonPath);
				String DraftCount = jsonPath.getString("size");
				logger.info("Draftposts count-->" + DraftCount);
				for (int i = 0; i < Integer.parseInt(DraftCount); i++) {
					String id = jsonPath.getString("drafts[" + i + "].id");
					logger.info("Draft  id-->" + id);
					reqSpec.when().delete(appHosts.get("1PAUTHORING") + "/drafts/draft/" + id);
					logger.info("Draft is deleted-->");
				}

				// logger.info(truids[j]);
				test.log(LogStatus.PASS, "1PAUTHORING API Called Successfully for truid -->" + truids[j]);
			} catch (Exception e) {
				test.log(LogStatus.FAIL, "1PContainer API Call Failed for truid-->" + truids[j]);
				ErrorUtil.addVerificationFailure(e);
			}
		}
	}

}
