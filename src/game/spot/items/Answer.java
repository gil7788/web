package game.spot.items;

public class Answer implements Ratable,Timestampable{

	public int id;

	public String author;

	public String text;

	public double rating;

	public String timestamp;

	public int questionId;

	@Override
	public double getRating() {
		return rating;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}

}
