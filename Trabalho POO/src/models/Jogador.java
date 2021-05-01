package models;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class Jogador {
	private String id;
	private HashMap<Ficha, Integer> fichas;
	private List<List<Carta>> maos = new ArrayList<List<Carta>>();

	Jogador(String id, HashMap<Ficha, Integer> fichas) {
		this.id = id;
		// clonar o HashMap para evitar problemas de referencia
		this.fichas = new HashMap<Ficha, Integer>(fichas);
	}
	
	public String getID() {
		return this.id;
	}
	
	public int contaValores() {
		int total = 0;
		
		for (Ficha f: this.fichas.keySet()) {
			total += this.fichas.get(f) * f.getValorFicha();
		}
		
		return total;
	}
	
	public HashMap<Ficha, Integer> getFichas() {
		return new HashMap<Ficha, Integer>(this.fichas);
	}
	
	public void recebeFichas(HashMap<Ficha, Integer> fichas) {
		for (Ficha f: fichas.keySet()) {
			int valApostado = fichas.get(f);
			
			int val = 0;
			if (this.fichas.containsKey(f)) {
				val = this.fichas.get(f);
			}
			
			// garantido ser >= 0
			int dVal = val + valApostado;
			this.fichas.put(f, dVal);
		}
	}
	
	public boolean apostaFichas(HashMap<Ficha, Integer> fichas) {
		
		// antes de retornar as fichas apostadas pelo jogador
		// precisamos verificar que a pessoa tem uma quantidade de fichas
		// maior ou igual ao valor apostado
		for (Ficha f: fichas.keySet()) {
			int valApostado = fichas.get(f);
			int val = this.fichas.get(f);
			
			if (valApostado > val) {
				return false;
			}
		}
		
		for (Ficha f: fichas.keySet()) {
			int valApostado = fichas.get(f);
			int val = this.fichas.get(f);
			
			// garantido ser >= 0
			int dVal = val - valApostado;
			this.fichas.put(f, dVal);
		}
		
		return true;		
	}
	
	public void compraCarta(Baralho b, int mao) {
		Carta c = b.compraCarta();
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
	
	public int qtdMaos() {
		return this.maos.size();
	}
}
