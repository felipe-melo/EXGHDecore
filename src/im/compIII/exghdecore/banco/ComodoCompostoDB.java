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
	
	public void salvar(ComodoComposto comodoComposto, String[] comodosId) throws ConexaoException, SQLException, ClassNotFoundException, RelacaoException {
		
		if (comodosId != null && comodosId.length > 0) {
		
			ComodoDB db = new ComodoDB();
			long id = db.salvar(comodoComposto);
			
			if (id != 0) {
				
				Conexao.initConnection();
				
				for (String comodo: comodosId) {
					String sql = "INSERT INTO COMODO_COMPOSTO (COMODOID, COMPONENTEID) VALUES(" + id + ", " + Integer.valueOf(comodo) + ");";
					
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
	
	public static Collection<Comodo> listarTodos(Collection<Long> ids) throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT DISTINCT C.COMODOID, C.DESCRICAO FROM COMODO C JOIN COMODO_COMPOSTO CC where C.COMODOID = CC.COMODOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			ids.add(result.getLong("COMODOID"));
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new ComodoComposto(descricao, Constants.COMPOSTO);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(comodo);
		}
		
		Conexao.closeConnection();
		return list;
	}
}