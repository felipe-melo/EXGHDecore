package im.compIII.exghdecore.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import im.compIII.exghdecore.banco.ContratoDB;
import im.compIII.exghdecore.entidades.Contrato;

@WebServlet("/ServicoVerContrato")
public class ServicoVerContrato extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String acao = (String) req.getParameter("acaoAtualizar");

		if (acao != null) {
			switch (acao) {
				case "voltar":
					req.getRequestDispatcher("ServicoListarContrato").forward(req, resp);
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
			Contrato contrato = ContratoDB.buscar(id);
			req.setAttribute("contrato", contrato);
			req.setAttribute("id", id);
			
			/*Collection<Mobilia> mobilias;
			Collection<Long> ids = new ArrayList<Long>();
			
			try {
				mobilias = MobiliaDB.listarTodos(ids);
			} catch (ClassNotFoundException e) {
				mobilias = new ArrayList<Mobilia>();
				e.printStackTrace();
			} catch (SQLException e) {
				mobilias = new ArrayList<Mobilia>();
				e.printStackTrace();
			}
			req.setAttribute("ids", ids);*/
			
			req.getRequestDispatcher("WEB-INF/contrato/VerContrato.jsp").forward(req, resp);
		}catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("erro", "falha ao carregar contrato!");
			req.setAttribute("acaoListar", "");
			req.getRequestDispatcher("ServicoListarContrato").forward(req, resp);
		}
	}
}
