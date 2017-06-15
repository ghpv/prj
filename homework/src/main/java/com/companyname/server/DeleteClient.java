package com.companyname.server;

import com.companyname.model.Client;
import com.companyname.db.DatabaseCommands;

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

public class DeleteClient extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long ID = Long.parseLong(request.getParameter("ID"));
		try
		{
			DatabaseCommands.deleteClientByID(ID);
			response.sendRedirect("/");
		}
		catch (SQLException e)
		{
			response.getWriter().println(e.getMessage());
		}
	}
}
