// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JanelaBanca extends Janela implements Observer {
	
	private HashMap<Integer, Point> fichasPosition = new HashMap<Integer, Point>();
	private List<String> cartas = new ArrayList<String>();
	private int indJogador = 0;
	private int soma = 0;
	private boolean init = false;
	private boolean praBaixo = true;
	private Observer obs;
	
	final int size = 59; // tamanho da ficha
	
	// botoes
	private JButton novaRodada = new JButton("Nova Rodada");
	private JButton salvar = new JButton("Salvar jogo");
	private JButton encerrar = new JButton("Encerrar jogo");
	
	public JanelaBanca(String titulo, int indJogador, Observer obs) {
		super(titulo);
		HashMap<Integer, Point> fichasPosition = this.fichasPosition;
		this.obs = obs;
		this.indJogador = indJogador;
		
		JPanel panel = new PaintPanel();
		panel.setLayout(null);
		
		// botoes
		
		Dimension ps = salvar.getPreferredSize();
		int width = (int) ps.getWidth();
		JButton btns[] = {novaRodada, salvar, encerrar};
		for (int j = 0; j < btns.length; j++) {
			JButton jb = btns[j];
			jb.setSize(ps);
			jb.setLocation( ((int)ps.getWidth() * j), 160);
			jb.setEnabled(false);
			
			jb.addActionListener( (evt) -> {
				String txt = jb.getText().toUpperCase().replace(" ", "_");
				switch(txt) {
				case "NOVA_RODADA":
					this.cartas = new ArrayList<String>();
					obs.update(txt, null);
					break;
				case "SALVAR_JOGO":
					this.salvar.setEnabled(false);
					obs.update(txt, null);
					break;
				case "ENCERRAR_JOGO":
					obs.update(txt, this.indJogador);
					break;
				default:
					obs.update(txt, null);	
				}
				
			});
			panel.add(jb);
		}
		this.getContentPane().add(panel);
		
		this.encerrar.setEnabled(true);
		this.novaRodada.setEnabled(true);
		
		// mouse
		this.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	int x=e.getX();
		        int y=e.getY();
		        for (int key : fichasPosition.keySet()) {
		        	Point fichaPos = fichasPosition.get(key);
		        	if  (fichaPos.x < x && 
	        			(fichaPos.x + size) > x && 
	        			(fichaPos.y + size/2) < y && 
	        			(fichaPos.y + size + size/2) > y &&
		        		init)
		        	{
		        		obs.update("FICHA_CLICK", key);
		        	}
		        }
		    }  
		});
		
		// resize
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent componentEvent) {
				for (int j = 0; j < btns.length; j++) {
					int relX = j - btns.length/2;
					int xc = getWidth()/2;
					JButton jb = btns[j];
					int w = (int) ps.getWidth();
					jb.setLocation( xc + (w * relX) - w/2, 160);
				}
		    }
		});
	}

	// pintar as fichas e as cartas
    private class PaintPanel extends JPanel {

        @Override
		public void paintComponent(Graphics g) {
    		Graphics2D g2 = (Graphics2D)g;
    		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_ON);
      
    		// desenhar imagem
    		Image img = assets.get("fundo");
    		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
    		
    		// deck1
    		img = assets.get("deck1");
    	    int xc = (this.getWidth() - img.getWidth(null)) / 2;
    		g.drawImage(img, xc - 50, 24, this);
    		
    		// deck2
    		img = assets.get("deck2");
    		g.drawImage(img, xc + 50, 24, this);
    		
    		// desenhar fichas
    		String fichasArr[] = {"ficha1", "ficha5", "ficha10", "ficha20", "ficha50", "ficha100"};
    		int fichasValueArr[] = {1, 5, 10, 20, 50, 100};
    		for (int i = 0; i < fichasArr.length; i++) {
    			String name = fichasArr[i];
    			img = assets.get(name);
    			if (img != null) {
    				int width = img.getWidth(this);
    				int height = img.getHeight(this);
    				int x = (width * i);
    				int y = getHeight() - (height + 8);
    				fichasPosition.put(fichasValueArr[i], new Point(x,y));
    				g.drawImage(img, x, y, width, height, this);
    			}
    		}
    		
        	int padding = 4;
        	
        	// desenhar cartas
        	int cInd = 0;
        	final int wCarta = 73; //px - largura da imagem da carta
        	final int hCarta = 97; //px - altura da imagem da carta

    		for (String c: cartas) {
    			img = assets.get( c );
    			
    			if (praBaixo && cInd == 0) {
    				img = assets.get( "b" );
    			}
    			if (img != null) {
    				int width = img.getWidth(this);
    				int height = img.getHeight(this);
    				g.drawImage(img, padding + (wCarta * cInd), getHeight() - (hCarta + padding + 80), width, height, this);
    				cInd += 1;
    			}
    		}
    		
            String text = "Somatorio das cartas:" + (soma == 0 || praBaixo ? "" : Integer.toString(soma));
            g.setColor(Color.WHITE);
            g.fillRect(8, 135, 165, 18);
            g.setColor(Color.BLACK);
            g.drawRect(8, 135, 165, 18);
            g.drawString(text, 14, 149);
        }
    }

	@Override
	public void update(String evento, Object val) {

    	switch (evento) {
	    	case "VEZ":
	    	case "HIT":
	    	case "ATUALIZA_FICHAS":
	    	case "PRE_APOSTA":
	    	case "QUIT":
	    		break;
	    	case "INIT":
	    		this.init = true;
	    		this.soma = 0;
	    		this.praBaixo = true;
	    		this.novaRodada.setEnabled(false);
	    		this.salvar.setEnabled(false);
	    		break;
	    	case "DAR_CARTAS":
	    		this.init = false;
	    		Object[] inf = (Object[]) val;
	    		List<String> cartas = (List<String>) inf[0];
	    		int soma = (int) inf[1];
	    		this.soma = soma;
	    		this.novaRodada.setEnabled(false);

	    		this.cartas = cartas;
	    		break;
	    	case "LIMPAR_CARTAS":
	    		this.cartas = new ArrayList<String>();
	    		this.novaRodada.setEnabled(false);
	    		break;
	    	case "DEALER_OPEN":
	    		this.praBaixo = false;
   		
	    		break;
	    	case "FINALIZA_TURNO":
	    		this.salvar.setEnabled(true);
	    		this.novaRodada.setEnabled(true);
   		
	    		break;
	    	default:
	    		System.out.println(evento);
	    		System.out.println("Erro fatal recebendo evento na janela banca, evento nao reconhecido!");
	    		System.exit(1);
    	}
    	this.repaint();
		
	}
	
	@Override
	public int getInd() {
		return this.indJogador;
	}

	@Override
	public void setInd(int ind) {
		this.indJogador = ind;
	}
}
