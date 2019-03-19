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
	
	User(String user, String pass, USER type){
		numUsers++;
		userName = user;
		password = pass;
		userType = type;
	}
	
	public boolean successfulLogin(String user, String pass) {
		return (user == userName && pass == password);
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
