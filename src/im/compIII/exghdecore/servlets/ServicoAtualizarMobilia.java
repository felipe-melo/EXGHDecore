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
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;

@WebServlet("/ServicoAtualizarMobilia")
public class ServicoAtualizarMobilia extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoAtualizar");

		if (acao != null) {
			switch (acao) {
				case "atualizar":
					atualizar(req, resp);
				case "voltar":
					req.getRequestDispatcher("ServicoListarMobilia").forward(req, resp);
					break;
				default:
					
			}
		}else{
			buscarForm(req, resp);
		}
	}
	
	private void buscarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			long id = Long.valueOf(req.getParameter("id"));
			Mobilia mobilia = Mobilia.buscar(id);
			req.setAttribute("mobilia", mobilia);
			req.setAttribute("id", id);
			
			Collection<Comodo> comodos;
			Collection<Long> comodosIds = new ArrayList<Long>();
			
			if (mobilia.getComodos() != null) {
				for(Comodo c: mobilia.getComodos()) {
					comodosIds.add(c.getId());
				}
			}
			
			req.setAttribute("comodosIds", comodosIds);
			
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
			
			req.getRequestDispatcher("WEB-INF/mobilia/AtualizarMobilia.jsp").forward(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("erro", "falha ao carregar mobilia!");
			req.setAttribute("acaoListar", "");
			req.getRequestDispatcher("ServicoListarMobilia").forward(req, resp);
		}
	}
	
	private void atualizar(HttpServletRequest req, HttpServletResponse resp) {
		try{
			long id = Long.valueOf(req.getParameter("id"));
			String descricao = req.getParameter("descricao");
			double custo = Double.valueOf(req.getParameter("custo"));
			int tempoEntrega = Integer.valueOf(req.getParameter("tempoEntrega"));
		
			Mobilia mobilia = new Mobilia(id, descricao, custo, tempoEntrega);
			mobilia.atualizar();
			req.setAttribute("message", "Mobilia atualizado com sucesso!");
		}catch(ClassNotFoundException cnfe){
			req.setAttribute("erro", "Valor invário para o Tipo!");
		}catch(CampoVazioException cve){
			req.setAttribute("erro", "campo " + cve.getMessage() + " é obrigatório.");
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}
	}
}
