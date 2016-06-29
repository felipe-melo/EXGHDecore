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
		<title>Contrato</title>
	</head>
	<body>
		<form name="contrato" action="ServicoCriarContrato" method="post">
			Comissão:<input type="text" name="comissao"><br>
			Ambientes:<br>
				<% Collection<Ambiente> ambientes = (Collection<Ambiente>) request.getAttribute("ambientes");
				ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
				int i = 0;
				for (Ambiente ambiente: ambientes) {%>
					<input type="checkbox" name="checkAmbientes" value="<%=ids.get(i)%>"><%=ambiente.getMetragem()%><br>
				<%i++;
				}%>
			<input type="submit" name="acaoCriar" value="criar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>