package im.compIII.exghdecore.entidades;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import im.compIII.exghdecore.banco.ComodoCompostoDB;
import im.compIII.exghdecore.exceptions.CampoVazioException;
import im.compIII.exghdecore.exceptions.ConexaoException;
import im.compIII.exghdecore.exceptions.RelacaoException;

public class ComodoComposto extends Comodo {
	
	private Collection<Comodo> comodos;
	
	public ComodoComposto(String descricao, int tipo) throws CampoVazioException {
		super(descricao, tipo);
	}
	
	public ComodoComposto(long id, String descricao, int tipo) throws CampoVazioException {
		super(id, descricao, tipo);
	}
	
	public void setComodos(Collection<Comodo> comodos) {
		this.comodos = comodos;
	}
	
	public Collection<Comodo> getComodos() {
		return this.comodos;
	}
	
	@Override
	public List<Mobilia> listaMobiliaDisponivel() {
		ArrayList<Mobilia> mobilias = new ArrayList<Mobilia>();
		if (comodos != null) {
			for (Comodo comodo: comodos) {
				mobilias.addAll(comodo.listaMobiliaDisponivel());
			}
		}
		return mobilias;
	}
	
	@Override
	public void adicionar() throws ClassNotFoundException, ConexaoException, SQLException, RelacaoException {
		ComodoCompostoDB db = new ComodoCompostoDB();
		db.salvar(this);
	}
}