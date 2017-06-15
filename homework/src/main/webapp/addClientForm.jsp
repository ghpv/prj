<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<form action="addClient" method="POST">
	<input type="text" name="FIRST_NAME" placeholder="FIRST NAME" value="${params.firstName}" required/>
	<input type="hidden" name="FORM_EDITED" value="1"/>
	<span class="error">${errors.firstName}</span>
	<br>
	<input type="text" name="LAST_NAME" placeholder="LAST NAME" value="${params.lastName}"/>
	<span>${errors.lastName}</span>
	<br>
	<input type="text" name="DATE_OF_BIRTH" placeholder="DATE OF BIRTH" value="${params.dateOfBirth}"/>
	<span>${errors.dateOfBirth}</span>
	<br>
	<input type="text" name="USERNAME" placeholder="USERNAME" value="${params.username}" required/>
	<span>${errors.username}</span>
	<br>
	<input type="password" name="PASSWORD" placeholder="PASSWORD" required/>
	<span>${errors.password}</span>
	<br>
	<input type="submit" value="ENTER"/>
</form>
