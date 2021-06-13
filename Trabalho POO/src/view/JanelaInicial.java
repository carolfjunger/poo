package view;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.reader;

public class JanelaInicial extends Janela {
	public JanelaInicial(String titulo, Runnable run) 
	{
		super(titulo);
		
        JButton button = new JButton("Iniciar novo jogo");
        JButton button2 = new JButton("Retomar jogo");
        JPanel panel = new JPanel();
        
        JanelaInicial ji = this;
        button.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		ji.setVisible(false);
        		run.run();
        		//System.exit(0);
        	}
        	
        });
        button2.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		try {
					reader.carregamento();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		ji.setVisible(false);
        		run.run();
        		//System.exit(0);
        	}
        	
        });
        
        // adicionar botoes
        panel.add(button);
        panel.add(button2);
        this.getContentPane().add(panel);
	}

	@Override
	public void carregarAssets() {
		return;
	}
}
