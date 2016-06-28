package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.sql.Statement;

import im.compIII.exghdecore.banco.Conexao;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;

public class ComodoMobilia {
	
	private long comodoId;
	private long mobiliaId;
	
	public ComodoMobilia(long comodoId, long mobiliaId) throws CampoVazioException {
		this.comodoId = comodoId;
		this.mobiliaId = mobiliaId;
		if (this.mobiliaId == 0) {
			throw new CampoVazioException("Mobilia");
		}
		if (this.comodoId == 0) {
			throw new CampoVazioException("Comodo");
		}
	}
	
	public void salvar() throws ConexaoException, SQLException, ClassNotFoundException {
		
		Conexao.initConnection();
				
		String sql = "INSERT INTO COMODO_MOBILIA (COMODOID, MOBILIAID) VALUES(" + this.comodoId + ", " + mobiliaId + ");";
			
		Statement stm = Conexao.prepare();
			
		stm.executeUpdate(sql);
		Conexao.commit();
		Conexao.closeConnection();
	}
}
