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

/**
 * The QuestionUtilities is used to read and write question from and to the data
 * base.
 */
public class QuestionUtilities {

	/**
	 * Create the question table
	 */
	public static void createQuestionsTable() {
		Utilities.createTable(Config.QUESTIONS_TABLE_CREATE);
	}

	/**************************/

	/**
	 * Build a new question from a result set
	 * 
	 * @param rs
	 *            the result set that holds the question data
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return new question base on the result set data
	 */
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

	/**
	 * Create a list of question base on a result set with questions data
	 * 
	 * @param rs
	 *            the result set that holds all questions data in rows
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return a list of question built from result set
	 */
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

	/**
	 * Get all questions that are in the data base
	 * 
	 * @return a list of all questions
	 */
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

	/**
	 * Get interval of a questions list
	 * 
	 * @param questions
	 *            the questions list
	 * @param startIndex
	 *            the start index of the interval
	 * @param endIndex
	 *            the end index of the interval
	 * @return interval sub list of the questions list
	 */
	public static List<Question> getQuestionsInterval(List<Question> questions, int startIndex, int endIndex) {
		return Utilities.subList(questions, startIndex, endIndex);
	}

	// -----------------------------------------------

	/**
	 * Add a question to the data base
	 * 
	 * @param username
	 *            the username that published the question
	 * @param text
	 *            the question text
	 * @param topics
	 *            list of all question's topics
	 * @param timestamp
	 *            timestamp this question was published
	 */
	public static void addQuestion(String username, String text, List<String> topics, String timestamp) {
		String[] values = new String[] { "'" + username + "'", "'" + text + "'", "'" + topcisToString(topics) + "'",
				"'" + timestamp + "'" };
		String columnStructure = "(" + Config.AUTHOR + "," + Config.TEXT + "," + Config.TOPICS + "," + Config.TIMESTAMP
				+ ")";
		Utilities.insertIntoTable(Config.QUESTIONS_TABLE_NAME, values, columnStructure);
	}

	/**
	 * Get question by id
	 * 
	 * @param id
	 *            the id of the requested question
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return the requested question
	 */
	public static Question getQuestionFromId(int id, String user,Statement statement) {
		return resultsetToQuestion(user,Utilities.getElementById(Config.QUESTIONS_TABLE_NAME, id), statement);
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

	/**
	 * Get new questions in interval from index
	 * 
	 * @param index
	 *            start index of requested questions
	 * @return sub list of new questions
	 */
	public static List<Question> getNewQuestions(int index,String user){
		List<Question> questions = getAllQuestions(user);
		Utilities.sortByTimestamp(questions);
		List<Question> result = new ArrayList<Question>();
		try {
			for (Question question : questions) {
				if(AnswersUtilities.getQuestionAnswers(question.id,user).size() == 0){
					result.add(question);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		result = getQuestionsInterval(result, index, index + 20);
		for (Question r : result) {
			r.authorsNickname = UserUtilities.usernameToNickname(r.author);
		}
		return result;
	}

	/**
	 * Get rating of a question
	 * 
	 * @param id
	 *            the question id
	 * @return rating of a rating
	 */
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

	/**
	 * Get topics of a question
	 * 
	 * @param id
	 *            the question id
	 * @return a list of question's topics
	 */
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

	/**
	 * Convert topics string to topics list
	 * 
	 * @param topics
	 *            topics in one string
	 * @return a list of topics converted from that one string
	 */
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

	/**
	 * Topics list convert to one topic string
	 * 
	 * @param topics
	 *            list of topics
	 * @return on string representing all topics
	 */
	private static String topcisToString(List<String> topics) {
		String result = "";
		for (String topic : topics) {
			result += topic + "#";
		}
		return result;
	}

	/**
	 * Get popular topics
	 * 
	 * @param index
	 *            index of list
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return list of popular topics
	 */
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

	/**
	 * Get all topics in this data
	 * 
	 * @return a list of topics
	 */
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

	/**
	 * Get all questions of a topic
	 * 
	 * @param topic
	 *            the requested topic
	 * @param index
	 *            the index of the lsit
	 * @return list of all questions in topic
	 */
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

	/**
	 * Get the average rating of a questions list
	 * 
	 * @param questions
	 *            the questions list
	 * @return average rating of the lsit
	 */
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

	/**
	 * Get the author of a question by it's id
	 * 
	 * @param id
	 *            the question id
	 * @return the author name
	 */
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
