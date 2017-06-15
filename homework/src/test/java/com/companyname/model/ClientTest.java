package com.companyname.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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

import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DatabaseCommands.class)
public class ClientTest extends Mockito
{
	private long id = 1;
	private String firstName = "aaaa";
	private String lastName = "aaaa";
	private LocalDate dateOfBirth = LocalDate.now();
	private String username = "aaaa";
	private String password = "aaaa";	
	private String password_hash = "implied_password_hash";
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private String dateString = formatter.format(dateOfBirth);

	@Before
	public void initMocks()
	{
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(DatabaseCommands.class);
		id = 1;
		firstName = "aaaa";
		lastName = "aaaa";
		dateOfBirth = LocalDate.now();
		username = "aaaa";
		password = "aaaa";
		password_hash = "implied_password_hash";
		dateString = formatter.format(dateOfBirth);
	}


	@Test
	public void fullClientConstructorFieldChecks()
	{
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);
		Client c = new Client(id, firstName, lastName, dateOfBirth, username, password_hash);
		assertEquals("ID's are not equal", id, c.getID());
		assertEquals("firstName's are not equal", firstName, c.getFirstName());
		assertEquals("lastName's are not equal", lastName, c.getLastName());
		assertEquals("dateOfBirth's are not equal", dateOfBirth, c.getDateOfBirth());
		assertEquals("usernames are not equal", username, c.getUsername());
		assertEquals("password hashes are not equal", password_hash, c.getPassword());
	}

	@Test
	public void checkPasswordHashing()
	{
		when(DatabaseCommands.uniqueUsername("aaaa")).thenReturn(true);
		Client c = new Client(id, firstName, lastName, dateOfBirth, username, "");
		c.setPassword(password);
		assertTrue("Password does not correspond to own hash", c.checkPassword(password));
		assertFalse("Wrong password passes hash check", c.checkPassword("bbbb"));
	}

	@Test
	public void parseDateStringCheck()
	{
		assertEquals("Dates are not the same", dateOfBirth, Client.parseDateString(dateString));
	}

	@Test
	public void correctUsernameChecks()
	{
		HashMap<String, Boolean> tests = new HashMap<String, Boolean>();
		tests.put("aaaa", true);
		tests.put(null, false);
		tests.put("", false);
		tests.put("ииии", false);
		tests.put("0123456789", true);
		tests.put("----", false);
		tests.put("012345678901234567890123456789012345678901234567890123456789", true); //60 chars
		tests.put("0123456789012345678901234567890123456789012345678901234567890", false); //61 chars
		for (Map.Entry<String, Boolean> test : tests.entrySet())
		{
			String un = test.getKey();
			boolean correct = test.getValue();
			if (correct)
			{
				assertTrue("valid username '" + un + "' did not pass", Client.correctUsername(un));
			}
			else
			{
				assertFalse("invalid username '" + un + "' passed", Client.correctUsername(un));
			}
		}
	}

	@Test
	public void correctPasswordChecks()
	{
		HashMap<String, Boolean> tests = new HashMap<String, Boolean>();
		tests.put("aaaa", true);
		tests.put(null, false);
		tests.put("", false);
		tests.put("ииии", false);
		tests.put("0123456789", true);
		tests.put("----", false);
		tests.put("012345678901234567890123456789012345678901234567890", true); //51
		tests.put("0123456789012345678901234567890123456789012345678901", false); //52
		for (Map.Entry<String, Boolean> test : tests.entrySet())
		{
			String pw = test.getKey();
			boolean correct = test.getValue();
			if (correct)
			{
				assertTrue("valid password '" + pw + "' did not pass", Client.correctPassword(pw));
			}
			else
			{
				assertFalse("invalid password '" + pw + "' passed", Client.correctPassword(pw));
			}
		}
	}
}
