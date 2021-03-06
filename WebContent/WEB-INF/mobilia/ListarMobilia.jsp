<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Mobilia</title>
	</head>
	<body>
		<form name="mobilia" action="ServicoListarMobilia" method="post">
			<table width="80%">
				<tr>
					<th>Selecione</th>
					<th>Descricao</th>
					<th>Custo</th>
					<th>Tempo de Entrega</th>
				</tr>
				<% Collection<Mobilia> mobilias = (Collection<Mobilia>) request.getAttribute("mobilias");
				for (Mobilia mobilia: mobilias) {%>
					<tr align="center">
					    <td><input type="radio" name="id" value="<%=mobilia.getId()%>"></td>
					    <td><%=mobilia.getDescricao()%></td>
					    <td><%=mobilia.getCusto()%></td>
					    <td><%=mobilia.getTempoEntrega()%></td>
					</tr>
				<%
				} %>
			</table>
			
			<input type="submit" name ="acaoListar" value = "criar">
			<input type="submit" name ="acaoListar" value = "ver/atualizar">
			
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>