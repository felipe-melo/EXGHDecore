<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="im.compIII.exghdecore.entidades.Contrato"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Contrato</title>
	</head>
	<body>
		
		<form name="contrato" action="ServicoListarContrato" method="post">
			<table width="80%">
				<tr>
					<th>Selecione</th>
					<th>Comissão</th>
				</tr>
				<% Collection<Contrato> contratos = (Collection<Contrato>) request.getAttribute("contratos");
				ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
				int i = 0;
				for (Contrato contrato: contratos) {
					long id = ids.get(i);%>
					<tr align="center">
					    <td><input type="radio" name="id" value="<%=id%>"></td>
					    <td><%=contrato.getComissao()%></td>
					</tr>
				<%i++;
				} %>
			</table>
			
			<input type="submit" name ="acaoListar" value = "criar">
			<input type="submit" name ="acaoListar" value = "ver">
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>