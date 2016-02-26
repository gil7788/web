package game.spot.items;

public class Question implements Ratable, Timestampable {

	public int id;

	public String author;

	public String text;

	public String topics;

	public String timestamp;

	public double rating;

	public int voteCount;

	@Override
	public double getRating() {
		return rating;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}
}