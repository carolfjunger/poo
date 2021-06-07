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
//		jbl.darCartas();
		
		at = new Atualizador();
		ger = new Gerenciador();

		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			String id = jID.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
			//System.out.println(cartas);
			JanelaJogador jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			
			// registrar janela jogador como observador
			ger.registraObs(jg);
			//System.out.println(tam);
			
			Point p = new Point(i*400, 420);
			jg.setLocation(p);
			jJogador.add(jg);
		}
		
	    SwingUtilities.invokeLater(() -> {
        	jBanca.setVisible(true);
        	for (Janela jg: jJogador) {
        		jg.setVisible(true);
        	}
        	ger.notificaObs("INIT");
	    });
	}
	
	private static class Atualizador implements Observer {
		@Override
		//TODO: falta implementar corretamente, o abaixo eh apenas um exemplo
		public void update(String evento, Object val) {
			switch(evento.toUpperCase()) {
			case "DEAL":
				int proxVez = jbl.getVez() + 1;
				int totalDeJogadores = jbl.getIDJogadores().size();
				System.out.println(proxVez);
				if(proxVez >= totalDeJogadores - 1) {
					jbl.darCartas();
					proxVez = 0;
					jbl.setVez(proxVez);
					ger.notificaObs("DAR_CARTAS");
				} else {
					jbl.setVez(proxVez);
					ger.notificaObs("INIT");
				}
				
//				ger.notificaObs("INIT");
				break;
//			case "HIT":
//				int fichas = (int) val;
//				break;
			case "VEZ":
				ger.notificaObs("VEZ");
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
			case "INIT":
				for (Observer o: this.observers) {
					o.update("INIT", jbl.getVez());
				}
				break;
			case "VEZ":
				for (Observer o: this.observers) {
					o.update("VEZ", jbl.getVez());
				}
				break;
			case "DAR_CARTAS":
				int i = 0; // nao sei se foi a maneira mais eficiente, mas foi só pra continuas
				for (Observer o: this.observers) {
					HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
					i++;
					o.update("DAR_CARTAS", cartas);
					ger.notificaObs("VEZ");
				}
				break;
			default:
				System.out.println("Erro fatal recebendo mensagem na Main! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}
	}
}
