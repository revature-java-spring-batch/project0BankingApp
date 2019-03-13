package pages;

import java.util.Scanner;

abstract public class Page {
	Scanner sc = new Scanner(System.in);
	public int getInput() {
		return sc.nextInt();
	}
	
	abstract void displayChoices();
}
