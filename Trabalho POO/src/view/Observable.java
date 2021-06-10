package view;

public interface Observable {
	public void registraObs(String jogId, Observer observer);
	public void removeObs(String jogId);
	public void notificaObs(String evento, Object val);
}