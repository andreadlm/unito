package arg1;

import libs.SIn;

/**
Calcolare la circonferenza di un cerchio. In un primo momento, inserire nel codice del programma il valore del raggio. 
In un secondo momento, farlo inserire all'utente.
*/
public class Circonferenza{

	public static void main(String [] args){
		final float PI_GRECO = 3.14f;

		System.out.print("Inserire il raggio del cerchio: ");
		float raggio = SIn.readFloat();
		float circonferenza = 2 * PI_GRECO * raggio;

		System.out.println("La circonferenza e': " + circonferenza);
	}
}