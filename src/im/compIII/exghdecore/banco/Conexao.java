package im.compIII.exghdecore.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
	
	private static Connection connection;
	protected static PreparedStatement psmt;
	protected static Statement smt;
	
	private Conexao() {}
	
	public static void initConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/exghDecoreDB", "admin", "admin123");
	}
	
	public static PreparedStatement prepare(String sql) throws SQLException {

		psmt =  connection.prepareStatement(sql);
		return psmt;
	}
	
	public static Statement prepare() throws SQLException {

		smt =  connection.createStatement();
		return smt;
	}
	
	public static void closeConnection() throws SQLException {
		if (psmt != null && !psmt.isClosed())
			psmt.close();
		if (smt != null && !smt.isClosed())
			smt.close();
		if(!connection.isClosed())
			connection.close();
	}
}
