package com.companyname.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Locale;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Class representing the client.
 */

public class Client
{
	public Client(long ID, String first_name, String last_name, LocalDate dateOfBirth, String username, String password_hash)
	{
		setID(ID);
		setFirstName(first_name);
		setLastName(last_name);
		setDateOfBirth(dateOfBirth);
		setUsername(username);
		setPasswordHash(password_hash);
	}

	public Client(String first_name, String last_name, LocalDate dateOfBirth, String username, String password_hash)
	{
		setFirstName(first_name);
		setLastName(last_name);
		setDateOfBirth(dateOfBirth);
		setUsername(username);
		setPasswordHash(password_hash);
	}

	public Client(long id, String first_name, String username, String password_hash)
	{
		setID(id);
		setFirstName(first_name);
		setUsername(username);
		setPasswordHash(password_hash);
	}

	public void setID(long input)
	{
		id = input;
	}

	public void setFirstName(String input)
	{
		if (Client.correctFirstName(input))
		{
			first_name = input;
		}
	}

	public void setLastName(String input)
	{
		if (Client.correctLastName(input))
		{
			last_name = input;
		}
	}

	public void setDateOfBirth(LocalDate date)
	{
		if (Client.correctDateOfBirth(date))
		{
			date_of_birth = date;
		}
	}

	public void setUsername(String input)
	{
		if (Client.correctUsername(input))
		{
			username = input;
		}
	}

	/**
	 * Password setter
	 * @param input password
	 * Checks if the password is correct @see correctPassword(String)
	 * If it is then it hashes the password
	 */
	public void setPassword(String input)
	{
		if (Client.correctPassword(input))
		{
			password = Client.hashPassword(input);
		}
	}

	/**
	 * Password setter
	 * @param input password <strong>HASH</strong>
	 * Does not do any checks
	 */
	public void setPasswordHash(String input)
	{
		password = input;
	}

	/**
	 * Function to hash the password
	 * @param input correct password string
	 * @return password hashed with bcrypt
	 */

	public long getID()
	{
		return id;
	}

	public String getFirstName()
	{
		return first_name;
	}

	public String getLastName()
	{
		return last_name;
	}

	public LocalDate getDateOfBirth()
	{
		return date_of_birth;
	}

