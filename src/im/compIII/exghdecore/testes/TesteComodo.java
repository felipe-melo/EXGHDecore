package im.compIII.exghdecore.testes;

import org.junit.Test;

import im.compIII.exghdecore.entidades.Cozinha;
import im.compIII.exghdecore.entidades.Quarto;
import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.util.Constants;

public class TesteComodo {
	
	@Test(expected = CampoVazioException.class)
	public void CriacaoSala() throws CampoVazioException {
		new Sala("", Constants.SALA);
	}
	
	@Test(expected = CampoVazioException.class)
	public void CriacaoQuarto() throws CampoVazioException {
		new Quarto("", Constants.QUARTO);
	}
	
	@Test(expected = CampoVazioException.class)
	public void CriacaoCozinha() throws CampoVazioException {
		new Cozinha("", Constants.COZINHA);
	}

}
