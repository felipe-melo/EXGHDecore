package im.compIII.exghdecore.testes.funcional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import im.compIII.exghdecore.entidades.Ambiente;
import im.compIII.exghdecore.entidades.Contrato;
import im.compIII.exghdecore.entidades.ItemVenda;
import im.compIII.exghdecore.entidades.Mobilia;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class TesteContrato {
	
	private Collection<Ambiente> ambientes;
	private Collection<ItemVenda> itensVenda;
	
	@Before
	public void setUp() {
		ambientes = new ArrayList<Ambiente>();
		itensVenda = new ArrayList<ItemVenda>();
		
		try {
			Ambiente ambiente = new Ambiente(1, 1, 1);
			Mobilia mobilia = new Mobilia("Mobilia1", 1.0, 5);
			ItemVenda itemVenda = new ItemVenda(1, mobilia);
			this.itensVenda.add(itemVenda);
			ambientes.add(ambiente);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void valorContrato() {
		try {
			Contrato contrato = new Contrato(0.1f);
			contrato.setAmbientes(ambientes);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			float v = Float.valueOf(df.format(contrato.valorContrato()));
			Assert.assertEquals(20.35, v, 0);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void AmbienteTempo() {
		try {
			Contrato contrato = new Contrato(0.1f);
			contrato.setAmbientes(ambientes);
			Assert.assertEquals(7, contrato.prazo(), 0);
		} catch (CampoVazioException e) {
			e.printStackTrace();
		}
	}

}
