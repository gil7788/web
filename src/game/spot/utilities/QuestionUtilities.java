package game.spot.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.spot.items.Config;
import game.spot.items.Question;

public class QuestionUtilities {
	public static void createQuestionsTable(Statement statement) {
		Utilities.createTable(Config.QUESTIONS_TABLE_CREATE, statement);
	}

	public static void insertIntoQuestions(String row, Statement statement) {
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.TOPICS + "," + Config.TIMESTAMP
				+ "," + Config.RATING + "," + Config.ANSWERSCOUNTER + ")";
		Utilities.insertIntoTable(Config.QUESTIONS_TABLE_NAME, columnStructure, row, statement);
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
		String[] values = { "0", String.format("%d", index), String.format("%d", index + 20) };
		return orderByAndFilterQuestions(statement, orderBy, columns, ops, values);

	}

	/**************************/
	public static Question resultsetToQuestion(ResultSet rs) {
		Question question = new Question();
		try {
			question.setId(rs.getInt(Config.ID));
			question.setAuthor(rs.getString(Config.AUTHOR));
			question.setText(rs.getString(Config.TEXT));
			question.setTopics(rs.getString(Config.TOPICS));
			question.setTimestamp(rs.getString(Config.TIMESTAMP));
			question.setRating(rs.getString(Config.RATING));
			question.setAnswerCount(rs.getInt(Config.ANSWERSCOUNTER));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}

	public static ArrayList<Question> resultsetToArrayListQuestion(ResultSet rs) {
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

	public static ArrayList<Question> getAllQuestions() {
		return resultsetToArrayListQuestion(Utilities.getAllTable(Config.QUESTIONS_TABLE_NAME));
	}

	public static void orderQuestionsByTimestamp(ArrayList<Question> questions) {
		Comparator<Question> comp = new Comparator<Question>() {
			@Override
			public int compare(Question q1, Question q2) {
				return Integer.parseInt(q2.getTimestamp()) - Integer.parseInt(q1.getTimestamp()); // Descending
			}
		};
		Collections.sort(questions, comp);
	}

	public static void filterUnansweredQuestions(ArrayList<Question> questions) {
		for (Question question : questions) {
			if (question.getAnswerCount() != 0) {
				questions.remove(question);
			}
		}
	}

	public static List<Question> getQuestionsInterval(ArrayList<Question> questions, int startIndex, int endIndex) {
		return Utilities.subList(questions, startIndex, endIndex);
	}
}
