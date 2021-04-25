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
	public void testCriarCarta() {
		Carta carta = new Carta(Naipe.COPAS, ValorCarta.DOIS);
		
		assertEquals(ValorCarta.DOIS.getValorCarta(), carta.getValor());
		assertEquals(Naipe.COPAS, carta.getNaipe());
	}
}
