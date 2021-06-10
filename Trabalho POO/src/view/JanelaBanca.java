package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class JanelaBanca extends Janela {
	HashMap<Integer, Point> fichasPosition = new HashMap<Integer, Point>();
	final int size = 59; // tamanho da ficha
	Observer obs;
	
	public JanelaBanca(String titulo,  Observer obs) {
		super(titulo);
		HashMap<Integer, Point> fichasPosition = this.fichasPosition;
		this.obs = obs;
		
		this.addMouseListener(new MouseListener() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	int x=e.getX();
		        int y=e.getY();
		        for (int key : fichasPosition.keySet()) {
		        	Point fichaPos = fichasPosition.get(key);
		        	if(fichaPos.x < x && (fichaPos.x + size) > x && fichaPos.y < y && (fichaPos.y + size) > y) {
		        		obs.update("FICHA_CLICK", key);
		        	}
		        }

		    }

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
  
		// desenhar imagem
		Image img = this.assets.get("fundo");
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
		
		// deck1
		img = this.assets.get("deck1");
		g.drawImage(img, 100, 50, this);
		
		// deck2
		img = this.assets.get("deck2");
		g.drawImage(img, 200, 50, this);
		
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
				this.fichasPosition.put(fichasValueArr[i], new Point(x,y));
				g.drawImage(img, x, y, width, height, this);
			}
		}		
	}

	@Override
	public void carregarAssets() {
		String imageURL;
		Image image;
		String baseURL = "../Imagens/";
		
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
	}
}
