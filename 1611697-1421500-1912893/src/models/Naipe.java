// Ana Carolina Junger
// Joao Marcus
// Thomas Botelho

package models;

public enum Naipe {
	COPAS("h"), 
	ESPADAS("s"), 
	OUROS("d"), 
	PAUS("c");
	
	private String repr;
	
	Naipe(String r){
		this.repr = r;
	}
	
	public String Repr() {
		return this.repr;
	}
	
}
