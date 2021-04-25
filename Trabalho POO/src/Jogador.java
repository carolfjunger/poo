import java.util.List;
import java.util.ArrayList;

public class Jogador {
	private String id;
	private List<Ficha> fichas;
	private List<List<Carta>> cartas = new ArrayList<List<Carta>>();

	Jogador(String id, List<Ficha> fichas) {
		this.id = id;
		this.fichas = fichas;
	}
	
//	void apostaFichas(Mesa m) {
//		
//	}
	
	public void recebeFichas(List<Ficha> fichas) {
		this.fichas.addAll(fichas);
	}
	
	public void compraCarta(Baralho b, int mao) {
		Carta c = b.compraCarta();
		this.cartas.get(mao).add(c);
	}
	
	public List<Carta> getMao(int indice) {
		return this.cartas.get(indice);
	}
	
	public int qtdMaos() {
		return this.cartas.size();
	}
}
