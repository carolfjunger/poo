package models;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class JogoBlackjack {
	private Baralho baralho;
	private HashMap<Ficha, Integer> fichas = new HashMap<Ficha, Integer>(); 
	private List<Jogador> jogadores = new ArrayList<Jogador>();
	
	public List<Jogador> getJogadores() {
		return this.jogadores;
	}
	
	public int getQtdJogadores() {
		return this.jogadores.size();
	}
	
	public HashMap<Ficha, Integer> getFichasMesa() {
		return this.fichas;
	}

	public void inicializar() {
		// montar baralho
		// acrescentar jogadores
		// dar fichas aos jogadores
		// :)
	}
	
	public void turno(String comando) {	
	}
}
