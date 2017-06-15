<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<form action="editClient" method="POST">
	<input type="hidden" name="ID" value="${id}"/>
	<input type="hidden" name="FORM_EDITED" value="1"/>
	<input style="width:300px" type="text" name="FIRST_NAME" placeholder="FIRST NAME" value="${params.firstName}" required/>
	<span class="error">${errors.firstName}</span>
	<br>
	<input style="width:300px" type="text" name="LAST_NAME" placeholder="LAST NAME" value="${params.lastName}"/>
	<span>${errors.lastName}</span>
	<br>
	<input style="width:300px" type="text" name="DATE_OF_BIRTH" placeholder="DATE OF BIRTH" value="${params.dateOfBirth}"/>
	<span>${errors.dateOfBirth}</span>
	<br>
	<input style="width:300px" type="text" name="USERNAME" placeholder="USERNAME(LEAVE EMPTY FOR THE SAME)" value="${params.username}"/>
	<span>${errors.username}</span>
	<br>
	<input style="width:300px" type="password" name="PASSWORD" placeholder="PASSWORD(LEAVE EMPTY FOR THE SAME)"/>
	<span>${errors.password}</span>
	<br>
	<input type="submit" value="ENTER"/>
</form>
