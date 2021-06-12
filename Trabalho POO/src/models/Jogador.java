package models;
import java.util.HashMap;
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
		List<Carta> ac = new ArrayList<Carta>();
		this.maos.add(ac);
		
		return this.maos.size() - 1;
	}
	
	public boolean removeMao(int indice) {
		List<Carta> lc;
		int qtd = this.qtdMaos();
		if (indice < 0 || indice >= qtd) {
			return false;
		}
		
		lc = this.maos.remove(indice);
		return lc != null;
	}
	
	public void limpaMao(int indice) {
		for (int i = 0; i < this.maos.size(); i++) {
			this.maos.remove(i);
		}
		this.novaMao();
	}
	
	public int qtdMaos() {
		return this.maos.size();
	}
}
