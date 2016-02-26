package game.spot;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.QuestionVoteUtilities;
import game.spot.utilities.UserUtilities;

public class Main implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			/* Create DB */
			System.out.println("Initialize data base");
			UserUtilities.createUsersTable();
			QuestionUtilities.createQuestionsTable();
			QuestionVoteUtilities.createQuestionVotesTable();
		} catch (Exception e) {

		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

}
