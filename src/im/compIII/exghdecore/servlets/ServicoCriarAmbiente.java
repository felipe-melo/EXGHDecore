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

import im.compIII.exghdecore.banco.AmbienteDB;
import im.compIII.exghdecore.banco.MobiliaDB;
import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

@WebServlet("/ServicoCriarAmbiente")
public class ServicoCriarAmbiente extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoCriar");

		if (acao != null) {
			switch (acao) {
				case "criar":
					criar(req, resp);
				default:
					req.getRequestDispatcher("ServicoListarAmbiente").forward(req, resp);
			}
		} else {
			criarForm(req, resp);
		}
	}
	
	private void criarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Collection<Mobilia> mobilias;
		Collection<Long> ids = new ArrayList<Long>();
		
		try {
			mobilias = MobiliaDB.listarTodos(ids);
		} catch (ClassNotFoundException e) {
			mobilias= new ArrayList<Mobilia>();
			e.printStackTrace();
		} catch (SQLException | ConexaoException e) {
			mobilias = new ArrayList<Mobilia>();
			e.printStackTrace();
		}
		
		req.setAttribute("mobilias", mobilias);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/ambiente/CriarAmbiente.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		int numParedes = Integer.valueOf(req.getParameter("numParedes"));
		int numPortas = Integer.valueOf(req.getParameter("numPortas"));
		float metragem = Integer.valueOf(req.getParameter("metragem"));
		String[] mobiliasId = req.getParameterValues("checkMobilias");
		int[] quantidades = new int[mobiliasId.length];
		
		int i = 0;
		for (String mob: mobiliasId) {
			quantidades[i] = Integer.valueOf(req.getParameter("quantidade-" + mob));
			i++;
		}
		
		try{
			Ambiente ambiente = new Ambiente(numParedes, numPortas, metragem, new ArrayList<ItemVenda>());
			AmbienteDB db = new AmbienteDB();
			db.salvar(ambiente, mobiliasId, quantidades);
			req.setAttribute("message", "Ambiente criada com sucesso!");
		}catch(ClassNotFoundException cnfe){
			req.setAttribute("erro", "Valor invário para o Tipo!");
		}catch(RelacaoException re){
			req.setAttribute("erro", "campo " + re.getMessage() + " é obrigatório.");
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
