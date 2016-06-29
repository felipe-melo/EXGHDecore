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
import im.compIII.exghdecore.banco.ContratoDB;
import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

@WebServlet("/ServicoCriarContrato")
public class ServicoCriarContrato extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoCriar");

		if (acao != null) {
			switch (acao) {
				case "criar":
					criar(req, resp);
				default:
					req.getRequestDispatcher("ServicoListarContrato").forward(req, resp);
			}
		} else {
			criarForm(req, resp);
		}
	}
	
	private void criarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Collection<Ambiente> ambientes;
		Collection<Long> ids = new ArrayList<Long>();
		
		try {
			ambientes = AmbienteDB.listarTodosSemContrato(ids);
		} catch (ClassNotFoundException e) {
			ambientes = new ArrayList<Ambiente>();
			e.printStackTrace();
		} catch (SQLException e) {
			ambientes = new ArrayList<Ambiente>();
			e.printStackTrace();
		} catch (ConexaoException e) {
			ambientes = new ArrayList<Ambiente>();
			e.printStackTrace();
		}
		
		req.setAttribute("ambientes", ambientes);
		req.setAttribute("ids", ids);
		
		req.getRequestDispatcher("WEB-INF/contrato/CriarContrato.jsp").forward(req, resp);
	}
	
	private void criar(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			String ambientes[] = req.getParameterValues("checkAmbientes");
			float comissao = Float.valueOf(req.getParameter("comissao"));
			Contrato contrato = new Contrato(comissao, new ArrayList<Ambiente>());
			ContratoDB db = new ContratoDB();
			db.salvar(contrato, ambientes);
			req.setAttribute("message", "Contrato criada com sucesso!");
		}catch(NumberFormatException cnfe){
			req.setAttribute("erro", "Valores inválidos de entrada!");
		}catch(RelacaoException re){
			req.setAttribute("erro", "O contrato deve ter pelo menos um " + re.getMessage());
		}catch(SQLException | ClassNotFoundException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		} catch (CampoVazioException e) {
			req.setAttribute("erro", "campo " + e.getMessage() + "inválido.");
			e.printStackTrace();
		}
	}
}
