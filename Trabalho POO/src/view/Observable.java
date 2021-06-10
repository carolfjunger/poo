package view;

public interface Observable {
	public void registraObs(int jogInd, Observer observer);
	public void removeObs(int jogInd);
	public void notificaObs(String evento, Object val);
}