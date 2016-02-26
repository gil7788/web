package game.spot.utilities;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.servlets.convertion.items.User;

public class VoteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = Utilities.getConnection();
			statement = connection.createStatement();

			String dataFromClient = Utilities.readDataFromUser(request);

			System.out.println(dataFromClient);

			Gson gson = new Gson();
			User vote = gson.fromJson(dataFromClient, User.class);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
	}

}
