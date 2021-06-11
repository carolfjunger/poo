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
		
    	JanelaBanca jBanca = new JanelaBanca("Banca", at);
    	ArrayList<Janela> jJogador = new ArrayList<Janela>();
		
		jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		jbl.inicializa(2);
		
		List<Integer> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		ger.registraObs(jID.get(tam-1), jBanca);
//		jbl.darCartas();
		
		// tam-1 para excluir o dealer
		for (int i=0; i< tam - 1; i++) {
			int numFichas = jf.get(i);
			int id = jID.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(id, 0);
			JanelaJogador jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			
			// registrar janela jogador como observador
			ger.registraObs(id, jg);
			
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
//					System.out.println("Finaliza turno");
					jbl.abreMaoDealer();
//					jbl.setVez(proxVez);
//					jbl.finalizaTurno();
					ger.notificaObs("DEALER_OPEN", null);
				} else {
					jbl.setVez(proxVez);
					ger.notificaObs("VEZ", null);
				}
				break;
			case "FICHA_CLICK":
				ger.notificaObs("FICHA_CLICK", val);
				break;
			case "INIT":
				ger.notificaObs("INIT", null);
				break;
			case "FINALIZA_TURNO":
				System.out.println("Finaliza turno");
//				jbl.setVez(0);
				jbl.finalizaTurno();
				List<Integer> jf = jbl.getFichasJogadores();
				ger.notificaObs("FINALIZA_TURNO", jf);
				break;
			default:
				System.out.println("Erro fatal! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}
	}
	
	private static class Gerenciador implements Observable {
		private HashMap<Integer, Observer> observers = new  HashMap<Integer, Observer>();
		
		@Override
		public void registraObs(int jogId, Observer observer) {
			observers.put(jogId, observer);			
		}

		@Override
		public void removeObs(int jogId) {
			observers.remove(jogId);
		}

		@Override
		public void notificaObs(String evento, Object val) {
			for (int id: observers.keySet()) {
				Observer o = observers.get(id);
				HashMap<String, Boolean> cartas = jbl.getCartasJogador(id, 0);
				switch(evento) {
				case "INIT":
					if (id == observers.size() - 1) {
						return;
					}
					o.update("INIT", jbl.getVez());
					break;
				case "FINALIZA_TURNO":
					o.update("DAR_CARTAS", cartas);
					List<Integer> jf = (List<Integer>) val;
					if (id != observers.size() - 1) {
						o.update("FINALIZA_TURNO", jf.get(id));
					}
					jbl.setVez(0);
					break;
				case "VEZ":
					if (id == observers.size() - 1) {
						return;
					}
					int[] value = { jbl.getVez(), jbl.getSomaCartasJogador(id, 0) };
					o.update("VEZ", value);
					break;
				case "DAR_CARTAS":
//					HashMap<String, Boolean> cartas = jbl.getCartasJogador(id, 0);
					
					o.update("DAR_CARTAS", cartas);
					ger.notificaObs("VEZ", null);
					
					break;
				case "FICHA_CLICK":
					if (id == observers.size() - 1) {
						return;
					}
					int[] vezEficha = { jbl.getVez(), (int) val };
					o.update("FICHA_CLICK", vezEficha);
					break;
				case "DEALER_OPEN":
					if (id == observers.size() - 1) {
						o.update("DEALER_OPEN", cartas);
					}
					
					break;
				default:
					System.out.println("Erro fatal recebendo mensagem na Main! Tipo de evento '" + evento + "' nao reconhecido.");
					System.exit(1);
				}
			}
		}
	}
}
