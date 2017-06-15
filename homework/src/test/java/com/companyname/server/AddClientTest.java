package com.companyname.server;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.mockito.PowerMockito;

import com.companyname.db.DatabaseCommands;

import javax.servlet.ServletException;

import java.io.IOException;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseCommands.class)
/**
 * All tests throw Exception because they should not be throwing exceptions
 * So if they do throw any it's a failed test
 */
public class AddClientTest extends Mockito
{
	@Mock
	HttpServletRequest request;

	@Mock
	HttpServletResponse response;

	@Mock
	RequestDispatcher rd;

	@Before
	public void initMocks()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(DatabaseCommands.class);
		when(request.getParameter("FORM_EDITED")).thenReturn("1");
	}

	@Test
	public void emptyParametersShouldRedirectBack() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, never()).sendRedirect(anyString());
		verify(rd, atLeast(1)).forward(request, response);
	}

	@Test
	public void minimumCorrectParametersShouldPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("aaaa");
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, atLeast(1)).sendRedirect(anyString());
		verify(rd, never()).forward(request, response);
	}

	@Test
	public void utfNightmaresShouldPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("一郎");
		when(request.getParameter("LAST_NAME")).thenReturn("田中");
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, atLeast(1)).sendRedirect(anyString());
		verify(rd, never()).forward(request, response);
	}

	@Test
	public void utfNightmaresInUsernameShouldNotPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("一郎");
		when(request.getParameter("LAST_NAME")).thenReturn("田中");
		when(request.getParameter("USERNAME")).thenReturn("コーヒー");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, never()).sendRedirect(anyString());
		verify(rd, atLeast(1)).forward(request, response);
	}

	@Test
	public void nonUniqueUsernameShouldNotPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("aaaa");
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(false);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, never()).sendRedirect(anyString());
		verify(rd, atLeast(1)).forward(request, response);
	}

	@Test
	public void incorrectDateFormatShouldNotPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("aaaa");
		when(request.getParameter("DATE_OF_BIRTH")).thenReturn("aaaa");
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, never()).sendRedirect(anyString());
		verify(rd, atLeast(1)).forward(request, response);
	}

	@Test
	public void dateAfterTodayShouldNotPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("aaaa");
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String dateString = formatter.format(tomorrow);
		when(request.getParameter("DATE_OF_BIRTH")).thenReturn(dateString);
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, never()).sendRedirect(anyString());
		verify(rd, atLeast(1)).forward(request, response);
	}

	@Test
	public void todayShouldPass() throws Exception
	{
		when(request.getRequestDispatcher(anyString())).thenReturn(rd);
		when(request.getParameter("FIRST_NAME")).thenReturn("aaaa");
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		String dateString = formatter.format(today);
		when(request.getParameter("DATE_OF_BIRTH")).thenReturn(dateString);
		when(request.getParameter("USERNAME")).thenReturn("aaaa");
		when(request.getParameter("PASSWORD")).thenReturn("aaaa");
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);

		AddClient addClient = new AddClient();
		addClient.doPost(request, response);

		verify(response, atLeast(1)).sendRedirect(anyString());
		verify(rd, never()).forward(request, response);
	}

}
