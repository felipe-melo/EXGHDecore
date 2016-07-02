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
import im.compIII.exghdecore.util.Constants;

@WebServlet("/ServicoAtualizarComodoComposto")
public class ServicoAtualizarComodoComposto extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoAtualizar");

		if (acao != null) {
			switch (acao) {
				case "atualizar":
					atualizar(req, resp);
				case "voltar":
					req.getRequestDispatcher("ServicoListarComodo").forward(req, resp);
					break;
				default:
					
			}
		}else{
			buscarForm(req, resp);
		}
	}
	
	private void buscarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Collection<Comodo> comodos;
		
		try {
			long id = Long.valueOf(req.getParameter("id"));
			Comodo comodo = ComodoComposto.buscar(id);
			req.setAttribute("comodo", comodo);
			req.setAttribute("id", id);
			
			Collection<Long> componentes = new ArrayList<Long>();
			
			for (Comodo c: ((ComodoComposto)comodo).getComodos()) {
				componentes.add(c.getId());
			}
			
			req.setAttribute("componentes", componentes);
			
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
			
			req.getRequestDispatcher("WEB-INF/comodo/AtualizarComodoComposto.jsp").forward(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("erro", "falha ao carregar comodo!");
			req.setAttribute("acaoListar", "");
			req.getRequestDispatcher("ServicoListarComodo").forward(req, resp);
		}
	}
	
	private void atualizar(HttpServletRequest req, HttpServletResponse resp) {
		long id = Long.valueOf(req.getParameter("id"));
		String descricao = req.getParameter("descricao");
		try{
			Comodo comodo = new ComodoComposto(id, descricao, Constants.COMPOSTO);
			comodo.atualizar();
			req.setAttribute("message", "Comodo atualizado com sucesso!");
		}catch(ClassNotFoundException cnfe){
			req.setAttribute("erro", "Valor invário para o Tipo!");
		}catch(CampoVazioException cve){
			req.setAttribute("erro", "campo " + cve.getMessage() + " é obrigatório.");
		}catch(SQLException sqlE){
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}
	}
}
