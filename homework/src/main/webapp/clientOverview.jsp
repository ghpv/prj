<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<%@ taglib prefix = "c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt"%>

<html>
	<body>
		<table border="1">
			<tr align="center">
				<td>ID</td>
				<td>FIRST NAME</td>
				<td>LAST NAME</td>
				<td>DATE OF BIRTH</td>
				<td>USERNAME</td>
				<td>PASSWORD</td>
				<td>DELETE</td>
				<td>EDIT</td>
			</tr>
			<c:forEach items="${clients}" var="client">
				<tr align="center">
					<td>${client.getID()}</td>
					<td>${client.getFirstName()}</td>
					<td>${client.getLastName()}</td>
					<td>${client.getDateOfBirthString()}</td>
					<td>${client.getUsername()}</td>
					<td>${client.getPassword()}</td>
					<td>
						<form action="/deleteClient" method="POST" onsubmit="return confirm('Really delete this client?');">
							<input type="hidden" name="ID" value="${client.getID()}">
							<input type="submit" value="X">
						</form>
					</td>
					<td>
						<form action="/editClient" method="POST">
							<input type="hidden" name="ID" value="${client.getID()}">
							<input type="hidden" name="FORM_EDITED" value="0">
							<input type="submit" value="E">
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
		<form action="/addClient" method="POST">
			<input type="hidden" name="FORM_EDITED" value="0"/>
			<input type="submit" value="ADD NEW CLIENT">
		</form>
	</body>
</html>
