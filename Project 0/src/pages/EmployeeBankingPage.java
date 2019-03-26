package pages;

public class EmployeeBankingPage implements ChoicePage{

	@Override
	public void displayChoices() {
		System.out.println("1. View Open Applications \n2. View Accounts \n3. Exit");
	
	}
	
	public void displayApplicationChoices(){
		System.out.println("1. Bank Applications \n2. Joint Account Applications");
	}

}
