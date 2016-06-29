package im.compIII.exghdecore.entidades;

import java.util.Collection;

import im.compIII.exghdecore.exceptions.CampoVazioException;

public class Ambiente {
	
	private int numParedes;
	private int numPortas;
	private float metragem;
	private Collection<ItemVenda> itensVenda;
	
	public Ambiente(int numParedes, int numPortas, float metragem, Collection<ItemVenda> itensVenda) throws CampoVazioException {
		this.numParedes = numParedes;
		this.numPortas = numPortas;
		this.metragem = metragem;
		this.itensVenda = itensVenda;
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

	public int getNumParedes() {
		return numParedes;
	}

	public int getNumPortas() {
		return numPortas;
	}

	public float getMetragem() {
		return metragem;
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