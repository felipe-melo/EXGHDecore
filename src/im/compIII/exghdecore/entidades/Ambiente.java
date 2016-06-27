package im.compIII.exghdecore.entidades;

public class Ambiente {
	
	private int ambienteID;
	private int numParedes;
	private int numPortas;
	private float metragem;
	
	public float custo() {
		return 0f;
	}
	
	public int tempoEntrega() {
		return 0;
	}

	public int getNumParedes() {
		return numParedes;
	}

	public void setNumParedes(int numParedes) {
		this.numParedes = numParedes;
	}

	public int getNumPortas() {
		return numPortas;
	}

	public void setNumPortas(int numPortas) {
		this.numPortas = numPortas;
	}

	public float getMetragem() {
		return metragem;
	}

	public void setMetragem(float metragem) {
		this.metragem = metragem;
	}

	public int getAmbienteID() {
		return ambienteID;
	}

	public void setAmbienteID(int ambienteID) {
		this.ambienteID = ambienteID;
	}

}
