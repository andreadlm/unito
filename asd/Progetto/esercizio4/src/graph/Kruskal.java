package graph;

import unionfindset.UnionFindSet;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Class implementing the Kruskal algorithm for the minimum spanning tree on a graph
 *
 *  @author Andrea Delmastro
 */
public class Kruskal {
  /**
   * The method implements the Kruskal algorithm for the minimum spanning tree on a graph.
   * @param graph the graph you want to calculate the mst on. The graph must be undirected.
   * @param <V> type of the vertexes.
   * @param <L> type of the labels, must extend Comparable interface.
   * @return a graph containing all the vertexes of the original graph and the edges of the mst.
   *
   * @throws GraphException if the graph passed as argument is directed.
   * @throws NullPointerException if the graph passed as argument is null.
   */
  public static <V, L extends Comparable<L>> Graph<V, L> getMST(Graph<V, L> graph) throws GraphException,
      NullPointerException {
    if(graph == null)
      throw new NullPointerException("graph parameter cannot be null");
    if(graph.isDirected())
      throw new GraphException("graph cannot be directed");

    LinkedList<V> vertexesList = graph.getVertexesList();
    LinkedList<Graph<V, L>.Edge> orderedEdgesList = graph.getEdgesList();
    orderedEdgesList.sort(new Comparator<Graph<V, L>.Edge>() {
      @Override
      public int compare(Graph<V, L>.Edge edge1, Graph<V, L>.Edge edge2) {
        return edge1.getLabel().compareTo(edge2.getLabel());
      }
    });

    Graph<V, L> graphMST = new Graph<>(false);
    UnionFindSet<V> vertexesSets = new UnionFindSet<>();

    for(V vertex : vertexesList)
      vertexesSets.make(vertex);

    for(Graph<V, L>.Edge edge : orderedEdgesList) {
      if(!vertexesSets.find(edge.getFrom()).equals(vertexesSets.find(edge.getTo()))) {
        if(!graphMST.containsVertex(edge.getFrom()))
          graphMST.addVertex(edge.getFrom());
        if(!graphMST.containsVertex(edge.getTo()))
          graphMST.addVertex(edge.getTo());
        graphMST.addEdge(edge.getFrom(), edge.getTo(), edge.getLabel());
        vertexesSets.union(edge.getFrom(), edge.getTo());
      }
    }

    return graphMST;
  }
}
