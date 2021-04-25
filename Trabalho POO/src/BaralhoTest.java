import static org.junit.Assert.*;
import org.junit.Test;

public class BaralhoTest {
	
	@Test
	public void testAdicionarCarta() {
		Baralho baralho = new Baralho();
		Carta c = new Carta(Naipe.COPAS, ValorCarta.AS);
		
		baralho.adicionaCarta(c);
		Carta bc = baralho.getCarta(0);

		assertEquals(c, bc);
	}
	
	@Test
	public void testComprarCarta() {
		Baralho baralho = new Baralho();
		Carta[] cartas = {
				new Carta(Naipe.COPAS, ValorCarta.AS),
				new Carta(Naipe.OUROS, ValorCarta.DOIS),
				new Carta(Naipe.PAUS, ValorCarta.TRES),
				new Carta(Naipe.ESPADAS, ValorCarta.QUATRO)
		};
		
		for (Carta c: cartas) {
			baralho.adicionaCarta(c);
		}
		
		int len = cartas.length;
		for (int i = 0; i < len; i++) {
			Carta c = baralho.compraCarta();
			Carta tc = cartas[i];
		
			assertEquals(c, tc);
		}
	}

	@Test
	public void testAdicionarDeck() {
		// numero de cartas em um baralho completo (sem coringas)
		final int TAM_COMPLETO = 52;
		Baralho baralho = new Baralho();
		 
		Carta[] deckCompleto = {
			new Carta(Naipe.COPAS, ValorCarta.DOIS),
			new Carta(Naipe.COPAS, ValorCarta.TRES),
			new Carta(Naipe.COPAS, ValorCarta.QUATRO),
			new Carta(Naipe.COPAS, ValorCarta.CINCO),
			new Carta(Naipe.COPAS, ValorCarta.SEIS),
			new Carta(Naipe.COPAS, ValorCarta.SETE),
			new Carta(Naipe.COPAS, ValorCarta.OITO),
			new Carta(Naipe.COPAS, ValorCarta.NOVE),
			new Carta(Naipe.COPAS, ValorCarta.DEZ),
			new Carta(Naipe.COPAS, ValorCarta.VALETE),
			new Carta(Naipe.COPAS, ValorCarta.DAMA),
			new Carta(Naipe.COPAS, ValorCarta.REI),
			new Carta(Naipe.COPAS, ValorCarta.AS),
			new Carta(Naipe.ESPADAS, ValorCarta.DOIS),
			new Carta(Naipe.ESPADAS, ValorCarta.TRES),
			new Carta(Naipe.ESPADAS, ValorCarta.QUATRO),
			new Carta(Naipe.ESPADAS, ValorCarta.CINCO),
			new Carta(Naipe.ESPADAS, ValorCarta.SEIS),
			new Carta(Naipe.ESPADAS, ValorCarta.SETE),
			new Carta(Naipe.ESPADAS, ValorCarta.OITO),
			new Carta(Naipe.ESPADAS, ValorCarta.NOVE),
			new Carta(Naipe.ESPADAS, ValorCarta.DEZ),
			new Carta(Naipe.ESPADAS, ValorCarta.VALETE),
			new Carta(Naipe.ESPADAS, ValorCarta.DAMA),
			new Carta(Naipe.ESPADAS, ValorCarta.REI),
			new Carta(Naipe.ESPADAS, ValorCarta.AS),
			new Carta(Naipe.OUROS, ValorCarta.DOIS),
			new Carta(Naipe.OUROS, ValorCarta.TRES),
			new Carta(Naipe.OUROS, ValorCarta.QUATRO),
			new Carta(Naipe.OUROS, ValorCarta.CINCO),
			new Carta(Naipe.OUROS, ValorCarta.SEIS),
			new Carta(Naipe.OUROS, ValorCarta.SETE),
			new Carta(Naipe.OUROS, ValorCarta.OITO),
			new Carta(Naipe.OUROS, ValorCarta.NOVE),
			new Carta(Naipe.OUROS, ValorCarta.DEZ),
			new Carta(Naipe.OUROS, ValorCarta.VALETE),
			new Carta(Naipe.OUROS, ValorCarta.DAMA),
			new Carta(Naipe.OUROS, ValorCarta.REI),
			new Carta(Naipe.OUROS, ValorCarta.AS),
			new Carta(Naipe.PAUS, ValorCarta.DOIS),
			new Carta(Naipe.PAUS, ValorCarta.TRES),
			new Carta(Naipe.PAUS, ValorCarta.QUATRO),
			new Carta(Naipe.PAUS, ValorCarta.CINCO),
			new Carta(Naipe.PAUS, ValorCarta.SEIS),
			new Carta(Naipe.PAUS, ValorCarta.SETE),
			new Carta(Naipe.PAUS, ValorCarta.OITO),
			new Carta(Naipe.PAUS, ValorCarta.NOVE),
			new Carta(Naipe.PAUS, ValorCarta.DEZ),
			new Carta(Naipe.PAUS, ValorCarta.VALETE),
			new Carta(Naipe.PAUS, ValorCarta.DAMA),
			new Carta(Naipe.PAUS, ValorCarta.REI),
			new Carta(Naipe.PAUS, ValorCarta.AS)
		 };
			
		 baralho.adicionaDeck(1);
		 
		 if (baralho.getQtdCartas() != TAM_COMPLETO) {
			 fail("O baralho esta com a quantidade incorreta de cartas.");
		 }
		 
		 for (int i = 0; i < baralho.getQtdCartas(); i++) {
			 Carta c = baralho.getCarta(i);
			 Naipe bd = c.getNaipe();
			 int bv = c.getValor();
			 
			 boolean found = false;
			 for (int j = 0; j < deckCompleto.length; j++) {
				 Naipe d = deckCompleto[i].getNaipe();
				 int v = deckCompleto[i].getValor();
				 
				 if (d == bd && v == bv) {
					 found = true;
					 break;
				 }
			 }
			 
			 if (!found) {
				 fail("O baralho esta com as cartas (NAIPE/NUMERO) incorretas.");
			 }
		 }
	}
}
