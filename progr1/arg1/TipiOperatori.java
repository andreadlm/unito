public class TipiOperatori{

	public static void main(String [] args){

		int m1= 3; //assegnamento
		int n1= m1+1; //o m++
		double p= 2.3; // o float p = 4.1f
		long t= 2000000000;
		
		
		//operatori + - * / ecc. attenzione: / se chiamato su interi da la divisione intera
		
		System.out.println(99/50); // divisione intera
		System.out.println(99%50); //resto della divisione intera
		System.out.println(99/50.0); // divisione reale
		System.out.println(99.0/50); // divisione reale
		double d = 99;
		int m = 99;
		int n = 50;
		System.out.println(m/n); // divisione intera
		System.out.println(d/n); // divisione reale
		System.out.println("col cast: "+(double)m/n);
		System.out.println("col cast sul risultato: "+(double)(m/n));
		
		
		
		//Errori di Overflow
		
		int max=Integer.MAX_VALUE;
		System.out.println(max);
		max = max + 3;
		System.out.println(max);
		
		
		//Altri tipi
		
		boolean b = true;
		boolean b1 = false;
		boolean b2= b && b1; //true || true operatori lazy a cui si contrappongono & e |
		b2=!b1;
		b2=m>n;
		
		char c= 'c';
		String s = "stringa";
		
		
	
	}
}