package im.compIII.exghdecore.entidades;

import java.util.Collection;

import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class Contrato {
	
	private float comissao;
	private Collection<Ambiente> ambientes;
	
	public Contrato(float comissao, Collection<Ambiente> ambientes) throws CampoVazioException, RelacaoException {
		this.comissao = comissao;
		this.ambientes = ambientes;
		if (comissao == 0 || comissao < 0)
			throw new CampoVazioException("Comissão");
		if (ambientes == null || ambientes.size() == 0)
			throw new RelacaoException("Ambiente");
	}
	
	public float getComissao() {
		return comissao;
	}
	
	public Collection<Ambiente> getAmbientes() {
		return ambientes;
	}
	
	public float valorContrato() {
		return 0f;
	}
	
	public int prazo() {
		return 0;
	}
}
