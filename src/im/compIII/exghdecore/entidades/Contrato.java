package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.Collection;

import im.compIII.exghdecore.banco.ContratoDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class Contrato {
	
	private long id;
	private float comissao;
	private Collection<Ambiente> ambientes;
	
	public Contrato(float comissao) throws CampoVazioException {
		this.comissao = comissao;
		if (comissao == 0 || comissao < 0)
			throw new CampoVazioException("Comissão");
	}
	
	public Contrato(long id, float comissao) throws CampoVazioException {
		this.id = id;
		this.comissao = comissao;
		if (comissao == 0 || comissao < 0)
			throw new CampoVazioException("Comissão");
	}
	
	public float getComissao() {
		return comissao;
	}
	
	public long getId() {
		return this.id;
	}
	
	public Collection<Ambiente> getAmbientes() {
		return this.ambientes;
	}
	
	public void setAmbientes(Collection<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}
	
	public void adicionar() throws NumberFormatException, ClassNotFoundException, ConexaoException, SQLException, RelacaoException {
		ContratoDB db = new ContratoDB();
		db.salvar(this);
	}
	
	public float valorContrato() {
		float valor = 0;
		for (Ambiente ambiente: ambientes) {
			valor += ambiente.custo();
		}
		return valor + (valor * this.comissao);
	}
	
	public int prazo() {
		int tempo = 0;
		for (Ambiente ambiente: ambientes) {
			tempo += ambiente.tempoEntrega();
		}
		return tempo;
	}
}
