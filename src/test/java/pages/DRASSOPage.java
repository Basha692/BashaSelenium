package pages;

import static com.jayway.restassured.RestAssured.baseURI;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.with;
import static com.jayway.restassured.specification.ProxySpecification.host;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.jayway.restassured.path.xml.element.Node;

import base.TestBase;
import util.OnePObjectMap;

public class DRASSOPage extends TestBase {

	protected static String local = "";

	public DRASSOPage(WebDriver ob) {
		this.ob = ob;
		pf = new PageFactory();
	}

	

	public void loginDRAApp(String email,
			String pass) throws Exception {
		ob.findElement(By.name(OnePObjectMap.DRA_SSO_LOGIN_PAGE_USERNAME_NAME.toString())).sendKeys(email);
		ob.findElement(By.name(OnePObjectMap.DRA_SSO_LOGIN_PAGE_PASSWORD_NAME.toString())).sendKeys(pass);
		pf.getBrowserActionInstance(ob).jsClick(OnePObjectMap.DRA_SSO_LOGIN_PAGE_LOGIN_BUTTON_CSS);

	}

	public void checkSuspendUserErrorMessage() {
		pf.getBrowserWaitsInstance(ob).waitUntilText("Your account has been suspended");
		String errorMessage1 = ob
				.findElement(By.xpath(OnePObjectMap.DRA_SSO_SUSPEND_PAGE_ERROR_MESSAGE_XPATH.toString())).getText();
		String errorMessage2 = ob
				.findElement(By.xpath(OnePObjectMap.DRA_SSO_SUSPEND_PAGE_ERROR_MESSAGE_TEXT_XPATH.toString()))
				.getText();
		Assert.assertEquals(errorMessage1, "Your account has been suspended.");
		Assert.assertEquals(errorMessage2, "Please contact community.info@clarivate.com to unlock your account.");

	}

	public void deleteUserUsingXRPC(String userId) throws Exception{
		String sid = getSessionId();
		deleteUser(sid, userId);
	}

	
	@SuppressWarnings("rawtypes")
	private void deleteUser(String sid,
			String userId)throws Exception {

		String deleteUser = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<request xmlns=\"http://www.isinet.com/xrpc40\">" + "<fn name=\"STEAMAdminAPI.loadAdminSession\">"
				+ "<list>" + "<val name=\"SID\">" + sid + "</val>" + "<map name=\"clientInfo\">"
				+ "<val name=\"COMPONENT\">ESTI</val>" + "<val name=\"VERSION\">1</val>"
				+ "<val name=\"ENVIRONMENT\">steam</val>" + "</map>" + "</list>" + "</fn>"
				+ "<fn name=\"STEAMCoreAPI.deleteObject\">" + "<list>" + "<val name=\"type\">USER</val>"
				+ "<val name=\"UserId\">" + userId + "</val>" + "</list>" + "</fn>" + "</request>";

		String userResponse;
		String errorStatus="";
		try {

			userResponse = given().body(deleteUser).when().post(baseURI).thenReturn().asString();
			logger.info(userResponse);
			Node fnResponse1 = with(userResponse).get("userResponse.fn[1]");
			Map hm = fnResponse1.attributes();
			String rc = (hm.get("rc")).toString();
			if (rc.equalsIgnoreCase("OK")) {
				logger.info("USER DELETED");
			}else if(rc.equalsIgnoreCase("30000")){
				errorStatus = with(userResponse).get("userResponse.fn[1].error");
				logger.info("ERROR MSG : "+errorStatus);
				
			} else{
				throw new Exception("Somthing happen");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("rawtypes")
	private String getSessionId() {

		// CI Environemt URL
		baseURI = "http://10.205.147.235:5000/esti/xrpc";
		String UserName = "mahesh.morsu@thomsonreuters.com";
		String Password = "Neon@123";
		String SID = null;

		/**
		 * //Spring (Stable) environemt URL baseURI = "http://10.204.33.120:8003/esti/xrpc";
		 * 
		 * String UserName = "Janardhan.reddyelluru@thomsonreuters.com"; String Password = "Pickle32@";
		 */
		String response = null;
		String myEnvelope = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ " <request xmlns=\"http://www.isinet.com/xrpc40\">" + " <fn name=\"STEAMAdminAPI.loginAdmin\">"
				+ "<list>" + " <val name=\"Username\">" + UserName + "</val>" + "<val name=\"Password\">" + Password
				+ "</val>" + "<map name=\"clientInfo\">" + " <val name=\"COMPONENT\">ESTI</val>"
				+ "<val name=\"VERSION\">1</val>" + "<val name=\"ENVIRONMENT\">steam</val>" + "</map>" + "</list>"
				+ "</fn>" + "</request>";
		try {
			
			response = given().body(myEnvelope).when().post(baseURI).thenReturn().asString();
			logger.info(response);
			Node fnResponse = with(response).get("response.fn[0]");

			Map hm = fnResponse.attributes();
			String rc = (hm.get("rc")).toString();
			if (rc.equalsIgnoreCase("ok")) {
				SID = with(response).get("response.fn.val");
			}
		} catch (Exception ex) {
			logger.info("====== Exception in getting SID ===== " + ex.getMessage());
			SID = "Exception occured while getting Admin SID: " + ex.getMessage();
			ex.printStackTrace();
		}
		return SID;

	}

}
