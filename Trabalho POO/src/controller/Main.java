package controller;

import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;
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

		JogoBlackjack jbl = JogoBlackjack.getInstancia();
		jbl.setJogadores(numJogadores);
		List<String> jID = jbl.getIDJogadores();
		List<Integer> jf = jbl.getFichasJogadores();
		int tam = jID.size();
		
		
		Boolean inicia = jbl.inicializa(5);
		
		if(inicia != true) {
			System.out.print("erro");
		}
		
		List<JanelaJogador> janelasJogadores = new ArrayList<JanelaJogador>(); 
		
		for (int i=0; i<tam; i++) {
			int numFichas = jf.get(i);
			HashMap<String, Boolean> cartas = jbl.getCartasJogador(i, 0);
			String jogId = jID.get(i);
			
//			HashMap<String, Integer> apostadores = this.apostadores;
			
			ActionListener buttonDealAction = new ActionListener() {
			     public void actionPerformed(ActionEvent ae) {
//			    	 apostadores.put(jogId , 0);
			      }
			    };
			    
			JanelaJogador jg = new JanelaJogador(jogId, numFichas, 0, cartas, buttonDealAction);
			
			Point p = new Point(i*400, 420);
			jg.setLocation(p);
			jg.setVisible(true);
			janelasJogadores.add(jg);
		}
		
		HashMap<String, Integer> apostadores = new HashMap<String, Integer>();
		
		ActionListener getApostas = new ActionListener() {
		     public void actionPerformed(ActionEvent ae) {
		    	 for(JanelaJogador jg: janelasJogadores) {
		    		 if(jg.verificaSeEstaApostando()) {
		    			 apostadores.put(jg.getTitle(), jg.getFichasApostadas());
		    		 }
		    	 }
		    	 System.out.print(apostadores);
		      }
		    };
		
		Janela jBanca = new JanelaBanca("Banca", getApostas);
		jBanca.setVisible(true);
	}
}
