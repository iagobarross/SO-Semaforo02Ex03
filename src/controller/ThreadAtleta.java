/*3. Numa prova de triatlo moderno, o circuito se dá da seguinte maneira:
- 3Km de corrida onde os atletas correm entre 20 e 25 m / 30 ms
- 3 tiros ao alvo com pontuação de 0 a 10
- 5 km de ciclismo onde os atletas pedalam entre 30 e 40 m/ 40 ms
25 atletas participam da prova e largam juntos, no entanto, apenas 5 armas de tiro estão a
disposição. Cada atleta leva de 0,5 a 3s por tiro. Conforme os atletas finalizam o circuito de corrida,
em ordem de chegada, pegam a arma para fazer os disparos. Uma vez encerrados os disparos, a
arma é liberada para o próximo, e o atleta segue para pegar a bicicleta e continuar o circuito.
Para determinar o ranking final dos atletas, considera-se a seguinte regra:
- O primeiro que chegar recebe 250 pontos, o segundo recebe 240, o terceiro recebe
230, ... , o último recebe 10.
- Soma-se à pontuação de cada atleta, o total de pontos obtidos nos 3 tiros (somados)
Ordenar a pontuação e exibir o resultado final do maior pontuador para o menor.
*/
package controller;

import java.util.concurrent.Semaphore;
import br.edu.fateczl.quicksort.*;

public class ThreadAtleta extends Thread{

	private int distanciaPercorrida;
	private int distanciaCorrida = 3000;
	private int distanciaCiclismo = 5000;
	private int idAtleta;
	private Semaphore semaforoArma;
	private static int chegada;
	private static int [] pontuacao = new int [25];
	private static int [] atletas = new int [25];
	private QuickSort sort = new QuickSort();
	
	public ThreadAtleta(int idAtleta, Semaphore semaforoArma) {
		this.idAtleta = idAtleta;
		this.semaforoArma = semaforoArma;
	}

	@Override
	public void run() {
		correr();
		
		int pontuacaoTiro = 0; 
		try{
			semaforoArma.acquire();
			pontuacaoTiro = atirar();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			semaforoArma.release();
		}
		
		pedalar(pontuacaoTiro);
		
	}


	private void correr() {
		distanciaPercorrida = 0;
		
		while(distanciaPercorrida < distanciaCorrida) {
			int velocidade = (int)((Math.random() * 6) + 20);
			distanciaPercorrida += velocidade;
			System.out.println("O atleta #" + idAtleta + " correu " + distanciaPercorrida + 
					" metros.");
			
			if(distanciaPercorrida >= distanciaCorrida) {
				System.out.println("O atleta #" + idAtleta + " finalizou a corrida");
				break;
			}
			
			try {
				sleep(30);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		
		
		
		
	}

	private int atirar() {
		int pontuacaoTotal = 0;
		for(int tiros = 0; tiros < 3; tiros++) {
			int pontuacaoTiro = (int)(Math.random() * 11);
			pontuacaoTotal += pontuacaoTiro;
			System.out.println("O atleta #" + idAtleta + " pontuou " + pontuacaoTiro + 
					" no tiro " + (tiros +1));
			try {
				sleep((int)((Math.random() * 2501) + 500));
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		System.out.println("O atleta #" + idAtleta + " pontuou " + pontuacaoTotal +
				" na fase de tiros");
		
		return pontuacaoTotal;
	}

	private void pedalar(int pontuacaoTiro) {
		distanciaPercorrida = 0;
		int pontuacaoTotal = 0;
		
		while(distanciaPercorrida < distanciaCiclismo) {
			int velocidade = (int) ((Math.random() * 11) + 30);
			distanciaPercorrida += velocidade;
			System.out.println("O atleta #" + idAtleta + " pedalou por " + distanciaPercorrida +
					" metros");
			try {
				sleep(40);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			
			if (distanciaPercorrida >= distanciaCiclismo) {
				pontuacao[chegada] += (250 - (chegada *10) + pontuacaoTiro);
				atletas[24 - chegada] = idAtleta;
				System.out.println("O atleta #" + idAtleta + " finalizou o percurso em " + (chegada + 1) + "o lugar");
				chegada++;
				
			}
			
		}
		
	
		if(chegada == 25) {
			sort.quick(pontuacao, 0, 24);
			System.out.println("\nClassificação: \n");
			for(int i = 24; i >= 0; i--) {
				System.out.println("Atleta #" + atletas[i] + " | Pontuacao: " + pontuacao[i]);
			}
		}
	
		
		
	}

}
