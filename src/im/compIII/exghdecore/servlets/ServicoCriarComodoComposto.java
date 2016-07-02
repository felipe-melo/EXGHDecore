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

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.ComodoComposto;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;
import im.compIII.exghdecore.util.Constants;

@WebServlet("/ServicoCriarComodoComposto")
public class ServicoCriarComodoComposto extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoCriar");

		if (acao != null) {
			switch (acao) {
				case "criar":
					criar(req, resp);
				default:
					req.getRequestDispatcher("ServicoListarComodo").forward(req, resp);
			}
		} else {
			criarForm(req, resp);
		}
	}
	
	private void criarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Collection<Comodo> comodos;
		Collection<Long> ids = new ArrayList<Long>();
		try {
			comodos = Comodo.buscarTodos();
		} catch (ClassNotFoundException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		} catch (SQLException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		}
		req.setAttribute("comodos", comodos);
		req.setAttribute("ids", ids);
		req.getRequestDispatcher("WEB-INF/comodo/CriarComodoComposto.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		String descricao = req.getParameter("descricao");
		String[] ids = req.getParameterValues("checkComodos");
		Collection<Comodo> componentes = new ArrayList<Comodo>();
		try{
			
			for (String id: ids) {
				componentes.add(Comodo.buscar(Long.valueOf(id)));
			}
			
			ComodoComposto comodo = new ComodoComposto(descricao, Constants.COMPOSTO);
			comodo.setComodos(componentes);
			comodo.adicionar();
			req.setAttribute("message", "Comodo criado com sucesso!");
		}catch(SQLException | ClassNotFoundException ex){
			ex.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}catch (CampoVazioException e) {
			e.printStackTrace();
			req.setAttribute("erro", "Campo " + e.getMessage() + " é obrigatório.");
		} catch (NullPointerException | RelacaoException e) {
			req.setAttribute("erro", "seleciona pelo menos um comodo.");
			e.printStackTrace();
		}
	}
}
