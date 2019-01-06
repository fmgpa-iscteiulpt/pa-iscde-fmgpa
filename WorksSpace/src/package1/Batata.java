package package1;

import java.util.ArrayList;

public class Batata extends Vegetal{
	private int peso;
	private ArrayList<Tomate> tomates= new ArrayList<Tomate>();
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public int getPeso() {
		double tamanho=12;
		return peso;
	}
	public Batata() {
		for(int i= 0;i<5;i++) {
			
		}
		peso=10;
	}
	public void coisas() {
		
	}
	public class Frango implements Coletavel{
		
		public int getPeso() {
			return peso;
		}
	}
	public void killBatata() {
		
	}
}
