package pages;

import dto.Account.ACCOUNTTYPE;
import dto.PersonalInfo;
import repository.BankAppRepository;

public class ApplyAccountPage {
	private PersonalInfo personInfo;

	public void retrievePersonalInfo(String user) {
		String firstName = null, lastName = null, address = null, phoneNumber = null;

		boolean isValidFirst = false, isValidLast = false, isValidAddress = false, isValidPhoneNum = false,
				isValidType = false;
		ACCOUNTTYPE type = null;

		while (!isValidFirst) {
			System.out.print("Please enter your first name: ");
			firstName = Page.sc.next();

			if (firstName.length() != 0)
				isValidFirst = true;
			else
				System.out.println("Invalid first name");
		}

		while (!isValidLast) {
			System.out.print("Please enter your last name: ");
			lastName = Page.sc.next();

			if (lastName.length() != 0)
				isValidLast = true;
			else
				System.out.println("Invalid last name");
		}

		while (!isValidAddress) {
			System.out.print("Please enter your Address: ");
			address = Page.sc.nextLine();

			if (address.length() != 0)
				isValidAddress = true;
			else
				System.out.println("Invalid address");
		}

		while (!isValidPhoneNum) {
			System.out.print("Please enter your phone number: ");
			phoneNumber = Page.sc.next();

			if (phoneNumber.length() == 10)
				isValidPhoneNum = true;
			else
				System.out.println("Invalid phone number");
		}

		// Ask user for account type
		while (!isValidType) {
			byte input = 0;
			System.out.println("Please enter account type");
			System.out.println("1. Checking \n2. Savings");

			do {
				input = getInput();
				if (input < 0 || input > 3)
					System.out.println("Invalid choice");
			} while (input < 0 || input > 3);

			if (input == 1) {
				type = ACCOUNTTYPE.CHECKING;
				isValidType = true;
			}
			else {
				isValidType = true;
				type = ACCOUNTTYPE.SAVINGS;
			}
		}

		personInfo = new PersonalInfo(firstName, lastName, address, phoneNumber);
		BankAppRepository.createBankApplication(personInfo, user, type);
	}

	private static byte getInput() {
		String choice;
		byte input;
		do {
			try {
				choice = Page.sc.nextLine();
				input = Byte.parseByte(choice);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		} while (true);
		return input;
	}

	public PersonalInfo getPersonInfo() {
		return personInfo;
	}
}
