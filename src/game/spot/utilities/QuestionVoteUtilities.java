package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import game.spot.items.Config;
import game.spot.servlets.convertion.items.QuestionVote;

public class QuestionVoteUtilities {

	public static void createQuestionVotesTable(Statement statement) {
		Utilities.createTable(Config.QUESTIONS_VOTE_TABLE_CREATE, statement);
	}

	public static QuestionVote resultsetToQuestionVote(ResultSet rs) {
		Connection connection = null;
		Statement statement = null;
		QuestionVote vote = new QuestionVote();
		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();

			vote.setQuestionId(rs.getInt(Config.QUESTION_ID));
			vote.setVoter(rs.getString(Config.VOTER));
			vote.setValue(rs.getInt(Config.VALUE));

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
		removeVote(id, username);
		if (value != 0) {
			String[] values = new String[] { "" + id, "" + value, username };
			Utilities.insertIntoTable(Config.QUESTIONS_VOTE_TABLE_NAME, values, Config.QUESTIONS_VOTE_TABLE_ROW);
		}
	}

	public static void removeVote(int id, String voter) {
		Utilities.deleteFromTable(Config.QUESTIONS_VOTE_TABLE_NAME, new String[] { Config.ID, Config.VOTER },
				new String[] { "" + id, voter });
	}

	public static int getQuestionVoteCount(int id) {
		ArrayList<QuestionVote> votes = resultsetToQuestionsVote(Utilities.getElementById(Config.QUESTIONS_VOTE_TABLE_NAME, id));
		int count = 0;
		for(QuestionVote vote : votes){
			count += vote.getValue();
		}
		return count;
	}
}
