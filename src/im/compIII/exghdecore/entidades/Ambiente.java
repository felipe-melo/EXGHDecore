package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.Collection;

import im.compIII.exghdecore.banco.AmbienteDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class Ambiente {
	
	private long id;
	private int numParedes;
	private int numPortas;
	private float metragem;
	private Collection<ItemVenda> itensVenda;
	private Contrato contrato;
	
	public Ambiente(long id, int numParedes, int numPortas, float metragem) throws CampoVazioException {
		this.numParedes = numParedes;
		this.numPortas = numPortas;
		this.metragem = metragem;
		this.id = id;
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
	
	public Ambiente(int numParedes, int numPortas, float metragem) throws CampoVazioException {
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
	
	public void atualizar(String[] mobiliasIds, int[] quantidades) throws ClassNotFoundException, ConexaoException, SQLException {
		AmbienteDB db = new AmbienteDB();
		db.atualizar(this, mobiliasIds, quantidades);
	}
	
	public void adicionar(String[] mobiliasIds, int[] quantidades) throws NumberFormatException, ClassNotFoundException, ConexaoException, SQLException, CampoVazioException, RelacaoException {
		AmbienteDB db = new AmbienteDB();
		db.salvar(this, mobiliasIds, quantidades);
	}
	
	public void adicionarNovo(long contratoId, String[] mobiliasIds, int[] quantidades) throws NumberFormatException, ClassNotFoundException, ConexaoException, SQLException, CampoVazioException, RelacaoException {
		AmbienteDB db = new AmbienteDB();
		db.salvarNovo(this, contratoId, mobiliasIds, quantidades);
	}
	
	public static Ambiente buscar(long id) throws ClassNotFoundException, SQLException, ConexaoException {
		return AmbienteDB.buscar(id);
	}
	
	public static Collection<Ambiente> buscarTodos() throws ClassNotFoundException, SQLException, ConexaoException {
		return AmbienteDB.listarTodos();
	}
	
	public static void remover(long id) throws ClassNotFoundException, SQLException, ConexaoException, NoRemoveException {
		AmbienteDB.remover(id);
	}
	
	public void setItensVenda(Collection<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}
	
	public Collection<ItemVenda> getItensVenda() {
		return this.itensVenda;
	}

	public int getNumParedes() {
		return numParedes;
	}

	public int getNumPortas() {
		return numPortas;
	}

	public float getMetragem() {
		return metragem;
	}
	
	public long getId() {
		return id;
	}
	
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	
	public Contrato getContrato() {
		return this.contrato;
	}
	
	public float custo() {
		float custo = 0f;
		for (ItemVenda itemVenda: this.itensVenda) {
			custo += itemVenda.getMobilia().getCusto() + 10*numParedes + 5*numPortas + 2.5 * metragem;
		}
		return custo;
	}
	
	public int tempoEntrega() {
		int maior = 0;
		for (ItemVenda itemVenda: this.itensVenda) {
			if (itemVenda.getMobilia().getTempoEntrega() > maior)
				maior = itemVenda.getMobilia().getTempoEntrega();
		}
		return maior + 2 * numParedes + numPortas/2;
	}
}