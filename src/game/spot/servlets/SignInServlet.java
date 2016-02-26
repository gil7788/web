package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import game.spot.servlets.convertion.items.User;
import game.spot.utilities.Config;
import game.spot.utilities.UserUtilities;
import game.spot.utilities.Utilities;

public class SignInServlet extends HttpServlet {

	private static final long serialVersionUID = 3270157866417039754L;
	/* Get connection to the DB */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/* Read incoming data */
		HttpSession session = request.getSession(true);

		String dataFromClient = Utilities.readDataFromUser(request);

		System.out.println("Got from client: " + dataFromClient);

		Gson gson = new Gson();
		User user = gson.fromJson(dataFromClient, User.class);
		String username = user.username;
		String password = user.password;

		/* Sends data from server side */
		PrintWriter writer = response.getWriter();
		/* Create a statement with the DB */
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			/* Creating a table in DB */
			connection = Utilities.getConnection();
			statement = Utilities.getStatement(connection);
			/* Adding a row to DB */
			/* Get all the data out of the DB table */
			rs = UserUtilities.findInUsersBySingle(username, Config.USERNAME, statement);

			if (rs.next() && (rs.getString(Config.PASSWORD).equals(password))) {
				session.setAttribute("username", username);
				session.setAttribute("nickname", rs.getString(Config.NICKNAME));
				writer.println("0");
			} else {
				writer.println("1");
			}
			UserUtilities.printUsersTable();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Utilities.closeResultSet(rs);
			Utilities.closeStatement(statement);
			Utilities.closeConnection(connection);
		}
	}

}
