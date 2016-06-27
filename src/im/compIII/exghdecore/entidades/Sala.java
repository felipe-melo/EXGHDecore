package im.compIII.exghdecore.entidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import im.compIII.exghdecore.banco.Conexao;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.util.Constants;

public class Sala extends Comodo {

	public Sala(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}
	
	public Sala(long id, String descricao, int tipo) throws CampoVazioException {
		super(id, descricao, tipo);
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		return null;
	}

	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN SALA S where C.COMODOID = S.COMODOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			long id = result.getInt("COMODOID");
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Sala(descricao, Constants.SALA);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			comodo.setComodoID(id);
			list.add(comodo);
		}
		
		Conexao.closeConnection();
		return list;
	}

	@Override
	public void salvar() throws ConexaoException, SQLException, ClassNotFoundException {
		
		this.salvarProcted();
		
		if (this.getComodoID() != 0) {
			Conexao.initConnection();
			
			String sql = "INSERT INTO SALA (COMODOID) VALUES(" + this.getComodoID() + ");";
			
			Statement psmt = Conexao.prepare();
			
			int linhasAfetadas = psmt.executeUpdate(sql);
			
			Conexao.closeConnection();
		
			if (linhasAfetadas == 0) {
				throw new ConexaoException();
			}
		}
	}
}
