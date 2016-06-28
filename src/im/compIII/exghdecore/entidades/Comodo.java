package im.compIII.exghdecore.entidades;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import im.compIII.exghdecore.banco.Conexao;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;
import im.compIII.exghdecore.util.Constants;

public abstract class Comodo {
	
	private long comodoID;
	private String descricao;
	private int tipo;
	
	public Comodo(String descricao, int tipo) throws CampoVazioException {
		this.descricao = descricao;
		this.tipo = tipo;
		if (this.descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.tipo == 0)
			throw new CampoVazioException("tipo");
	}
	
	public Comodo(long id, String descricao, int tipo) throws CampoVazioException {
		this.comodoID = id;
		this.descricao = descricao;
		this.tipo = tipo;
		if (this.descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.comodoID == 0)
			throw new CampoVazioException("tipo"); //Criar uma exception
		if (this.tipo == 0)
			throw new CampoVazioException("tipo");
	}
	
	abstract public List<Mobilia> listaMobiliaDisponivel();

	public long getComodoID() {
		return comodoID;
	}

	public void setComodoID(long comodoID) {
		this.comodoID = comodoID;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	abstract public void salvar() throws ConexaoException, SQLException, ClassNotFoundException, RelacaoException;
	
	protected final void salvarProcted() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "INSERT INTO comodo (DESCRICAO) VALUES(?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, this.getDescricao());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            this.setComodoID(generatedKeys.getLong(1));
            Conexao.commit();
            Conexao.closeConnection();
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
	}
	
	public final void atualizar() throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE COMODO SET DESCRICAO = ? WHERE COMODOID = " + this.getComodoID() + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, this.getDescricao());
		
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
	
	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		list.addAll(Sala.listarTodos());
		list.addAll(Quarto.listarTodos());
		list.addAll(Cozinha.listarTodos());
		list.addAll(ComodoComposto.listarTodos());
		return list;
	}
	
	public final static Comodo buscar(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Comodo comodo = null;
		
		Class.forName("org.h2.Driver");
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO C JOIN SALA S where C.COMODOID = S.COMODOID and C.COMODOID = " + id + ";";
		
		Statement psmt = Conexao.prepare();
		ResultSet result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			try {
				comodo = new Sala(descricao, Constants.SALA);
				comodo.setComodoID(id);
				Conexao.closeConnection();
				return comodo;
			} catch (CampoVazioException e) {}
		}
		
		sql = "SELECT * FROM COMODO C JOIN QUARTO Q where C.COMODOID = Q.COMODOID and C.COMODOID = " + id + ";";
		
		psmt = Conexao.prepare();
		result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			try {
				comodo = new Quarto(descricao, Constants.SALA);
				comodo.setComodoID(id);
				Conexao.closeConnection();
				return comodo;
			} catch (CampoVazioException e) {}
		}
		
		sql = "SELECT * FROM COMODO C JOIN COZINHA CO where C.COMODOID = CO.COMODOID and C.COMODOID = " + id + ";";
		
		psmt = Conexao.prepare();
		result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			try {
				comodo = new Cozinha(descricao, Constants.SALA);
				comodo.setComodoID(id);
				Conexao.closeConnection();
				return comodo;
			} catch (CampoVazioException e) {}
		}
		
		sql = "SELECT DISTINCT C.COMODOID, C.DESCRICAO FROM COMODO C JOIN COMODO_COMPOSTO CC where C.COMODOID = CC.COMODOID and CC.COMODOID = " + id + ";";
		
		psmt = Conexao.prepare();
		result = psmt.executeQuery(sql);
		
		if(result.next()) {
			String descricao = result.getString("DESCRICAO");
			try {
				comodo = new ComodoComposto(descricao, Constants.COMPOSTO);
				comodo.setComodoID(id);
				Conexao.closeConnection();
				return comodo;
			} catch (CampoVazioException e) {}
		}
		
		return comodo;
	}
}