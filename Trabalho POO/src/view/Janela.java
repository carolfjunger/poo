package view;

import models.JogoBlackjack;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Image;

public class Janela extends JFrame {
	//public JogoBlackjack jbl = JogoBlackjack.getInstancia();
	//private List<Jogadores> jogs = new ArrayList<Jogador>();
	protected HashMap<String, Image> assets = new HashMap<String, Image>();
	private boolean inicializado;
	private int largura = 400;
	private int altura = 400;
	
	public Janela(String titulo) {
		super(titulo);
		carregarAssets();
		
		Dimension tamMax = new Dimension(1200, 700);
		this.setMaximumSize(tamMax);

		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.display();
	}
	
	public Image getAsset(String chave) {
		return this.assets.get(chave);
	}
	
	public void setDimensao(int largura, int altura) {
		this.largura = largura;
		this.altura = altura;
	}
	
	public void carregarAssets() {};
	
	public void display() {
		
		setSize(this.largura, this.altura);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
}
