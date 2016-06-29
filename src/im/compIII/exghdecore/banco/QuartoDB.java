package im.compIII.exghdecore.banco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Quarto;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.util.Constants;

public class QuartoDB {

	public static Collection<Comodo> listarTodos(Collection<Long> ids) throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN QUARTO Q where C.COMODOID = Q.COMODOID;";
		Statement psmt = Conexao.prepare();
		
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			ids.add(result.getLong("COMODOID"));
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Sala(descricao, Constants.QUARTO);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(comodo);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public void salvar(Comodo comodo) throws ConexaoException, SQLException, ClassNotFoundException {
		
		ComodoDB db = new ComodoDB();
		long id = db.salvar(comodo);
		
		if (id != 0) {
			
			Conexao.initConnection();
			
			String sql = "INSERT INTO QUARTO (COMODOID) VALUES(" + id + ");";
			
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
	
public void remover(long id) throws ConexaoException, SQLException, ClassNotFoundException, NoRemoveException {
		
		ComodoDB db = new ComodoDB();
		
		if (db.remover(id)) {
			Conexao.initConnection();
			
			String sql = "DELETE from QUARTO where COMODOID = " + id;
			
			Statement psmt = Conexao.prepare();
			
			int linhasAfetadas = psmt.executeUpdate(sql);
		
			if (linhasAfetadas == 0) {
				Conexao.rollBack();
				Conexao.closeConnection();
				throw new ConexaoException();
			}else{
				Conexao.commit();
				Conexao.closeConnection();
			}
		}else{
			throw new NoRemoveException();
		}
	}

}
