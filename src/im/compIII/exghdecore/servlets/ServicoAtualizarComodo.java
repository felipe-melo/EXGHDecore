package im.compIII.exghdecore.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import im.compIII.exghdecore.banco.ComodoDB;
import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Cozinha;
import im.compIII.exghdecore.entidades.Quarto;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.util.Constants;

@WebServlet("/ServicoAtualizarComodo")
public class ServicoAtualizarComodo extends HttpServlet {
	
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
		
		try {
			long id = Long.valueOf(req.getParameter("id"));
			Comodo comodo = ComodoDB.buscar(id);
			req.setAttribute("comodo", comodo);
			req.setAttribute("id", id);
			req.getRequestDispatcher("WEB-INF/comodo/AtualizarComodo.jsp").forward(req, resp);
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
		int tipo = Integer.valueOf(req.getParameter("tipo"));
		Comodo comodo = null;
		try{
			if (tipo == Constants.SALA)
				comodo = new Sala(descricao, tipo);
			else if (tipo == Constants.QUARTO)
				comodo = new Quarto(descricao, tipo);
			else if (tipo == Constants.COZINHA)
				comodo = new Cozinha(descricao, tipo);
			ComodoDB db = new ComodoDB();
			db.atualizar(id, comodo);
			req.setAttribute("message", "Comodo atualizado com sucesso!");
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
