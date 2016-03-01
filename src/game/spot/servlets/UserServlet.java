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

import game.spot.items.User;
import game.spot.servlets.convertion.items.SignInConvertion;
import game.spot.servlets.convertion.items.SignUpConvertion;
import game.spot.utilities.UserUtilities;
import game.spot.utilities.Utilities;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!ServletUtilities.sessionValid(request, response)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}

		Pattern p1 = Pattern.compile("/user/([a-zA-Z0-9]*)/data");
		Pattern p2 = Pattern.compile("/user/leaderboard");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		Matcher m2 = p2.matcher(path);

		if (m2.find()) {
			getLeaderboard(request, response);

		} else if (m1.find()) {
			System.out.println((m1.group(1)));
			String username = (m1.group(1));
			getUser(request, response, username);
		} else {
			throw new ServletException("Invalid URL");
		}
	}

	private void getUser(HttpServletRequest request, HttpServletResponse response, String username)
			throws IOException, ServletException {
		Connection connection = Utilities.getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		User user;
		try {
			user = UserUtilities.getUserByUsername(username, statement);
		} catch (SQLException e) {
			throw new ServletException(e);
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.append(gson.toJson(user));
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
	}

	private void getLeaderboard(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<User> users = UserUtilities.getLeaderboard(ServletUtilities.getUserNameFromHttpSession(request, response));

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.append(gson.toJson(users));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		SignInConvertion user = gson.fromJson(dataFromClient, SignInConvertion.class);

		boolean success = UserUtilities.signIn(user.username, user.password);
		if (success) {
			request.getSession().setAttribute("username", user.username);
			request.getSession().setAttribute("password", user.password);
		}

		PrintWriter out = response.getWriter();
		out.write(success ? "success" : "failure");
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String dataFromClient = ServletUtilities.readDataFromUser(request);
		Gson gson = new Gson();
		SignUpConvertion user = gson.fromJson(dataFromClient, SignUpConvertion.class);
		String username = user.username;
		String password = user.password;
		String nickname = user.nickname;
		String description = user.description == null ? "" : user.description;
		String photo = user.photo == null ? "" : user.photo;

		if (username == null || password == null || nickname == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		UserUtilities.createNewUser("'" + username + "'", "'" + password + "'", "'" + nickname + "'",
				"'" + description + "'", "'" + photo + "'");
	}

}
