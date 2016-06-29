package im.compIII.exghdecore.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import im.compIII.exghdecore.banco.CozinhaDB;
import im.compIII.exghdecore.banco.QuartoDB;
import im.compIII.exghdecore.banco.SalaDB;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.util.Constants;

@WebServlet("/ServicoRemoverComodo")
public class ServicoRemoverComodo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoCriar");
		
		if (acao == null)
			acao = "";

		switch (acao) {
			case "remover":
				remover(req, resp);
				break;
			default:
				req.getRequestDispatcher("ServicoListarComodo").forward(req, resp);
		}
	}
	
	private void remover(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			long id = Long.valueOf(req.getParameter("id"));
			int tipo = Integer.valueOf(req.getParameter("tipo-" + id));
			
			if (tipo == Constants.SALA) {
				SalaDB db = new SalaDB();
				db.remover(id);
			} else if (tipo == Constants.QUARTO) {
				QuartoDB db = new QuartoDB();
				db.remover(id);
			} else if (tipo == Constants.COZINHA) {
				CozinhaDB db = new CozinhaDB();
				db.remover(id);
			}
			req.setAttribute("message", "Comodo removido com sucesso!");
		}catch(ClassNotFoundException | NumberFormatException cnfe){
			req.setAttribute("erro", "Valor invário para o Tipo!");
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}catch (NoRemoveException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Este comodo possiu associações não foi possível remover.");
		}finally{
			req.setAttribute("acaoListar", "");
		}
	}
}
