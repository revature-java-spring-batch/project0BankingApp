package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Account;
import dto.Account.ACCOUNTTYPE;
import dto.PersonalInfo;
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
				String userType = rs.getString("user_type");
				
				if(userType.equals("Customer"))
					type = USER.CUSTOMER;
				else if(userType.equals("Employee"))
					type = USER.EMPLOYEE;
				else {
					type = USER.ADMIN;
				}
				User tempUser = new User(rs.getString("username"), rs.getString("password"), type);
				return tempUser;
			}
			else
				return null;
		} catch (SQLException e) {
			System.out.println("Error. Could not verify login. Try again later.");
		} finally {
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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

			if (rs.next() && rs.getInt(1) == 0)
				return true;

			else
				return false;
		} catch (SQLException e) {
			System.out.println("Error. Try again later.");
		} finally {
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
		}
		return false;
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
				String type = rs.getString("account_type");

				if(type.equals("Checking"))
					temp = ACCOUNTTYPE.CHECKING;
				else
					temp = ACCOUNTTYPE.SAVINGS;
				accList.add(new Account(rs.getLong("account_id"), rs.getDouble("balance"), temp, rs.getBoolean("joint_account"), rs.getBoolean("active")));
			}
			return accList;
			
		} catch (SQLException e) {
			System.out.println("Error. Could not find account. Try again later." + e.getMessage());
		} finally {
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
		}
		
		return null;
	}
	
	//to do modify to not return result set
	public static ResultSet getAccountInfo(int low, int high) {
		try {
			conn = DBUtil.getInstance();
			String sql = "select * from bank_accounts where rownum between ? and ?";
			st = conn.prepareStatement(sql);
			st.setInt(1, low);
			st.setInt(2, high);
			rs = st.executeQuery();

			return rs;
		} catch (SQLException e) {
			System.out.println("Error. Could not get account info. Try again later");
		} finally {
			try { rs.close(); } catch (Exception e) { }
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
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
			try { st.close(); } catch (Exception e) { }
			try { conn.close(); } catch (Exception e) { }
		}
	}
	
	public static void main(String[] args) {
		User user = verifyUser("jbtran", "pass123");
		System.out.println(user.getUserType().toString());
	}
}
