// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package models;
import static org.junit.Assert.*;
import org.junit.Test;

public class TesteCarta {
	
	@Test
	public void testGetValor() {
		Carta carta = new Carta(Naipe.COPAS, ValorCarta.QUATRO);
		
		assertEquals(ValorCarta.QUATRO.getValorCarta(), carta.getValor());
	}
	
	@Test
	public void testGetNaipe() {
		Carta carta = new Carta(Naipe.PAUS, ValorCarta.AS);
		
		assertEquals(Naipe.PAUS, carta.getNaipe());
	}
	
	@Test
	public void testGetPraBaixo() {
		Carta carta = new Carta(Naipe.PAUS, ValorCarta.AS, true);
		
		assertEquals(true, carta.getPraBaixo());
	}
	
	@Test
	public void testSetPraBaixo() {
		Carta carta = new Carta(Naipe.PAUS, ValorCarta.AS);
		
		carta.setPraBaixo(true);
		assertEquals(true, carta.getPraBaixo());
	}

	@Test
	public void testCriarCarta() {
		Carta carta = new Carta(Naipe.COPAS, ValorCarta.DOIS);

		assertEquals(ValorCarta.DOIS.getValorCarta(), carta.getValor());
		assertEquals(Naipe.COPAS, carta.getNaipe());
		assertEquals(false, carta.getPraBaixo());
		
		carta = new Carta(Naipe.COPAS, ValorCarta.DOIS, true);
		
		assertEquals(ValorCarta.DOIS.getValorCarta(), carta.getValor());
		assertEquals(Naipe.COPAS, carta.getNaipe());
		assertEquals(true, carta.getPraBaixo());
	}
}
