package view;

import models.JogoBlackjack;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

public abstract class Janela extends JFrame {
	protected HashMap<String, Image> assets = new HashMap<String, Image>();
	private boolean inicializado;
	private int largura = 400;
	private int altura = 400;
	
	public Janela(String titulo) {
		super(titulo);
		carregarAssets();
		
		Dimension tamMin = new Dimension(400, 400);
		Dimension tamMax = new Dimension(1200, 700);
		this.setMinimumSize(tamMin);
		this.setMaximumSize(tamMax);
		this.display();
	}
	
	public Image getAsset(String chave) {
		return this.assets.get(chave);
	}
	
	public void display() {
		setSize(this.largura, this.altura);
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
			    SwingUtilities.invokeLater(new Runnable() {
			        @Override
			        public void run() {
			            System.exit(0);
			        }
			    });
			}
		});
	}
	
	public void carregarAssets() {
		String imageURL;
		Image image;
		
		String baseURL = "../Imagens/";
		String nomes[] = {"a", "t", "j", "q", "k"};
		String naipes[] = {"c", "d", "h", "s"};
		
		String nome = "b";
		Image imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".gif");
		this.assets.put(nome, imagem);
		
		// cartas numericas de todos os naipes
		for (int i=2; i<10; i++) {
			for (String n: naipes) {
				nome = Integer.toString(i) + n;
				imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".gif");
				this.assets.put(nome, imagem);
			}
		}
		
		for (String nom: nomes) {
			for (String nai: naipes) {
				nome = nom + nai;
				imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".gif");
				this.assets.put(nome, imagem);
			}
		}
		
		// fundo
		imageURL = "../Imagens/blackjackBKG.png";
		image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.assets.put("fundo", image);
		
		//fichas
		String fichasArr[] = {"ficha1", "ficha5", "ficha10", "ficha20", "ficha50", "ficha100"};
		for (String f: fichasArr) {
			image = Toolkit.getDefaultToolkit().getImage(baseURL + f + ".png");
			this.assets.put(f, image);
		}
		
		// deck 1
		imageURL = "../Imagens/deck1.gif";
		image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.assets.put("deck1", image);
		
		// deck 1
		imageURL = "../Imagens/deck2.gif";
		image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.assets.put("deck2", image);
	};
}
