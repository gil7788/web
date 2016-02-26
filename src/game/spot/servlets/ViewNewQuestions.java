package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.items.Question;
import game.spot.utilities.QuestionUtilities;

/**
 * Servlet implementation class viewNewQuestions
 */
public class ViewNewQuestions extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int index = Integer.parseInt(request.getParameter("index"));

		QuestionUtilities.printQuestionsTable();

		List<Question> questions = QuestionUtilities.orderQuestionsByTimestamp();
		QuestionUtilities.filterUnansweredQuestions(questions);
		List<Question> newQuestions = QuestionUtilities.getQuestionsInterval(questions, index, index + 20);
		String resultQuestions = new Gson().toJson(newQuestions);
		PrintWriter writer = response.getWriter();
		writer.println(resultQuestions);

		System.out.println("ArrayList is ordered by timestap: ");
	}

}
