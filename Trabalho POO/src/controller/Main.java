package controller;

import javax.swing.JOptionPane;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import models.*;
import view.*;

public class Main {
	public static void main(String[] args) {
		
		Janela ji = new JanelaInicial("Jogo de blackjack", () -> {
    		String input = "";
    		int numJogadores = 0;
    		while (numJogadores <= 0 || numJogadores > 4) {
    			input = JOptionPane.showInputDialog("Favor entrar com valor entre 1 e 4:");
    			try {
    				numJogadores = Integer.parseInt(input);
    			}
    			catch (NumberFormatException nfe) {
    				continue;
    			}
    		}
    		
    		iniciaJogo(numJogadores);
		});
		
		ji.setVisible(true);
		ji.setLocationRelativeTo(null);
	}
	
	public static void iniciaJogo(int numJogadores) {
		Janela jBanca = new JanelaBanca("Banca");
		jBanca.setVisible(true);
		
		JogoBlackjack jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		List<String> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			//HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
			Janela jg = new JanelaJogador(jID.get(i), numFichas, 0, null);
			
			Point p = new Point(i*400, 420);
			jg.setLocation(p);
			jg.setVisible(true);
		}
	}
}
