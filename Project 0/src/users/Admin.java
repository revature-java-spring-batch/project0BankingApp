package users;

public class Admin extends User{
	public Admin(String user, String pass) {
		super(user, pass, USER.ADMIN);
	}
}
