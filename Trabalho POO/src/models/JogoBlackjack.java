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
	
	public void setJogadores(int num) {
		List<Jogador> lj = new ArrayList<Jogador>();
		for (int i=0; i<num; i++) {
			Jogador j = new Jogador(Integer.toString(i), 0);
			lj.add(j);
		}
		
		this.jogadores = lj;
	}
	
	public Jogador getJogadorById(String idJog) {
		List<Jogador> allJogadores = this.jogadores;
		
		for(Jogador j : allJogadores) {
			if(Objects.equals( j.getID(), idJog)) {
				return j;
			}

		
		}
		System.out.println("Erro, nao encontrou o jogador com id:" + idJog);
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

	public void colheAposta(String idJog, int fichasAposta) {
		
		this.fichasMesa += fichasAposta;
		Jogador j = this.getJogadorById(idJog);
		
		// apenas para garantir que o aposta nao eh null
		if(this.apostas == null) {
			this.apostas = new HashMap<Jogador, Integer>();
		}
		
		if(j != null) {
			this.apostas.put(j, fichasAposta);
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
		j.getMao(0).get(0).setPraBaixo(false);
	}
	
	public void finalizaTurno() {
		// TODO: alterar pra quando existir o split
		List<Jogador> apostadores = new ArrayList<Jogador>(this.apostas.keySet());
		List<Jogador> vencedores = new ArrayList<Jogador>();
		List<Jogador> allJogadores = this.jogadores;
		
		int maior = 0;
		boolean vDealer = false;
		
		int last = this.jogadores.size() - 1;
		Jogador dealer = this.jogadores.get(last);
		apostadores.add(dealer);
		
		for (Jogador j: apostadores) {
			// tratando apenas uma mao, falta adicionar o split
			int val = contaMao( j.getMao(0) );
			System.out.print(j.getID());
			System.out.print(" --- MAO:   ");
			System.out.println(val);
			if (val == maior) {
				vDealer = (j.getID() == "dealer");
				vencedores.add(j);
			} else 
			if (val > maior) {
				vencedores = new ArrayList<Jogador>();
				vencedores.add(j);
				vDealer = (j.getID() == "dealer");
				maior = val;
			}
			
			if (j.getID() != "dealer")
				j.apostaFichas(this.apostas.get(j)); // remove as fichas do jogador	
			
		}
		
		for (Jogador j: allJogadores) {
			 //devolve as cartas de todos os jogadores
			for (int i = 0; i < j.qtdMaos(); i++) {
				for (Carta c: j.getMao(i)) {
					this.baralho.adicionaCarta(c);
				}
				
				j.limpaMao(i);
			}
		}
		
		System.out.println("VENCEDORES");
		if(!vDealer) {	
			for (Jogador j: vencedores) {
				System.out.print("JOGID:   ");
				System.out.println(j.getID());
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
