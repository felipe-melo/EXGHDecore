<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="im.compIII.exghdecore.entidades.ComodoComposto"%>
<%@ page import="java.util.Collection"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Comodo</title>
	</head>
	<body>
		<form name="comodo" action="ServicoAtualizarComodoComposto" method="post">
			<% Comodo comodo = (Comodo) request.getAttribute("comodo"); %>
			<hidden name="id" readonly="true" value="<%=comodo.getComodoID() %>" /><br>
			Descrição:<input type="text" name="descricao" value="<%=comodo.getDescricao() %>"/><br>
			<%Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
			for (Comodo c: comodos) {%>
				<input type="checkbox" name="checkComodos" readonly="true" value="<%=c.getComodoID()%>"><%=c.getDescricao()%><br>
			<%}%><br>
			<input type="submit" name="acaoAtualizar" value="atualizar" />
			<input type="submit" name="acaoAtualizar" value="voltar" />
		</form>
		<br>
	</body>
</html>