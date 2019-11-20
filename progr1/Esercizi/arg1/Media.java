package arg1;

import libs.SIn;

/** classe Media: serve per calcolare la media
    di 3 numeri letti da tastiera.
    Seguono diverse implementazioni del
    calcolo della media, che differiscono per le
    dichiarazioni delle variabili:
    ci aspettiamo problemi.
    Ad esempio provare ad eseguire il programma con
    i valori 2, 3, 3, o con 8, 2, 3.
  */


public class Media {

  public static void main(String[] args) {

    int x, y, z, media1;
    double media2, media3, media4, media5;

    System.out.println("Introduci il primo numero (per es. 8): ");
    x = SIn.readInt();
    System.out.println("Introduci il secondo numero (per es. 2): ");
    y = SIn.readInt();
    System.out.println("Introduci il terzo numero (per es. 3): ");
    z = SIn.readInt();

    media1 = (x + y + z) / 3; //divisione intera
    System.out.println("int media1: (x+y+z)/3 = " + media1 + " (divisione intera)");

    media2 = (x + y + z) / 3; //divisione intera e cast a double
    System.out.println("double media2: (x+y+z)/3 = " + media2  + " (divisione intera e cast a double)");

    media3 = (x + y + z) / 3.0; // divisione con virgola - risultato atteso!!
    System.out.println("double media3: (x+y+z)/3.0 = " +media3  + " (divisione con virgola - risultato atteso!)");


    // VARIANTI all'esercizio:
    // posso scrivere: media3 = (double)(x + y + z) / 3;
    // ma non: media3 = (double)((x + y + z) / 3); Perche'?

    media4 = (double)((x + y + z) / 3);
    System.out.println("double media4: (double)((x + y + z) / 3) = " + media4 + " (cast applicato al risultato della divisione intera)");

    media5 = (double)(x + y + z) / 3;
    System.out.println("double media5: (double)(x + y + z) / 3 = " + media5 + " (cast applicato alla divisione - risultato atteso)");

    // Aggiungiamo le seguenti istruzioni:
    // int m = media3; // problema incontrato negli es. precedenti...
    // Per le seguenti provare  ad es. con 1,2,5
    System.out.println("arrotondamento: (int)media5 = " + (int)media5 + " (cast - errato)");
    // modo sbagliato di arrotondare!
    System.out.println("arrotondamento: (int)Math.round(media5) = " + (int)Math.round(media5) + " (parsing - corretto)");
    //modo giusto di arrotondare!

    /*
      ancora problemi imprevisti dovuti
      a range di numeri finito
      (che portano a rappresentazione binaria
      approssimata)
      */
    double f=4.35;
    System.out.println("arrotondamento: double f=4.35; (int)(f*100) = " + (int)(f*100));
    System.out.println("arrotondamento: double f=4.35; (int)Math.round((f*100)) = " + (int)Math.round((f*100))); // usare il Math.round prima del cast!
  }
}