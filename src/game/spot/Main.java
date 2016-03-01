package game.spot;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import game.spot.utilities.AnswerVoteUtilities;
import game.spot.utilities.AnswersUtilities;
import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.QuestionVoteUtilities;
import game.spot.utilities.UserUtilities;

/**
 * The Main class is used to init all data base utilities when the server is
 * started.
 */
public class Main implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet.
	 * ServletContextEvent)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

}
