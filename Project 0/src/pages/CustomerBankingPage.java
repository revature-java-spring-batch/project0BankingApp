package pages;

public class CustomerBankingPage implements ChoicePage {
	@Override
	public void displayChoices() {
		System.out.println(
				"1. Withdraw \n2. Deposit \n3. Transfer \n4. View Balance \n5. Apply For Joint Account \n6. Exit");
	}

}
