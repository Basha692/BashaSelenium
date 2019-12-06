package util;

public class RowData {

	private String testclassName;
	private String testcaseDescription;
	private String testcaseId;
	private String testcaseRunmode;
	private String testResults;

	public RowData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RowData(String testclassName,
			String testcaseDescription,
			String testcaseId,
			String testcaseRunmode,
			String testResults) {
		super();
		this.testclassName = testclassName;
		this.testcaseDescription = testcaseDescription;
		this.testcaseId = testcaseId;
		this.testcaseRunmode = testcaseRunmode;
		this.testResults = testResults;
	}

	public String getTestclassName() {
		return testclassName;
	}

	public void setTestclassName(String testclassName) {
		this.testclassName = testclassName;
	}

	public String getTestcaseDescription() {
		return testcaseDescription;
	}

	public void setTestcaseDescription(String testcaseDescription) {
		this.testcaseDescription = testcaseDescription;
	}

	public String getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(String testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getTestcaseRunmode() {
		return testcaseRunmode;
	}

	public void setTestcaseRunmode(String testcaseRunmode) {
		this.testcaseRunmode = testcaseRunmode;
	}

	public String getTestResults() {
		return testResults;
	}

	public void setTestResults(String testResults) {
		this.testResults = testResults;
	}

	@Override
	public String toString() {
		return "RowData [testclassName=" + testclassName + ", testcaseDescription=" + testcaseDescription
				+ ", testcaseId=" + testcaseId + ", testcaseRunmode=" + testcaseRunmode + ", testResults=" + testResults
				+ "]";
	}

}
