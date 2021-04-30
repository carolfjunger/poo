package models;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JogadorTest {
	
	private HashMap<Ficha, Integer> fichas;
	private Jogador jog;
	private final int VAL_INICIAL = 500;

	@Before
	public void setUp() {
		this.fichas = new HashMap<Ficha, Integer>();
		this.jog = new Jogador("Teste", fichas);
		
		// Botar 500 fichas na pilha do jogador
		fichas.put(Ficha.CEM, 2);
		fichas.put(Ficha.CINQUENTA, 2);
		fichas.put(Ficha.VINTE, 5);
		fichas.put(Ficha.DEZ, 5);
		fichas.put(Ficha.CINCO, 8);
		fichas.put(Ficha.UM, 10);
	}

	@Test
	public void testContaValores() {
		int val = jog.contaValores();
		
		assertEquals(this.VAL_INICIAL, val);
	}
	
	@Test
	public void testRecebeFicha() {
		HashMap<Ficha, Integer> fichas = new HashMap<Ficha, Integer>();
		// 602 fichas
		fichas.put(Ficha.CEM, 1);
		fichas.put(Ficha.UM, 2);
		
		jog.recebeFichas(fichas);
		
		int val = jog.contaValores();
		assertEquals(this.VAL_INICIAL + 102, val);
	}
	
	@Test
	public void testNovaMao() {
		int qtdMaos = this.jog.qtdMaos();
		assertEquals(0, qtdMaos);
		
		this.jog.novaMao();
		qtdMaos = this.jog.qtdMaos();
		assertEquals(1, qtdMaos);
	}
	
	@Test
	public void testRemoveMao() {
		boolean sucesso = this.jog.removeMao(0);
		assertEquals(false, sucesso);
		
		sucesso = this.jog.removeMao(-1);
		assertEquals(false, sucesso);
		
		sucesso = this.jog.removeMao(1);
		assertEquals(false, sucesso);
		
		int ind = this.jog.novaMao();
		sucesso = this.jog.removeMao(ind);
		assertEquals(true, sucesso);
		
		// garantir que a mao criada foi removida
		int qtdMaos = this.jog.qtdMaos();
		assertEquals(0, qtdMaos);
	}
	
	@Test
	public void testQtdMaos() {
		int qtdMaos = this.jog.qtdMaos();
		assertEquals(0, qtdMaos);
		
		this.jog.novaMao();
		qtdMaos = this.jog.qtdMaos();
		assertEquals(1, qtdMaos);
		
		boolean sucesso = this.jog.removeMao(0);
		if (sucesso) {
			qtdMaos = this.jog.qtdMaos();
			assertEquals(0, qtdMaos);
		} else {
			fail("Tentando remover mão que não existe!");
		}
	}
	
	@Test
	public void testCompraCarta() {
		Baralho b = new Baralho();
		b.adicionaDeck(1);
		
		Carta prim = b.peekCarta();
		
		int ind = this.jog.novaMao();
		List<Carta> mao = this.jog.getMao(ind);
		assertEquals(0, mao.size());
		this.jog.compraCarta(b, ind);
		
		mao = this.jog.getMao(ind);
		assertEquals(1, mao.size());
		
		Carta c = mao.get(0);
		assertEquals(prim.valor, c.valor);
		assertEquals(prim.naipe, c.naipe);
	}
	
	@Test
	public void testApostaFicha() {	
		// aposta que o jogador tem como cobrir
		HashMap<Ficha, Integer> fichas = new HashMap<Ficha, Integer>();
		// 102 fichas a serem apostadas
		fichas.put(Ficha.CEM, 1);
		fichas.put(Ficha.UM, 2);
		
		int val = this.jog.contaValores();
		assertEquals(this.VAL_INICIAL, val);
		
		boolean sucesso = this.jog.apostaFichas(fichas);
		assertEquals(true, sucesso);
		
		// valor deveria ser igual a o valor inical subtraido das fichas
		assertEquals(this.VAL_INICIAL - 102, this.jog.contaValores());
		
		// aposta que o jogador nao tem como cobrir
		fichas = new HashMap<Ficha, Integer>();
		
		// CEM MIL FICHAS!
		fichas.put(Ficha.CEM, 1000);
		
		sucesso = this.jog.apostaFichas(fichas);
		assertEquals(false, sucesso);
	}
}
