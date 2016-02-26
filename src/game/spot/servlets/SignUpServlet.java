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

import game.spot.items.Config;
import game.spot.servlets.convertion.items.NewUser;
import game.spot.utilities.UserUtilities;
import game.spot.utilities.Utilities;

/**
 * Servlet implementation class SignUpServlet
 */
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* Start a writing object */
		PrintWriter writer = response.getWriter();
		/* Get data from client */
		String dataFromClient = Utilities.readDataFromUser(request);
		System.out.println("Got: " + dataFromClient);
		/* Parse & Save the data */
		Gson gson = new Gson();
		NewUser user = gson.fromJson(dataFromClient, NewUser.class);
		/* Parsed username */
		String username = user.getUsername();
		/* Parsed password */
		String password = user.getPassword();
		/* Parsed nickname */
		String nickname = user.getNickname();
		/* Parsed description */
		String description = user.getDescription();
		/* Parsed photo */
		String photo = user.getPhoto();

		/* Connect to the DB */
		/* Create Statement */
		Connection connection = Utilities.getConnection();
		Statement statement = Utilities.getStatement(connection);
		/* Checks if username is already exists in the DB */
		if (UserUtilities.existsInUsersBy(username, Config.USERNAME)) {
			writer.println("1");
		}
		/* If username is not taken,add him and send ok (0) */
		else {
			UserUtilities.insertIntoUsers(username, password, nickname, description, photo);
			/* prints the output */
			System.out.println("User's database: ");
			UserUtilities.printUsersTable();
			System.out.println("***********************End of database***********************");
			writer.println("0");
		}
		Utilities.closeStatement(statement);
		Utilities.closeConnection(connection);
	}

}
