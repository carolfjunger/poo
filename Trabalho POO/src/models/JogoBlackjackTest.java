package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JogoBlackjackTest {
	
	private Jogador j1;
	private Jogador j2; 
	private JogoBlackjack jbl;
	private int QTD_DECKS = 5;

	@Before
	public void setUp() {
		int f = 0;
		this.j1 = new Jogador("teste1", f);
		this.j2 = new Jogador("teste2", f);
		List<Jogador> jogs = new ArrayList<Jogador>();
		jogs.add(j1);
		jogs.add(j2);
		
		this.jbl = JogoBlackjack.getInstancia(); 
		this.jbl.setJogadores(jogs);
	}
	
	@After
	public void tearDown() {
		this.jbl.resetaJogo();
	}
	
	@Test
	public void testJogoNaoIniciado() {
		assertNotEquals(null, this.jbl);
		
		List<Jogador> jogsJogo = this.jbl.getJogadores();
		// 2 jogadores, o dealer entra quando o jogo for iniciado
		assertEquals(2, jogsJogo.size());
		assertEquals(2, this.jbl.getQtdJogadores());
		
		// garantir que os jogadores adicionados no setUp estao presentes
		assertEquals(true, jogsJogo.contains(this.j1));
		assertEquals(true, jogsJogo.contains(this.j2));
		
		// baralho nao foi inicializado
		assertEquals(0, this.jbl.getBaralho().getQtdCartas());
		
		// nenhuma ficha na mesa
		assertEquals(0, this.jbl.getFichasMesa());
		
		// nenhuma aposta foi feita
		assertEquals(null, this.jbl.getApostas());
	}
	
	@Test
	public void testIniciaJogo() {
		boolean sucesso;
		
		sucesso = this.jbl.inicializa(-1);
		assertEquals(false, sucesso);
		
		sucesso = this.jbl.inicializa(this.QTD_DECKS);
		assertEquals(true, sucesso);
		
		List<Jogador> jogs = this.jbl.getJogadores();
		// existe um jogador adicional
		assertEquals(3, jogs.size());
		assertEquals(3, this.jbl.getQtdJogadores());
		
		// todos os jogadores tem 500 fichas
		// todos os jogadores tem mao vazia
		boolean hasDealer = false;
		for (Jogador j: jogs) {
			int qtdFichas = j.getFichas();
			assertEquals(500, qtdFichas);
			if (j.getID() == "dealer") {
				hasDealer = true;
			}
		}
		
		//existe um dealer
		assertEquals(true, hasDealer);
		
		// baralho foi inicializado
		assertNotEquals(null, this.jbl.getBaralho());
		
		// baralho tem NUM_DECKS
		int qtdCartas = this.jbl.getBaralho().getQtdCartas();
		assertEquals(52 * this.QTD_DECKS, qtdCartas);
		
		// nenhuma ficha na mesa
		assertEquals(0, this.jbl.getFichasMesa());
		
		// nenhuma aposta foi feita
		assertEquals(null, this.jbl.getApostas());
	}
	
	@Test
	public void testColheApostas() {
		this.jbl.inicializa(this.QTD_DECKS);
		
		HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>();
		
		
		int aposta1 = 1;
		int aposta2 = 20;
		
		apostas.put(this.j1, aposta1);
		apostas.put(this.j2, aposta2);
	
		this.jbl.colheApostaInicial(apostas);
		int f = this.jbl.getFichasMesa();
		
		// 20 apostado pelo jogador j2, sem contar o dealer
		final int VAL_RESTANTE = 500 - 20;
		assertEquals(20, f);
		
		// aposta de menos que 20 deveria falhar
		assertEquals(500, this.j1.getFichas());
		assertEquals(false, this.jbl.getApostas().keySet().contains(j1));
		
		// aposta >= 20 funciona
		assertEquals(VAL_RESTANTE, this.j2.getFichas());
		assertEquals(true, this.jbl.getApostas().keySet().contains(j2));
	}
	
	@Test
	public void testRecebeCartas() {
		// pre teste
		this.jbl.inicializa(this.QTD_DECKS);
		
		HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>();
		int aposta1 = 20;
		int aposta2 = 20;
		
		// gravar as apostas
		apostas.put(this.j1, aposta1);
		apostas.put(this.j2, aposta2);
		
		this.jbl.colheApostaInicial(apostas);
		
		// teste
		this.jbl.recebeCartas();
		
		// cada jogador tem duas cartas viradas para cima
		// dealer tem uma carta virada para baixo
		for (Jogador j: this.jbl.getApostas().keySet()) {
			Carta c1 = j.getMao(0).get(0);
			Carta c2 = j.getMao(0).get(1);
			
			if (j.getID() == "dealer") {
				assertEquals(false, c1.getPraBaixo());
				assertEquals(true, c2.getPraBaixo());
				continue;
			}
			
			assertEquals(false, c1.getPraBaixo());
			assertEquals(false, c2.getPraBaixo());
		}
	}
	
	@Test
	public void testVez() {
		// pre teste
		this.jbl.inicializa(this.QTD_DECKS);
		
		HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>();
		int aposta1 = 20;
		int aposta2 = 20;
		
		// gravar apostas
		apostas.put(this.j1, aposta1);
		apostas.put(this.j2, aposta2);
		
		this.jbl.colheApostaInicial(apostas);
		this.jbl.recebeCartas();
		
		// teste
		Jogador dealer = null;
		for (Jogador j: this.jbl.getJogadores()) {
			if (j.getID() == "dealer") {
				dealer = j;
				continue;
			}
		}
		
		// comando HIT compra uma carta
		this.jbl.vez(this.j1, Comando.HIT, 0);
		assertEquals(3, this.j1.getMao(0).size());
		
		// dealer sempre compra uma carta
		this.jbl.vez(dealer, null, 0);
		assertEquals(3, this.j1.getMao(0).size());
		
		// double, a aposta na mesa deve ser incrementada
		// e as fichas do jogador diminuidas
		this.jbl.vez(this.j2, Comando.DOUBLE, aposta2);
		assertEquals(this.j2.getFichas(), 460);
		
		assertEquals( 60, this.jbl.getFichasMesa() );
		
		// surrender, jogador sai da aposta e perde metade do que apostou
		this.jbl.vez(this.j2, Comando.SURRENDER, 0);
		assertEquals(false, this.jbl.getApostas().keySet().contains(this.j2));

		// SPLIT -> N/A
	}
	
	@Test
	public void testFinalizaTurno() {
		// pre teste
		this.jbl.inicializa(this.QTD_DECKS);
		
		HashMap<Jogador, Integer> apostas = new HashMap<Jogador, Integer>();
		int aposta1 = 20;
		int aposta2 = 20;
		
		// gravar apostas
		apostas.put(this.j1, aposta1);
		apostas.put(this.j2, aposta2);
		
		this.jbl.colheApostaInicial(apostas);
		this.jbl.recebeCartas();
		
		// teste
		this.jbl.finalizaTurno();
		
		// baralho deveria ter todas as cartas de volta
		// mao de cada jogador deve ser zerada
		assertEquals(52 * this.QTD_DECKS, this.jbl.getBaralho().getQtdCartas());
		assertEquals(0, this.j1.qtdMaos());
		assertEquals(0, this.j2.qtdMaos());
		for (Jogador j: this.jbl.getJogadores()) {
			System.out.println(j.getID());

			if (j.qtdMaos() > 0) {
				assertEquals(0, j.getMao(0).size());
				continue;
			}
			assertEquals(0, j.qtdMaos());
		}
	}
}
