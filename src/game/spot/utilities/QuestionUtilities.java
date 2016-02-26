package game.spot.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import game.spot.items.Answer;
import game.spot.items.Question;

public class QuestionUtilities {

	public static void createQuestionsTable() {
		Utilities.createTable(Config.QUESTIONS_TABLE_CREATE);
	}

	public static void printQuestionsTable() {
		System.out.println("Questions Table:");
		Utilities.printTable(Config.QUESTIONS_TABLE_NAME);
	}

	/**************************/
	public static ResultSet orderQuestionsBy(Statement statement, String orderBy) {
		return Utilities.orderBy(Config.QUESTIONS_TABLE_NAME, statement, orderBy);
	}

	public static ResultSet orderByAndFilterQuestions(Statement statement, String orderParameter, String[] column,
			String[] comparisonOp, String[] value) {
		return Utilities.orderByAndFilter(Config.QUESTIONS_TABLE_NAME, statement, orderParameter, column, comparisonOp,
				value);
	}

	public static ResultSet filterQuestions(Statement statement, String column, String comparisonOp, String value) {
		return Utilities.filter(Config.QUESTIONS_TABLE_NAME, statement, column, comparisonOp, value);
	}

	public static ResultSet getNewQuestions(Statement statement, String orderBy, int index) {
		String[] columns = { Config.ANSWERSCOUNTER, Config.ID, Config.ID };
		String[] ops = { "=", ">=", "<" };
		String[] values = { "0", "" + index, "" + (index + 20) };
		return orderByAndFilterQuestions(statement, orderBy, columns, ops, values);

	}

	/**************************/
	public static Question resultsetToQuestion(ResultSet rs) {
		Question question = new Question();
		try {
			question.id = rs.getInt(Config.ID);
			question.author = rs.getString(Config.AUTHOR);
			question.text = rs.getString(Config.TEXT);
			question.topics = rs.getString(Config.TOPICS);
			question.timestamp = rs.getString(Config.TIMESTAMP);
			question.voteCount = QuestionVoteUtilities.getQuestionVoteCount(question.id);
			question.rating = getQuestionRating(question.id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}

	public static List<Question> resultsetToArrayListQuestion(ResultSet rs) {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			while (rs.next()) {
				questions.add(resultsetToQuestion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}

	public static List<Question> getAllQuestions() {
		return resultsetToArrayListQuestion(Utilities.getAllTable(Config.QUESTIONS_TABLE_NAME));
	}

	public static void filterUnansweredQuestions(List<Question> questions) {
		try {
			for (Question question : questions) {
				if (AnswersUtilities.getQuestionAnswers(question.id).size() != 0) {
					questions.remove(question);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Question> getQuestionsInterval(List<Question> questions, int startIndex, int endIndex) {
		return Utilities.subList(questions, startIndex, endIndex);
	}

	// -----------------------------------------------

	public static void addQuestion(String username, String text, String topics, String timestamp) {
		String[] values = new String[] { username, text, topics, timestamp, "0" };
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.TOPICS + "," + Config.TIMESTAMP
				+ "," + Config.ANSWERSCOUNTER + ")";
		Utilities.insertIntoTable(Config.QUESTIONS_TABLE_NAME, values, columnStructure);
	}

	public static Question getQuestionFromId(int id) {
		return resultsetToQuestion(Utilities.getElementById(Config.QUESTIONS_TABLE_NAME, id));
	}

	public static List<Question> orderQuestionsByTimestamp() {
		List<Question> questions = getAllQuestions();
		Utilities.sortByTimestamp(questions);
		return questions;
	}

	public static List<Question> getExistingQuestions(int index) {
		List<Question> questions = getAllQuestions();
		Utilities.sortByRating(questions);
		questions = getQuestionsInterval(questions, index, index + 20);
		return questions;
	}

	public static double getQuestionRating(int id) {
		int voteCount = QuestionVoteUtilities.getQuestionVoteCount(id);
		double answersRating = 0;

		List<Answer> answers;
		try {
			answers = AnswersUtilities.getQuestionAnswers(id);

			for (Answer answer : answers) {
				answersRating += answer.rating;
			}
			if (answers.size() != 0) {
				answersRating /= answers.size();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return voteCount * 0.2 + answersRating * 0.8;

	}

}
