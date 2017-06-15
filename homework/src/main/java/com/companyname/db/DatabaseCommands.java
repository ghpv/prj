package com.companyname.db;

import java.util.ArrayList;
import java.util.Date;

import java.time.Instant;
import java.time.LocalDate;

import com.companyname.model.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseCommands
{
	public static String DB_DRIVER = "org.h2.Driver";
	public static String DB_CONNECTION = "jdbc:h2:file:./data/";
	public static String DB_NAME = "clients";
	public static String DB_USER = "user";
	public static String DB_PASSWORD = "user_password";

	/**
	 * Creates a connection with database
	 */
	private static Connection getConnection() throws SQLException
	{
		Connection dbConnection = null;
		try
		{
			Class.forName(DB_DRIVER);
			dbConnection = DriverManager.getConnection(DB_CONNECTION + DB_NAME, DB_USER, DB_PASSWORD);
			return dbConnection;
		}
		catch (ClassNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}

	/**
	 * @return current list of users and all their data
	 */
	public static ArrayList<Client> getClients() throws SQLException
	{
		ArrayList<Client> ret = new ArrayList<Client>();
		String comm = "SELECT * FROM client_overview";
		ResultSet rs = null;
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(comm);
		rs = ps.executeQuery();
		while (rs.next())
		{
			long id = rs.getLong("client_id");
			String fname = rs.getString("client_first_name");
			String sname = rs.getString("client_last_name");
			String us = rs.getString("client_username");
			String pw = rs.getString("client_password");
			LocalDate dateOfBirth = rs.getObject("client_date_of_birth", LocalDate.class);

			Client c = new Client(id, fname, us, pw);
			c.setLastName(sname);
			c.setDateOfBirth(dateOfBirth);
			ret.add(c);
		}
		conn.close();
		return ret;
	}

	/**
	 * @param username String with username
	 * @return true if such a username does NOT exist
	 */
	public static boolean uniqueUsername(String username)
	{
		boolean ret = false;
		try
		{
			Connection conn = getConnection();
			String comm = "SELECT COUNT(client_username) AS cnt FROM CLIENTS WHERE client_username = ?";
			PreparedStatement ps = conn.prepareStatement(comm);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.first();
			ret = (rs.getLong("cnt") == 0);
			conn.close();
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage());
		}
		return ret;
	}

	/**
	 * Adds a specified client to the database
	 */
	public static void addClient(Client c) throws SQLException
	{
		Connection conn = getConnection();
		String comm = "INSERT INTO CLIENTS (client_first_name, client_last_name, client_date_of_birth, client_username, client_password) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement ps = conn.prepareStatement(comm);
		ps.setString(1, c.getFirstName());
		ps.setString(2, c.getLastName());
		ps.setDate(3, null);
		if (c.getDateOfBirth() != null)
		{
			ps.setDate(3, java.sql.Date.valueOf(c.getDateOfBirth()));
		}
		ps.setString(4, c.getUsername());
		ps.setString(5, c.getPassword());
		ps.executeUpdate();
		conn.close();
	}

	/**
	 * Deletes specified client by ID if exists
	 */
	public static void deleteClientByID(long ID) throws SQLException
	{
		Connection conn = getConnection();
		String comm = "DELETE FROM CLIENTS WHERE client_id = ?";
		PreparedStatement ps = conn.prepareStatement(comm);
		ps.setLong(1, ID);
		ps.executeUpdate();
		conn.close();
	}

	/**
	 * Updates all client fields except for ID
	 */
	public static void editClientByID(Client c) throws SQLException
	{
		Connection conn = getConnection();
		String comm = "UPDATE CLIENTS SET client_first_name = ?, client_last_name = ?, client_date_of_birth = ?, client_username = ?, client_password = ? WHERE client_id = ?";
		PreparedStatement ps = conn.prepareStatement(comm);
		ps.setString(1, c.getFirstName());
		ps.setString(2, c.getLastName());
		ps.setDate(3, null);
		if (c.getDateOfBirth() != null)
		{
			ps.setDate(3, java.sql.Date.valueOf(c.getDateOfBirth()));
		}
		ps.setString(4, c.getUsername());
		ps.setString(5, c.getPassword());
		ps.setLong(6, c.getID());
		ps.executeUpdate();
		conn.close();
	}

	/**
	 * Gets the client by ID from database, null if no one
	 */
	public static Client getClientByID(long ID) throws SQLException
	{
		Connection conn = getConnection();
		String comm = "SELECT * FROM CLIENTS WHERE client_id = ?";
		PreparedStatement ps = conn.prepareStatement(comm);
		ps.setLong(1, ID);
		ResultSet rs = ps.executeQuery();
		if (!rs.first())
		{
			conn.close();
			return null;
		}
		long id = rs.getLong("client_id");
		String fname = rs.getString("client_first_name");
		String sname = rs.getString("client_last_name");
		String us = rs.getString("client_username");
		String pw = rs.getString("client_password");
		LocalDate dateOfBirth = rs.getObject("client_date_of_birth", LocalDate.class);
		Client c = new Client(id, fname, sname, dateOfBirth, us, pw);
		conn.close();
		return c;
	}
}
