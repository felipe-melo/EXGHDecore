package im.compIII.exghdecore.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import im.compIII.exghdecore.banco.ContratoDB;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.exceptions.ConexaoException;

@WebServlet("/ServicoListarContrato")
public class ServicoListarContrato extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoListar");

		if (req.getAttribute("acaoListar") != null)
			acao = (String) req.getAttribute("acaoListar");
			
		if (acao == null)
			acao = "";
		
		switch (acao) {
			case "criar":
				req.getRequestDispatcher("ServicoCriarContrato").forward(req, resp);
				break;
			case "ver":
				req.getRequestDispatcher("ServicoVerContrato").forward(req, resp);
				break;
			default:
				this.listar(req, resp);
		}
	}
	
	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Contrato> contrato;
		Collection<Long> ids = new ArrayList<Long>();
		try {
			contrato = ContratoDB.listarTodos(ids);
		} catch (ConexaoException | SQLException e) {
			contrato = new ArrayList<Contrato>();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			contrato = new ArrayList<Contrato>();
			e.printStackTrace();
		}
		
		req.setAttribute("contratos", contrato);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/contrato/ListarContrato.jsp").forward(req, resp);
	}
}
