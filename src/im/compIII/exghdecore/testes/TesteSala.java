package im.compIII.exghdecore.testes;

import org.junit.Test;

import im.compIII.exghdecore.entidades.Sala;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.util.Constants;

public class TesteSala {
	
	@Test(expected = CampoVazioException.class)
	public void Criacao() throws CampoVazioException {
		new Sala("", Constants.SALA);
	}
	
	/*@Test
	public void testSub() {
		double result = new Calculator().sub(1, 3);
		Assert.assertEquals(-2, result, 0);
	}
	
	@Test(expected = DivisionByZeroException.class)
	public void testDiv() throws DivisionByZeroException {
		new Calculator().div(1, 0);
	}*/

}
