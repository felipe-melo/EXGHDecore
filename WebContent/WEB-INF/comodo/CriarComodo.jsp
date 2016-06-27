<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Comodo</title>
	</head>
	<body>
		<form name="comodo" action="ServicoCriarComodo" method="post">
			Descrição:<input type="text" name="descricao" /><br>
			<select name="tipo">
				<option value="">tipo</option>
			    <option value="<%= Constants.SALA %>">Sala</option>
			    <option value="<%= Constants.QUARTO %>">Quarto</option>
			    <option value="<%= Constants.COZINHA %>">Cozinha</option>
			</select><br>
			<input type="submit" name="acaoCriar" value="criar" />
		</form>
		<br>
	</body>
</html>