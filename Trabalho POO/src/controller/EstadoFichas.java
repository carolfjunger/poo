package controller;

import java.util.ArrayList;

import view.Observable;
import view.Observer;

public class EstadoFichas implements Observable {
	private int fichas = 0;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	@Override
	public void registraObs(Observer obs) {
	    observers.add(obs);
	}

	@Override
	public void removeObs(Observer obs) {
	    observers.remove(obs);
	}

	@Override
	public void notificaObs() {
	    for (Observer ob : observers) {
	    	//ob.update();
	    }
	}
}
