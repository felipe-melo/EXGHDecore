package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import im.compIII.exghdecore.banco.ComodoDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;

public class Quarto extends Comodo {

	public Quarto(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}
	
	public Quarto(long id, String descricao, int tipo) throws CampoVazioException {
		super(id, descricao, tipo);
	}

	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		Comodo c = null;
		try {
			c = ComodoDB.buscar(this.id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if (c != null)
			return (List<Mobilia>) c.getMobilias();
		else
			return new ArrayList<Mobilia>();
	}
}
