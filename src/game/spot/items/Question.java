package game.spot.items;

public class Question {
	private int id;
	private String author;
	private String text;
	private String topics;
	private String timestamp;
	private String rating;
	private int answerCount;
	
	public int getId(){
		return id;
	}
	
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

	public int getAnswerCount(){
		return answerCount;
	}

	public void setId(int id){
		 this.id = id;
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

	public void setAnswerCount(int answerCount){
		this.answerCount = answerCount;
	}
}
