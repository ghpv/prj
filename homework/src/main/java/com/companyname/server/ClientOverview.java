package com.companyname.server;

import com.companyname.db.DatabaseCommands;
import com.companyname.model.Client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.sql.SQLException;

public class ClientOverview extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("text/html");
		try
		{
			request.setAttribute("clients", DatabaseCommands.getClients());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/clientOverview.jsp");
			dispatcher.forward(request, response);
		}
		catch (SQLException e)
		{
			response.getWriter().println(e.getMessage());
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
