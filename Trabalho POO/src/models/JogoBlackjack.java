package models;

import java.util.List;
import java.util.Objects;

import view.Observer;

import java.util.ArrayList;
import java.util.HashMap;

public class JogoBlackjack {
	private Baralho baralho = new Baralho(); // verificar oq saiu?
	private int fichasMesa = 0; 
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>() ;
	
	private final int APOSTA_MIN = 20;
	private final int FICHAS_INI = 500;
	private int vez = 0;
	private int qtdCartasUsadas = 0;
	
	private JogoBlackjack() {}
		
	// singleton
	private static JogoBlackjack instancia;
	public static JogoBlackjack getInstancia() {
		if (instancia == null) {
			instancia = new JogoBlackjack();
		}

		return instancia;
	}
	
	public void resetaJogo() {
		this.vez = 0;
		this.qtdCartasUsadas = 0;
		this.apostas = null;
		this.jogadores = new ArrayList<Jogador>();
		this.fichasMesa = 0;
		this.baralho = new Baralho();
	}
	
	public void setVez(int vez) {
		this.vez = vez;
	}
	
	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	
	public void setJogadores(int num) {
		List<Jogador> lj = new ArrayList<Jogador>();
		for (int i=0; i<num; i++) {
			Jogador j = new Jogador(Integer.toString(i), 0);
			lj.add(j);
		}
		
		this.jogadores = lj;
	}
	
	public List<Jogador> getJogadores() {
		return new ArrayList<Jogador>(this.jogadores);
	}
	
	public Jogador getJogadorById(String idJog) {
		List<Jogador> allJogadores = this.getJogadores();
		
		for(Jogador j : allJogadores) {
			if(Objects.equals( j.getID(), idJog)) {
				return j;
			}

		
		}
		System.out.println("Erro na fun��o getJogadorById, n�o encontrou o jogador com id:" + idJog);
		return null;
		
	}
	
	public List<Integer> getIDJogadores() {
		List<Integer> ids = new ArrayList<Integer>();
		int i = 0;
		for (Jogador j: this.jogadores) {
			ids.add(i);
			i++;
		}
		
		return ids;
	}
	
	public HashMap<String, Boolean> getCartasJogador(int indJog, int iMao) {
		Jogador j = this.jogadores.get(indJog);
		List<Carta> lc = j.getMao(iMao);
		HashMap<String, Boolean> cartas = new HashMap<String, Boolean>();
		
		String nomes[] = {"a", "t", "j", "q", "k"};
		for (Carta c: lc) {
			int val = c.getValor();
			String num = Integer.toString( val );
			if (val > 9) {
				switch(c.getCarta()) {
				case DEZ:
					num = "t";
					break;
				case VALETE:
					num = "j";
					break;
				case DAMA:
					num = "q";
					break;
				case REI:
					num = "k";
					break;
				case AS:
					num =  "a";
					break;
				default:
					break;
				}
			}
			
			String naipe = c.getNaipe().Repr();
			
			String chave = num + naipe;
			Boolean praBaixo = c.getPraBaixo();
			
			cartas.put(chave, praBaixo);
		}
		
		return cartas;
	}
	
	public int getSomaCartasJogador(int indJog, int iMao) {
		Jogador j = this.jogadores.get(indJog);
		List<Carta> lc = j.getMao(iMao);
		List<String> ls = new ArrayList<String>();
		int somaCartas = 0;
		
		for (Carta c: lc) {
			int val = c.getValor();
			somaCartas += val;
		}
		
		return somaCartas;
	}
	
	public int getVez() {
		return this.vez;
	}
	
	public Baralho getBaralho() {
		return this.baralho;
	}
	
	public HashMap<Jogador, Integer> getApostas() {
		if (this.apostas == null) {
			return null;
		}
		return new HashMap<Jogador, Integer>(this.apostas);
	}
	
	public int getQtdJogadores() {
		return this.jogadores.size();
	}
	
	public int getFichasMesa() {
		return this.fichasMesa;
	}
	
