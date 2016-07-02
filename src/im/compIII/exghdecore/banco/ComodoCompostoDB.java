package im.compIII.exghdecore.banco;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.ComodoComposto;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;
import im.compIII.exghdecore.util.Constants;

public class ComodoCompostoDB {
	
	public void salvar(ComodoComposto comodoComposto) throws ConexaoException, SQLException, ClassNotFoundException, RelacaoException {
		
		Collection<Comodo> componentes = comodoComposto.getComodos();
		
		if (componentes != null && componentes.size() > 0) {
		
			ComodoDB db = new ComodoDB();
			long id = db.salvar(comodoComposto);
			
			if (id != 0) {
				
				Conexao.initConnection();
				
				for (Comodo comodo: componentes) {
					String sql = "INSERT INTO COMODO_COMPOSTO (COMODOID, COMPONENTEID) VALUES(" + id + ", " + comodo.getId() + ");";
					
					Statement stm = Conexao.prepare();
					
					stm.executeUpdate(sql);
				}
				Conexao.commit();
				Conexao.closeConnection();
			}
		}else{
			throw new RelacaoException("Comodo");
		}
	}
	
	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT DISTINCT C.COMODOID, C.DESCRICAO FROM COMODO C JOIN COMODO_COMPOSTO CC where C.COMODOID = CC.COMODOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new ComodoComposto(result.getLong("COMODOID"), descricao, Constants.COMPOSTO);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(comodo);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final static Comodo buscar(long id) throws SQLException, ClassNotFoundException {
		ComodoComposto comodo = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		Collection<Comodo> componentes = new ArrayList<Comodo>();
		
		String sql = "SELECT * FROM COMODO C JOIN COMODO_COMPOSTO CC where C.COMODOID = CC.COMODOID and C.COMODOID = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		while(result.next()) {
			String descricao = result.getString("DESCRICAO");
			try {
				if (comodo == null)
					comodo = new ComodoComposto(id, descricao, Constants.SALA);
				Comodo componente = Comodo.buscar(result.getLong("COMPONENTEID"));
				componentes.add(componente);
			} catch (CampoVazioException e) {}
		}
		if (comodo != null) {
			comodo.setComodos(componentes);
		}
		Conexao.closeConnection();
		return comodo;
	}
}