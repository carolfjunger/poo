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
		
		// tam-1 para excluir o dealer
		for (int i=0; i< tam - 1; i++) {
			int numFichas = jf.get(i);
			int id = jID.get(i);
			List<String> cartas = jbl.getCartasJogador(id, 0);
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
			int vez = jbl.getVez();
			int proxVez = vez + 1;
			int totalDeJogadores = jbl.getIDJogadores().size();
			List<Integer> jf;

			switch(evento.toUpperCase()) {
			case "DEAL":
				int fichasApostadas = (int) val;
				jbl.colheAposta(jbl.getVez(), fichasApostadas);
				
				if(proxVez >= totalDeJogadores - 1) {
					jbl.darCartas(0);
					jbl.setMaoCorrente(0);
					jbl.setVez(0);
					System.out.println("DEAL: SETANDO PROX VEZ: 0");
					ger.notificaObs("DAR_CARTAS", 0);
				} else {
					jbl.setVez(proxVez);
					System.out.println("DEAL: SETANDO PROX VEZ:" + proxVez);
					ger.notificaObs("INIT", null);
				}
				break;
			case "HIT":
				int indMao = (int) val;
				int resultado = jbl.hit(vez, indMao);
				ger.notificaObs("DAR_CARTAS", indMao);
				ger.notificaObs("HIT", resultado);
				
				// se quebrou ou obteve 21...
				if (resultado != 0) {
					System.out.println("Voce quebrou ou obteve 21, passando a vez para:" + proxVez);
					at.update("STAND", null);
				}
				break;
			case "DOUBLE":
				jf = jbl.getApostasJogadores();
				vez = jbl.getVez();
				int valFichas = jf.get(vez);
				jbl.colheAposta(vez, valFichas);
				
				ger.notificaObs("ATUALIZA_FICHA", valFichas);
				at.update("HIT", val);
				break;
			case "STAND":
				int mc = jbl.getMaoCorrente();
				//System.out.println("SETANDO MAO CORRENTE:" + );
				int qtdMao = jbl.getQtdMaosJogador(vez);
				if ( mc < qtdMao - 1 ) {
					System.out.println("SETANDO MAO CORRENTE:" + (mc + 1));
					jbl.setMaoCorrente( mc + 1 );
					ger.notificaObs("VEZ", null);
					return;
				}
				
				jbl.setMaoCorrente(0);
				if(proxVez >= totalDeJogadores - 1) {
					System.out.println("Stand: Finalizando turno");
					jbl.abreMaoDealer();
					
					ger.notificaObs("DEALER_OPEN", null);
					at.update("FINALIZA_TURNO", null);
				} else {
					System.out.println("STAND: SETANDO PROX VEZ:" + proxVez);
					jbl.setVez(proxVez);
					ger.notificaObs("VEZ", null);
				}
				break;
			case "SPLIT":
				indMao = (int) val;
				vez = jbl.getVez();
				jf = jbl.getFichasJogadores();
				valFichas = jf.get(vez);
				
				int indNova = jbl.split(vez, indMao);
				
				JanelaJogador jg = new JanelaJogador(vez, valFichas, indNova, null, at);
				ger.registraObs(vez, jg);
				
				Point p = new Point(400*indNova, 820);
				jg.setLocation(p);
				jg.setVisible(true);
				
				ger.notificaObs("DAR_CARTAS", null);
				break;
			case "FICHA_CLICK":
				ger.notificaObs("FICHA_CLICK", val);
				break;
			case "FINALIZA_TURNO":
				List<Integer> vencedores = jbl.finalizaRodada();
				jf = jbl.getFichasJogadores();
				ger.notificaObs("FINALIZA_TURNO", jf);
				break;
			case "NOVA_RODADA":
				ger.notificaObs("LIMPAR_CARTAS", null);
				ger.notificaObs("INIT", null);
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
		public void notificaObs(String evento, Object val) {
			for (int id: observers.keySet()) {
				Observer o = observers.get(id);
				List<String> cartas = jbl.getCartasJogador(id, 0); // TODO: split
				switch(evento) {
				case "INIT":
					o.update("INIT", jbl.getVez());
					break;
				case "FINALIZA_TURNO":
					List<Integer> jf = (List<Integer>) val;
					o.update("FINALIZA_TURNO", jf.get(id));
					jbl.setVez(0);
					jbl.setMaoCorrente(0);
					break;
				case "DAR_CARTAS":	
					//int indMao = (int) val;
					for (int i = 0; i < jbl.getQtdMaosJogador(id); i++) {
						cartas = jbl.getCartasJogador(id, i);
						o.update("DAR_CARTAS", cartas);
					}
					// fallthrough
				case "VEZ":
					boolean prim = cartas.size() == 2 && jbl.getQtdMaosJogador(jbl.getVez()) == 1;
					int vezInicial =  prim ? 1 : 0;
					int mc = jbl.getMaoCorrente();
					int vez = jbl.getVez();
					int soma = jbl.getSomaCartasJogador(vez, mc); // TODO: split
					
					int[] value = { vez, soma, vezInicial, mc}; // TODO: split
					o.update("VEZ", value);
//					for (int i = 0; i < jbl.getQtdMaosJogador(id); i++) {
//
//					}
//					
					if (id == vez && prim && soma == 21) {
						at.update("STAND", null);
					}
					
					break;
				case "HIT":
					if (id == jbl.getVez())
						o.update("HIT", val);
					break;
				case "LIMPAR_CARTAS":
					o.update(evento, null);
					break;
				case "FICHA_CLICK":
					// se o jogador ja tiver uma mao
					// nao queremos mandar esse evento
					if (jbl.getCartasJogador(id, 0).size() != 0) {
						continue;
					}
					
					o.update("ATUALIZA_FICHA", new int[]{ jbl.getVez(), (int) val });
					break;
				case "ATUALIZA_FICHA":
					o.update("ATUALIZA_FICHA", new int[]{ jbl.getVez(), (int) val });
					break;
				case "DEALER_OPEN":
					if (id == observers.size() - 1)
						o.update("DEALER_OPEN", cartas);
					
					break;
				default:
					System.out.println("Erro fatal mandando mensagem na Main! Tipo de evento '" + evento + "' nao reconhecido.");
					System.exit(1);
				}
			}
		}
		
		@Override
		public void registraObs(int jogId, Observer observer) {
			observers.put(jogId, observer);			
		}

		@Override
		public void removeObs(int jogId) {
			observers.remove(jogId);
		}
		
	}
}
