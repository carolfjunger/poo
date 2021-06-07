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

public class JanelaJogador extends Janela implements Observer {
	private HashMap<String, Boolean> cartas;
	private ArrayList<PainelImagem> imagens = new ArrayList<PainelImagem>();
	private Observer obs;
	
	private int fichas = 0;
	private int aposta = 0;
	private int indMao = 0;
	private int indJogador = 0;
	private final int apostaMinima = 20;
	private final int wCarta = 73; //px - largura da imagem da carta
	private final int hCarta = 97; //px - altura da imagem da carta
	
	// componentes
	private JButton deal = new JButton("Deal");
	private JButton stand = new JButton("Stand");
	private JButton hit = new JButton("Hit");
	private JButton dbl = new JButton("Double");
	private JButton split = new JButton("Split");
	
	JLabel vezStatus = new JLabel();
	
	public JanelaJogador(String titulo, int fichas, int indMao, HashMap<String, Boolean> cartas, Observer obs) {
		super(titulo);
		this.fichas = fichas;
		this.cartas = cartas;
		this.obs = obs;
		// TODO: fazer isso de forma mais correta
		this.indJogador = Integer.parseInt(titulo);
		
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
//		JButton deal = new JButton("Deal");
//		JButton stand = new JButton("Stand");
//		JButton hit = new JButton("Hit");
//		JButton dbl = new JButton("Double");
//		JButton split = new JButton("Split");
		
		Dimension ps = stand.getPreferredSize();
		int width = (int) ps.getWidth();
		JButton btns[] = {deal, stand, hit, dbl, split};
		for (int j = 0; j < btns.length; j++) {
			JButton jb = btns[j];
			jb.setSize(ps);
			jb.setLocation(2 + (width * j), 32);
			jb.setEnabled(false);
			
			jb.addActionListener( (evt) -> {
				String txt = jb.getText().toUpperCase();
				switch(txt) {
				case "DEAL":
					this.vezStatus.setText("Apostando");
					this.deal.setEnabled(false);
					//obs.update("")
					break;
				default:
		    		//System.out.println("Erro fatal enviando evento na janela jogador, evento nao reconhecido!");
		    		//System.exit(1);	
				}
				obs.update(txt, null);
			});
			panel.add(jb);
		}
		
		// valor total das fichas
		JLabel lFichas = new JLabel();
		lFichas.setText("Fichas: " + Integer.toString(fichas));
		lFichas.setSize(lFichas.getPreferredSize());
		lFichas.setLocation(this.getWidth() - 94, this.getHeight() - 60);
        
		
		// Mostra a vez do jogador
//		JLabel vezStatus = new JLabel();
//		if (this.fichasApostadas > apostaMinima ) {
//			vezStatus.setText("Fichas Apostada: " + Integer.toString(fichas));
//		} else {
//			vezStatus.setText("Fa�a sua aposta");
//		}
		this.vezStatus.setText("Aguarde sua vez");
		this.vezStatus.setSize(vezStatus.getPreferredSize());
		this.vezStatus.setLocation(this.getWidth() - 125, this.getHeight() - 80);
		
		
		// botando tudo no painel principal
		panel.add(lFichas);
		panel.add(this.vezStatus);
        
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

            String text = "Jogador #" + (indJogador + 1);
            g.drawString(text, 20, 20);
        }

    }
    
    @Override 
    public void update(String evento, Object val) {
    	int vez;
    	switch (evento) {
    	case "INIT":
    		vez = (int) val;
    		if (vez == this.indJogador) {
//    			JLabel vezStatus = new JLabel();
    			if (this.aposta > apostaMinima ) {
    				this.vezStatus.setText("Fichas Apostada: " + Integer.toString(fichas));
    			} else {
    				this.vezStatus.setText("Fa�a sua aposta");
    			}
        		this.deal.setEnabled(true);
        		this.repaint();
    		}

    		break;
    	case "VEZ":
    		vez = (int) val;
    		// falta verificar se a aposta � maior que a aposta minima
    		if (vez == this.indJogador) {
        		this.stand.setEnabled(true);
        		this.dbl.setEnabled(true);
        		this.hit.setEnabled(true);
        		this.split.setEnabled(true);
        		this.vezStatus.setText("� a sua vez");
    		} else {
    			this.vezStatus.setText("Aguarde a sua vez");
    		} 		
    		this.repaint();
    		break;
    	case "DAR_CARTAS":
    		HashMap<String, Boolean> cartas = (HashMap<String, Boolean>) val;
    		this.cartas = cartas;
    		this.repaint();
    		break;
    	default:
    		System.out.println("Erro fatal recebendo evento na janela jogador, evento nao reconhecido!");
    		System.exit(1);
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
