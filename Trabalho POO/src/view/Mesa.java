package view;

import models.JogoBlackjack;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mesa extends Frame {
	//public JogoBlackjack jbl = JogoBlackjack.getInstancia();
	//private List<Jogadores> jogs = new ArrayList<Jogador>();
	public Mesa() {
		super("Jogo de Blackjack");
	}
	
	public static void main(String[] args){
		Mesa m = new Mesa();
		m.display();
		m.setVisible(true);
	}
	
	public void display() {
		setSize(400, 400);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
	
//   @Override
//   public void paint(Graphics g) {
//      Graphics2D g2 = (Graphics2D)g;
//      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//         RenderingHints.VALUE_ANTIALIAS_ON);
//      Font font = new Font("Serif", Font.PLAIN, 24);
//      g2.setFont(font);
//      g2.drawString("Welcome to TutorialsPoint", 50, 70); 
//   }
}
