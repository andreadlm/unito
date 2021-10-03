package unionfindset;

import java.util.HashMap;

/**
 * A Union-find-set data structure.
 * @param <E> the type of the elements you want to store inside the data structure.
 *
 * @author Andrea Delmastro
 */
public class UnionFindSet<E> {
  /**
   * A single node. Nodes are linked to form a tree, where the root represent the whole tree-set.
   * Each node has a parent and a rank.
   */
  private class Node {
    private final E element;
    private Node parent;
    private int rank;

    private Node(E element) {
      this.element = element;
      this.parent = this;
      this.rank = 0;
    }
  }

  /* Maps each element to the corresponding node */
  private final HashMap<E, Node> map;

  public UnionFindSet() {
    map = new HashMap<E, Node>();
  }

  /**
   * Creates a set containing an element.
   * @param element the element to be added to the new set.
   * @throws NullPointerException if the element parameter is null.
   */
  public void make(E element) throws NullPointerException {
    if(element == null)
      throw new NullPointerException("element parameter cannot be null");
    Node node = new Node(element);
    if(!map.containsKey(element)) map.put(element, node);
  }

  /**
   * Joins two sets to create a single set containing all the elements stored in the two combined sets.
   * @param element1 an element of the first set.
   * @param element2 an element of the second set.
   * @throws IllegalArgumentException if one of the parameters is null.
   * @throws NullPointerException if you try to join a set with itself.
   */
  public void union(E element1, E element2) throws NullPointerException, UnionFindSetException {
    if(element1 == null)
      throw new NullPointerException("element1 parameter cannot be null");
    if(element2 == null)
      throw new NullPointerException("element2 parameter cannot be null");

    Node node1 = map.get(element1);
    if(node1 == null)
      throw new UnionFindSetException("the element " + element1 + " is not stored inside the structure");
    Node node2 = map.get(element2);
    if(node2 == null)
      throw new UnionFindSetException("the element " + element2 + " is not stored inside the structure");

    link(findNode(node1), findNode(node2));
  }

  /**
   * Links two nodes.
   * @param node1 the first node you want to link.
   * @param node2 the second node you want to link.
   * @throws UnionFindSetException if you try to link a node to itself.
   */
  private void link(Node node1, Node node2) throws UnionFindSetException {
    if(node1 == node2)
      throw new UnionFindSetException("the two elements already belong to the same set");

    if(node1.rank > node2.rank)
      node2.parent = node1;
    else {
      node1.parent = node2;
      if(node1.rank == node2.rank)
        node2.rank++;
    }
  }

  /**
   * Finds the set of an element. An element representing the set is returned such that, if you execute the find function
   * twice on the same set, you will get the same representative.
   * @param element the element you want to find the set.
   * @return An element representing the set.
   * @throws NullPointerException if the element passed as parameter is null.
   * @throws UnionFindSetException if the element is not stored inside any set.
   */
  public E find(E element) throws NullPointerException, UnionFindSetException {
    if(element == null)
      throw new NullPointerException("element parameter cannot be null");
    Node node = map.get(element);
    if(node == null)
      throw new UnionFindSetException("the element " + element + " is not stored inside the structure");
    return findNode(node).element;
  }

  /**
   * Finds the root of a tree containing the node.
   * @param node the node of which you want to find the root of the tree.
   * @return the root of the tree containing the node.
   */
  private Node findNode(Node node) {
    if(node.parent != node)
      node.parent = findNode(node.parent);
    return node.parent;
  }
}
