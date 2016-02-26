package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import game.spot.items.Answer;

public class AnswersUtilities {

	public static void createAnswerTable() {
		Utilities.createTable(Config.ANSWERS_TABLE_CREATE);
	}

	private static void insertIntoAnswers(String[] values) {
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.QUESTION_ID + ","
				+ Config.TIMESTAMP + "," + Config.ANSWERSCOUNTER + ")";
		Utilities.insertIntoTable(Config.ANSWERS_TABLE_NAME, values, columnStructure);
	}

	private static Answer resultSetToAnswer(ResultSet rs) throws SQLException {
		Answer answer = new Answer();
		answer.id = rs.getInt(Config.ID);
		answer.author = rs.getString(Config.AUTHOR);
		answer.questionId = rs.getInt(Config.QUESTION_ID);
		answer.rating = AnswerVoteUtilities.getAnswerVoteCount(answer.id);
		answer.text = rs.getString(Config.TEXT);
		answer.timestamp = rs.getString(Config.TIMESTAMP);
		return answer;
	}

	public static void addAnswer(String author, String text, int questionId, String timestamp) {
		insertIntoAnswers(
				new String[] { "'" + author + "'", "'" + text + "'", "" + questionId + "'" + timestamp + "'" });
	}

	public static Answer getAnswer(int answerId) throws SQLException {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = null;

		Answer answer = null;
		try {
			rs = statement.executeQuery(
					"SELECT * FROM " + Config.ANSWERS_TABLE_NAME + " WHERE " + Config.ID + " = " + answerId);
			if (!rs.next()) {
				throw new SQLException("Answer was not found");
			}
			answer = resultSetToAnswer(rs);
		} catch (SQLException e) {
			throw e;

		} finally {
			Utilities.closeResultSet(rs);
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}

		return answer;
	}

}
