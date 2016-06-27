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

public class ComodoComposto extends Comodo {
	
	private Collection<Comodo> comodos;
	private String[] comodosIds;

	public ComodoComposto(String descricao, int tipo, String... comodosIds) throws CampoVazioException {
		super(descricao, tipo);
		comodos = new ArrayList<Comodo>();
		this.comodosIds = comodosIds;
	}
	
	public ComodoComposto(long id, String descricao, int tipo) throws CampoVazioException {
		super(id, descricao, tipo);
		comodos = new ArrayList<Comodo>();
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		return null;
	}
	
	@Override
	public void salvar() throws ConexaoException, SQLException, ClassNotFoundException {
		
		if (this.comodosIds != null) {
		
			this.salvarProcted();
			
			if (this.getComodoID() != 0) {
				
				for (String id: this.comodosIds) {
					this.comodos.add(Comodo.buscar(Integer.valueOf(id)));
				}
				
				Conexao.initConnection();
				
				for (Comodo comodo: this.comodos) {
					String sql = "INSERT INTO COMODO_COMPOSTO (COMODOID, COMPONENTEID) VALUES(" + this.getComodoID() + ", " + comodo.getComodoID() + ");";
					
					Statement stm = Conexao.prepare();
					
					stm.executeUpdate(sql);
				}
				
				Conexao.closeConnection();
			}
		}
	}
	
	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT DISTINCT C.COMODOID, C.DESCRICAO FROM COMODO C JOIN COMODO_COMPOSTO CC where C.COMODOID = CC.COMODOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		
		while(result.next()) {
			
			long id = result.getInt("COMODOID");
			String descricao = result.getString("DESCRICAO");
			
			Comodo comodo;
			try {
				comodo = new ComodoComposto(descricao, Constants.COMPOSTO);
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
}