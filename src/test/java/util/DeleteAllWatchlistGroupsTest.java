package util;

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

import base.TestBase;

public class DeleteAllWatchlistGroupsTest extends TestBase {

	@BeforeTest
	public void beforeTest() throws Exception {
		extent = ExtentManager.getReporter(filePath);
		rowData = testcase.get(this.getClass().getSimpleName());
		test = extent.startTest("Delete Watchlists and Groups", "Delete all watchlists and Groups using user truid").assignCategory("1PContainer");
	}
	
	/**
	 * Method for Delete all public and private watchlist using truid's
	 * @param truid
	 * @throws Exception, When watchlists are not getting deleted
	 */
	public  void deleteAllWatchlists(String truid,String containerType) throws Exception {
		String truids[]=truid.split("\\|");
		for(int j=0;j<truids.length;j++) {
		try {
			String local = null;
			if (StringUtils.isNotBlank(System.getenv("SELENIUM_BROWSER"))) {
				local = "N";
			}else{
				local = "Y";
			}
			getAllAppHostsForGivenEnv("http://eureka.us-west-2.dev.oneplatform.build:8080/v2/apps", "1p.stable.dev", local);
			
			RequestSpecification reqSpec = given();
			Response resp;
			Map<String, String> hm= new HashMap<String, String>();
			hm.put("X-1P-User",truids[j]);
			hm.put("Content-Type","application/json");
			reqSpec.headers(hm);
			logger.info("host name-->"+appHosts.get("1PCONTAINER"));
			resp = reqSpec.when().get(appHosts.get("1PCONTAINER")+"/container?type="+containerType);
			String jsonResponse = resp.asString();
			JsonPath jsonPath = new JsonPath(jsonResponse);
			String publicCount=jsonPath.getString("public_count");
			String privateCount=jsonPath.getString("private_count");
			logger.info("publicCount-->"+publicCount);
			logger.info("privateCount-->"+privateCount);
			
			
			//Delete public watchlists/Groups
			for(int i=0;i<Integer.parseInt(publicCount);i++) {
				String id=jsonPath.getString("public["+i+"].id");
				logger.info("public "+containerType+" id-->"+id);
				reqSpec.when().delete(appHosts.get("1PCONTAINER")+"/container/"+id);
			}
			
			//Delete private watchlists/Groups
			for(int i=0;i<Integer.parseInt(privateCount);i++) {
				String id=jsonPath.getString("private["+i+"].id");
				logger.info("private "+containerType+" id-->"+id);
				reqSpec.when().delete(appHosts.get("1PCONTAINER")+"/container/"+id);
			}
			test.log(LogStatus.PASS, "1PContainer API Called Successfully for truid -->"+ truids[j]);
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "1PContainer API Call Failed for truid-->"+truids[j]);
			ErrorUtil.addVerificationFailure(e);
		}
	  }
	}
	
	@Test
	@Parameters({"truid","containerType"})
	public void deletWatchlistAndGroups(String truid,String containerType) throws Exception {
		deleteAllWatchlists(truid, containerType);
	}
	
	public static void main(String[] args) throws Exception {
		new DeleteAllWatchlistGroupsTest().deleteAllWatchlists("f5e86f1f-b0b9-4bb2-ab2b-6b14145344a9","project");
	}
}
