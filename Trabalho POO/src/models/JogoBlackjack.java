package models;

import java.util.List;
import java.util.Objects;

import java.util.ArrayList;
import java.util.HashMap;

public class JogoBlackjack {
	private Baralho baralho = new Baralho(); // verificar oq saiu?
	
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private List<HashMap<Integer, Integer>> apostas = null;
	private int vez = 0;
	private int maoCorrente = 0;
	private int qtdCartasUsadas = 0;
	
	// constantes
	private final int APOSTA_MIN = 20;
	private final int FICHAS_INI = 500;
	//private int fichasMesa = 0; 

	// construtor privado, 
	// necessario para nossa impl de singleton
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
		this.baralho = new Baralho();
	}
	
	public List<Integer> getIDJogadores() {
		List<Integer> ids = new ArrayList<Integer>();
		
		for (int i = 0; i < this.jogadores.size(); i++) {
			ids.add(i);
		}
		
		return ids;
	}
	
	public List<String> getCartasJogador(int indJog, int iMao) {
		Jogador j = this.jogadores.get(indJog);
		List<Carta> lc = j.getMao(iMao);
		List<String> cartas = new ArrayList<String>();
		
		//String nomes[] = {"a", "t", "j", "q", "k"};
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
			
			cartas.add(chave);
		}
		
		return cartas;
	}
	
	public int getSomaCartasJogador(int indJog, int iMao) {
		Jogador j = this.jogadores.get(indJog);
//		System.out.println( "IND JOG:" + indJog);
//		System.out.println( "IND MAO:" + iMao);
		List<Carta> lc = j.getMao(iMao);
		
		return this.contaMao(lc);
	}
	
	public int getVez() {
		return this.vez;
	}
	
	public int getMaoCorrente() {
		return this.maoCorrente;
	}
	
	public void setVez(int vez) {
		this.vez = vez;
	}
	
	public void setMaoCorrente(int mc) {
		this.maoCorrente = mc;
	}
	
	public void setJogadores(int num) {
		List<Jogador> lj = new ArrayList<Jogador>();
		for (int i=0; i<num; i++) {
			Jogador j = new Jogador(Integer.toString(i), 0);
			lj.add(j);
		}
		
		this.jogadores = lj;
	}
	
	public boolean removeJogador(int indJog) {
		int tam = this.jogadores.size();
		if (indJog >= tam - 1)
			return false;
					
		this.jogadores.remove(indJog);
		return true;
	}
	
	public int getQtdJogadores() {
		return this.jogadores.size();
	}
	
	public int getQtdMaosJogador(int indJog) {
		return this.jogadores.get(indJog).qtdMaos();
	}
	
	public int getTamMaoJogador(int indJog, int indMao) {
		return this.jogadores.get(indJog).getMao(indMao).size();
	}
	
	public List<Integer> getFichasJogadores() {
		List<Integer> fs = new ArrayList<Integer>();
		for (Jogador j: this.jogadores) {
			System.out.println(j.getID());
			fs.add(j.getFichas());
		}
		
		return fs;
	}
	
	public List<HashMap<Integer, Integer>> getApostasJogadores() {
		List<HashMap<Integer, Integer>> fs = new ArrayList<HashMap<Integer, Integer>>();
		for (int i = 0; i < this.jogadores.size() - 1; i++) {
			Jogador j = this.jogadores.get(i);
			
			HashMap<Integer, Integer> apostaMao = new HashMap<Integer, Integer>();
			for (int m = 0; m < j.qtdMaos(); m++) {
				if (this.apostas.size() <= i)
					continue;
				
				if (this.apostas.get(i) != null) {
					apostaMao = this.apostas.get(i);
				}
			}
			fs.add(apostaMao);
		}
		
		return fs;
	}

	// retorna `true` se deu certo
	// ou `false` se a quantidade de jogadores ou decks for invalida
	public boolean inicializa(int qtdDecks) {
		this.qtdCartasUsadas = 0;
		this.baralho.adicionaDeck(qtdDecks);
		this.baralho.embaralha();
		this.apostas = new ArrayList<HashMap<Integer, Integer>>();
		
		if (qtdDecks <= 0) {
			return false;
		}
		
		int fichas = 500;
		for (int i=0; i < this.jogadores.size(); i++) {
			Jogador j = this.jogadores.get(i);
			j.recebeFichas(fichas);
			j.novaMao();
			
			HashMap<Integer, Integer> apostaMao = new HashMap<Integer, Integer>();
			apostaMao.put(0, 0);
			this.apostas.add(apostaMao);
		}
		
		// dealer entra por ultimo
		Jogador dealer = new Jogador("dealer", fichas);
		dealer.novaMao();
		this.jogadores.add(dealer);
		
		return true;
	}

	public void colheAposta(int indJog, int indMao, int fichasAposta) {
		Jogador j = this.jogadores.get(indJog);
		
		if (fichasAposta < this.APOSTA_MIN) {
			System.out.printf("Jogador #%d com mao #%d esta tentando apostar menos que o minimo (%d)\n", 
					indJog, indMao, this.APOSTA_MIN);
			return;
		}
		
		HashMap<Integer, Integer> li = null;
		if (this.apostas.size() > indJog) {
			li = this.apostas.get(indJog);
		}
		
		int atual = 0;
		if (!li.containsKey(indMao)) {
			li.put(indMao, 0);
		};
		
		atual += li.get(indMao);
		if (j.apostaFichas(fichasAposta)) {
			li.put(indMao, fichasAposta + atual);
		}
	}
	
	public List<HashMap<Integer, Integer>> getApostas() {
		return this.apostas;
	}
	
	public void darCartas(int indMao) {
		for (int i = 0; i < this.jogadores.size(); i++) {
			Jogador j = this.jogadores.get(i);
			
			if (j.qtdMaos() == 0)
				j.novaMao();
			
			j.compraCarta(this.baralho, indMao);
			j.compraCarta(this.baralho, indMao);
			this.qtdCartasUsadas += 2;
			
			// PARA TESTE - BLACKJACK
			if (i == 0 || i == 1) {
				j.limpaMao();
				j.novaMao();
				Carta c1 = new Carta(Naipe.COPAS, ValorCarta.AS);
				Carta c2 = new Carta(Naipe.COPAS, ValorCarta.AS);
				j.adicionaCarta(c1, 0);
				j.adicionaCarta(c2, 0);
			}
			
			// dealer carta pra baixo
			if (i == this.jogadores.size() - 1) {
				j.getMao(0).get(1).setPraBaixo(true);
			}
			
			List<Carta> cs = j.getMao(indMao);
			System.out.println("Cartas do jogador: " + i);
			for (Carta c: cs) {
				System.out.print("Nome: " + c.getNome());
				System.out.print(" - Naipe: " + c.getNaipe().Repr());
				System.out.println("---");
			}
			
		}
	}
	
	public void abreMaoDealer() {
		Jogador dealer = this.jogadores.get(this.jogadores.size() - 1); // pega o dealer
		List<Carta> maoDealer = dealer.getMao(0);
		
		maoDealer.get(0).setPraBaixo(false);
		maoDealer.get(1).setPraBaixo(false);
		
		// estrategia, HIT ate somar no min. 17
		int soma = this.contaMao(maoDealer);
		while (soma < 17) {
			dealer.compraCarta(this.baralho, 0);
			soma = this.contaMao(maoDealer);
		}
	}
	
	// retorna
	// 1 = 21
	// -1 = quebrou
	// 0 = nao quebrou
	public int hit(int indJogador, int indMao) {
		Jogador j = this.jogadores.get(indJogador);
		
		//int qtdMao = j.qtdMaos();
		List<Carta> mao = j.getMao(indMao);
		
		
		j.compraCarta(this.baralho, indMao);
		int valMao = this.contaMao(mao);
		
		if (valMao > 21)
			return -1;
					
		if (valMao == 21)
			return 1;
					
		return 0;
	}
	
	public int split(int indJogador, int indMao) {
		
		Jogador j = this.jogadores.get(indJogador);
		
		List<Carta> mao = j.getMao(indMao);
		Carta c1 = mao.get(0);
		Carta c2 = mao.get(1);
		
		if ( mao.size() != 2 || c1.getCarta() != c2.getCarta() ) {
			System.out.println("Erro fatal no split! Mao de tamanho != 2 ou cartas desiguais.");
			System.exit(1);
		}
		
		int indNova = j.novaMao();
		List<Carta> maoNova = j.getMao(indNova);
		maoNova.add(c2);
		mao.remove(1);
		
		return indNova;
	}
	
	public void surrender(int indJogador) {
		int aposta = this.apostas.get(indJogador).get(0);
		this.apostas.get(indJogador).put(0, 0);
		Jogador j = this.jogadores.get(indJogador);
		
		j.recebeFichas((int) Math.round(aposta/2.0));
	}
	
	// retorna 
	// 1 no caso de vitoria
	// 0 no caso de empate
	// -1 no caso de derrota
	// -2 no caso de aposta zerada
	public int finalizaTurnoJog(int indJogador, int indMao) {
		int last = this.jogadores.size() - 1;
		Jogador dealer = this.jogadores.get(last);
		List<Carta> maoDealer = dealer.getMao(0);
		int somaDealer = this.contaMao( maoDealer );
		
		Jogador j = this.jogadores.get(indJogador);
		List<Carta> maoJog = j.getMao(indMao);
		int somaJogador = this.contaMao(maoJog);
		
		boolean dealerQuebrou = somaDealer > 21;
		boolean jogadorQuebrou = somaJogador > 21;
		
		boolean dealerBj = somaDealer == 21 
							&& maoDealer.size() == 2;
		boolean jogadorBj = somaJogador == 21 
							&& maoJog.size() == 2 
							&& j.qtdMaos() == 1;
		
		int fichas = 0;
		if (this.apostas.size() > indJogador 
				&& this.apostas.get(indJogador).containsKey(indMao)) {
			fichas = this.apostas.get(indJogador).get(indMao);
		}
		
		if (fichas == 0) {
			return -2;
		}
		
		if (jogadorQuebrou) {
			return -1;
		}
		
		if (dealerBj && !jogadorBj) {
			return -1;
		}
		
		if (!dealerBj && jogadorBj) {
			fichas = fichas + (int)1.5*fichas;
			j.recebeFichas(fichas);
			
			return 1;
		}
		
		if (dealerQuebrou) {
			j.recebeFichas(fichas * 2);
			return 1;
		}
		
		//
		// empate e vitoria ordinaria
		//
		if (somaJogador > somaDealer) {
			j.recebeFichas(fichas * 2);
			return 1;
		}
	
		if (somaDealer > somaJogador) {
			return -1;
		}

		if (somaJogador == somaDealer) {
			j.recebeFichas(fichas);
			return 0;
		}
		
		j.recebeFichas(fichas);
		return -1;
	}
	
	public List<HashMap<Integer, Integer>> finalizaRodada() {
		List<HashMap<Integer, Integer>> resultados = new ArrayList<HashMap<Integer, Integer>>();
		
		for (int i = 0; i < this.jogadores.size() - 1; i++) {
			Jogador jog = this.jogadores.get(i);
			HashMap<Integer, Integer> apMao = new HashMap<Integer, Integer>();
			
			for (int j = 0; j < jog.qtdMaos(); j++) {
				int resultado = this.finalizaTurnoJog(i, j);
				apMao.put(j, resultado);
				
				if (resultado >= 0) {
					System.out.println("Vencedor: " + resultado);
				}
				apostas.get(i).put(j, 0);
			}
			
			resultados.add(apMao);
		}
		
		limpaMaos();
		return resultados;
	}
	
	private void limpaMaos() {
		//devolve as cartas de todos os jogadores
		for (Jogador j: this.jogadores) {
			for (int i = 0; i < j.qtdMaos(); i++) {
				for (Carta c: j.getMao(i)) {
					this.baralho.adicionaCarta(c);
				}	
			}
		}
		
		for (Jogador j: this.jogadores) {
			while (j.qtdMaos() > 0) {
				j.removeMao(j.qtdMaos() - 1);

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
	
	protected Jogador getJogadorById(String idJog) {
		List<Jogador> allJogadores = this.jogadores;
		
		for(Jogador j : allJogadores) {
			if(Objects.equals( j.getID(), idJog)) {
				return j;
			}
		
		}
		return null;
	}
}
