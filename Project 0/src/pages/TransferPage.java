package pages;

import bank.Account;

public class TransferPage {
	public double getTransferAmt(Account account) {
		String transferAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter transfer amount: ");
			transferAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(transferAmt);
				// Validate withdrawal amount
				if (account.getBalance() > amount)
					validAmt = true;
				else
					System.out.println("Insufficient funds. Enter a valid amount.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
		return amount;
	}
}
