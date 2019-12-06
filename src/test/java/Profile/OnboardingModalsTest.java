package Profile;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;

import base.TestBase;
import util.ErrorUtil;

public class OnboardingModalsTest extends TestBase {

	/**
	 * Method for set onboarding status is false to get onboarding modals
	 * @param truid
	 * @throws Exception, When onboarding status is not getting changed
	 */
	@BeforeClass
	@Parameters("truid")
	public  void setOnboardingStatus(String truid) throws Exception {
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
			hm.put("X-1P-User",truid);
			hm.put("Content-Type","application/json");
			reqSpec.headers(hm);
			reqSpec.body("{\"truid\":\""+truid+"\",\"onboarded\":\"false\"}");
			logger.info("host name-->"+appHosts.get("1PPROFILE"));
			resp = reqSpec.when().put(appHosts.get("1PPROFILE")+"/users/user/"+truid+"/status/onboarded");
			String jsonResponse = resp.asString();
			logger.info("response-->"+jsonResponse); 
			JsonPath jsonPath = new JsonPath(jsonResponse);
			String onboarding=jsonPath.getString("onboarded");
			logger.info("onboarding status-->"+onboarding);
			test.log(LogStatus.PASS, "Onboarding API Called Successfully");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Onboarding API Call Failed");
			ErrorUtil.addVerificationFailure(e);
		}
		
	}
	
}
