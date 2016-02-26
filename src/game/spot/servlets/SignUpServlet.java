package game.spot.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import game.spot.servlets.convertion.items.NewUser;
import game.spot.utilities.Config;
import game.spot.utilities.UserUtilities;
import game.spot.utilities.Utilities;

/**
 * Servlet implementation class SignUpServlet
 */
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		String dataFromClient = Utilities.readDataFromUser(request);

		// Parse data
		Gson gson = new Gson();
		NewUser user = gson.fromJson(dataFromClient, NewUser.class);
		String username = user.username;
		String password = user.password;
		String nickname = user.nickname;
		String description = user.description;
		String photo = user.photo;

		/* Checks if username is already exists in the DB */
		if (UserUtilities.existsInUsersBy(username, Config.USERNAME)) {
			writer.println("1");
		}
		/* If username is not taken,add him and send ok (0) */
		else {
			UserUtilities.createNewUser(username, password, nickname, description, photo);
			/* prints the output */
			System.out.println("User's database: ");
			UserUtilities.printUsersTable();
			System.out.println("***********************End of database***********************");
			writer.println("0");
		}
	}

}
