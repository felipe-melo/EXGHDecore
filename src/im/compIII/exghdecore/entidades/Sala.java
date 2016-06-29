package im.compIII.exghdecore.entidades;

import java.util.List;

import im.compIII.exghdecore.exceptions.CampoVazioException;

public class Sala extends Comodo {

	public Sala(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		return null;
	}
}
