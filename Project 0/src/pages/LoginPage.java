package pages;

public class LoginPage extends Page{
	@Override
	void displayChoices() {
		System.out.println("1. Login \n2. Register \n3. Exit");
	}
	
	/*public boolean login(String user, String pass) {
		
	}*/
	public static void main(String[] args) {
		LoginPage lp = new LoginPage();
		lp.displayChoices();
	}

	
}
