package utility;

public enum EmployeeType {
	
	PARTTIME("Parttime"), FULLTIME("Fulltime"), MANAGER("Manager");
	
	private final String stringType;
	
	/**
	 * Constructor to initialize instance variables. 
	 * @param stringType
	 */
	EmployeeType(String stringType) {
		this.stringType = stringType;
	}
	
	/**
	 * Gets string type.
	 * @return stringType
	 */
	public String getType() {
		return stringType;
	}
}
