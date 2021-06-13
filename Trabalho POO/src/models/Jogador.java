package models;
import java.util.List;
import java.util.ArrayList;

class Jogador {
	private String id;
	private int fichas;
	private List<List<Carta>> maos = new ArrayList<List<Carta>>();

	Jogador(String id, int fichas) {
		this.id = id;
		this.fichas = fichas;
	}
	
	public String getID() {
		return this.id;
	}
	
	public int getFichas() {
		return this.fichas;
	}
	
	public boolean recebeFichas(int fichas) {
		if (fichas <= 0) {
			return false;
		}
		
		this.fichas += fichas;
		return true;
	}
	
	public boolean apostaFichas(int fichas) {
		if (this.fichas >= fichas) {
			this.fichas -= fichas;
			return true;
		}
		
		return false;
	}
	
	public void compraCarta(Baralho b, int mao) {
		Carta c = b.compraCarta();
		this.maos.get(mao).add(c);
	}
	
	public void adicionaCarta(Carta c, int mao) {
		//Carta c = b.compraCarta();
		this.maos.get(mao).add(c);
	}
	
	public List<Carta> getMao(int indice) {
		return this.maos.get(indice);
	}
	
	public int novaMao() {
		if (this.maos == null) {
			this.maos = new ArrayList<List<Carta>>();
		}
		List<Carta> ac = new ArrayList<Carta>();
		this.maos.add(ac);
		
		return this.maos.size() - 1;
	}
	
	public boolean removeMao(int indice) {
		List<Carta> lc;

		if (indice < 0) {
			return false;
		}
		
		lc = this.maos.remove(indice);
		return lc != null;
	}
	
	public void limpaMao() {
		this.maos = null;
	}
	
	public int qtdMaos() {
		if (this.maos == null)
			return 0;
		
		return this.maos.size();
	}
}
