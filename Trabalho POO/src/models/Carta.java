package models;

class Carta {
	private Naipe naipe; 
	private ValorCarta valor;
	boolean praBaixo;
	
	public Carta(Naipe cNaipe, ValorCarta cValor) {
		this.naipe = cNaipe;
		this.valor = cValor;
		this.praBaixo = false;
	}
	
	public Carta(Naipe cNaipe, ValorCarta cValor, boolean praBaixo) {
		this.naipe = cNaipe;
		this.valor = cValor;
		this.praBaixo = praBaixo;
	}
	
	public boolean getPraBaixo()  {
		return this.praBaixo;
	}
	
	public void setPraBaixo(boolean praBaixo)  {
		this.praBaixo = praBaixo;
	}
	
	public int getValor()  {
		return this.valor.getValorCarta();
	}
	
	public ValorCarta getCarta() {
		return this.valor;
	}
	
	public Naipe getNaipe()  {
		return this.naipe;
	}
	
}
