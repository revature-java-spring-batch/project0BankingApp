package pages;

public class AdminBankingPage implements ChoicePage{

	@Override
	public void displayChoices() {
		System.out.println("1. Withdraw \n2. Deposit \n3. Transfer \n4. View Open Applications \n5. View Account \n6. Edit Account \n7. Exit");
	}

}
