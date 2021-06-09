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
	
	private JLabel lFichas = new JLabel();
	private JLabel lAposta = new JLabel();
	private JLabel vezStatus = new JLabel();
	private JLabel somaCartas = new JLabel();
	
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
					obs.update(txt, this.aposta);
					break;
				case "STAND":
					this.vezStatus.setText("Apostando");
					this.deal.setEnabled(false);
	        		this.stand.setEnabled(false);
	        		this.dbl.setEnabled(false);
	        		this.hit.setEnabled(false);
	        		this.split.setEnabled(false);
					obs.update(txt, null);
					break;
				default:
					obs.update(txt, null);
		    		//System.out.println("Erro fatal enviando evento na janela jogador, evento nao reconhecido!");
		    		//System.exit(1);	
				}
				
			});
			panel.add(jb);
		}
		
		// valor total das fichas

		this.lFichas.setText("Fichas: " + Integer.toString(fichas));
		this.lFichas.setSize(this.lFichas.getPreferredSize());
		this.lFichas.setLocation(this.getWidth() - 94, this.getHeight() - 60);
		
		// valor total das fichas apostadas

		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
		this.lAposta.setSize(this.lAposta.getPreferredSize());
		this.lAposta.setLocation(this.getWidth() - 94, this.getHeight() - 80);
        
		
		// Mostra a vez do jogador
		this.vezStatus.setText("Aguarde sua vez");
		this.vezStatus.setSize(vezStatus.getPreferredSize());
		this.vezStatus.setLocation(this.getWidth() - 125, this.getHeight() - 100);
		
		// Mostra a soma das cartas do jogador
		this.somaCartas.setText("Somatório das cartas: 0");
		this.somaCartas.setSize(somaCartas.getPreferredSize());
		this.somaCartas.setLocation(this.getWidth() - 175, this.getHeight() - 120);
		
		
		// botando tudo no painel principal
		panel.add(this.lFichas);
		panel.add(this.lAposta);
		panel.add(this.vezStatus);
		panel.add(this.somaCartas);
        
        this.getContentPane().add(panel);
	}
	
	public void addFichaApostada(int ficha) {
		if(this.fichas - ficha >= 0) {
			this.aposta += ficha;
			this.fichas -= ficha;
		}

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
        	int somaCartas = 0;
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
    			if (this.aposta > apostaMinima ) {
    				this.vezStatus.setText("Fichas Apostada: " + Integer.toString(fichas));
    				this.deal.setEnabled(true);
    			} else {
    				this.vezStatus.setText("Faça sua aposta");
    				this.deal.setEnabled(false);
    			}
        		
        		this.repaint();
    		}

    		break;
    	case "VEZ":
    		int [] vezEsoma = (int[]) val;
    		vez = (int) vezEsoma[0];
    		int sumCarta = (int) vezEsoma[1];
    		// falta verificar se a aposta é maior que a aposta minima
    		if (vez == this.indJogador) {
        		this.stand.setEnabled(true);
        		this.dbl.setEnabled(true);
        		this.hit.setEnabled(true);
        		this.split.setEnabled(true);
        		this.vezStatus.setText("É a sua vez");
    		} else {
    			this.vezStatus.setText("Aguarde a sua vez");
    		}
    		this.vezStatus.setSize(vezStatus.getPreferredSize());
    		this.somaCartas.setText("Somatório das cartas:" + Integer.toString(sumCarta));
    		this.somaCartas.setSize(somaCartas.getPreferredSize());
    		this.repaint();
    		break;
    	case "DAR_CARTAS":
    		HashMap<String, Boolean> cartas = (HashMap<String, Boolean>) val;
    		this.cartas = cartas;
    		this.repaint();
    		break;
       	case "FICHA_CLICK":
       		int [] vezEFicha = (int[]) val;
       		vez = vezEFicha[0];
       		int ficha = vezEFicha[1];
       		if (vez == this.indJogador) {
           		this.addFichaApostada(ficha);
           		if(this.aposta >= apostaMinima) {
           			this.deal.setEnabled(true);
           		}
           		this.lFichas.setText("Fichas: " + Integer.toString(this.fichas));
           		this.lFichas.setSize(this.lFichas.getPreferredSize());
        		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
        		this.lAposta.setSize(this.lAposta.getPreferredSize());
       		}

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
