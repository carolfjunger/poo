package controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		for (int i = 0; i < numJogadores; i++) {
			
		}
	}
}
