package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import im.compIII.exghdecore.banco.ComodoDB;
import im.compIII.exghdecore.banco.SalaDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.NoRemoveException;

public class Sala extends Comodo {

	public Sala(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}
	
	public Sala(long id, String descricao, int tipo) throws CampoVazioException {
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
			return (ArrayList<Mobilia>) c.getMobilias();
		else
			return new ArrayList<Mobilia>();
	}

	@Override
	public void adicionar() throws ClassNotFoundException, ConexaoException, SQLException {
		SalaDB db = new SalaDB();
		db.salvar(this);
	}
	
	public static void remover(long id) throws ClassNotFoundException, ConexaoException, SQLException, NoRemoveException {
		SalaDB db = new SalaDB();
		db.remover(id);
	}
}
