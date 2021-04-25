import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Baralho {
	// Vetor dinamico para guardar cartas
	private List<Carta> cartas = new ArrayList<Carta>();
	
	// Retorna a quantidade de cartas no baralho
	public int getQtdCartas() {
		return this.cartas.size();
	}
	
	// Olhar uma carta do baralho
	public Carta getCarta(int indice) {		
		return this.cartas.get(indice);
	}
		
	// Embaralha o baralho, dando posicoes randomicas novas a todas as cartas
	public void embaralha() {
		List<Carta> embaralhadas = new ArrayList<Carta>();
		Random rn = new Random();
		
		for (int i = 0; i < this.cartas.size(); i++) {
			int tam = this.cartas.size();
			int indice = rn.nextInt() % tam;
			
			Carta c = this.getCarta(indice);
			this.cartas.remove(indice);
			
			embaralhadas.add(c);
		}
		
		this.cartas = embaralhadas;
	}
	
	// Utilitario: Adiciona um deck de cartas completo ao baralho
	public void adicionaDeck(int num_decks) {
		for (Naipe n : Naipe.values()) {
			for (ValorCarta v : ValorCarta.values()) {
				Carta c = new Carta(n, v);
				this.adicionaCarta(c);
			}
		}
	}
	
	// Adiciona uma carta ao baralho
	public void adicionaCarta(Carta carta) {
		this.cartas.add(carta);
	}
	
	// Compra uma carta, remove a primeira da lista e retorna ela
	public Carta compraCarta() {
		Carta c = this.getCarta(0);
		this.cartas.remove(0);
		
		return c;
	}
	
	// Retorna se contem a carta ou nao
	public boolean contem(Carta carta) {
		return this.cartas.contains(carta);
	}
}
