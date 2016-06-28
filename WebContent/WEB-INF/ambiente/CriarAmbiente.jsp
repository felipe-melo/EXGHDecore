<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
<%@ page import="im.compIII.exghdecore.entidades.Ambiente"%>
<%@ page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Ambiente</title>
	</head>
	<body>
		<form name="ambiente" action="ServicoCriarAmbiente" method="post">
			Número de Paredes:<input type="text" name="numParedes" /><br>
			Número de Portas:<input type="text" name="numPortas" /><br>
			Metragem:<input type="text" name="metragem" /><br><br>
			Mobilias:<br>
				<% Collection<Mobilia> mobilias = (Collection<Mobilia>) request.getAttribute("mobilias");
				for (Mobilia mobilia: mobilias) {%>
					<input type="checkbox" name="checkMobilias" value="<%=mobilia.getMobiliaID()%>"><%=mobilia.getDescricao()%>
					<input type="text" name="quantidade-<%=mobilia.getMobiliaID()%>"><br>
				<%}%>
			<input type="submit" name="acaoCriar" value="criar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>