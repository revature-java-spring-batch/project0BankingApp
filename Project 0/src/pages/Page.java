package pages;

import java.util.Scanner;

public interface Page {
	static Scanner sc = new Scanner(System.in);
	
	static void newPage() {
		System.out.println("\n\n\n");
	}
}
