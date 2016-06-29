<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Comodo</title>
	</head>
	<body>
		<form name="comodo" action="ServicoCriarComodoComposto" method="post">
			Descrição:<input type="text" name="descricao" /><br>
			<%Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
			ArrayList<Long> ids = (ArrayList<Long>) request.getAttribute("ids");
			int i = 0;
			for (Comodo comodo: comodos) {%>
				<input type="checkbox" name="checkComodos" value="<%=ids.get(i)%>"><%=comodo.getDescricao()%><br>
			<%i++;
			}%>
			<br>
			<input type="submit" name="acaoCriar" value="criar" />
		</form>
		<br>
	</body>
</html>