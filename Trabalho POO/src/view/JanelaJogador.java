package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JanelaJogador extends Janela {
	private int fichas = 0;
	private int indMao = 0;
	HashMap<String, Boolean> cartas;
	
	public JanelaJogador(String titulo, int fichas, int indMao, HashMap<String, Boolean> cartas) {
		super(titulo);
		this.fichas = fichas;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		PainelImagem pi = new PainelImagem(this.assets.get( "9c" ));
		JLabel lFichas = new JLabel();
		
		lFichas.setText("Fichas: " + Integer.toString(fichas));
        panel.add(lFichas);
        panel.add(pi);
        
        this.getContentPane().add(panel);
	}
	
//	public void repaint(Graphics g) {
//		Graphics2D g2 = (Graphics2D)g;
//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//				RenderingHints.VALUE_ANTIALIAS_ON);
//  
//		// desenhar imagem
//		Image img = this.assets.get("9c");
//		System.out.println(img);
//		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
//	}
	
	@Override
	public void carregarAssets() {
		String imageURL;
		Image image;
		
		String baseURL = "../Imagens/";
		String nomes[] = {"a", "t", "j", "q", "k"};
		String naipes[] = {"c", "d", "h", "s"};
		
		// cartas numericas de todos os naipes
		for (int i=2; i<10; i++) {
			for (String n: naipes) {
				String nome = Integer.toString(i) + n;
				Image imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".gif");
				this.assets.put(nome, imagem);
			}
		}
		
		for (String nom: nomes) {
			for (String nai: naipes) {
				String nome = nom + nai;
				Image imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".gif");
				this.assets.put(nome, imagem);
			}
		}
	}
}
