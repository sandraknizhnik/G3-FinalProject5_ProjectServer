package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB_Connection {
	
	// Class variables *************************************************
	static Connection conn;
	
	//******************************************************************
	
	/**
	 * function to connect the database according to the user info 
	 * @param password
	 * @param dbUserNameRoot
	 * @param dbName
	 */
	
	public static void connectDB(String password,String dbUserNameRoot,String dbName) {
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost/project?serverTimezone=IST","root",password);
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	/**
	 * function that get info to save in db from the user
	 * the info arriving in ArrayList 
	 * @param data
	 */
	public static ArrayList<String> getUserData(String userName) {
		ArrayList<String> userData = new ArrayList<>();
		Statement stmt;
		String datafromdb = "";
		
		try {
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT userName,firstName,lastName FROM project.users where userName = '" + userName +  "';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3);
			}
			rs = stmt.executeQuery("SELECT status FROM project.customer where userName = '" + userName +  "';");
			while (rs.next()) {
				datafromdb = datafromdb + " " + rs.getString(1);
			}
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");
				for(int i=0;i<arrOfSub.length;i++) {
					userData.add(arrOfSub[i]);
				}
				rs.close();
			}
			else {
				userData.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	return userData;
	}
	
	public static ArrayList<String> addNewCustomer(ArrayList<String> data) throws SQLException{
		Statement stmt;
		String datafromdb = "";
		ArrayList<String> retVal = new ArrayList<>();
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT userName FROM project.users where userName = '" + data.get(0) +  "';");
		while (rs.next()) {
			datafromdb = rs.getString(1);
		}
		if(datafromdb.length() >= 1) {
			retVal.add("Error");
			return retVal;
		}
		rs.close();
		PreparedStatement ps = conn.prepareStatement("insert into users values(?,?,?,?,?,?,?,?,?,null)");
		ps.setString(1, data.get(0));// userName
		ps.setString(2, data.get(1));// passWord
		ps.setString(3, data.get(2));// firstName
		ps.setString(4, data.get(3));// lastName
		ps.setString(5, data.get(4));// role
		ps.setString(6, data.get(5));// email
		ps.setString(7, data.get(6));// phoneNumber
		ps.setString(8, data.get(7));// isLogIn
		ps.setString(9, data.get(8));// ID
		ps.executeUpdate();
		
		
		
		ps = conn.prepareStatement("insert into customer values(?,?,?,?,?,?,?)");
		ps.setString(1, data.get(0));// userName
		ps.setString(2, data.get(9));// status
		ps.setInt(3, 12);
		ps.setString(4, data.get(10));// isAMember
		ps.setString(5, data.get(11));// cardNumber
		ps.setString(6, data.get(12));// expierdDate
		ps.setString(7, data.get(13));// cvv

		ps.executeUpdate();
		ps.close();
		retVal.add("Inserted");
		return retVal;
		
		
	}
	
	
	
	
	public static void signOutUser(String data) {
		
		try {
			PreparedStatement ps = conn
					.prepareStatement("update users set isLoggedIn = ?  where userName = ?;");
			ps.setString(1, "0");
			ps.setString(2, data);
			ps.executeUpdate(); 
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	public static ArrayList<String> checkUserNameAndPassword(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> subscriber = new ArrayList<>();
		String userName = data.get(0);
		String password = data.get(1);
		String datafromdb = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT firstName,lastName,role,isLoggedIn FROM project.users where userName = '" + userName + "'and password = '" + password  + "';");
			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4);
			}
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");

				subscriber.add(arrOfSub[0]);
				subscriber.add(arrOfSub[1]);
				subscriber.add(arrOfSub[2]);
				subscriber.add(arrOfSub[3]);
				PreparedStatement ps = conn
						.prepareStatement("update users set isLoggedIn = ?  where userName = ?;");
				ps.setString(1, "1");
				ps.setString(2, userName);
				ps.executeUpdate(); 
				ps.close();
				rs.close();
			}
			else {
				subscriber.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void saveSubscriber(ArrayList<String> data) {
		try {

			PreparedStatement ps = conn.prepareStatement("insert into subscriber values(?,?,?,?,?,?,?)");
			ps.setString(1, data.get(0));// first name
			ps.setString(2, data.get(1));// last name
			ps.setString(3, data.get(2));// id
			ps.setString(4, data.get(3));// phone number
			ps.setString(5, data.get(4));// email
			ps.setString(6, data.get(5));// credit_card_number
			ps.setString(7, data.get(6));// subscriber_number
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * function that get data to extract from db and return all the requested data of user
	 * @param data
	 */
	
	public static ArrayList<String> getSubscriber(ArrayList<String> data) {
		Statement stmt;
		ArrayList<String> Sub = new ArrayList<>();
		String id = data.get(0);
		String datafromdb = "";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM subscriber WHERE id= '" + id + "';");

			while (rs.next()) {
				datafromdb = rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4)
						+ " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7);
			}
			if (datafromdb.length() > 1) {
				String[] arrOfSub = ((String) datafromdb).split(" ");

				Sub.add(arrOfSub[0]);
				Sub.add(arrOfSub[1]);
				Sub.add(arrOfSub[2]);
				Sub.add(arrOfSub[3]);
				Sub.add(arrOfSub[4]);
				Sub.add(arrOfSub[5]);
				Sub.add(arrOfSub[6]);
				
				rs.close();
			}
			else {
				Sub.add("Error");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Sub;
	}

	
	/**
	 * function that get data to update from db and update all the requested data of user
	 * @param data
	 */
	public static void Update(ArrayList<String> data) {

		String ID;
		String card_number;
		String sub_number;

		try {

			ID = data.get(0);

			if (data.get(1).equals("")) {
				card_number = data.get(1);
				PreparedStatement ps = conn
						.prepareStatement("update subscriber set credit_card_number = ?  where ID = ?");
				ps.setString(1, card_number);
				ps.setString(2, ID);
				ps.executeUpdate();
				ps.close();
			} else if (data.get(2).equals("")) {
				sub_number = data.get(2);
				PreparedStatement ps = conn
						.prepareStatement("update subscriber set subscriber_number = ?  where ID = ?");
				ps.setString(1, sub_number);
				ps.setString(2, ID);
				ps.executeUpdate();
				ps.close();
			} else if (!data.get(1).equals("") && !data.get(2).equals("")) {
				card_number = data.get(1);
				sub_number = data.get(2);
				PreparedStatement ps = conn.prepareStatement(
						"update subscriber set subscriber_number = ? , credit_card_number = ?  where id = ?");
				ps.setString(1, sub_number);
				ps.setString(2, card_number);
				ps.setString(3, ID);
				ps.executeUpdate();
				ps.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
