package game.spot.items;
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
	public static final String QUESTIONSCOUNTER = "questionsCounter";
	/* User's type fields */
	private static final String PASSWORD_TYPE = "VARCHAR(8) NOT NULL";
	private static final String USERNAME_TYPE = "VARCHAR(10) PRIMARY KEY";
	private static final String NICKNAME_TYPE = "VARCHAR(20) NOT NULL";
	private static final String DESCRIPTION_TYPE = "VARCHAR(50)";
	private static final String PHOTO_TYPE = "VARCHAR(500)";
	public static final String QUESTIONSCOUNTER_TYPE = "INTEGER NOT NULL";
	/* Question fields */
	public static final String AUTHOR = "author";
	public static final String TEXT = "text";
	public static final String TOPICS = "topics";
	public static final String TIMESTAMP = "timestamp";
	public static final String RATING = "rating";
	public static final String ANSWERSCOUNTER = "answersCounter";
	/* Questions type fields */
	private static final String AUTHOR_TYPE = "VARCHAR(20) NOT NULL";
	private static final String TEXT_TYPE = "VARCHAR(300) NOT NULL";
	private static final String TOPICS_TYPE = "VARCHAR(50)";
	private static final String TIMESTAMP_TYPE = "VARCHAR(50) NOT NULL";
	private static final String RATING_TYPE = "INTEGER NOT NULL";
	private static final String ANSWERSCOUNTER_TYPE = "INTEGER NOT NULL";
	/* Question fields */
	public static final String QUESTION_ID = "questionId";
	public static final String VOTER = "voter";
	public static final String VALUE = "value";
	/* Questions type fields */
	private static final String QUESTION_ID_TYPE = "INTEGER PRIMARY KEY";
	private static final String VOTER_TYPE = "INTEGER";
	private static final String VALUE_TYPE = "VARCHAR(10) PRIMARY KEY";
	/* Names */
	public static final String USERS_TABLE_NAME = "users";
	public static final String QUESTIONS_TABLE_NAME = "questions";
	public static final String QUESTIONS_VOTE_TABLE_NAME = "questionsVote";
	/* Operations on users: */
	public static final String USERS_TABLE_CREATE = "CREATE TABLE " + USERS_TABLE_NAME + " (" + ID + " " + ID_TYPE
			+ ", " + USERNAME + " " + USERNAME_TYPE + ", " + PASSWORD + " " + PASSWORD_TYPE + ", " + NICKNAME + " "
			+ NICKNAME_TYPE + ", " + DESCRIPTION + " " + DESCRIPTION_TYPE + ", " + PHOTO + " " + PHOTO_TYPE + ", "
			+ QUESTIONSCOUNTER + " " + QUESTIONSCOUNTER_TYPE + ")";
	public static final String FIND_IN_USERS = "SELECT * FROM " + USERS_TABLE_NAME + " WHERE ";
	public static final String GET_ALL_USERS = "SELECT * FROM USERS";
	/* Operations on questions: */
	public static final String QUESTIONS_TABLE_CREATE = "CREATE TABLE " + QUESTIONS_TABLE_NAME + " (" + ID + " "
			+ ID_TYPE + ", " + AUTHOR + " " + AUTHOR_TYPE + ", " + TEXT + " " + TEXT_TYPE + ", " + TOPICS + " "
			+ TOPICS_TYPE + ", " + TIMESTAMP + " " + TIMESTAMP_TYPE + ", " + RATING + " " + RATING_TYPE + ", "
			+ ANSWERSCOUNTER + " " + ANSWERSCOUNTER_TYPE + ")";
	public static final String QUESTIONS_VOTE_TABLE_CREATE = "CREATE TABLE " + QUESTIONS_VOTE_TABLE_NAME + " ("
			+ QUESTION_ID + " " + QUESTION_ID_TYPE + ", " + VOTER + " " + VOTER_TYPE + ", " + VALUE + " " + VALUE_TYPE
			+ ")";

	/* Tests */
	public static void main(String[] args) {
		System.out.println("Create users table: " + USERS_TABLE_CREATE);
		System.out.println("Create questions table: " + QUESTIONS_TABLE_CREATE);
		System.out.println(FIND_IN_USERS);
		System.out.println(QUESTIONS_VOTE_TABLE_CREATE);
	}

}
