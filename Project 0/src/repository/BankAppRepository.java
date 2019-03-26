package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.Account;
import dto.Account.ACCOUNTTYPE;
import dto.BankAccountApplication;
import dto.JointAccountApplication;
import dto.PersonalInfo;
import main.driver.BankApp.TRANSACTION_TYPE;
import users.Customer;
import users.User;
import users.User.USER;
import util.DBUtil;

public class BankAppRepository {
	private static Connection conn = null;
	private static PreparedStatement st = null;
	private static ResultSet rs = null;
	
	static {
		conn = null;
		st = null;
		rs = null;
	}
	
	public static User verifyUser(String user, String pass) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from users where username=? and password=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, pass);
			rs = st.executeQuery();

			if (rs.next()) {
				USER type;
				String userType = rs.getString(3);
				
				if(userType.equals("Customer"))
					type = USER.CUSTOMER;
				else if(userType.equals("Employee"))
					type = USER.EMPLOYEE;
				else {
					type = USER.ADMIN;
				}
				User tempUser = new User(rs.getString(1), rs.getString(2), type);
				return tempUser;
			}
			else
				return null;
		} catch (SQLException e) {
			System.out.println("Error. Could not verify login. Try again later.");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	public static boolean verifyUserName(String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from users where username=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;

			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}
	
	public static boolean verifyFunds(long account_id, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts where account_id=? and balance >= ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, account_id);
			st.setDouble(2, amount);

			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}

	public static boolean verifyAccount(long account_id) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts where account_id=?";
			st = conn.prepareStatement(sql);
			st.setLong(1, account_id);

			rs = st.executeQuery();

			if (rs.next() && rs.getInt(1) == 1)
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later.");
		} finally {
			closeConnections();
		}
		return false;
	}

	public static Account getAccount(long accId) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts where account_id=?";
			st = conn.prepareStatement(sql);
			st.setLong(1, accId);

			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				return new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean("joint_account"), rs.getBoolean(5), rs.getString(6));
			}
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	public static List<Account> getUserAccounts(String user) {
		List<Account> accList = new ArrayList<>();
		
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts where username=?";
			st = conn.prepareStatement(sql);
			st.setString(1, user);

			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				accList.add(new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean("joint_account"), rs.getBoolean(5), rs.getString(6)));
			}
			return accList;
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	public static List<Customer> getCustomerInfo(int low, int high) {
		List<Customer> custList = new ArrayList<>();
		
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from customers where rownum between ? and ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, low);
			st.setInt(2, high);

			rs = st.executeQuery();
			
			//String user, USER type, String firstName, String lastName, String address, String phoneNumber
			while(rs.next()) {
				custList.add(new Customer(rs.getString("username"), USER.CUSTOMER, rs.getString("first_name"), 
							rs.getString("last_name"), rs.getString("address"), rs.getString("phone_number")));
			}
			return custList;
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later." + e.getMessage());
		} finally {
			closeConnections();
		}
		
		return null;
	}
	
	public static List<Account> getAccountInfo() {
		List<Account> accounts = new ArrayList<>();
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				String type = rs.getString(3);

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				
				accounts.add(new Account(rs.getLong(1), rs.getDouble(2), temp, rs.getBoolean("joint_account"), rs.getBoolean(5), rs.getString(6)));
			}

			return accounts;
		} catch (SQLException e) {
			System.out.println("Error. Could not get account info. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}

	public static int withdraw(Long accountId, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set balance = balance - ? where account_id = ? and balance >= ?";
			st = conn.prepareStatement(sql);
			st.setDouble(1, amount);
			st.setLong(2, accountId);
			st.setDouble(3, amount);

			int result = st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
			return result;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform withdrawal. Try again later");
		} finally {
			closeStatementConnection();
		}
		return 0;
	}

	public static int deposit(Long accountId, double amount) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set balance = balance + ? where account_id = ? and balance >= ?";
			st = conn.prepareStatement(sql);
			st.setDouble(1, amount);
			st.setLong(2, accountId);
			st.setDouble(3, amount);

			int result = st.executeUpdate();

			try { conn.commit(); } catch (Exception e) { }
			return result;
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform deposit. Try again later");
		} finally {
			closeStatementConnection();
		}
		return 0;
	}

	public static int transfer(long fromAccountId, long toAccountId, double amount) {
		PreparedStatement withdrawSt = null;
		PreparedStatement depositSt = null;
		int depStatus = 0;
		int withStatus = 0;
		String depSql = "update bank_accounts set balance = balance + ? where account_id = ? and balance >= ?";
		String withSql = "update bank_accounts set balance = balance - ? where account_id = ? and balance >= ?";

		try {
			conn = DBUtil.getInstance();
			withdrawSt = conn.prepareStatement(withSql);
			depositSt = conn.prepareStatement(depSql);

			withdrawSt.setDouble(1, amount);
			withdrawSt.setLong(2, fromAccountId);
			withdrawSt.setDouble(3, amount);

			depositSt.setDouble(1, amount);
			depositSt.setLong(2, toAccountId);
			depositSt.setDouble(3, amount);

			withStatus = withdrawSt.executeUpdate();
			depStatus = depositSt.executeUpdate();

			// If one fails, reverse the actions and print error message. Do not create new
			// transaction record
			if (depStatus == 1 && withStatus == 1) {
				try { conn.commit(); } catch (Exception e) { }
				return 1;
			} else {
				conn.rollback();
				return 0;
			}
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not perform transfer. Try again later.");
		} finally {
			try { depositSt.close(); } catch (Exception e) { }
			try { withdrawSt.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
		}
		return 0;
	}

	public static void addNewUser(String user, String pass) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into users values(?, ?, 'Customer')";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, pass);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not add user. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void createBankApplication(PersonalInfo pi, String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into bank_applications (username, first_name, last_name, address, phone_number) values(?,?,?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, pi.getFirstName());
			st.setString(3, pi.getLastName());
			st.setString(4, pi.getAddress());
			st.setString(5, pi.getPhoneNumber());
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void approveOrDenyBankApplication(long appId, int i) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_applications set approved = ? where application_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, i);
			st.setLong(2, appId);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not find application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void approveOrDenyJointApplication(long appId, int i) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update joint_account_applications set approved = ? where application_id = ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, i);
			st.setLong(2, appId);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not find application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void cancelAccount(long appId) {
		try {
			conn = DBUtil.getInstance();
			String sql = "update bank_accounts set active = 0 where application_id = ?";
			st = conn.prepareStatement(sql);
			st.setLong(1, appId);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not find application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void createJointApplication(String user1, String user2, String type) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into joint_account_applications (joint_user1, joint_user2, account_type) values(?,?,?)";
			st = conn.prepareStatement(sql);
			st.setString(1, user1);
			st.setString(2, user2);
			st.setString(3, type);

			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void createBankAccount(ACCOUNTTYPE type, boolean joint, String user) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into bank_accounts (balance, account_type, joint_account, username) values(0,?,?,?)";
			st = conn.prepareStatement(sql);
			if(type == ACCOUNTTYPE.CHECKING)
				st.setString(1, "Checking");
			else
				st.setString(1, "Saving");
			
			if(joint)
				st.setInt(2, 1);
			else
				st.setInt(2, 0);
			
			st.setString(3, user);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void createJointAccount(String primaryUser, String jointUser) {
		try {
			long accNum = 0;
			conn = DBUtil.getInstance();
			String getAccount = "select * from bank_accounts where username=? and joint_account=1";
			st = conn.prepareStatement(getAccount);
			ResultSet rs = st.executeQuery();
			
			//Get most recent joint account id
			while(rs.next()) {
				long temp = rs.getLong("account_id");
				if(temp > accNum)
					accNum = temp;
			}
			
			if(accNum != 0){
				String sql = "insert into joint_accounts values(?,?)";
				st = conn.prepareStatement(sql);
				st.setString(1, primaryUser);
				st.setLong(2, accNum);
				st.executeUpdate();
				
				st.setString(1, jointUser);
				st.setLong(2, accNum);
				st.executeUpdate();
			}
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeConnections();
		}
	}
	
	public static void createTransaction(TRANSACTION_TYPE type, double amount, long from, long to) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into transactions (transaction_type, amount, account_id_from, account_id_to) values(?,?,?,?)";
			st = conn.prepareStatement(sql);
			if(type == TRANSACTION_TYPE.WITHDRAW)
				st.setString(1, "Withdraw");
			else if(type == TRANSACTION_TYPE.DEPOSIT)
				st.setString(1, "Deposit");
			else
				st.setString(1, "Transfer");
			
			st.setDouble(2, amount);
			st.setLong(3, from);
			st.setLong(4, to);
			
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}
	
	public static void createCustomer(String user, String fName, String lName, String address, String phoneNum) {
		try {
			conn = DBUtil.getInstance();
			String sql = "insert into customers values(?,?,?,?,?)";
			st = conn.prepareStatement(sql);
			
			st.setString(1, user);
			st.setString(2, fName);
			st.setString(3, lName);
			st.setString(4, address);
			st.setString(5, phoneNum);
			st.executeUpdate();
			
			try { conn.commit(); } catch (Exception e) { }
		} catch (SQLException e) {
			try { conn.rollback(); } catch (Exception e1) { }
			System.out.println("Error. Could not create application. Try again later.");
		}finally {
			closeStatementConnection();
		}
	}

	private static void closeStatementConnection() {
		try { st.close(); } catch (Exception e) { }
		try { conn.close(); } catch (Exception e) { }
	}
	
	public static List<BankAccountApplication> getBankApplications(){	
		try {
			List<BankAccountApplication> applications = new ArrayList<>();
			conn = DBUtil.getInstance();
			String sql = "select * from bank_applications where approved is null";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				applications.add(new BankAccountApplication(rs.getLong(1), rs.getString(2), rs.getBoolean(3),
								rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
			}
			return applications;
		} catch (SQLException e) {
			System.out.println("Error. Could not get account info. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	public static List<JointAccountApplication> getJointApplications(){	
		try {
			List<JointAccountApplication> applications = new ArrayList<>();
			conn = DBUtil.getInstance();
			String sql = "select * from joint_account_applications where approved is null";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			while(rs.next()) {
				ACCOUNTTYPE temp;
				if(rs.getString(5) == "Checking")
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				applications.add(new JointAccountApplication(rs.getLong(1), rs.getBoolean(2), rs.getString(3), rs.getString(4), temp));
			}
			return applications;
		} catch (SQLException e) {
			System.out.println("Error. Could not get account info. Try again later");
		} finally {
			closeConnections();
		}
		return null;
	}
	
	public static int countBankAccounts() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_accounts";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}
	
	public static int countCustomers() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from customers";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	public static int countBankApplications() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from bank_applications";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	public static int countJointApplications() {
		try {
			conn = DBUtil.getInstance();
			String sql = "select count(*) from joint_account_applications";
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);

			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			closeConnections();
		}
		return 0;
	}

	private static void closeConnections() {
		try { rs.close(); } catch (Exception e) { }
		closeStatementConnection();
	}
	
	public static void main(String[] args) {
		User user = verifyUser("jbtran", "pass123");
		System.out.println(user.getUserType().toString());
	}
}
