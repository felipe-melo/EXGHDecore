package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class AmbienteDB {
	
	public static Collection<Ambiente> listarTodos(Collection<Long> ids) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Ambiente> list = new ArrayList<Ambiente>();
		
		while(result.next()) {
			
			ids.add(result.getLong("AMBIENTEID"));
			int numPortas = result.getInt("NUMERO_PORTAS");
			int numParedes = result.getInt("NUMERO_PAREDES");
			float metragem = result.getFloat("METRAGEM");
			
			Ambiente ambiente;
			try {
				ambiente = new Ambiente(numParedes, numPortas, metragem, new ArrayList<ItemVenda>());
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(ambiente);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public static Collection<Ambiente> listarTodosSemContrato(Collection<Long> ids) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE WHERE CONTRATOID IS NULL;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Ambiente> list = new ArrayList<Ambiente>();
		
		while(result.next()) {
			
			ids.add(result.getLong("AMBIENTEID"));
			int numPortas = result.getInt("NUMERO_PORTAS");
			int numParedes = result.getInt("NUMERO_PAREDES");
			float metragem = result.getFloat("METRAGEM");
			
			Ambiente ambiente;
			try {
				ambiente = new Ambiente(numParedes, numPortas, metragem, new ArrayList<ItemVenda>());
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(ambiente);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final long salvar(Ambiente ambiente, String[] mobiliasId, int[] quantidades) throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
	RelacaoException {
		Conexao.initConnection();
		
		long id;
		
		String sql = "INSERT INTO AMBIENTE (NUMERO_PAREDES, NUMERO_PORTAS, METRAGEM) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setInt(1, ambiente.getNumParedes());
		psmt.setInt(2, ambiente.getNumPortas());
		psmt.setFloat(3, ambiente.getMetragem());
		
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
		
		if (mobiliasId == null || mobiliasId.length == 0) {
			Conexao.rollBack();
			Conexao.closeConnection();
			throw new RelacaoException("Comodo");
		}
		
		Conexao.commit();
        Conexao.closeConnection();
		
		int i = 0;
		for(String mobilia: mobiliasId) {
			Mobilia mob = MobiliaDB.buscar(Long.valueOf(mobilia));
			ItemVenda itemVenda = new ItemVenda(quantidades[i], mob);
			ItemVendaDB db = new ItemVendaDB();
			db.salvar(itemVenda, Long.valueOf(mobilia), id);
			i++;
		}
		return id;
	}
	
	public final static Ambiente buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Ambiente ambiente = null;
		
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE A JOIN ITEM_VENDA IV where A.AMBIENTEID = IV.AMBIENTEID AND A.AMBIENTEID = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<ItemVenda> itensVenda = new ArrayList<ItemVenda>();
		
		int numParedes = 0;
		int numPortas = 0;
		float metragem = 0f;
		
		if(result.next()) {
			numParedes = result.getInt("NUMERO_PAREDES");
			numPortas = result.getInt("NUMERO_PORTAS");
			metragem = result.getFloat("METRAGEM");
			int quantidade = result.getInt("QUANTIDADE");
			long mobiliaId = result.getInt("MOBILIAID");
			
			try {
				Mobilia mobilia = MobiliaDB.buscar(mobiliaId);
				ItemVenda itemVenda = new ItemVenda(quantidade, mobilia);
				itensVenda.add(itemVenda);
			} catch (CampoVazioException e) {}
		}
		try {
			ambiente = new Ambiente(numParedes, numPortas, metragem, itensVenda);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
		return ambiente;
	}
	
	public final void atualizar(long id, Ambiente ambiente) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE AMBIENTE SET NUMERO_PAREDES = ?, NUMERO_PORTAS = ?, METRAGEM = ? WHERE AMBIENTEID = " + id + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setInt(1, ambiente.getNumParedes());
		psmt.setInt(2, ambiente.getNumPortas());
		psmt.setFloat(3, ambiente.getMetragem());
		
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
	
	public final void addContrato(long id, long contrato) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE AMBIENTE SET CONTRATOID = ? WHERE AMBIENTEID = " + id + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setLong(1, contrato);
		
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