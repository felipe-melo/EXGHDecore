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

public class Quarto extends Comodo {

	public Quarto(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}
	
	public Quarto(long id, String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		return null;
	}

	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN QUARTO Q where C.COMODOID = Q.COMODOID;";
		Statement psmt = Conexao.prepare();
		
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			long id = result.getInt("COMODOID");
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Sala(descricao, Constants.QUARTO);
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
			
			String sql = "INSERT INTO QUARTO (COMODOID) VALUES(" + this.getComodoID() + ");";
			
			Statement pstmt = Conexao.prepare();
			
			int linhasAfetadas = pstmt.executeUpdate(sql);
			
			
			
	    	if (linhasAfetadas == 0) {
	    		Conexao.rollBack();
	    		Conexao.closeConnection();
				throw new ConexaoException();
			}else{
				Conexao.commit();
	    		Conexao.closeConnection();
			}
		}
	}

}
