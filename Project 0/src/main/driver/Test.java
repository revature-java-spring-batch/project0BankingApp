package main.driver;

import java.util.Scanner;

import users.Customer;
import users.User.USER;

public class Test {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		Customer c = new Customer("jtran", "4160", USER.CUSTOMER, "Johnny", "Tran");
		if(c.successfulLogin("jtran", "4160"))
			System.out.println("Success");
		else
			System.out.println("Fail");
	}
	
}
