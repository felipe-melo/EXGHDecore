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
import im.compIII.exghdecore.exceptions.RelacaoException;

public class Ambiente {
	
	private long ambienteID;
	private int numParedes;
	private int numPortas;
	private float metragem;
	private String[] comodosId;
	private String[] quantidades;
	
	public Ambiente(int numParedes, int numPortas, float metragem, String[] comodosId, String[] quantidades) throws CampoVazioException {
		this.numParedes = numParedes;
		this.numPortas = numPortas;
		this.metragem = metragem;
		this.comodosId = comodosId;
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
	
	private Ambiente(int numParedes, int numPortas, float metragem) throws CampoVazioException {
		this.numParedes = numParedes;
		this.numPortas = numPortas;
		this.metragem = metragem;
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
	
	public final void salvar() throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
	RelacaoException {
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
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		if (this.comodosId == null || this.comodosId.length == 0) {
			Conexao.rollBack();
			Conexao.closeConnection();
			throw new RelacaoException("Comodo");
		}
		
		Conexao.commit();
        Conexao.closeConnection();
		
		int i = 0;
		for(String id: this.comodosId) {
			ItemVenda itemVenda = new ItemVenda(Integer.valueOf(this.quantidades[i]), Integer.valueOf(id), this.getAmbienteID());
			itemVenda.salvar();
			i++;
		}
	}
	
	public final static Ambiente buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Ambiente ambiente = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		String sql = "SELECT * FROM AMBIENTE A where A.AMBIENTE = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		if(result.next()) {
			int numParedes = result.getInt("NUMERO_PAREDES");
			int numPortas = result.getInt("NUMERO_PORTAS");
			float metragem = result.getFloat("METRAGEM");
			try {
				ambiente = new Ambiente(numParedes, numPortas, metragem);
				ambiente.setAmbienteID(id);
				Conexao.closeConnection();
				return ambiente;
			} catch (CampoVazioException e) {}
		}
		return ambiente;
	}
}