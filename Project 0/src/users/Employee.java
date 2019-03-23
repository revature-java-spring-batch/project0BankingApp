package users;

public class Employee extends User{
	public Employee(String user, String pass) {
		super(user, pass, USER.EMPLOYEE);
	}
}
