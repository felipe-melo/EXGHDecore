package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class AmbienteDB {
	
	public static Collection<Ambiente> listarTodos() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE A JOIN Contrato C where A.CONTRATOID = C.CONTRATOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Ambiente> list = new ArrayList<Ambiente>();
		
		while(result.next()) {
			
			long ambienteId = result.getLong("AMBIENTEID");
			int numPortas = result.getInt("NUMERO_PORTAS");
			int numParedes = result.getInt("NUMERO_PAREDES");
			float metragem = result.getFloat("METRAGEM");
			float comissao = result.getFloat("COMISSAO");
			
			Ambiente ambiente;
			try {
				ambiente = new Ambiente(ambienteId, numParedes, numPortas, metragem);
				Contrato contrato = new Contrato(comissao);
				ambiente.setContrato(contrato);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			list.add(ambiente);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	/*public static Collection<Ambiente> listarTodosSemContrato(Collection<Long> ids) throws ConexaoException, SQLException, ClassNotFoundException {
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
	}*/
	
	public final long salvar(Ambiente ambiente, String[] mobiliasId, int[] quantidades) throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
	RelacaoException {
		Conexao.initConnection();
		
		long id;
		
		ContratoDB db = new ContratoDB();
		
		id = db.salvar(ambiente.getContrato());
		
		//Ambiente
		
		String sql = "INSERT INTO AMBIENTE (CONTRATOID, NUMERO_PAREDES, NUMERO_PORTAS, METRAGEM) VALUES(?, ?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setLong(1, id);
		psmt.setInt(2, ambiente.getNumParedes());
		psmt.setInt(3, ambiente.getNumPortas());
		psmt.setFloat(4, ambiente.getMetragem());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            id = generatedKeys.getLong(1);
            Conexao.commit();
        } else {
        	Conexao.rollBack();
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		if (mobiliasId != null && mobiliasId.length > 0) {
			for (int i = 0; i < mobiliasId.length; i++) {
				sql = "INSERT INTO ITEM_VENDA (QUANTIDADE, MOBILIAID, AMBIENTEID) VALUES(?, ?, ?);";
				
				psmt = Conexao.prepare(sql);
				
				psmt.setInt(1, quantidades[i]);
				psmt.setLong(2, Long.valueOf(mobiliasId[i]));
				psmt.setLong(3, id);
				
				linhasAfetadas = psmt.executeUpdate();
				
				if (linhasAfetadas == 0) {
					throw new ConexaoException();
				}
				
				generatedKeys = psmt.getGeneratedKeys();
		        
				if (generatedKeys.next()) {
		            Conexao.commit();
		        } else {
		        	Conexao.rollBack();
		        	Conexao.closeConnection();
		            throw new SQLException();
		        }
			}
		}
		
		return id;
	}
	
	public final static Ambiente buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Ambiente ambiente = null;
		
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE A JOIN CONTRATO C where A.AMBIENTEID = " + id + ""
				+ " AND A.CONTRATOID = C.CONTRATOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		Contrato contrato = null;
		
		long contratoId = 0;
		float comissao = 0;
		
		if(result.next()) {
			long ambienteId = result.getInt("AMBIENTEID");
			int numParedes = result.getInt("NUMERO_PAREDES");
			int numPortas = result.getInt("NUMERO_PORTAS");
			float metragem = result.getFloat("METRAGEM");
			contratoId = result.getLong("CONTRATOID");
			comissao = result.getFloat("COMISSAO");
			
			try {
				ambiente = new Ambiente(ambienteId, numParedes, numPortas, metragem);
				contrato = new Contrato(contratoId, comissao);
				ambiente.setContrato(contrato);
				ambiente.setItensVenda(ItemVendaDB.buscar(ambienteId));
			} catch (CampoVazioException e) {
				ambiente.setItensVenda(new ArrayList<ItemVenda>());
				e.printStackTrace();
			} catch (NumberFormatException e) {
				ambiente.setItensVenda(new ArrayList<ItemVenda>());
				e.printStackTrace();
			} catch (RelacaoException e) {
				ambiente.setItensVenda(new ArrayList<ItemVenda>());
				e.printStackTrace();
			}
		}
		Conexao.closeConnection();
		
		return ambiente;
	}
	
	public final void atualizar(Ambiente ambiente, String[] mobiliasIds, int[] quantidades) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE AMBIENTE SET NUMERO_PAREDES = ?, NUMERO_PORTAS = ?, METRAGEM = ? WHERE AMBIENTEID = " + ambiente.getId() + ";";
		
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
		
		ContratoDB db = new ContratoDB();
		
		try {
			db.atualizar(ambiente.getContrato());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RelacaoException e) {
			e.printStackTrace();
		}
	}
}