package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

enum Ficha {
	UM(1),
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
	
	public static int contaFichas(HashMap<Ficha, Integer> fichas) {
		int total = 0;
		
		for (Ficha f: fichas.keySet()) {
			total += fichas.get(f) * f.getValorFicha();
		}
		
		return total;	
	}
	
	public static HashMap<Ficha, Integer> fichasAposta(int valor) {
		int resto = valor;
		HashMap<Ficha, Integer> fichas = new HashMap<Ficha, Integer>();
		
		ArrayList<Ficha> nomesFicha = new ArrayList<Ficha>();
		nomesFicha.add(CEM);
		nomesFicha.add(CINQUENTA);
		nomesFicha.add(VINTE);
		nomesFicha.add(CINCO);
		nomesFicha.add(UM);
		
		for (int i = 0; i < nomesFicha.size(); i++) {
			Ficha denom = nomesFicha.get(i);
			int val = denom.getValorFicha();
			
			int qtd = (resto / val);
			if (qtd > 0) {
				fichas.put(denom, qtd);
				resto = resto % val;
			}
		}
		
		return fichas;	
	}
	
}