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

@WebServlet("/ServicoCriarMobilia")
public class ServicoCriarMobilia extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoCriar");

		if (acao != null) {
			switch (acao) {
				case "criar":
					criar(req, resp);
				default:
					req.getRequestDispatcher("ServicoListarMobilia").forward(req, resp);
			}
		} else {
			criarForm(req, resp);
		}
	}
	
	private void criarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Collection<Comodo> comodos;
		
		try {
			comodos = Comodo.listarTodos();
		} catch (ClassNotFoundException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		} catch (SQLException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		}
		
		req.setAttribute("comodos", comodos);
		
		req.getRequestDispatcher("WEB-INF/mobilia/CriarMobilia.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		String descricao = req.getParameter("descricao");
		double custo = Double.valueOf(req.getParameter("custo"));
		int tempoEntrega = Integer.valueOf(req.getParameter("tempoEntrega"));
		String[] comodosId = req.getParameterValues("checkComodos");
		Mobilia mobilia = null;
		try{
			mobilia = new Mobilia(descricao, custo, tempoEntrega, comodosId);
			mobilia.salvar();
			req.setAttribute("message", "Mobilia criada com sucesso!");
		}catch(ClassNotFoundException cnfe){
			req.setAttribute("erro", "Valor invário para o Tipo!");
		}catch(CampoVazioException cve){
			req.setAttribute("erro", "campo " + cve.getMessage() + " é obrigatório.");
		}catch(SQLException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}
	}
}
