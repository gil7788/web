package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.items.Question;
import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.Utilities;

public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		PrintWriter writer = response.getWriter();
		String dataFromClient = Utilities.readDataFromUser(request);
		System.out.println("Got from client: " + dataFromClient);

		String username = Utilities.getUserNameFromHttpSession(request, response);
		Gson gson = new Gson();
		Question newQuestion = gson.fromJson(dataFromClient, Question.class);
		String text = newQuestion.text;
		String topics = newQuestion.topics;
		String timestamp = newQuestion.timestamp;

		QuestionUtilities.createQuestionsTable(statement);
		QuestionUtilities.printQuestionsTable();
		QuestionUtilities.addQuestion(username, text, topics, timestamp);
		QuestionUtilities.printQuestionsTable();
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
		if (Utilities.sessionValid(request, response)) {
			writer.println("0");
		}

	}

}
