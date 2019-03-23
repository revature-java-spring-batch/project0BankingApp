package pages;

public interface ChoicePage extends Page{
	
	void displayChoices();
	
	static int getChoice() {
		System.out.print("Please enter your choice: ");
		return sc.nextInt();
	}
}
