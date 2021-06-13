package controller;

import java.io.File;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {
	/**
	 * @throws IOException ****/

    public static void carregamento() throws IOException {// caso o arquivo não exista
        
        int jqtd,id,ficha,fichamesa,vez,qtdCartasUsadas;
        List<Integer> Jid = new ArrayList<Integer>();                                                    
        List<Integer> Jf = new ArrayList<Integer>(); 
        
        File save = new File("Save.txt"); 
        System.out.println(save);
        Scanner s = null;
        try {
        	s = new Scanner(save);
        } catch (FileNotFoundException e)   {
        	Boolean criouArquivo = save.createNewFile();
        	if(criouArquivo) {
        		save = new File("Save.txt");
        		s = new Scanner(save);
        	}
        }
        

        jqtd = s.nextInt();// o numero de jogadores
        qtdCartasUsadas = s.nextInt();//cartas usadas
        
        for(int i = 0;i < jqtd - 1;i++) {
        	id = s.nextInt();
        	ficha = s.nextInt();
        	Jid.add(id);
        	Jf.add(ficha);
        }//listas de ids e fichas de jogador
        

	
        
        
        /****/
        //teste

        s.close();
        System.out.println("Jogadores = "+jqtd);
        System.out.println("ids ="+Jid);
        System.out.println("fichas = "+Jf);
        System.out.println("cartas usadas = "+qtdCartasUsadas);
        
        
        
        /*******/
        
    }
}