	package game.spot.items;

import java.util.List;

public class Question implements Ratable, Timestampable {
	
	public Question(){
		
	}

	public int id;

	public String author;
	
	public String authorsNickname;

	public String text;

	public List<String> topics;

	public String timestamp;

	public double rating;

	public int voteCount;
	
	public int currentUserVote;

	@Override
	public double getRating() {
		return rating;
	}

	@Override
	public String getTimestamp() {
		return timestamp;
	}
}