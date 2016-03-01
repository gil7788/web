package game.spot;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import game.spot.utilities.AnswerVoteUtilities;
import game.spot.utilities.AnswersUtilities;
import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.QuestionVoteUtilities;
import game.spot.utilities.UserUtilities;

public class Main implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			System.out.println("Initialize data base");
			UserUtilities.createUsersTable();
			QuestionUtilities.createQuestionsTable();
			QuestionVoteUtilities.createQuestionVotesTable();
			AnswersUtilities.createAnswerTable();
			AnswerVoteUtilities.createAnswerVotesTable();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

}
