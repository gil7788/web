package game.spot.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

	/**************************/
	public static Question resultsetToQuestion(String user ,ResultSet rs, Statement statement) {
		Question question = new Question();
		try {
			question.id = rs.getInt(Config.ID);
			question.author = rs.getString(Config.AUTHOR);
			question.text = rs.getString(Config.TEXT);
			question.topics = getQuestionTopics(question.id);
			question.timestamp = rs.getString(Config.TIMESTAMP);
			question.voteCount = QuestionVoteUtilities.getQuestionVoteCount(question.id);
			question.rating = getQuestionRating(question.id,user);
			question.currentUserVote = QuestionVoteUtilities.getUserVote(user, question.id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return question;
	}

	public static List<Question> resultsetToQuestionsList(String user,ResultSet rs, Statement statement) {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			while (rs.next()) {
				questions.add(resultsetToQuestion(user,rs, statement));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questions;
	}

	public static List<Question> getAllQuestions(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = Utilities.getAllTable(Config.QUESTIONS_TABLE_NAME, statement);
		List<Question> questions = resultsetToQuestionsList(user,rs, statement);
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return questions;
	}

	public static void filterUnansweredQuestions(List<Question> questions,String user) {
		try {
			for (Question question : questions) {
				if (AnswersUtilities.getQuestionAnswers(question.id,user).size() != 0) {
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

	public static void addQuestion(String username, String text, List<String> topics, String timestamp) {
		String[] values = new String[] {"'" + username + "'","'" + text + "'","'" + topcisToString(topics) + "'","'" + timestamp + "'"};
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.TOPICS + "," + Config.TIMESTAMP
				+ ")";
		Utilities.insertIntoTable(Config.QUESTIONS_TABLE_NAME, values, columnStructure);
	}

	public static Question getQuestionFromId(int id, String user,Statement statement) {
		return resultsetToQuestion(user,Utilities.getElementById(Config.QUESTIONS_TABLE_NAME, id), statement);
	}

	public static List<Question> orderQuestionsByTimestamp(String user) {
		List<Question> questions = getAllQuestions(user);
		Utilities.sortByTimestamp(questions);
		return questions;
	}

	public static List<Question> getExistingQuestions(String user,int index) {
		List<Question> questions = getAllQuestions(user);
		Utilities.sortByRating(questions);
		questions = getQuestionsInterval(questions, index, index + 20);
		for (Question question : questions) {
				question.authorsNickname = UserUtilities.usernameToNickname(question.author);
		}
		return questions;
	}
	
	public static List<Question> getNewQuestions(int index,String user){
		List<Question> questions = getAllQuestions(user);
		Utilities.sortByTimestamp(questions);
		List<Question> result = new ArrayList<Question>();
		try{
		
			for (Question question : questions) {
				if(AnswersUtilities.getQuestionAnswers(question.id,user).size() == 0){
					result.add(question);
				}
			
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = getQuestionsInterval(result, index, index + 20);
		for (Question r : result) {
			r.authorsNickname = UserUtilities.usernameToNickname(r.author);
	}
		return result;
	}
	
	public static double getQuestionRating(int id,String user) {
		int voteCount = QuestionVoteUtilities.getQuestionVoteCount(id);
		double answersRating = 0;

		List<Answer> answers;
		try {
			answers = AnswersUtilities.getQuestionAnswers(id,user);

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

	public static List<String> getQuestionTopics(int id) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<String> result = null;
		try {
			rs = Utilities.findInTableBySingle("" + id, Config.ID, Config.QUESTIONS_TABLE_NAME, statement);
			if (!rs.next())
				return new ArrayList<String>();
			String topics = rs.getString(Config.TOPICS);
			result = stringToTopics(topics);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return result;
	}

	private static List<String> stringToTopics(String topics) {
		int index = topics.indexOf("#");
		List<String> result = new ArrayList<String>();
		while (index != -1) {
			String topic = topics.substring(0, index);
			if (topic.length() > 0)
				result.add(topic);
			topics = topics.substring(index + 1);
			index = topics.indexOf("#");
		}
		if (topics.length() > 0)
			result.add(topics);
		return result;
	}

	private static String topcisToString(List<String> topics) {
		String result = "";
		for (String topic : topics) {
			result += topic + "#";
		}
		return result;
	}

	public static List<String> getPopularTopics(int index, String user,Statement statement) {
		final HashMap<String, Double> topicsRating = new HashMap<String, Double>();
		List<Question> questions = getAllQuestions(user);
		for (Question question : questions) {
			List<String> topics = question.topics;
			for (String topic : topics) {
				if (!topicsRating.containsKey(topic)) {
					topicsRating.put(topic, 0.0);
				}
				topicsRating.put(topic, topicsRating.get(topic) + getQuestionRating(question.id,user));
			}
		}
		List<String> topics = new ArrayList<String>(topicsRating.keySet());
		Collections.sort(topics, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				double r1 = topicsRating.get(o1);
				double r2 = topicsRating.get(o2);

				if (r1 == r2) {
					return 0;
				} else if (r1 > r2) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		return Utilities.subList(topics, index, index + 20);
	}

	public static List<String> getAllTopics(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ResultSet rs = null;
		List<Question> questions = QuestionUtilities.getAllQuestions(user);
		ArrayList<String> topics = new ArrayList<String>();
		for (Question question : questions) {
			topics.addAll(getQuestionTopics(question.id));
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return topics;
	}

	public static List<Question> getTopicsQuestions(String topic, int index, String user) {
		ArrayList<Question> topicsQuestions = new ArrayList<Question>();
		List<Question> questions = getAllQuestions(user);
		for (Question question : questions) {
			List<String> topics = question.topics;
			for (String t : topics) {
				if (topic.equals(t) && !topicsQuestions.contains(question))
					topicsQuestions.add(question);
			}
		}
		Utilities.sortByRating(topicsQuestions);
		return Utilities.subList(topicsQuestions, index, index + 20);
	}

	public static List<Question> getQuestionsByAutor(List<Question> questions, String author, String user,Statement statement) {
		return resultsetToQuestionsList(user,Utilities.findInTableBySingle("'" + author + "'", Config.AUTHOR,
				Config.QUESTIONS_TABLE_NAME, statement), statement);
	}

	public static List<Question> getAllQuestionsByAuthor(String user) {
		List<Question> questions = getAllQuestions(user);
		List<Question> result = new ArrayList<Question>();
		for (Question question : questions) {
			if (question.author.equals(user)) {
				result.add(question);
			}
		}
		return result;
	}

	public static double questionAvarage(List<Question> questions) {
		double value = 0;
		int counter = questions.size();
		for (Question question : questions) {
			value += question.rating;
		}
		if (counter == 0) {
			return 0;
		}
		return value / counter;
	}

	public static String getAuthorById(int id) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		String result = "";
		try {
			statement = connection.createStatement();
			rs = Utilities.findInTableBySingle("" + id, Config.ID, Config.QUESTIONS_TABLE_NAME, statement);
			if (rs.next())
				result = rs.getString(Config.AUTHOR);
			else
				return "";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return result;

	}
}
