// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import models.*;
import view.*;

public class Main {
	
	private static Atualizador at;
	private static Gerenciador ger;
	private static JogoBlackjack jbl;
	
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
    		
    		iniciaJogo(numJogadores, null);
		}, () -> {
			HashMap<Integer, Integer> jogoSalvo = carregamento();
			
			iniciaJogo(jogoSalvo.size(), jogoSalvo);
		}) ;
		
//		iniciaJogo(2);
		
		ji.setVisible(true);
		ji.setLocationRelativeTo(null);
	}
	
	public static void iniciaJogo(int numJogadores, HashMap<Integer, Integer> jogadoresSave) {
		at = new Atualizador();
		ger = new Gerenciador();
		
		jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		if(jogadoresSave ==  null) {
			jbl.inicializa(2);
		} else {
			jbl.inicializaBySalvamento(2, jogadoresSave);
		}
		
		
		List<Integer> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		JanelaBanca jBanca = new JanelaBanca("Banca", jID.get(tam-1), at);
    	ArrayList<Janela> jJogador = new ArrayList<Janela>();
    	
		ger.registraObs(jBanca);
		
		// tam-1 para excluir o dealer
		for (int i=0; i< tam - 1; i++) {
			int numFichas = jf.get(i);
			int id = jID.get(i);
			List<String> cartas = jbl.getCartasJogador(id, 0);
			JanelaJogador jg = new JanelaJogador(id, numFichas, 0, cartas, at);
			
			// registrar janela jogador como observador
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
        	//ger.notificaObs("INIT", null);
	    });
	}
	
	public static HashMap<Integer, Integer> carregamento()  {
        
        int jqtd,id,ficha,qtdCartasUsadas;
        HashMap<Integer, Integer> jogadores = new HashMap<Integer, Integer>();
        
        File save = new File("Save.txt"); 
        System.out.println(save);
        Scanner s = null;
		try {
			s = new Scanner(save);
		} catch (FileNotFoundException e) {
			System.out.println("Erro você não tem nenhum jogo salvo ainda");
			System.exit(0);
		}
       
        

        jqtd = s.nextInt();// o numero de jogadores
        qtdCartasUsadas = s.nextInt();//cartas usadas
        
        for(int i = 0;i < jqtd - 1;i++) {
        	id = s.nextInt();
        	ficha = s.nextInt();
        	jogadores.put(id, ficha);
        } //listas de ids e fichas de jogador
        

	
        
        
        /****/

        s.close();
        System.out.println("Jogadores = "+jqtd);
        System.out.println("jogs + fichas ="+jogadores);
        System.out.println("cartas usadas = "+qtdCartasUsadas);
        
        
        
        /*******/
        
         return jogadores;
    }
	
	public static void salvamento()  {

//		JogoBlackjack jbl = JogoBlackjack.getInstancia();

		List<Integer> jID = jbl.getIDJogadores();//id jogadores
		List<Integer> jf = jbl.getFichasJogadores();//ficha de cada jogador

		int tam = jID.size() -1;




		List<Integer> jval = jbl.pegaVal();//numero de jogadores,fichasmesa,vez,qtdCartasUsadas

        /*****/
        //forma de escrever
        File  Save = new File("Save.txt");
        FileWriter w = null;
		try {
			w = new FileWriter(Save);
		} catch (IOException e) {
			System.out.println("Erro ao salvar o jogo");
			e.printStackTrace();
			System.exit(0);
		}
        PrintWriter writer = new PrintWriter(w);





        /******/
        //escrever
        for(int k = 0; k < 2 ;k++) {
        	writer.write(jval.get(k) + "\n");

        }//jogadores, cartas usadas

        for(int i = 0; i < tam ;i++) {
        	writer.write(jID.get(i) + " ");
        	writer.write(jf.get(i) + "\n");


        }//id, fichas jogador


        System.out.println("Salvou");

        writer.close();  
    }
	
	
	private static class Atualizador implements Observer {
		@Override
		public void update(String evento, Object val) {
			int vez = jbl.getVez();
			int proxVez = vez + 1;
			int totalDeJogadores = jbl.getIDJogadores().size();
			List<HashMap<Integer, Integer>> aj;

			switch(evento.toUpperCase()) {
			case "DEAL":
				int fichasApostadas = (int) val;
				jbl.colheAposta(jbl.getVez(), 0, fichasApostadas);
				
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
				
				int soma = jbl.getSomaCartasJogador(vez, indMao);
				int tam = jbl.getTamMaoJogador(vez, indMao);
				boolean temAs = (tam == 1) && (soma == 11);
				
				// se quebrou ou obteve 21...
				// ou se fez um split com AS
				// passa a vez
				if (resultado != 0 || temAs) {
					at.update("STAND", null);
				}
				break;
			case "DOUBLE":
				aj = jbl.getApostasJogadores();
				vez = jbl.getVez();
				indMao = (int) val;
				int valFichas = 0;
				if (aj.size() > vez && aj.get(vez).containsKey(indMao)) {
					valFichas = aj.get(vez).get(indMao);
					System.out.println("DOBRANDO:" + valFichas);
				}
					
				jbl.colheAposta(vez, indMao, valFichas);
				
				ger.notificaObs("ATUALIZA_FICHAS", null);
				at.update("HIT", val);
				break;
			case "STAND":
				int mc = jbl.getMaoCorrente();
				int qtdMao = jbl.getQtdMaosJogador(vez);
				if ( mc < qtdMao - 1 ) {
					System.out.println("SETANDO MAO CORRENTE:" + (mc + 1));
					System.out.println("QTD MAO:" + qtdMao);
					jbl.setMaoCorrente( mc + 1 );
					ger.notificaObs("VEZ", null);
					return;
				}
				
				if(proxVez >= totalDeJogadores - 1) {
					System.out.println("Stand: Finalizando turno");
					jbl.abreMaoDealer();
					
					ger.notificaObs("DEALER_OPEN", null);
					at.update("FINALIZA_TURNO", null);
				} else {
					System.out.println("STAND: SETANDO PROX VEZ:" + proxVez);
					jbl.setMaoCorrente(0);
					jbl.setVez(proxVez);
					ger.notificaObs("VEZ", null);
				}
				break;
			case "SPLIT":
				indMao = (int) val;
				vez = jbl.getVez();
				List <Integer> jf = jbl.getFichasJogadores();
				valFichas = jf.get(vez);
				
				int indNova = jbl.split(vez, indMao);
				
				JanelaJogador jg = new JanelaJogador(vez, valFichas, indNova, null, at);
				ger.registraObs(jg);
				
				int offset = ger.observers.size() - 2;
				Point p = new Point(400*offset, 420);
				jg.setLocation(p);
				jg.setVisible(true);
				
				ger.notificaObs("DAR_CARTAS", null);
				
				valFichas = jbl.getApostasJogadores().get(vez).get(indMao);
				jbl.colheAposta(vez, indNova, valFichas);				
				ger.notificaObs("ATUALIZA_FICHAS", null);
				break;
			case "SURRENDER":
				int ind = (int) val;
				jbl.surrender(ind);
				at.update("STAND", null);
			case "FICHA_CLICK":
				ger.notificaObs("PRE_APOSTA", val);
				break;
			case "FINALIZA_TURNO":
				List<HashMap<Integer, Integer>> res = jbl.finalizaRodada();
				jf = jbl.getFichasJogadores();
				
				Object[] inf = {jf, res};
				ger.notificaObs("FINALIZA_TURNO", inf);
				break;
			case "NOVA_RODADA":
				ger.notificaObs("LIMPAR_CARTAS", null);
				ger.notificaObs("INIT", null);
				
				SwingUtilities.invokeLater(() -> {
					// remover observers criados por splits	
					while (ger.observers.size() > jbl.getQtdJogadores())
						ger.observers.remove(ger.observers.size() - 1);
					
					// remover pessoas com poucas fichas
					List<Integer> jogF = jbl.getFichasJogadores();
					List<Integer> removidos = new ArrayList<Integer>();
					int apostaMin = 20;
					for (int i=0; i<jogF.size(); i++) {
						if (jogF.get(i) < apostaMin)
							removidos.add(i);
					}
					
					for (int r: removidos) {
						at.update("QUIT", r);
					}
				});
				break;
			case "QUIT":
				jbl.removeJogador((int) val);
				ger.notificaObs("QUIT", val);
				break;
			case "ENCERRAR_JOGO":
				System.exit(0);
				break;
			case "SALVAR_JOGO":
				salvamento();
				break;
			default:
				System.out.println("Erro fatal! Tipo de evento '" + evento + "' nao reconhecido.");
				System.exit(1);
			}
		}

		@Override
		public int getInd() { return -1; }
		@Override
		public void setInd(int ind) {}
	}
	
	private static class Gerenciador implements Observable {
		protected List<Observer> observers = new ArrayList<Observer>();
		
		@Override
		public void notificaObs(String evento, Object val) {
			for (int io=0; io < this.observers.size(); io++) {
				Observer o = this.observers.get(io);
				int id = o.getInd();
				System.out.println(evento);
				List<String> cartas = null;
				switch(evento) {
				case "INIT":
					o.update("INIT", jbl.getVez());
					
					break;
				case "FINALIZA_TURNO":
					Object[] objs = (Object[]) val;
					List<Integer> jf = (List<Integer>) objs[0];
					List<HashMap<Integer, Integer>> resultados = (List<HashMap<Integer, Integer>>) objs[1];
					Object[] inf = {jf.get(id), resultados};
					o.update("FINALIZA_TURNO", inf);
					jbl.setVez(0);
					jbl.setMaoCorrente(0);

					break;
				case "DAR_CARTAS":
					for (int i = 0; i < jbl.getQtdMaosJogador(id); i++) {
						cartas = jbl.getCartasJogador(id, i);
						int soma = jbl.getSomaCartasJogador(id, i);
						Object[] infC = {cartas, soma, i};
						o.update("DAR_CARTAS", infC);
					}
					// fallthrough
				case "VEZ":
					int mc = jbl.getMaoCorrente();
					int vez = jbl.getVez();
					int soma = jbl.getSomaCartasJogador(vez, mc);
					
					cartas = jbl.getCartasJogador(vez, mc);
					boolean prim = cartas.size() == 2 && jbl.getQtdMaosJogador(jbl.getVez()) == 1;
					int vezInicial =  prim ? 1 : 0;
					
					int[] value = { vez, soma, vezInicial, mc};
					o.update("VEZ", value);
					if (id == vez && prim && soma == 21) {
						at.update("STAND", null);
					}
					
					break;
				case "HIT":
					int resultado = (int) val;
					int mao = jbl.getMaoCorrente();
					int[] info = {resultado, mao};
					if (id == jbl.getVez())
						o.update("HIT", info);
					break;
				case "LIMPAR_CARTAS":
					o.update(evento, null);
					break;
				case "PRE_APOSTA":
					vez = jbl.getVez();
					// se o jogador ja tiver uma mao
					// nao queremos mandar esse evento
					if (jbl.getQtdMaosJogador(vez) > 0 && jbl.getCartasJogador(vez, 0).size() != 0) {
						continue;
					}
					o.update("PRE_APOSTA", new int[]{ jbl.getVez(), jbl.getMaoCorrente(), (int) val });
					break;
				case "ATUALIZA_FICHAS":
					jf = jbl.getFichasJogadores();
					List<HashMap<Integer, Integer>> aj = jbl.getApostasJogadores();
					if (id == jbl.getQtdJogadores() - 1)
						break;
						
					for (int i=0; i<jbl.getQtdMaosJogador(id); i++) {
						int fichas = jf.get(id);
						int ap = aj.get(id).get(i);
						o.update("ATUALIZA_FICHAS", new int[]{ fichas, ap, i });
					}
					break;
					
				case "DEALER_OPEN":
					List<String> cartasD = jbl.getCartasJogador(id, 0);
					//if (id == jbl.getN)
					if (id == jbl.getQtdJogadores() - 1) {
						cartas = jbl.getCartasJogador(id, 0);
						soma = jbl.getSomaCartasJogador(id, 0);
						Object[] infC = {cartas, soma, 0};
						o.update("DAR_CARTAS", infC);
					}

					o.update("DEALER_OPEN", null);
					
					break;
				case "QUIT":
					int iJog = (int) val;
					
					o.update("QUIT", iJog);
					if (id != iJog)
						break;

					SwingUtilities.invokeLater( () -> {
						// remover a janela cujo jogador apertou "QUIT"
						for (int k=0; k<this.observers.size(); k++) {
							int ind = this.observers.get(k).getInd();
							if (ind == iJog) {
								this.observers.remove(k);
								break;
							}
						}
						
						// mover a banca pro final
						// a banca representa o jogador dealer
						// precisamos que esse jogador tenha maior indice (por convencao)
						Observer rem = null;
						for (int k=0; k<this.observers.size(); k++) {
							Observer ob = this.observers.get(k);
							if (ob.getInd() == jbl.getQtdJogadores())
								rem = this.observers.remove(k);
						}
						this.observers.add(rem);
						
						// reajustar os indices dos observers
						// para ficar sem buracos
						
						for (int k=0; k<this.observers.size(); k++) {
							Observer ob = this.observers.get(k);
							ob.setInd(k);
						}
					});
					
					break;
				default:
					System.out.println("Erro fatal mandando mensagem na Main! Tipo de evento '" + evento + "' nao reconhecido.");
					System.exit(1);
				}
			}
		}
		
		@Override
		public void registraObs(Observer observer) {
			observers.add(observer);			
		}

		@Override
		public void removeObs(int jogInd) {
			observers.remove(jogInd);
		}
		
	}
}
