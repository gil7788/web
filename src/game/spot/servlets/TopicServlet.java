package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.items.Question;
import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.Utilities;

/**
 * Servlet implementation class TopicServlet
 */
public class TopicServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!ServletUtilities.sessionValid(request, response)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		Pattern p1 = Pattern.compile("/topic/populartopics");
		Pattern p2 = Pattern.compile("/topic/([a-zA-Z0-9]*)/bestquestions");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		Matcher m2 = p2.matcher(path);

		if (m1.find()) {
			getPopularTopics(request, response);
		} else if (m2.find()) {
			String topic = m2.group(1);
			getTopicsQuestion(request, response, topic);
		} else {
			throw new ServletException("Invalid URL");
		}
	}

	private void getPopularTopics(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("In getPopularTopics method");
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
		
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();

		int index = gson.fromJson(dataFromClient, Integer.class);
		List<String> topics = QuestionUtilities.getPopularTopics(index,ServletUtilities.getUserNameFromHttpSession(request, response),statement);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.append(gson.toJson(topics));
	}

	private void getTopicsQuestion(HttpServletRequest request, HttpServletResponse response, String topic)
			throws IOException {
		System.out.println("In get topics question method");
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		int questionIndex = gson.fromJson(dataFromClient, Integer.class);
		System.out.println("dataFromClient: " + dataFromClient);
		List<Question> questions = QuestionUtilities.getTopicsQuestions(topic, questionIndex,ServletUtilities.getUserNameFromHttpSession(request, response));
		for(Question question : questions ){
			System.out.println(question.text);
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.append(gson.toJson(questions));
	}

}
