package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserUtilities {

	public static void createUsersTable() {
		Utilities.createTable(Config.USERS_TABLE_CREATE);
	}

	public static void printUsersTable() {
		System.out.println("Users Table:");
		Utilities.printTable(Config.USERS_TABLE_NAME);
		/* GET_ALL_USERS */
	}

	public static ResultSet findInUsersBySingle(String value, String column, Statement statement) {
		return Utilities.findInTableBySingle(value, column, statement, Config.USERS_TABLE_NAME);
	}

	public static void insertIntoUsers(String username,String password,String nickname,String description,String photo) {
		String[] values = new String[]{username,password,nickname,description,photo};
		String columnStrucutre = "(" + Config.USERNAME + "," + Config.PASSWORD + "," + Config.NICKNAME + ","
				+ Config.DESCRIPTION + "," + Config.PHOTO + "," + Config.QUESTIONSCOUNTER + ")";
		Utilities.insertIntoTable(Config.USERS_TABLE_NAME, values,columnStrucutre);
	}

	public static boolean existsInUsersBy(String value, String column) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();
			System.out.println(Config.FIND_IN_USERS + column + " = " + "'" + value + "'");
			ResultSet rs = statement.executeQuery(Config.FIND_IN_USERS + column + " = " + "'" + value + "'");
			/* If user already exists */
			if (rs.next())
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
		return false;
	}

	public void main() {
		printUsersTable();
	}
}
