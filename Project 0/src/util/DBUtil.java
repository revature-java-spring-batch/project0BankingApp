package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	static String url, username, password;
	static Connection conn = null;
	static {
		Properties properties= new Properties();
		try {
			properties.load(new FileReader("C:\\Users\\Johnny Tran\\Desktop\\project0BankingApp\\Project 0\\src\\resource\\database.properties"));
			url = properties.getProperty("url");
			username = properties.getProperty("username");
			password = properties.getProperty("password");
			Class.forName(properties.getProperty("driver"));
		} catch(ClassNotFoundException e){
			System.out.println("Cannot find class.");
		}catch (FileNotFoundException e) {
			System.out.println("Cannot find file");
		} catch (IOException e) {
			System.out.println("Error reading file");
		}
	}
	
	public static Connection getInstance() throws SQLException {
		if(conn == null)
		{
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
		return conn;
	}
	
	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection(url, username, password);
		System.out.println(conn);
		
		Statement st = conn.createStatement();
		ResultSet resultSet = st.executeQuery("select * from bank_accounts");
		while(resultSet.next()) {
			int empNum = resultSet.getInt("account_id");
			String name = resultSet.getString("username");
			System.out.println(empNum + " " + name);
		}
		
		conn.close();
	}
}
