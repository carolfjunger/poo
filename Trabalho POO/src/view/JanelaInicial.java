package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import controller.Reader;

public class JanelaInicial extends Janela {
	public JanelaInicial(String titulo, Runnable run) {
		super(titulo);
		
        JButton button = new JButton("Iniciar novo jogo");
        JButton button2 = new JButton("Retomar jogo");
        JPanel panel = new JPanel();
        
        JanelaInicial ji = this;
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ji.setVisible(false);
        		run.run();
        	}
        	
        });
        
        button2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) 
        	{
        		try {
        			Reader.carregamento();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		ji.setVisible(false);
        		run.run();
        	}
        });
        
        // adicionar botoes
        panel.add(button);
        panel.add(button2);
        this.getContentPane().add(panel);
	}
}
