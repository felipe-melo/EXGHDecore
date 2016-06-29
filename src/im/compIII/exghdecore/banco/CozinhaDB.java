package im.compIII.exghdecore.banco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Cozinha;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.util.Constants;

public class CozinhaDB {

	public static Collection<Comodo> listarTodos(Collection<Long> ids) throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN COZINHA CO where C.COMODOID = CO.COMODOID;";
		
		Statement stm = Conexao.prepare();
		
		ResultSet result = stm.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			ids.add(result.getLong("COMODOID"));
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Sala(descricao, Constants.COZINHA);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(comodo);
		}
		Conexao.commit();
		Conexao.closeConnection();
		return list;
	}
	
	public void salvar(Comodo comodo) throws ConexaoException, SQLException, ClassNotFoundException {
		
		ComodoDB db = new ComodoDB();
		long id = db.salvar(comodo);
		
		if (id != 0) {
			Class.forName("org.h2.Driver");
			Conexao.initConnection();
			
			String sql = "INSERT INTO COZINHA (COMODOID) VALUES(" + id + ");";
			
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
		}
	}
	
	public void remover(long id) throws ConexaoException, SQLException, ClassNotFoundException, NoRemoveException {
		
		ComodoDB db = new ComodoDB();
		
		if (db.remover(id)) {
			Conexao.initConnection();
			
			String sql = "DELETE from COZINHA where COMODOID = " + id;
			
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
