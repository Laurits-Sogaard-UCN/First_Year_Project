package utility;

public enum EmployeeType {
	PARTTIME("Parttime"), FULLTIME("Fulltime"), MANAGER("Manager");
	
	private final String stringType;
	
	EmployeeType(String stringType) {
		this.stringType = stringType;
	}
	
	public String getType() {
		return stringType;
	}
}
