package pages;

public class AdminBankingPage extends EmployeeBankingPage {
	@Override
	public void displayChoices() {
		System.out.println(
				"1. Withdraw \n2. Deposit \n3. Transfer \n4. View Open Applications \n5. View Accounts \n6. Cancel Account \n7. Exit");
	}
}
