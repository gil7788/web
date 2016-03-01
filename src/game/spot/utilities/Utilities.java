package game.spot.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.spot.items.Ratable;
import game.spot.items.Timestampable;

public class Utilities {

	static void createTable(String tableData) {
		Connection connection = getConnection();
		Statement statement = getStatement(connection);
		try {
			statement.executeUpdate(tableData);
		} catch (SQLException e) {
			System.out.println("Using existing table.");
		}
		closeStatement(statement);
		closeStatement(statement);
	}

	public static Connection getConnection() {
		Connection connection = null;

		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			connection = DriverManager.getConnection("jdbc:derby:projectDatabase;create=true");
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

	static void closeResultSet(ResultSet rs) {
		try {

			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static ResultSet findInTableBySingle(String value, String column, String tableName , Statement statement) {
		try {
			
			/* If user already exists */
			return statement
					.executeQuery("SELECT * FROM " + tableName + " WHERE " + column + " = " +  value );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static Statement getStatement(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			return statement;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	static void insertIntoTable(String tableName, String[] values, String columnStructure) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			/* Add user */
			String insertString = "INSERT INTO " + tableName + " " + columnStructure + " VALUES (";

			for (int i = 0; i < values.length; i++) {
				
				if (i != values.length - 1){
						insertString = insertString + values[i] + " , ";
				}		
				else{
						insertString = insertString + values[i] + " ) ";
				}
					

			}
			System.out.println(insertString);
			statement.executeUpdate(insertString);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closeStatement(statement);
		}
	}

	static void deleteFromTable(String tableName, String[] columns, String[] values) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			/* Add user */
			String deleteString = "DELETE FROM " + tableName + " WHERE ";
			for (int i = 0; i < values.length; i++) {
				if (i != values.length - 1)
					deleteString = deleteString + columns[i] + " = " + values[i] + " AND ";
				else
					deleteString = deleteString + columns[i] + " = " + values[i];
			}
			statement.executeUpdate(deleteString);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closeStatement(statement);
		}
	}

	static void printTable(String tableName) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
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

	static ResultSet orderBy(String tableName, Statement statement, String orderParameter) {
		ResultSet rs = null;
		try {
			rs = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY " + orderParameter + " DECS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	static ResultSet getAllTable(String tableName, Statement statement) {
		try {
			return statement.executeQuery("SELECT * FROM " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	static ResultSet filter(String tableName, Statement statement, String column, String comparisonOp, String value) {
		ResultSet rs = null;
		try {
			statement.executeUpdate(
					"SELECT * FROM " + tableName + " WHERE " + column + " " + comparisonOp + " " + value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	static ResultSet orderByAndFilter(String tableName, Statement statement, String orderParameter, String[] column,
			String[] comparisonOp, String[] value) {
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

	static <T> List<T> subList(List<T> list, int start, int end) {
		if (list.size() <= start) {
			return new ArrayList<T>();
		}
		return list.subList(start, Math.min(end, list.size()));
	}

	static ResultSet getElementById(String tableName, int id) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM " + tableName + " WHERE ID = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(connection);
			closeStatement(statement);
		}
		return rs;
	}
	
	static ResultSet getQuestionVotesById(String tableName, int id ,Statement statement , ResultSet rs ) {
		
		try {
			
			rs = statement.executeQuery("SELECT * FROM " + tableName + " WHERE " + Config.QUESTION_ID + " = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return rs;
	}

	static void sortByRating(List<? extends Ratable> list) {
		Collections.sort(list, new Comparator<Ratable>() {

			@Override
			public int compare(Ratable o1, Ratable o2) {
				double r1 = o1.getRating();
				double r2 = o2.getRating();
				if (o1 == o2) {
					return 0;
				} else if (r1 < r2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
	}

	static void sortByTimestamp(List<? extends Timestampable> list) {
		Collections.sort(list, new Comparator<Timestampable>() {

			@Override
			public int compare(Timestampable o1, Timestampable o2) {
				String t1 = o1.getTimestamp();
				String t2 = o2.getTimestamp();
				return (int)(Long.parseLong(t2) - Long.parseLong(t1));
			}
		});
	}

}
