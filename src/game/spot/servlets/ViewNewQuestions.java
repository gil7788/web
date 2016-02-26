package game.spot.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.Utilities;

/**
 * Servlet implementation class viewNewQuestions
 */
public class ViewNewQuestions extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orderBy = request.getParameter("orderBy");
		int index = Integer.parseInt(request.getParameter("index"));
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);

		QuestionUtilities.printQuestionsTable();
		ResultSet rs = QuestionUtilities.getNewQuestions(statement, orderBy, index);
		System.out.println("Resultset is ordered by timestap: ");
		Utilities.printResultSet(rs);

		Utilities.closeResultSet(rs);
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
	}

}
