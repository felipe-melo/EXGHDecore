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

public class Mobilia {
	
	private long mobiliaID;
	private String descricao;
	private double custo;
	private int tempoEntrega;
	private Collection<Comodo> comodos;
	private String[] ids;
	
	public Mobilia(long id, String descricao, double custo, int tempoEntrega, String... comodosId) throws CampoVazioException {
		this.mobiliaID = id;
		this.descricao = descricao;
		this.custo = custo;
		this.tempoEntrega = tempoEntrega;
		this.ids = comodosId;
		
		if (descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.custo == 0 || this.custo < 0)
			throw new CampoVazioException("custo");
		if (this.tempoEntrega == 0 || this.tempoEntrega < 0)
			throw new CampoVazioException("tempo de entrega");
	}
	
	public Mobilia(String descricao, double custo, int tempoEntrega, String... comodosId) throws CampoVazioException {
		this.descricao = descricao;
		this.custo = custo;
		this.tempoEntrega = tempoEntrega;
		this.ids = comodosId;
		
		if (descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.custo == 0 || this.custo < 0)
			throw new CampoVazioException("custo");
		if (this.tempoEntrega == 0 || this.tempoEntrega < 0)
			throw new CampoVazioException("tempo de entrega");
	}
	
	public String getDescricao() {
		return descricao;
	}
	public double getCusto() {
		return custo;
	}
	public int getTempoEntrega() {
		return tempoEntrega;
	}
	public long getMobiliaID() {
		return mobiliaID;
	}
	public void setMobiliaID(long mobiliaID) {
		this.mobiliaID = mobiliaID;
	}
	
	public final void salvar() throws ConexaoException, SQLException, ClassNotFoundException, NumberFormatException, CampoVazioException,
				RelacaoException {
		Conexao.initConnection();
		
		String sql = "INSERT INTO mobilia (DESCRICAO, CUSTO, TEMPO_ENTREGA) VALUES(?, ?, ?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, this.getDescricao());
		psmt.setDouble(2, this.getCusto());
		psmt.setInt(3, this.getTempoEntrega());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            this.setMobiliaID(generatedKeys.getLong(1));
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
		
		if (this.ids == null || this.ids.length == 0) {
			Conexao.rollBack();
			Conexao.closeConnection();
			throw new RelacaoException("Comodo");
		}
		
		Conexao.commit();
        Conexao.closeConnection();
		
		for(String id: this.ids) {
			ComodoMobilia cm = new ComodoMobilia(Integer.valueOf(id), this.getMobiliaID());
			cm.salvar();
		}
	}
	
	public static Collection<Mobilia> listarTodos() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM MOBILIA;";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		ArrayList<Mobilia> list = new ArrayList<Mobilia>();
		
		while(result.next()) {
			
			long id = result.getInt("MOBILIAID");
			String descricao = result.getString("DESCRICAO");
			double custo = result.getDouble("CUSTO");
			int tempoEntrega = result.getInt("TEMPO_ENTREGA");
			
			Mobilia mobilia;
			try {
				mobilia = new Mobilia(descricao, custo, tempoEntrega);
			} catch (CampoVazioException e) {
				e.printStackTrace();
				continue;
			}
			mobilia.setMobiliaID(id);
			list.add(mobilia);
		}
		
		Conexao.closeConnection();
		return list;
	}
	
	public final static Mobilia buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Mobilia mobilia = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		String sql = "SELECT * FROM MOBILIA M where M.MOBILIAID = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			double custo = result.getDouble("CUSTO");
			int tempoEntrega = result.getInt("TEMPO_ENTREGA");
			try {
				mobilia = new Mobilia(descricao, custo, tempoEntrega);
				mobilia.setMobiliaID(id);
				Conexao.closeConnection();
				return mobilia;
			} catch (CampoVazioException e) {}
		}
		return mobilia;
	}
	
	public final void atualizar() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE MOBILIA SET DESCRICAO = ?, CUSTO = ?, TEMPO_ENTREGA = ? WHERE MOBILIAID = " + this.getMobiliaID() + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, this.getDescricao());
		psmt.setDouble(2, this.getCusto());
		psmt.setInt(3, this.getTempoEntrega());
		
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
