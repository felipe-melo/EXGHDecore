package im.compIII.exghdecore.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String acao = (String) req.getParameter("navegacao");
		
		if (acao == null) acao = "";
		
		switch (acao) {
			case "Comodo":
				req.getRequestDispatcher("ServicoListarComodo").forward(req, resp);
				break;
			case "Mobilia":
				req.getRequestDispatcher("ServicoListarMobilia").forward(req,resp);
				break;
			case "Ambiente":
				req.getRequestDispatcher("ServicoListarAmbiente").forward(req,resp);
				break;
			case "Contrato":
				req.getRequestDispatcher("ServicoListarContrato").forward(req,resp);
				break;
			default:
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req, resp);
		}
	}
}
