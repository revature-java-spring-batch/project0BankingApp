package users;

public class User {
	private String userName;
	private String password;
	private USER userType;
	private static int numUsers = 0;
	
	public enum USER{
		CUSTOMER, EMPLOYEE, ADMIN;
	}
	
	User(){
		userName = "";
		password = "";
	}
	
	public User(String user, String pass) {
		this.userName = user;
		this.password = pass;
	}
	
	User(String userName, String password, USER userType){
		numUsers++;
		this.userName = userName;
		this.password = password;
		this.userType = userType;
	}
	
	@Override
	public int hashCode() {
		
		return userName.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.userName.equals((String)(obj));
	}
	
	public USER getUserType() {
		return userType;
	}
	
	public static int getNumUsers() {
		return numUsers;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
