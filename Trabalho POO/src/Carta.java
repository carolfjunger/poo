
public class Carta {
	Naipe naipe; 
	ValoresCarta valor;
	
	public Carta(Naipe cNaipe, ValoresCarta cValor) throws Exception {
		naipe = cNaipe;
		valor = cValor;
	}
	
	public int getValor()  {
		return this.valor.getValorCarta();
	}
	
	public Naipe getNaipe()  {
		return this.naipe;
	}
	
}
