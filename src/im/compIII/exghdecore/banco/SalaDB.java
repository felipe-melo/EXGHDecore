package im.compIII.exghdecore.banco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.util.Constants;

public class SalaDB {

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
			
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new Sala(result.getLong("COMODOID"), descricao, Constants.SALA);
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
			
			String sql = "INSERT INTO SALA (COMODOID) VALUES(" + id + ");";
			
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
		
		String sql = "SELECT * FROM COMODO C JOIN SALA S JOIN COMODO_MOBILIA CM where C.COMODOID = S.COMODOID and C.COMODOID = " + id + ""
				+ " AND CM.COMODOID = C.COMODOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		while(result.next()) {
			String descricao = result.getString("DESCRICAO");
			
			try {
				if (comodo == null)
					comodo = new Sala(id, descricao, Constants.SALA);
				Mobilia mobilia = MobiliaDB.buscar(result.getLong("MOBILIAID"), new ArrayList<Long>());
				mobilias.add(mobilia);
			} catch (CampoVazioException | ConexaoException e) {
				e.printStackTrace();
			}
		}
		if (comodo != null) {
			comodo.setMobilias(mobilias);
		}
		Conexao.closeConnection();
		return comodo;
	}
	
	public void remover(long id) throws ConexaoException, SQLException, ClassNotFoundException, NoRemoveException {
		
		ComodoDB db = new ComodoDB();
		
		if (!db.remover(id, "SALA"))
			throw new NoRemoveException();
	}
}
