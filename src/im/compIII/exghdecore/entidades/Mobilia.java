package im.compIII.exghdecore.entidades;

import im.compIII.exghdecore.exceptions.CampoVazioException;

public class Mobilia {
	
	private long id;
	private String descricao;
	private double custo;
	private int tempoEntrega;
	
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
}
