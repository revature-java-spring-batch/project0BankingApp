package pages;

import org.apache.log4j.Logger;

import main.driver.BankApp.TRANSACTION_TYPE;
import repository.BankAppRepository;

public class TransferPage {
	static Logger logger = Logger.getLogger(TransferPage.class);
	private static TRANSACTION_TYPE type = TRANSACTION_TYPE.TRANSFER;

	public static void transfer(long fromId, long toId) {
		String transferAmt;
		boolean validAmt = false;
		double amount = 0;
		while (!validAmt) {
			System.out.print("Please enter transfer amount: ");
			transferAmt = Page.sc.nextLine();
			try {
				amount = Double.parseDouble(transferAmt);
				
				if(amount < 0)
					amount *= -1;
				
				// Validate withdrawal amount
				if (BankAppRepository.verifyFunds(fromId, amount)) {
					validAmt = true;

					if (BankAppRepository.transfer(fromId, toId, amount)) {
						long transactionId = BankAppRepository.createTransaction(type, amount, fromId, toId);

						String transaction = String.format(
								"Transaction Id = %d  Transaction Type= %s  Transaction Amount=$ %f  Account Id From= %d  Account Id To= %d",
								transactionId, type.toString(), amount, fromId, toId);
						logger.info(transaction);

						System.out.println("Transfer Successful");
					} else
						System.out.println("Transfer not successful");
				} else
					System.out.println("Insufficient funds. Enter a valid amount.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		}
	}
}
