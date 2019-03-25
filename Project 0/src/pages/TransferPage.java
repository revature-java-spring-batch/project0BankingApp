package pages;

import repository.BankAppRepository;

public class TransferPage {
	public static void transfer(long fromId, long toId) {
		String transferAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.println("Please enter transfer amount: ");
			transferAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(transferAmt);
				// Validate withdrawal amount
				if (BankAppRepository.verifyFunds(fromId, amount)) {
					validAmt = true;
					BankAppRepository.transfer(fromId, toId, amount);
				}
				else
					System.out.println("Insufficient funds. Enter a valid amount.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
		
	}
}
