package view;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.Comando;

public class JanelaJogador extends Janela {
	private HashMap<String, Boolean> cartas;
	private ArrayList<PainelImagem> imagens = new ArrayList<PainelImagem>();
	private Observer obs;
	
	private int fichas = 0;
	private int indMao = 0;
	private final int wCarta = 73; //px - largura da imagem da carta
	private final int hCarta = 97; //px - altura da imagem da carta
	
	public JanelaJogador(String titulo, int fichas, int indMao, HashMap<String, Boolean> cartas, Observer obs) {
		super(titulo);
		this.fichas = fichas;
		this.cartas = cartas;
		this.obs = obs;
		
		JPanel panel = new PaintPanel();
		panel.setLayout(null);
		
		// cartas
		int i = 0;
		for (String c: cartas.keySet()) {
			PainelImagem pim = new PainelImagem(this.assets.get( c ));
			if (cartas.get(c) == true) {
				pim = new PainelImagem(this.assets.get( "b" ));
			}
			
			pim.setBounds(8 + (i * wCarta), this.getHeight() - 140, wCarta, hCarta);
			imagens.add(pim);
			i += 1;
		}
		
		// botoes
		JButton deal = new JButton("Deal");
		JButton stand = new JButton("Stand");
		JButton hit = new JButton("Hit");
		JButton dbl = new JButton("Double");
		JButton split = new JButton("Split");
		
		Dimension ps = stand.getPreferredSize();
		int width = (int) ps.getWidth();
		JButton btns[] = {deal, stand, hit, dbl, split};
		for (int j = 0; j < btns.length; j++) {
			JButton jb = btns[j];
			jb.setSize(ps);
			jb.setLocation(20 + (width * j), 20);
			jb.setEnabled(false);
			
			jb.addActionListener( (evt) -> {
				String txt = jb.getText();
				obs.update(txt, null);
			});
			panel.add(jb);
		}
		
		// valor total das fichas
		JLabel lFichas = new JLabel();
		lFichas.setText("Fichas: " + Integer.toString(fichas));
		lFichas.setSize(lFichas.getPreferredSize());
		lFichas.setLocation(this.getWidth() - 94, this.getHeight() - 60);
        
		// botando tudo no painel principal
		panel.add(lFichas);
        
        this.getContentPane().add(panel);
	}
	
//	public getID() {
//		return this.getTitle();
//	}

	// pintar as fichas e as cartas
    private class PaintPanel extends JPanel {

        @Override
		public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	int padding = 4;
        	
        	// desenhar cartas
        	int cInd = 0;
    		for (String c: cartas.keySet()) {
    			Image img = assets.get( c );
    			if (img != null) {
    				int width = img.getWidth(this);
    				int height = img.getHeight(this);
    				g.drawImage(img, padding + (wCarta * cInd), getHeight() - (hCarta + padding), width, height, this);
    				cInd += 1;
    			}
    		}

            String text = "Look ma, no hands";
            g.drawString(text, 20, 20);
        }

    }
	
	@Override
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
	}
}
