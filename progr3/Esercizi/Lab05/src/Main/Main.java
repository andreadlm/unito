package Main;

import Kernel.SortableArray;
import Module.SortableListImpl;
import Module.Person;

public class Main {
  public static void main(String[] args) {
    SortableArray<Person> sArr = new SortableArray<>();
    sArr.setModule(new SortableListImpl<Person>());

    sArr.add(new Person("Gerry", "Scotti"));
    sArr.add(new Person("Ezio", "Greggio"));
    sArr.add(new Person("Fabio", "Fazio"));

    sArr.sort();
    sArr.print();
  }
}
