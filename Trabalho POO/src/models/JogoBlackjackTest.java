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
		this.jbl = JogoBlackjack.getInstancia(); 
		this.jbl.setJogadores(2);
	}
	
	@After
	public void tearDown() {
		this.jbl.resetaJogo();
	}
	
	@Test
	public void testJogoNaoIniciado() {
		assertNotEquals(null, this.jbl);
		
		List<Integer> jogsJogo = this.jbl.getIDJogadores();
		// 2 jogadores, o dealer entra quando o jogo for iniciado
		assertEquals(2, jogsJogo.size());
		assertEquals(2, this.jbl.getQtdJogadores());
		
		// garantir que os jogadores adicionados no setUp estao presentes
		assertEquals(true, jogsJogo.contains(0));
		assertEquals(true, jogsJogo.contains(1));
		
		
		// nenhuma ficha na mesa
		assertEquals(0, this.jbl.getFichasMesa());
		
	}
	
	@Test
	public void testIniciaJogo() {
		boolean sucesso;
		
		sucesso = this.jbl.inicializa(-1);
		assertEquals(false, sucesso);
		
		sucesso = this.jbl.inicializa(this.QTD_DECKS);
		assertEquals(true, sucesso);
		
		List<Integer> jogs = this.jbl.getIDJogadores();
		// existe um jogador adicional
		assertEquals(3, jogs.size());
		assertEquals(3, this.jbl.getQtdJogadores());
		
		// todos os jogadores tem 500 fichas
		// todos os jogadores tem mao vazia
		boolean hasDealer = false;
		for (Integer jId: this.jbl.getIDJogadores()) {
			
			if (jId == this.jbl.getQtdJogadores() - 1) {
				hasDealer = true;
				continue;
			}
			
			Jogador j = this.jbl.getJogadorById(Integer.toString(jId));
			int qtdFichas = j.getFichas();
			assertEquals(500, qtdFichas);
		}

		
		
		//existe um dealer
		assertEquals(true, hasDealer);
		
		// nenhuma ficha na mesa
		assertEquals(0, this.jbl.getFichasMesa());

	}
	
	@Test
	public void testColheApostas() {
		this.jbl.inicializa(this.QTD_DECKS);
		
		
		
		int aposta1 = 20;
		int aposta2 = 20;

	
		this.jbl.colheAposta("0", aposta1);
		this.jbl.colheAposta("1", aposta2);
		int f = this.jbl.getFichasMesa();

		assertEquals(40, f);
		
	}
	
	@Test
	public void testRecebeCartas() {
		// pre teste
		this.jbl.inicializa(this.QTD_DECKS);
		
		int aposta1 = 1;
		int aposta2 = 20;

	
		this.jbl.colheAposta("0", aposta1);
		this.jbl.colheAposta("1", aposta2);
		
		// teste
		this.jbl.darCartas();
		
		// cada jogador tem duas cartas viradas para cima
		// dealer tem uma carta virada para baixo
		for (Integer jId: this.jbl.getIDJogadores()) {
			
			if (jId == this.jbl.getQtdJogadores() - 1) {
				Jogador j = this.jbl.getJogadorById("dealer");
				Carta c1 = j.getMao(0).get(0);
				Carta c2 = j.getMao(0).get(1);
				assertEquals(false, c1.getPraBaixo());
				assertEquals(true, c2.getPraBaixo());
				continue;
			}
			
			Jogador j = this.jbl.getJogadorById(Integer.toString(jId));
			Carta c1 = j.getMao(0).get(0);
			Carta c2 = j.getMao(0).get(1);

			
			assertEquals(false, c1.getPraBaixo());
			assertEquals(false, c2.getPraBaixo());
		}
	}
	
	@Test
	public void testFinalizaTurno() {
		// pre teste
		this.jbl.inicializa(this.QTD_DECKS);
		
		
		int aposta1 = 1;
		int aposta2 = 20;

	
		this.jbl.colheAposta("0", aposta1);
		this.jbl.colheAposta("1", aposta2);
		
		
		this.jbl.darCartas();
		
		// teste
		this.jbl.finalizaTurno();
		
		for (Integer jId: this.jbl.getIDJogadores()) {
			
			if (jId == this.jbl.getQtdJogadores() - 1) {
				continue;
			}
			
			Jogador j = this.jbl.getJogadorById(Integer.toString(jId));
			assertEquals(1, j.qtdMaos());;
			assertEquals(0, j.getMao(0).size());
		}


	}
}
