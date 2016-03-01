package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.items.Answer;
import game.spot.servlets.convertion.items.AnswerConvertion;
import game.spot.servlets.convertion.items.VoteConverstion;
import game.spot.utilities.AnswerVoteUtilities;
import game.spot.utilities.AnswersUtilities;

/**
 * Servlet implementation class AnswerServlet
 */
public class AnswerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if(!ServletUtilities.sessionValid(request, response)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		Pattern p1 = Pattern.compile("/answer/([0-9]*)");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);

		int questiondId = Integer.parseInt(m1.group(1));
		List<Answer> answers = new ArrayList<Answer>();
		try {
			answers = AnswersUtilities.getQuestionAnswers(questiondId);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.append(gson.toJson(answers));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if(!ServletUtilities.sessionValid(request, response)){
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		Pattern p1 = Pattern.compile("/answer/add");
		Pattern p2 = Pattern.compile("/answer/vote");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		Matcher m2 = p2.matcher(path);

		if (m1.find()) {
			addAnswer(request, response);
		} else if (m2.find()) {
			voteAnswer(request, response);
		} else {
			throw new ServletException("Invalid URL");
		}
	}

	private void addAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		AnswerConvertion answer = gson.fromJson(dataFromClient, AnswerConvertion.class);
		String username = ServletUtilities.getUserNameFromHttpSession(request, response);

		AnswersUtilities.addQuestion(username, answer.text, answer.questionId, answer.timestamp);
	}

	private void voteAnswer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("In voteAnswer!");
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		VoteConverstion vote = gson.fromJson(dataFromClient, VoteConverstion.class);
		String username = ServletUtilities.getUserNameFromHttpSession(request, response);
		try {
			AnswerVoteUtilities.addAnswerVote(vote.id, username, vote.value);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
}

}
