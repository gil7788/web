package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.items.Answer;
import game.spot.items.Question;
import game.spot.servlets.convertion.items.QuestionConvertion;
import game.spot.servlets.convertion.items.QuestionListConvertion;
import game.spot.servlets.convertion.items.QuestionVote;
import game.spot.servlets.convertion.items.VoteConverstion;
import game.spot.utilities.AnswersUtilities;
import game.spot.utilities.QuestionUtilities;
import game.spot.utilities.QuestionVoteUtilities;
import game.spot.utilities.Utilities;

public class QuestionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (!ServletUtilities.sessionValid(request, response)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		Pattern p1 = Pattern.compile("/question/([0-9]*)/answers");
		Pattern p2 = Pattern.compile("/question/([0-9]*)");
		Pattern p3 = Pattern.compile("/question/newquestions");
		Pattern p4 = Pattern.compile("/question/existingquestions");
		Pattern p5 = Pattern.compile("/question/votes/([[a-b][A-Z][0-9]]*)");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		Matcher m2 = p2.matcher(path);
		Matcher m3 = p3.matcher(path);
		Matcher m4 = p4.matcher(path);
		Matcher m5 = p5.matcher(path);

		if (m3.find()) {
			getNewestQuestions(request, response);
		} else if (m5.find()) {
			String user = m5.group(1);
			getUsersVote(request, response, user);
		} else if (m4.find()) {
			getExistingQuestions(request, response);
		} else if (m1.find()) {
			int answerId = Integer.parseInt(m1.group(1));
			getQuestionsAnswers(request, response, answerId);
		} else if (m2.find()) {
			int questionId = Integer.parseInt(m2.group(1));
			getQuestion(request, response, questionId);
		} else {
			throw new ServletException("Invalid URL");
		}

	}

	private void getQuestionsAnswers(HttpServletRequest request, HttpServletResponse response, int questionId)
			throws IOException, ServletException {

		List<Answer> answers;
		try {
			answers = AnswersUtilities.getQuestionAnswers(questionId);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.append(gson.toJson(answers));

	}

	private void getQuestion(HttpServletRequest request, HttpServletResponse response, int questionId)
			throws IOException {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
		Question question = QuestionUtilities.getQuestionFromId(questionId, statement);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.append(gson.toJson(question));

	}

	private void getNewestQuestions(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		int questionIndex = Integer.parseInt(dataFromClient);

		List<Question> questions = QuestionUtilities.getNewQuestions(questionIndex);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.append(gson.toJson(questions));
	}

	private void getExistingQuestions(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		int questionIndex = Integer.parseInt(dataFromClient);

		List<Question> questions = QuestionUtilities.getExistingQuestions(questionIndex);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.append(gson.toJson(questions));
	}

	private void getUsersVote(HttpServletRequest request, HttpServletResponse response, String user)
			throws IOException {
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		QuestionListConvertion temp = gson.fromJson(dataFromClient, QuestionListConvertion.class);
		List<QuestionVote> result = new ArrayList<QuestionVote>();
		List<QuestionVote> votes = QuestionVoteUtilities.getUsersVote(user);
		for (Question question : temp.questions) {
			for (QuestionVote vote : votes) {
				if (question.id == vote.questionId)
					result.add(vote);
			}
		}
		for(QuestionVote vote : votes){
			System.out.println(vote.voter);
			System.out.println(vote.value);
			System.out.println(vote.questionId);
			System.out.println("*************************");
		}
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.append(gson.toJson(result));
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!ServletUtilities.sessionValid(request, response)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		Pattern p1 = Pattern.compile("/question/add");
		Pattern p2 = Pattern.compile("/question/vote");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		Matcher m2 = p2.matcher(path);
		if (m1.find()) {
			addQuestion(request, response);
		} else if (m2.find()) {
			voteQuestion(request, response);
		} else {
			throw new ServletException("Invalid URL");
		}
	}

	private void addQuestion(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		QuestionConvertion question = gson.fromJson(dataFromClient, QuestionConvertion.class);
		if (question.topics == null) {
			question.topics = new ArrayList<String>();
		}
		String username = ServletUtilities.getUserNameFromHttpSession(request, response);

		QuestionUtilities.addQuestion(username, question.text, question.topics, question.timestamp);

	}

	private void voteQuestion(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		VoteConverstion vote = gson.fromJson(dataFromClient, VoteConverstion.class);
		String username = ServletUtilities.getUserNameFromHttpSession(request, response);
		if (!username.equals(QuestionUtilities.getAuthorById(vote.id))) {
			QuestionVoteUtilities.voteQuestion(vote.id, vote.value, username);
			writer.println(QuestionVoteUtilities.getQuestionVoteCount(vote.id));
		} else {
			writer.println("2");
		}
	}
}
