package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PainelImagem extends JPanel {
	private Image  img;
	private int posX;
	private int posY;
	private boolean isBackGround;
	
	public PainelImagem(Image  img, int posX, int posY, boolean isBackGround) {
		this.img = img;
		this.posX = posX;
		this.posY = posY;
		this.isBackGround = isBackGround;
		
	}
	
	
//    @Override
//    public Dimension getPreferredSize()
//    {
//        return (new Dimension(img.getWidth(), img.getHeight()));
//    }
//
//    /*
//     * This is where the actual Painting
//     * Code for the JPanel/JComponent
//     * goes. Here we will draw the image.
//     * Here the first line super.paintComponent(...),
//     * means we want the JPanel to be drawn the usual 
//     * Java way first, then later on we will
//     * add our image to it, by writing the other line,
//     * g.drawImage(...).
//     */
//    @Override
//    protected void paintComponent(Graphics g)
//    {
//        super.paintComponent(g);
//        g.drawImage(img, 0, 0, this);
//    }
	
    private void doDrawing(Graphics g) {
        //Graphics2D g2d = (Graphics2D) g;
    	if(this.isBackGround) {
    		g.drawImage(this.img, this.posX,this.posY, this.getWidth(), this.getHeight(), this);
    		setLayout(null);
    	} else {
    		g.drawImage(this.img, this.posX,this.posY, this);
    	}
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
}
