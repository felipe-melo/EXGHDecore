<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="im.compIII.exghdecore.entidades.Ambiente"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Ambiente</title>
	</head>
	<body>
		<form name="ambiente" action="ServicoAdicionarAmbiente" method="post">
			<%long contratoId = (Long)request.getAttribute("contratoId");%>
			<input type="hidden" name="contratoId" value="<%=contratoId%>"/>
			Número de Paredes:<input type="text" name="numParedes" /><br>
			Número de Portas:<input type="text" name="numPortas" /><br>
			Metragem:<input type="text" name="metragem" /><br><br>
			<select name="comodo">
				<option value="">comodo</option>
				<% Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
				for (Comodo comodo: comodos) {
					long id = comodo.getId(); %>
					<option value="<%=id%>"><%=comodo.getDescricao()%></option>
				<%
				}%>
			</select><input type="submit" name="acaoAdicionar" value="listar mobilias" /><br>
			
			<% Collection<Mobilia> mobilias = (Collection<Mobilia>) request.getAttribute("mobilias");
				int i = 0;
				if (mobilias != null) {
					for (Mobilia mobilia: mobilias) {%>
						<input type="checkbox" name="checkMobilias" value="<%=mobilia.getId()%>-<%=i%>"><%=mobilia.getDescricao()%>
						<input type="text" name="quantidades"><br>
					<%i++;
					}
				}
			%>
			<br>
			<input type="submit" name="acaoAdicionar" value="add" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>