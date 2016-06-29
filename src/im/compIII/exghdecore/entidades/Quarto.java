package im.compIII.exghdecore.entidades;

import java.util.List;

import im.compIII.exghdecore.exceptions.CampoVazioException;

public class Quarto extends Comodo {

	public Quarto(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		return null;
	}
}
