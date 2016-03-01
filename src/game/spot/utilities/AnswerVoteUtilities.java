package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import game.spot.servlets.convertion.items.AnswerVote;

public class AnswerVoteUtilities {

	public static void createAnswerVotesTable() {
		Utilities.createTable(Config.ANSWERS_VOTE_TABLE_CREATE);
	}

	private static void insertIntoAnswerVote(String[] values) {
		String columnStructure = "(" + Config.ANSWER_ID + "," + Config.VOTER + "," + Config.VALUE + ")";
		Utilities.insertIntoTable(Config.ANSWERS_VOTE_TABLE_NAME, values, columnStructure);
	}

	private static ResultSet selectAll(Statement statement) {
		return Utilities.getAllTable(Config.ANSWERS_TABLE_NAME, statement);
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

	public static ArrayList<AnswerVote> resultsetToAnswersVote(ResultSet rs) {
		ArrayList<AnswerVote> votes = new ArrayList<AnswerVote>();
		try {
			while (rs.next()) {
				votes.add(resultsetToAnswerVote(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return votes;
	}

	public static AnswerVote resultsetToAnswerVote(ResultSet rs) {
		Connection connection = null;
		Statement statement = null;
		AnswerVote vote = new AnswerVote();
		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();

			vote.answerId = rs.getInt(Config.QUESTION_ID);
			vote.voter = rs.getString(Config.VOTER);
			vote.value = rs.getInt(Config.VALUE);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeConnection(connection);
			Utilities.closeStatement(statement);
		}
		return vote;
	}

	public static List<AnswerVote> getUsersVote(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		List<AnswerVote> result = new ArrayList<AnswerVote>();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<AnswerVote> votes = resultsetToAnswersVote(selectAll(statement));
		for (AnswerVote vote : votes) {
			if (user.equals(vote.voter)) {
				result.add(vote);
			}
		}
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return result;
	}

	public static int getUserVote(String user, int answerId) {
		List<AnswerVote> userVotes = getUsersVote(user);
		for (AnswerVote vote : userVotes) {
			if (vote.answerId == answerId) {
				return vote.value;
			}
		}
		return 0;
	}

}
