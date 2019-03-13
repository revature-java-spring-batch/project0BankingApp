package users;

public class User {
	private String userName;
	private String password;
	private USER userType;
	
	public enum USER{
		CUSTOMER, EMPLOYEE, ADMIN;
	}
	
	User(){
		userName = "";
		password = "";
	}
	
	User(String user, String pass, USER type){
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

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
