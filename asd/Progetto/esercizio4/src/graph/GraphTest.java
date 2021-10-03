package graph;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.LinkedList;

/**
 * Class implementing unit tests on #{@link graph.Graph}
 *
 * @author Andrea Delmastro
 */
public class GraphTest {

  private static class City {
    private final String name;
    private final int population;

    City(String name, int population) {
      this.name = name;
      this.population = population;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      City city = (City) o;
      return population == city.population &&
             name.equals(city.name);
    }

    @Override
    public String toString() {
      return "{" + name + ", " + population + "}";
    }
  }

  Graph<City, Integer> map;
  City boves;
  City cuneo;
  City fossano;
  City savigliano;
  City mondovi;

  @Before
  public void createOrientedGraph() {
    map = new Graph<City, Integer>(true);
    boves = new City("Boves", 9688);
    cuneo = new City("Cuneo", 56072);
    fossano = new City("Fossano", 24559);
    savigliano = new City("Savigliano", 21490);
    mondovi = new City("Mondovì",  22511);
  }

  @Test
  public void testIsDirected() {
    assertTrue(map.isDirected());
  }

  @Test
  public void testGetVertexesCount() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    assertSame(2, map.getVertexesCount());
  }

  @Test(expected = GraphException.class)
  public void testGetVertexesCount_multipleSameVertexAdd() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(boves);
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
  }

  @Test
  public void testContainsVertex() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    assertTrue(map.containsVertex(cuneo) && map.containsVertex(boves));
  }

  @Test
  public void testContainsVertex_noVertex() {
    map.addVertex(boves);
    assertSame(false, map.containsVertex(fossano));
  }

  @Test
  public void testAddEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    assertTrue(map.containsEdge(boves, cuneo));
  }

  @Test(expected = GraphException.class)
  public void testAddSameEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, cuneo, 30);
  }

  @Test(expected = GraphException.class)
  public void testAddSameEdge_undirected() {
    map = new Graph<City, Integer>(false);
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(cuneo, boves, 10);
  }

  @Test(expected = GraphException.class)
  public void testContainsEdge_noEdge_noVertex() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    assertFalse(map.containsEdge(cuneo, fossano));
  }

  @Test
  public void testContainsEdge_noEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(fossano);
    map.addEdge(boves, fossano, 10);
    assertFalse(map.containsEdge(cuneo, fossano));
  }

  @Test
  public void testGetEdgeWeight() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    assertSame(10, map.getEdgeLabel(boves, cuneo));
  }

  @Test(expected = GraphException.class)
  public void testGetEdgeWeight_noEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    map.getEdgeLabel(savigliano, cuneo);
  }

  @Test
  public void testAddEdge_undirected() {
    map = new Graph<>(false);
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addEdge(boves, cuneo, 10);
    assertTrue(map.containsEdge(boves, cuneo) &&
        map.containsEdge(cuneo, boves) &&
        map.getEdgeLabel(boves, cuneo).equals(map.getEdgeLabel(cuneo, boves)));
  }

  @Test
  public void testGetEdgesCount() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addVertex(fossano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(cuneo, boves, 10);
    map.addEdge(savigliano, boves, 50);
    map.addEdge(savigliano, cuneo, 40);
    assertSame(4, map.getEdgesCount());
  }

  @Test
  public void testGetEdgesCount_undirected() {
    map = new Graph<>(false);
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addVertex(fossano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(savigliano, boves, 50);
    map.addEdge(savigliano, cuneo, 40);
    assertSame(6, map.getEdgesCount());
  }

  @Test
  public void testGetVertexesList() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(fossano);
    LinkedList<City> vertexesList = map.getVertexesList();
    assertTrue(vertexesList.size() == 3 &&
               vertexesList.contains(boves) &&
               vertexesList.contains(cuneo) &&
               vertexesList.contains(fossano));
  }

  @Test
  public void testGetEdgesList() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, savigliano, 50);
    map.addEdge(savigliano, boves, 50);
    LinkedList<Graph<City, Integer>.Edge> edgesList = map.getEdgesList();
    assertTrue(edgesList.size() == 3 &&
               edgesList.get(0).getFrom() == savigliano &&
               edgesList.get(0).getTo() == boves &&
               edgesList.get(0).getLabel() == 50 &&
               edgesList.get(1).getFrom() == boves &&
               edgesList.get(1).getTo() == cuneo &&
               edgesList.get(1).getLabel() == 10 &&
               edgesList.get(2).getFrom() == boves &&
               edgesList.get(2).getTo() == savigliano &&
               edgesList.get(2).getLabel() == 50);
  }

  @Test
  public void testGetAdjacentVertexes() {
    map.addVertex(boves);
    map.addVertex(savigliano);
    map.addVertex(fossano);
    map.addEdge(boves, savigliano, 50);
    map.addEdge(savigliano, fossano, 20);
    map.addEdge(savigliano, boves, 50);
    LinkedList<City> adjVertexesList = map.getAdjacentVertexes(savigliano);
    assertTrue(adjVertexesList.size() == 2 &&
               adjVertexesList.get(0) == fossano &&
               adjVertexesList.get(1) == boves);
  }

  @Test
  public void testGetAdjacentVertexes_undirected() {
    map = new Graph<>(false);
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(savigliano, boves, 10);
    LinkedList<City> adjVertexesList = map.getAdjacentVertexes(boves);
    assertTrue(adjVertexesList.size() == 2 &&
               adjVertexesList.get(0) == cuneo &&
               adjVertexesList.get(1) == savigliano);
  }

  @Test
  public void testRemoveVertex() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.removeVertex(boves);
    assertFalse(map.containsVertex(boves));
  }

  @Test(expected = GraphException.class)
  public void testRemoveVertex_noVertex() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.removeVertex(savigliano);
  }

  @Test
  public void testRemoveVertex_removeRelativeEdges() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(fossano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, fossano, 30);
    map.addEdge(cuneo, fossano, 20);
    map.addEdge(cuneo, boves, 10);
    map.removeVertex(boves);
    assertTrue(map.containsEdge(cuneo, fossano) &&
               map.getEdgesCount() == 1);
  }

  @Test
  public void testRemoveVertex_removeAllVertexes() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(fossano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(cuneo, boves, 10);
    map.addEdge(cuneo, fossano, 30);
    map.addEdge(fossano, cuneo, 30);
    map.addEdge(boves, fossano, 40);
    map.removeVertex(boves);
    map.removeVertex(cuneo);
    map.removeVertex(fossano);
    assertTrue(map.getEdgesCount() == 0 &&
               map.getVertexesCount() == 0);
  }

  @Test
  public void testRemoveEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, savigliano, 50);
    map.removeEdge(boves, cuneo);
    assertTrue(!map.containsEdge(boves, cuneo) &&
        map.containsEdge(boves, savigliano));
  }

  @Test(expected = GraphException.class)
  public void testRemoveEdge_noEdge() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, savigliano, 50);
    map.removeEdge(cuneo, savigliano);
  }

  @Test
  public void testRemoveEdge_removeAllEdges() {
    map.addVertex(boves);
    map.addVertex(cuneo);
    map.addVertex(savigliano);
    map.addEdge(boves, cuneo, 10);
    map.addEdge(boves, savigliano, 50);
    map.removeEdge(boves, cuneo);
    map.removeEdge(boves, savigliano);
    assertTrue(!map.containsEdge(boves, cuneo) &&
               !map.containsEdge(boves, savigliano) &&
               map.getEdgesCount() == 0);
  }
}
