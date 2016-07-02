<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="im.compIII.exghdecore.util.Constants"%>
<%@ page import="im.compIII.exghdecore.entidades.Mobilia"%>
<%@ page import="im.compIII.exghdecore.entidades.Ambiente"%>
<%@ page import="im.compIII.exghdecore.entidades.Comodo"%>
<%@ page import="im.compIII.exghdecore.entidades.Contrato"%>
<%@ page import="im.compIII.exghdecore.entidades.ItemVenda"%>
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
			Collection<Mobilia> mobilias = (Collection<Mobilia>) request.getAttribute("mobilias");
			Collection<Long> mobiliasIds = (Collection<Long>) request.getAttribute("mobiliasIds");
			
			long id = (Long) request.getAttribute("id");%>
			<input type="hidden" name="id" value="<%=id%>" />
			<input type="hidden" name="contratoId" value="<%=ambiente.getContrato().getId()%>" />
			Número de Paredes:<input type="text" name="numParedes" value="<%=ambiente.getNumParedes()%>"/><br>
			Número de Portas:<input type="text" name="numPortas" value="<%=ambiente.getNumPortas()%>"/><br>
			Metragem:<input type="text" name="metragem" value="<%=ambiente.getMetragem()%>"/><br><br>
			comissao:<input type="text" name="comissao" value="<%=ambiente.getContrato().getComissao()%>"/><br><br>
			custo:<input type="text" name="custo" value="<%=ambiente.custo()%>"/><br><br>
			<% if (mobiliasIds != null && mobiliasIds.size() == 0) { %>
			<select name="comodo">
				<option value="">comodo</option>
				<% Collection<Comodo> comodos = (Collection<Comodo>) request.getAttribute("comodos");
				
					for (Comodo comodo: comodos) {
						long comodoId = comodo.getId();%>
						<option value="<%=comodoId%>"><%=comodo.getDescricao()%></option>
					<%}%>
				
			</select><input type="submit" name="acaoAtualizar" value="listar mobilias" /><br>
			<%}%>
			
			<% 	
				if (mobilias != null) {
					int i = 0;
					for (Mobilia mobilia: mobilias) {%>
						<input type="checkbox" name="checkMobilias" value="<%=mobilia.getId()%>-<%=i%>"><%=mobilia.getDescricao()%>
						<input type="text" name="quantidades"><br>
					<%i++;
					}
				}else if (mobiliasIds != null && mobiliasIds.size() > 0) {%>
				Mobilias:
				<%	for (ItemVenda itemVenda: ambiente.getItensVenda()) {%>
						<%=itemVenda.getMobilia().getDescricao() %> - <%=itemVenda.getQuantidade() %><br>
					<%
					}
				}
			%>
			<br>
			<input type="submit" name="acaoAtualizar" value="atualizar" />
			<input type="submit" name="acaoAtualizar" value="voltar" />
		</form>
		<br>
		<%@include file="../messagePage.jsp" %>
	</body>
</html>