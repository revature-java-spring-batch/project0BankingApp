package pages;

import java.util.List;
import dto.BankAccountApplication;
import repository.BankAppRepository;
import users.Customer;

public class OpenApplicationPage {
	static String headerSpace = "%-8s %-19s %-13s %-13s %-15s %-14s %-17s %-17s\n\n";
	static String dataSpace = "%-8d %-19d %-13s %-13s %-15s %-14s %-17s %-17s\n\n";

	public static void displayBankApplications() {
		int input;
		int count = 1;
		List<BankAccountApplication> applications = BankAppRepository.getBankApplications();

		if (applications == null || applications.size() == 0)
			System.out.println("There are no applications on file.");
		else {
			printHeader();
			for (BankAccountApplication app : applications) {
				Customer temp = app.getCustomer();
				System.out.format(dataSpace, count++, app.getApplicationId(), temp.getUserName(), "Pending",
						temp.getFirstName(), temp.getLastName(), temp.getAddress(), temp.getPhoneNumber());
			}

			System.out.println("1. Perform action \n2. exit");
			do {
				input = getInput();
				if (input < 0 || input > 3)
					System.out.println("Invalid choice");
			} while (input < 0 || input > 3);

			if (input == 1) {
				int rowNum;
				System.out.print("Enter the row number of the application: ");
				do {
					input = getInput();
					if (input < 0 || input > applications.size() + 1)
						System.out.println("Invalid choice");
				} while (input < 0 || input > applications.size() + 1);

				rowNum = input;

				System.out.println("1. Approve Account \n2. Deny Account \n3. Cancel");
				do {
					input = getInput();
					if (input < 0 || input > 4)
						System.out.println("Invalid choice");
				} while (input < 0 || input > 4);

				switch (input) {
				case 1:
					Customer customer;
					BankAccountApplication app = applications.get(rowNum - 1);
					if(BankAppRepository.approveOrDenyBankApplication(app.getApplicationId(), 1))
						System.out.println("Bank Application Approved.");

					customer = app.getCustomer();

					BankAppRepository.createBankAccount(app.getType(), false, customer.getUserName());

					if (!BankAppRepository.customerExists(customer.getUserName()))
						BankAppRepository.createCustomer(customer.getUserName(), customer.getFirstName(),
								customer.getLastName(), customer.getAddress(), customer.getPhoneNumber());
					break;
				case 2:
					if(BankAppRepository.approveOrDenyBankApplication(applications.get(rowNum - 1).getApplicationId(), 0))
						System.out.println("Bank Application Denied");
					break;
				}
			}
		}
	}

	static void printHeader() {
		System.out.format(headerSpace, "Row", "Application ID", "Username", "Approved", "First Name ", "Last Name",
				"Address", "Phone Number");
	}

	private static int getInput() {
		String choice;
		int input;
		do {
			try {
				choice = Page.sc.nextLine();
				input = Integer.parseInt(choice);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
			}
		} while (true);
		return input;
	}
}
