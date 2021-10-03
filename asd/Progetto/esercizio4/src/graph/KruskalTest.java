package graph;

import org.junit.Test;
import org.junit.Before;

import java.util.Comparator;

import static org.junit.Assert.*;

public class KruskalTest {

  Graph<Integer, Integer> graph;

  @Before
  public void initGraph() {
    graph = new Graph<>(false);
  }

  @Test
  public void testKruskal() {
    graph.addVertex(0);
    graph.addVertex(1);
    graph.addVertex(2);
    graph.addVertex(3);
    graph.addVertex(4);
    graph.addVertex(5);
    graph.addVertex(6);
    graph.addVertex(7);
    graph.addEdge(1, 3, 9);
    graph.addEdge(1, 0, 7);
    graph.addEdge(3, 0, 7);
    graph.addEdge(0, 4, 3);
    graph.addEdge(1, 2, 7);
    graph.addEdge(1, 6, 8);
    graph.addEdge(1, 5, 6);
    graph.addEdge(5, 7, 1);
    graph.addEdge(7, 4, 4);
    graph.addEdge(4, 2, 7);
    graph.addEdge(4, 6, 8);
    graph.addEdge(5, 2, 6);
    graph.addEdge(7, 6, 4);
    Graph<Integer, Integer> graphMST = Kruskal.getMST(graph);
    int sumEdges = 0;
    for(Graph<Integer, Integer>.Edge edge : graphMST.getEdgesList()) {
      sumEdges += edge.getLabel();
    }
    assertTrue(sumEdges/2 == 31 && graphMST.getEdgesCount()/2 == 7);
  }

  @Test
  public void testKruskal_emptyGraph() {
    Graph<Integer, Integer> graphMST = Kruskal.getMST(graph);
    assertTrue(graphMST.getEdgesCount() == 0 && graphMST.getVertexesCount() == 0);
  }

  @Test
  public void testKruskal_singleEdgeGraph() {
    graph.addVertex(0);
    graph.addVertex(1);
    graph.addEdge(0, 1, 1);
    Graph<Integer, Integer> graphMST = Kruskal.getMST(graph);
    assertTrue(graphMST.getEdgesCount()/2 == 1 && graphMST.getVertexesCount() == 2);
  }

  @Test(expected = NullPointerException.class)
  public void testKruskal_nullGraph() {
    Kruskal.getMST(null);
  }

  @Test(expected = GraphException.class)
  public void testKruskal_directedGraph() {
    Kruskal.getMST(new Graph<Integer, Integer>(true));
  }

  @Test
  public void testKruskal_notConnectedGraph() {
    graph.addVertex(0);
    graph.addVertex(1);
    graph.addVertex(2);
    graph.addVertex(3);
    graph.addVertex(4);
    graph.addVertex(5);
    graph.addVertex(6);
    graph.addVertex(7);
    graph.addVertex(8);
    graph.addVertex(9);
    graph.addEdge(1, 3, 9);
    graph.addEdge(1, 0, 7);
    graph.addEdge(3, 0, 7);
    graph.addEdge(0, 4, 3);
    graph.addEdge(1, 2, 7);
    graph.addEdge(1, 6, 8);
    graph.addEdge(1, 5, 6);
    graph.addEdge(5, 7, 1);
    graph.addEdge(7, 4, 4);
    graph.addEdge(4, 2, 7);
    graph.addEdge(4, 6, 8);
    graph.addEdge(5, 2, 6);
    graph.addEdge(7, 6, 4);
    graph.addEdge(8, 9, 1);
    Graph<Integer, Integer> graphMST = Kruskal.getMST(graph);
    int sumEdges = 0;
    for(Graph<Integer, Integer>.Edge edge : graphMST.getEdgesList()) {
      sumEdges += edge.getLabel();
    }
    assertTrue(sumEdges/2 == 32 && graphMST.getEdgesCount()/2 == 8);
  }

}
