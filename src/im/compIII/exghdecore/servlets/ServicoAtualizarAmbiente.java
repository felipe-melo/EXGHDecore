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

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;

@WebServlet("/ServicoAtualizarAmbiente")
public class ServicoAtualizarAmbiente extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoAtualizar");

		if (acao != null) {
			switch (acao) {
				case "listar mobilias":
					listarMobilias(req, resp);
				break;			
				case "atualizar":
					atualizar(req, resp);
				case "voltar":
					req.getRequestDispatcher("ServicoListarAmbiente").forward(req, resp);
					break;
				default:
					
			}
		}else{
			buscarForm(req, resp);
		}
	}
	
	private void buscarForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			Collection<Long> mobiliasIds = new ArrayList<Long>(); //Collection dos ids das mobilias associadas ao ambiente
			long id = Long.valueOf(req.getParameter("id"));
			Ambiente ambiente = Ambiente.buscar(id);
			req.setAttribute("ambiente", ambiente);
			req.setAttribute("id", id);
			
			Collection<Comodo> comodos;
			
			if (ambiente != null) {
				for (ItemVenda itemVenda: ambiente.getItensVenda()) {
					mobiliasIds.add(itemVenda.getMobilia().getId());
				}
			}
			
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
			req.setAttribute("mobiliasIds", mobiliasIds);
			
			req.getRequestDispatcher("WEB-INF/ambiente/AtualizarAmbiente.jsp").forward(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("erro", "falha ao carregar ambiente!");
			req.setAttribute("acaoListar", "");
			req.getRequestDispatcher("ServicoListarAmbiente").forward(req, resp);
		}
	}
	
	private void atualizar(HttpServletRequest req, HttpServletResponse resp) {
		
		try{
			
			float comissao = Float.valueOf(req.getParameter("comissao"));
			long contratoId = Long.valueOf(req.getParameter("contratoId"));
			
			int numParedes = Integer.valueOf(req.getParameter("numParedes"));
			int numPortas = Integer.valueOf(req.getParameter("numPortas"));
			float metragem = Float.valueOf(req.getParameter("metragem"));
			
			long id = Long.valueOf(req.getParameter("id"));
			
			String[] mobiliasId = req.getParameterValues("checkMobilias");
			String[] quantidades = req.getParameterValues("quantidades");
			
			String[] mobIds = new String[mobiliasId.length];
			int[] mobquant = new int[mobiliasId.length];
				
			int i = 0;
			for (String mob: mobiliasId) {
				String[] aux = mob.split("-");
				mobIds[i] = aux[0];
				int index = Integer.valueOf(aux[1]);
				mobquant[i] = Integer.valueOf(quantidades[index]);
				i++;
			}
			
			Ambiente ambiente = new Ambiente(id, numParedes, numPortas, metragem);
			Contrato contrato = new Contrato(contratoId, comissao);
			ambiente.setContrato(contrato);
			ambiente.atualizar(mobIds, mobquant);
			req.setAttribute("message", "Ambiente atualizado com sucesso!");
		}catch(NumberFormatException cnfe){
			cnfe.printStackTrace();
			req.setAttribute("erro", "Valores inválidos!");
		}catch(CampoVazioException cve){
			req.setAttribute("erro", "campo " + cve.getMessage() + " é obrigatório.");
		}catch(SQLException | ClassNotFoundException sqlE){
			sqlE.printStackTrace();
			req.setAttribute("erro", "Falha no banco dados.");
		}catch (ConexaoException ce) {
			ce.printStackTrace();
			req.setAttribute("erro", "Falhar na conexão com o servidor.");
		}
	}
	
	private void listarMobilias(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			Collection<Long> mobiliasIds = new ArrayList<Long>(); //Collection dos ids das mobilias associadas ao ambiente
			long id = Long.valueOf(req.getParameter("id"));
			Ambiente ambiente = Ambiente.buscar(id);
			
			if (ambiente != null) {
				for (ItemVenda itemVenda: ambiente.getItensVenda()) {
					mobiliasIds.add(itemVenda.getMobilia().getId());
				}
			}
			
			req.setAttribute("ambiente", ambiente);
			req.setAttribute("id", id);
			
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
			req.setAttribute("mobiliasIds", mobiliasIds);
			
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
		} catch (ConexaoException e1) {
			req.setAttribute("erro", "Falhar no servidor.");
			e1.printStackTrace();
		} finally {
			req.getRequestDispatcher("WEB-INF/ambiente/AtualizarAmbiente.jsp").forward(req, resp);
		}
	}
}
