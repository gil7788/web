package game.spot.items;

import java.util.List;

/**
 * The User class represent a user in the system that can answer question and
 * vote to other question and answers.
 */
public class User implements Ratable {

	/**
	 * The username of the user
	 */
	public String username;

	/**
	 * Nickname of the user
	 */
	public String nickname;

	/**
	 * Description of the user
	 */
	public String description;

	/**
	 * Url of the user's photo
	 */
	public String photo;

	/**
	 * Rating of the user
	 */
	public double rating;

	/**
	 * The user expertise - 5 best topics
	 */
	public List<String> expertise;

	/**
	 * Last question this user published
	 */
	public List<Question> latestQuestions;

	/**
	 * Last answer this user published
	 */
	public List<Answer> latestAnswers;

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.spot.items.Ratable#getRating()
	 */
	@Override
	public double getRating() {
		return rating;
	}
}
