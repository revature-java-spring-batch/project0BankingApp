package pages;

import java.util.List;

import dto.JointAccountApplication;
import repository.BankAppRepository;

public class JointApplicationsPage {
	static String headerSpace = "%-8s %-19s %-13s %-16s %-16s %-17s\n\n";
	static String dataSpace = "%-8d %-19d %-13s %-16s %-16s %-17s\n\n";

	public static void displayJointApplications() {
		int input;
		int count = 1;
		List<JointAccountApplication> applications = BankAppRepository.getJointApplications();

		if (applications == null)
			System.out.println("There are no applications on file.");
		else {
			printHeader();
			for (JointAccountApplication app : applications) {
				System.out.format(dataSpace, count++, app.getApplicationId(), "Pending", app.getPrimaryUser(), 
									app.getJointUser(), app.getAccountType().toString());
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
					if (input < 0 && input > applications.size() + 1)
						System.out.println("Invalid choice");
				} while (input < 0 || input > applications.size() + 1);

				rowNum = input;

				System.out.println("1. Approve Account \n2. Deny Account");
				do {
					input = getInput();
					if (input < 0 || input > 3)
						System.out.println("Invalid choice");
				} while (input < 0 || input > 3);

				switch (input) {
				case 1:
					JointAccountApplication app = applications.get(rowNum - 1);
					BankAppRepository.approveOrDenyJointApplication(app.getApplicationId(), 1);
					
					BankAppRepository.createBankAccount(app.getAccountType(), true, app.getPrimaryUser());
					BankAppRepository.createJointAccount(app.getPrimaryUser(), app.getJointUser());
					break;
				default:
					BankAppRepository.approveOrDenyJointApplication(applications.get(rowNum - 1).getApplicationId(), 0);
				}
			}
		}
	}

	static void printHeader() {
		System.out.format(headerSpace, "Row", "Application ID", "Approved", "Joint User1 ", "Joint User2", "Account Type");
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
