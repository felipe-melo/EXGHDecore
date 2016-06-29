<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Mobilia</title>
	</head>
	<body>
		<form name="mobilia" action="ServicoAtualizarMobilia" method="post">
			<% Mobilia mobilia = (Mobilia) request.getAttribute("mobilia");
			long id = (Long) request.getAttribute("id");%>
			<input type="hidden" name="id" readonly="true" value="<%=id %>" />
			Descrição:<input type="text" name="descricao" value="<%=mobilia.getDescricao()%>"/><br>
			Custo:<input type="text" name="custo" value="<%=mobilia.getCusto()%>"/><br>
			Tempo de Entrega:<input type="text" name="tempoEntrega" value="<%=mobilia.getTempoEntrega()%>"/><br><br>
			Comodos:<br>
				<% Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
				ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
				int i = 0;
				for (Comodo comodo: comodos) {%>
					<input type="checkbox" name="checkComodos" value="<%=ids.get(i)%>"><%=comodo.getDescricao()%><br>
				<%i++;
				}%>
			<input type="submit" name="acaoAtualizar" value="atualizar" />
			<input type="submit" name="acaoAtualizar" value="voltar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>