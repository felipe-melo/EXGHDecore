package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class ItemVendaDB {
	
	public final void salvar(ItemVenda itemVenda, long mobiliaId, long ambienteId) throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
		RelacaoException {
		Conexao.initConnection();
		
		String sql = "INSERT INTO item_venda (QUANTIDADE, MOBILIAID, AMBIENTEID) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setInt(1, itemVenda.getQuantidade());
		psmt.setLong(2, mobiliaId);
		psmt.setLong(3, ambienteId);
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
		
		if (generatedKeys.next()) {
			Conexao.commit();
			Conexao.closeConnection();
		} else {
			Conexao.closeConnection();
			throw new SQLException();
		}
	}
}
