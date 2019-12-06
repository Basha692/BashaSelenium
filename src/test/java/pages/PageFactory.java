package pages;

import org.openqa.selenium.WebDriver;

//import Authoring.Authoring;
import Authoring.LoginTR;
import util.BrowserAction;
import util.BrowserWaits;
import util.Utility;
import watpages.AuthorRecordPage;
import watpages.SearchAuthorClusterPage;
import watpages.SearchAuthorClusterResultsPage;
import watpages.WATLogInPage;
import watpages.WATProfilePage;

public class PageFactory {

	private SearchProfile searchProfilePage = null;
	private ProfilePage profilePage = null;
	private PostRecordViewPage postRVPage = null;
	private SearchResultsPage searchResultsPage = null;
	private Watchlist WatchlistPage = null;
	private HeaderFooterLinksPage hfPage = null;
	private BrowserAction browserAction = null;
	private BrowserWaits browserWaits = null;
	private LoginTR loginTR = null;
	// private Authoring authoring = null;
	private OnboardingModalsPage onboarding = null;
	private AccountPage accountPage = null;
	private ENWReferencePage enwReferencePage = null;
	private EnwReference enwReference = null;
	private LinkingModalsPage linkingModalsPage = null;
	private GroupDetailsPage groupDetailsPage = null;
	private GroupsListPage groupsListPage = null;
	private GroupInvitationPage groupInvitationPage = null;
	private GroupsPage groupsPage = null;
	private Utility utility = null;
	private NewsfeedPage newsfeedPage = null;
	private DRAPage draPage = null;
	private DRASSOPage draSSOPage=null;
	private IPASearch searchPage = null;
	private IPApage ipapage = null;
	private IAMPage iamPage = null;
	private PUBLONSPage pubPage=null;
	private CustomercarePage customercarePage = null;
	private IpaSavedSearchDetailsPage ipasavedsearchpage = null;
	private DashboardPage dashboardPage = null;
	private IPARecordViewPage rvpage = null;
	private GmailLoginPage gmpage = null;
	private CreatePostAndCommentPage postncommentpage = null;
	
	private WATLogInPage watLogInPage = null;
	private SearchAuthorClusterPage SearchAuthClusterPage = null;
	private SearchAuthorClusterResultsPage searchAuthorClusterResultsPage=null;
	private AuthorRecordPage authorRecordPage=null;
	private WATProfilePage watProfilePage=null;

	
	public IAMPage getIamPage(WebDriver ob) {

		if (iamPage == null) {
			iamPage = new IAMPage(ob);
		}
		return iamPage;
	}
	
	public PUBLONSPage getPubPage(WebDriver ob) {

		if (pubPage == null) {
			pubPage = new PUBLONSPage(ob);
		}
		return pubPage;
	}

	public IPASearch getSearchPageInstance(WebDriver ob) {
		if (searchPage == null) {
			searchPage = new IPASearch(ob);
		}
		return searchPage;

	}

	public CreatePostAndCommentPage getPostCommentPageInstance(WebDriver ob) {

		if (postncommentpage == null) {
			postncommentpage = new CreatePostAndCommentPage(ob);
		}
		return postncommentpage;
	}

	public SearchProfile getSearchProfilePageInstance(WebDriver ob) {
		if (searchProfilePage == null) {
			searchProfilePage = new SearchProfile(ob);
		}

		return searchProfilePage;

	}

	public ProfilePage getProfilePageInstance(WebDriver ob) {
		if (profilePage == null) {
			profilePage = new ProfilePage(ob);
		}

		return profilePage;

	}

	public PostRecordViewPage getpostRVPageInstance(WebDriver ob) {
		if (postRVPage == null) {
			postRVPage = new PostRecordViewPage(ob);
		}

		return postRVPage;

	}

	public SearchResultsPage getSearchResultsPageInstance(WebDriver ob) {
		if (searchResultsPage == null) {
			searchResultsPage = new SearchResultsPage(ob);
		}

		return searchResultsPage;

	}

