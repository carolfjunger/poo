// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package models;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JogadorTest {
	
	private int fichas;
	private Jogador jog;
	private final int VAL_INICIAL = 500;

	@Before
	public void setUp() {
		this.fichas = 500;
		this.jog = new Jogador("Teste", fichas);
	}

	@Test
	public void testGetFichas() {
		int val = jog.getFichas();
		
		assertEquals(this.VAL_INICIAL, val);
	}
	
	@Test
	public void testRecebeFicha() {
		int fichas = 102;
		
		jog.recebeFichas(fichas);
		
		int val = jog.getFichas();
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
		assertEquals(prim.getValor(), c.getValor());
		assertEquals(prim.getNaipe(), c.getNaipe());
	}
	
	@Test
	public void testApostaFicha() {	
		// aposta que o jogador tem como cobrir
		int fichas = 102;
		
		int val = this.jog.getFichas();
		assertEquals(this.VAL_INICIAL, val);
		
		boolean sucesso = this.jog.apostaFichas(fichas);
		assertEquals(true, sucesso);
		
		// valor deveria ser igual a o valor inical subtraido das fichas
		assertEquals(this.VAL_INICIAL - 102, this.jog.getFichas());
		
		// aposta que o jogador nao tem como cobrir
		fichas = 1000000; // UM MILHAO!
		
		sucesso = this.jog.apostaFichas(fichas);
		assertEquals(false, sucesso);
	}
}
