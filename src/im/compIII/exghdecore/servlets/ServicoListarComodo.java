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

import im.compIII.exghdecore.banco.ComodoDB;
import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.util.Constants;

@WebServlet("/ServicoListarComodo")
public class ServicoListarComodo extends HttpServlet {
	
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
				
				String id = (String)req.getParameter("id");
				if (id == null) {
					req.setAttribute("erro", "Erro ao carregar Comodo.");
					this.listar(req, resp);
					break;
				}
				
				if (((String)req.getParameter("tipo-" + id)).equals(String.valueOf(Constants.COMPOSTO))) {
					req.getRequestDispatcher("ServicoAtualizarComodoComposto").forward(req, resp);
				}else{
					req.getRequestDispatcher("ServicoAtualizarComodo").forward(req, resp);
				}
				break;
			case "criar":
				req.getRequestDispatcher("ServicoCriarComodo").forward(req, resp);
				break;
			case "remover":
				id = (String)req.getParameter("id");
				if (id == null) {
					req.setAttribute("erro", "Selecione o Comodo a ser removido.");
					this.listar(req, resp);
					break;
				}
				req.getRequestDispatcher("ServicoRemoverComodo").forward(req, resp);
				break;
			case "criar composto":
				req.getRequestDispatcher("ServicoCriarComodoComposto").forward(req, resp);
				break;
			default:
				this.listar(req, resp);
		}
	}
	
	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Comodo> comodos;
		Collection<Long> ids= new ArrayList<Long>();
		try {
			comodos = ComodoDB.listarTodos(ids);
		} catch (SQLException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		}
		
		req.setAttribute("comodos", comodos);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/comodo/ListarComodo.jsp").forward(req, resp);
	}
}
