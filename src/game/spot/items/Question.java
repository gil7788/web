package game.spot.items;

public class Question {
	private String author;
	private String text;
	private String topics;
	private String timestamp;
	private String rating;

	public String getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	public String getTopics() {
		return topics;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getRating() {
		return rating;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

}
