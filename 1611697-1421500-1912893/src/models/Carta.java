// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

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
	
	public String getNome() {
		int val = this.valor.getValorCarta();
		
		if (val < 9) {
			return Integer.toString(val);
		}
		
		switch (this.valor) {
		case DEZ:
			return "t";
		case VALETE:
			return "j";
		case DAMA:
			return "q";
		case REI:
			return "k";
		case AS:
			return "a";	
		}
		
		//funcao nao deveria chegar aqui
		return null;
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