	public Watchlist getWatchlistPageInstance(WebDriver ob) {
		if (WatchlistPage == null) {
			WatchlistPage = new Watchlist(ob);
		}

		return WatchlistPage;

	}

	public HeaderFooterLinksPage getHFPageInstance(WebDriver ob) {
		if (hfPage == null) {
			hfPage = new HeaderFooterLinksPage(ob);
		}

		return hfPage;

	}

	public void setHFPageInstance(HeaderFooterLinksPage obj) {
		hfPage = obj;
	}

	public BrowserAction getBrowserActionInstance(WebDriver ob) {
		if (browserAction == null) {
			browserAction = new BrowserAction(ob);
		}

		return browserAction;

	}

	public BrowserWaits getBrowserWaitsInstance(WebDriver ob) {
		if (browserWaits == null) {
			browserWaits = new BrowserWaits(ob);
		}

		return browserWaits;

	}
	//
	// public Authoring getAuthoringInstance(WebDriver ob) {
	// if (authoring == null) {
	// authoring = new Authoring(ob);
	// }
	//
	// return authoring;
	//
	// }

	public LoginTR getLoginTRInstance(WebDriver ob) {
		if (loginTR == null) {
			loginTR = new LoginTR(ob);
		}

		return loginTR;

	}

	public void setLoginTRInstance(LoginTR obj) {
		loginTR = obj;
	}

	public OnboardingModalsPage getOnboardingModalsPageInstance(WebDriver ob) {
		if (onboarding == null) {
			onboarding = new OnboardingModalsPage(ob);
		}

		return onboarding;

	}

	public AccountPage getAccountPageInstance(WebDriver ob) {
		if (accountPage == null) {
			accountPage = new AccountPage(ob);
		}

		return accountPage;

	}

	public ENWReferencePage getENWReferencePageInstance(WebDriver ob) {
		if (enwReferencePage == null) {
			enwReferencePage = new ENWReferencePage(ob);
		}

		return enwReferencePage;

	}

	// Arvind ENWReference method
	public EnwReference getEnwReferenceInstance(WebDriver ob) {
		if (enwReference == null) {
			enwReference = new EnwReference(ob);
		}

		return enwReference;
	}

	public LinkingModalsPage getLinkingModalsInstance(WebDriver ob) {

		if (linkingModalsPage == null) {
			linkingModalsPage = new LinkingModalsPage(ob);
		}
		return linkingModalsPage;
	}

	public void setAccountPageInstance(AccountPage obj) {
		accountPage = obj;
	}

	public GroupDetailsPage getGroupDetailsPage(WebDriver ob) {

		if (groupDetailsPage == null) {
			groupDetailsPage = new GroupDetailsPage(ob);
		}
		return groupDetailsPage;
	}

	public GroupsListPage getGroupsListPage(WebDriver ob) {

		if (groupsListPage == null) {
			groupsListPage = new GroupsListPage(ob);
		}
		return groupsListPage;
	}

	public GroupInvitationPage getGroupInvitationPage(WebDriver ob) {

		if (groupInvitationPage == null) {
			groupInvitationPage = new GroupInvitationPage(ob);
		}
		return groupInvitationPage;
	}

	public GroupsPage getGroupsPage(WebDriver ob) {

		if (groupsPage == null) {
			groupsPage = new GroupsPage(ob);
		}
		return groupsPage;
	}

	public Utility getUtility(WebDriver ob) {

		if (utility == null) {
			utility = new Utility(ob);
		}
		return utility;
	}

	public NewsfeedPage getNewsfeedPageInstance(WebDriver ob) {
		if (newsfeedPage == null) {
			newsfeedPage = new NewsfeedPage(ob);
		}
		return newsfeedPage;
	}

	public DRAPage getDraPageInstance(WebDriver ob) {

		if (draPage == null) {
			draPage = new DRAPage(ob);
		}
		return draPage;
	}
	
