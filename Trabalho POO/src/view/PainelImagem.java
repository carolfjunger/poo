package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PainelImagem extends JPanel {
	private Image img;
	
	public PainelImagem(Image img) {
		this.img = img;
	}
	
    private void Draw(Graphics g) {
        //Graphics2D g2d = (Graphics2D) g;
    	if (this.img != null) {
    		g.drawImage(this.img, 0, 0, img.getWidth(null), img.getHeight(null), this);	
    	}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Draw(g);
        g.dispose();
    }
}
