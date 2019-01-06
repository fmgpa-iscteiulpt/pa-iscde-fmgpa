package package1;

import outroPackage.copy.Agricultor;

public class Tomate extends Agricultor implements Coletavel {

		private int peso;
		private int tamanho;
		public void setPeso(int peso) {
			this.peso = peso;
		}
		public int getPeso() {
			return peso;
		}
		public Tomate() {
	

		peso=10;
		}
		
		
	

}
