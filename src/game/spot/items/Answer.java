package game.spot.items;

/**
 * The Answer class represents a answer object that was submitted by a user to a
 * specific question. This class implements the interfaces Ratable and
 * Timestampable so that other components can sort lists of answer by rating or
 * timestamp.
 */
public class Answer implements Ratable, Timestampable {

	/**
	 * Id of the answer
	 */
	public int id;

	/**
	 * The author username
	 */
	public String author;

	/**
	 * The text of the answer
	 */
	public String text;

	/**
	 * rating of the answer
	 */
	public double rating;

	/**
	 * timestamp of this answer, represent the time this answer was published as
	 * a string
	 */
	public String timestamp;

	/**
	 * the question id of the question this answer was answered to
	 */
	public int questionId;
	
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
