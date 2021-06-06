package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.*;
import view.*;

public class Main {
	
	private static Atualizador at;
	
	public static void main(String[] args) {
		
//		Janela ji = new JanelaInicial("Jogo de blackjack", () -> {
//    		String input = "";
//    		int numJogadores = 0;
//    		while (numJogadores <= 0 || numJogadores > 4) {
//    			input = JOptionPane.showInputDialog("Favor entrar com valor entre 1 e 4:");
//    			try {
//    				numJogadores = Integer.parseInt(input);
//    			}
//    			catch (NumberFormatException nfe) {
//    				continue;
//    			}
//    		}
//    		
//    		iniciaJogo(numJogadores);
//		});
		
		iniciaJogo(2);
		
//		ji.setVisible(true);
//		ji.setLocationRelativeTo(null);
	}
	
	public static void iniciaJogo(int numJogadores) {
		
    	Janela jBanca = new JanelaBanca("Banca");
    	ArrayList<Janela> jJogador = new ArrayList<Janela>();
		
		JogoBlackjack jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		List<String> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		jbl.inicializa(2);
		jbl.darCartas();
		
		at = new Atualizador(jbl);
//		at.jbl = jbl;

		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			String id = jID.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
			//System.out.println(cartas);
			Janela jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			
			Point p = new Point(i*400, 420);
			jg.setLocation(p);
			jJogador.add(jg);
		}
		
	    SwingUtilities.invokeLater(() -> {
        	jBanca.setVisible(true);
        	for (Janela jg: jJogador) {
        		jg.setVisible(true);
        	}
	    });
	}
	
	protected static class Atualizador implements Observer {
		
		JogoBlackjack jbl;
		
		public Atualizador(JogoBlackjack jbl) {
			this.jbl = jbl;
		}
		
		@Override
		public void update(String evento, Object val) {
			
			return;
		}
	}
	
	//protected static class Gerenciador implements Observable
}
