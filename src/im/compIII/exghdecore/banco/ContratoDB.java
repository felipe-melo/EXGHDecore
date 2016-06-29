package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class ContratoDB {
	
	public final long salvar(Contrato contrato, String... ambientesId) 
			throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, RelacaoException {
		Conexao.initConnection();
		
		long id;
		
		String sql = "INSERT INTO contrato (COMISSAO) VALUES(?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setFloat(1, contrato.getComissao());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            id = generatedKeys.getLong(1);
        } else {
        	Conexao.rollBack();
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		if (ambientesId == null || ambientesId.length == 0) {
			Conexao.rollBack();
			Conexao.closeConnection();
			throw new RelacaoException("Ambiente");
		}
		
		Conexao.commit();
        Conexao.closeConnection();
		
		AmbienteDB db = new AmbienteDB();
		
		for (String ambiente: ambientesId) {
			db.addContrato(Long.valueOf(ambiente), id);
		}
		return id;
	}
	
	public static Collection<Contrato> listarTodos(Collection<Long> ids) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM CONTRATO C JOIN AMBIENTE A where C.CONTRATOID = A.CONTRATOID order by C.CONTRATOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Contrato> list = new ArrayList<Contrato>();
		
		Contrato contrato = null;
		
		while(result.next()) {
			
			float comissao = result.getFloat("COMISSAO");
			
			long ambienteId = result.getLong("AMBIENTEID");
			Ambiente ambiente = AmbienteDB.buscar(ambienteId);
			
			if (!ids.contains(result.getLong("CONTRATOID"))) {
				ids.add(result.getLong("CONTRATOID"));
				
				try {
					ArrayList<Ambiente> ambientes = new ArrayList<Ambiente>();
					ambientes.add(ambiente);
					contrato = new Contrato(comissao, ambientes);
				} catch (CampoVazioException e) {
					e.printStackTrace();
					continue;
				} catch (RelacaoException re) {
					re.printStackTrace();
					continue;
				}
				list.add(contrato);
			}else if (contrato != null){
				contrato.getAmbientes().add(ambiente);
			}
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final static Contrato buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM CONTRATO C JOIN AMBIENTE A where C.CONTRATOID = A.CONTRATOID AND C.CONTRATOID = " + id + " order by C.CONTRATOID;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Contrato> list = new ArrayList<Contrato>();
		
		Contrato contrato = null;
		ArrayList<Ambiente> ambientes = new ArrayList<Ambiente>();
		float comissao = 0;
		
		while(result.next()) {
			
			comissao = result.getFloat("COMISSAO");
			
			long ambienteId = result.getLong("AMBIENTEID");
			Ambiente ambiente = AmbienteDB.buscar(ambienteId);
			ambientes.add(ambiente);
			
			list.add(contrato);
		}
		
		try {
			contrato = new Contrato(comissao, ambientes);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		} catch (RelacaoException re) {
			re.printStackTrace();
		}
		
		Conexao.closeConnection();
		return contrato;
	}
}
