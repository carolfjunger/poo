package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JanelaJogador extends Janela {
	private int fichas = 0;
	private int indMao = 0;
	private int fichasApostadas = 0;
	private boolean estaApostando = false;
	HashMap<String, Boolean> cartas;
	final private int APOSTA_MIN = 20;
	
	public JanelaJogador(String titulo, int fichas, int indMao, HashMap<String, Boolean> cartas) {
		super(titulo);
		this.fichas = fichas;
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		JButton deal = new JButton("DEAL");
		deal.setEnabled(false);
		
		deal.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent ae) {
        		if(fichasApostadas > APOSTA_MIN) {
        			estaApostando = true;
        			deal.setEnabled(false);
	    		} else {
	    			estaApostando = false;
	    			deal.setEnabled(true);
	    		}
		      }
		    }
		  );

		JLabel lFichas = new JLabel();
		
		lFichas.setText("Fichas totais: " + Integer.toString(this.fichas));
        
		JLabel lFichasApostadas = new JLabel();
		
		lFichasApostadas.setText("Fichas apostadas: " + Integer.toString(this.fichasApostadas));
		
		String fichasArray[] = {"1", "5", "10", "20", "50", "100"};
		
		JanelaJogador jogJanela = this;
		for (String ficha: fichasArray) {
			String nome = "ficha " + ficha + "$";
			PainelImagem fi = new PainelImagem(this.assets.get(nome), 0, 0);
			fi.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                	if(!jogJanela.estaApostando) {
                    	jogJanela.addFichaApostada(Integer.parseInt(ficha));
                    	lFichas.setText("Fichas totais: " + Integer.toString(jogJanela.fichas));
                    	lFichasApostadas.setText("Fichas apostadas: " + Integer.toString(jogJanela.fichasApostadas));
                		if(jogJanela.fichasApostadas >= APOSTA_MIN) {
                			deal.setEnabled(true);
        	    		} else {
        	    			deal.setEnabled(false);
        	    		}
                	}

                 }
            });
			panel.add(fi);
			
		}
		
//		System.out.print(cartas);
//		for(String carta: cartas.keySet()) {
//		if(cartas.get(carta)) {
//			
//			}
//				System.out.print(carta);
//			PainelImagem pi = new PainelImagem(this.assets.get(carta));
//			panel.add(pi);
//		}
		
//		if(fichasApostadas > APOSTA_MIN) {
//			System.out.print("oi");
//			deal.setVisible(true);
//		} else {
//			deal.setVisible(false);
//		}
		panel.add(deal);
		panel.add(lFichas);
		panel.add(lFichasApostadas);
        this.getContentPane().add(panel);
	}
	
	public void addFichaApostada(int ficha) {
		if(this.fichas - ficha >= 0) {
			this.fichasApostadas += ficha;
			this.fichas -= ficha;
		}

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
		String fichas[] = {"1", "5", "10", "20", "50", "100"};
		
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
		
		for (String ficha: fichas) {
			String nome = "ficha " + ficha + "$";
			Image imagem = Toolkit.getDefaultToolkit().getImage(baseURL + nome + ".png");
			this.assets.put(nome, imagem);
			
		}
	}
}
