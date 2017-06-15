package com.companyname.server;

import com.companyname.model.Client;
import com.companyname.db.DatabaseCommands;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.io.IOException;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.sql.SQLException;

public class AddClient extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		int edited = 0;
		try
		{
			edited = Integer.parseInt(request.getParameter("FORM_EDITED"));
		}
		catch (NumberFormatException e)
		{
			System.err.println("FORM_EDITED WAS NOT CORRECT:" + e.getMessage());
			edited = 0;
		}
		if (edited == 1 && AddClient.checkInput(request))
		{
			String firstName = request.getParameter("FIRST_NAME");
			String lastName = request.getParameter("LAST_NAME");
			if (lastName == null || lastName.equals(""))
			{
				lastName = null;
			}
			String dateString = request.getParameter("DATE_OF_BIRTH");
			LocalDate dateOfBirth = null;
			if (dateString != null && !dateString.equals(""))
			{
				dateOfBirth = Client.parseDateString(dateString);
			}
			String username = request.getParameter("USERNAME");
			String password = request.getParameter("PASSWORD");
			Client c = new Client(firstName, lastName, dateOfBirth, username, password);
			c.setPassword(password); //Hash the password
			try
			{
				DatabaseCommands.addClient(c);
				response.sendRedirect("/");
			}
			catch (SQLException e)
			{
				response.getWriter().println(e.getMessage());
			}
		}
		else
		{
			request.getRequestDispatcher("addClientForm.jsp").forward(request, response);
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private static boolean checkInput(HttpServletRequest request)
	{
		HashMap<String, String> errors = new HashMap<String, String>();
		HashMap<String, String> param = new HashMap<String, String>();

		String firstName = request.getParameter("FIRST_NAME");
		param.put("firstName", firstName);
		if (!Client.correctFirstName(firstName))
		{
			errors.put("firstName", "First name should be 1-600 chars. May not be only special characters.");
		}

		String lastName = request.getParameter("LAST_NAME");
		param.put("lastName", lastName);
		if (lastName == null || lastName.equals(""))
		{
			lastName = null;
		}
		if (!Client.correctLastName(lastName))
		{
			errors.put("lastName", "Last name should be 1-600 chars. May not be only special characters.");
		}

		String dateString = request.getParameter("DATE_OF_BIRTH");
		param.put("dateOfBirth", dateString);
		if (dateString == null || dateString.equals(""))
		{
			dateString = null;
		}
		if (dateString != null)
		{
			try
			{
				if (!Client.correctDateOfBirth(Client.parseDateString(dateString)))
				{
					errors.put("dateOfBirth", "Date of birth should not be after today");
				}
			}
			catch (DateTimeParseException e)
			{
				errors.put("dateOfBirth", "Date of birth should be dd.MM.yyyy");
			}
		}

		String username = request.getParameter("USERNAME");
		param.put("username", username);
		if (!Client.correctUsername(username))
		{
			errors.put("username", "Username should be 1-60 chars. Permitted chars a-z, A-Z, 0-9, !@#$%^&*()[]._- and space( ). May not be only special characters.");
		}
		else if (!DatabaseCommands.uniqueUsername(username))
		{
			errors.put("username", "Such username already exists");
		}

		String password = request.getParameter("PASSWORD");
		if (!Client.correctPassword(password))
		{
			errors.put("password", "Password should be 1-51 chars. Permitted chars a-z, A-Z, 0-9, !@#$%^&*()[]._- and space( ). May not be only special characters.");
		}

		if (!errors.isEmpty())
		{
			request.setAttribute("errors", errors);
			request.setAttribute("params", param);
		}
		return errors.isEmpty();
	}
}
