package game.spot.servlets;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogOutServlet
 */
public class LogOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Pattern p1 = Pattern.compile("/logout");
		

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);
		

		if (m1.find()) {
			logout(request, response);
		} else {
			throw new ServletException("Invalid URL");
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		response.sendRedirect("/GameSpot/index.html");
		session.removeAttribute("username");
		session.removeAttribute("password");
	}

}
