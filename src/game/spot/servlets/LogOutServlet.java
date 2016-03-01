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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Pattern p1 = Pattern.compile("/logout");

		String path = ServletUtilities.getPath(request);
		Matcher m1 = p1.matcher(path);

		if (m1.find()) {
			logout(request, response);
		} else {
			throw new ServletException("Invalid URL");
		}
	}

	/**
	 * Reform a 'logout'
	 * 
	 * @param request
	 *            the client request
	 * @param response
	 *            a response object used to write back to client
	 * @throws IOException
	 *             if fails to write back to client
	 */
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		response.sendRedirect("/GameSpot/index.html");
		session.removeAttribute("username");
		session.removeAttribute("password");
	}

}