	public List<Integer> getFichasJogadores() {
		List<Integer> fs = new ArrayList<Integer>();
		for (Jogador j: this.jogadores) {
			System.out.println(j.getID());
			fs.add(j.getFichas());
		}
		
		return fs;
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
		
		int fichas = 500;

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
	
	public void colheApostaInicial(HashMap<Jogador, Integer> apostas) {
		this.apostas = apostas;
		this.fichasMesa = 0;
		List<Jogador> aRemover = new ArrayList<Jogador>();
		
		for (Jogador j: apostas.keySet()) {
			if (j.getID() == "dealer") {
				continue;
			}
			
			int fichasAposta = apostas.get(j);
			
			if (fichasAposta < this.APOSTA_MIN) {
				aRemover.add(j);
				continue;
			}
			
			// fichas nao sao deduzidas do jog
			// pode falhar, mas o caso eh validado no front-end
			boolean sucesso = j.apostaFichas(fichasAposta);
			
			if (sucesso) {
				this.fichasMesa += fichasAposta;
			} else {
				this.apostas.remove(j);
			}
		}
		
		for (Jogador j: aRemover) {
			this.apostas.remove(j);
		}
	}

	// TODO
	public void colheAposta(String idJog, int fichasAposta) {
		
//		if (fichasAposta > this.APOSTA_MIN) {
//			/// botar no futuro toda a fun�ao aqui dentro,
//			// mas enquanto nao temos a l�gica das fichas vou manter fora
//		}
//		
		this.fichasMesa += fichasAposta;
		Jogador j = this.getJogadorById(idJog);
		
		// apenas para garantir que o aposta nao � null
		if(this.apostas == null) {
			this.apostas = new HashMap<Jogador, Integer>();
		}
		if(j != null) {
			this.apostas.put(j, fichasAposta);
		}
//		System.out.println(this.apostas);
	}
	
	public void recebeCartas() {
		int APOSTA_MIN = 20;
	
		this.vez = 0;
		int i = 0;
		for (Jogador j: this.apostas.keySet()) {
			if (j.getFichas() < APOSTA_MIN) {
				this.jogadores.remove(j);
				continue;
			}
			
			j.compraCarta(this.baralho, 0);
			j.compraCarta(this.baralho, 0);
			this.qtdCartasUsadas += 2;
			
			if (i == this.jogadores.size() - 1) {
				j.getMao(0).get(1).setPraBaixo(true);
			}
			i++;
		}
	}
	
	public void darCartas() {
		for (int i = 0; i < this.jogadores.size(); i++) {
			Jogador j = this.jogadores.get(i);
			j.compraCarta(this.baralho, 0); // TODO: split
			j.compraCarta(this.baralho, 0);
			this.qtdCartasUsadas += 2;
			
			if (i == this.jogadores.size() - 1) {
				j.getMao(0).get(1).setPraBaixo(true);
			}
		}
	}
	
	public void abreMaoDealer() {
		Jogador j = this.jogadores.get(this.jogadores.size() - 1); // pega o dealer
		j.getMao(0).get(1).setPraBaixo(false); // TODO: se botar estrategia no dealer deve ter que alterar aqui
	}
	
	// Funcao vez simula a vez de um jogador
	// Se o retorno for `BlackJack` ou `Fim de Turno`
	// a funcao avaliarMesa deve ser chamada em seguida
	public ResultadoVez vez(Comando c, int apostaDouble) {
		
		if (this.vez < 0) {
			this.vez = 0;
			return ResultadoVez.FIM_DE_TURNO;
		}
		
		Jogador j = jogadores.get(this.vez);
		
		// dealer eh o ultimo indice
		if (this.vez == this.jogadores.size()) {
			j.compraCarta(baralho, 0);
			return ResultadoVez.OK;
		}
		
		int qtd = this.apostas.get(j);
		
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
				if (qtd > j.getFichas()) {
					return ResultadoVez.FALTAM_FICHAS;
				}
				j.apostaFichas(apostaDouble);
				this.fichasMesa += apostaDouble;
				break;
			case SPLIT:
				// depois, tratar esse caso
				break;
			case SURRENDER:
				int fichas = qtd / 2;
				int qtdMesa = this.fichasMesa;
				int newQtdMesa = qtdMesa - (qtd / 2);
				
				this.fichasMesa = newQtdMesa;
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
		// TODO: alterar pra quando existir o split
		List<Jogador> apostadores = new ArrayList<Jogador>(this.apostas.keySet());
		List<Jogador> vencedores = new ArrayList<Jogador>();
		List<Jogador> allJogadores = this.getJogadores();
		
		int maior = 0;
		System.out.println("APOSTADORES");
		for (Jogador j: apostadores) {
			// tratando apenas uma mao, falta adicionar o split
			int val = contaMao( j.getMao(0) );
			System.out.print(j.getID());
			System.out.print(" --- MAO:   ");
			System.out.println(val);
			if (val == maior) {
				vencedores.add(j);
			} else 
			if (val > maior) {
				vencedores = new ArrayList<Jogador>();
				vencedores.add(j);
				maior = val;
			}
			

			
			j.apostaFichas(this.apostas.get(j)); // remove as fichas do jogador	
			
		}
		
		Jogador dealer = null;
		for (Jogador j: allJogadores) {
			 //devolve as cartas de todos os jogadores
			for (int i = 0; i < j.qtdMaos(); i++) {
				for (Carta c: j.getMao(i)) {
					this.baralho.adicionaCarta(c);
				}
				if(j.getID() != "dealer") {
					j.limpaMao(i);
				}
				
			}
			if(j.getID() == "dealer") {
				dealer = j;
			}
		}
		
		int maoDealer = contaMao( dealer.getMao(0));
		dealer.limpaMao(0);
		boolean vDealer = maoDealer > maior;
		System.out.print("DEALER --- MAO:   ");
		System.out.println(maoDealer);

		System.out.println("VENCEDORES");
		if(!vDealer) {
			
			for (Jogador j: vencedores) {
				System.out.print("JOGID:   ");
				System.out.print(j.getID());
				int qtdMesa = this.fichasMesa;
				if (maior == 21) {
					System.out.println("BLACKJACK");
					int fichas = (int) (qtdMesa * 1.5);
					j.recebeFichas(fichas);
				} else {
					j.recebeFichas(qtdMesa);
				}
				System.out.println(j.getFichas());
			}
			
		} else {
			System.out.print("DEALER : ");
			System.out.println(contaMao( dealer.getMao(0)));
		}
		System.out.println("-------");

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
	
//	private int avaliaMesa() {
//		
//	}
}
