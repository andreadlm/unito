package arg6;

import libs.Person;

public class TestDynamicQueue{
    public static void main(String[] args) {        
        System.out.println("** Integer test **");

        DynamicQueue<Integer> q_i = new DynamicQueue<Integer>();

        System.out.println("\tq.empty() : " + (q_i.empty() == true));
        
        q_i.enqueue(1);
        q_i.enqueue(2);
        q_i.enqueue(3);

        System.out.println("\tq.size() : " + (q_i.size() == 3));
        System.out.println("\tq.front() : " + (q_i.front() == 1 && q_i.size() == 3));
        System.out.println("\tq.dequeue() : " + (q_i.dequeue() == 1 && q_i.size() == 2));
        System.out.println("\tq.contains() : " + (q_i.contains(2) == true && q_i.contains(7) == false));

        System.out.println("** Double test **");

        DynamicQueue<Double> q_d = new DynamicQueue<Double>();
        
        System.out.println("\tq.empty() : " + (q_d.empty() == true));
        
        q_d.enqueue(1.1);
        q_d.enqueue(2.2);
        q_d.enqueue(3.3);

        System.out.println("\tq.size() : " + (q_d.size() == 3));
        System.out.println("\tq.front() : " + (q_d.front() == 1.1 && q_d.size() == 3));
        System.out.println("\tq.dequeue() : " + (q_d.dequeue() == 1.1 && q_d.size() == 2));
        System.out.println("\tq.contains() : " + (q_d.contains(2.2) == true && q_d.contains(7.1) == false));

        System.out.println("** String test **");

        DynamicQueue<String> q_s = new DynamicQueue<String>();
        
        System.out.println("\tq.empty() : " + (q_s.empty() == true));
        
        q_s.enqueue("andrea");
        q_s.enqueue("alessandro");
        q_s.enqueue("alessio");

        System.out.println("\tq.size() : " + (q_s.size() == 3));
        System.out.println("\tq.front() : " + (q_s.front().equals("andrea") && q_s.size() == 3));
        System.out.println("\tq.dequeue() : " + (q_s.dequeue().equals("andrea") && q_s.size() == 2));
        System.out.println("\tq.contains() : " + (q_s.contains("alessandro") == true && q_s.contains("antonio") == false));
        
        System.out.println("** Person test **");

        DynamicQueue<Person> q_p = new DynamicQueue<Person>();
        
        System.out.println("\tq.empty() : " + (q_p.empty() == true));
        
        Person p1 = new Person("Andrea", "Delmastro");
        Person p2 = new Person("Alessandro", "Delmastro");
        Person p3 = new Person("Alessio", "Dell'Ombra");

        q_p.enqueue(p1);
        q_p.enqueue(p2);
        q_p.enqueue(p3);

        System.out.println("\tq.size() : " + (q_p.size() == 3));
        System.out.println("\tq.front() : " + (q_p.front() == p1 && q_p.size() == 3));
        System.out.println("\tq.dequeue() : " + (q_p.dequeue() == p1 && q_p.size() == 2));
        System.out.println("\tq.contains() : " + (q_p.contains(p2) == true && q_p.contains(new Person("Antonio", "Dalmasso")) == false));
    }
}

