package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;


import models.JogoBlackjack;
public class writer {


	public static void salvamento() throws IOException {
		
		JogoBlackjack jbl = JogoBlackjack.getInstancia();
		//jbl.setJogadores(2);
		//jbl.inicializa(2);
		//jbl.darCartas();
    	//variaveis

		List<Integer> jID = jbl.getIDJogadores();//id jogadores
		List<Integer> jf = jbl.getFichasJogadores();//ficha de cada jogador

		int tam = jID.size() -1;
		
		
		

		List<Integer> jval = jbl.pegaVal();//numero de jogadores,fichasmesa,vez,qtdCartasUsadas
		// o baralho n vai escrever, na hora de carregar o save, vai pegar as cartas do jogadores e retirar do baralho novo
		/***/
		//escrever as cartas do dealer e do jogador 1

        
        /*****/
        //forma de escrever
        File  Save = new File("Save.txt");
        FileWriter w = new FileWriter(Save);
        PrintWriter writer = new PrintWriter(w);
        
        
        
        
        
        /******/
        //escrever
        for(int k = 0; k < 4 ;k++) {
        	writer.write(jval.get(k) + "\n");
        	
        }//jogadores,fichamesa,vez,cartas usadas
        
        for(int i = 0; i < tam ;i++) {
        	writer.write(jID.get(i) + " ");
        	writer.write(jf.get(i) + "\n");

        	
        }//id, fichas jogador



        
        writer.close();  
    }


}