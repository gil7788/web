package game.spot.utilities;
import java.sql.Statement;

import game.spot.items.Config;

public class VoteUtilities {

	public static void createQuestionVotesTable(Statement statement) {
		Utilities.createTable(Config.QUESTIONS_VOTE_TABLE_CREATE, statement);
	}

}