	public DRASSOPage getDraSSOPageInstance(WebDriver ob) {

		if (draSSOPage == null) {
			draSSOPage = new DRASSOPage(ob);
		}
		return draSSOPage;
	}
	

	public IPApage getIpaPage(WebDriver ob) {
		if (ipapage == null) {
			ipapage = new IPApage(ob);
		}

		return ipapage;

	}

	public WATLogInPage getWatPageInstance(WebDriver ob) {

		if (watLogInPage == null) {
			watLogInPage = new WATLogInPage(ob);
		}
		return watLogInPage;
	}

	public CustomercarePage getCustomercarePage(WebDriver ob) {
		if (customercarePage == null) {
			customercarePage = new CustomercarePage(ob);
		}

		return customercarePage;

	}

	public IpaSavedSearchDetailsPage getIpaSavedSearchpage(WebDriver ob) {
		if (ipasavedsearchpage == null) {
			ipasavedsearchpage = new IpaSavedSearchDetailsPage(ob);
		}
		return ipasavedsearchpage;
	}

	public DashboardPage getDashboardPage(WebDriver ob) {
		if (dashboardPage == null) {
			dashboardPage = new DashboardPage(ob);
		}

		return dashboardPage;
	}

	public IPARecordViewPage getIpaRecordViewPage(WebDriver ob) {
		if (rvpage == null) {
			rvpage = new IPARecordViewPage(ob);
		}
		return rvpage;
	}

	public GmailLoginPage getGmailLoginPage(WebDriver ob) {
		if (gmpage == null) {
			gmpage = new GmailLoginPage(ob);
		}
		return gmpage;
	}
	
	
	/**
	 * This Method provides Search author cluster page object to access the common methods related to search author
	 * cluster page.
	 * 
	 * @throws
	 * 
	 */
	public SearchAuthorClusterPage getSearchAuthClusterPage(WebDriver ob) {

		if (SearchAuthClusterPage == null) {
			SearchAuthClusterPage = new SearchAuthorClusterPage(ob);
		}
		return SearchAuthClusterPage;
	}
	
	/**
	 * Method for Provide Search Author Results page methods
	 * @param ob
	 * @return
	 */
	public SearchAuthorClusterResultsPage getSearchAuthClusterResultsPage(WebDriver ob) {

		if (searchAuthorClusterResultsPage == null) {
			searchAuthorClusterResultsPage = new SearchAuthorClusterResultsPage(ob);
		}
		return searchAuthorClusterResultsPage;
	}
	
	/**
	 * Method for Provide Author Record page methods
	 * @param ob
	 * @return
	 */
	public AuthorRecordPage getAuthorRecordPage(WebDriver ob) {

		if (authorRecordPage == null) {
			authorRecordPage = new AuthorRecordPage(ob);
		}
		return authorRecordPage;
	}


	/**
	 * Method for Provide WAT Profile page methods
	 * @param ob
	 * @return
	 */
	public WATProfilePage getWatProfilePage(WebDriver ob) {

		if (watProfilePage == null) {
			watProfilePage = new WATProfilePage(ob);
		}
		return watProfilePage;
	}
	
	public void clearAllPageObjects() {
		searchResultsPage = null;
		accountPage = null;
		loginTR = null;
		onboarding = null;
		// authoring = null;
		browserWaits = null;
		browserAction = null;
		hfPage = null;
		searchProfilePage = null;
		profilePage = null;
		postRVPage = null;
		WatchlistPage = null;
		enwReference = null;
		enwReferencePage = null;
		linkingModalsPage = null;
		groupDetailsPage = null;
		groupsPage = null;
		groupInvitationPage = null;
		groupsListPage = null;
		utility = null;
		draPage = null;
		ipapage = null;
		ipasavedsearchpage = null;
		dashboardPage = null;
		rvpage = null;
		gmpage = null;
		postncommentpage = null;
		searchAuthorClusterResultsPage=null;
		authorRecordPage=null;
		watProfilePage=null;
		pubPage=null;
	}
}
