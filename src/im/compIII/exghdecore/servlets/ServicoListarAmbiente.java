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
import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.exceptions.ConexaoException;

@WebServlet("/ServicoListarAmbiente")
public class ServicoListarAmbiente extends HttpServlet {
	
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
				req.getRequestDispatcher("ServicoAtualizarAmbiente").forward(req, resp);
				break;
			case "criar":
				req.getRequestDispatcher("ServicoCriarAmbiente").forward(req, resp);
				break;
			case "remover":
				String id = req.getParameter("id");
				if (id == null) {
					req.setAttribute("erro", "Selecione o Ambiente a ser removido.");
					this.listar(req, resp);
					break;
				}
				req.getRequestDispatcher("ServicoRemoverAmbiente").forward(req, resp);
				break;
			case "adicionar":
				if (req.getParameter("id") != null) {
					String contratoId = (String)req.getParameter("contrato-" + req.getParameter("id"));
					req.setAttribute("contratoId", contratoId);
					req.getRequestDispatcher("ServicoAdicionarAmbiente").forward(req, resp);
					break;
				}
				req.setAttribute("erro", "Selecione um ambiente.");
			default:
				this.listar(req, resp);
		}
	}
	
	private void listar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Ambiente> ambientes;
		try {
			ambientes = Ambiente.buscarTodos();
		} catch (ConexaoException | SQLException e) {
			ambientes = new ArrayList<Ambiente>();
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			ambientes = new ArrayList<Ambiente>();
			e.printStackTrace();
		}
		
		req.setAttribute("ambientes", ambientes);
		
		req.getRequestDispatcher("WEB-INF/ambiente/ListarAmbiente.jsp").forward(req, resp);
	}
}
