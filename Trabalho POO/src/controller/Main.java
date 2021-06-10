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
		at = new Atualizador();
		ger = new Gerenciador();
		
    	Janela jBanca = new JanelaBanca("Banca", at);
    	ArrayList<Janela> jJogador = new ArrayList<Janela>();
		
		jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		List<String> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		jbl.inicializa(2);
//		jbl.darCartas();
		


		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			String id = jID.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(id, 0);
			//System.out.println(cartas);
			JanelaJogador jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			
			// registrar janela jogador como observador
			ger.registraObs(id,jg);
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
        	ger.notificaObs("INIT", null);
	    });
	}
	
	private static class Atualizador implements Observer {
		@Override
		//TODO: falta implementar corretamente, o abaixo eh apenas um exemplo
		public void update(String evento, Object val) {
			int proxVez = jbl.getVez() + 1;
			int totalDeJogadores = jbl.getIDJogadores().size();
			switch(evento.toUpperCase()) {
			case "DEAL":
				int fichasApostadas = (int) val;
				jbl.colheAposta(Integer.toString(jbl.getVez()), fichasApostadas);
				if(proxVez >= totalDeJogadores - 1) {
					jbl.darCartas();
					proxVez = 0;
					jbl.setVez(proxVez);
					ger.notificaObs("DAR_CARTAS", null);
				} else {
					jbl.setVez(proxVez);
					ger.notificaObs("INIT", null);
				}
				
//				ger.notificaObs("INIT");
				break;
//			case "HIT":
//				int fichas = (int) val;
//				break;
//			case "VEZ":
//				ger.notificaObs("VEZ", null);
//				break;
			case "STAND":
				if(proxVez >= totalDeJogadores - 1) {
					System.out.println("Finaliza turno");
				} else {
					jbl.setVez(proxVez);
					ger.notificaObs("VEZ", null);
				}
				break;
			case "FICHA_CLICK":
				ger.notificaObs("FICHA_CLICK", val);
				break;
			default:
				System.out.println("Erro fatal! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}
	}
	
	private static class Gerenciador implements Observable {
		private HashMap<String, Observer> observers = new  HashMap<String, Observer>();
		
		@Override
		public void registraObs(String jogId, Observer observer) {
			observers.put(jogId, observer);			
		}

		@Override
		public void removeObs(String jogId) {
			observers.remove(jogId);
		}

		@Override
		public void notificaObs(String evento, Object val) {
			switch(evento) {
			case "INIT":
				for (Observer o: observers.values()) {
					o.update("INIT", jbl.getVez());
				}
				break;
			case "VEZ":
				for (String jogId: observers.keySet()) {
					int[] value = { jbl.getVez(), jbl.getSomaCartasJogador(jogId, 0)};
					observers.get(jogId).update("VEZ", value);
				}
				break;
			case "DAR_CARTAS":
				for (String jogId: observers.keySet()) {
					HashMap<String, Boolean> cartas = jbl.getCartasJogador(jogId, 0);
					observers.get(jogId).update("DAR_CARTAS", cartas);
					ger.notificaObs("VEZ", null);
				}
				break;
			case "FICHA_CLICK":
				for (String jogId: observers.keySet()) {
					int[] vezEficha = { jbl.getVez(), (int) val};
					observers.get(jogId).update("FICHA_CLICK", vezEficha);
				}
				break;
			default:
				System.out.println("Erro fatal recebendo mensagem na Main! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}
	}
}
