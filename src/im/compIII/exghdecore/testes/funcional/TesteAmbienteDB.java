package im.compIII.exghdecore.testes.funcional;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import im.compIII.exghdecore.banco.AmbienteDB;
import im.compIII.exghdecore.entidades.Ambiente;

public class TesteAmbienteDB extends DatabaseTestCase{

	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/exghDecoreDBTeste", "admin", "admin123");
		return new DatabaseConnection(conn);
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSet(
			new FileInputStream("WebContent/META-INF/datasetContrato.xml")
		);
	}
	
	protected DatabaseOperation getSetUpOperation(){
		return DatabaseOperation.CLEAN_INSERT;
	}
	
	protected DatabaseOperation getTearDownOperation(){
		return DatabaseOperation.NONE;
	}
	
	@Test
	public void testaCadastrarAmbient() throws Exception{
		Ambiente amb = new Ambiente(10, 5, 2);
		AmbienteDB ambd = new AmbienteDB();
		String str []= {"1", "2"};
		int quant[] = {1, 2};
		long contratoId = 1;
		//amb.adicionar(contratoId, str, quant);
		
		IDataSet dsAtual = getConnection().createDataSet();
		ITable tabelaAtual = dsAtual.getTable("Ambiente");

		assertEquals(1, tabelaAtual.getRowCount());
	
	}
}
