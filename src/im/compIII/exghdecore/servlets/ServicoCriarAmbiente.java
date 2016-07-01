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
import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Contrato;
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
				case "listar mobilias":
					listarMobilias(req, resp);
				break;
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
		
		Collection<Comodo> comodos;
		
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
		
		req.getRequestDispatcher("WEB-INF/ambiente/CriarAmbiente.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			float comissao = Float.valueOf(req.getParameter("comissao"));
			int numParedes = Integer.valueOf(req.getParameter("numParedes"));
			int numPortas = Integer.valueOf(req.getParameter("numPortas"));
			float metragem = Integer.valueOf(req.getParameter("metragem"));
			String[] mobiliasId = req.getParameterValues("checkMobilias");
			int[] quantidades = null;
			
			if (mobiliasId != null) {
				quantidades = new int[mobiliasId.length];
				
				int i = 0;
				for (String mob: mobiliasId) {
					quantidades[i] = Integer.valueOf(req.getParameter("quantidade-" + mob));
					i++;
				}
			}
			
			Ambiente ambiente = new Ambiente(numParedes, numPortas, metragem);
			Contrato contrato = new Contrato(comissao);
			ambiente.setContrato(contrato);
			ambiente.adicionar(mobiliasId, quantidades);
			req.setAttribute("message", "Ambiente criada com sucesso!");
		}catch(ClassNotFoundException cnfe){
			req.setAttribute("erro", "falha no servidor!");
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
	
	private void listarMobilias(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				
		Collection<Comodo> comodos;
		
		try {
			long comodoId = Long.valueOf(req.getParameter("comodo"));
			
			Comodo comodo = Comodo.buscar(comodoId);
			comodos = Comodo.buscarTodos();
			
			req.setAttribute("comodos", comodos);
			
			if (comodo != null) {
				Collection<Mobilia> mobilias = comodo.listaMobiliaDisponivel();
				req.setAttribute("mobilias", mobilias);
			}
		} catch (ClassNotFoundException | SQLException e) {
			req.setAttribute("erro", "Falhar na base de dados.");
			e.printStackTrace();
		} finally {
			req.getRequestDispatcher("WEB-INF/ambiente/CriarAmbiente.jsp").forward(req, resp);
		}
	}
}
