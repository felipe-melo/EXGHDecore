<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Ambiente"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Ambiente</title>
	</head>
	<body>
		<form name="ambiente" action="ServicoListarAmbiente" method="post">
			<table width="80%">
				<tr>
					<th>Selecione</th>
					<th>Comissão</th>
					<th>Número de paredes</th>
					<th>Número de portas</th>
					<th>Metragem</th>
				</tr>
				<% Collection<Ambiente> ambientes = (Collection<Ambiente>) request.getAttribute("ambientes");
				int i = 0;
				for (Ambiente ambiente: ambientes) {%>
					<tr align="center">
						<input type="hidden" name="contrato-<%=ambiente.getId() %>" value="<%=ambiente.getContrato().getId() %>">
					    <td><input type="radio" name="id" value="<%=ambiente.getId() %>"></td>
					    <td><%=ambiente.getContrato().getComissao()%></td>
					    <td><%=ambiente.getNumParedes()%></td>
					    <td><%=ambiente.getNumPortas()%></td>
					    <td><%=ambiente.getMetragem()%></td>
					</tr>
				<%
				} i++; %>
			</table>
			
			<input type="submit" name="acaoListar" value="criar">
			<input type="submit" name="acaoListar" value="ver/atualizar">
			<input type="submit" name="acaoListar" value="adicionar">
			<input type="submit" name="acaoListar" value="remover">
			
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>