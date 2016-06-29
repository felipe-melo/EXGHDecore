<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
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
		<form name="ambiente" action="ServicoAtualizarAmbiente" method="post">
			<% Ambiente ambiente = (Ambiente) request.getAttribute("ambiente");
			long id = (Long) request.getAttribute("id");%>
			<input type="hidden" name="id" readonly="true" value="<%=id%>" />
			Número de Paredes:<input type="text" name="numParedes" value="<%=ambiente.getNumParedes()%>"/><br>
			Número de Portas:<input type="text" name="numPortas" value="<%=ambiente.getNumPortas()%>"/><br>
			Metragem:<input type="text" name="metragem" value="<%=ambiente.getMetragem()%>"/><br><br>
			custo:<input type="text" name="custo" value="<%=ambiente.custo()%>"/><br><br>
			Mobilias:<br>
				<% Collection<Mobilia> mobilias = (Collection<Mobilia>) request.getAttribute("mobilias");
				ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
				int i = 0;
				for (Mobilia mobilia: mobilias) {%>
					<input type="checkbox" name="checkComodos" value="<%=ids.get(i)%>"><%=mobilia.getDescricao()%><br>
				<%i++;
				}%>
			<input type="submit" name="acaoAtualizar" value="atualizar" />
			<input type="submit" name="acaoAtualizar" value="voltar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>