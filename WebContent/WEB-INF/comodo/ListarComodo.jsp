<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="im.compIII.exghdecore.entidades.Sala"%>
<%@ page import="im.compIII.exghdecore.entidades.Cozinha"%>
<%@ page import="im.compIII.exghdecore.entidades.Quarto"%>
<%@ page import="im.compIII.exghdecore.entidades.ComodoComposto"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Comodo</title>
	</head>
	<body>
		
		<form name="comodo" action="ServicoListarComodo" method="post">
			<table width="80%">
				<tr>
					<th>Selecione</th>
					<th>Descricao</th>
					<th>Tipo</th>
				</tr>
				<% Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
				ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
				int i = 0;
				for (Comodo comodo: comodos) {
					long id = ids.get(i);%>
					<tr align="center">
					    <td><input type="radio" name="id" value="<%=id%>"></td>
					    <td><%=comodo.getDescricao()%></td>
					    <% if(comodo instanceof Sala) { %>
					    	<td><input type="hidden" name="tipo-<%=id%>" value="<%=Constants.SALA%>">Sala</td>
					 	<%} if(comodo instanceof Quarto) { %>
					 		<td><input type="hidden" name="tipo-<%=id%>" value="<%=Constants.QUARTO%>">Quarto</td>
					 	<%} if (comodo instanceof Cozinha) { %>
					 		<td><input type="hidden" name="tipo-<%=id%>" value="<%=Constants.COZINHA%>">Cozinha</td>
					 	<%}if (comodo instanceof ComodoComposto) { %>
					 		<td><input type="hidden" name="tipo-<%=id%>" value="<%=Constants.COMPOSTO%>">Composto</td>
					 	<%}%>
					</tr>
				<%i++;
				} %>
			</table>
			
			<input type="submit" name ="acaoListar" value = "criar">
			<input type="submit" name ="acaoListar" value = "ver/atualizar">
			<input type="submit" name ="acaoListar" value = "remover">
			<input type="submit" name ="acaoListar" value = "criar composto">
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>