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
	private static Gerenciador ger;
	private static JogoBlackjack jbl;
	
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
		
		jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		List<String> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		jbl.inicializa(2);
		jbl.darCartas();
		
		at = new Atualizador();
		ger = new Gerenciador();

		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			String id = jID.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
			//System.out.println(cartas);
			JanelaJogador jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			ger.registraObs(jg);
			
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
	
	private static class Atualizador implements Observer {
		@Override
		//TODO: falta implementar corretamente, o abaixo eh apenas um exemplo
		public void update(String evento, Object val) {
			switch(evento.toUpperCase()) {
			case "HIT":
				int fichas = (int) val;
				break;
			default:
				System.out.println("Erro fatal! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}
	}
	
	private static class Gerenciador implements Observable {
		private ArrayList<Observer> observers = new ArrayList<Observer>();
		
		@Override
		public void registraObs(Observer observer) {
			observers.add(observer);			
		}

		@Override
		public void removeObs(Observer observer) {
			observers.remove(observer);
		}

		@Override
		public void notificaObs(String evento) {
			switch(evento) {
			case "VEZ":
				for (Observer o: this.observers) {
					o.update("VEZ", jbl.getVez());
				}
				break;
			default:
				System.out.println("Erro fatal! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}}
}
