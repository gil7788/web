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
import game.spot.items.User;

/**
 * The UserUtilities class provide a set of method used to read and write users
 * data to data base. All methods are static
 */
public class UserUtilities {

	/**
	 * Create users table
	 */
	public static void createUsersTable() {
		Utilities.createTable(Config.USERS_TABLE_CREATE);
	}

	/**
	 * Create new user
	 * 
	 * @param username
	 *            username of the new user
	 * @param password
	 *            password of new user
	 * @param nickname
	 *            nickname of new user
	 * @param description
	 *            description of new user
	 * @param photo
	 *            photo url of the user
	 */
	public static void createNewUser(String username, String password, String nickname, String description,
			String photo) {
		String[] values = new String[] { username, password, nickname, description, photo };
		String columnStrucutre = "(" + Config.USERNAME + "," + Config.PASSWORD + "," + Config.NICKNAME + ","
				+ Config.DESCRIPTION + "," + Config.PHOTO + ")";
		Utilities.insertIntoTable(Config.USERS_TABLE_NAME, values, columnStrucutre);
	}

	/**
	 * Sign in a user, check it's password
	 * 
	 * @param username
	 *            the user username
	 * @param password
	 *            the checked password
	 * @return true if the password match the username, else false
	 */
	public static boolean signIn(String username, String password) {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = Utilities.getAllTable(Config.USERS_TABLE_NAME, statement);
		try {
			while (rs.next()) {
				if ((rs.getString(Config.USERNAME).equals(username))
						&& (rs.getString(Config.PASSWORD).equals(password)))
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);

		return false;
	}

	public static User getUserById(int id, Statement statement) {
		return resultSetToUser(Utilities.getElementById(Config.USERS_TABLE_NAME, id), statement);
	 * 
	 * @param username
	 *            the user's username
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return user with the requested username
	 * @throws SQLException
	 *             if fails to read from data base
	 */
	public static User getUserByUsername(String username, Statement statement) throws SQLException {
		ResultSet rs = Utilities.findInTableBySingle("'" + username + "'", Config.USERNAME, Config.USERS_TABLE_NAME,
				statement);
		if (!rs.next()) {
			// No such user
			return null;
		}
		return resultSetToUser(rs,username, statement);
	}

	/**
	 * Get the leaderboard - a list of most rated users
	 * 
	 * @return a list of most rated users
	 */
	public static List<User> getLeaderboard(String user) {
		List<User> users = getAllUsers(user);
		Utilities.sortByRating(users);
		return Utilities.subList(users, 0, 20);
	}

	/**
	 * Get a list of all users
	 * 
	 * @return lsit of all users
	 */
	public static List<User> getAllUsers(String user) {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		ResultSet rs = Utilities.getAllTable(Config.USERS_TABLE_NAME, statement);
		List<User> users = new ArrayList<User>();
		try {
			while (rs.next()) {
				users.add(resultSetToUser(rs,user, statement));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return users;
	}

	/**
	 * Build user from result set data
	 * 
	 * @param rs
	 *            the result set that holds the user data
	 * @param statement
	 *            a statement object used to operate data base operations
	 * @return user built by the data in result set
	 */
	public static User resultSetToUser(ResultSet rs, String username, Statement statement) {
		User user = new User();
		try {
			user.username = rs.getString(Config.USERNAME);
			user.nickname = rs.getString(Config.NICKNAME);
			user.description = rs.getString(Config.DESCRIPTION);
			user.photo = rs.getString(Config.PHOTO);
			user.rating = QuestionUtilities.questionAvarage(QuestionUtilities.getAllQuestionsByAuthor(user.username))
					* 0.2 + 0.8 * AnswersUtilities.answerAvarage(AnswersUtilities.getAllAnswersByAuthor(user.username));
			user.expertise = getExpertise(user.username);
			Utilities.subList(user.latestQuestions = QuestionUtilities.getAllQuestionsByAuthor(user.username), 0, 5);
			Utilities.subList(user.latestAnswers = AnswersUtilities.getAllAnswersByAuthor(user.username), 0, 5);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Get a user expertise
	 * 
	 * @param username
	 *            the user's usernmae
	 * @return the user's expertise
	 */
	public static List<String> getExpertise(String username) {
		List<String> expertise = getUsersTopicsRank(username);
		return Utilities.subList(expertise, 0, 5);
	}

	/**
	 * Get users topics rank
	 * 
	 * @param username
	 *            the user's username
	 * @return list of user ranks
	 */
	public static List<String> getUsersTopicsRank(String username) {
		List<Answer> usersAnswers = AnswersUtilities.getAllAnswersByAuthor(username);
		List<String> answerTopics;
		final HashMap<String, Double> topicsRating = new HashMap<String, Double>();
		for (Answer answer : usersAnswers) {
			answerTopics = QuestionUtilities.getQuestionTopics(answer.questionId);

			for (String topic : answerTopics) {
				if (topicsRating.containsKey(topic)) {
					topicsRating.put(topic, topicsRating.get(topic) + answer.rating);
				} else
					topicsRating.put(topic, answer.rating);
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

		return topics;
	}

	/**
	 * Get nickname of a user by it's username
	 * 
	 * @param username
	 *            the user's username
	 * @return the user's nickname
	 */
	public static String usernameToNickname(String username) {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		ResultSet rs = null;
		String nickname = "";
		try {
			statement = connection.createStatement();
			rs = Utilities.findInTableBySingle("'" + username + "'", Config.USERNAME, Config.USERS_TABLE_NAME,
					statement);
			if (!rs.next())
				return "";
			nickname = rs.getString(Config.NICKNAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		return nickname;
	}

}
