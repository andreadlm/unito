package Module;

public class Person implements Comparable<Person> {
  private final String name;
  private final String surname;

  public Person(String name, String surname) {
    this.name = name;
    this.surname = surname;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  @Override
  public int compareTo(Person p) {
    if(p.surname.equals(this.surname)) return this.name.compareTo(p.name);
    else return this.surname.compareTo(p.surname);
  }

  @Override
  public String toString() {
    return name + " " + surname;
  }
}