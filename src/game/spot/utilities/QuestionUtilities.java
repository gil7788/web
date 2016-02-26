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
			question.id =(rs.getInt(Config.ID));
			question.author = (rs.getString(Config.AUTHOR));
			question.text = (rs.getString(Config.TEXT));
			question.topics = (rs.getString(Config.TOPICS));
			question.timestamp = (rs.getString(Config.TIMESTAMP));
			question.answerCount = (rs.getInt(Config.ANSWERSCOUNTER));
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
				return Integer.parseInt(q2.timestamp) - Integer.parseInt(q1.timestamp); // Descending
			}
		};
		Collections.sort(questions, comp);
	}

	public static void filterUnansweredQuestions(ArrayList<Question> questions) {
		for (Question question : questions) {
			if (question.answerCount != 0) {
				questions.remove(question);
			}
		}
	}

	public static List<Question> getQuestionsInterval(ArrayList<Question> questions, int startIndex, int endIndex) {
		return Utilities.subList(questions, startIndex, endIndex);
	}

	// -----------------------------------------------
	public static void addQuestion(String username,String text,String topics,String timestamp) {
		String[] values = new String[]{username,text,topics,timestamp,"0"};
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.TOPICS + "," + Config.TIMESTAMP
				+ "," + Config.ANSWERSCOUNTER + ")";
		Utilities.insertIntoTable(Config.QUESTIONS_TABLE_NAME, values ,columnStructure);
	}
	
	public static Question getQuestionFromId(int id){
		return resultsetToQuestion(Utilities.getElementById(Config.QUESTIONS_TABLE_NAME, id));
	}
}
