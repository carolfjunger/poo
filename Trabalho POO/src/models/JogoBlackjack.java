package models;

import java.util.List;
import java.util.Objects;

import java.util.ArrayList;
import java.util.HashMap;

public class JogoBlackjack {
	private Baralho baralho = new Baralho(); // verificar oq saiu?
	private int fichasMesa = 0; 
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	private HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>() ;
	private int vez = 0;
	private int maoCorrente = 0;
	private int qtdCartasUsadas = 0;
	
	// constantes
	private final int APOSTA_MIN = 20;
	private final int FICHAS_INI = 500;

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
		this.fichasMesa = 0;
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
	
	public int getQtdJogadores() {
		return this.jogadores.size();
	}
	
	public int getQtdMaosJogador(int indJog) {
		return this.jogadores.get(indJog).qtdMaos();
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
	
	public List<Integer> getApostasJogadores() {
		List<Integer> fs = new ArrayList<Integer>();
		for (Jogador j: this.jogadores) {
			if (j.getID() == "dealer") 
				continue;
			
			int aposta = apostas.get(j);
			fs.add(aposta);
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

	public void colheAposta(int indJog, int fichasAposta) {
		
		this.fichasMesa += fichasAposta;
		Jogador j = this.jogadores.get(indJog);
		
		// apenas para garantir que o aposta nao eh null
		if(this.apostas == null) {
			this.apostas = new HashMap<Jogador, Integer>();
		}
		
		int atual = 0;
		if (this.apostas.containsKey(j)) {
			atual = this.apostas.get(j);
		}
			
		if (j.apostaFichas(fichasAposta)) {
			this.apostas.put(j, fichasAposta + atual);
		}
	}
	
	public void darCartas(int indMao) {
		for (int i = 0; i < this.jogadores.size(); i++) {
			Jogador j = this.jogadores.get(i);
			j.compraCarta(this.baralho, indMao); // TODO: split
			j.compraCarta(this.baralho, indMao);
			this.qtdCartasUsadas += 2;
			
			// PARA TESTE - BLACKJACK
			if (i == 0) {
				j.limpaMao(0);
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
				System.out.println(" -");
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
		List<Carta> mao = j.getMao(indMao); //TODO: split
		
		
		j.compraCarta(this.baralho, indMao);
		// PARA TESTE
//		Carta c1 = new Carta(Naipe.COPAS, ValorCarta.TRES);
//		j.adicionaCarta(c1, 0);
		int valMao = this.contaMao(mao);
		
		if (valMao > 21)
			return -1;
					
		if (valMao == 21)
			return 1;
					
		return 0;
	}
	
	public int split(int indJogador, int indMao) {
		
		Jogador j = this.jogadores.get(indJogador);
		
		List<Carta> mao = j.getMao(indMao); //TODO: split
		Carta c1 = mao.get(0);
		Carta c2 = mao.get(1);
		
		if ( mao.size() != 2 || c1.getCarta() != c2.getCarta() ) {
			System.out.println(mao.size() == 2);
			System.out.println(c1);
			System.out.println(c2);
			System.out.println("Erro fatal no split! Mao de tamanho != 2 ou cartas desiguais.");
			System.exit(1);
		}
		
		int indNova = j.novaMao();
		List<Carta> maoNova = j.getMao(indNova);
		maoNova.add(c2);
		mao.remove(1);
		
		System.out.println( "TAM MAO VELHA:" + mao.size() );
		System.out.println( "TAM MAO NOVA:" + maoNova.size() );
		
		return indNova;
	}
	
	// retorna o indice do vencedor
	// ou -1 no caso de empate
	public int finalizaTurnoJog(int indJogador, int indMao) {
		Jogador vencedor = null;
		
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
		
		int fichas = this.apostas.get(j);
		int ind = last;
		
		this.colheAposta(indJogador, 0);
		
		//
		// vitoria do dealer
		//
		if (jogadorQuebrou) {
			return last;
		}
		
		if (dealerBj && !jogadorBj) {
			return last;
		}

		if (somaDealer > somaJogador) {
			ind = last;
			fichas = 0;
		}
		
		//
		// vitoria do jogador
		//
		if (dealerQuebrou) {
			j.recebeFichas(fichas);
			return indJogador;
		}
		
		if (!dealerBj && jogadorBj) {
			fichas = fichas + (int)1.5*fichas;
			j.recebeFichas(fichas);
			
			return indJogador;
		}
		
		if (somaJogador > somaDealer) {
			fichas *= 2;
			ind = indJogador;
		}
	
		//
		// empate e vitoria ordinaria
		//
		if (somaJogador == somaDealer) {
			j.recebeFichas(fichas);
			ind = -1;
		}
		
		j.recebeFichas(fichas);
		return ind;
	}
	
	public List<Integer> finalizaRodada() {
		List<Integer> vencedores = new ArrayList<Integer>();
		
		for (int i = 0; i < this.jogadores.size() - 1; i++) {
			Jogador jog = this.jogadores.get(i);
			
			for (int j = 0; j < jog.qtdMaos(); j++) {
				int resultado = this.finalizaTurnoJog(i, j);
				if (resultado >= 0 && !vencedores.contains(resultado)) {
					vencedores.add(resultado);
					System.out.println("Vencedor: " + resultado);
				}
			}
			apostas.remove(jog);
		}
		
		limpaMaos();
		
		return vencedores;
	}
	
	private void limpaMaos() {
		//devolve as cartas de todos os jogadores
		for (Jogador j: this.jogadores) {
			for (int i = 0; i < j.qtdMaos(); i++) {
				for (Carta c: j.getMao(i)) {
					this.baralho.adicionaCarta(c);
				}
				j.limpaMao(i);
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
