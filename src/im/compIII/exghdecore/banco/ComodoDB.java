package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.entidades.ComodoComposto;
import im.compIII.exghdecore.entidades.Cozinha;
import im.compIII.exghdecore.entidades.Quarto;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.util.Constants;

public class ComodoDB {
	
	final long salvar(Comodo comodo) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		long id;
		
		String sql = "INSERT INTO comodo (DESCRICAO) VALUES(?);";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, comodo.getDescricao());
		
		int linhasAfetadas = psmt.executeUpdate();
		
		if (linhasAfetadas == 0) {
			throw new ConexaoException();
		}
		
		ResultSet generatedKeys = psmt.getGeneratedKeys();
        
		if (generatedKeys.next()) {
            id = generatedKeys.getLong(1);
            Conexao.commit();
            Conexao.closeConnection();
        } else {
        	Conexao.closeConnection();
            throw new SQLException();
        }
		return id;
	}
	
	final boolean remover(long id) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO_COMPOSTO WHERE COMODOID = " + id + ");";
		
		Statement smt = Conexao.prepare();
		
		ResultSet result = smt.executeQuery(sql);
		
		boolean b1 = result.next();
		
		sql = "SELECT * FROM COMODO_MOBILIA WHERE COMODOID = " + id + ");";
		
		smt = Conexao.prepare();
		
		result = smt.executeQuery(sql);
		
		boolean b2 = result.next();
		
		if (!b1 && !b2) {
			sql = "DELETE from COMODO where COMODOID = " + id;
			
			smt = Conexao.prepare();
			
			int linhasAfetadas = smt.executeUpdate(sql);
		
			if (linhasAfetadas == 0) {
				Conexao.rollBack();
				Conexao.closeConnection();
				throw new ConexaoException();
			}else{
				Conexao.commit();
				Conexao.closeConnection();
			}
		}
           
		Conexao.closeConnection();
		
		return !b1 && !b2;
		
	}
	
	public final void atualizar(long id, Comodo comodo) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE COMODO SET DESCRICAO = ? WHERE COMODOID = " + id + ";";
		
		PreparedStatement psmt = Conexao.prepare(sql);
		
		psmt.setString(1, comodo.getDescricao());
		
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
	
	public static Collection<Comodo> listarTodos(Collection<Long> ids) throws SQLException, ClassNotFoundException {
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		list.addAll(SalaDB.listarTodos(ids));
		list.addAll(QuartoDB.listarTodos(ids));
		list.addAll(CozinhaDB.listarTodos(ids));
		list.addAll(ComodoCompostoDB.listarTodos(ids));
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
				Conexao.closeConnection();
				return comodo;
			} catch (CampoVazioException e) {}
		}
		
		return comodo;
	}
}