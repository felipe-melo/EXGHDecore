package im.compIII.exghdecore.testes.funcional;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import org.junit.Assert;

public class TesteAmbiente {
	
	private Collection<ItemVenda> itensVenda;
	
	@Before
	public void setUp() {
		itensVenda = new ArrayList<ItemVenda>();
		
		Mobilia mobilia;
		try {
			mobilia = new Mobilia("Mobilia1", 1.0, 1);
			ItemVenda itemVenda = new ItemVenda(1, mobilia);
			this.itensVenda.add(itemVenda);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void AmbienteCusto() {
		try {
			Ambiente ambiente = new Ambiente(1, 1, 1, this.itensVenda);
			Assert.assertEquals(18.5, ambiente.custo(), 0);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void AmbienteTempo() {
		try {
			Ambiente ambiente = new Ambiente(1, 1, 1, this.itensVenda);
			Assert.assertEquals(3, ambiente.tempoEntrega(), 0);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}

}
