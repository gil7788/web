package game.spot.utilities;
import java.sql.ResultSet;
import java.sql.Statement;

import game.spot.items.Config;

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
}
