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

import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.ConexaoException;

@WebServlet("/ServicoListarMobilia")
public class ServicoListarMobilia extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoListar");

		if (req.getAttribute("acaoListar") != null)
			acao = (String) req.getAttribute("acaoListar");
			
		if (acao == null)
			acao = "";
		
		switch (acao) {
			case "ver/atualizar":
				req.getRequestDispatcher("ServicoAtualizarMobilia").forward(req, resp);
				break;
			case "criar":
				req.getRequestDispatcher("ServicoCriarMobilia").forward(req, resp);
				break;
			default:
				this.listar(req, resp);
		}
	}
	
	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Mobilia> mobilias;
		Collection<Long> ids = new ArrayList<Long>();
		try {
			mobilias = Mobilia.buscarTodos();
		} catch (ConexaoException | SQLException e) {
			mobilias = new ArrayList<Mobilia>();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			mobilias = new ArrayList<Mobilia>();
			e.printStackTrace();
		}
		
		req.setAttribute("mobilias", mobilias);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/mobilia/ListarMobilia.jsp").forward(req, resp);
	}
}
