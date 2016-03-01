package game.spot.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.spot.utilities.UserUtilities;

public class ServletUtilities {

	static String getUserNameFromHttpSession(HttpServletRequest request, HttpServletResponse response) {
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

	static String getPath(HttpServletRequest request) {
		String fullPath = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (fullPath.length() == contextPath.length()) {
			return "";
		} else {
			return fullPath.substring(contextPath.length());
		}
	}

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
