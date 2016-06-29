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
import im.compIII.exghdecore.banco.MobiliaDB;
import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

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
		Collection<Long> ids = new ArrayList<Long>();
		
		try {
			comodos = ComodoDB.listarTodos(ids);
		} catch (ClassNotFoundException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		} catch (SQLException e) {
			comodos = new ArrayList<Comodo>();
			e.printStackTrace();
		}
		
		req.setAttribute("comodos", comodos);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/mobilia/CriarMobilia.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			String descricao = req.getParameter("descricao");
			double custo = Double.valueOf(req.getParameter("custo"));
			int tempoEntrega = Integer.valueOf(req.getParameter("tempoEntrega"));
			String[] comodosId = req.getParameterValues("checkComodos");
			
			Mobilia mobilia = new Mobilia(descricao, custo, tempoEntrega);
			MobiliaDB db = new MobiliaDB();
			db.salvar(mobilia, comodosId);
			req.setAttribute("message", "Mobilia criada com sucesso!");
		}catch(NumberFormatException cnfe){
			req.setAttribute("erro", "Valores inválidos de entrada!");
		}catch(RelacaoException re){
			req.setAttribute("erro", "A mobilia deve ter pelo menos um " + re.getMessage());
		}catch(SQLException | ClassNotFoundException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}catch (CampoVazioException cve) {
			cve.printStackTrace();
			req.setAttribute("erro", "campo " + cve.getMessage() + " é obrigatório.");
		}
	}
}
