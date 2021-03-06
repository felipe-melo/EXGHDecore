package im.compIII.exghdecore.banco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Cozinha;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.util.Constants;

public class CozinhaDB {

	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN COZINHA CO where C.COMODOID = CO.COMODOID;";
		
		Statement stm = Conexao.prepare();
		
		ResultSet result = stm.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Cozinha(result.getLong("COMODOID"), descricao, Constants.COZINHA);
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
	
	public final static Comodo buscar(long id) throws SQLException, ClassNotFoundException {
		Comodo comodo = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		Collection<Mobilia> mobilias = new ArrayList<Mobilia>();
		
		String sql = "SELECT * FROM COMODO C JOIN COZINHA CO where C.COMODOID = CO.COMODOID and C.COMODOID = " + id + "";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			
			try {
				comodo = new Cozinha(id, descricao, Constants.COZINHA);
			} catch (CampoVazioException e) {}
		}
		
		if (comodo == null) return comodo;
		
		sql = "SELECT * FROM COMODO C JOIN COMODO_MOBILIA CM where C.COMODOID = CM.COMODOID and C.COMODOID = " + id + "";
		
		psmt = Conexao.prepare();
		result = psmt.executeQuery(sql);
		
		while(result.next()) {
			try {
				Mobilia mobilia = MobiliaDB.buscar(result.getLong("MOBILIAID"));
				mobilias.add(mobilia);
			} catch (ConexaoException e) {}
		}
		comodo.setMobilias(mobilias);
		
		Conexao.closeConnection();
		return comodo;
	}
	
	public final static Comodo buscarSimples(long id) throws SQLException, ClassNotFoundException {
		Comodo comodo = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN COZINHA CO where C.COMODOID = CO.COMODOID and C.COMODOID = " + id + "";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			
			try {
				comodo = new Cozinha(id, descricao, Constants.COZINHA);
			} catch (CampoVazioException e) {
				e.printStackTrace();
			}
		}
		
		Conexao.closeConnection();
		return comodo;
	}
	
	public void remover(long id) throws ConexaoException, SQLException, ClassNotFoundException, NoRemoveException {
		
		ComodoDB db = new ComodoDB();
		
		if (!db.remover(id, "COZINHA")) {
			throw new NoRemoveException();
		}
	}
}
