package im.compIII.exghdecore.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.banco.Conexao;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;

public class Ambiente {
	
	private long ambienteID;
	private int numParedes;
	private int numPortas;
	private float metragem;
	private String[] ambientesId;
	
	public Ambiente(int numParedes, int numPortas, float metragem, String... ambientesId) throws CampoVazioException {
		this.numParedes = numParedes;
		this.numPortas = numPortas;
		this.metragem = metragem;
		this.ambientesId = ambientesId;
		if (this.numParedes == 0 || this.numParedes < 0) {
			throw new CampoVazioException("número de paredes");
		}
		if (this.numPortas == 0 || this.numPortas < 0) {
			throw new CampoVazioException("número de portas");
		}
		if (this.metragem == 0 || this.metragem < 0) {
			throw new CampoVazioException("metragem");
		}
	}
	
	public float custo() {
		return 0f;
	}
	
	public int tempoEntrega() {
		return 0;
	}

	public int getNumParedes() {
		return numParedes;
	}

	public void setNumParedes(int numParedes) {
		this.numParedes = numParedes;
	}

	public int getNumPortas() {
		return numPortas;
	}

	public void setNumPortas(int numPortas) {
		this.numPortas = numPortas;
	}

	public float getMetragem() {
		return metragem;
	}

	public void setMetragem(float metragem) {
		this.metragem = metragem;
	}

	public long getAmbienteID() {
		return ambienteID;
	}

	public void setAmbienteID(long ambienteID) {
		this.ambienteID = ambienteID;
	}
	
	public static Collection<Ambiente> listarTodos() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Ambiente> list = new ArrayList<Ambiente>();
		
		while(result.next()) {
			
			long id = result.getInt("AMBIENTEID");
			int numPortas = result.getInt("NUMERO_PORTAS");
			int numParedes = result.getInt("NUMERO_PAREDES");
			float metragem = result.getFloat("METRAGEM");
			
			Ambiente ambiente;
			try {
				ambiente = new Ambiente(numParedes, numPortas, metragem);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			ambiente.setAmbienteID(id);
			list.add(ambiente);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final void salvar() throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException {
		Conexao.initConnection();
		
		String sql = "INSERT INTO AMBIENTE (NUMERO_PAREDES, NUMERO_PORTAS, METRAGEM) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setInt(1, this.getNumParedes());
		psmt.setInt(2, this.getNumPortas());
		psmt.setFloat(3, this.getMetragem());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            this.setAmbienteID(generatedKeys.getLong(1));
            Conexao.closeConnection();
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		/*for(String id: this.ids) {
			ComodoAmbiente cm = new ComodoAmbiente(Integer.valueOf(id), this.getAmbienteID());
			cm.salvar();
		}*/
	}

}
