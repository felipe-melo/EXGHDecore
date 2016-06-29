package im.compIII.exghdecore.entidades;

import java.util.List;
import im.compIII.exghdecore.exceptions.CampoVazioException;

public abstract class Comodo {
	
	private String descricao;
	private int tipo;
	
	public Comodo(String descricao, int tipo) throws CampoVazioException {
		this.descricao = descricao;
		this.tipo = tipo;
		if (this.descricao.equals(""))
			throw new CampoVazioException("descrição");
		if (this.tipo == 0)
			throw new CampoVazioException("tipo");
	}
	
	abstract public List<Mobilia> listaMobiliaDisponivel();
	
	public String getDescricao() {
		return descricao;
	}
	
	public int getTipo() {
		return tipo;
	}

}
