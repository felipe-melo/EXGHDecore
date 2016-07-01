package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class MobiliaDB {
	
	public final long salvar(Mobilia mobilia, String... comodosId) 
			throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException, RelacaoException {
		Conexao.initConnection();
		
		long id;
		
		String sql = "INSERT INTO mobilia (DESCRICAO, CUSTO, TEMPO_ENTREGA) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, mobilia.getDescricao());
		psmt.setDouble(2, mobilia.getCusto());
		psmt.setInt(3, mobilia.getTempoEntrega());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            id = generatedKeys.getLong(1);
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		if (comodosId == null || comodosId.length == 0) {
			Conexao.rollBack();
			Conexao.closeConnection();
			throw new RelacaoException("Comodo");
		}
		
		Conexao.commit();
        Conexao.closeConnection();
		
		for(String comodo: comodosId) {
			ComodoMobilia cm = new ComodoMobilia(Integer.valueOf(comodo), id);
			cm.salvar();
		}
		return id;
	}
	
	public static Collection<Mobilia> listarTodos() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM MOBILIA;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Mobilia> list = new ArrayList<Mobilia>();
		
		while(result.next()) {
			
			String descricao = result.getString("DESCRICAO");
			double custo = result.getDouble("CUSTO");
			int tempoEntrega = result.getInt("TEMPO_ENTREGA");
			
			Mobilia mobilia;
			try {
				mobilia = new Mobilia(result.getLong("MOBILIAID"), descricao, custo, tempoEntrega);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(mobilia);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final static Mobilia buscar(long id, Collection<Long> comodosIds) throws ConexaoException, SQLException, ClassNotFoundException {
		Mobilia mobilia = null;
		
		Conexao.initConnection();
		
		String sql = "SELECT * FROM MOBILIA M JOIN COMODO_MOBILIA CM where CM.MOBILIAID = M.MOBILIAID AND M.MOBILIAID = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		String descricao = "";
		double custo = 0;
		int tempoEntrega = 0;
		
		while(result.next()) {
			descricao = result.getString("DESCRICAO");
			custo = result.getDouble("CUSTO");
			tempoEntrega = result.getInt("TEMPO_ENTREGA");
			comodosIds.add(result.getLong("COMODOID"));
		}
		try {
			if (!descricao.equals(""))
				mobilia = new Mobilia(id, descricao, custo, tempoEntrega);
		} catch (CampoVazioException e) {}
		Conexao.closeConnection();
		return mobilia;
	}
	
	public final void atualizar(long id, Mobilia mobilia) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE MOBILIA SET DESCRICAO = ?, CUSTO = ?, TEMPO_ENTREGA = ? WHERE MOBILIAID = " + id + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, mobilia.getDescricao());
		psmt.setDouble(2, mobilia.getCusto());
		psmt.setInt(3, mobilia.getTempoEntrega());
		
		int linhasAfetadas = psmt.executeUpdate();
		
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
