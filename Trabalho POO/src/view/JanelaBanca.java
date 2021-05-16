package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

public class JanelaBanca extends Janela {
	public JanelaBanca(String titulo) {
		super(titulo);
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
	}

	@Override
	public void carregarAssets() {
		String imageURL;
		Image image;
		
		// fundo
		imageURL = "../Imagens/blackjackBKG.png";
		image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.assets.put("fundo", image);
		
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
