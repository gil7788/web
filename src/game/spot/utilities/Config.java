package game.spot.utilities;

/**
 * The Config class contains only static constants that used by all data base
 * utilities.
 */
public class Config {

	/* Global fields */
	public static final String ID = "id";

	/* Global type fields */
	private static final String ID_TYPE = "INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)";

	/* User fields */
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String NICKNAME = "nickname";
	public static final String DESCRIPTION = "description";
	public static final String PHOTO = "photo";

	/* User's type fields */
	private static final String PASSWORD_TYPE = "VARCHAR(8) NOT NULL";
	private static final String USERNAME_TYPE = "VARCHAR(10) PRIMARY KEY";
	private static final String NICKNAME_TYPE = "VARCHAR(20) NOT NULL";
	private static final String DESCRIPTION_TYPE = "VARCHAR(50)";
	private static final String PHOTO_TYPE = "VARCHAR(500)";

	/* Question fields */
	public static final String AUTHOR = "author";
	public static final String TEXT = "text";
	public static final String TOPICS = "topics";
	public static final String TIMESTAMP = "timestamp";

	/* Questions type fields */
	private static final String AUTHOR_TYPE = "VARCHAR(20) NOT NULL";
	private static final String TEXT_TYPE = "VARCHAR(300) NOT NULL";
	private static final String TOPICS_TYPE = "VARCHAR(50)";
	private static final String TIMESTAMP_TYPE = "VARCHAR(50) NOT NULL";

	/* Question fields */
	public static final String QUESTION_ID = "questionId";
	public static final String ANSWER_ID = "answerId";
	public static final String VOTER = "voter";
	public static final String VALUE = "value";

	/* Questions type fields */
	private static final String QUESTION_ID_TYPE = "INTEGER NOT NULL";
	private static final String ANSWER_ID_TYPE = "INTEGER NOT NULL";
	private static final String VOTER_TYPE = "VARCHAR(10)";
	private static final String VALUE_TYPE = "INTEGER";

	/* Names */
	public static final String USERS_TABLE_NAME = "users";
	public static final String QUESTIONS_TABLE_NAME = "questions";
	public static final String QUESTIONS_VOTE_TABLE_NAME = "questionsVote";
	public static final String ANSWERS_TABLE_NAME = "answers";
	public static final String ANSWERS_VOTE_TABLE_NAME = "answersVote";

	/* Operations on users: */
	public static final String USERS_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE_NAME + " (" + ID + " " + ID_TYPE
			+ ", " + USERNAME + " " + USERNAME_TYPE + ", " + PASSWORD + " " + PASSWORD_TYPE + ", " + NICKNAME + " "
			+ NICKNAME_TYPE + ", " + DESCRIPTION + " " + DESCRIPTION_TYPE + ", " + PHOTO + " " + PHOTO_TYPE + ")";
	public static final String FIND_IN_USERS = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE ";
	public static final String GET_ALL_USERS = "SELECT * FROM USERS";

	/* Operations on questions: */
	public static final String QUESTIONS_TABLE_CREATE = "CREATE TABLE " + QUESTIONS_TABLE_NAME + " (" + ID + " "
			+ ID_TYPE + ", " + AUTHOR + " " + AUTHOR_TYPE + ", " + TEXT + " " + TEXT_TYPE + ", " + TOPICS + " "
			+ TOPICS_TYPE + ", " + TIMESTAMP + " " + TIMESTAMP_TYPE + ")";
	public static final String QUESTIONS_VOTE_TABLE_CREATE = "CREATE TABLE " + QUESTIONS_VOTE_TABLE_NAME + " ("
			+ QUESTION_ID + " " + QUESTION_ID_TYPE + ", " + VOTER + " " + VOTER_TYPE + ", " + VALUE + " " + VALUE_TYPE
			+ ")";
	public static final String QUESTIONS_VOTE_TABLE_ROW = "( " + QUESTION_ID + " , " + VOTER + " , " + VALUE + " )";
	public static final String ANSWERS_TABLE_CREATE = "CREATE TABLE " + ANSWERS_TABLE_NAME + " (" + ID + " " + ID_TYPE
			+ ", " + AUTHOR + " " + AUTHOR_TYPE + ", " + TEXT + " " + TEXT_TYPE + ", " + QUESTION_ID + " "
			+ QUESTION_ID_TYPE + ", " + TIMESTAMP + " " + TIMESTAMP_TYPE + ")";
	public static final String ANSWERS_VOTE_TABLE_CREATE = "CREATE TABLE " + ANSWERS_VOTE_TABLE_NAME + " (" + ANSWER_ID
			+ " " + ANSWER_ID_TYPE + ", " + VOTER + " " + VOTER_TYPE + ", " + VALUE + " " + VALUE_TYPE + ")";

}
