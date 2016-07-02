package im.compIII.exghdecore.banco;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import im.compIII.exghdecore.entidades.Comodo;
import im.compIII.exghdecore.exceptions.ConexaoException;

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
	
	final boolean remover(long id, String tabela) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "SELECT * FROM COMODO_COMPOSTO WHERE COMODOID = " + id + ";";
		
		Statement smt = Conexao.prepare();
		
		ResultSet result = smt.executeQuery(sql);
		
		boolean b1 = result.next();
		
		sql = "SELECT * FROM COMODO_MOBILIA WHERE COMODOID = " + id + ";";
		
		smt = Conexao.prepare();
		
		result = smt.executeQuery(sql);
		
		boolean b2 = result.next();
		
		if (!b1 && !b2) {
			sql = "DELETE from " + tabela + " where COMODOID = " + id + ";";
			
			smt = Conexao.prepare();
			
			int linhasAfetadas = smt.executeUpdate(sql);
		
			if (linhasAfetadas == 0) {
				Conexao.rollBack();
				Conexao.closeConnection();
				throw new ConexaoException();
			}else{
				Conexao.commit();
			}
			
			sql = "DELETE from COMODO where COMODOID = " + id + ";";
			
			smt = Conexao.prepare();
			
			linhasAfetadas = smt.executeUpdate(sql);
		
			if (linhasAfetadas == 0) {
				Conexao.rollBack();
				Conexao.closeConnection();
				throw new ConexaoException();
			}else{
				Conexao.commit();
			}
		}
           
		Conexao.closeConnection();
		
		return !b1 && !b2;
	}
	
	public final void atualizar(Comodo comodo) throws ConexaoException, SQLException, ClassNotFoundException {
		Conexao.initConnection();
		
		String sql = "UPDATE COMODO SET DESCRICAO = ? WHERE COMODOID = " + comodo.getId() + ";";
		
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
	
	public static Collection<Comodo> listarTodos() throws SQLException, ClassNotFoundException {
		ArrayList<Comodo> list = new ArrayList<Comodo>();
		list.addAll(SalaDB.listarTodos());
		list.addAll(QuartoDB.listarTodos());
		list.addAll(CozinhaDB.listarTodos());
		list.addAll(ComodoCompostoDB.listarTodos());
		return list;
	}
	
	public final static Comodo buscar(long id) throws SQLException, ClassNotFoundException {
		
		Comodo comodo = null;
		
		comodo = SalaDB.buscar(id);
		
		if (comodo != null)
			return comodo;
		
		comodo = QuartoDB.buscar(id);
		
		if (comodo != null)
			return comodo;
		
		comodo = CozinhaDB.buscar(id);	
		
		if (comodo != null)
			return comodo;
		
		comodo = ComodoCompostoDB.buscar(id);	
		
		return comodo;
	}
	
	public final static Comodo buscarSimples(long id) throws SQLException, ClassNotFoundException {
		
		Comodo comodo = null;
		
		comodo = SalaDB.buscarSimples(id);
		
		if (comodo != null)
			return comodo;
		
		comodo = QuartoDB.buscarSimples(id);
		
		if (comodo != null)
			return comodo;
		
		comodo = CozinhaDB.buscarSimples(id);	
		
		if (comodo != null)
			return comodo;
		
		return comodo;
	}
}