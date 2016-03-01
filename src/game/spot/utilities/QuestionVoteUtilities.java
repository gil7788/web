package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import game.spot.servlets.convertion.items.QuestionVote;

public class QuestionVoteUtilities {

	public static void createQuestionVotesTable() {
		Utilities.createTable(Config.QUESTIONS_VOTE_TABLE_CREATE);
	}

	public static ResultSet getAllQuestionVote(Statement statement){
		return Utilities.getAllTable(Config.QUESTIONS_VOTE_TABLE_NAME, statement);
	}
	
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

	public static ArrayList<QuestionVote> resultsetToQuestionsVote(ResultSet rs) {
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

	public static void voteQuestion(int id, int value, String username) {
		removeQuestionVote(id, username);
		if (value  == -1 || value == 1) {
			String[] values = new String[] { "" + id, "'" + username + "'", "" + value};
			Utilities.insertIntoTable(Config.QUESTIONS_VOTE_TABLE_NAME, values, Config.QUESTIONS_VOTE_TABLE_ROW);
		}
	}
	
	public static void removeQuestionVote(int id, String voter) {
		Utilities.deleteFromTable(Config.QUESTIONS_VOTE_TABLE_NAME, new String[] { Config.QUESTION_ID, Config.VOTER },
				new String[] { "" + id, "'" + voter + "'"});
	}

	public static List<QuestionVote> getUsersVote(String user){
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		List<QuestionVote> result = new ArrayList<QuestionVote>();
		try{
			statement = connection.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
		ArrayList<QuestionVote> votes = resultsetToQuestionsVote(getAllQuestionVote(statement));
		for (QuestionVote vote : votes) {
			if(vote.voter.equals(user)){
				result.add(vote);
			}
		}
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return result;
	}
	
	public static int getQuestionVoteCount(int id) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		try{
			statement = connection.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
		ArrayList<QuestionVote> votes = resultsetToQuestionsVote(
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

	public static void printQuestionVoteTable(){
		Utilities.printTable(Config.QUESTIONS_VOTE_TABLE_NAME);
	}
}
