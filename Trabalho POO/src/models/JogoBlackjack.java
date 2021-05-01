package models;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class JogoBlackjack {
	private Baralho baralho = new Baralho(); // verificar oq saiu?
	private HashMap<Ficha, Integer> fichasMesa; 
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	
	private final int APOSTA_MIN = 20;
	private int vez = 0;
	private int qtdCartasUsadas = 0;
	private HashMap<Jogador, HashMap<Ficha, Integer>> apostas;
	
	public JogoBlackjack() {}
		
	// singleton
//	private static JogoBlackjack instancia;
//	public static JogoBlackjack getInstancia() {
//		if (instancia == null) {
//			instancia = new JogoBlackjack();
//		}
//		
//		return instancia;
//	}
	
	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	
	public List<Jogador> getJogadores() {
		return new ArrayList<Jogador>(this.jogadores);
	}
	
	public Baralho getBaralho() {
		return this.baralho;
	}
	
	public HashMap<Jogador, HashMap<Ficha, Integer>> getApostas() {
		if (this.apostas == null) {
			return null;
		}
		return new HashMap<Jogador, HashMap<Ficha, Integer>>(this.apostas);
	}
	
	public int getQtdJogadores() {
		return this.jogadores.size();
	}
	
	public HashMap<Ficha, Integer> getFichasMesa() {
		return this.fichasMesa;
	}

	// retorna `true` se deu certo
	// ou `false` se a quantidade de jogadores ou decks for invalida
	public boolean inicializa(int qtdDecks) {
		this.qtdCartasUsadas = 0;
		this.baralho.adicionaDeck(qtdDecks);
		this.baralho.embaralha();
		this.apostas = null;
		
		if (qtdDecks <= 0) {
			return false;
		}
		
		HashMap<Ficha, Integer> fichas = new HashMap<Ficha, Integer>();
		
		// Botar 500 fichas na pilha do jogador
		fichas.put(Ficha.CEM, 2);
		fichas.put(Ficha.CINQUENTA, 2);
		fichas.put(Ficha.VINTE, 5);
		fichas.put(Ficha.DEZ, 5);
		fichas.put(Ficha.CINCO, 8);
		fichas.put(Ficha.UM, 10);

		for (Jogador j: this.jogadores) {
			j.recebeFichas(fichas);
			j.novaMao();
			//this.jogadores.add(j);
		}
		
		// dealer entra por ultimo
		Jogador dealer = new Jogador("dealer", fichas);
		dealer.novaMao();
		this.jogadores.add(dealer);
		
		return true;
	}
	
	public void colheApostaInicial(HashMap<Jogador, HashMap<Ficha, Integer>> apostas) {
		this.apostas = apostas;
		this.fichasMesa = new HashMap<Ficha, Integer>();
		List<Jogador> aRemover = new ArrayList<Jogador>();
		
		for (Jogador j: apostas.keySet()) {
			if (j.getID() == "dealer") {
				continue;
			}
			
			HashMap<Ficha, Integer> fichasAposta = apostas.get(j);
			
			if (Ficha.contaFichas(fichasAposta) < this.APOSTA_MIN) {
				aRemover.add(j);
				continue;
			}
			
			// fichas nao sao deduzidas do jog
			// pode falhar, mas o caso eh validado no front-end
			boolean sucesso = j.apostaFichas(fichasAposta);
			
			if (sucesso) {
				for (Ficha f: fichasAposta.keySet()) {
					Integer qtdMesa = 0;
					if (this.fichasMesa.containsKey(f)) {
						qtdMesa = this.fichasMesa.get(f);
					}
					Integer qtdAposta = fichasAposta.get(f);
					
					if (qtdMesa == null) {
						this.fichasMesa.put(f, qtdAposta);
						continue;
					}
					
					this.fichasMesa.put(f, qtdAposta + qtdMesa);
				}
			} else {
				apostas.remove(j);
			}
		}
		
		for (Jogador j: aRemover) {
			this.apostas.remove(j);
		}
	}
	
	// retorna um Jogador se o mesmo n tiver fichas para apostar 
	// se nao, retorna nulo
	public void recebeCartas() {
		int APOSTA_MIN = 20;
	
		this.vez = 0;
		for (Jogador j: this.apostas.keySet()) {
			
			if (j.contaValores() < APOSTA_MIN) {
				this.jogadores.remove(j);
				continue;
			}
			
			j.compraCarta(this.baralho, 0);
			j.compraCarta(this.baralho, 0);
			this.qtdCartasUsadas += 2;
			
			if (j.getID() == "dealer") {
				j.getMao(0).get(1).setPraBaixo(true);
			}
		}
	}
	
	
	// Funcao vez simula a vez de um jogador
	// Se o retorno for `BlackJack` ou `Fim de Turno`
	// a funcao avaliarMesa deve ser chamada em seguida
	public ResultadoVez vez(Jogador j, Comando c, HashMap<Ficha, Integer> apostaDouble) {
		
		if (j.getID() == "dealer") {
			j.compraCarta(baralho, 0);
			return ResultadoVez.OK;
		}
		
		int qtd = Ficha.contaFichas( this.apostas.get(j) );
		
		if (vez < 0) {
			return ResultadoVez.FIM_DE_TURNO;
		}
		
		this.vez += 1;
		// depois, trocar jogadores.size() pela qtd de apostadores
		// para evitar jogo infinito
		if (vez > this.jogadores.size()) {
			this.vez = -1;
			return ResultadoVez.FIM_DE_TURNO;
		}
		
		switch (c) {
			case STAND:
				return ResultadoVez.OK;
			case HIT:
				// depois, split
				j.compraCarta(this.baralho, 0);
				break;
			case DOUBLE:
				if (qtd > j.contaValores()) {
					return ResultadoVez.FALTAM_FICHAS;
				}
				j.apostaFichas(apostaDouble);
				for (Ficha f: apostaDouble.keySet()) {
					int qtdDouble = apostaDouble.get(f);
					int qtdMesa = this.fichasMesa.get(f);
					
					this.fichasMesa.put(f, qtdMesa + qtdDouble);
				}
				break;
			case SPLIT:
				// depois, tratar esse caso
				break;
			case SURRENDER:
				HashMap<Ficha, Integer> fichas = Ficha.fichasAposta( qtd / 2);
				int qtdMesa = Ficha.contaFichas(this.fichasMesa);
				int newQtdMesa = qtdMesa - (qtd / 2);
				
				this.fichasMesa = Ficha.fichasAposta(newQtdMesa);
				j.recebeFichas(fichas);
				
				this.apostas.remove(j);
				break;
		}
		
		// embaralhar quando 90% das cartas forem distribuidas
		int limCartas = (int) (0.9 * this.baralho.getQtdCartas());
		if (this.qtdCartasUsadas > limCartas) {
			this.baralho.embaralha();
			this.qtdCartasUsadas = 0;
		}
		
		// tratar o split depois
		ResultadoVez res = this.avaliaMao(j.getMao(0));
		
		if (res == ResultadoVez.QUEBROU) {
			// removendo jogadores com mais que 21
			this.apostas.remove(j);
		}
		
		return res;
	}
	
	public void finalizaTurno() {
		// alterar pra quando existir o split
		List<Jogador> jogs = new ArrayList<Jogador>(this.apostas.keySet());
		
		List<Jogador> vencedores = new ArrayList<Jogador>();
		
		int maior = 0;
		for (Jogador j: jogs) {
			// tratando apenas uma mao, falta adicionar o split
			int val = contaMao( j.getMao(0) );
			
			if (val > maior) {
				vencedores = new ArrayList<Jogador>();
				vencedores.add(j);
			}
			
			if (val == maior) {
				vencedores.add(j);
			}
			
			// devolve as cartas de todos os jogadores
			for (int i = 0; i < j.qtdMaos(); i++) {
				for (Carta c: j.getMao(i)) {
					this.baralho.adicionaCarta(c);
				}
				j.removeMao(i);
			}
		}
		
		boolean vDealer = false;
		for (Jogador j: vencedores) {
			if (j.getID() == "dealer") {
				vDealer = true;
			}
		}
		
		for (Jogador j: vencedores) {
			if (vDealer) {
				j.recebeFichas( this.apostas.get(j) );
			} else {
				int qtdMesa = (int) Ficha.contaFichas(this.fichasMesa);
				if (maior == 21) {
					HashMap<Ficha, Integer> fichas = Ficha.fichasAposta( (int) (qtdMesa * 1.5));
					j.recebeFichas(fichas);
				} else {
					j.recebeFichas(this.fichasMesa);
				}
			}
		}
	}
	
	private int contaMao(List<Carta> mao) {
		int valor = 0;
		
		boolean temAs = false;
		for (Carta c: mao) {
			valor += c.getValor();
			if (c.getCarta() == ValorCarta.AS) {
				temAs = true;
			}
		}
		
		if (valor > 21 && temAs == true) {
			valor -= 10;
		}
		
		return valor;
	}
	
	private ResultadoVez avaliaMao(List<Carta> mao) {
		int valor = this.contaMao(mao);
		
		if (valor == 21) {
			return ResultadoVez.BLACKJACK;
		}
		
		if (valor > 21) {
			return ResultadoVez.QUEBROU;
		}
		
		return ResultadoVez.OK;
	}
}
