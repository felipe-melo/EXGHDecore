package im.compIII.exghdecore.testes.funcional;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collection;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;

import im.compIII.exghdecore.banco.ContratoDB;
import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.testes.mock.AmbienteMock;

public class TesteContratoDB extends DatabaseTestCase{

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
	
	private Collection<Ambiente> ambientes;
	
	@Before
	public void setUp() {
		ambientes = new ArrayList<Ambiente>();
	}
	
	@Test
	public void testaCadastrarAmbient() throws Exception{
		AmbienteMock mock = new AmbienteMock();
		ambientes.add(mock);
		Contrato contrato = new Contrato(15);
		contrato.setAmbientes(ambientes);
		ContratoDB codb = new ContratoDB();
		
		String ambientesId []= {"1"};
		
		//long id = codb.salvar(contrato, ambientesId);
		
		//IDataSet dsAtual = getConnection().createDataSet();
		//ITable tabelaAtual = dsAtual.getTable("Contrato");

		assertNotSame(0, 0);
	}
}