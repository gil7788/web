package game.spot.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Utilities {
	public static boolean sessionValid(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		if (session.isNew()) {
			session.invalidate();
			return false;
		} else
			return true;
	}

	public static String getUserNameFromHttpSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			if (session.isNew()) {
				session.invalidate();
				response.getWriter().println("log out");
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) session.getAttribute("username");
	}

	public static String getNickNameFromHttpSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			if (session.isNew()) {
				session.invalidate();
				response.getWriter().println("log out");
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) session.getAttribute("nickname");
	}

	public static void createTable(String tableData, Statement statement) {
		try {
			statement.executeUpdate(tableData);
		} catch (SQLException e) {
			System.out.println("Using existing table.");
		}
	}

	public static String readDataFromUser(HttpServletRequest request) {
		try {
			BufferedReader reader = request.getReader();
			String dataFromClient = "";
			String line = "";
			while ((line = reader.readLine()) != null) {
				dataFromClient += line;
			}
			return dataFromClient;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			connection = DriverManager.getConnection(
					"jdbc:derby:C:\\Users\\student\\Desktop\\Univesity\\SemesterA2016\\Web\\ServerSide\\Database\\projectDatabase;create=true");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection(Connection connection) {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeStatement(Statement statement) {
		try {

			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet findInTableBySingle(String value, String column, Statement statement, String tableName) {
		try {
			/* If user already exists */
			return statement
					.executeQuery("SELECT * FROM " + tableName + " WHERE " + column + " = " + "'" + value + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Statement getStatement(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void insertIntoTable(String tableName, String columnStructure, String row, Statement statement) {
		try {
			/* Add user */
			statement.executeUpdate("INSERT INTO " + tableName + columnStructure + " VALUES (" + row + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void printTable(String tableName) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();

			ResultSet rs = getAllTable(tableName, statement);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			rs = statement.executeQuery("SELECT * FROM " + tableName);
			System.out.println("************************");
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i < columnsNumber)
						System.out.print(rs.getString(i) + ",");
					else
						System.out.println(rs.getString(i));
				}
				// System.out.println(rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5));
			}
			System.out.println("************************");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
	}

	public static void printResultSet(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ResultSet orderBy(String tableName, Statement statement, String orderParameter) {
		ResultSet rs = null;
		try {
			rs = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY " + orderParameter + " DECS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getAllTable(String tableName, Statement statement) {
		ResultSet rs = null;
		try {
			rs = statement.executeQuery("SELECT * FROM " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet filter(String tableName, Statement statement, String column, String comparisonOp,
			String value) {
		ResultSet rs = null;
		try {
			statement.executeUpdate(
					"SELECT * FROM " + tableName + " WHERE " + column + " " + comparisonOp + " " + value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet orderByAndFilter(String tableName, Statement statement, String orderParameter,
			String[] column, String[] comparisonOp, String[] value) {
		ResultSet rs = null;
		try {
			String SQLRequest = "SELECT * FROM " + tableName + " WHERE ";
			for (int i = 0; i < column.length; i++) {
				if (i == column.length - 1)
					SQLRequest = SQLRequest + column[i] + " " + comparisonOp[i] + " " + value[i];
				else
					SQLRequest = SQLRequest + column[i] + " " + comparisonOp[i] + " " + value[i] + " AND ";
			}
			SQLRequest = SQLRequest + " ORDER BY " + orderParameter + " DESC";
			System.out.println(SQLRequest);
			rs = statement.executeQuery(SQLRequest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

}
