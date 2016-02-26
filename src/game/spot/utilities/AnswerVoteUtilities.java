package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnswerVoteUtilities {

	public static void createAnswerVotesTable() {
		Utilities.createTable(Config.ANSWERS_VOTE_TABLE_CREATE);
	}

	private static void insertIntoAnswerVote(String[] values) {
		String columnStructure = "(" + Config.ANSWER_ID + "," + Config.VOTER + "," + Config.VALUE + ")";
		Utilities.insertIntoTable(Config.ANSWERS_VOTE_TABLE_NAME, values, columnStructure);
	}

	public static void addAnswerVote(int answerId, String username, int value) throws SQLException {
		// remove previous vote if exist
		removeAnswerVote(answerId, username);
		if (value == 0) {
			// no vote
			return;
		} else if (value != 1 && value != -1) {
			throw new IllegalArgumentException("Value of a vote can only be 1 or -1. value = " + value);
		}

		insertIntoAnswerVote(new String[] { "" + answerId, "'" + username + "'", "" + value });
	}

	public static void removeAnswerVote(int answerId, String username) throws SQLException {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);

		try {
			statement.executeUpdate("DELETE FROM " + Config.ANSWERS_VOTE_TABLE_NAME + " WHERE " + Config.ANSWER_ID
					+ " = " + answerId + " AND " + Config.VOTER + " = " + username);

		} catch (SQLException e) {
			throw e;

		} finally {
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
	}

	public static int getAnswerVoteCount(int answerId) throws SQLException {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = null;

		try {
			rs = statement.executeQuery("SELECT " + Config.VALUE + " FROM " + Config.ANSWERS_VOTE_TABLE_NAME + " WHERE "
					+ Config.ANSWER_ID + " = " + answerId);

			int voteCount = 0;
			while (rs.next()) {
				int voteValue = rs.getInt(Config.VALUE);
				voteCount += voteValue;
			}
			return voteCount;

		} catch (SQLException e) {
			throw e;

		} finally {
			Utilities.closeResultSet(rs);
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
	}

}
