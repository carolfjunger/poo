// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JanelaJogador extends Janela implements Observer {
	private List<String> cartas;
	private Observer obs;
	
	// estado
	private int fichas = 0;
	private int aposta = 0;
	private int indMao = 0;
	private int indJogador = 0;
	private boolean praBaixo = false;
	
	// constantes
	private final int apostaMinima = 20;
	private final int wCarta = 73; //px - largura da imagem da carta
	private final int hCarta = 97; //px - altura da imagem da carta
	
	// botoes
	private JButton deal = new JButton("Deal");
	private JButton stand = new JButton("Stand");
	private JButton hit = new JButton("Hit");
	private JButton dbl = new JButton("Double");
	private JButton split = new JButton("Split");
	private JButton surrender = new JButton("Surrender");
	private JButton quit = new JButton("Quit");
	
	// textos informativos
	private JLabel lFichas = new JLabel();
	private JLabel lAposta = new JLabel();
	private JLabel vezStatus = new JLabel();
	private JLabel somaCartas = new JLabel();
	
	public JanelaJogador(int indice, int fichas, int indMao, List<String> cartas, Observer obs) {
		super(Integer.toString(indice));
		this.fichas = fichas;
		this.cartas = cartas;
		this.obs = obs;
		this.indJogador = indice;
		this.indMao = indMao;
		
		JPanel panel = new PaintPanel();
		panel.setLayout(null);
		
		// botoes
		
		Dimension ps = surrender.getPreferredSize();
		int width = (int) ps.getWidth();
		JButton btns[] = {deal, stand, hit, dbl, split, surrender, quit};
		for (int j = 0; j < btns.length; j++) {
			JButton jb = btns[j];
			jb.setSize(ps);
			if (j < 3)
				jb.setLocation(2 + (width * j), 32);
			else if (j < 6)
				jb.setLocation(2 + (width * (j - 3)), 64);
			else
				jb.setLocation(2 + (width * (j - 6)), 96);
			jb.setEnabled(false);
			
			jb.addActionListener( (evt) -> {
				String txt = jb.getText().toUpperCase();
				switch(txt) {
				case "DEAL":
					this.vezStatus.setText("Apostando.");
					this.deal.setEnabled(false);
					obs.update(txt, this.aposta);
					break;
				case "STAND":
					this.vezStatus.setText("Apostando.");
					
					this.deal.setEnabled(false);
	        		this.stand.setEnabled(false);
	        		this.dbl.setEnabled(false);
	        		this.hit.setEnabled(false);
	        		this.split.setEnabled(false);
	        		this.surrender.setEnabled(false);
	        		
					obs.update(txt, null);
					break;
				case "HIT":
				case "DOUBLE":
				case "SPLIT":
					this.surrender.setEnabled(false);
					obs.update(txt, this.indMao);
					break;
				case "SURRENDER":
					this.vezStatus.setText("Rendido.");
					
					this.deal.setEnabled(false);
	        		this.stand.setEnabled(false);
	        		this.dbl.setEnabled(false);
	        		this.hit.setEnabled(false);
	        		this.split.setEnabled(false);
	        		this.surrender.setEnabled(false);
	        		
					obs.update(txt, this.indJogador);
					break;
				case "QUIT":
					obs.update(txt, this.indJogador);
					break;
				default:
					obs.update(txt, null);
				}
				
			});
			panel.add(jb);
		}
		
		if (this.indMao == 0)
			this.quit.setEnabled(true);
		
		// Mostra a vez do jogador
		this.vezStatus.setText("Aguarde sua vez");
		this.vezStatus.setSize(115, this.vezStatus.getPreferredSize().height);
		this.vezStatus.setLocation(this.getWidth()/2 - 115/2, this.getHeight() - 260);
		
		// valor total das fichas
		this.lFichas.setText("Fichas: " + Integer.toString(fichas));
		this.lFichas.setSize(this.lFichas.getPreferredSize());
		this.lFichas.setLocation(12, this.getHeight() - 160);
		
		// valor total das fichas apostadas
		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
		this.lAposta.setSize(this.lAposta.getPreferredSize());
		this.lAposta.setLocation(12, this.getHeight() - 180);
		
		// Mostra a soma das cartas do jogador
		this.somaCartas.setText("Somatorio das cartas: 0");
		this.somaCartas.setSize(somaCartas.getPreferredSize());
		this.somaCartas.setLocation(12, this.getHeight() - 200);
		
		// botando tudo no painel principal
		panel.add(this.lFichas);
		panel.add(this.lAposta);
		panel.add(this.vezStatus);
		panel.add(this.somaCartas);
        
        this.getContentPane().add(panel);
	}
	
	public void addFichaApostada(int ficha) {
		if(this.fichas - ficha >= 0) {
			this.aposta += ficha;
			this.fichas -= ficha;
		}

	}

	// pintar as fichas e as cartas
    private class PaintPanel extends JPanel {

        @Override
		public void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	int padding = 4;
        	
        	// desenhar cartas
        	int cInd = 0;
        	int somaCartas = 0;
    		for (String c: cartas) {
    			Image img = assets.get( c );
    			if (img != null) {
    				int width = img.getWidth(this);
    				int height = img.getHeight(this);
    				g.drawImage(img, padding + (wCarta * cInd), getHeight() - (hCarta + padding), width, height, this);
    				cInd += 1;
    			}
    		}

            String text = "Jogador #" + (indJogador + 1) + " -- " + "Mao #" + (indMao + 1);
            
            g.drawString(text, 20, 20);
        }

    }
    
    @Override 
    public void update(String evento, Object val) {
    	int vez;
    	List<String> cartas;
    	switch (evento) {
    	case "DEALER_OPEN":
    		break;
    	case "INIT":
    		// removendo maos resultantes de split
    		if (this.indMao != 0) {
    			this.dispose();
    		}
    		vez = (int) val;
    		if (vez == this.indJogador) {
    			if (this.aposta > apostaMinima ) {
    				this.vezStatus.setText("Faça sua aposta");
    				this.deal.setEnabled(true);
    			} else {
    				this.vezStatus.setText("Faça sua aposta");
    				this.deal.setEnabled(false);
    			}
    		} 
    		else {
    			this.vezStatus.setText("Aguarde a sua vez");
    		}

    		break;
    	case "VEZ":
    		int[] infoVez = (int[]) val;
    		vez = infoVez[0];
    		int sumCarta = infoVez[1];
    		int vezInicial = infoVez[2];
    		int indMao = infoVez[3];
    		
    		if (vez == this.indJogador && this.indMao == indMao) {
        		this.vezStatus.setText("Eh a sua vez de jogar...");
        		this.vezStatus.setSize(160, 18);
        		this.somaCartas.setText("Somatorio das cartas:" + Integer.toString(sumCarta));
        		this.somaCartas.setSize(somaCartas.getPreferredSize());
    			
    			if (sumCarta < 21) {
            		boolean podeSplit = this.cartas != null && this.cartas.size() == 2 && this.cartas.get(0).equals(this.cartas.get(1));
            		boolean podeDbl = this.fichas >= this.aposta;
            		
            		this.stand.setEnabled(true);
            		this.dbl.setEnabled(podeDbl);
            		this.hit.setEnabled(true);
            		this.split.setEnabled(podeSplit && podeDbl);
            		this.surrender.setEnabled(vezInicial == 1);
    			}
    			else if (sumCarta == 21) {
    				this.vezStatus.setText("21!");
        			if (vezInicial == 1) {
        				this.vezStatus.setText("Blackjack!");
        				//this.obs.update("STAND", null);
        			}
    			}
    		}    		

    		break;
    	case "HIT":
    		int[] info = (int[]) val;
    		int resultado = info[0];
    		int mao = info[1];
    		System.out.println("HIT NA JANELA:" + this.indJogador);
    		
    		// quebramos ou 21
    		if (resultado != 0 && this.indMao == mao) {
    			
        		this.stand.setEnabled(false);
        		this.dbl.setEnabled(false);
        		this.hit.setEnabled(false);
        		this.split.setEnabled(false);
        		this.surrender.setEnabled(false);
    			
        		if (resultado > 0)
        			this.vezStatus.setText("21!");
        		else if (resultado < 0)
        			this.vezStatus.setText("Voce quebrou! Espera a proxima rodada.");
    		}
    		break;
    	case "DAR_CARTAS":
    		Object[] inf = (Object[]) val;
    		cartas = (List<String>) inf[0];
    		sumCarta = (int) inf[1];
    		mao = (int) inf[2];
    		this.somaCartas.setText("Somatorio das cartas:" + Integer.toString(sumCarta));
    		this.somaCartas.setSize(somaCartas.getPreferredSize());
    		
    		if (this.indMao == mao)
    			this.cartas = cartas;
    		
    		break;
    	case "LIMPAR_CARTAS":
    		this.cartas = new ArrayList<String>();
    		this.somaCartas.setText("Somatorio das cartas:");
    		break;
       	case "PRE_APOSTA":
       		this.quit.setEnabled(false);
       		info = (int[]) val;
       		vez = info[0];
       		int iMao = info[1];
       		int ficha = info[2];
       		if (vez == this.indJogador && this.indMao == iMao) {
           		this.addFichaApostada(ficha);
           		if(this.aposta >= apostaMinima && this.cartas.size() == 0) {
           			this.deal.setEnabled(true);
           		}
           		this.lFichas.setText("Fichas: " + Integer.toString(this.fichas));
           		this.lFichas.setSize(this.lFichas.getPreferredSize());
        		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
        		this.lAposta.setSize(this.lAposta.getPreferredSize());
       		}

    		break;	
       	case "ATUALIZA_FICHAS":
       		info = (int[]) val;
       		ficha = info[0];
       		int aposta = info[1];
       		iMao = info[2];
       		
       		System.out.println("FICHAS: " + fichas);
   			System.out.println("APOSTAS: " + aposta);
    		
    		if (this.indMao == iMao) {
    			this.aposta = aposta;
        		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
        		this.lAposta.setSize(this.lAposta.getPreferredSize());	
    		}
    		
       		if(this.aposta >= apostaMinima && this.cartas.size() == 0) {
       			this.deal.setEnabled(true);
       		}
       		this.fichas = ficha;
       		this.lFichas.setText("Fichas: " + Integer.toString(this.fichas));
       		this.lFichas.setSize(this.lFichas.getPreferredSize());

    		break;	
    	case "FINALIZA_TURNO":
    		Object[] obj = (Object[]) val;
    		int fichas = (int) obj[0];
    		List<HashMap<Integer, Integer>> resultados = (List<HashMap<Integer, Integer>>) obj[1];
    		HashMap<Integer, Integer> apMao = resultados.get(this.indJogador);

			if (apMao.containsKey(this.indMao)) {
				int resul = apMao.get(this.indMao);
				if (resul > 0) {
					this.vezStatus.setText("Voce ganhou!");
				}
				else if (resul == 0) {
					this.vezStatus.setText("Voce empatou...");
				}
				else if (resul == -1) {
					this.vezStatus.setText("Voce perdeu...");
				}
				
			}
			
    		this.aposta = 0;
    		this.fichas = fichas;
    		
       		this.lFichas.setText("Fichas: " + Integer.toString(this.fichas));
       		this.lFichas.setSize(this.lFichas.getPreferredSize());
    		this.lAposta.setText("Aposta: " + Integer.toString(this.aposta));
    		this.lAposta.setSize(this.lAposta.getPreferredSize());

    		break;
    	case "QUIT":
    		vez = (int) val;
    		if (this.indJogador == vez+1) {
    			this.vezStatus.setText("Faça sua aposta...");
    			this.vezStatus.setSize(150, 18);
    		}
    		
    		if (this.indJogador == vez)
    			this.dispose();

    		break;
    	
    	default:
    		System.out.println("Erro fatal recebendo evento na janela jogador, evento nao reconhecido!" + evento);
    		System.exit(1);
    	}
    	
    	this.repaint();
    }
    
	@Override
	public int getInd() {
		return this.indJogador;
	}

	@Override
	public void setInd(int ind) {
		this.indJogador = ind;
	}
}
