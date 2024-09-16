package view;

import java.util.concurrent.Semaphore;

import controller.ThreadAtleta;

public class Main {

	public static void main(String[] args) {
		int permissoes = 5;
		Semaphore semaforoArma = new Semaphore(permissoes);
		
		for(int i = 0; i < 25; i++) {
			Thread t = new ThreadAtleta((i+1), semaforoArma);
			t.start();
		}
	}

}
