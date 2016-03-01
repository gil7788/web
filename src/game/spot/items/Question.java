package game.spot.items;

import java.util.List;

/**
 * The Question class represent a question that was submitted by a user. This
 * class can be sorted by rating and timestamp
 */
public class Question implements Ratable, Timestampable {
	
	public Question(){
		
	}

	/**
	 * Id of the question
	 */
	public int id;

	/**
	 * The author username
	 */
	public String author;

	/**
	 * The author nickname
	 */
	public String authorsNickname;

	/**
	 * This question text
	 */
	public String text;

	/**
	 * List of all topics related to this question
	 */
	public List<String> topics;

	/**
	 * timestamp this question was published
	 */
	public String timestamp;

	/**
	 * rating of this question
	 */
	public double rating;

	/**
	 * vote count of thisquestion
	 */
	public int voteCount;
	
	public int currentUserVote;

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.spot.items.Ratable#getRating()
	 */
	@Override
	public double getRating() {
		return rating;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.spot.items.Timestampable#getTimestamp()
	 */
	@Override
	public String getTimestamp() {
		return timestamp;
	}
}