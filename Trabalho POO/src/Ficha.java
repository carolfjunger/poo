
public enum Ficha {
	CINCO(5), 
	DEZ(10), 
	VINTE(20), 
	CINQUENTA(50), 
	CEM(100);
	
	private int valor;
	
	Ficha(int cValor){
		this.valor = cValor;
	}
	
	public int getValorFicha() {
		return valor;
	}
}