package bank;

public class Account {
	private long accountNumber;
	private double balance;
	private String accountType;
	
	public long getAccountNumber() {
		return accountNumber;
	}
	public double getBalance() {
		return balance;
	}
	public String getAccountType() {
		return accountType;
	}
	
	public void withdraw(double amount) {
		if(amount <= 0)
			System.out.println("Error: Amount entered is zero or negative.");
		else {
			balance -= amount;
		}
	}
	
	public void deposit(double amount) {
		if(amount <= 0)
			System.out.println("Error: Amount entered is zero or negative.");
		else{
			balance += amount;
		}
	}
}
