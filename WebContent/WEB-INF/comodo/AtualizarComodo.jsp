<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="im.compIII.exghdecore.entidades.Sala"%>
<%@ page import="im.compIII.exghdecore.entidades.Quarto"%>
<%@ page import="im.compIII.exghdecore.entidades.Cozinha"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Comodo</title>
	</head>
	<body>
		<form name="comodo" action="ServicoAtualizarComodo" method="post">
			<% Comodo comodo = (Comodo) request.getAttribute("comodo"); %>
			<input type="hidden" name="id" readonly="true" value="<%=comodo.getComodoID() %>" /><br>
			Descrição:<input type="text" name="descricao" value="<%=comodo.getDescricao() %>"/><br>
			<select name="tipo" readonly="true">
				<option value="">tipo</option>
			    <option value="<%= Constants.SALA %>" <%if (comodo instanceof Sala){ %> selected <%} %> >Sala</option>
			    <option value="<%= Constants.QUARTO %>" <%if (comodo instanceof Quarto){ %> selected <%} %> >Quarto</option>
			    <option value="<%= Constants.COZINHA %>" <%if (comodo instanceof Cozinha){ %> selected <%} %>>Cozinha</option>
			</select><br>
			<input type="submit" name="acaoAtualizar" value="atualizar" />
			<input type="submit" name="acaoAtualizar" value="voltar" />
		</form>
		<br>
	</body>
</html>