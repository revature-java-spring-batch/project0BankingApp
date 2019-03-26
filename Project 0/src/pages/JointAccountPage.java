package pages;

import repository.BankAppRepository;
import users.User;

public class JointAccountPage {
	public static void getUserInput(User pUser) {
		boolean validUser = false;
		String jointUser = null;
		byte input;
		String type;
		
		while(!validUser) {
			System.out.print("Please enter the username of the other joint account user: ");
			
			jointUser = Page.sc.nextLine();
			
			if(BankAppRepository.verifyUserName(jointUser))
				validUser = true;
			else
				System.out.println("Invalid user. Please enter a valid user");
		}
		
		System.out.println("Please enter what type of account you would like: ");
		System.out.println("1. Checking \n2. Savings");
		do {
			input = getInput();
			if (input < 0 && input > 3)
				System.out.println("Invalid choice");
		} while (input < 0 && input > 3);
		if(input == 1)
			type = "Checking";
		else
			type = "Savings";
		
		BankAppRepository.createJointApplication(pUser.getUserName(), jointUser, type);
		System.out.println("Successfully created joint account application");
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
}
