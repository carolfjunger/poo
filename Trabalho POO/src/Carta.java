
public class Carta {
	Naipe naipe; 
	ValorCarta valor;
	
	public Carta(Naipe cNaipe, ValorCarta cValor) {
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
