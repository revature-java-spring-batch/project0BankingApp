package pages;

import java.util.Scanner;

abstract public class Page {
	static Scanner sc = new Scanner(System.in);
	
	static int getChoice() {
		System.out.print("Please enter your choice: ");
		return sc.nextInt();
	}
	
	abstract void displayChoices();
}
