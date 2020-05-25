package arg10;

// Classe fornita dall'esercizio, modificata con le implementazioni
// richieste dal testo

//Contatto.java
public class Contatto implements Comparable<Contatto> {
    //un contatto e' la coppia di una persona e il suo indirizzo email
    //costruttore a 2 argomenti, metodi get e set
    private String nome;
    private String email;

    public Contatto(String nome, String email) {
        this.nome = nome; 
        this.email = email;
    }

    // metodi get
    public  String getNome() {
        return nome;
    }
    public  String getEmail() {
        return email;
    }

    // metodi set
    public void setNome(String n){
        nome = n;
    }
    public void setEmail(String e){
        email = e;
    }

    // Confronta due contatti, ritona 0 se sono uguali lessilogicamente,
    // un valore >0 se il contatto su cui il metodo è stato invocato è
    // maggiore del parametro, un valore <0 se invece è minore.
    // Il confronto avviene esclusivamente sul nome
    @Override
    public int compareTo(Contatto c) {
        return this.nome.compareTo(c.nome);
    }

    // stampa le informazioni del contatto 
    public void scriviOutput() {
        System.out.println(" - " + nome + " : " + email);
    }
}
