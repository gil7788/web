package game.spot.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.spot.utilities.UserUtilities;

/**
 * The ServletUtilities class provide a set of helpers method used by all
 * servlets. All method are static
 */
public class ServletUtilities {

	/**
	 * Get the username of the user that send the request
	 * 
	 * @param request
	 *            the request from the client
	 * @param response
	 *            response object
	 * @return the username of the active user in this session
	 */
	public static String getUserNameFromHttpSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		try {
			if (session.isNew()) {
				session.invalidate();
				response.getWriter().println("log out");
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) session.getAttribute("username");
	}

	/**
	 * Get the relevant path for servlets
	 * 
	 * @param request
	 *            the request from client
	 * @return relevant path
	 */
	static String getPath(HttpServletRequest request) {
		String fullPath = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (fullPath.length() == contextPath.length()) {
			return "";
		} else {
			return fullPath.substring(contextPath.length());
		}
	}

	/**
	 * Read data from client request
	 * 
	 * @param request
	 *            the request from client
	 * @return data read from request
	 * @throws IOException
	 *             if fails to read data
	 */
	static String readDataFromUser(HttpServletRequest request) throws IOException {
		if (request.getMethod().equals("GET")) {
			return request.getParameter("data");
		}

		BufferedReader reader = request.getReader();
		String dataFromClient = "";
		String line;
		while ((line = reader.readLine()) != null) {
			dataFromClient += line;
		}
		return dataFromClient;
	}

	/**
	 * Checks if this sesstion is valid
	 * 
	 * @param request
	 *            the request from client
	 * @param response
	 *            a response object
	 * @return true if this session is valid, else false
	 */
	static boolean sessionValid(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			String password = (String) session.getAttribute("password");
			if (username == null || password == null) {
				return false;
			}
			return UserUtilities.signIn(username, password);
		} catch (Exception e) {
			return false;
		}
	}

}
