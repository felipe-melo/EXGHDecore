package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.Collection;

import im.compIII.exghdecore.banco.MobiliaDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class Mobilia {
	
	private long id;
	private String descricao;
	private double custo;
	private int tempoEntrega;
	private Collection<Comodo> comodos;
	
	public Mobilia(long id, String descricao, double custo, int tempoEntrega) throws CampoVazioException {
		this.descricao = descricao;
		this.custo = custo;
		this.tempoEntrega = tempoEntrega;
		this.id = id;
		if (descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.custo == 0 || this.custo < 0)
			throw new CampoVazioException("custo");
		if (this.tempoEntrega == 0 || this.tempoEntrega < 0)
			throw new CampoVazioException("tempo de entrega");
	}
	
	public Mobilia(String descricao, double custo, int tempoEntrega) throws CampoVazioException {
		this.descricao = descricao;
		this.custo = custo;
		this.tempoEntrega = tempoEntrega;
		
		if (descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.custo == 0 || this.custo < 0)
			throw new CampoVazioException("custo");
		if (this.tempoEntrega == 0 || this.tempoEntrega < 0)
			throw new CampoVazioException("tempo de entrega");
	}
	
	public static final Mobilia buscar(long id) throws ClassNotFoundException, ConexaoException, SQLException {
		return MobiliaDB.buscar(id);
	}
	
	public static final Collection<Mobilia> buscarTodos() throws ClassNotFoundException, ConexaoException, SQLException {
		return MobiliaDB.listarTodos();
	}
	
	public void adicionar() throws NumberFormatException, ClassNotFoundException, ConexaoException, SQLException, CampoVazioException, RelacaoException{
		MobiliaDB db = new MobiliaDB();
		db.salvar(this);
	}
	
	public void atualizar() throws ClassNotFoundException, ConexaoException, SQLException {
		MobiliaDB db = new MobiliaDB();
		db.atualizar(this);
	}
	
	public String getDescricao() {
		return descricao;
	}
	public long getId() {
		return id;
	}
	public double getCusto() {
		return custo;
	}
	public int getTempoEntrega() {
		return tempoEntrega;
	}

	public Collection<Comodo> getComodos() {
		return comodos;
	}

	public void setComodos(Collection<Comodo> comodos) {
		this.comodos = comodos;
	}
}
