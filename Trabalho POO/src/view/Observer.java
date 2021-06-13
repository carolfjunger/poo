package view;

public interface Observer {
	public void update(String evento, Object val);
	public int getInd();
	public void setInd(int ind);
}
