package view;

public interface Observable {
	public void registraObs(Observer observer);
	public void removeObs(Observer observer);
	public void notificaObs(String evento, Object val);
}