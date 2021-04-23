import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TesteTest {
	
	private Carta carta;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValorCarta() throws Exception {
		carta = new Carta(Naipe.Copas, ValoresCarta.DOIS);
		assertEquals(ValoresCarta.DOIS.getValorCarta(), carta.getValor());
		assertEquals(Naipe.Copas, carta.getNaipe());
		// fail("Not yet implemented");
	}

}
