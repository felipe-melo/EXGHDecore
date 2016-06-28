package im.compIII.exghdecore.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import im.compIII.exghdecore.banco.Conexao;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class ItemVenda {
	
	private long itemVendaId;
	private int quantidade;
	private long mobiliaId;
	private long ambienteId;
	
	public ItemVenda(int quantidade, long mobiliaId, long ambienteId) {
		this.quantidade = quantidade;
		this.mobiliaId = mobiliaId;
		this.ambienteId = ambienteId;
	}
	
	public final void salvar() throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
		RelacaoException {
		Conexao.initConnection();
		
		String sql = "INSERT INTO item_venda (QUANTIDADE, MOBILIAID, AMBIENTEID) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setInt(1, this.quantidade);
		psmt.setLong(2, this.mobiliaId);
		psmt.setLong(3, this.ambienteId);
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
		
		if (generatedKeys.next()) {
		this.itemVendaId = generatedKeys.getLong(1);
			Conexao.commit();
			Conexao.closeConnection();
		} else {
			Conexao.closeConnection();
			throw new SQLException();
		}
	}
}
