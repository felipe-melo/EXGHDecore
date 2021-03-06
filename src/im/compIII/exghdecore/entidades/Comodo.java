package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import im.compIII.exghdecore.banco.ComodoDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public abstract class Comodo {
	
	protected long id;
	private String descricao;
	private int tipo;
	
	private Collection<Mobilia> mobilias;
	
	public Comodo(String descricao, int tipo) throws CampoVazioException {
		this.descricao = descricao;
		this.tipo = tipo;
		if (this.descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.tipo == 0)
			throw new CampoVazioException("tipo");
	}
	
	public Comodo(long id, String descricao, int tipo) throws CampoVazioException {
		this.id = id;
		this.descricao = descricao;
		this.tipo = tipo;
		if (this.descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.tipo == 0)
			throw new CampoVazioException("tipo");
	}
	
	public void setMobilias(Collection<Mobilia> mobilias) {
		this.mobilias = mobilias;
	}
	
	public Collection<Mobilia> getMobilias() {
		return this.mobilias;
	}
	
	abstract public List<Mobilia> listaMobiliaDisponivel();
	
	abstract public void adicionar() throws ClassNotFoundException, ConexaoException, SQLException, RelacaoException;
	
	public String getDescricao() {
		return descricao;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public long getId() {
		return id;
	}
	
	public void atualizar() throws ClassNotFoundException, SQLException, ConexaoException {
		ComodoDB db = new ComodoDB();
		db.atualizar(this);
	}
	
	public static Comodo buscar(long id) throws ClassNotFoundException, SQLException {
		return ComodoDB.buscar(id);
	}
	
	public static Comodo buscarSimples(long id) throws ClassNotFoundException, SQLException {
		return ComodoDB.buscarSimples(id);
	}
	
	public static Collection<Comodo> buscarTodos() throws ClassNotFoundException, SQLException {
		return ComodoDB.listarTodos();
	}
}
