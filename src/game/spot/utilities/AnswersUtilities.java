package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import game.spot.items.Answer;

/**
 * The AnswersUtilities used to read and create answer from and to data base.
 * All methods are static
 */
public class AnswersUtilities {

	/**
	 * Create the answers table
	 */
	public static void createAnswerTable() {
		Utilities.createTable(Config.ANSWERS_TABLE_CREATE);
	}

	/**
	 * Insert new row into answer
	 * 
	 * @param values
	 *            the values of the new row
	 */
	private static void insertIntoAnswers(String[] values) {
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.QUESTION_ID + ","
				+ Config.TIMESTAMP + ")";
		Utilities.insertIntoTable(Config.ANSWERS_TABLE_NAME, values, columnStructure);
	}

	/**
	 * Create an answer from a result set
	 * 
	 * @param rs
	 *            the result set with the answer data
	 * @return new Answer object base on the data from the result set
	 * @throws SQLException
	 *             if fails to read data from result set
	 */
	private static Answer resultSetToAnswer(ResultSet rs , String user) throws SQLException {
		Answer answer = new Answer();
		answer.id = rs.getInt(Config.ID);
		answer.author = rs.getString(Config.AUTHOR);
		answer.questionId = rs.getInt(Config.QUESTION_ID);
		answer.rating = AnswerVoteUtilities.getAnswerVoteCount(answer.id);
		answer.text = rs.getString(Config.TEXT);
		answer.timestamp = rs.getString(Config.TIMESTAMP);
		answer.currentUserVote = AnswerVoteUtilities.getUserVote(user, answer.id);
		return answer;
	}

	/**
	 * Create a list of answers from a result set with rows
	 * 
	 * @param rs
	 *            the result set with the answers data
	 * @return list of answer base on the result set rows
	 */
	public static List<Answer> resultsetToAnswers(ResultSet rs,String user) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		try {
			while (rs.next()) {
				answers.add(resultSetToAnswer(rs,user));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answers;
	}

	/**
	 * Add new answer and write to data abse
	 * 
	 * @param author
	 *            the author username of the answer
	 * @param text
	 *            the answer text
	 * @param questionId
	 *            the question id of this answer
	 * @param timestamp
	 *            timestamp this answer was submitted
	 */
	public static void addAnswer(String author, String text, int questionId, String timestamp) {
		insertIntoAnswers(
				new String[] { "'" + author + "'", "'" + text + "'", "" + questionId, "'" + timestamp + "'" });
	}

	/**
	 * Get an answer by it's id
	 * 
	 * @param answerId
	 *            the id of the requested answer
	 * @return the requested answer
	 * @throws SQLException
	 *             if fails to read from data base
	 */
	public static Answer getAnswer(int answerId,String user) throws SQLException {
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
			answer = resultSetToAnswer(rs, user);
		} catch (SQLException e) {
			throw e;

		} finally {
			Utilities.closeResultSet(rs);
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}

		return answer;
	}

	/**
	 * Get all answers of a question
	 * 
	 * @param questionId
	 *            the question id
	 * @return a list with all answer of the question
	 * @throws SQLException
	 *             if fails to read from data base
	 */
	public static List<Answer> getQuestionAnswers(int questionId,String user) throws SQLException {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = null;

		List<Answer> answers = new ArrayList<Answer>();
		try {
			rs = statement.executeQuery(
					"SELECT * FROM " + Config.ANSWERS_TABLE_NAME + " WHERE " + Config.QUESTION_ID + " = " + questionId);
			while (rs.next()) {
				Answer answer = resultSetToAnswer(rs,user);
				answers.add(answer);
			}
		} catch (SQLException e) {
			throw e;

		} finally {
			Utilities.closeResultSet(rs);
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}

		return answers;
	}

	/**
	 * Get all answers stored in data base
	 * 
	 * @return a list of all answers
	 */
	public static List<Answer> getAllAnswers(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = Utilities.getAllTable(Config.ANSWERS_TABLE_NAME, statement);
		List<Answer> answers = resultsetToAnswers(rs,user);
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return answers;
	}

	/**
	 * Get all answers in this data by a specific user
	 * 
	 * @param author
	 *            the author username
	 * @return a list of all author's answers
	 */
	public static List<Answer> getAllAnswersByAuthor(String user) {
		List<Answer> answers = getAllAnswers(user);
		List<Answer> result = new ArrayList<Answer>();
		for (Answer answer : answers) {
			if (answer.author.equals(user)) {
				result.add(answer);
			}
		}
		return result;
	}

	/**
	 * Get the average rating of a answers list
	 * 
	 * @param answers
	 *            a list of answers
	 * @return the average rating
	 */
	public static double answerAvarage(List<Answer> answers) {
		double value = 0;
		int counter = answers.size();
		for (Answer answer : answers) {
			value += answer.rating;
		}
		if (counter == 0) {
			return 0;
		}
		return value / counter;
	}

}
