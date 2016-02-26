package game.spot;

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.QuestionVoteUtilities;
import game.spot.utilities.UserUtilities;
import game.spot.utilities.Utilities;

public class Main implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			Connection connection = Utilities.getConnection();
			Statement statement = Utilities.getStatement(connection);
			/* Create DB */
			System.out.println("in init method");
			UserUtilities.createUsersTable(statement);
			QuestionUtilities.createQuestionsTable(statement);
			QuestionVoteUtilities.createQuestionVotesTable(statement);
			Utilities.closeConnection(connection);
			Utilities.closeStatement(statement);
		} catch (Exception e) {

		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

}
