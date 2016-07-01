package im.compIII.exghdecore.testes.mock;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.exceptions.CampoVazioException;

public class AmbienteMock extends Ambiente {

	public AmbienteMock(int numParedes, int numPortas, float metragem) throws CampoVazioException {
		super(numParedes, numPortas, metragem);
	}

	public AmbienteMock() throws CampoVazioException {
		super(1, 1, 1);
	}

}
