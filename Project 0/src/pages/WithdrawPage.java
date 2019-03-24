package pages;

import repository.BankAppRepository;

public class WithdrawPage {
	public static void withdraw(long accountId) {
		String withdrawalAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter withdrawal amount: ");
			withdrawalAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(withdrawalAmt);
				// Validate withdrawal amount
				
				
				if (BankAppRepository.withdraw(accountId, amount) == 1)
					validAmt = true;
				else
					System.out.println("Insufficient funds. Enter a valid amount");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
	}
	
	public static void main(String[] args) {
		withdraw(1011001001l);
	}
}
