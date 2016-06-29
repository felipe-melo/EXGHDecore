package im.compIII.exghdecore.entidades;

import im.compIII.exghdecore.exceptions.CampoVazioException;

public class ItemVenda {
	
	private int quantidade;
	private Mobilia mobilia;
	
	public ItemVenda(int quantidade, Mobilia mobilia) throws CampoVazioException {
		this.quantidade = quantidade;
		this.mobilia = mobilia;
		if (this.quantidade == 0 || this.quantidade < 0)
			throw new CampoVazioException("quantidade");
	}
	
	public int getQuantidade() {
		return this.quantidade;
	}
	
	public Mobilia getMobilia() {
		return this.mobilia;
	}
}