	public String getDateOfBirthString()
	{
		if (getDateOfBirth() != null)
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			return formatter.format(getDateOfBirth());
		}
		return "";
	}

	public String getUsername()
	{
		return username;
	}

	/**
	 * @return returns password <strong>HASH</strong>
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @return true if this password is equal to the one we have hashed
	 */
	public boolean checkPassword(String input)
	{
		return BCrypt.checkpw(input, password);
	}

	public static String hashPassword(String input)
	{
		return BCrypt.hashpw(input, BCrypt.gensalt(HASH_ROUNDS));
	}

	/**
	 * @param input Input string
	 * @return @see correctName(String)
	 */
	public static boolean correctFirstName(String input)
	{
		if (input == null)
		{
			return false;
		}
		return Client.correctName(input);
	}

	/**
	 * @param input Input string
	 * @return true if valid last name
	 * Correct last name is either a null or @see correctName(String)
	 */
	public static boolean correctLastName(String input)
	{
		return input == null || Client.correctName(input);
	}

	/**
	 * @param input Input string
	 * @return true if input can be saved as first/last name
	 * Restrictions:
	 * Does not contain only special chars @see #ONLY_SPECIAL_CHARS
	 * At least 1 char
	 * At most @see #NAME_MAX_CHARS
	 */
	private static boolean correctName(String input)
	{
		Pattern onlySpecials = Pattern.compile(Client.ONLY_SPECIAL_CHARS);
		Matcher m1 = onlySpecials.matcher(input);
		return !m1.matches() && input.length() <= Client.NAME_MAX_CHARS;
	}

	/**
	 * Intermediate function for username/password checking.
	 * @param input Input string
	 * @return true if input can potentially be saved as username/password
	 * Restrictions:
	 * Is a valid string according to regex in @see #CORRECT_ACCOUNT_FIELD
	 * Does not contain only special chars @see #ONLY_SPECIAL_CHARS
	 * At least 1 char
	 */
	private static boolean correctAccountField(String input)
	{
		Pattern onlyWhitelistChars = Pattern.compile(Client.CORRECT_ACCOUNT_FIELD);
		Pattern onlySpecials = Pattern.compile(Client.ONLY_SPECIAL_CHARS);
		Matcher m = onlyWhitelistChars.matcher(input);
		Matcher m1 = onlySpecials.matcher(input);
		return m.matches() && !m1.matches();
	}

	/**
	 * Function to check if the username if correct.
	 * @param input Input string
	 * @return true if this is a valid username string
	 * Checks if this is a @see #correctAccountField(String) and it's less than @see #USERNAME_MAX_CHARS;
	 */
	public static boolean correctUsername(String input)
	{
		if (input == null)
		{
			return false;
		}
		return correctAccountField(input) && input.length() <= Client.USERNAME_MAX_CHARS;
	}

	public static LocalDate parseDateString(String input) throws DateTimeParseException
	{
		if (input == null)
		{
			return null;
		}
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate localDate = LocalDate.parse(input, dateFormat);
		return localDate;
	}

	/**
	 * Function to check if the password if correct.
	 * @param input Input string
	 * @return true if this is a valid password string
	 * Checks if this is a @see #correctAccountField(String) and it's less than @see #PASSWORD_MAX_CHARS;
	 */
	public static boolean correctPassword(String input)
	{
		if (input == null)
		{
			return false;
		}
		return correctAccountField(input) && input.length() <= Client.PASSWORD_MAX_CHARS;
	}

	/**
	 * Function to check if input is a valid date of birth
	 * @param input input date
	 * @return true if it's not after today or null
	 */
	public static boolean correctDateOfBirth(LocalDate input)
	{
		return input == null || !input.isAfter(LocalDate.now());
	}

	/**
	 * Client's ID in the database
	 * NOT NULL
	 */
	private long id;

	/**
	 * Client's first name.
	 * @see correctName(String)
	 * NOT NULL.
	 */
	private String first_name = "";

	/**
	 * Client's last name.
	 * @see correctName(String)
	 * May be NULL.
	 */
	private String last_name;

	/**
	 * Client's date of birth.
	 * @see correctDateOfBirth(Date)
	 * May be NULL.
	 */
	private LocalDate date_of_birth;

	/**
	 * Client's username.
	 * @see correctUsername(String)
	 * NOT NULL UNIQUE.
	 */
	private String username = "";

	/**
	 * Client's password hash.
	 * @see correctPassword(String)
	 * NOT NULL.
	 */
	private String password = "";

	/**
	 * Maximum characters allowed in first_name and last_name fields
	 */
	private static final int NAME_MAX_CHARS = 600;

	/**
	 * Maximum characters allowed for usernames
	 */
	private static final int USERNAME_MAX_CHARS = 60;

	/**
	 * Maximum characters allowed for passwords
	 */
	private static final int PASSWORD_MAX_CHARS = 51;

	/**
	 * Regex for a correct username/password
	 */
	private static final String CORRECT_ACCOUNT_FIELD = "^[a-zA-Z0-9\\!\\@\\#\\$\\%\\^\\&\\*\\\\\\/\\(\\)\\[\\]\\.\\_\\-\\s]+$";

	/**
	 * Regex that contains only special characters
	 */
	private static final String ONLY_SPECIAL_CHARS = "^[\\!\\@\\#\\$\\%\\^\\&\\*\\\\\\/\\(\\)\\[\\]\\.\\_\\-\\s]+$";

	/**
	 * Amount of rounds that brypt does 10-31
	 */
	private static final int HASH_ROUNDS = 12;

	public static final ZoneId zoneID = ZoneId.of("UTC");

}
