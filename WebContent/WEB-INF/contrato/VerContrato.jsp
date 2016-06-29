<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Contrato"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Contrato</title>
	</head>
	<body>
		<form name="contrato" action="ServicoVerContrato" method="post">
			<%Contrato contrato = (Contrato) request.getAttribute("contrato");%>
			Comissão:<input type="text" name="comissao" value="<%=contrato.getComissao()%>"><br>
			Valor:<input type="text" name="valor" value="<%=contrato.valorContrato()%>"><br>
			Prazo:<input type="text" name="valor" value="<%=contrato.prazo()%>"><br>
			<input type="submit" name="acaoCriar" value="voltar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>