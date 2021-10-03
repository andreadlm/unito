package unionfindset;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertSame;


public class UnionFindSetTest {

  private static class Person {
    private final String name;
    private final String surname;

    Person(String name, String surname) {
      this.name = name;
      this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Person person = (Person) o;
      return this.name.equals(person.name) && this.surname.equals(person.surname);
    }

    @Override
    public String toString() {
      return name + " " + surname;
    }
  }

  private UnionFindSet<Person> groups;
  private Person andrea;
  private Person alessandro;
  private Person fabrizio;
  private Person nadia;
  private Person luca;
  private Person fabio;

  @Before
  public void createUnionFindSet() {
    groups = new UnionFindSet<Person>();

    andrea = new Person("Andrea", "Delmastro");
    alessandro = new Person("Alessandro", "Delmastro");
    fabrizio = new Person("Fabrizio", "Delmastro");
    nadia = new Person("Nadia", "Carletto");
    luca = new Person("Luca", "Kroj");
    fabio = new Person("Fabio", "Bertaina");
  }

  @Test
  public void testFindSet() {
    groups.make(andrea);
    assertSame(groups.find(andrea), andrea);
  }

  @Test(expected = UnionFindSetException.class)
  public void testFindSet_elementNotInSet() {
    groups.make(andrea);
    groups.find(alessandro);
  }

  @Test(expected = NullPointerException.class)
  public void testFindSet_elementIsNull() {
    groups.find(null);
  }

  @Test
  public void testUnionSet_twoSets() {
    groups.make(andrea);
    groups.make(alessandro);
    groups.union(andrea, alessandro);
    assertSame(groups.find(andrea), groups.find(alessandro));
  }

  @Test
  public void testUnionSet_moreSets() {
    groups.make(andrea);
    groups.make(alessandro);
    groups.make(nadia);
    groups.make(fabrizio);
    groups.union(andrea, alessandro);
    groups.union(nadia, andrea);
    groups.union(nadia, fabrizio);
    assertSame(groups.find(andrea), groups.find(alessandro));
    assertSame(groups.find(alessandro), groups.find(nadia));
    assertSame(groups.find(nadia), groups.find(fabrizio));
  }

  @Test(expected = UnionFindSetException.class)
  public void testUnionSet_elementNotInSet() {
    groups.make(andrea);
    groups.union(andrea, alessandro);
  }

  @Test(expected = NullPointerException.class)
  public void testUnionSet_elementIsNull() {
    groups.union(null, null);
  }

  @Test
  public void testUnionFindSet_moreFinds() {
    groups.make(alessandro);
    groups.make(andrea);
    groups.make(nadia);
    groups.make(fabrizio);
    groups.make(fabio);
    groups.make(luca);
    groups.union(andrea, alessandro);
    groups.union(alessandro, nadia);
    groups.union(andrea, fabrizio);
    groups.union(fabio, fabrizio);
    groups.union(luca, fabio);
    assertSame(groups.find(luca), groups.find(fabio));
    assertSame(groups.find(andrea), groups.find(alessandro));
    assertSame(groups.find(andrea), groups.find(nadia));
    assertSame(groups.find(nadia), groups.find(fabio));
  }

  @Test(expected =  UnionFindSetException.class)
  public void testUnionSet_sameSet() {
    groups.make(andrea);
    groups.make(alessandro);
    groups.make(nadia);
    groups.union(andrea, nadia);
    groups.union(nadia, alessandro);
    groups.union(andrea, alessandro);
  }
}
