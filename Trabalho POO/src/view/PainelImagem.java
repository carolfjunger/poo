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
	
    private void doDrawing(Graphics g) {
        //Graphics2D g2d = (Graphics2D) g;
        g.drawImage(this.img, 0, 0, 100, 100, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
