package controller;

import java.io.File;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class reader {
	/******/

    public static void carregamento() throws FileNotFoundException {// caso o arquivo não exista
        
        int jqtd,id,ficha,fichamesa,vez,qtdCartasUsadas;
        List<Integer> Jid = new ArrayList<Integer>();                                                    
        List<Integer> Jf = new ArrayList<Integer>(); 
        
        File Save = new File("Save.txt"); 
        
        Scanner s = new Scanner(Save);

        jqtd = s.nextInt();// o numero de jogadores
        fichamesa= s.nextInt();//fichas que ja foram usadas
        vez = s.nextInt();//vez
        qtdCartasUsadas = s.nextInt();//cartas usadas
        
        for(int i =1;i < jqtd;i++) {
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
        System.out.println("fichasmesa = "+fichamesa);
        System.out.println("vez = "+vez);
        System.out.println("cartas usadas = "+qtdCartasUsadas);
        
        
        
        /*******/
       
        
        
        
    }
}