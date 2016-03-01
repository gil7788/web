package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import game.spot.servlets.convertion.items.QuestionVote;

/**
 * The QuestionVoteUtilities is used to read and write questions votes from and
 * to the data base. all methods are static
 */
public class QuestionVoteUtilities {

	/**
	 * Create the questions votes table
	 */
	public static void createQuestionVotesTable() {
		Utilities.createTable(Config.QUESTIONS_VOTE_TABLE_CREATE);
	}

	/**
	 * Get all questions votes in this data base
	 * 
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return result set with all rows in this data base
	 */
	public static ResultSet getAllQuestionVote(Statement statement) {
		return Utilities.getAllTable(Config.QUESTIONS_VOTE_TABLE_NAME, statement);
	}

	/**
	 * Convert result set to question vote
	 * 
	 * @param rs
	 *            result set with the vote data
	 * @return QuestionVote base on the result set
	 */
	public static QuestionVote resultsetToQuestionVote(ResultSet rs) {
		Connection connection = null;
		Statement statement = null;
		QuestionVote vote = new QuestionVote();
		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();

			vote.questionId = rs.getInt(Config.QUESTION_ID);
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

	/**
	 * Convert result set to question votes
	 * 
	 * @param rs
	 *            result set with the questions votes data
	 * @return list with all votes
	 */
	public static ArrayList<QuestionVote> resultsetToQuestionsVotes(ResultSet rs) {
		ArrayList<QuestionVote> votes = new ArrayList<QuestionVote>();
		try {
			while (rs.next()) {
				votes.add(resultsetToQuestionVote(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return votes;
	}

	/**
	 * Vote to a question
	 * 
	 * @param id
	 *            id of the question
	 * @param value
	 *            value of the vote (1, 0, -1)
	 * @param username
	 *            the voter username
	 */
	public static void voteQuestion(int id, int value, String username) {
		removeQuestionVote(id, username);
		if (value == -1 || value == 1) {
			String[] values = new String[] { "" + id, "'" + username + "'", "" + value };
			Utilities.insertIntoTable(Config.QUESTIONS_VOTE_TABLE_NAME, values, Config.QUESTIONS_VOTE_TABLE_ROW);
		}
	}

	/**
	 * Remove a vote from the data base
	 * 
	 * @param id
	 *            id of the question id
	 * @param voter
	 *            the voter username
	 */
	public static void removeQuestionVote(int id, String voter) {
		Utilities.deleteFromTable(Config.QUESTIONS_VOTE_TABLE_NAME, new String[] { Config.QUESTION_ID, Config.VOTER },
				new String[] { "" + id, "'" + voter + "'" });
	}

	/**
	 * Get votes of user
	 * 
	 * @param user
	 *            the user's username
	 * @return list of all votes of user
	 */
	public static List<QuestionVote> getUserVotes(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		List<QuestionVote> result = new ArrayList<QuestionVote>();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<QuestionVote> votes = resultsetToQuestionsVotes(getAllQuestionVote(statement));
		for (QuestionVote vote : votes) {
			if (vote.voter.equals(user)) {
				result.add(vote);
			}
		}
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return result;
	}
	
	public static int getUserVote(String user, int questionId) {
		List<QuestionVote> userVotes = getUsersVote(user);
		for(QuestionVote vote : userVotes){
			if(vote.questionId == questionId){
				return vote.value;
			}
		}
		return 0;
	}
	
	public static int getQuestionVoteCount(int id) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrayList<QuestionVote> votes = resultsetToQuestionsVotes(
				Utilities.getQuestionVotesById(Config.QUESTIONS_VOTE_TABLE_NAME, id, statement, rs));
		int count = 0;
		for (QuestionVote vote : votes) {
			count += vote.value;
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return count;
	}

}
