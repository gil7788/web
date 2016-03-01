package game.spot.items;

import java.util.List;

public class User implements Ratable{
	public String username;
	public String nickname;
	public String description;
	public String  photo;
	public double  rating;
	public List<String> expertise;
	public List<Question> latestQuestions;
	public List<Answer> latestAnswers;
	@Override
	public double getRating() {
		return rating;
	}
}
